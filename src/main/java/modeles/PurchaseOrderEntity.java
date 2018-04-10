package modeles;

import java.sql.Date;

/**
 * Correspond à un enregistrement de la table PURCHASE_ORDER (auquel on a ajouté le prix ici)
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

    // Constructeur
    public PurchaseOrderEntity(int order_num, int quantity, float shipping_cost, Date sales_date, Date shipping_date, 
            String freight_company, int customer_id, int product_id, float purchase_cost) {
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

    /**
     * Get the value of order_num
     * @return the value of order_num
     */
    public int getOrder_num() {
        return order_num;
    }

    /**
     * Get the value of quantity
     * @return the value of quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get the value of shipping_cost
     * @return the value of shipping_cost
     */
    public float getShipping_cost() {
        return shipping_cost;
    }

    /**
     * Get the value of sales_date
     * @return the value of sales_date
     */
    public Date getSales_date() {
        return sales_date;
    }

    /**
     * Get the value of shipping_date
     * @return the value of shipping_date
     */
    public Date getShipping_date() {
        return shipping_date;
    }

    /**
     * Get the value of freight_company
     * @return the value of freight_company
     */
    public String getFreight_company() {
        return freight_company;
    }

    /**
     * Get the value of customer_id
     * @return the value of customer_id
     */
    public int getCustomer_id() {
        return customer_id;
    }

    /**
     * Get the value of product_id
     * @return the value of product_id
     */
    public int getProduct_id() {
        return product_id;
    }
    /**
     * Get the value of purchase_cost
     * @return the value of purchase_cost
     */
    public float getPurchase_cost() {
        return purchase_cost;
    }
}