package org.shipwire.inv.dao;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.shipwire.inv.constants.STATUS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Hariharane Ramasamy on 3/6/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {


    private long orderID;
    private int Header;
    private ArrayList<Lines> lines;
    private STATUS orderStatus;
    private int totalLeftToFill;
    private Date recvDate;
    private Date fillDate;

    public Order() {
        lines = new ArrayList<Lines>();
        totalLeftToFill = 0;
    }

    public Date getFillDate() {
        return fillDate;
    }

    public void setFillDate(Date fillDate) {
        this.fillDate = fillDate;
    }

    public Date getRecvDate() {
        return recvDate;
    }

    public void setRecvDate(Date recvDate) {
        this.recvDate = recvDate;
    }

    public ArrayList<Lines> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Lines> lines) {
        this.lines = lines;
    }

    public int getHeader() {
        return Header;
    }

    public void setHeader(int header) {
        Header = header;
    }

    public STATUS getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(STATUS orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTotalLeftToFill() {
        return totalLeftToFill;
    }

    public void setTotalLeftToFill(int totalQunatity) {
        this.totalLeftToFill = totalQunatity;
    }

    public void addTotalQuantity(int addValue) {

        this.totalLeftToFill += addValue;
    }

    public void subTotalQuantity(int subValue) {

        this.totalLeftToFill -= subValue;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public void addItem(Lines item) {
        lines.add(item);
    }

    public void setLineFilled(int i, int quantity) {
        lines.get(i).addFilledQuantity(quantity);
        if (lines.get(i).getFilledQuantity() == lines.get(i).getQuantity()) {
            lines.get(i).setLineStatus(STATUS.FILLED);

        } else {
            if (quantity != 0) {
                lines.get(i).setLineStatus(STATUS.PARTIAL);
            } else {
                lines.get(i).setLineStatus(STATUS.OUT_OF_STOCK);
            }
        }
        lines.get(i).setUpdateDate(new Date());
    }

    public void printOrders() {

        for (int i = 0; i < lines.size(); i++) {
            System.out.println(lines.get(i).getProduct() + " : " + lines.get(i).getQuantity());
        }
    }

    public void printOrderJson() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);

        try {

            System.out.println(mapper.writeValueAsString(this));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
