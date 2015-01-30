package edu.warbot.FSMEditor.dialogues;

import javax.swing.JDialog;

public class AbstractDialogue extends JDialog{
	
	private static final long serialVersionUID = 1L;

	boolean isValide = false;
	
	public AbstractDialogue() {
		super();
		this.setModal(true);
		//TODO il faut que le modal à true pour que la fenetre soit bloquante ?
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	public boolean isValideComponent(){
		return isValide;
	}

}
