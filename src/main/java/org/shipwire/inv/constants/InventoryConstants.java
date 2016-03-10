package org.shipwire.inv.constants;

/**
 * Created by Hariharane Ramasamy on 3/6/16.
 */


public class InventoryConstants {

    public static final int MAX_ORDER = 5;
    public static final int MIN_ORDER = 1;
    public static long orderID = 0;
    public static long productID = 0;

    public static long getProductID() {

        ++productID;
        return (productID);
    }

    public synchronized static long getOrderID() {

        ++orderID;
        return (orderID);

    }

}
