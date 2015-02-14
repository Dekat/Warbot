package edu.warbot.FSMEditor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.plan.WarPlan;
import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.models.Model;
import edu.warbot.FSMEditor.models.ModeleBrain;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.adapters.WarExplorerAdapter;

/**
 * Permet de générer et d'instanicer un objet de type FSM grâce à un modele de FSM
 * Prend en parametre un modele de FSM
 * Appeler la méthode getBrainControleurForAgent pour récupérer l'instance du brain d'un agent (instance d'une sous classe de warBrain) (le controleur renvoyé peut etre assimilé à un controleur issue d'une équipe classique)
 * @author Olivier
 *
 */
public class FSMInstancier<AgentAdapterType extends ControllableWarAgentAdapter> {
	
	WarFSM<AgentAdapterType> fsm = new WarFSM<>();
	
	Model model;
	
	//HashMap pour assicier les états et condition avec leurs nom
	//Ca pourrait plutot etre des hashmap de ModelState associé au warState
	HashMap<String, WarEtat<AgentAdapterType>> hashMapState = new HashMap<>();
	HashMap<String, WarCondition<AgentAdapterType>> hashMapCond = new HashMap<>();

	public FSMInstancier(Model modele) {
		this.model = modele;

		if(!modele.isRebuild()){
			System.out.println("FSMInstancier : model is not rebuild, asking for rebuilding model...");
			FSMModelRebuilder fsmModeleRebuilder = new FSMModelRebuilder(modele);
			this.model = fsmModeleRebuilder.getRebuildModel();
		}
			
	}
	
//	public FSMInstancier(String xmlConfigurationFileName) {
//		this(getModel(xmlConfigurationFileName));
//		FSMXmlReader xmlReader = new FSMXmlReader(xmlConfigurationFileName);
//		Modele model = xmlReader.getGeneratedFSMModel();
//		FSMModelRebuilder fsmRebuilder = new FSMModelRebuilder(model);
//	}

	/**
	 * 
	 * @param agentType le type de l'agent que l'on souhaite récupére le brain
	 * @param adapter l'adapteur de l'agent dont on souhaite récupérer le BrainContoleur
	 * @return une instance de BrainControleur pour une fsm (semble à un brainController du équipe classique) (sous classe de WarBrain)
	 */
	public WarFSM getBrainControleurForAgent(WarAgentType agentType, ControllableWarAgentAdapter adapter) {
		System.out.println("FSMInstancier begining instanciation for " + agentType + "...");
		
		//On recupère le modeleBrain qui correspond à l'agentType
		ModeleBrain modelBrain = this.model.getModelBrain(agentType);

		//On commence par ajouter tous les états à la FSM
		for (ModelState modelState : modelBrain.getStates()) {
			WarEtat<AgentAdapterType> warState = getGenerateWarState(modelState, adapter);
			
			//Ajoute l'état à la fsm
			fsm.addEtat(warState);
			
			//Ajoute l'état dans la hashMap d'état
			hashMapState.put(warState.getName(), warState);
		}
		
		//On ajoute ensuite les conditions
		for (ModelCondition modelCond : modelBrain.getConditions()) {
			WarCondition<AgentAdapterType> warCond = getGeneratedCondition(modelCond, adapter);
			
			//Ajoute l'état de destination de la condition
			WarEtat<AgentAdapterType> etat = hashMapState.get(modelCond.getStateDestination().getName());
			warCond.setDestination(etat);
			
			//Ajoute la condition à la HashMap
			hashMapCond.put(warCond.getName(), warCond);
		}
		
		//Reparcourir tous les états pour leurs ajouter leurs conditions de sorties
		//(C'était pas possible avant car les conditions n'existaient pas
		for (ModelState modelState : modelBrain.getStates()) {
			WarEtat<AgentAdapterType> warEtat = hashMapState.get(modelState.getName());
			
			for (ModelCondition modelCond : modelState.getConditionsOut()) {
				WarCondition<AgentAdapterType> warCond = hashMapCond.get(modelCond.getName());
				
				warEtat.addCondition(warCond);
			}
		}
		
		System.out.println("FSMInstancier : lancement de l'initialisation de la FSM");
		fsm.initFSM();
		System.out.println("FSMInstancier : initialisation terminé avec succes");
		
		return fsm;
	}

//	private HashMap<String, ModelState> getHashMapStates(ModeleBrain modelBrain) {
//		HashMap<String, ModelState> hashRes = new HashMap<>();
//		for (ModelState state : modelBrain.getStates()) {
//			hashRes.put(state.getName(), state);
//		}
//		return hashRes;
//	}
//
//	private HashMap<String, ModelCondition> getHashMapConditions(ModeleBrain modelBrain) {
//		HashMap<String, ModelCondition> hashRes = new HashMap<>();
//		for (ModelCondition cond: modelBrain.getConditions()) {
//			hashRes.put(cond.getName(), cond);
//		}
//		return hashRes;
//	}
	
