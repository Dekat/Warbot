package edu.warbot.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.warbot.agents.percepts.InRadiusPerceptsGetter;
import edu.warbot.agents.percepts.PerceptsGetter;
import edu.warbot.tools.WarXmlReader;


public class WarConfig {

	public static final String AGENT_CONFIG_HITBOX_RADIUS = "HitboxRadius";
	public static final String AGENT_CONFIG_ANGLE_OF_VIEW = "AngleOfView";
	public static final String AGENT_CONFIG_DISTANCE_OF_VIEW = "DistanceOfView";
	public static final String AGENT_CONFIG_COST = "Cost";
	public static final String AGENT_CONFIG_MAX_HEALTH = "MaxHealth";
	public static final String AGENT_CONFIG_BAG_SIZE = "BagSize";
	public static final String AGENT_CONFIG_SPEED = "Speed";
	public static final String AGENT_CONFIG_TICKS_TO_RELOAD = "TicksToReload";
	
	public static final String PROJECTILE_CONFIG_EXPLOSION_RADIUS = "ExplosionRadius";
	public static final String PROJECTILE_CONFIG_DAMAGE = "Damage";
	public static final String PROJECTILE_CONFIG_AUTONOMY = "Autonomy";
	
	public static final String RESOURCE_WARFOOD_CONFIG_HEALTH_GIVEN = "HealthGived";
	public static final String GROUND_CONFIG_COLOR = "Color";
	
	private static final String FILE_NAME = "DefaultGame.xml";

	static private String _filePath = "config" + File.separatorChar + FILE_NAME;
	static private Document _document = null;

	static {
		try {
			_document = WarXmlReader.openXmlFile(_filePath);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Le fichier de configuration est introuvable.",
					"Fichier manquant", JOptionPane.ERROR_MESSAGE);
			// TODO G�n�rer un fichier de configuration par d�faut
		} catch (ParserConfigurationException e) {
			System.err.println("Error when trying to create a document builder.");
		} catch (SAXException e) {
			System.err.println("Error when trying to read \"" + _filePath + "\", default values will be used.");
		} catch (IOException e) {
			System.err.println("Error when trying to open \"" + _filePath + "\", default values will be used.");
		}
	}
	
	public static HashMap<String, String> getConfigOfControllableWarAgent(String agent) {
		return WarXmlReader.getNodesFromXPath(_document, "/Warbot/WarControllableAgents/" + agent);
	}
	
	public static HashMap<String, String> getConfigOfWarProjectile(String agent) {
		return WarXmlReader.getNodesFromXPath(_document, "/Warbot/WarProjectiles/" + agent);
	}

	public static HashMap<String, String> getConfigOfWarResource(String agent) {
		return WarXmlReader.getNodesFromXPath(_document, "/Warbot/WarResources/" + agent);
	}

	public static HashMap<String, String> getConfigOfWarGround(String agent) {
		return WarXmlReader.getNodesFromXPath(_document, "/Warbot/WarGrounds/" + agent);
	}

	public static Level getLoggerLevel() {
		String strLevel = WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/DefaultLoggerLevel");
		try {
			return Level.parse(strLevel);
		} catch (IllegalArgumentException e) {
			System.err.println("Error : the logger level \"" + strLevel + "\" doesn't exist. The logger level will be set to WARNING instead.");
			return Level.WARNING;
		}
	}
	
	public static int getMaxDistanceTake() {
		return Integer.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/WarResources/MaxDistanceTake"));
	}

	public static int getMaxDistanceGive() {
		return Integer.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/WarControllableAgents/MaxDistanceGive"));
	}
	
	public static int getFoodAppearanceRate() {
		return Integer.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/FoodAppearanceRate"));
	}
	
	public static int getNbResourcesAreasPerTeam() {
		return Integer.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/NbResourcesAreasPerTeam"));
	}	

	public static double getRadiusResourcesAreas() {
		return Double.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/RadiusResourcesAreas"));
	}	

	public static double getMaxDistanceOfResourcesAreasFromOwnerTeam() {
		return Double.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/MaxDistanceOfResourcesAreasFromOwnerTeam"));
	}

	public static boolean isOpenWorld() {
		return Boolean.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/OpenWorld"));
	}

	public static int getNbAgentsAtStartOfType(String agent) {
		String result = WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/NbAgentsAtStart/" + agent);
		if (result != "")
			return Integer.valueOf(result);
		else
			return 0;
	}
	
	@SuppressWarnings("unchecked")
	public static Class<? extends PerceptsGetter> getDefaultPerception() {
		String className = PerceptsGetter.class.getPackage().getName() + "." + WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/DefaultPerception");
		try {
			Class<? extends PerceptsGetter> perceptGetter = (Class<? extends PerceptsGetter>) Class.forName(className);
			return perceptGetter;
		} catch (IllegalArgumentException | SecurityException | ClassNotFoundException e) {
			System.err.println("Nom de classe invalide pour \"" + className + "\". InRadiusPerceptsGetter pris par d�faut.");
			return InRadiusPerceptsGetter.class;
		}
	}

	public static double getMaxHitBoxRadius() {
		double toReturn = 0;
		
		ArrayList<String> values = WarXmlReader.getAllStringResultOfXPath(_document, "//HitboxRadius");
		for (String s : values) {
			double v = Double.valueOf(s);
			if (v > toReturn)
				toReturn = v;
		}
		
		return toReturn;
	}
}
