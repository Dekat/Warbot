
package edu.warbot.gui.launcher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import edu.warbot.agents.enums.WarAgentCategory;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Game;
import edu.warbot.game.Team;
import edu.warbot.game.WarGameMode;
import edu.warbot.gui.GuiIconsLoader;
import edu.warbot.launcher.Simulation;
import edu.warbot.launcher.WarConfig;
import edu.warbot.launcher.WarLauncher;

@SuppressWarnings("serial")
public class WarLauncherInterface extends JFrame implements Observer {

	public static final String TEXT_ADDED_TO_DUPLICATE_TEAM_NAME = "_bis";

	// Eléments de l'interface
	private HashMap<WarAgentType, NbWarAgentSlider> sliderNbAgents;
	private HashMap<WarAgentType, NbWarAgentSlider> _foodCreationRates;

	private PnlTeamSelection _pnlSelectionTeam1;
	private PnlTeamSelection _pnlSelectionTeam2;

	private JButton boutonQuitter;
	private JButton boutonValider;

	private JComboBox<String> cbLogLevel;

	private JCheckBox _enableEnhancedGraphism;
	private LoadingDialog _loadingDialog;

	public WarLauncherInterface() {
		super("Warbot 3D");

		/* *** Fenêtre *** */
		setSize(1000, 600);
		setMinimumSize(new Dimension(600, 400));
		setLocationRelativeTo(null);
		setIconImage(GuiIconsLoader.getLogo("iconLauncher.png").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		setContentPane(mainPanel);

		// TODO
		setJMenuBar(new InterfaceLauncherMenuBar(this));

		/* *** Haut : Titre *** */
		JPanel panelTitre = new JPanel();
		JLabel imageTitre = new JLabel(GuiIconsLoader.getWarbotLogo());
		panelTitre.add(imageTitre);
		mainPanel.add(panelTitre, BorderLayout.NORTH);

		/* *** Droite : Options *** */
		JPanel panelOption = new JPanel();
		panelOption.setPreferredSize(new Dimension(300, panelOption.getPreferredSize().height));
		panelOption.setLayout(new BorderLayout());
		panelOption.setBorder(new TitledBorder("Options"));

		JPanel pnlNbUnits = new JPanel();
		pnlNbUnits.setLayout(new BoxLayout(pnlNbUnits, BoxLayout.Y_AXIS));
		sliderNbAgents = new HashMap<>();

		// Buildings, Soldiers et Workers
		for(WarAgentType a : WarAgentType.getAgentsOfCategories(WarAgentCategory.Building, WarAgentCategory.Soldier, WarAgentCategory.Worker)) {
			NbWarAgentSlider slider = new NbWarAgentSlider("Nombre de " + a.toString(),
					0, 30, WarConfig.getNbAgentsAtStartOfType(a.toString()), 1, 10);
			sliderNbAgents.put(a, slider);
			pnlNbUnits.add(slider);
		}

		// Ressources
		_foodCreationRates = new HashMap<>();
		for(WarAgentType a : WarAgentType.getAgentsOfCategories(WarAgentCategory.Resource)) {
			NbWarAgentSlider slider = new NbWarAgentSlider(a.toString() + " tous les x ticks",
					0, 500, 150, 1, 100);
			_foodCreationRates.put(a, slider);
			pnlNbUnits.add(slider);
		}

		panelOption.add(new JScrollPane(pnlNbUnits), BorderLayout.CENTER);

		JPanel panelAdvanced = new JPanel();
		panelAdvanced.setLayout(new BorderLayout());
		panelAdvanced.setBorder(new TitledBorder("Avancé"));
		String comboOption[] = {"ALL", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST"};
		this.cbLogLevel = new JComboBox<String>(comboOption);
		cbLogLevel.setSelectedItem("WARNING");
		panelAdvanced.add(new JLabel("Niveau de détail des logs"), BorderLayout.NORTH);
		panelAdvanced.add(cbLogLevel, BorderLayout.CENTER);
		// graphisme
		_enableEnhancedGraphism = new JCheckBox("Charger la vue 2D isométrique", false);
		panelAdvanced.add(_enableEnhancedGraphism, BorderLayout.SOUTH);

		panelOption.add(panelAdvanced, BorderLayout.SOUTH);

		mainPanel.add(panelOption, BorderLayout.EAST);

		/* *** Bas : Boutons *** */
		JPanel panelBas = new JPanel();

		boutonValider = new JButton("Valider");
		boutonValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSimu(false);
			}
		});
		getRootPane().setDefaultButton(boutonValider);
		panelBas.add(boutonValider);