	private WarCondition<AgentAdapterType> getGeneratedCondition(ModelCondition modelCond,
			ControllableWarAgentAdapter adapter) {
		
		//Instancie le plan
		WarCondition<AgentAdapterType> instanciateCond = null;
		try {
			
			Class c = Class.forName(modelCond.getConditionLoaderName());

			//Récupère le constructeur
			Class typeOfAdapter = c.getConstructors()[0].getParameterTypes()[1];
			
			instanciateCond = (WarCondition<AgentAdapterType>) c
					.getConstructor(String.class, typeOfAdapter, modelCond.getConditionSettings().getClass())
					.newInstance(modelCond.getName(), adapter, modelCond.getConditionSettings());
			
		} catch (NoSuchMethodException | SecurityException
				| ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			System.err.println("*** ERROR in dynamic instanciation of generated model check everything:");
			System.err.println("* Check constructor in WarPlan, WarCondition, WarReflexe");
			System.err.println("* Check attribut usage in subclass of previews and in " + modelCond.getConditionLoaderName());
			
			System.err.println("ERROR during instanciate WarCondition with class name " + modelCond.getConditionLoaderName() + " check name, constructor, classPath, etc...");
			System.err.println("Objects send : Adapter : " + adapter.getClass() + " , WarPlanSettings : " + modelCond.getConditionLoaderName().getClass());
			try {
				System.err.println("Objects expected : Adapter : " + Class.forName(modelCond.getConditionLoaderName()).getConstructors()[0].getParameterTypes()[0] + " , WarPlanSettings : " + Class.forName(modelCond.getConditionLoaderName()).getConstructors()[0].getParameterTypes()[1]);
			} catch (SecurityException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
		return instanciateCond;
	}

	private WarEtat<AgentAdapterType> getGenerateWarState(
			ModelState modelState, ControllableWarAgentAdapter adapter) {
		//Récupère le plan
		WarPlan<AgentAdapterType> warPlan = 
				getGenerateWarPlan(modelState, adapter);
		
		//Crée l'état
		WarEtat<AgentAdapterType> warState = 
				new WarEtat<>(modelState.getName(), warPlan);
				
		return warState;
	}

	private WarPlan<AgentAdapterType> getGenerateWarPlan(
			ModelState modelState, ControllableWarAgentAdapter adapter) {
		
		//Instancie le plan
		WarPlan<AgentAdapterType> instanciatePlan = null;
		try {
			
			Class c = Class.forName(modelState.getPlanLoaderName());

			//Récupère le constructeur
			Class typeOfAdapter = c.getConstructors()[0].getParameterTypes()[0];
			
			instanciatePlan = (WarPlan<AgentAdapterType>) c
					.getConstructor(typeOfAdapter, modelState.getPlanSettings().getClass())
					.newInstance(adapter, modelState.getPlanSettings());
			
		} catch (NoSuchMethodException | SecurityException
				| ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			System.err.println("*** ERROR in dynamic instanciation of generated model check everything:");
			System.err.println("* Check constructor in WarPlan, WarCondition, WarReflexe");
			System.err.println("* Check attribut usage in subclass of previews and in " + modelState.getPlanLoaderName());
			
			System.err.println("ERROR during instanciate WarPlan with class name " + modelState.getPlanLoaderName() + " check name, constructor, classPath, etc...");
			System.err.println("Objects send : Adapter : " + adapter.getClass() + " , WarPlanSettings : " + modelState.getPlanSettings().getClass());
			try {
				System.err.println("Objects expected : Adapter : " + Class.forName(modelState.getPlanLoaderName()).getConstructors()[0].getParameterTypes()[0] + " , WarPlanSettings : " + Class.forName(modelState.getPlanLoaderName()).getConstructors()[0].getParameterTypes()[1]);
			} catch (SecurityException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
		return instanciatePlan;
	}


}
