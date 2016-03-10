package org.shipwire.inv.queue;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.shipwire.inv.constants.InventoryConstants;
import org.shipwire.inv.constants.STATUS;
import org.shipwire.inv.dao.Lines;
import org.shipwire.inv.dao.Order;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Hariharane Ramasamy on 3/7/16.
 */
public class ProduceOrder {

    private OrderBroker broker;
    private String orderFileName;


    public String getOrderFileName() {
        return orderFileName;
    }

    public void setOrderFileName(String orderFileName) {
        this.orderFileName = orderFileName;
    }

    public ProduceOrder(OrderBroker broker, String ordFileName) {
        this.broker = broker;
        this.setOrderFileName(ordFileName);

    }

    public OrderBroker getBroker() {
        return broker;
    }

    public void setBroker(OrderBroker broker) {
        this.broker = broker;
    }


    public void readOrders() {


        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(getOrderFileName()));

            System.out.println("########## Current Orders Begin ################");
            JSONArray jsonObject = (JSONArray) obj;

            ObjectMapper mapper = new ObjectMapper();

            Iterator<JSONObject> orderiterator = jsonObject.iterator();
            while (orderiterator.hasNext()) {
                Order order = new Order();
                order.setOrderID(InventoryConstants.getOrderID());
                boolean inValidOrder = false;

                JSONObject json = (JSONObject) (orderiterator.next());
                String jsonStr = json.toString();
                long value = (Long) json.get("Header");
                order.setHeader((int) value);
                JSONArray items = (JSONArray) json.get("Lines");
                Iterator<JSONObject> itemiter = items.iterator();
                while (itemiter.hasNext()) {
                    JSONObject itemObj = itemiter.next();
                    // System.out.println(prodObj.toString()) ;
                    Lines lines = new Lines();
                    int quantity = Integer.parseInt((String) itemObj.get("Quantity"));
                    if (quantity < InventoryConstants.MIN_ORDER ||
                            quantity > InventoryConstants.MAX_ORDER) {
                        //System.out.println(" The value of Quantity and product is " + quantity + " " + (String) itemObj.get("Product"));
                        lines.setLineStatus(STATUS.REJECTED);
                        inValidOrder = true;
                    }

                    lines.setCreateDate(new Date());
                    lines.setProduct((String) itemObj.get("Product"));
                    if (broker.getProductList().containsKey(lines.getProduct()) == false) {
                        lines.setLineStatus(STATUS.NOT_AVAILABLE);
                        inValidOrder = true;

                    }
                    lines.setQuantity(Integer.parseInt((String) itemObj.get("Quantity")));
                    //mapper.readValue(prodObj.toString(), Lines.class);
                    order.addItem(lines);
                    order.addTotalQuantity(lines.getQuantity());
                    //System.out.println (lines.getProduct() + " " + lines.getQuantity()) ;

                }

                order.setRecvDate(new Date());
                if (inValidOrder) {
                    order.setOrderStatus(STATUS.INVALID);
                }


                //System.out.println(json.get("Header")) ;
                //System.out.println(jsonStr) ;
                order.printOrderJson();
                try {
                    broker.put(order);
                } catch (InterruptedException e) {
                    //System.out.println() ;
                }

            }
            System.out.println("########## Current Orders End  ################");
            // System.out.println (productList.size() ) ;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

        }

        this.broker.continueProducing = Boolean.FALSE;
        System.out.println("Producer finished its job; terminating.");


    }

}
