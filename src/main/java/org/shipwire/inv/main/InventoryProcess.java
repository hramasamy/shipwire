package org.shipwire.inv.main;

import org.shipwire.inv.queue.ConsumeOrder;
import org.shipwire.inv.queue.OrderBroker;
import org.shipwire.inv.queue.ProduceOrder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by Hariharane Ramasamy on 3/6/16.
 */

public class InventoryProcess {


    public static void main(String args[]) {

        if (args.length < 2) {

            System.out.println("Usage java inventory <prodfilename> <orderfilename>");
            System.exit(0);
        }


        try {

            OrderBroker orderBroker = new OrderBroker();
            orderBroker.setProductFileName(args[0]);

            orderBroker.readProductLists();
            ProduceOrder produceOrder = new ProduceOrder(orderBroker, args[1]);
            produceOrder.readOrders(); //change this readOrders

            int noofThreads = 2;
            if (args.length > 2) {
                if (args[2] != null) {
                    noofThreads = Integer.parseInt(args[2]);
                }
            }

            ExecutorService threadPool = Executors.newFixedThreadPool(noofThreads);

            Future consumerStatus = threadPool.submit(new ConsumeOrder(orderBroker));

            // this will wait for the producer to finish its execution.
            consumerStatus.get();


            threadPool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
