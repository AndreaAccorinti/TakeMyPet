package business;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import interfaces.ProprietarioInterface;
import model.Animale;
import model.Annuncio;
import model.Evento;
import model.Proprietario;
import model.Segnalazione;
import model.UtenteApp;
import model.Valutazione;
import utils.JPAUtil;

public class ProprietarioManager extends UtenteAppManager implements ProprietarioInterface {

	public void modificaProfilo(String username) {
		// TODO Auto-generated method stub
		
	}

	public UtenteApp cambiaTipoProfilo(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public void partecipaEvento(Evento evento, UtenteApp partecipante) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	//Animali
	public List<Animale> visualizzaAnimale(Proprietario utente) {
		List<Animale> _return= new ArrayList<Animale>();
	
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		
		//_return = em.createNamedQuery("cercaAnimaliPerProprietario", Animale.class).setParameter("username", utente).getResultList();
		
		for (Animale e : em.createNamedQuery("cercaAnimaliPerProprietario", Animale.class).setParameter("username", utente).getResultList())  {
			_return.add(e);
		}
		
		
		
		em.close();
		return _return;
	}

	

	@Override
	public void eliminaAnimale(Integer idAnimale) {
		EntityManager em =JPAUtil.getInstance().getEmf().createEntityManager();
		
		em.getTransaction().begin();
		em.remove(em.find(Animale.class,idAnimale));
		em.getTransaction().commit();
	}

	@Override
	public String aggiungiAnimale(Date dataDiNascita, String dettagli, Integer eta, String nome, String razza,
			String tipo) {
EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		
		Animale a= new Animale();
		a.setDataDiNascita(dataDiNascita);
		a.setDettagli(dettagli);
		a.setEta(eta);
		a.setNome(nome);
		a.setRazza(razza);
		a.setTipo(tipo);
		
		em.getTransaction().begin();
		em.persist(a);
		em.getTransaction().commit();
		em.close();
		return null;
	}


public void aggiungiAnnuncio(String usernameProprietario,String descrizione,String longitudine, String latitudine, List<Animale> listaAnimali) {
	EntityManager em =JPAUtil.getInstance().getEmf().createEntityManager();
	
	Annuncio annuncio=new Annuncio();
	Proprietario proprietario=new Proprietario();
	
	proprietario=em.find(Proprietario.class, usernameProprietario);
	
	annuncio.setProprietarioAnnuncio(proprietario);
	annuncio.setDescrizione(descrizione);
	annuncio.setAnimaliAnnuncio(listaAnimali);
	annuncio.setLongitudine(longitudine);
	annuncio.setLongitudine(longitudine);
	
	em.getTransaction().begin();
	em.persist(annuncio);
	em.getTransaction().commit();
	em.close();
	
}

public void rimuoviAnnuncio(Integer idAnnuncio) {
	
	EntityManager em =JPAUtil.getInstance().getEmf().createEntityManager();
	
	em.getTransaction().begin();
	em.remove(em.find(Annuncio.class, idAnnuncio));
	em.getTransaction().commit();
	em.close();
	
}

public List<Annuncio> listaAnnunciProprietario(String usernameProprietario){
	EntityManager em =JPAUtil.getInstance().getEmf().createEntityManager();
	
	List<Annuncio> _return=new ArrayList<Annuncio>();
	_return= em.createNamedQuery("annuncio.findByProprietario").setParameter("name", usernameProprietario).getResultList();
	return _return;
	
}


}
