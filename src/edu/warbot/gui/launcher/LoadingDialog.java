package edu.warbot.gui.launcher;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class LoadingDialog extends JFrame {

	public LoadingDialog(String message) {
		super("Chargement...");
		setSize(250, 100);

		JPanel content = new JPanel();
		content.setLayout(new GridLayout(2, 1));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		content.add(new JLabel(message, JLabel.CENTER));
		JPanel pnlProgressBar = new JPanel();
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		pnlProgressBar.add(progressBar);
		content.add(pnlProgressBar);
		
		add(content);
	}
	
}
