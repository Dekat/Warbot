package edu.warbot.launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import madkit.action.SchedulingAction;
import madkit.message.SchedulingMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import turtlekit.agr.TKOrganization;
import turtlekit.kernel.TKEnvironment;
import turtlekit.kernel.TKLauncher;
import turtlekit.kernel.TurtleKit;
import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentCategory;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrainController;
import edu.warbot.game.Game;
import edu.warbot.game.MotherNatureTeam;
import edu.warbot.game.Team;
import edu.warbot.gui.WarViewer;
import edu.warbot.maps.WarMap;
import edu.warbot.tools.WarCircle;
import edu.warbot.tools.WarIOTools;
import edu.warbot.tools.WarXmlReader;

@SuppressWarnings("serial")
public class WarLauncher extends TKLauncher {

	public static final String TEAMS_DIRECTORY_NAME = "teams";
	public static final Color[] TEAM_COLORS = {
		new Color(149, 149, 255), // Blue
		new Color(255, 98, 98), // Red
		Color.YELLOW,
		Color.PINK,
		Color.CYAN,
		Color.ORANGE,
		Color.MAGENTA 
	};

	public WarLauncher() {
		super();
	}

	@Override
	protected void activate() {
		super.activate();
		setMadkitProperty("GPU_gradients", "true");
	}

	@Override
	protected void createSimulationInstance() {
		setLogLevel(Level.ALL);

		initProperties();
		setMadkitProperty(TurtleKit.Option.startSimu, "false");
		Dimension mapSize = Game.getInstance().getMap().getSize();
		setMadkitProperty(TurtleKit.Option.envWidth, String.valueOf(mapSize.width));
		setMadkitProperty(TurtleKit.Option.envHeight, String.valueOf(mapSize.height));

		setMadkitProperty(TurtleKit.Option.viewers, WarViewer.class.getName());
		setMadkitProperty(TurtleKit.Option.scheduler, WarScheduler.class.getName());
		setMadkitProperty(TurtleKit.Option.environment, TKEnvironment.class.getName());

		super.createSimulationInstance();

		File xmlSituationFile = Simulation.getInstance().getXmlSituationFileToLoad();
		if (xmlSituationFile == null)
			launchAllAgents();
		else
			launchAllAgentsFromXmlSituationFile(xmlSituationFile);

		Game.getInstance().setGameStarted();
	}

	@Override
	protected void end() {
		super.end();
		// On ferme l'interface
		Game.getInstance().setSimulationClosed();
	}

	public void launchAllAgents() {
		ArrayList<Team> playerTeams = Game.getInstance().getPlayerTeams();
		WarMap map = Game.getInstance().getMap();
		ArrayList<WarCircle> teamsPositions = map.getTeamsPositions();
		int teamCount = 0;
		MotherNatureTeam motherNatureTeam = Game.getInstance().getMotherNatureTeam();

		try {
			int compteur;
			for (Team t : playerTeams) {
				for (WarAgentType agentType : WarAgentType.values()) {
					for (compteur = 0; compteur < Simulation.getInstance().getNbAgentOfType(agentType); compteur++) {
						try {
							WarAgent agent = Game.instantiateNewControllableWarAgent(agentType.toString(), t);
							launchAgent(agent);
							agent.setRandomPositionInCircle(teamsPositions.get(teamCount));
						} catch (InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
							System.err.println("Erreur lors de l'instanciation de l'agent. Type non reconnu : " + agentType);
							e.printStackTrace();
						}
						// On cr�� autant de WarFood que d'agent au d�part
						motherNatureTeam.createAndLaunchNewResource(this, WarAgentType.WarFood);
					}
				}
				teamCount++;
			}

			// Puis on lance la simulation
			sendMessage(getMadkitProperty(turtlekit.kernel.TurtleKit.Option.community),
					TKOrganization.ENGINE_GROUP, TKOrganization.SCHEDULER_ROLE, new SchedulingMessage(SchedulingAction.RUN)); 


		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
			System.err.println("Erreur lors de l'instanciation des classes � partir des donn�es XML");
			e.printStackTrace();
		}
	}

