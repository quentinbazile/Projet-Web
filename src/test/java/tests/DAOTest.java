package tests;

import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import modeles.CustomerEntity;
import modeles.DAO;
import modeles.DAOException;
import modeles.DataSourceFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class DAOTest {

    private DAO myDAO; // L'objet à tester
    private DataSource myDataSource; // La source de données à utiliser

    @Before
    public void setUp() throws SQLException {
        myDataSource = DataSourceFactory.getDataSource();
        myDAO = new DAO(myDataSource);
    }
    
    @Test
    public void testAjoutCommande() throws DAOException {
        int order_num = myDAO.orderNum() + 1;
        int customer_id = 1;
        int product_id = 980005;
        int quantity = 15;
        String freight_company = "We deliver";
        float shipping_cost = 2 * quantity;
        Date sales_date = new Date(System.currentTimeMillis());
        Date shipping_date = sales_date;
        assertEquals(1, myDAO.ajoutCommande(order_num, customer_id, product_id, quantity, shipping_cost, sales_date, shipping_date, freight_company));
    }

    @Test
    public void testUpdateCommande() throws DAOException {
        int product_id = 980005;
        int quantity = 50;
        float shipping_cost = 30;
        Date sales_date = new Date(System.currentTimeMillis());
        String freight_company = "We deliver";
        int order_num = 30298005;
        assertEquals(1, myDAO.updateCommande(product_id, quantity, shipping_cost, sales_date, freight_company, order_num));
    }
    
    @Test
    public void testDeleteCommande() throws DAOException {
        int order_num = 30298005;
        assertEquals(1, myDAO.deleteCommande(order_num));
    }

}
