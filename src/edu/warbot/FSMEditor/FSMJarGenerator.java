package edu.warbot.FSMEditor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSMEditor.Modele.Modele;
import edu.warbot.FSMEditor.Modele.ModeleBrain;
import edu.warbot.FSMEditor.Modele.ModeleState;

public class FSMJarGenerator {
	
	Manifest manifest;
	JarOutputStream jarStream;
	String fileName;
	Modele modele;
	
	public FSMJarGenerator(Modele modele, String fileName) {
		this.fileName = fileName;
		this.modele = modele;
		
		//Crée le manifest
		manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		
		//Ouvre le fichier jar
		try {
			jarStream = new JarOutputStream(new FileOutputStream(this.fileName), manifest);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/**
		 * récupere la liste des fichiers class necessaire à la FSM 
		 * (les états, plan, conditions...)
		 */
		//Met tout ce qu'il y a dans le package FSM
		
		//Récupère les plans
		for (ModeleBrain currentBrain: this.modele.getModeleBrains()) {
			for (ModeleState currentState : currentBrain.getStates()) {
				Class<?> c;
				
				try {
//					c = Class.forName("java.util.ArrayList");
//					System.out.println(ArrayList.class.getName());
//					c = Class.forName(ArrayList.class.getName());
					c = Class.forName(currentState.getPlanName());
					jarStream.putNextEntry(new ZipEntry(c.getName()));
					jarStream.putNextEntry(new ZipEntry(c.getSuperclass().getName()));
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("Impossible d'acceder au fichier " + fileName);
				}

			}
		}
		
		try {
			jarStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
