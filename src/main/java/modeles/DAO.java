package modeles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DAO {

	protected final DataSource myDataSource;

	/**
	 *
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
		try (   Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
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
	 * @param customerId la clé du client à détruire
	 * @return le nombre d'enregistrements détruits (1 ou 0 si pas trouvé)
	 * @throws DAOException
	 */
	public int deleteCustomer(int customerId) throws DAOException {

		// Une requête SQL paramétrée
		String sql = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?";
		try (   Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)
                ) {
                        // Définir la valeur du paramètre
			stmt.setInt(1, customerId);
			
			return stmt.executeUpdate();

		}  catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}
	}
	
	/**
	 *
	 * @param customerId la clé du client à recherche
	 * @return le nombre de bons de commande pour ce client (table PURCHASE_ORDER)
	 * @throws DAOException
	 */
	public int numberOfOrdersForCustomer(int customerId) throws DAOException {
		int result = 0;

		// Une requête SQL paramétrée
		String sql = "SELECT COUNT(*) AS NUMBER FROM PURCHASE_ORDER WHERE CUSTOMER_ID = ?";
		try (   Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)
                ) {
                        // Définir la valeur du paramètre
			stmt.setInt(1, customerId);

			try (ResultSet rs = stmt.executeQuery()) {
				rs.next(); // On a toujours exactement 1 enregistrement dans le résultat
				result = rs.getInt("NUMBER");
			}
		}  catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}
		return result;
	}

	/**
	 * Trouver un Customer à partir de sa clé
	 *
	 * @param customerID la clé du CUSTOMER à rechercher
	 * @return l'enregistrement correspondant dans la table CUSTOMER, ou null si pas trouvé
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
		}  catch (SQLException ex) {
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
		}  catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}

		return result;

	}
    
     public boolean checkLogin(String login, String password) throws DAOException {
         
                boolean result = false;
                
		String sql = "SELECT * FROM CUSTOMER WHERE EMAIL = ? AND CUSTOMER_ID = ?";
                
		try (Connection connection = myDataSource.getConnection();
                    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    
                    stmt.setString(1, login);
                    stmt.setString(2, password);
		
                    try (ResultSet rs = stmt.executeQuery()) {
				rs.next(); // On a toujours exactement 1 enregistrement dans le résultat
				result = true;
                    }
		}  catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}

		return result;
	}
    
     public void ajoutCommande (String order_num, String customer_id, String product_id, String quantity, String shipping_cost, String sales_date, String shipping_date, String freight_company) throws DAOException {
        String sql = "INSERT INTO purchase_order(order_num, customer_id, product_id, quantity, shipping_cost, sales_date, shipping_date, freight_company) VALUES(?, ?, ?, ?, ?, ?, ?)"; 
        try (Connection connection = myDataSource.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery(sql);
        	   pstmt.setString(1, order_num); 
        	   pstmt.setString(2, customer_id);
                   pstmt.setString(3, product_id);
                   pstmt.setString(4, quantity);
                   pstmt.setString(5, shipping_cost);
                   pstmt.setString(6, sales_date);
                   pstmt.setString(7, shipping_date);
                   pstmt.setString(8, freight_company);
                   
           int count = pstmt.executeUpdate();
           		 
           if(count == 0)
        		throw new SQLException ("ERREUR: count = 0."); 
           pstmt.close();
        }
        
        
        catch (SQLException e) { 
           System.err.println("Erreur lors de l'exécution de la requète SQL.");
        }

    }
                 
     public int deleteCommande (String order_num) throws DAOException {
         
        String sql = "DELETE FROM PURCHASE_ORDER WHERE ORDER_NUM= ?";
		try (   Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)
                ) {
                        // Définir la valeur du paramètre
			stmt.setString(1, order_num);
			
			return stmt.executeUpdate();

		}  catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}
	}
     
     public void updateCommande (String order_num, String customer_id, String product_id, String quantity, String shipping_cost, String sales_date, String shipping_date, String freight_company) throws DAOException {
         
        String sql = "UPDATE PURCHASE_ORDER SET order_num= ?, customer_id=?, product_id = ?, quantity = ?, shipping_cost= ?, sales_date= ?, shipping_date = ?, freight_company= ? ";
         
        try ( Connection connection = myDataSource.getConnection();
                PreparedStatement pstmt = connection.prepareStatement (sql) ) {
            
            int count;
            for ( int i =0; i<10; i++) {
               pstmt.setString(1, order_num); 
               pstmt.setString(2, customer_id);
               pstmt.setString(3, product_id);
               pstmt.setString(4, quantity);
               pstmt.setString(5, shipping_cost);
               pstmt.setString(6, sales_date);
               pstmt.setString(7, shipping_date);
               pstmt.setString(8, freight_company); 
               count = pstmt.executeUpdate ();
            }
        }  catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
            
     }
     } 
}

     


