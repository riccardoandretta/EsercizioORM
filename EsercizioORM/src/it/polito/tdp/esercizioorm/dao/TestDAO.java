package it.polito.tdp.esercizioorm.dao;

import it.polito.tdp.esercizioorm.model.Corso;
import it.polito.tdp.esercizioorm.model.Studente;

public class TestDAO {

	public static void main(String[] args) {
		
		CorsoDAO cdao = new CorsoDAO();
		for (Corso c : cdao.getTuttiCorsi(null))
			System.out.println(c);
		
		StudenteDAO sdao = new StudenteDAO();
		for (Studente s : sdao.getTuttiStudenti(null)) 
			System.out.println(s);
	}
}
