package edu.warbot.launcher;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.warbot.brains.WarBrain;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.gui.launcher.LoadingDialog;
import edu.warbot.gui.launcher.WarLauncherInterface;
import edu.warbot.tools.WarIOTools;
import javassist.*;

public class WarMain implements Observer {
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

	@Override
	public void update(Observable o, Object arg) {
		Integer reason = (Integer) arg;
		if ((reason == WarGame.UPDATE_TEAM_REMOVED && game.getPlayerTeams().size() <= 1) ||
				reason == WarGame.GAME_STOPPED) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					endGame();
				}
			});
		} else if (reason == WarGame.GAME_LAUNCHED) {
			loadingDialog.dispose();
		}
	}
	
	public void startGame() {
		loadingDialog = new LoadingDialog("Lancement de la simulation...");
		loadingDialog.setVisible(true);
		
		game = new WarGame(settings);
		Shared.game = game;

		new WarLauncher().executeLauncher();
		game.addObserver(this);
	}
	
	public void endGame() {
		launcherInterface.displayGameResults(game);
		game.deleteObserver(this);
		settings.prepareForNewGame();
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
					JarEntry currentEntry;
					Enumeration<JarEntry> entries = jarCurrentFile.entries();
					HashMap<String, JarEntry> allJarEntries = new HashMap<>();
					boolean configFileFound = false;
					while (entries.hasMoreElements()) {
						currentEntry = entries.nextElement();

						// Si c'est le fichier config.xml
						if (currentEntry.getName().endsWith("config.xml")) {
							// On le lit et on l'analyse grâce à la classe TeamXmlReader
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
						// On a maintenant tous les fichiers dans un tableau et le fichier de configuration a été analysé

						// On récupère le logo
						JarEntry logoEntry = allJarEntries.get(analXML.getIconeName());
						ImageIcon teamLogo = new ImageIcon(WarIOTools.toByteArray(jarCurrentFile.getInputStream(logoEntry)));
						// On change sa taille
						Image tmp = teamLogo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
						teamLogo = new ImageIcon(tmp);

						// On récupère le son
						// TODO ajouter le son aux équipes

						// On créé l'équipe
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
									(Class<? extends WarBrain>) classLoader.loadClass(classEntry.getName().replaceAll(".class", "").replaceAll("/", ".")));
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
	
	public Map<String, Team> getAvailableTeams() {
		return new HashMap<String, Team>(availableTeams);
	}
	
	public static void main(String[] args) {
		new WarMain();
	}
	
	// TODO Horrible class, change it ! Beurk !
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
}
