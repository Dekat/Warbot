package edu.warbot.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.warbot.tools.WarXmlReader;

/**
 * Classe servant à la lecture du document XML définit par l'utilisateur, contenant les informations sur son équipe.
 */
public class TeamXMLReader {

	private static final String mainNodPath = "/Team";
	private static final String teamNameNodePath = mainNodPath + "/Name";
	private static final String teamDescriptionNodePath = mainNodPath + "/Description";
	private static final String iconPathNodePath = mainNodPath + "/IconPath";
	private static final String soundPathNodePath = mainNodPath + "/SoundPath";
	
	private Document _document = null;

	/**
	 * Méthode permettant d'ouvrir le fichier XML et d'obtenir l'élément racine.
	 */
	public void Ouverture(InputStream input) {
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
	public String getIconeName() {
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
}