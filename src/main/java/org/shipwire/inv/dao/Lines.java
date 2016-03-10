package org.shipwire.inv.dao;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.shipwire.inv.constants.STATUS;

import java.util.Date;

/**
 * Created by Hariharane Ramasamy on 3/6/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lines {

    private String Product;
    private int Quantity;
    private int filledQuantity;
    private STATUS lineStatus;
    private Date createDate;
    private Date updateDate;


    public Lines() {
        filledQuantity = 0;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void addFilledQuantity(int addValue) {
        filledQuantity += addValue;
        if (filledQuantity == Quantity) {
            setLineStatus(STATUS.FILLED);
        } else {
            setLineStatus(STATUS.PARTIAL);
        }
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getFilledQuantity() {
        return filledQuantity;
    }

    public void setFilledQuantity(int filledQuantity) {
        this.filledQuantity = filledQuantity;
    }

    public STATUS getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(STATUS lineStatus) {
        this.lineStatus = lineStatus;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
