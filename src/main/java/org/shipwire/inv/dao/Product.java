package org.shipwire.inv.dao;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.shipwire.inv.constants.InventoryConstants;

import java.util.Date;

/**
 * Created by Hariharane Ramasamy on 3/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    long productID;
    String productName;
    int quantityAvailable;
    Date createdDate;
    Date updatedDate;

    public Product() {

    }

    public Product(String pName, int available) {

        this.productName = pName;
        this.quantityAvailable = available;
        this.productID = InventoryConstants.getProductID();
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }


    public long getProductID() {
        return productID;
    }

    public void setProductID(long productId) {
        this.productID = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void subProductAvailable(int subValue) {
        quantityAvailable -= subValue;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
