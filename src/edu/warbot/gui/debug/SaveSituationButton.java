package edu.warbot.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import edu.warbot.launcher.SituationLoader;
import madkit.action.SchedulingAction;
import madkit.message.SchedulingMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import turtlekit.agr.TKOrganization;
import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarProjectile;
import edu.warbot.game.WarGame;
import edu.warbot.game.Team;
import edu.warbot.gui.toolbar.WarToolBar;
import edu.warbot.tools.WarXmlWriter;

@SuppressWarnings("serial")
public class SaveSituationButton extends JButton implements ActionListener {

	private WarToolBar _toolBar;
	private WarGame game;

	public SaveSituationButton(WarToolBar toolBar) {
		super("Sauvegarder la situation");
		_toolBar = toolBar;
		this.game = _toolBar.getViewer().getGame();

//		setIcon(WarViewer.getIconFromGuiImages("save.png"));
//		setSelectedIcon(WarViewer.getIconFromGuiImages("save_s.png"));
//		setToolTipText("Sauvegarder la situation");
//		setMnemonic('s');
//		setPreferredSize(new Dimension(32, 32));
//		setBorder(BorderFactory.createEmptyBorder());
//		setContentAreaFilled(false);

		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_toolBar.getViewer().sendMessage(_toolBar.getViewer().getCommunity(), TKOrganization.ENGINE_GROUP,
				TKOrganization.SCHEDULER_ROLE, new SchedulingMessage(SchedulingAction.PAUSE));

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "*" + SituationLoader.SITUATION_FILES_EXTENSION;
			}
			
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(SituationLoader.SITUATION_FILES_EXTENSION);
			}
		});
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String fileName = file.toString();
			if (!fileName.endsWith(SituationLoader.SITUATION_FILES_EXTENSION))
				file = new File(fileName + SituationLoader.SITUATION_FILES_EXTENSION);
			if (saveSituation(file))
				JOptionPane.showMessageDialog(this, "Situation sauvegardée dans " + file.getAbsolutePath(), "Sauvegarde effectuée", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde dans " + file.getAbsolutePath(), "Sauvegarde échouée", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean saveSituation(File file) {
		try {
			// Création du document
			Document doc;
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("WarSituation");
			doc.appendChild(rootElement);

			Element teams = doc.createElement("Teams");
			rootElement.appendChild(teams);
			for (Team t : game.getAllTeams()) {
				Element currentTeam = doc.createElement("Team");
				rootElement.appendChild(currentTeam);
				currentTeam.appendChild(WarXmlWriter.createTextElement(doc,
						"Name", t.getName()));
				for (WarAgent a : t.getAllAgents()) {
					if (! (a instanceof WarProjectile)) {
						Element currentAgent = doc.createElement("WarAgent");

						currentAgent.appendChild(WarXmlWriter.createTextElement(doc,
								"Type", a.getClass().getSimpleName()));
						currentAgent.appendChild(WarXmlWriter.createTextElement(doc,
								"xPosition", String.valueOf(a.getX())));
						currentAgent.appendChild(WarXmlWriter.createTextElement(doc,
								"yPosition", String.valueOf(a.getY())));
						currentAgent.appendChild(WarXmlWriter.createTextElement(doc,
								"Heading", String.valueOf(a.getHeading())));

						if (a instanceof ControllableWarAgent) {
							currentAgent.appendChild(WarXmlWriter.createTextElement(doc,
									"ViewDirection", String.valueOf(((ControllableWarAgent) a).getViewDirection())));
							currentAgent.appendChild(WarXmlWriter.createTextElement(doc,
									"Health", String.valueOf(((ControllableWarAgent) a).getHealth())));
							currentAgent.appendChild(WarXmlWriter.createTextElement(doc,
									"NbElementsInBag", String.valueOf(((ControllableWarAgent) a).getNbElementsInBag())));
						}
						else if (a instanceof WarProjectile) {
							currentAgent.appendChild(WarXmlWriter.createTextElement(doc,
									"CurrentAutonomy", String.valueOf(String.valueOf(((WarProjectile) a).getCurrentAutonomy()))));
						}
						currentTeam.appendChild(currentAgent);
					}
				}
				teams.appendChild(currentTeam);
			}

			// Sauvegarde du document
			WarXmlWriter.saveXmlFile(doc, file);

			return true;
		} catch (ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException | IOException e) {
			System.err.println("Erreur lors de la création du fichier XML.");
			e.printStackTrace();
		}
		return false;
	}

}
