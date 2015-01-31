package edu.warbot.FSMEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.swing.text.html.HTMLDocument.Iterator;

import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleBrain;
import edu.warbot.FSMEditor.Modeles.ModeleCondition;
import edu.warbot.FSMEditor.Modeles.ModeleState;

public class FSMJarGenerator {

	Manifest manifest;
	JarFile jarFile;
	JarOutputStream jarStream;
	String fileName;
	Modele modele;

	ArrayList<Class> classForJar;

	public FSMJarGenerator(Modele modele, String fileName) {
		this.fileName = fileName;
		this.modele = modele;
		
	}
	
	public void pushConfigurationInJarFile(){

		//Generer les fichiers de configurations
		FSMXMLSaver configFile = new FSMXMLSaver();
		configFile.saveFSM(this.modele, "FSMconfiguration.xml");
		
		//Ouvre le fichier Jar
		try {
		
			FileInputStream is = new FileInputStream("FSMconfiguration.xml");
//			jarFile = new JarFile(this.fileName);
			
			jarStream = new JarOutputStream(
					new FileOutputStream(this.fileName));
			
			byte buffer[] = new byte[1000];
			is.read(buffer);
			
			JarEntry entry = new JarEntry("FSMconfiguration.xml");
			jarStream.putNextEntry(entry);
			jarStream.write(buffer);
			jarStream.closeEntry();
			
			jarStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void generateJarFile(){

		// Crée le manifest
		manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION,
				"1.0");

		// Ouvre le fichier jar
		try {
			jarStream = new JarOutputStream(
					new FileOutputStream(this.fileName), manifest);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * récupere la liste des fichiers class necessaire à la FSM (les états,
		 * plan, conditions...)
		 */
		classForJar = new ArrayList<Class>();

		// Récupère les plans
		for (ModeleBrain currentBrain : this.modele.getModeleBrains()) {
			for (ModeleState currentState : currentBrain.getStates()) {
				try {
					Class<?> c = Class.forName(currentState.getPlanName());
					// c.get
					addClass(c);
					addClass(c.getSuperclass());

				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}

		// Récupère les condition
		for (ModeleBrain currentBrain : this.modele.getModeleBrains()) {
			for (ModeleCondition currentCondition : currentBrain
					.getConditions()) {
				try {
					Class<?> c = Class.forName(currentCondition.getType());

					addClass(c);
					addClass(c.getSuperclass());

				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}

		// Ajoute toutes les classes dans le jar
		for (Class<?> c : classForJar) {
			try {
				JarEntry entry = new JarEntry(c.getName());
				jarStream.putNextEntry(new JarEntry(c.getName()));
			} catch (IOException e) {
				System.err.println("ERRER add " + c.getName()
						+ " class in jarFile");
				e.printStackTrace();
			}
		}

		// Ferme le fichier jar
		try {
			jarStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addClass(Class<?> c) {
		if (!classForJar.contains(c))
			classForJar.add(c);
	}

}
