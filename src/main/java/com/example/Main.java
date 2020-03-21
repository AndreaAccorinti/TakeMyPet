package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import model.Admin;
import model.Proprietario;
import utils.JPAUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import javax.persistence.EntityManager;

import model.Admin;
import model.Proprietario;
import utils.JPAUtil;

/**
 * 
 * This class launches the web application in an embedded Jetty container.
 * This is the entry point to your application. The Java command that is used for
 * launching should fire this main method.
 *
 */
public class Main {
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
        String webappDirLocation = "src/main/webapp/";
        
        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        Server server = new Server(Integer.valueOf(webPort));
        WebAppContext root = new WebAppContext();

        root.setContextPath("/");
        //root.setDescriptor(webappDirLocation+"/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);
        
        //Parent loader priority is a class loader setting that Jetty accepts.
        //By default Jetty will behave like most web containers in that it will
        //allow your application to replace non-server libraries that are part of the
        //container. Setting parent loader priority to true changes this behavior.
        //Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
        root.setParentLoaderPriority(true);
        
        server.setHandler(root);
        
        server.start();
        server.join();  
        
     
        
        
        Connection connection = getConnection();
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
        }
        
        
   EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
    	
    	Admin a = new Admin();
    	Proprietario ps = new Proprietario();
    	
    	ps.setUsername("alex");
    	ps.setPassword("password");
    	ps.setTipoUtente("proprietario");
    	ps.setBloccato(false);
    	ps.setAttivo(true);

    	a.setUsername("admin");
    	a.setPassword("password");
    	a.setTipoUtente("admin");
    	
    	

    	
    	em.getTransaction().begin();
    	em.persist(ps);
    	em.persist(a);
    	em.getTransaction().commit();
    }
    
	
   
    private static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
    }
    
    
    
    
    }

