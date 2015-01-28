package edu.warbot.FSMEditor.dialogues;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jfree.ui.tabbedui.VerticalLayout;

import edu.warbot.FSMEditor.Configuration;

public class DialogueStateSetting extends AbstractDialogue{

	private static final long serialVersionUID = 1L;
	
	public DialogueStateSetting(JFrame f){
		super(f, true);
		
		this.setTitle("State settings");
		this.setSize(new Dimension(250, 150));
		
		JPanel panel = new JPanel(new VerticalLayout());
		this.setContentPane(panel);

		fieldNameEtat = new JTextField();
		comboxPlan = new JComboBox<>(Configuration.PLAN);

		console = new JLabel();
		buttonOk = new JButton("Ok");
		
		JPanel panelName = new JPanel(new GridLayout(2, 2));
		panelName.setBorder(new TitledBorder("State settings"));
		panel.add(panelName);
		
		panelName.add(add(new JLabel("Name")));
		panelName.add(fieldNameEtat);
		
		
		panelName.add(new JLabel("Plan"));
		panelName.add(comboxPlan);
		
		JPanel panelBottom = new JPanel();
		panel.add(panelBottom);
		
		panelBottom.add(console);
		panelBottom.add(buttonOk);
		
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventValider();
			}
		});

		this.setVisible(true);
	}
	
	private void eventValider(){
		
		if(this.fieldNameEtat.getText().isEmpty())
			console.setText("Entrer un nom pour l'�tat");
		else{
			checkValidity();
			this.dispose();
		}
	}
	
	private void checkValidity() {
		if(this.fieldNameEtat.getText().isEmpty() == false && this.comboxPlan.getSelectedIndex() != -1)
			super.isValide = true;
	}

	public String getNom(){
		return this.fieldNameEtat.getText();
	}
	
	public String getPlanName(){//TODO warning
		return (String) comboxPlan.getSelectedItem();
	}
	
	JComboBox<String> comboxPlan;
	JTextField fieldNameEtat;
	
	JLabel console;
	JButton buttonOk;

}
