package edu.warbot.FSMEditor.Modele;

import java.util.ArrayList;
import java.util.Vector;

public class Modele {
	
	private ArrayList<ModeleState> sates = new ArrayList<>();
	private ArrayList<ModeleCondition> conditions = new ArrayList<>();

	public void addState(ModeleState d) {
		this.sates.add(d);
	}
	
	public ArrayList<ModeleState> getStates(){
		return this.sates;
	}

	public void addCondition(ModeleCondition c) {
		this.conditions.add(c);
	}

	public void removeState(ModeleState m) {
		//On supprime toutes les conditions de l'état source
		this.conditions.removeAll(m.getModeleConditions());
		
		//on parcours toutes les condtions pour voir si il yen a qui vont jusqua l'état qui va etre suppr
		ArrayList<ModeleCondition> toDelet = new ArrayList<>();
		for (ModeleCondition cond: this.conditions) {
			if(cond.getDestination().equals(m)){
				toDelet.add(cond);
			}
		}
		
		this.conditions.removeAll(toDelet);
		
		this.sates.remove(m);
	}

	public ArrayList<ModeleCondition> getConditions() {
		return this.conditions;
	}

	public Vector<String> getConditionsName() {
		Vector<String> res = new Vector<>();
		for (ModeleCondition m : this.conditions) {
			res.add(m.getNom());
		}
		return res;
	}

}
