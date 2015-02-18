package edu.warbot.launcher;

import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class UserPreferences {

    static private String userSettingsFilePath = "config" + File.separatorChar + "preferences.yml";
    static private Map<String, Object> settings = null;

    static {
        try {
            InputStream input = new FileInputStream(new File(userSettingsFilePath));
            Yaml yaml = new Yaml();
            settings = (Map<String, Object>) yaml.load(input);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Le fichier des préférences de l'utilisateur est introuvable.", "Fichier manquant", JOptionPane.ERROR_MESSAGE);
            // TODO Générer un fichier de configuration par défaut
        }
    }

    public static Map<String, String> getTeamsSourcesFolders() {
        if (settings.containsKey("teamsSourcesFolders"))
            return (Map<String, String>) settings.get("teamsSourcesFolders");
        else
            return new HashMap<>();
    }

    public static int getNbAgentsAtStartOfType(String agentType) {
        Map<String, Integer> nbAgentsAtStart = (Map<String, Integer>) settings.get("NbAgentsAtStart");
        if (nbAgentsAtStart.containsKey(agentType))
            return nbAgentsAtStart.get(agentType);
        else
            return 0;
    }

    public static int getFoodAppearanceRate() {
        try {
            return (int) settings.get("FoodAppearanceRate");
        } catch (NumberFormatException e) {
            System.err.println("Wrong number in user preferences for key FoodAppearanceRate");
            return 150;
        }
    }

    public static Level getLoggerLevel() {
        String strLevel = (String) settings.get("DefaultLoggerLevel");
        try {
            return Level.parse(strLevel);
        } catch (IllegalArgumentException e) {
            System.err.println("Error : the logger level \"" + strLevel + "\" doesn't exist. The logger level will be set to WARNING instead.");
            return Level.WARNING;
        }
    }

}
