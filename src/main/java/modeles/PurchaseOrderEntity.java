/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeles;

import java.sql.Date;

/**
 *
 * @author Quentin
 */
public class PurchaseOrderEntity {
    
    private int order_num;
    private int quantity;
    private float shipping_cost;
    private Date sales_date;
    private Date shipping_date;
    private String freight_company;
    private int customer_id;
    private int product_id;
    private float purchase_cost;

    public PurchaseOrderEntity(int order_num, int quantity, float shipping_cost, Date sales_date, Date shipping_date, String freight_company, int customer_id, int product_id, float purchase_cost) {
        this.order_num = order_num;
        this.quantity = quantity;
        this.shipping_cost = shipping_cost;
        this.sales_date = sales_date;
        this.shipping_date = shipping_date;
        this.freight_company = freight_company;
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.purchase_cost = purchase_cost;
    }

    public int getOrder_num() {
        return order_num;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getShipping_cost() {
        return shipping_cost;
    }

    public Date getSales_date() {
        return sales_date;
    }

    public Date getShipping_date() {
        return shipping_date;
    }

    public String getFreight_company() {
        return freight_company;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public int getProduct_id() {
        return product_id;
    }
    public float getPurchase_cost() {
        return purchase_cost;
    }
}

 
