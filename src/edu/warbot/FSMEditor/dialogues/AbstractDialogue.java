package edu.warbot.FSMEditor.dialogues;

import javax.swing.JDialog;
import javax.swing.JFrame;

import edu.warbot.FSMEditor.View;

public class AbstractDialogue extends JDialog{
	
	private static final long serialVersionUID = 1L;

	boolean isValide = false;
	
	public AbstractDialogue(JFrame f, boolean modal) {
		super(f, modal);
		this.setLocationRelativeTo(f);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	public boolean isValideComponent(){
		return isValide;
	}

}
