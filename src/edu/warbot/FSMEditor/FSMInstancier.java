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
	
	//Remarque : avant d'instancier la FSM il faut reconstruire complement le modele avec les informations dedans
	//notament reconstruire les conditions de sorties avec les ID de conditions de sorties (dans modeleState)

	ArrayList<WarFSM> fsm = new ArrayList<WarFSM>();
	Modele modele;

	HashMap<WarAgentType, Class<? extends ControllableWarAgentAdapter>> mapAgentTypeAdapter = new HashMap<>();

	public FSMInstancier(Modele modele) {
		this.modele = modele;

		generateHashMap();

		generateAllFSM();
	}

	private void generateHashMap() {
		mapAgentTypeAdapter.put(WarAgentType.WarExplorer,
				WarExplorerAdapter.class);
	}

	private void generateAllFSM() {
		for (ModeleBrain modelBrain : this.modele.getModelsBrains()) {
			// TODO ici je n'arrive pas à mettre le type générique
			// Remarque : dans le modele l'agent type est a null pour l'instant
			// il faut penser à le mettre dans la generation du modele grace au
			// XML
			// (le nom du type de l'agent sous formede strign est bien stoqué
			// dans le XML)

			Class classAdapter = this.mapAgentTypeAdapter.get(modelBrain
					.getAgentType());

			WarFSM<ControllableWarAgentAdapter> currentFsm = new WarFSM<>();
			generateFSM(currentFsm, modelBrain);

			fsm.add(currentFsm);

		}
	}

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

	public ArrayList<WarFSM> getGeneratedFSM() {
		return fsm;
	}

	// public void pushConfigurationInJarFile(){
	//
	// //Generer les fichiers de configurations
	// FSMXmlSaver configFile = new FSMXmlSaver();
	// configFile.saveFSM(this.modele, "FSMconfiguration.xml");
	//
	// //Ouvre le fichier Jar
	// try {
	//
	// FileInputStream is = new FileInputStream("FSMconfiguration.xml");
	// // jarFile = new JarFile(this.fileName);
	//
	// jarStream = new JarOutputStream(
	// new FileOutputStream(this.fileName));
	//
	// byte buffer[] = new byte[1000];
	// is.read(buffer);
	//
	// JarEntry entry = new JarEntry("FSMconfiguration.xml");
	// jarStream.putNextEntry(entry);
	// jarStream.write(buffer);
	// jarStream.closeEntry();
	//
	// jarStream.close();
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

	// public void generateJarFile(){
	//
	// // Crée le manifest
	// manifest = new Manifest();
	// manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION,
	// "1.0");
	//
	// // Ouvre le fichier jar
	// try {
	// jarStream = new JarOutputStream(
	// new FileOutputStream(this.fileName), manifest);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// /**
	// * récupere la liste des fichiers class necessaire à la FSM (les états,
	// * plan, conditions...)
	// */
	// classForJar = new ArrayList<Class>();
	//
	// // Récupère les plans
	// for (ModeleBrain currentBrain : this.modele.getModeleBrains()) {
	// for (ModeleState currentState : currentBrain.getStates()) {
	// try {
	// Class<?> c = Class.forName(currentState.getPlanName());
	// // c.get
	// addClass(c);
	// addClass(c.getSuperclass());
	//
	// } catch (ClassNotFoundException e1) {
	// e1.printStackTrace();
	// }
	// }
	// }
	//
	// // Récupère les condition
	// for (ModeleBrain currentBrain : this.modele.getModeleBrains()) {
	// for (ModeleCondition currentCondition : currentBrain
	// .getConditions()) {
	// try {
	// Class<?> c = Class.forName(currentCondition.getType());
	//
	// addClass(c);
	// addClass(c.getSuperclass());
	//
	// } catch (ClassNotFoundException e1) {
	// e1.printStackTrace();
	// }
	// }
	// }
	//
	// // Ajoute toutes les classes dans le jar
	// for (Class<?> c : classForJar) {
	// try {
	// JarEntry entry = new JarEntry(c.getName());
	// jarStream.putNextEntry(new JarEntry(c.getName()));
	// } catch (IOException e) {
	// System.err.println("ERRER add " + c.getName()
	// + " class in jarFile");
	// e.printStackTrace();
	// }
	// }
	//
	// // Ferme le fichier jar
	// try {
	// jarStream.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

}
