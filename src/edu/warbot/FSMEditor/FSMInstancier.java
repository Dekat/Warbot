package edu.warbot.FSMEditor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.plan.WarPlan;
import edu.warbot.FSMEditor.models.Modele;
import edu.warbot.FSMEditor.models.ModeleBrain;
import edu.warbot.FSMEditor.models.ModeleState;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.brains.adapters.WarExplorerAdapter;

/**
 * Permet de générer et d'instanicer un objet de type FSM grâce à un modele de FSM
 * Prend en parametre un modele de FSM
 * Appeler la méthode getBrainControleurForAgent pour récupérer l'instance du brain d'un agent (instance d'une sous classe de warBrain) (le controleur renvoyé peut etre assimilé à un controleur issue d'une équipe classique)
 * @author Olivier
 *
 */
public class FSMInstancier {
	
//	ArrayList<WarFSM> fsm = new ArrayList<WarFSM>();
	Modele model;

	HashMap<WarAgentType, Class<? extends ControllableWarAgentAdapter>> mapAgentTypeAdapter = new HashMap<>();

	public FSMInstancier(Modele modele) {
		this.model = modele;

		if(!modele.isRebuild()){
			System.out.println("FSMInstancier : model is not rebuild, asking for rebuilding model...");
			FSMModelRebuilder fsmModeleRebuilder = new FSMModelRebuilder(modele);
			this.model = fsmModeleRebuilder.getRebuildModel();
		}
			
		generateHashMap();

	}
	
//	public FSMInstancier(String xmlConfigurationFileName) {
//		this(getModel(xmlConfigurationFileName));
//		FSMXmlReader xmlReader = new FSMXmlReader(xmlConfigurationFileName);
//		Modele model = xmlReader.getGeneratedFSMModel();
//		FSMModelRebuilder fsmRebuilder = new FSMModelRebuilder(model);
//	}

//	private static Modele getModel(String xmlConfigurationFileName) {
//		FSMXmlReader xmlReader = new FSMXmlReader(xmlConfigurationFileName);
//		Modele model = xmlReader.getGeneratedFSMModel();
//		FSMModelRebuilder fsmRebuilder = new FSMModelRebuilder(model);
//		return fsmRebuilder.getRebuildModel();
//	}

	//Ici le type generique de FSM est le type de l'adapteur mais comment le mettre comme type generique ?
	/**
	 * 
	 * @param agentType le type de l'agent que l'on souhaite récupére le brain
	 * @param adapter l'adapteur de l'agent dont on souhaite récupérer le BrainContoleur
	 * @return une instance de BrainControleur pour une fsm (semble à un brainController du équipe classique) (sous classe de WarBrain)
	 */
	public WarFSM getBrainControleurForAgent(WarAgentType agentType, ControllableWarAgentAdapter adapter) {
		System.out.println("FSMInstancier begining instanciation for " + agentType + "...");
		
		WarFSM fsm = new WarFSM<ControllableWarAgentAdapter>();

		//On recupère le modeleBrain qui correspond à l'agentType 
		ModeleBrain modelBrain = this.model.getModelBrain(agentType);
		
		//On commence par ajouter tous les états à la FSM
		for (ModeleState modelState : modelBrain.getStates()) {
			WarEtat<?> warState = getGenerateWarState(modelState, adapter);
			
			fsm.addEtat(warState);
		}
		
		System.out.println("FSMInstancier : lancement de l'initialisation de la FSM");
		fsm.initFSM();
		System.out.println("FSMInstancier : initialisation terminé avec succes");
		
		return fsm;
	}

	private WarEtat<? extends ControllableWarAgentAdapter> getGenerateWarState(
			ModeleState modelState, ControllableWarAgentAdapter adapter) {
		//Récupère le plan
		WarPlan<? extends ControllableWarAgentAdapter> warPlan = 
				getGenerateWarPlan(modelState, adapter);
		
		//Crée l'état
		WarEtat<? extends ControllableWarAgentAdapter> warState = 
				new WarEtat<ControllableWarAgentAdapter>(modelState.getName(), warPlan);
				
		return warState;
	}

	private WarPlan<? extends ControllableWarAgentAdapter> getGenerateWarPlan(
			ModeleState modelState, ControllableWarAgentAdapter adapter) {
		
		//Instancie le plan
		WarPlan<? extends ControllableWarAgentAdapter> instanciatePlan = null;
		try {
			
			Class c = Class.forName(modelState.getPlanLoaderName());

			//Récupère le constructeur
			Class typeOfAdapter = null;
			Constructor<?>[] constructors = c.getConstructors();
			if(constructors.length > 0){
				Constructor<?> constructor = constructors[0];
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				if(parameterTypes.length > 0) {
					typeOfAdapter = parameterTypes[0];
				}else{
					System.err.println("ERREUR la class " + modelState.getPlanLoaderName() + "  ne possède pas de constructeur correct");
				}
			}else{
				System.err.println("ERREUR la class " + modelState.getPlanLoaderName() + "  ne possède pas de constructeur");
			}
			
			instanciatePlan = (WarPlan<MovableWarAgentAdapter>) c
					.getConstructor(typeOfAdapter, modelState.getPlanSettings().getClass())
					.newInstance(adapter, modelState.getPlanSettings());
			
		} catch (NoSuchMethodException | SecurityException
				| ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
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

	private void generateHashMap() {
		mapAgentTypeAdapter.put(WarAgentType.WarExplorer,
				WarExplorerAdapter.class);
	}

}
