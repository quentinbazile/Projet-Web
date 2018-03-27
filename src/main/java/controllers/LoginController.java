package controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modeles.DAO;
import modeles.DAOException;
import modeles.DataSourceFactory;

public class LoginController extends HttpServlet {
    
    protected static String userName, Admin;
    protected static int passwordParam;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
         * @throws modeles.DAOException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, DAOException {
		// Quelle action a appelé cette servlet ?
		String action = request.getParameter("action");
		if (null != action) {
			switch (action) {
				case "Connexion":
					checkLogin(request);
					break;
				case "DECONNEXION":
					doLogout(request);
					break;
			}
		}

		// Est-ce que l'utilisateur est connecté ?
		// On cherche l'attribut userName dans la session
		userName = findUserInSession(request);
                Admin = findAdminInSession(request);
		String jspView;
		if (null == userName) { // L'utilisateur n'est pas connecté
			// On choisit la page de login
			jspView = "views/login.jsp";

		} else { // L'utilisateur est connecté
			// On choisit la page d'affichage
                        if(userName.equals(Admin))
                            jspView = "views/ChiffreCateg.jsp";
                        else   
                            jspView = "views/index.html";
		}
		// On va vers la page choisie
		request.getRequestDispatcher(jspView).forward(request, response);

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
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
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

	private void checkLogin(HttpServletRequest request) throws DAOException {
		// Les paramètres transmis dans la requête
		String loginParam = request.getParameter("loginParam");
                String passwordParamAdmin = request.getParameter("passwordParam");
                try {
                    passwordParam = Integer.parseInt(request.getParameter("passwordParam"));
                }
                catch(NumberFormatException nfe){ 
                    passwordParam = 0;
                }
                
                // Le login/password défini dans web.xml
		String login = getInitParameter("login");
		String password = getInitParameter("password");
   
                DAO dao = new DAO(DataSourceFactory.getDataSource());

		if (dao.checkLogin(loginParam, passwordParam) || (login.equals(loginParam) && (password.equals(passwordParamAdmin)))) {
			// On a trouvé la combinaison login / password
			// On stocke l'information dans la session
			HttpSession session = request.getSession(true); // démarre la session
			session.setAttribute("userName", loginParam);
                        session.setAttribute("Admin", login);
                        System.out.println("ok");
		}
                System.out.println(dao.checkLogin(loginParam, passwordParam));
                System.out.println(login + password + loginParam + passwordParam);
	}

	private void doLogout(HttpServletRequest request) {
		// On termine la session
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	private String findUserInSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return (session == null) ? null : (String) session.getAttribute("userName");
	}
        
        private String findAdminInSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return (session == null) ? null : (String) session.getAttribute("Admin");
	}
}