package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import business.AdminManager;
import model.Segnalazione;

/**
 * Servlet implementation class SegnalazioniAdminController
 */
@WebServlet("/segnalazioniAdmin")
public class SegnalazioniAdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(SegnalazioniAdminController.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SegnalazioniAdminController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminManager am = new AdminManager();
		List<Segnalazione> listaS = new ArrayList<>();
		ObjectMapper om = new ObjectMapper();
		
		log.debug("SegnalazioniAdminController Pronto");
		
		listaS = am.visualizzaSegnalazioni();
		response.setContentType("application/json");
		response.getWriter().append(om.writeValueAsString(listaS));
		
		log.debug("SegnalazioniAdminController Funziona");
		
	}

}
