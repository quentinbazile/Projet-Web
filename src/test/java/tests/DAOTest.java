package tests;

import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import modeles.DAO;
import modeles.DAOException;
import modeles.DataSourceFactory;
import modeles.ProductEntity;
import modeles.PurchaseOrderEntity;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DAOTest {

    private DAO myDAO; // L'objet à tester
    private DataSource myDataSource; // La source de données à utiliser

    @Before
    public void setUp() throws SQLException {
        myDataSource = DataSourceFactory.getDataSource();
        myDAO = new DAO(myDataSource);
    }
    
    /**
     * Test of checkLogin method fail, of class DAO.
     * @throws modeles.DAOException
     */
    @Test
    public void testcheckLoginFail() throws DAOException {
        String login = "notInBDD";
        int password = 0000;
        assertEquals(false, myDAO.checkLogin(login, password));
    }
    
    /**
     * Test of checkLogin method success, of class DAO.
     * @throws modeles.DAOException
     */
    @Test
    public void testcheckLoginSuccess() throws DAOException {
        String login = "jumboeagle@example.com";
        int password = 1;
        assertEquals(true, myDAO.checkLogin(login, password));
    }
    
    /**
     * Test of listeProduits method, of class DAO.
     * @throws java.sql.SQLException
     */
    @Test
    public void testListeProduits() throws SQLException {
        List<ProductEntity> result = myDAO.listeProduits();
        assertEquals(25, result.size());
    }
    
    /**
     * Test of listeCommandes method, of class DAO.
     * @throws java.sql.SQLException
     */
    @Test
    public void testListeCommandes() throws SQLException {
        String userName = "test@example.com";
        List<PurchaseOrderEntity> result = myDAO.listeCommandes(userName);
        assertEquals(0, result.size());
    }
    
    /**
     * Test of ajoutCommande method fail, of class DAO.
     * @throws modeles.DAOException
     */
    @Test
    public void testAjoutCommandeFail() throws DAOException {
        int order_num = 10398001;
        int customer_id = 1;
        int product_id = 980005;
        int quantity = 15;
        String freight_company = "We deliver";
        float shipping_cost = 2 * quantity;
        Date sales_date = new Date(System.currentTimeMillis());
        Date shipping_date = sales_date;
        try {
            myDAO.ajoutCommande(order_num, customer_id, product_id, quantity, shipping_cost, sales_date, shipping_date, freight_company); // Cette ligne doit lever une exception
            fail(); // On ne doit pas passer par ici
        } catch (DAOException e) {
            // On doit passer par ici, violation d'intégrité : contrainte de clé primaire unique
        }
    }
    
    /**
     * Test of updateCommande method fail, of class DAO.
     * @throws modeles.DAOException
     */
    @Test
    public void testUpdateCommandeFail() throws DAOException {
        int quantity = 400;
        String freight_company = "FR Express";
        float shipping_cost = 450f;
        int order_num = 40000000;
        assertEquals(0, myDAO.updateCommande(quantity, freight_company, shipping_cost, order_num));
    }
    
    /**
     * Test of deleteCommande method fail, of class DAO.
     * @throws java.sql.SQLException
     */
    @Test
    public void testDeleteCommandeFail() throws SQLException {
        int order_num = 40000000;
        assertEquals(0, myDAO.deleteCommande(order_num));
    }
    
    /**
     * Test of updateQuantity method, of class DAO.
     * @throws modeles.DAOException
     */
    @Test
    public void testUpdateQuantity() throws DAOException {
        int quantity = 0;
        int product_id = 980001;
        assertEquals(1, myDAO.updateQuantity(quantity, product_id));
    }
    
    /**
     * Test of salesByCustomer method, of class DAO.
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     */
    @Test
    public void testSalesByCustomer() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date debut = new Date(format.parse("2010-01-01").getTime());
        Date fin = new Date(format.parse("2012-01-01").getTime());
        Map<String, Double> result = myDAO.salesByCustomer(debut, fin);
        assertEquals(12, result.size());
    }
    
    /**
     * Test of salesByZone method, of class DAO.
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     */
    @Test
    public void testSalesByZone() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date debut = new Date(format.parse("2010-01-01").getTime());
        Date fin = new Date(format.parse("2012-01-01").getTime());
        Map<String, Double> result = myDAO.salesByZone(debut, fin);
        assertEquals(5, result.size());
    }
    
    /**
     * Test of salesByProduct method, of class DAO.
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     */
    @Test
    public void testSalesByProduct() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date debut = new Date(format.parse("2010-01-01").getTime());
        Date fin = new Date(format.parse("2012-01-01").getTime());
        Map<String, Double> result = myDAO.salesByProduct(debut, fin);
        assertEquals(5, result.size());
    }  
}
