package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Properties;

import modeles.DAO;
import modeles.DataSourceFactory;

@WebServlet(name = "salesByCustomer", urlPatterns = {"/salesByCustomer"})
public class JsonSalesByCustomerController extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, ParseException {
		// Créér le DAO avec sa source de données
		DAO dao = new DAO(DataSourceFactory.getDataSource());
                
                String datePickerDebut = request.getParameter("datePickerDebut");
                String datePickerFin = request.getParameter("datePickerFin");
                
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date dateDebut = new java.sql.Date(format.parse(datePickerDebut).getTime());
                Date dateFin = new java.sql.Date(format.parse(datePickerFin).getTime());
                
		// Properties est une Map<clé, valeur> pratique pour générer du JSON
		Properties resultat = new Properties();
		try {
			resultat.put("records", dao.salesByCustomer(dateDebut, dateFin));
		} catch (SQLException ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resultat.put("records", Collections.EMPTY_LIST);
			resultat.put("message", ex.getMessage());
		}

		try (PrintWriter out = response.getWriter()) {
			// On spécifie que la servlet va générer du JSON
			response.setContentType("application/json;charset=UTF-8");
			// Générer du JSON
			// Gson gson = new Gson();
			// setPrettyPrinting pour que le JSON généré soit plus lisible
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String gsonData = gson.toJson(resultat);
			out.println(gsonData);
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
            } catch (ParseException ex) {
                Logger.getLogger(JsonSalesByCustomerController.class.getName()).log(Level.SEVERE, null, ex);
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
            } catch (ParseException ex) {
                Logger.getLogger(JsonSalesByCustomerController.class.getName()).log(Level.SEVERE, null, ex);
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
