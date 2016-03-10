package org.shipwire.inv.queue;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import org.shipwire.inv.constants.STATUS;
import org.shipwire.inv.dao.Lines;
import org.shipwire.inv.dao.Order;
import org.shipwire.inv.dao.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Hariharane Ramasamy on 3/7/16.
 */
public class ConsumeOrder implements Runnable {

    private OrderBroker broker;


    public ConsumeOrder(OrderBroker broker) {
        this.broker = broker;
    }


    public void run() {

        System.out.println("########## Orders Status begin ################");

        try {
            Order order = broker.get();
            ObjectMapper mapper = new ObjectMapper();

            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);

            while (broker.continueProducing || order != null) {
                if (order.getOrderStatus() != (STATUS.INVALID)) {

                    ArrayList<Lines> lines = order.getLines();
                    for (int i = 0; i < lines.size(); i++) {
                        if (broker.invCount.get() == 0) {
                            order.setOrderStatus(STATUS.PARTIAL);
                            order.setLineFilled(i, 0);
                            break;
                        } else {
                            int amount = lines.get(i).getQuantity();
                            String productName = lines.get(i).getProduct();
                            Map<String, Product> product;
                            Product prod = broker.getProductList().get(productName);
                            if (prod == null) {
                                order.setOrderStatus(STATUS.REJECTED);
                            } else {
                                if (prod.getQuantityAvailable() >= amount) {
                                    //System.out.println ("The value of i is " + i) ;
                                    broker.subProductAmount(productName, amount);
                                    broker.invCount.set(broker.invCount.get() - amount);
                                    order.subTotalQuantity(amount);
                                    order.setLineFilled(i, amount);


                                    if (order.getTotalLeftToFill() == 0) {
                                        order.setOrderStatus(STATUS.FILLED);
                                        order.setFillDate(new Date());
                                        break;
                                    }
                                } else {
                                    broker.invCount.set(broker.invCount.get() - prod.getQuantityAvailable());
                                    broker.subProductAmount(productName, prod.getQuantityAvailable());
                                    order.subTotalQuantity(prod.getQuantityAvailable());
                                    order.setLineFilled(i, prod.getQuantityAvailable());
                                    order.setOrderStatus(STATUS.PARTIAL);

                                }

                            }

                        }
                        //System.out.println("Consumer " + " processed data from broker: " +
                        //      lines.get(i).getProduct() + " " + lines.get(i).getQuantity());
                    }

                    try {
                        order.printOrderJson();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (broker.invCount.get() == 0) {
                    break;
                }

                order = broker.get();

            }

            System.out.println("########## Orders Status end ################");
            System.out.println("########## Inventory Status after product fill begin ################");
            broker.printProductJson();
            System.out.println("########## Inventory Status after product fill end  ################");


            System.out.println("Comsumer " + " finished its job; terminating.");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
