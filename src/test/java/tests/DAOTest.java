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
        int customer_id = 1;
        int product_id = 980005;
        int quantity = 15;
        String freight_company = "We deliver";
        float shipping_cost = 2 * quantity;
        Date sales_date = new Date(System.currentTimeMillis());
        Date shipping_date = sales_date;
        assertEquals(1, myDAO.ajoutCommande(customer_id, product_id, quantity, shipping_cost, sales_date, shipping_date, freight_company));
    }

}
