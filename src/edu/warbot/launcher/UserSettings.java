package edu.warbot.launcher;

import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Map;

public class UserSettings {

    private static final String SETTING_TEAMS_SOURCES_FOLDERS = "teamsSourcesFolders";

    private static final String SETTINGS_FILE_NAME = "settings.yml";

    static private String userSettingsFilePath = "config" + File.separatorChar + SETTINGS_FILE_NAME;
    static private Map<String, Object> settings = null;

    static {
        try {
            InputStream input = new FileInputStream(new File(userSettingsFilePath));
            Yaml yaml = new Yaml();
            settings = (Map<String, Object>) yaml.load(input);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Les paramètres de l'utilisateur sont introuvable.", "Fichier manquant", JOptionPane.ERROR_MESSAGE);
            // TODO Générer un fichier de configuration par défaut
        }
    }

    public static Map<String, String> getTeamsSourcesFolders() {
        return (Map<String, String>) settings.get(SETTING_TEAMS_SOURCES_FOLDERS);
    }
}
