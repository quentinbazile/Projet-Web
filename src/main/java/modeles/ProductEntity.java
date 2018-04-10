package modeles;

/**
 * Correspond à un enregistrement de la table PRODUCT
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

    // Constructeur
    public ProductEntity(int product_id, float purchase_cost, int quantity_on_hand, float markup, String available, 
            String description, int manufacturer_id, String product_code) {
        this.product_id = product_id;
        this.purchase_cost = purchase_cost;
        this.quantity_on_hand = quantity_on_hand;
        this.markup = markup;
        this.available = available;
        this.description = description;
        this.manufacturer_id = manufacturer_id;
        this.product_code = product_code;
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

    /**
     * Get the value of quantity_on_hand
     * @return the value of quantity_on_hand
     */
    public int getQuantity_on_hand() {
        return quantity_on_hand;
    }

    /**
     * Get the value of markup
     * @return the value of markup
     */
    public float getMarkup() {
        return markup;
    }
    
    /**
     * Get the value of description
     * @return the value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the value of available
     * @return the value of available
     */
    public String getAvailable() {
        return available;
    }

    /**
     * Get the value of manufacturer_id
     * @return the value of manufacturer_id
     */
    public int getManufacturer_id() {
        return manufacturer_id;
    }

    /**
     * Get the value of product_code
     * @return the value of product_code
     */
    public String getProduct_code() {
        return product_code;
    }  
}