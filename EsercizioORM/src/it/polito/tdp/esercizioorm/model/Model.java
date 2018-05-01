package it.polito.tdp.esercizioorm.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.esercizioorm.dao.CorsoDAO;
import it.polito.tdp.esercizioorm.dao.StudenteDAO;

public class Model {

	// Ho bisogno dell'associazione degli studenti ai corsi: perchè voglio sapere i
	// crediti di uno studente

	private CorsoDAO cdao;
	private StudenteDAO sdao;

	private List<Corso> corsi;
	private List<Studente> studenti;

	private CorsoIdMap corsomap;
	private StudenteIdMap studentemap;

	public Model() {

		cdao = new CorsoDAO();
		sdao = new StudenteDAO();

		corsomap = new CorsoIdMap(); // Vogliamo creare l'IdentityMap
		studentemap = new StudenteIdMap();

		// Quando creo i corsi li vado già ad inserire nella mappa
		corsi = cdao.getTuttiCorsi(corsomap); // Questo è un trucco che mi assicura di non creare oggetti duplicati
		studenti = sdao.getTuttiStudenti(studentemap); // Dovrei fare lo stesso per studenti (ora mi serve)

		for (Studente s : studenti) {
			cdao.getCorsiFromStudente(s, corsomap);
		}

		for (Corso c : corsi) {
			sdao.getStudentiFromCorso(c, studentemap);
		}
	}

	public List<Studente> getStudentiFromCorso(String codins) {
		Corso c = corsomap.get(codins);

		if (c == null) {
			return new ArrayList<Studente>();
		}

		return c.getStudenti();
	}

	public List<Corso> getCorsiFromStudente(int matricola) {
		Studente s = studentemap.get(matricola);

		if (s == null) {
			return new ArrayList<Corso>(); // Ritorno una lista vuota, meglio che ritornare null!
		}

		return s.getCorsi();
	}

	public int getTotCreditiFromStudente(int matricola) {

		int sum = 0;

		for (Studente s : studenti) {
			if (s.getMatricola() == matricola) {
				for (Corso c : s.getCorsi()) {
					sum += c.getCrediti();
				}
				return sum;
			}
		}

		return -1;
	}

	public boolean iscriviStudenteACorso(int matricola, String codins) {

		Studente studente = studentemap.get(matricola);
		Corso corso = corsomap.get(codins);
		
		//Importante fare il controllo
		if (studente == null || corso == null) {
			// non posso iscrivere uno studente ad un corso
			return false;
		}

		// Aggiorno il DB (faccio prima il db perchè in caso di errori di accesso avrei poi
		// problemi di compatibilità)
		boolean result = sdao.iscriviStudenteACorso(studente, corso); // meglio passare gli oggetti completi perchè il
																		// model non sa cosa deve farne il dao

		if (result) { // aggiornamento db effettuato con successo

			// Aggiorno i riferimenti in memoria
			if (!studente.getCorsi().contains(corso)) { // NON inserisco duplicati
				studente.getCorsi().add(corso);
			}
			if (!corso.getStudenti().contains(studente)) {
				corso.getStudenti().add(studente);
			}
			return true;
		}

		return false;
	}

	/*
	 * Questo metodo viene utilizzato solo per testare le performance di
	 * ConnectDBCP.
	 */
	public void testCP() {
		double avgTime = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			List<Studente> studenti = sdao.getTuttiStudenti(new StudenteIdMap());
			for (Studente s : studenti) {
				sdao.studenteIscrittoACorso(s.getMatricola(), "01NBAPG");
			}
			double tt = (System.nanoTime() - start) / 10e9;
			System.out.println(tt);
			avgTime += tt;
		}
		System.out.println("AvgTime (mean on 10 loops): " + avgTime / 10);
	}

}