	public void launchAllAgentsFromXmlSituationFile(File file) {
		HashMap<String, ArrayList<HashMap<String, String>>> xmlSituationFileContent = getXmlSituationFileContent(file);

		MotherNatureTeam motherNatureTeam = Game.getInstance().getMotherNatureTeam();

		try {
			for (String teamName : xmlSituationFileContent.keySet()) {

				// On r�cup�re l'�quipe
				Team currentTeam;
				if (teamName.equals(motherNatureTeam.getName()))
					currentTeam = motherNatureTeam;
				else {
					currentTeam = Game.getInstance().getPlayerTeam(teamName);
					// On v�rifie si le jar de l'�quipe existe
					if (Simulation.getInstance().getTeam(Team.getRealNameFromTeamName(teamName)) == null) {
						System.err.println("Le fichier JAR de l'�quipe " + Team.getRealNameFromTeamName(teamName) + " est manquant.");
					}
				}

				ArrayList<HashMap<String, String>> agentsOfCurrentTeam = xmlSituationFileContent.get(teamName);
				for (HashMap<String, String> agentDatas : agentsOfCurrentTeam) {
					WarAgent agent;
					String agentTypeName = agentDatas.get("Type");
					try {
						if (WarAgentType.valueOf(agentTypeName).getCategory() == WarAgentCategory.Resource) {
							agent = Game.instantiateNewWarResource(agentTypeName);
						} else {
							agent = Game.instantiateNewControllableWarAgent(agentTypeName, currentTeam);
						}
						launchAgent(agent);
						agent.setPosition(Double.valueOf(agentDatas.get("xPosition")),
								Double.valueOf(agentDatas.get("yPosition")));
						if (agent instanceof ControllableWarAgent) {
							agent.setHeading(Double.valueOf(agentDatas.get("Heading")));
							((ControllableWarAgent) agent).setViewDirection(Double.valueOf(agentDatas.get("ViewDirection")));
							((ControllableWarAgent) agent).init(Integer.valueOf(agentDatas.get("Health")),
									Integer.valueOf(agentDatas.get("NbElementsInBag")));
						}
					} catch (InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
						System.err.println("Erreur lors de l'instanciation de l'agent. Type non reconnu : " + agentTypeName);
						e.printStackTrace();
					}
				}
			}

			// Puis on lance la simulation
			sendMessage(getMadkitProperty(turtlekit.kernel.TurtleKit.Option.community),
					TKOrganization.ENGINE_GROUP, TKOrganization.SCHEDULER_ROLE, new SchedulingMessage(SchedulingAction.RUN)); 

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
			System.err.println("Erreur lors de l'instanciation des classes � partir des donn�es XML");
			e.printStackTrace();
		}
	}

