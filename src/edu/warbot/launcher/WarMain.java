package edu.warbot.launcher;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.warbot.FSM.WarFSMBrainController;
import edu.warbot.FSMEditor.FSMModelRebuilder;
import edu.warbot.FSMEditor.xmlParser.FsmXmlParser;
import edu.warbot.FSMEditor.xmlParser.FsmXmlReader;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrain;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.game.WarGameListener;
import edu.warbot.game.WarGameMode;
import edu.warbot.gui.launcher.LoadingDialog;
import edu.warbot.gui.launcher.WarLauncherInterface;
import edu.warbot.tools.WarIOTools;

public class WarMain implements WarGameListener {
    private static final String CMD_NAME = "java WarMain";
    private static final String CMD_LOG_LEVEL = "--loglevel";
    private static final String CMD_NB_AGENT_OF_TYPE = "--nb";
    private static final String CMD_FOOD_APPEARANCE_RATE = "--foodrate";
    private static final String CMD_GAME_MODE = "--gamemode";
    private static final String CMD_HELP = "--help";

	public static final String TEAMS_DIRECTORY_NAME = "teams";
	
	private LoadingDialog loadingDialog;
	
	private WarGame game;
	private WarGameSettings settings;
	private WarLauncherInterface launcherInterface;
	
	private Map<String, Team> availableTeams;

