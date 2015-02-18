package edu.warbot.launcher;

import edu.warbot.FSMEditor.xmlParser.FsmXmlReader;
import edu.warbot.tools.WarXmlReader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Classe servant à la lecture du document XML définit par l'utilisateur, contenant les informations sur son équipe.
 */
public class TeamXMLReader {

	private static final String mainNodPath = "/Team";
	private static final String teamNameNodePath = mainNodPath + "/Name";
	private static final String teamDescriptionNodePath = mainNodPath + "/Description";
	private static final String iconPathNodePath = mainNodPath + "/IconPath";
	private static final String soundPathNodePath = mainNodPath + "/SoundPath";
	private static final String isFSMNodePath = mainNodPath + "/FSMImplementtion";
    private static final String brainsPackageNodePath = mainNodPath + "/BrainsPackage";

	private Document _document = null;

	/**
	 * Méthode permettant d'ouvrir le fichier XML et d'obtenir l'élément racine.
	 */
	public void load(InputStream input) {
		try {
			_document = WarXmlReader.getDocumentFromInputStream(input);
		} catch (ParserConfigurationException e) {
			System.err.println("Error when trying to create a document builder.");
		} catch (SAXException e) {
			System.err.println("Error when trying to read \"" + input + "\", default values will be used.");
		} catch (IOException e) {
			System.err.println("Error when trying to open \"" + input + "\", default values will be used.");
		}
	}

	/**
	 * Methode permettant de recuperer le nom de l'equipe.
	 * 
	 * @return {@code String} - le nom de l'equipe definit par l'utilisateur
	 * @throws XPathExpressionException 
	 */
	public String getTeamName() {
		return WarXmlReader.getFirstStringResultOfXPath(_document, teamNameNodePath);
	}

	public String getTeamDescription() {
		return WarXmlReader.getFirstStringResultOfXPath(_document, teamDescriptionNodePath);
	}
	
	/**
	 * Methode permettant de recuperer le nom de l'icone definit par 
	 * l'utilisateur.
	 * 
	 * @return {@code String} - le nom de l'icone 
	 * @throws XPathExpressionException 
	 */
	public String getIconName() {
		return WarXmlReader.getFirstStringResultOfXPath(_document, iconPathNodePath);
	}

	/**
	 * Methode permettant de recuperer le nom de la musique de victoire
	 * definit par l'utilisateur.
	 * 
	 * @return {@code String} - le nom de la musique de victoire
	 */
	public String getSoundName() {
		return WarXmlReader.getFirstStringResultOfXPath(_document, soundPathNodePath);
	}

    /**
     * Methode permettant de recuperer le nom du package où se trouvent les classes définis par l'utilisateur
     *
     * @return {@code String} - le package où se trouvent les classes de cerveaux
     */
    public String getBrainsPackageName() {
        return WarXmlReader.getFirstStringResultOfXPath(_document, brainsPackageNodePath);
    }

    /**
	 * Methode permettant de recuperer sous forme d'une HashMap les noms
	 * des classes definis par l'utilisateur (constituant la valeur). La
	 * cle est represente par le type d'agent sous forme de chaine de 
	 * caractere. 
	 * 
	 * @return {@code HashMap<String, String>} - une HashMap avec comme cle
	 * le type d'agent et comme valeur le nom de la classe correspondante.
	 */
	public HashMap<String, String> getBrainControllersClassesNameOfEachAgentType() {
		return WarXmlReader.getNodesFromXPath(_document, "//AgentsBrainControllers");
	}

	/**
	 * Méthode que li le fichier de configuration et renvoi si l'équipe est une FSM ou non
	 * Pour l'instant une équipe doit entièrement etre une FSM ou non (ça ne peut pas etre dépendant de chaque agent)
	 * Par la suite il faudra modifier le fichier XML et mettre pour chaque agent si il est programmé avec un brain
	 * ou si il est programmé avec un fichier de configuration FSM
	 * @return
	 */
	public boolean isFSMTeam() {
		return Boolean.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, isFSMNodePath));
	}

	public String getFSMConfigurationFileName() {
		return FsmXmlReader.xmlConfigurationDefaultFilename;
	}
}