	/**
	 * Retourne une hashmap contenant les �quipes (identifi�s par leur nom). Chaque �quipe est compos�e d'une liste d'agents.
	 * Chaque donn�e de chaque agent est enregistr� dans une hashmap
	 * Exemple : je veux la position en X du premier agent de l'�quipe "Test" :
	 * 		Double.valueOf(getXmlSituationFileContent(file).get("Test").get(0).get("xPosition"));
	 */
	public HashMap<String, ArrayList<HashMap<String, String>>> getXmlSituationFileContent(File file) {
		String rootPath = "/WarSituation";
		String teamsPath = rootPath + "/Teams";
		HashMap<String, ArrayList<HashMap<String, String>>> toReturn = new HashMap<>();
		try {
			// Ouverture du document
			Document doc = WarXmlReader.openXmlFile(file.getAbsolutePath());

			// Chargement
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList teamsNode =  (NodeList) xPath.compile(teamsPath).evaluate(doc, XPathConstants.NODESET);
			teamsNode = teamsNode.item(0).getChildNodes();
			Node node;
			int countColor = 0;
			for (int i = 0; i < teamsNode.getLength(); i++) { // Parcours des �quipes
				if (teamsNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
					NodeList teamNode = teamsNode.item(i).getChildNodes();
					// On r�cup�re l'�quipe courante (l'objet)
					String currentTeamName = teamNode.item(0).getFirstChild().getNodeValue();
					Team team = null;
					ArrayList<HashMap<String, String>> agentsOfCurrentTeam = new ArrayList<>();
					// On charge l'�quipe dans la liste des �quipes
					if (currentTeamName.equals(Game.getInstance().getMotherNatureTeam().getName()))
						team = Game.getInstance().getMotherNatureTeam();
					else {
						team = Team.duplicate(Simulation.getInstance().getTeam(Team.getRealNameFromTeamName(currentTeamName)), currentTeamName);
						Color teamNextColor = WarLauncher.TEAM_COLORS[countColor];
						Game.getInstance().addPlayerTeam(team, teamNextColor);
						countColor++;
					}

					for (int j = 0; j < teamNode.getLength(); j++) { // Parcours des agents d'une �quipe
						node = teamNode.item(j);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							if (node.getNodeName().equals("WarAgent")) {
								// On r�cup�re tous les param�tres de l'agent
								agentsOfCurrentTeam.add(WarXmlReader.getNodesFromNodeList(doc, node.getChildNodes()));
							}
						}
					} // Fin parcours des agents

					toReturn.put(currentTeamName, agentsOfCurrentTeam);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (ParserConfigurationException | TransformerFactoryConfigurationError | IOException | SAXException | IllegalArgumentException | SecurityException e) {
			System.err.println("Erreur lors du chargement depuis le fichier XML.");
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			System.err.println("Wrong XPath : " + teamsPath);
			e.printStackTrace();
		}

		return toReturn;
	}


	@SuppressWarnings("unchecked")
	public static void getTeamsFromDirectory() {
		String jarDirectoryPath = WarLauncher.TEAMS_DIRECTORY_NAME + File.separator;
		File jarDirectory = new File(jarDirectoryPath);
		// On regarde si un dossier jar existe
		if (! jarDirectory.exists() || jarDirectory.isDirectory()) {
			jarDirectory.mkdir();
		}
		File[] filesInJarDirectory = jarDirectory.listFiles();

		TeamXMLReader analXML = new TeamXMLReader();
		Team currentTeam;

		// On va chercher les fichiers .jar dans le dossier ad�quate
		for (File currentFile : filesInJarDirectory) {
			try {
				if (currentFile.getCanonicalPath().endsWith(".jar")) {
					JarFile jarCurrentFile = new JarFile(currentFile);

					// On parcours les entr�es du fichier JAR � la recherche des fichiers souhait�s
					JarEntry currentEntry;
					Enumeration<JarEntry> entries = jarCurrentFile.entries();
					HashMap<String, JarEntry> allJarEntries = new HashMap<>();
					boolean configFileFound = false;
					while (entries.hasMoreElements()) {
						currentEntry = entries.nextElement();

						// Si c'est le fichier config.xml
						if (currentEntry.getName().endsWith("config.xml")) {
							// On le lit et on l'analyse gr�ce � la classe TeamXmlReader
							BufferedInputStream input = new BufferedInputStream(jarCurrentFile.getInputStream(currentEntry));
							analXML.Ouverture(input);
							input.close();
							configFileFound = true;
						}
						else { // Sinon, on ne peut pas encore identifier le fichier donc nous allons le conserver (il faut d'abord analyser le fichier de configuration)
							String currentEntryName = currentEntry.getName();
							allJarEntries.put(currentEntryName.substring(currentEntryName.lastIndexOf("/") + 1, currentEntryName.length()), currentEntry);
						}
					}
					if (configFileFound) {
						// On a maintenant tous les fichiers dans un tableau et le fichier de configuration a �t� analys�

						// On r�cup�re le logo
						JarEntry logoEntry = allJarEntries.get(analXML.getIconeName());
						ImageIcon teamLogo = new ImageIcon(WarIOTools.toByteArray(jarCurrentFile.getInputStream(logoEntry)));
						// On change sa taille
						Image tmp = teamLogo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
						teamLogo = new ImageIcon(tmp);

						// On r�cup�re le son
						// TODO ajouter le son aux �quipes

						// On cr�� l'�quipe
						currentTeam = new Team(analXML.getTeamName());
						currentTeam.setLogo(teamLogo);
						currentTeam.setDescription(analXML.getTeamDescription().trim());

						// On recherche les classes de type BrainController
						// Pour cela, on utilise un URLClassLoader
						String urlName = currentFile.getCanonicalPath();
						URLClassLoader classLoader = new URLClassLoader(new URL [] { new URL("jar:file:/" + urlName + "!/") });
						// On parcours chaque nom de classe, puis on les charge
						HashMap<String, String> brainControllersClassesName = analXML.getBrainControllersClassesNameOfEachAgentType();

						for (String agentName : brainControllersClassesName.keySet()) {
							JarEntry classEntry = allJarEntries.get(brainControllersClassesName.get(agentName));
							currentTeam.addBrainControllerClassForAgent(agentName,
									(Class<? extends WarBrainController>) Class.forName(classEntry.getName().replaceAll(".class", "").replaceAll("/", "."),
											true, classLoader));
						}

						// On ferme le loader
						classLoader.close();

						// Puis on ferme le fichier JAR
						jarCurrentFile.close();

						// Si il y a d�j� une �quipe du m�me nom on ne l'ajoute pas
						if (Simulation.getInstance().getTeam(currentTeam.getName()) != null)
							System.err.println("Erreur lors de la lecture d'une �quipe : le nom " + currentTeam.getName() + " est d�j� utilis�.");
						else
							Simulation.getInstance().addAvailableTeam(currentTeam);
					} else { // Si le fichier de configuration n'a pas �t� trouv�
						System.err.println("Le fichier de configuration est introuvable dans le fichier JAR " + currentFile.getCanonicalPath());
					}
				}
			} catch (MalformedURLException e) {
				System.err.println("Lecture des fichiers JAR : URL mal form�e");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.err.println("Lecture des fichiers JAR : Classe non trouv�e");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Lecture des fichiers JAR : Lecture de fichier");
				e.printStackTrace();
			}
		}
	}

	public static void main() {
		executeThisLauncher();
	}
}
