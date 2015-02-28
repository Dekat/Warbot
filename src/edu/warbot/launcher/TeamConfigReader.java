package edu.warbot.launcher;

import edu.warbot.FSMEditor.xmlParser.FsmXmlReader;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe servant à la lecture du document YAML définit par l'utilisateur, contenant les informations sur son équipe.
 */
public class TeamConfigReader {

    public static final String FILE_NAME = "config.yml";

    private Map<String, Object> config;

	/**
	 * Méthode permettant d'ouvrir le fichier YAML et d'obtenir l'élément racine.
	 */
	public void load(InputStream input) {
        Yaml yaml = new Yaml();
        config = (Map<String, Object>) yaml.load(input);
	}

	/**
	 * Methode permettant de recuperer le nom de l'equipe.
	 * 
	 * @return {@code String} - le nom de l'equipe definit par l'utilisateur
	 */
	public String getTeamName() {
        if (config.containsKey("Name"))
            return (String) config.get("Name");
        else
            return "NoName";
	}

	public String getTeamDescription() {
        if (config.containsKey("Description"))
            return (String) config.get("Description");
        else
            return "";
	}
	
	/**
	 * Methode permettant de recuperer le nom de l'icone definit par 
	 * l'utilisateur.
	 * 
	 * @return {@code String} - le nom de l'icone 
	 */
	public String getIconPath() {
        if (config.containsKey("IconPath"))
            return (String) config.get("IconPath");
        else
            return "";
	}

	/**
	 * Methode permettant de recuperer le nom de la musique de victoire
	 * definit par l'utilisateur.
	 * 
	 * @return {@code String} - le nom de la musique de victoire
	 */
	public String getSoundPath() {
        if (config.containsKey("SoundPath"))
            return (String) config.get("SoundPath");
        else
            return "";
	}

    /**
     * Methode permettant de recuperer le nom du package où se trouvent les classes définis par l'utilisateur
     *
     * @return {@code String} - le package où se trouvent les classes de cerveaux
     */
    public String getBrainsPackageName() {
        if (config.containsKey("BrainsPackage"))
            return (String) config.get("BrainsPackage");
        else
            return "";
    }

    /**
	 * Methode permettant de recuperer sous forme d'une HashMap les noms
	 * des classes definis par l'utilisateur (constituant la valeur). La
	 * cle est represente par le type d'agent sous forme de chaine de 
	 * caractere. 
	 * 
	 * @return {@code Map<String, String>} - une Map avec comme clé
	 * le type d'agent et comme valeur le nom de la classe correspondante.
	 */
	public Map<String, String> getBrainControllersClassesNameOfEachAgentType() {
        if (config.containsKey("AgentsBrainClasses"))
            return (Map<String, String>) config.get("AgentsBrainClasses");
        else
            return new HashMap<>();
    }

	/**
	 * Méthode que li le fichier de configuration et renvoi si l'équipe est une FSM ou non
	 * Pour l'instant une équipe doit entièrement etre une FSM ou non (ça ne peut pas etre dépendant de chaque agent)
	 * Par la suite il faudra modifier le fichier XML et mettre pour chaque agent si il est programmé avec un brain
	 * ou si il est programmé avec un fichier de configuration FSM
	 * @return
	 */
	public boolean isFSMTeam() {
        if (config.containsKey("FromFsmEditor"))
            return (boolean) config.get("FromFsmEditor");
        else
            return false;
	}

	public String getFSMConfigurationFileName() {
		return FsmXmlReader.xmlConfigurationDefaultFilename;
	}
}