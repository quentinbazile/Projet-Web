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
     *
     * @return le nombre d'enregistrements dans la table CUSTOMER
     * @throws DAOException
     */
    public int numberOfCustomers() throws DAOException {
        int result = 0;
        String sql = "SELECT COUNT(*) AS NUMBER FROM CUSTOMER";
        // Syntaxe "try with resources" 
        // cf. https://stackoverflow.com/questions/22671697/try-try-with-resources-and-connection-statement-and-resultset-closing
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                Statement stmt = connection.createStatement(); // On crée un statement pour exécuter une requête
                ResultSet rs = stmt.executeQuery(sql) // Un ResultSet pour parcourir les enregistrements du résultat
                ) {
            if (rs.next()) { // Pas la peine de faire while, il y a 1 seul enregistrement
                // On récupère le champ NUMBER de l'enregistrement courant
                result = rs.getInt("NUMBER");
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * Detruire un enregistrement dans la table CUSTOMER
     *
     * @param customerId la clé du client à détruire
     * @return le nombre d'enregistrements détruits (1 ou 0 si pas trouvé)
     * @throws DAOException
     */
    public int deleteCustomer(int customerId) throws DAOException {
        // Une requête SQL paramétrée
        String sql = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Définir la valeur du paramètre
            stmt.setInt(1, customerId);
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
    }

    /**
     * @param customerId la clé du client à recherche
     * @return le nombre de bons de commande pour ce client (table
     * PURCHASE_ORDER)
     * @throws DAOException
     */
    public int numberOfOrdersForCustomer(int customerId) throws DAOException {
        int result = 0;
        // Une requête SQL paramétrée
        String sql = "SELECT COUNT(*) AS NUMBER FROM PURCHASE_ORDER WHERE CUSTOMER_ID = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Définir la valeur du paramètre
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next(); // On a toujours exactement 1 enregistrement dans le résultat
                result = rs.getInt("NUMBER");
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * Trouver un Customer à partir de sa clé
     *
     * @param customerID la clé du CUSTOMER à rechercher
     * @return l'enregistrement correspondant dans la table CUSTOMER, ou null si
     * pas trouvé
     * @throws DAOException
     */
    public CustomerEntity findCustomer(int customerID) throws DAOException {
        CustomerEntity result = null;

        String sql = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ?";
        try (Connection connection = myDataSource.getConnection(); // On crée un statement pour exécuter une requête
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // On a trouvé
                    String name = rs.getString("NAME");
                    String address = rs.getString("ADDRESSLINE1");
                    // On crée l'objet "entity"
                    result = new CustomerEntity(customerID, name, address);
                } // else on n'a pas trouvé, on renverra null
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * Liste des clients localisés dans un état des USA
     *
     * @param state l'état à rechercher (2 caractères)
     * @return la liste des clients habitant dans cet état
     * @throws DAOException
     */
    public List<CustomerEntity> customersInState(String state) throws DAOException {
        List<CustomerEntity> result = new LinkedList<>(); // Liste vIde
        String sql = "SELECT * FROM CUSTOMER WHERE STATE = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, state);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) { // Tant qu'il y a des enregistrements
                    // On récupère les champs nécessaires de l'enregistrement courant
                    int id = rs.getInt("CUSTOMER_ID");
                    String name = rs.getString("NAME");
                    String address = rs.getString("ADDRESSLINE1");
                    // On crée l'objet entité
                    CustomerEntity c = new CustomerEntity(id, name, address);
                    // On l'ajoute à la liste des résultats
                    result.add(c);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    public boolean checkLogin(String login, int password) throws DAOException {
        boolean result = false;
        String sql = "SELECT * FROM CUSTOMER WHERE EMAIL = ? AND CUSTOMER_ID = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setInt(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    public List<ProductEntity> listeProduits() throws SQLException {
        List<ProductEntity> result = new LinkedList<>(); // Liste vIde
        String sql = "SELECT * FROM PRODUCT WHERE QUANTITY_ON_HAND > 0";
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
    
    public List<PurchaseOrderEntity> listeCommandes(String userName) throws SQLException {
        List<PurchaseOrderEntity> result = new LinkedList<>(); // Liste vIde
        String sql = "SELECT * FROM PURCHASE_ORDER INNER JOIN CUSTOMER USING(CUSTOMER_ID) INNER JOIN PRODUCT USING(PRODUCT_ID) WHERE EMAIL = ?";
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
                    PurchaseOrderEntity o = new PurchaseOrderEntity(order_num, quantity, shipping_cost, sales_date, shipping_date, freight_company, customer_id, product_id, purchase_cost);
                    // On l'ajoute à la liste des résultats
                    result.add(o);
                }
            }
        } 
        return result;
    }

    public int orderNum() throws DAOException {
        int result = 0;
        String sql = "SELECT ORDER_NUM FROM PURCHASE_ORDER WHERE ORDER_NUM = (SELECT MAX(ORDER_NUM) FROM PURCHASE_ORDER)";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                Statement stmt = connection.createStatement(); // On crée un statement pour exécuter une requête
                ResultSet rs = stmt.executeQuery(sql) // Un ResultSet pour parcourir les enregistrements du résultat
                ) {
            if (rs.next()) { // Pas la peine de faire while, il y a 1 seul enregistrement
                // On récupère le champ NUMBER de l'enregistrement courant
                result = rs.getInt("ORDER_NUM");
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    public int ajoutCommande(int order_num, int customer_id, int product_id, int quantity, float shipping_cost, Date sales_date, Date shipping_date, String freight_company) throws DAOException {
        int result = 0;
        String sql = "INSERT INTO PURCHASE_ORDER(order_num, customer_id, product_id, quantity, shipping_cost, sales_date, shipping_date, freight_company) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
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

    public int deleteCommande(int order_num) throws SQLException {
        int result = 0;
        String sql = "DELETE FROM PURCHASE_ORDER WHERE ORDER_NUM = ?";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, order_num);
            result = stmt.executeUpdate();
        }
        return result;
    }

    public int updateCommande(int quantity, String freight_company, float shipping_cost, int order_num) throws DAOException {
        int result = 0;
        String sql = "UPDATE PURCHASE_ORDER SET quantity = ?, freight_company = ?, shipping_cost = ? WHERE order_num = ?";
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
    
    public int updateQuantity(int quantity, int product_id) throws DAOException {
        int result = 0;
        String sql = "UPDATE PRODUCT SET quantity_on_hand = quantity_on_hand - ? WHERE product_id = ?";
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
	 * Contenu de la table DISCOUNT_CODE
	 * @return Liste des discount codes
	 * @throws SQLException renvoyées par JDBC
	 */
	public List<DiscountCode> allCodes() throws SQLException {

		List<DiscountCode> result = new LinkedList<>();

		String sql = "SELECT * FROM DISCOUNT_CODE ORDER BY DISCOUNT_CODE";
		try (Connection connection = myDataSource.getConnection(); 
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("DISCOUNT_CODE");
				float rate = rs.getFloat("RATE");
				DiscountCode c = new DiscountCode(id, rate);
				result.add(c);
			}
		}
		return result;
	}

	/**
	 * Ajout d'un enregistrement dans la table DISCOUNT_CODE
	 * @param code le code (non null)
	 * @param rate le taux (positive or 0)
	 * @return 1 si succès, 0 sinon
	 * @throws SQLException renvoyées par JDBC
	 */
	public int addDiscountCode(String code, float rate) throws SQLException {
		int result = 0;
		String sql = "INSERT INTO DISCOUNT_CODE VALUES (?, ?)";
		try (Connection connection = myDataSource.getConnection(); 
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, code);
			stmt.setFloat(2, rate);
			result = stmt.executeUpdate();
		}
		return result;
	}

		
	/**
	 * Supprime un enregistrement dans la table DISCOUNT_CODE
	 * @param code la clé de l'enregistrement à supprimer
	 * @return le nombre d'enregistrements supprimés (1 ou 0)
	 * @throws java.sql.SQLException renvoyées par JDBC
	 **/
	public int deleteDiscountCode(String code) throws SQLException {
		int result = 0;
		String sql = "DELETE FROM DISCOUNT_CODE WHERE DISCOUNT_CODE = ?";
		try (Connection connection = myDataSource.getConnection(); 
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, code);
			result = stmt.executeUpdate();
		}
		return result;
	}
        
        
        
        
        
        
	/**
	 * Liste des clients localisés dans un état des USA
	 *
	 * @param state l'état à rechercher (2 caractères)
	 * @return la liste des clients habitant dans cet état
	 * @throws SQLException
	 */
	/*public List<CustomerEntity> customers(String id, ) throws SQLException {
		List<CustomerEntity> result = new LinkedList<>();
		// Une requête SQL paramétrée
		String sql = "SELECT * FROM CUSTOMER WHERE STATE = ?";
		try (Connection connection  = myDataSource.getConnection(); 
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, state);			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					// On récupère les champs nécessaires de l'enregistrement courant
					int id = rs.getInt("CUSTOMER_ID");
					String name = rs.getString("NAME");
					String address = rs.getString("ADDRESSLINE1");
					// On crée l'objet entité
					CustomerEntity c = new CustomerEntity(id, name, address);
					// On l'ajoute à la liste des résultats
					result.add(c);
				}
			}
		}		
		return result;
	}

	/**
	 * Liste des états des USA présents dans la table CUSTOMER
	 *
	 * @return la liste des états
	 * @throws SQLException
	 */
	public List<String> existingStates() throws SQLException {
		List<String> result = new LinkedList<>();
		String sql = "SELECT DISTINCT STATE FROM CUSTOMER ORDER BY STATE";
		try ( Connection connection = myDataSource.getConnection(); 
		      Statement stmt = connection.createStatement(); 
		      ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				// On récupère les champs nécessaires de l'enregistrement courant
				String state = rs.getString("STATE");
				// On l'ajoute à la liste des résultats
				result.add(state);
			}
		}
		return result;
	}

	/**
	 * ventes par client
	 *
	 * @return Une Map associant le nom du client à son chiffre d'affaires
	 * @throws SQLException
	 */
	public Map<String, Double> salesByCustomer() throws SQLException {
		Map<String, Double> result = new HashMap<>();
		String sql = "SELECT NAME, SUM(PURCHASE_COST * QUANTITY) AS SALES "
                        + "FROM CUSTOMER "
                        + "INNER JOIN PURCHASE_ORDER USING(CUSTOMER_ID) "
                        + "INNER JOIN PRODUCT USING(PRODUCT_ID) "
                        + "GROUP BY NAME";
		try (Connection connection = myDataSource.getConnection(); 
		     Statement stmt = connection.createStatement(); 
		     ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				// On récupère les champs nécessaires de l'enregistrement courant
				String name = rs.getString("NAME");
				double sales = rs.getDouble("SALES");
				// On l'ajoute à la liste des résultats
				result.put(name, sales);
			}
		}
		return result;
	}
        
        public Map<String, Double> salesByZone() throws SQLException {
		Map<String, Double> result = new HashMap<>();
		String sql = "SELECT CITY, SUM(PURCHASE_COST * QUANTITY) AS SALES" +
		"	      FROM CUSTOMER c" +
		"	      INNER JOIN PURCHASE_ORDER o ON (c.CUSTOMER_ID = o.CUSTOMER_ID)" +
		"	      INNER JOIN PRODUCT p ON (o.PRODUCT_ID = p.PRODUCT_ID)" +
		"	      GROUP BY CITY";
		try (Connection connection = myDataSource.getConnection(); 
		     Statement stmt = connection.createStatement(); 
		     ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				// On récupère les champs nécessaires de l'enregistrement courant
				String city = rs.getString("CITY");
				double sales = rs.getDouble("SALES");
				// On l'ajoute à la liste des résultats
				result.put(city, sales);
			}
		}
		return result;
	}
        
         public Map<String, Double> salesByProduct() throws SQLException {
		Map<String, Double> result = new HashMap<>();
		String sql = "SELECT CITY, SUM(PURCHASE_COST * QUANTITY) AS SALES" +
		"	      FROM CUSTOMER c" +
		"	      INNER JOIN PURCHASE_ORDER o ON (c.CUSTOMER_ID = o.CUSTOMER_ID)" +
		"	      INNER JOIN PRODUCT p ON (o.PRODUCT_ID = p.PRODUCT_ID)" +
		"	      GROUP BY CITY";
		try (Connection connection = myDataSource.getConnection(); 
		     Statement stmt = connection.createStatement(); 
		     ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				// On récupère les champs nécessaires de l'enregistrement courant
				String city = rs.getString("CITY");
				double sales = rs.getDouble("SALES");
				// On l'ajoute à la liste des résultats
				result.put(city, sales);
			}
		}
		return result;
	}
	
}

    
    


