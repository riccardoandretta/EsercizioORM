package it.polito.tdp.esercizioorm.model;

import java.util.HashMap;
import java.util.Map;

public class CorsoIdMap {

	private Map<String, Corso> map;

	public CorsoIdMap() {
		this.map = new HashMap<>();
	}

// Vogliamo creare un wrapper per la mappa, quindi implemento get e put

	public Corso get(Corso corso) { // gli passiamo un corso, perchè se non ci fosse dovrei creare un oggetto Corso
									// e dunque necessito di tutte le informazioni
		
		Corso old = map.get(corso.getCodIns()); // in old viene salvato o null o l'oggetto associato al codins
		if (old == null) {
			// nella mappa non c'è questo corso --> devo aggiungerlo
			map.put(corso.getCodIns(), corso);
			return corso;
		}
		return old;
	}
	
	public Corso get(String codins) {
		return map.get(codins);
	}
	
	public void put (String codins, Corso corso){
		map.put(codins, corso);
	}

}
