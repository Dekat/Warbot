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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jfree.ui.tabbedui.VerticalLayout;

import edu.warbot.FSMEditor.FSMSettings.ConditionEnum;
import edu.warbot.FSMEditor.FSMSettings.Settings;
import edu.warbot.FSMEditor.Views.ViewBrain;

public class DialogueCondSetting extends AbstractDialogue{
	
	private static final long serialVersionUID = 1L;

	public DialogueCondSetting(ViewBrain f) {
		super();
		
		this.setTitle("Condition Setting");
		this.setSize(new Dimension(322, 500));
		
		JPanel panelMain = new JPanel(new BorderLayout());
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
		panel.setBorder(new TitledBorder("Général"));
		
		fieldNameCond = new JTextField();
		comboTypeCond = new JComboBox<>(ConditionEnum.values());
		
		panel.add(new JLabel("Name "));
		panel.add(fieldNameCond);
		panel.add(new JLabel("Type "));
		panel.add(comboTypeCond);
		
		return panel;
	}

	private Component getPanelActionTerminate() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Action terminate"));
		panel.add(new JLabel("<Action terminate> s'applique à l'état source"));
		return panel;
	}

	private JPanel getPanelAttributeCheck() {
		JPanel panel = new JPanel(new GridLayout(4, 2));
		panel.setBorder(new TitledBorder("Action terminate"));
		
		comboListAttribut = new JComboBox<String>(Settings.ATTRIBUTES);
		fieldOperateur = new JTextField();
		fieldValeurAttribut = new JTextField();
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
	
	//Acceseurs
	public String getAttributCheckName(){
		return (String) comboListAttribut.getSelectedItem();
	}
	
	public String getAttributCheckOperateur(){
		return fieldOperateur.getText();
	}
	
	public String getAttributCheckValue(){
		return fieldValeurAttribut.getText();
	}
	
	public boolean getAttributCheckPourcentage(){
		return checkBoxPourcentage.isSelected();
	}
	
	JComboBox<ConditionEnum> comboTypeCond;
	JTextField fieldNameCond;
	
	//Attribut Check
	JComboBox<String> comboListAttribut;
	JTextField fieldOperateur;
	JTextField fieldValeurAttribut;
	JCheckBox checkBoxPourcentage;
	
	JLabel console;
	
	JButton buttonOk;
	
}
