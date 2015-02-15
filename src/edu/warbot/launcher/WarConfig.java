package edu.warbot.launcher;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import edu.warbot.agents.Hitbox;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.CoordPolar;
import edu.warbot.tools.WarMathTools;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.warbot.agents.percepts.InRadiusPerceptsGetter;
import edu.warbot.agents.percepts.PerceptsGetter;
import edu.warbot.tools.WarXmlReader;


public class WarConfig {

	public static final String AGENT_CONFIG_ANGLE_OF_VIEW = "AngleOfView";
	public static final String AGENT_CONFIG_DISTANCE_OF_VIEW = "DistanceOfView";
	public static final String AGENT_CONFIG_COST = "Cost";
	public static final String AGENT_CONFIG_MAX_HEALTH = "MaxHealth";
	public static final String AGENT_CONFIG_BAG_SIZE = "BagSize";
	public static final String AGENT_CONFIG_SPEED = "Speed";
	public static final String AGENT_CONFIG_TICKS_TO_RELOAD = "TicksToReload";
    public static final String AGENT_CONFIG_MAX_REPAIRS_PER_TICK = "MaxRepairsPerTick";
	
	public static final String PROJECTILE_CONFIG_EXPLOSION_RADIUS = "ExplosionRadius";
	public static final String PROJECTILE_CONFIG_DAMAGE = "Damage";
	public static final String PROJECTILE_CONFIG_AUTONOMY = "Autonomy";
	
	public static final String RESOURCE_WARFOOD_CONFIG_HEALTH_GIVEN = "HealthGived";

	private static final String FILE_NAME = "DefaultGame.xml";

	static private String _filePath = "config" + File.separatorChar + FILE_NAME;
	static private Document _document = null;

	static {
		try {
			_document = WarXmlReader.openXmlFile(_filePath);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Le fichier de configuration est introuvable.",
					"Fichier manquant", JOptionPane.ERROR_MESSAGE);
			// TODO Générer un fichier de configuration par défaut
		} catch (ParserConfigurationException e) {
			System.err.println("Error when trying to create a document builder.");
		} catch (SAXException e) {
			System.err.println("Error when trying to read \"" + _filePath + "\", default values will be used.");
		} catch (IOException e) {
			System.err.println("Error when trying to open \"" + _filePath + "\", default values will be used.");
		}
	}

    public static HashMap<String, String> getConfigOfWarAgent(WarAgentType agentType) {
        return WarXmlReader.getNodesFromXPath(_document, "/Warbot/WarAgents/" + agentType.getCategory().name() + "/" + agentType.name());
    }

    public static Hitbox getHitboxOfWarAgent(WarAgentType agentType) {
        HashMap<String, String> shapeData = WarXmlReader.getNodesFromXPath(_document, "/Warbot/WarAgents/" + agentType.getCategory().name() + "/" + agentType.name() + "/Hitbox");
        Hitbox hitbox = null;
        double radius;
        CoordCartesian position, leftPosition, rightPosition, firstPosition, centerPosition;
        switch (shapeData.get("Shape")) {
            case "Square":
                double sideLength = Double.valueOf(shapeData.get("SideLength"));
                hitbox = new Hitbox(new Rectangle2D.Double(0, 0, sideLength, sideLength), sideLength, sideLength);
                break;
            case "Rectangle":
                double height = Double.valueOf(shapeData.get("Height"));
                double width = Double.valueOf(shapeData.get("Width"));
                hitbox = new Hitbox(new Rectangle2D.Double(0, 0, width, height), width, height);
                break;
            case "Circle":
                radius = Double.valueOf(shapeData.get("Radius"));
                hitbox = new Hitbox(new Ellipse2D.Double(0, 0, radius*2., radius*2.), radius*2., radius*2.);
                break;
            case "Triangle":
                radius = Double.valueOf(shapeData.get("Radius"));
                Path2D.Double triangle = new Path2D.Double();
                centerPosition = new CoordCartesian(radius, radius);
                firstPosition = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 0));
                triangle.moveTo(firstPosition.getX(), firstPosition.getY());
                leftPosition = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 220));
                triangle.lineTo(leftPosition.getX(), leftPosition.getY());
                rightPosition = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 140));
                triangle.lineTo(rightPosition.getX(), rightPosition.getY());
                triangle.lineTo(firstPosition.getX(), firstPosition.getY());
                hitbox = new Hitbox(triangle, rightPosition.getX() - leftPosition.getX(), rightPosition.getY() - firstPosition.getY());
                break;
            case "Diamond":
                radius = Double.valueOf(shapeData.get("Radius"));
                Path2D.Double diamond = new Path2D.Double();
                centerPosition = new CoordCartesian(radius, radius);
                firstPosition = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 270));
                diamond.moveTo(firstPosition.getX(), firstPosition.getY());
                position = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 0));
                diamond.lineTo(position.getX(), position.getY());
                position = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 90));
                diamond.lineTo(position.getX(), position.getY());
                position = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 180));
                diamond.lineTo(position.getX(), position.getY());
                diamond.lineTo(firstPosition.getX(), firstPosition.getY());
                hitbox = new Hitbox(diamond, radius*2., radius*2.);
                break;
            case "Arrow":
                radius = Double.valueOf(shapeData.get("Radius"));
                Path2D.Double arrow = new Path2D.Double();
                centerPosition = new CoordCartesian(radius, radius);
                firstPosition = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 0));
                arrow.moveTo(firstPosition.getX(), firstPosition.getY());
                leftPosition = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 220));
                arrow.lineTo(leftPosition.getX(), leftPosition.getY());
                arrow.lineTo(centerPosition.getX(), centerPosition.getY());
                rightPosition = WarMathTools.addTwoPoints(centerPosition, new CoordPolar(radius, 140));
                arrow.lineTo(rightPosition.getX(), rightPosition.getY());
                arrow.lineTo(firstPosition.getX(), firstPosition.getY());
                hitbox = new Hitbox(arrow, rightPosition.getX() - leftPosition.getX(), rightPosition.getY() - firstPosition.getY());
                break;
        }
        return hitbox;
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
		return Integer.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Game/MaxDistanceTake"));
	}

	public static int getMaxDistanceGive() {
		return Integer.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Game/MaxDistanceGive"));
	}

    public static int getMaxDistanceBuild() {
        return Integer.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Game/MaxDistanceBuild"));
    }

    public static double getRepairsMultiplier() {
        return Double.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Game/RepairsMultiplier"));
    }

    public static int getFoodAppearanceRate() {
		return Integer.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/FoodAppearanceRate"));
	}
	
	public static double getRadiusResourcesAreas() {
		return Double.valueOf(WarXmlReader.getFirstStringResultOfXPath(_document, "/Warbot/Simulation/DefaultStartParameters/RadiusResourcesAreas"));
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
			System.err.println("Nom de classe invalide pour \"" + className + "\". InRadiusPerceptsGetter pris par défaut.");
			return InRadiusPerceptsGetter.class;
		}
	}
}
