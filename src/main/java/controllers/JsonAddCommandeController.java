package controllers;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modeles.DAO;
import modeles.DAOException;
import modeles.DataSourceFactory;

/**
 *
 * @author rbastide
 */
@WebServlet(name = "addCommande", urlPatterns = {"/addCommande"})
public class JsonAddCommandeController extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, DAOException {
            
		DAO dao = new DAO(DataSourceFactory.getDataSource());
                
                int quantity = Integer.parseInt(request.getParameter("qte"));
                int product_id = Integer.parseInt(request.getParameter("product_id"));
                String freight_company = "We deliver";
		String message;
                
		int order_num = dao.orderNum() + 1;
		int customer_id = LoginController.passwordParam;
                float shipping_cost = 400 + 2 * quantity;
                Date sales_date = new Date(System.currentTimeMillis());
                Date shipping_date = sales_date;
		
		dao.ajoutCommande(order_num, customer_id, product_id, quantity, shipping_cost, sales_date, shipping_date, freight_company);
                message = String.format("Commande %s ajoutée", order_num);

		Properties resultat = new Properties();
		resultat.put("message", message);

		try (PrintWriter out = response.getWriter()) {
			// On spécifie que la servlet va générer du JSON
			response.setContentType("application/json;charset=UTF-8");
			// Générer du JSON
			Gson gson = new Gson();
			out.println(gson.toJson(resultat));
		}
	}

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
            try {
                processRequest(request, response);
            } catch (DAOException ex) {
                Logger.getLogger(JsonAddCommandeController.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
            try {
                processRequest(request, response);
            } catch (DAOException ex) {
                Logger.getLogger(JsonAddCommandeController.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
