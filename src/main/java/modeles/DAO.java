package modeles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DAO {

    protected final DataSource myDataSource;

    /**
     * @param dataSource la source de données à utiliser
     */
    public DAO(DataSource dataSource) {
        this.myDataSource = dataSource;
    }

    /**
     * @param login le nom d'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @return la vérification de la connexion de l'utilisateur ('true' ou 'false' si login et/ou password est incorrect) (table : CUSTOMER)
     * @throws DAOException
     */
    public boolean checkLogin(String login, int password) throws DAOException {
        boolean result = false ; // On n'a pas encore de correspondance
        String sql = "SELECT * FROM CUSTOMER "  // Une requête SQL paramétrée
                + "WHERE EMAIL = ? AND CUSTOMER_ID = ?";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement stmt = connection.prepareStatement(sql)) { // On crée un PreparedStatement pour exécuter la requête paramétrée
            stmt.setString(1, login); // Définir la valeur des paramètres
            stmt.setInt(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // On a trouvé une correspondance
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * @return la liste des produits disponibles à l'achat (table : PRODUCT)
     * @throws SQLException
     */
    public List<ProductEntity> listeProduits() throws SQLException {
        List<ProductEntity> result = new LinkedList<>(); // Liste vide
        String sql = "SELECT * FROM PRODUCT "
                + "WHERE QUANTITY_ON_HAND > 0 "
                + "ORDER BY PRODUCT_ID";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                Statement stmt = connection.createStatement()) { // On crée un statement pour exécuter une requête
            ResultSet rs = stmt.executeQuery(sql); // Un ResultSet pour parcourir les enregistrements du résultat
            while (rs.next()) { // Tant qu'il y a des enregistrements
                // On récupère les champs nécessaires de l'enregistrement courant
                int product_id = rs.getInt("PRODUCT_ID");
                float purchase_cost = rs.getFloat("PURCHASE_COST");
                int quantity_on_hand = rs.getInt("QUANTITY_ON_HAND");
                float markup = rs.getFloat("MARKUP");
                String available = rs.getString("AVAILABLE");
                String description = rs.getString("DESCRIPTION");
                int manufacturer_id = rs.getInt("MANUFACTURER_ID");
                String product_code = rs.getString("PRODUCT_CODE");
                // On crée l'objet entité
                ProductEntity p = new ProductEntity(product_id, purchase_cost, quantity_on_hand, markup, available, description, manufacturer_id, product_code);
                // On l'ajoute à la liste des résultats
                result.add(p);
            }
        }
        return result;
    }

    /**
     * @param userName l'email du client
     * @return la liste des commandes pour ce client (tables : PURCHASE_ORDER, CUSTOMER, PRODUCT)
     * @throws SQLException
     */
    public List<PurchaseOrderEntity> listeCommandes(String userName) throws SQLException {
        List<PurchaseOrderEntity> result = new LinkedList<>(); // Liste vIde
        String sql = "SELECT * FROM PURCHASE_ORDER "
                + "INNER JOIN CUSTOMER USING(CUSTOMER_ID) "
                + "INNER JOIN PRODUCT USING(PRODUCT_ID) "
                + "WHERE EMAIL = ? "
                + "ORDER BY SALES_DATE DESC";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement pstmt = connection.prepareStatement(sql)) { // On crée un statement pour exécuter une requête
            pstmt.setString(1, userName);
            try (ResultSet rs = pstmt.executeQuery()) { // Un ResultSet pour parcourir les enregistrements du résultat
                while (rs.next()) { // Tant qu'il y a des enregistrements
                    // On récupère les champs nécessaires de l'enregistrement courant
                    int order_num = rs.getInt("ORDER_NUM");
                    int quantity = rs.getInt("QUANTITY");
                    float shipping_cost = rs.getFloat("SHIPPING_COST");
                    Date sales_date = rs.getDate("SALES_DATE");
                    Date shipping_date = rs.getDate("SHIPPING_DATE");
                    String freight_company = rs.getString("FREIGHT_COMPANY");
                    int customer_id = rs.getInt("CUSTOMER_ID");
                    int product_id = rs.getInt("PRODUCT_ID");
                    float purchase_cost = rs.getFloat("PURCHASE_COST") * quantity + shipping_cost;
                    // On crée l'objet entité
                    PurchaseOrderEntity o = new PurchaseOrderEntity(order_num, quantity, shipping_cost, sales_date, shipping_date, 
                            freight_company, customer_id, product_id, purchase_cost);
                    // On l'ajoute à la liste des résultats
                    result.add(o);
                }
            }
        }
        return result;
    }

    /**
     * @return le prochain numéro de commande à créer (table : PURCHASE_ORDER)
     * @throws DAOException
     */
    public int orderNum() throws DAOException {
        int result = 0;
        String sql = "SELECT ORDER_NUM FROM PURCHASE_ORDER "
                + "WHERE ORDER_NUM = (SELECT MAX(ORDER_NUM) FROM PURCHASE_ORDER)";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                Statement stmt = connection.createStatement(); // On crée un statement pour exécuter une requête
                ResultSet rs = stmt.executeQuery(sql) // Un ResultSet pour parcourir les enregistrements du résultat
                ) {
            if (rs.next()) { // Pas la peine de faire while, il y a 1 seul enregistrement
                // On récupère le champs nécessaire de l'enregistrement courant
                result = rs.getInt("ORDER_NUM");
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }
    
    /**
     * @param order_num le numéro de la commande
     * @param customer_id le numéro de client qui passe la commande
     * @param product_id la référence produit
     * @param quantity la quantité à commander
     * @param shipping_cost les frais de livraison
     * @param sales_date la date de la commande
     * @param shipping_date la date d'expédition
     * @param freight_company la compagnie de transport
     * @return le nombre d'enregistrements insérés (1 ou 0 si échec) (table : PURCHASE_ORDER)
     * @throws DAOException
     */
    public int ajoutCommande(int order_num, int customer_id, int product_id, int quantity, float shipping_cost, Date sales_date, Date shipping_date, String freight_company) 
            throws DAOException {
        int result = 0;
        String sql = "INSERT INTO PURCHASE_ORDER(order_num, customer_id, product_id, quantity, shipping_cost, sales_date, shipping_date, freight_company) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, order_num);
            pstmt.setInt(2, customer_id);
            pstmt.setInt(3, product_id);
            pstmt.setInt(4, quantity);
            pstmt.setFloat(5, shipping_cost);
            pstmt.setDate(6, sales_date);
            pstmt.setDate(7, shipping_date);
            pstmt.setString(8, freight_company);
            result = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * @param order_num la clé de la commande à détruire
     * @return le nombre d'enregistrements détruits (1 ou 0 si non trouvé) (table : PURCHASE_ORDER)
     * @throws SQLException
     */
    public int deleteCommande(int order_num) throws SQLException {
        int result = 0;
        String sql = "DELETE FROM PURCHASE_ORDER "
                + "WHERE ORDER_NUM = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, order_num);
            result = stmt.executeUpdate();
        }
        return result;
    }

    /**
     * @param quantity la nouvelle quantité
     * @param freight_company la nouvelle compagnie de transport
     * @param shipping_cost les nouveaux frais de port
     * @param order_num la clé de la commande à modifier
     * @return l'enregistrement modifié (1 ou 0 si non trouvé) (table : PURCHASE_ORDER)
     * @throws DAOException
     */
    public int updateCommande(int quantity, String freight_company, float shipping_cost, int order_num) throws DAOException {
        int result = 0;
        String sql = "UPDATE PURCHASE_ORDER "
                + "SET quantity = ?, freight_company = ?, shipping_cost = ? "
                + "WHERE order_num = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, freight_company);
            pstmt.setFloat(3, shipping_cost);
            pstmt.setInt(4, order_num);
            result = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * @param quantity la nouvelle quantité choisie
     * @param product_id la clé du produit à modifier
     * @return l'enregistrement modifié (1 ou 0 si non trouvé) (table : PRODUCT)
     * @throws DAOException
     */
    public int updateQuantity(int quantity, int product_id) throws DAOException {
        int result = 0;
        String sql = "UPDATE PRODUCT "
                + "SET quantity_on_hand = quantity_on_hand - ? "
                + "WHERE product_id = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, product_id);
            result = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * @param debut la date de départ de la période des ventes par client
     * @param fin la date de fin de la période des ventes par client
     * @return la liste des chiffres d'affaire des ventes par client durant la période choisie (tables : CUSTOMER, PURCHASE_ORDER, PRODUCT)
     * @throws SQLException
     */
    public Map<String, Double> salesByCustomer(Date debut, Date fin) throws SQLException {
        Map<String, Double> result = new HashMap<>();
        String sql = "SELECT NAME, SUM(PURCHASE_COST * QUANTITY) AS SALES "
                + "FROM CUSTOMER "
                + "INNER JOIN PURCHASE_ORDER USING(CUSTOMER_ID) "
                + "INNER JOIN PRODUCT USING(PRODUCT_ID) "
                + "WHERE SALES_DATE BETWEEN ? AND ? "
                + "GROUP BY NAME";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, debut);
            pstmt.setDate(2, fin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // On récupère les champs nécessaires de l'enregistrement courant
                    String name = rs.getString("NAME");
                    double sales = rs.getDouble("SALES");
                    // On l'ajoute à la liste des résultats
                    result.put(name, sales);
                }
            }
        }
        return result;
    }

    /**
     * @param debut la date de départ de la période des ventes par zone géographique
     * @param fin la date de fin de la période des ventes par zone géographique
     * @return la liste des chiffres d'affaire des ventes par zone géographique durant la période choisie (tables : CUSTOMER, PURCHASE_ORDER, PRODUCT)
     * @throws SQLException
     */
    public Map<String, Double> salesByZone(Date debut, Date fin) throws SQLException {
        Map<String, Double> result = new HashMap<>();
        String sql = "SELECT STATE, SUM(PURCHASE_COST * QUANTITY) AS SALES "
                + "FROM CUSTOMER "
                + "INNER JOIN PURCHASE_ORDER USING(CUSTOMER_ID) "
                + "INNER JOIN PRODUCT USING(PRODUCT_ID) "
                + "WHERE SALES_DATE BETWEEN ? AND ? "
                + "GROUP BY STATE";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, debut);
            pstmt.setDate(2, fin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // On récupère les champs nécessaires de l'enregistrement courant
                    String name = rs.getString("STATE");
                    double sales = rs.getDouble("SALES");
                    // On l'ajoute à la liste des résultats
                    result.put(name, sales);
                }
            }
        }
        return result;
    }

    /**
     * @param debut la date de départ de la période des ventes par catégorie d'articles
     * @param fin la date de fin de la période des ventes par catégorie d'articles
     * @return la liste des chiffres d'affaire des ventes par catégorie d'articles durant la période choisie (tables : PRODUCT_CODE, PURCHASE_ORDER, PRODUCT)
     * @throws SQLException
     */
    public Map<String, Double> salesByProduct(Date debut, Date fin) throws SQLException {
        Map<String, Double> result = new HashMap<>();
        String sql = "SELECT PRODUCT_CODE.DESCRIPTION, SUM(PURCHASE_COST * QUANTITY) AS SALES "
                + "FROM PURCHASE_ORDER "
                + "INNER JOIN PRODUCT USING(PRODUCT_ID) "
                + "INNER JOIN PRODUCT_CODE ON PRODUCT_CODE.PROD_CODE = PRODUCT.PRODUCT_CODE "
                + "WHERE SALES_DATE BETWEEN ? AND ? "
                + "GROUP BY PRODUCT_CODE.DESCRIPTION";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, debut);
            pstmt.setDate(2, fin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // On récupère les champs nécessaires de l'enregistrement courant
                    String name = rs.getString("DESCRIPTION");
                    double sales = rs.getDouble("SALES");
                    // On l'ajoute à la liste des résultats
                    result.put(name, sales);
                }
            }
        }
        return result;
    }

    /**
     * @param userName l'email du client
     * @param dateNow la date du jour
     * @return la liste des commandes déjà envoyées pour ce client (tables : CUSTOMER, PURCHASE_ORDER, PRODUCT)
     * @throws SQLException
     */
    public List listeCommandesEnvoyees(String userName, Date dateNow) throws SQLException {
        List result = new LinkedList<>(); // Liste vide
        String sql = "SELECT * FROM PURCHASE_ORDER "
                + "INNER JOIN CUSTOMER USING(CUSTOMER_ID) "
                + "INNER JOIN PRODUCT USING(PRODUCT_ID) "
                + "WHERE EMAIL = ? AND SALES_DATE < ? "
                + "ORDER BY SALES_DATE DESC";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement pstmt = connection.prepareStatement(sql)) { // On crée un statement pour exécuter une requête
            pstmt.setString(1, userName);
            pstmt.setDate(2, dateNow);
            try (ResultSet rs = pstmt.executeQuery()) { // Un ResultSet pour parcourir les enregistrements du résultat
                while (rs.next()) { 
                    // On récupère les champs nécessaires de l'enregistrement courant
                    int order_num = rs.getInt("ORDER_NUM");
                    // On l'ajoute à la liste des résultats
                    result.add(order_num);
                }
            }
        }
        return result;
    }
}
