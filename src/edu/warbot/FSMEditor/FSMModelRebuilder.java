package edu.warbot.FSMEditor;

import java.util.HashMap;

import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleBrain;
import edu.warbot.FSMEditor.Modeles.ModeleCondition;
import edu.warbot.FSMEditor.Modeles.ModeleState;
/**
 * Cette classe permet de prendre un Model du FSMEditor qui n'est pas complet
 * Le model contient des ID pour les états destinations des conditions et les conditions des états
 * Cette classe va permettre de reconstuire le modele avec les vrais pointeurs en remplacant les ID 
 * par les conditions ou états auquels ils correspondent
 * Passer en parametre du constucteur un modele issue du fichier de configuration
 * Récupérer le modele reconstuit avec getRebuildModel
 * @author Olivier
 */
public class FSMModelRebuilder {

	private Modele model;

	/**
	 * Appeler le constructeur lancer directement la reconstruction du modele
	 * @param model
	 */
	public FSMModelRebuilder(Modele model) {
		this.model = model;
		System.out.println("FSMRebuilder : begining construction...");
		rebuildModel();
		this.model.setIsRebuild(true);
	}

	private void rebuildModel() {
		//Rebuild chaque brains
		for (ModeleBrain brain : model.getModelsBrains()) {
			rebuildBrain(brain);
		}
	}

	private void rebuildBrain(ModeleBrain brain) {
		/**
		 * Construit une liste d'assotion pour les id (states et conditions) et les objets r�els (pointeurs state et conditions)
		 */
		//Construit une liste d'association conditionID - condition pointeur
		//Construit une liste d'association stateID - state pointeur
		
		HashMap<String, ModeleCondition> mapConditionsID = new HashMap<>();
		HashMap<String, ModeleState> mapStatesID = new HashMap<>();
		
		//construit liste états
		for (ModeleState state : brain.getStates()) {
			mapStatesID.put(state.getName(), state);
		}
		
		//Constuit liste condition
		for (ModeleCondition condition: brain.getConditions()) {
			mapConditionsID.put(condition.getName(), condition);
		}
		
		//Pour chaque �tat
		for (ModeleState state : brain.getStates()) {
			rebuildState(state, mapConditionsID);
		}
		
		//Pour chaque �tat
		for (ModeleCondition cond : brain.getConditions()) {
			rebuildCondition(cond, mapStatesID);
		}
	}

	private void rebuildCondition(ModeleCondition cond,
			HashMap<String, ModeleState> mapStatesID) {
		cond.setDestination(mapStatesID.get(cond.getStateOutId()));
	}

	private void rebuildState(ModeleState state,
			HashMap<String, ModeleCondition> mapConditionsID) {
		
		//Pour chaque ID conditions de sortie de l'état
		for (String currentConditionID : state.getConditionsOutID()) {
			//On récupère la conditions dan la liste d'association et on l'ajoute au modele
			state.addConditionOut(mapConditionsID.get(currentConditionID));
		}
	}

	/**
	 * @return Le modèle de la FSM reconstuit (correspond au modèle utilisable pour instiancier des FSMBrains)
	 */
	public Modele getRebuildModel() {
		return this.model;
	}

}
