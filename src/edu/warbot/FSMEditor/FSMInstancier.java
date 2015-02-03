package edu.warbot.FSMEditor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.plan.WarPlan;
import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleBrain;
import edu.warbot.FSMEditor.Modeles.ModeleState;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.WarAgentAdapter;
import edu.warbot.brains.adapters.WarExplorerAdapter;

public class FSMInstancier {
	
//	ArrayList<WarFSM> fsm = new ArrayList<WarFSM>();
	Modele model;

	HashMap<WarAgentType, Class<? extends ControllableWarAgentAdapter>> mapAgentTypeAdapter = new HashMap<>();

	public FSMInstancier(Modele modele) {
		this.model = modele;

		generateHashMap();

//		generateAllFSM();
	}

	//Ici le type generique de FSM est le type de l'adapteur mais comment le mettre comme type generic ???
	public WarFSM getInstanciateFSM(WarAgentType agentType, ControllableWarAgentAdapter adapter) {
		
		WarFSM fsm = new WarFSM<ControllableWarAgentAdapter>();
		
		//On recupère le modeleBrain qui correspond à l'agentType 
		ModeleBrain modelBrain = this.model.getModelBrain(agentType);
		
		//On commence par ajouter tous les états à la FSM
		for (ModeleState modelState : modelBrain.getStates()) {
			
			WarEtat<ControllableWarAgentAdapter> warState = getGenerateWarState(modelState, adapter);
			
			fsm.addEtat(warState);
		}
		
		return fsm;
	}

	private WarEtat<ControllableWarAgentAdapter> getGenerateWarState(
			ModeleState modelState, ControllableWarAgentAdapter adapter) {
		//Récupère le plan
		WarPlan<ControllableWarAgentAdapter> warPlan = 
				getGenerateWarPlan(modelState, adapter);
		
		//Crée l'état
		WarEtat<ControllableWarAgentAdapter> warState = 
				new WarEtat<ControllableWarAgentAdapter>(modelState.getName(), warPlan);
				
		return warState;
	}

	private WarPlan<ControllableWarAgentAdapter> getGenerateWarPlan(
			ModeleState modelState, ControllableWarAgentAdapter adapter) {
		
		//Instancie le plan
		WarPlan<ControllableWarAgentAdapter> instanciatePlan = null;
		try {
			Constructor<?> constructorPlan = 
					Class.forName(modelState.getPlanName()).getConstructor(adapter.getClass(), modelState.getWarPlanSettings().getClass());
			
			instanciatePlan = (WarPlan<ControllableWarAgentAdapter>) constructorPlan.newInstance(adapter, modelState.getWarPlanSettings());
		
		} catch (NoSuchMethodException | SecurityException
				| ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			System.err.println("ERRER during instanciate WarPlan with class name " + modelState.getPlanName() + " check name, constructor, classPath, etc...");
		}
		
		return instanciatePlan;
	}

	private void generateHashMap() {
		mapAgentTypeAdapter.put(WarAgentType.WarExplorer,
				WarExplorerAdapter.class);
	}

//	private void generateAllFSM() {
//		for (ModeleBrain modelBrain : this.model.getModelsBrains()) {
//			// TODO ici je n'arrive pas à mettre le type générique
//			// Remarque : dans le modele l'agent type est a null pour l'instant
//			// il faut penser à le mettre dans la generation du modele grace au
//			// XML
//			// (le nom du type de l'agent sous formede strign est bien stoqué
//			// dans le XML)
//
//			Class classAdapter = this.mapAgentTypeAdapter.get(modelBrain
//					.getAgentType());
//
//			WarFSM<ControllableWarAgentAdapter> currentFsm = new WarFSM<>();
//			generateFSM(currentFsm, modelBrain);
//
//			fsm.add(currentFsm);
//
//		}
//	}

	private void generateFSM(WarFSM<?> fsm, ModeleBrain model) {
		// Pour chaque état du modele on l'ajoute à la FSM
		for (ModeleState state : model.getStates()) {
			generateState(fsm, state);
		}
		// Pour chaque conditions du modele on l'ajoute a la FSM

		// Pour conditions on lui place son état de sortie

		// Pour chaque état on lui place ses conditions de sorties
	}

	private void generateState(WarFSM<?> fsm, ModeleState modelState) {
		String name = modelState.getName();

		WarPlan<ControllableWarAgentAdapter> plan = getGeneratedPlan(modelState
				.getPlanName());

		WarEtat<ControllableWarAgentAdapter> warState = new WarEtat<ControllableWarAgentAdapter>(
				name, plan);
	}

	private WarPlan<ControllableWarAgentAdapter> getGeneratedPlan(
			String planName) {
		WarPlan<ControllableWarAgentAdapter> plan = null;

		WarAgentAdapter adapterForConstructor = null;
		WarPlanSettings settingForConstructor = null;

		try {
			Constructor<?> constructor = Class.forName(planName)
					.getConstructor(WarAgentAdapter.class,
							WarPlanSettings.class);
			plan = (WarPlan<ControllableWarAgentAdapter>) constructor
					.newInstance(adapterForConstructor, settingForConstructor);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.err.println("Impossible to instanciate class for name"
					+ planName + " because class does not existe");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// plan = (WarPlan<ControllableWarAgentAdapter>)
		// Class.forName(planName).newInstance();

		
		return plan;
	}

	public void instanciateFSM() {
		// TODO Auto-generated method stub
		
	}


}
