package edu.warbot.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

public class WarIOTools {

	public static byte[] toByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while (reads != -1) {
			baos.write(reads);
			reads = is.read();
		}
		return baos.toByteArray();
	}
	
	public static ImageIcon loadImage(String imagePath) {
		try {
			return new ImageIcon(new URL("file:" + imagePath));
		} catch (MalformedURLException e) {
			System.err.println("Erreur lors de l'ouverture d'une image : " + imagePath);
			e.printStackTrace();
		}
		return new ImageIcon();
	}

}
