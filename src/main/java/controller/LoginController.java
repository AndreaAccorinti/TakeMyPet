package controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import business.LoginManager;
import model.Utente;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginController() {
		super();
	}
	/*
	/**
	 * @throws IOException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LoginManager lm = new LoginManager();
		String isBloccato;
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		Utente u;
		try {
			u = lm.login(username, password);
			if(u.isBloccato()) {
				isBloccato = "qualcosa";
			}
			else { 
				isBloccato = null;
			}
			
			response.getWriter().append(isBloccato);
			
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
*/
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		ObjectMapper om = new ObjectMapper();
		LoginManager lm = new LoginManager();

		Utente u;
		try {
				u = lm.login(username, password);

				log.debug("LoginController Pronto");
			
				response.setContentType("application/json");
				response.getWriter().append(om.writeValueAsString(u));
			
				log.debug("LoginController Funziona");
				
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	
			//log.debug(u.getUsername() + " si e loggato");
		
	

	}

}
