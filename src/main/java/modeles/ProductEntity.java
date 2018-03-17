/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeles;

/**
 *
 * @author lgrange
 */
public class ProductEntity {
    private int product_id;
    private float purchase_cost;
    private int quantity_on_hand;
    private float markup;
    private String available;
    private String description;
    private int manufacturer_id;
    private String product_code;

    public ProductEntity(int product_id, float purchase_cost, int quantity_on_hand, float markup, String available, String description, int manufacturer_id, String product_code) {
        this.product_id = product_id;
        this.purchase_cost = purchase_cost;
        this.quantity_on_hand = quantity_on_hand;
        this.markup = markup;
        this.available = available;
        this.description = description;
        this.manufacturer_id = manufacturer_id;
        this.product_code = product_code;
    }

    public int getProduct_id() {
        return product_id;
    }

    public float getPurchase_cost() {
        return purchase_cost;
    }

    public int getQuantity_on_hand() {
        return quantity_on_hand;
    }

    public float getMarkup() {
        return markup;
    }
    
    public String getDescription() {
        return description;
    }

    public String getAvailable() {
        return available;
    }

    public int getManufacturer_id() {
        return manufacturer_id;
    }

    public String getProduct_code() {
        return product_code;
    }  
}