	public WarMain() {
		availableTeams = new HashMap<String, Team>();
		settings = new WarGameSettings();
		
		// On récupère les équipes
		LoadingDialog loadDial = new LoadingDialog("Chargement des équipes...");
		loadDial.setVisible(true);

		// On initialise la liste des équipes existantes dans le dossier "teams"
		availableTeams = getTeamsFromDirectory();
		Shared.availableTeams = new HashMap<String, Team>(availableTeams);

		// On vérifie qu'au moins une équipe a été chargée
		if (availableTeams.size() > 0) {
			// On lance la launcher interface
			final WarMain warMain = this;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					launcherInterface = new WarLauncherInterface(warMain, settings);
					launcherInterface.setVisible(true);
				}
			});
		} else {
			JOptionPane.showMessageDialog(null, "Aucune équipe n'a été trouvé dans le dossier \""+ TEAMS_DIRECTORY_NAME + "\"",
					"Aucune équipe", JOptionPane.ERROR_MESSAGE);
		}

		loadDial.dispose();
	}

    public WarMain(WarGameSettings settings, String ... selectedTeamsName) throws WarCommandException {
        availableTeams = new HashMap<String, Team>();

        // On initialise la liste des équipes existantes dans le dossier "teams"
        availableTeams = getTeamsFromDirectory();
        Shared.availableTeams = new HashMap<String, Team>(availableTeams);

        // On vérifie qu'au moins une équipe a été chargée
        if (availableTeams.size() > 0) {
            // On lance le jeu
            this.settings = settings;
            if(selectedTeamsName.length > 1) {
                for (String teamName : selectedTeamsName) {
                    if (availableTeams.containsKey(teamName))
                        settings.addSelectedTeam(availableTeams.get(teamName));
                    else
                        throw new WarCommandException("Team \"" + teamName + "\" does not exists. Available teams are : " + availableTeams.keySet());
                }
            } else {
                throw new WarCommandException("Please select at least two teams. Available teams are : " + availableTeams.keySet());
            }
            start();
        } else {
            throw new WarCommandException("Not team found in folder \""+ TEAMS_DIRECTORY_NAME + "\"");
        }
    }

	public void startGame() {
		loadingDialog = new LoadingDialog("Lancement de la simulation...");
		loadingDialog.setVisible(true);
		start();
	}

    private void start() {
        game = new WarGame(settings);
        Shared.game = game;

        new WarLauncher().executeLauncher();
        game.addWarGameListener(this);
    }
	
	@SuppressWarnings("unchecked")
	public Map<String, Team> getTeamsFromDirectory() {
		Map<String, Team> loadedTeams = new HashMap<String, Team>();
		
		String jarDirectoryPath = TEAMS_DIRECTORY_NAME + File.separator;
		File jarDirectory = new File(jarDirectoryPath);
		// On regarde si un dossier jar existe
		if (! jarDirectory.exists() || jarDirectory.isDirectory()) {
			jarDirectory.mkdir();
		}
		File[] filesInJarDirectory = jarDirectory.listFiles();

		TeamXMLReader analXML = new TeamXMLReader();
		Team currentTeam;

		// On va chercher les fichiers .jar dans le dossier adéquate
		for (File currentFile : filesInJarDirectory) {
			try {
				if (currentFile.getCanonicalPath().endsWith(".jar")) {
					JarFile jarCurrentFile = new JarFile(currentFile);

					// On parcours les entrées du fichier JAR à la recherche des fichiers souhaités
					HashMap<String, JarEntry> allJarEntries = getAllJarEntry(jarCurrentFile);
					
					boolean configFileFound = allJarEntries.containsKey("config.xml");
					
					if(configFileFound){
						
						//On analyse le fichier XML
						BufferedInputStream input = new BufferedInputStream(jarCurrentFile.getInputStream(allJarEntries.get("config.xml")));
						analXML.ouverture(input);
						input.close();
						
						// On a maintenant tous les fichiers dans un tableau et le fichier de configuration a été analysé

						// On récupère le logo
						ImageIcon teamLogo = getTeamLogo(allJarEntries.get(analXML.getIconeName()), jarCurrentFile);

						// On récupère le son

						// On créé l'équipe
						currentTeam = new Team(analXML.getTeamName());
						currentTeam.setLogo(teamLogo);
						currentTeam.setDescription(analXML.getTeamDescription().trim());

						// On recherche les classes de type BrainController
						// Pour cela, on utilise un URLClassLoader
						String urlName = currentFile.getCanonicalPath();
						URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new URL("jar:file:" + urlName + "!/")});

                        //Vérifie si l'équipe est une FSM (regard dans le fichier de configuration)
                        if(analXML.isFSMTeam()){
                        	try{
	                        	System.out.println("WarMain : FSM Team found");
	                        	
	                        	JarEntry entryFSMConfiguration = allJarEntries.get(FsmXmlParser.xmlConfigurationDefaultFilename);
	                        	System.out.println("WarMain : jarEntry for FSMXmlConfiguration found");
	                        	
	                        	InputStream fileFSMConfig = jarCurrentFile.getInputStream(entryFSMConfiguration);
	                        	FsmXmlReader fsmXmlReader = new FsmXmlReader(fileFSMConfig);
	                        	System.out.println("WarMain : FSMXmlReader successfull read fsmConfigFile");
	                        	FSMModelRebuilder fsmModelRebuilder = new FSMModelRebuilder(fsmXmlReader.getGeneratedFSMModel());
	                        	currentTeam.setFsmModel(fsmModelRebuilder.getRebuildModel());
	                        	System.out.println("WarMain : FSMModelRebuilder successfull rebuild model");
	                        	
	                        	HashMap<String, String> brainControllersClassesName = analXML.getBrainControllersClassesNameOfEachAgentType();
	                    		
								for (String agentName : brainControllersClassesName.keySet()) {
									currentTeam.addBrainControllerClassForAgent(agentName, WarFSMBrainController.class);
								}
                        	}catch(IllegalArgumentException e){
                        		System.err.println("ERROR during readind FSM teams " + analXML.getTeamName());
                        	}

                        }else{
	                        // On parcours chaque nom de classe, puis on les charge
							HashMap<String, String> brainControllersClassesName = analXML.getBrainControllersClassesNameOfEachAgentType();
	
							for (String agentName : brainControllersClassesName.keySet()) {
								JarEntry classEntry = allJarEntries.get(brainControllersClassesName.get(agentName));
	                            currentTeam.addBrainControllerClassForAgent(agentName,
										classLoader.loadClass(classEntry.getName().replace(".class", "").replace("/", ".")).asSubclass(WarBrain.class));
							}
                        }

						// On ferme le loader
						classLoader.close();

						// Puis on ferme le fichier JAR
						jarCurrentFile.close();

						// Si il y a déjà une équipe du même nom on ne l'ajoute pas
						if (loadedTeams.containsKey(currentTeam.getName()))
							System.err.println("Erreur lors de la lecture d'une équipe : le nom " + currentTeam.getName() + " est déjà utilisé.");
						else
							loadedTeams.put(currentTeam.getName(), currentTeam);
						
					} else { // Si le fichier de configuration n'a pas été trouvé
						System.err.println("Le fichier de configuration est introuvable dans le fichier JAR " + currentFile.getCanonicalPath());
					}
				}
			} catch (MalformedURLException e) {
				System.err.println("Lecture des fichiers JAR : URL mal formée");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.err.println("Lecture des fichiers JAR : Classe non trouvée");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Lecture des fichiers JAR : Lecture de fichier");
				e.printStackTrace();
			}
        }
		return loadedTeams;
	}
	
	private ImageIcon getTeamLogo(JarEntry logoEntry, JarFile jarCurrentFile) {
        ImageIcon teamLogo = null;
		try {
			teamLogo = new ImageIcon(WarIOTools.toByteArray(jarCurrentFile.getInputStream(logoEntry)));
		} catch (IOException e) {
			System.err.println("ERROR loading file " + logoEntry.getName() + " inside jar file " + jarCurrentFile.getName());
			e.printStackTrace();
		}
        // TODO set general logo if no image found
		// On change sa taille
		Image tmp = teamLogo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		teamLogo = new ImageIcon(tmp);
		return teamLogo;
	}

	private HashMap<String, JarEntry> getAllJarEntry(JarFile jarFile) {
		HashMap<String, JarEntry> allJarEntries = new HashMap<>();

		Enumeration<JarEntry> entries = jarFile.entries();
		
		JarEntry currentEntry;
		while (entries.hasMoreElements()) {
			currentEntry = entries.nextElement();

			String currentEntryName = currentEntry.getName();
			allJarEntries.put(currentEntryName.substring(currentEntryName.lastIndexOf("/") + 1, currentEntryName.length()), currentEntry);

			// Si c'est le fichier config.xml
			if (currentEntry.getName().endsWith("config.xml")) {
				// On le lit et on l'analyse grâce à la classe TeamXmlReader
				
			}
		}
		return allJarEntries;
	}

	public Map<String, Team> getAvailableTeams() {
		return new HashMap<>(availableTeams);
	}
	
	public static void main(String[] args) {

        if(args.length == 0) {
            new WarMain();
        } else {
            try {
                System.out.println("Command arguments = " + Arrays.asList(args));

                WarGameSettings settings = new WarGameSettings();
                ArrayList<String> selectedTeams = new ArrayList<>();
                boolean askedForHelp = false;

                for(int currentArgIndex = 0; currentArgIndex < args.length; currentArgIndex++) {
                    String currentArg = args[currentArgIndex];
                    if(currentArg.startsWith("-")) {
                        String[] splitDoubleDot = currentArg.split(":");
                        String[] splitEquals = splitDoubleDot[splitDoubleDot.length - 1].split("=");
                        String cmdName = splitDoubleDot[0];
                        if(splitDoubleDot.length == 1)
                            cmdName = splitEquals[0];
                        switch(cmdName) {
                            case CMD_HELP:
                                System.out.println(getCommandHelp());
                                askedForHelp = true;
                                break;
                            case CMD_LOG_LEVEL:
                                if(splitEquals.length == 2) {
                                    try {
                                        settings.setDefaultLogLevel(Level.parse(splitEquals[1]));
                                    } catch (IllegalArgumentException e) {
                                        throw new WarCommandException("Invalid log level : " + splitEquals[1]);
                                    }
                                } else {
                                    throw new WarCommandException("Invalid argument syntax : " + currentArg);
                                }
                                break;
                            case CMD_NB_AGENT_OF_TYPE:
                                if(splitEquals.length == 2) {
                                    try {
                                        WarAgentType agentType = WarAgentType.valueOf(splitEquals[0]);
                                        try {
                                            int nbAgent = Integer.parseInt(splitEquals[1]);
                                            settings.setNbAgentOfType(agentType, nbAgent);
                                        } catch (NumberFormatException e) {
                                            throw new WarCommandException("Invalid integer : " + splitEquals[1]);
                                        }
                                    } catch (IllegalArgumentException e) {
                                        throw new WarCommandException("Unknown agent type : " + splitEquals[0]);
                                    }
                                } else {
                                    throw new WarCommandException("Invalid argument syntax : " + currentArg);
                                }
                                break;
                            case CMD_FOOD_APPEARANCE_RATE:
                                if(splitEquals.length == 2) {
                                    try {
                                        settings.setFoodAppearanceRate(Integer.parseInt(splitEquals[1]));
                                    } catch (NumberFormatException e) {
                                        throw new WarCommandException("Invalid integer : " + splitEquals[1]);
                                    }
                                } else {
                                    throw new WarCommandException("Invalid argument syntax : " + currentArg);
                                }
                                break;
                            case CMD_GAME_MODE:
                                if(splitEquals.length == 2) {
                                    try {
                                        WarGameMode gameMode = WarGameMode.valueOf(splitEquals[1]);
                                        settings.setGameMode(gameMode);
                                    } catch (IllegalArgumentException e) {
                                        throw new WarCommandException("Unknown game mode : " + splitEquals[1]);
                                    }
                                } else {
                                    throw new WarCommandException("Invalid argument syntax : " + currentArg);
                                }
                                break;
                            default:
                                throw new WarCommandException("Invalid argument : " + currentArg);
                        }
                    } else {
                        if(currentArg.endsWith(SituationLoader.SITUATION_FILES_EXTENSION)) {
                            settings.setSituationLoader(new SituationLoader(new File(currentArg)));
                        } else {
                            selectedTeams.add(currentArg);
                        }
                    }
                }

                if(! askedForHelp)
                    new WarMain(settings, selectedTeams.toArray(new String[]{}));
            } catch (WarCommandException e) {
                System.err.println(e.getMessage());
            }
        }
	}

    private static String getCommandHelp() {
        return "Use : "+CMD_NAME+" [OPTION]... TEAM_NAME...\n" +
                " Or : "+CMD_NAME+" WAR_SITUATION_FILE\n" +
                "Launch a Warbot simulation or load and start a Warbot simulation from a situation file (*" + SituationLoader.SITUATION_FILES_EXTENSION + ")\n\n" +
                "Available options :\n" +
                "\t"+CMD_LOG_LEVEL+"=LEVEL\t\tuse LEVEL as log level. LEVEL in [SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST]\n" +
                "\t"+CMD_NB_AGENT_OF_TYPE+":AGENT_TYPE=NB\t\tset NB as number of agents of type AGENT_TYPE created at game start. AGENT_TYPE in " + Arrays.asList(WarAgentType.values()) + "\n" +
                "\t"+CMD_FOOD_APPEARANCE_RATE+"=RATE\t\t\tnew food will appear every RATE ticks\n" +
                "\t"+CMD_GAME_MODE+"=MODE\t\t\tset MODE as game mode. MODE in " + Arrays.asList(WarGameMode.values()) + "\n" +
                "\t"+CMD_HELP+"\t\t\t\t\tdisplay this help\n";
    }

    @Override
    public void onNewTeamAdded(Team newTeam) {

    }

    @Override
    public void onTeamRemoved(Team removedTeam) {

    }

    @Override
    public void onGameOver() {
        if(launcherInterface != null)
            launcherInterface.displayGameResults(game);
        else { // Si la simulation a été lancée depuis la ligne de commande
            String finalTeams = "";
            for(Team team : game.getPlayerTeams()) {
                finalTeams += team.getName() + ", ";
            }
            finalTeams = finalTeams.substring(0, finalTeams.length() - 2);
            if (game.getPlayerTeams().size() == 1) {
                System.out.println("Victoire de : " + finalTeams);
            } else {
                System.out.println("Ex-Aequo entre les équipes : " + finalTeams);
            }
            game.stopGame();
        }
    }

    @Override
    public void onGameStopped() {
        game.removeWarGameListener(this);
        settings.prepareForNewGame();
        launcherInterface.setVisible(true);
    }

    @Override
    public void onGameStarted() {
        loadingDialog.dispose();
    }

    protected static class Shared {
		private static WarGame game;
		private static Map<String, Team> availableTeams;
		
		public static WarGame getGame() {
			return game;
		}
		
		public static Map<String, Team> getAvailableTeams(){
			return availableTeams;
		}
	}

    protected static class WarCommandException extends Exception {

        public WarCommandException(String message) {
            super(message + "\nEnter \""+CMD_NAME+" "+CMD_HELP+"\" for more informations.");
        }

    }

}