		boutonQuitter = new JButton("Quitter");
		boutonQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(NORMAL);
			}
		});
		panelBas.add(boutonQuitter);

		mainPanel.add(panelBas, BorderLayout.SOUTH);

		/* *** Centre : Choix du mode et sélection des équipes *** */
		JTabbedPane tabbedPaneMillieu = new JTabbedPane();
		tabbedPaneMillieu.add(creerPanelModeClassic(), "Mode Duel");
		tabbedPaneMillieu.add(creerPanelModeMulti(), "Mode Tournoi");

		mainPanel.add(tabbedPaneMillieu, BorderLayout.CENTER);

		setVisible(true);
	}

	private void initParameters() {
		/* Application des paramètres de la partie */
		Simulation.getInstance().setDefaultLogLevel((Level.parse((String) cbLogLevel.getSelectedItem())));

		for (WarAgentType agent : WarAgentType.values()) {
			NbWarAgentSlider slider = sliderNbAgents.get(agent);
			if (slider != null)
				Simulation.getInstance().setNbAgentOfType(agent, slider.getSelectedValue());
		}
		Simulation.getInstance().setFoodAppearanceRate(_foodCreationRates.get(WarAgentType.WarFood).getSelectedValue());

		Simulation.getInstance().setGameMode(WarGameMode.Duel); // TODO varier selon le choix dans l'interface

		Simulation.getInstance().setEnabledEnhancedGraphism(_enableEnhancedGraphism.isSelected());
	}

	public void startSimu(boolean fromXmlSituationFile) {
		_loadingDialog = new LoadingDialog("Lancement de la simulation...");
		_loadingDialog.setVisible(true);		

		initParameters();

		if (! fromXmlSituationFile) {
			/* Mise en place du jeu */
			Team team1 = _pnlSelectionTeam1.getSelectedTeam();
			Team team2 = _pnlSelectionTeam2.getSelectedTeam();

			// Si c'est les mêmes équipes, on la duplique
			if (team1.equals(team2)) {
				team2 = Team.duplicate(team1, team1.getName() + TEXT_ADDED_TO_DUPLICATE_TEAM_NAME);
			}

			// On ajoute les équipes au jeu
			Game.getInstance().addPlayerTeam(team1, WarLauncher.TEAM_COLORS[0]);
			Game.getInstance().addPlayerTeam(team2, WarLauncher.TEAM_COLORS[1]);
		}
		// TODO Traitement XML avant de lancer la simu
		setVisible(false);
		WarLauncher.main();
		Game.getInstance().addObserver(this);
	}

	private JPanel creerPanelModeMulti() {
		JPanel toReturn = new JPanel(new GridLayout());

		// TODO

		return toReturn;
	}

	private JPanel creerPanelModeClassic() {
		JPanel toReturn = new JPanel(new GridLayout(1, 2));

		_pnlSelectionTeam1 = new PnlTeamSelection("Choix de l'équipe 1");
		toReturn.add(new JScrollPane(_pnlSelectionTeam1));
		_pnlSelectionTeam2 = new PnlTeamSelection("Choix de l'équipe 2");
		toReturn.add(new JScrollPane(_pnlSelectionTeam2));

		return toReturn;
	}

	public JCheckBox getEnableEnhancedGraphism() {
		return _enableEnhancedGraphism;
	}

	public static void main(String[] args) {
		LoadingDialog loadDial = new LoadingDialog("Chargement des équipes...");
		loadDial.setVisible(true);

		// On initialise la liste des équipes existantes dans le dossier "teams"
		WarLauncher.getTeamsFromDirectory();

		// On vérifie qu'au moins une équipe a été chargé
		if (Simulation.getInstance().getNbAvailableTeams() > 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new WarLauncherInterface().setVisible(true);
				}
			});
		} else {
			JOptionPane.showMessageDialog(null, "Aucune équipe n'a été trouvé dans le dossier \""+ WarLauncher.TEAMS_DIRECTORY_NAME + "\"",
					"Aucune équipe", JOptionPane.ERROR_MESSAGE);
		}

		loadDial.dispose();
	}

	@Override
	public void update(Observable o, Object arg) {
		Integer reason = (Integer) arg;
		if ((reason == Game.UPDATE_TEAM_REMOVED && Game.getInstance().getPlayerTeams().size() <= 1) ||
				reason == Game.GAME_STOPPED) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					endTheGame();
				}
			});
		} else if (reason == Game.SIMULATION_CLOSED) {
			dispose();
		} else if (reason == Game.GAME_LAUNCHED) {
			_loadingDialog.dispose();
		}
	}

	private void endTheGame() {
		// JPanel d'affichage des équipes gagnantes (insérées dans le JOptionPane)
		JPanel pnlResult = new JPanel(new BorderLayout());
		JPanel pnlWinners = new JPanel(new FlowLayout());
		for (Team t : Game.getInstance().getPlayerTeams())
			pnlWinners.add(new JLabel(t.getName(), t.getImage(), JLabel.CENTER));
		pnlResult.add(pnlWinners, BorderLayout.CENTER);

		// On afficher le JDialog
		// Cas où il y n'y a plus qu'une équipe en jeu
		if (Game.getInstance().getPlayerTeams().size() == 1) {
			// On la déclare vainqueur
			pnlResult.add(new JLabel("Victoire de :"), BorderLayout.NORTH);
		} else {
			// Sinon il y a ex-aequo !
			pnlResult.add(new JLabel("Ex-Aequo entre les équipes :"), BorderLayout.NORTH);
		}
		JOptionPane.showMessageDialog(this, pnlResult, "Fin du jeu !", JOptionPane.PLAIN_MESSAGE);

		// On stoppe la simulation et raffiche la fenêtre de paramétrage de la partie
		// On réinitialise le jeu
		Game.getInstance().restartNewGame();
		// On arrête d'écouter
		Game.getInstance().deleteObserver(this);
		// On affiche l'interface du launcher
		setVisible(true);
	}
}
