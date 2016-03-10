package org.shipwire.inv.queue;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.shipwire.inv.constants.InventoryConstants;
import org.shipwire.inv.dao.Order;
import org.shipwire.inv.dao.Product;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Hariharane Ramasamy on 3/7/16.
 */
public class OrderBroker {

    public ArrayBlockingQueue<Order> orderQueue;
    public Map<String, Product> productList;
    public AtomicInteger invCount = new AtomicInteger(0);
    public Boolean continueProducing;

    private String productFileName;

    public Boolean getContinueProducing() {
        return continueProducing;
    }

    public void setContinueProducing(Boolean continueProducing) {
        this.continueProducing = continueProducing;
    }

    public ArrayBlockingQueue<Order> getOrderQueue() {
        return orderQueue;
    }

    public void setOrderQueue(ArrayBlockingQueue<Order> orderQueue) {
        this.orderQueue = orderQueue;
    }

    public String getProductFileName() {
        return productFileName;
    }

    public void setProductFileName(String productFileName) {
        this.productFileName = productFileName;
    }

    public Map<String, Product> getProductList() {
        return productList;
    }

    public void setProductList(Map<String, Product> productList) {
        this.productList = productList;
    }

    public OrderBroker() {

        orderQueue = new ArrayBlockingQueue<Order>(100);
        productList = new ConcurrentHashMap<String, Product>();
        continueProducing = Boolean.TRUE;

    }

    public void readProductLists() {

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(getProductFileName()));


            JSONObject jsonObject = (JSONObject) obj;

            ObjectMapper mapper = new ObjectMapper();

            JSONArray products = (JSONArray) jsonObject.get("product");
            Iterator<JSONObject> iterator = products.iterator();
            while (iterator.hasNext()) {
                JSONObject json = (JSONObject) (iterator.next());
                String jsonStr = json.toString();
                Product product = mapper.readValue(jsonStr, Product.class);
                product.setCreatedDate(new Date());
                product.setUpdatedDate(product.getCreatedDate());
                product.setProductID(InventoryConstants.getProductID());
                productList.put(product.getProductName(), product);
                invCount.set(invCount.get() + product.getQuantityAvailable());

                //System.out.println (product.getProductName()) ;
                // System.out.println(product.getQuantityAvailable()) ;

            }
            // System.out.println (productList.size() + " " + invCount.get() ) ;

            System.out.println("########## Initial Product Inventory Begin ################");
            this.printProductJson();
            System.out.println("########## Initial Product Inventory Ends ################");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    void subProductAmount(String product, int amount) {


        //System.out.println ("THe size of productlist is " + productList.size()) ;

        productList.get(product).subProductAvailable(amount);

    }

    public void put(Order order) throws InterruptedException {
        this.orderQueue.add(order);
    }

    public Order get() throws InterruptedException {
        return this.orderQueue.poll(1, TimeUnit.SECONDS);
    }

    public void printProductJson() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        try {
            System.out.println(mapper.writeValueAsString(productList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
