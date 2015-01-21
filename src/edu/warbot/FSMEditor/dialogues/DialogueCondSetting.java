package edu.warbot.FSMEditor.dialogues;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jfree.ui.tabbedui.VerticalLayout;

import edu.warbot.FSMEditor.Configuration;
import edu.warbot.FSMEditor.Frame;

public class DialogueCondSetting extends JDialog{
	
	public DialogueCondSetting(Frame f) {
		super(f, true);
		
		this.setTitle("Condition Setting");
		this.setSize(new Dimension(322, 500));
		
		JPanel panelMain = new JPanel(new BorderLayout());
		panelMain.setBounds(10, 10, 10, 10);
		this.setContentPane(panelMain);
		
		//Top panel
		JPanel panelTop = new JPanel(new FlowLayout());
		panelMain.add(panelTop);
		panelTop.add(new JLabel("Conditions settings"));
		
		//Center Panel
		JPanel panelCenter = new JPanel(new VerticalLayout());
		JScrollPane panelScrollCenter = new JScrollPane(panelCenter);
		panelMain.add(panelScrollCenter, BorderLayout.CENTER);
		
		// general
		panelCenter.add(getPanelTitleAndType());
		
		// Action terminate
		panelCenter.add(this.getPanelActionTerminate());
		
		// Attribute check
		panelCenter.add(this.getPanelAttributeCheck());

		// Bottom layout
		JPanel panelBottom = new JPanel(new FlowLayout());
		panelMain.add(panelBottom, BorderLayout.SOUTH);
		
		console = new JLabel();
		buttonOk = new JButton("Ok");
		panelBottom.add(console);
		panelBottom.add(buttonOk);
		
		
		
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				valider();
			}
		});

		this.setVisible(true);
	}

	private Component getPanelTitleAndType() {
		JPanel panel = new JPanel(new GridLayout(2, 2));
		panel.setBorder(new TitledBorder("G�n�ral"));
		
		fieldNameCond = new JTextField();
		comboTypeCond = new JComboBox<String>(Configuration.CONDITION);
		
		panel.add(new JLabel("Name "));
		panel.add(fieldNameCond);
		panel.add(new JLabel("Type "));
		panel.add(comboTypeCond);
		
		return panel;
	}

	private Component getPanelActionTerminate() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Action terminate"));
		panel.add(new JLabel("<Action terminate> s'applique � l'�tat source"));
		return panel;
	}

	private JPanel getPanelAttributeCheck() {
		JPanel panel = new JPanel(new GridLayout(4, 2));
		panel.setBorder(new TitledBorder("Action terminate"));
		
		comboListAttribut = new JComboBox<String>(Configuration.ATTRIBUTES);
		fieldOperateur = new JTextField("  ");
		fieldValeurAttribut = new JTextField("     ");
		checkBoxPourcentage = new JCheckBox();
		
		panel.add(new JLabel("Attribut "));
		panel.add(comboListAttribut);
		panel.add(new JLabel("Operateur "));
		panel.add(fieldOperateur);
		panel.add(new JLabel("Valeur "));
		panel.add(fieldValeurAttribut);
		panel.add(new JLabel("Pourcentage "));
		panel.add(checkBoxPourcentage);
		
		return panel;
	}

	private void valider(){
		if(!this.fieldNameCond.getText().isEmpty())
			this.dispose();
		else
			console.setText("Veuillez entrer un nom");
	}
	
	public String getNom(){
		return this.fieldNameCond.getText();
	}
	
	public String getConditionType(){
		return (String)(comboTypeCond.getSelectedItem());
	}
	
	JComboBox<String> comboTypeCond;
	JTextField fieldNameCond;
	
	//Attribut Check
	JComboBox<String> comboListAttribut;
	JTextField fieldOperateur;
	JTextField fieldValeurAttribut;
	JCheckBox checkBoxPourcentage;
	
	JLabel console;
	
	JButton buttonOk;
	
}
