package edu.warbot.FSMEditor.dialogues;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jfree.ui.tabbedui.VerticalLayout;

import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSMEditor.FSMSettings.PlanEnum;
import edu.warbot.FSMEditor.Modeles.ModeleState;
import edu.warbot.FSMEditor.Views.ViewBrain;
import edu.warbot.agents.enums.WarAgentType;

public class DialogueStateSetting extends AbstractDialogue {

	private static final long serialVersionUID = 1L;

	WarPlanSettings planSettings;
	Field fields[];
	HashMap<Field, JComponent> mapFieldComp = new HashMap<>();

	private ModeleState modelState;

	public DialogueStateSetting(ViewBrain viewBrain, ModeleState modelState) {
		super();
		this.modelState = modelState;
		
		//Pas très beau de mettre ça ici mais on pourrait supposer que ce n'est jamais à null
		if(this.modelState.getPlanSettings() == null)
			this.planSettings = new WarPlanSettings();
		else
			this.planSettings = this.modelState.getPlanSettings();
		
		createDialog();
	}
	
	private void createDialog(){
		this.setTitle("General settings");
		this.setSize(new Dimension(400, 350));

		JPanel panel = new JPanel(new VerticalLayout());
		this.setContentPane(panel);

		panel.add(getPanelMainSetting());

		panel.add(getPanelPlanSettring());

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

	private JPanel getPanelPlanSettring() {
		JPanel panel = new JPanel(new VerticalLayout());
		panel.setBorder(new TitledBorder("Specific settings"));
		
		fields = planSettings.getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			panel.add(getComponentForField(fields[i]));
		}

		return panel;
	}

	private JPanel getComponentForField(Field field) {
		JPanel panel = new JPanel(new GridLayout(1, 2));

		panel.add(getLabelForField(field));

		JComponent component = null;
		if (field.getType().equals(Boolean.class)) {
			panel.add(component = getComponentForBoolean(field));
		} else if (field.getType().equals(Integer.class)) {
			panel.add(component = getComponentForInteger(field));
		}else if (field.getType().equals(String.class)) {
			panel.add(component = getComponentForString(field));
		}else if (field.getType().equals(Integer[].class)) {
			panel.add(component = getComponentForIntegerTab(field));
		}else if (field.getType().equals(String[].class)) {
			panel.add(component = getComponentForStringTab(field));
		}else if (field.getType().equals(WarAgentType[].class)) {
			panel.add(component = getComponentForAgentTypeTab(field));
		}else{
			panel.add(component = new JLabel("<unknow type>"));
			System.err.println("The type " + field.getName() + " : " + field.getType() + " is not available for de the generated dynamic interface");
		}

		mapFieldComp.put(field, component);
		return panel;
	}

	private JTextField getComponentForInteger(Field field) {
		Integer b = null;
		try {
			b = (Integer) field.get(planSettings);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		JTextField cb = new JTextField();

		if (b != null)
			cb.setText(String.valueOf(b));

		return cb;
	}
	
	private JComboBox<Integer> getComponentForIntegerTab(Field field) {
		Integer[] integers = null;
		try {
			integers = Integer[].class.cast(field.get(this.planSettings));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		JComboBox<Integer> cb = new JComboBox<>();
		for (Integer i = 0; i < 11; i++) {
//			JCheckBox cbmi = new JCheckBox(String.valueOf(i));
			cb.addItem(i);
		}
		
		if(integers != null && integers.length > 0){
			System.out.println("save ici");
			cb.setSelectedItem(integers[0]);
		}
		
		return cb;
	}
	
	//Ce composant pourra peut etre plutot etre simplment 
	//un textField dans lequel on demande de séparer les valeurs par des virgules
	private JComboBox<String> getComponentForStringTab(Field field) {
		JComboBox<String> cb = new JComboBox<>();
		for (int i = 0; i < 11; i++) {
//			JCheckBox cbmi = new JCheckBox(String.valueOf(i));
			cb.addItem(String.valueOf(i));
		}
		return cb;
	}
	
	private JComboBox<String> getComponentForAgentTypeTab(Field field) {
		JComboBox<String> cb = new JComboBox<>();
		for (WarAgentType at : WarAgentType.values()) {
//			JCheckBox cbmi = new JCheckBox(at.name());
			cb.addItem(at.name());
		}
		return cb;
	}
	
	private JTextField getComponentForString(Field field) {
		String b = null;
		try {
			b = (String) field.get(planSettings);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		JTextField cb = new JTextField();

		if (b != null)
			cb.setText(b);

		return cb;
	}

	private JCheckBox getComponentForBoolean(Field field) {
		Boolean b = null;
		try {
			b = (Boolean) field.get(planSettings);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		JCheckBox cb = new JCheckBox();

		if (b != null)
			cb.setSelected(b.booleanValue());

		return cb;
	}

	private JLabel getLabelForField(Field field) {
		return new JLabel(field.getName());
	}

	private JPanel getPanelMainSetting() {
		JPanel panelName = new JPanel(new GridLayout(2, 2));
		panelName.setBorder(new TitledBorder("State settings"));

		fieldNameEtat = new JTextField(this.modelState.getName());
		comboxPlan = new JComboBox<>(PlanEnum.values());
		comboxPlan.setSelectedItem(this.modelState.getPlanName());

		console = new JLabel();
		buttonOk = new JButton("Ok");

		panelName.add(add(new JLabel("Name")));
		panelName.add(fieldNameEtat);

		panelName.add(new JLabel("Plan"));
		panelName.add(comboxPlan);

		return panelName;

	}

	private void eventValider() {

		if (checkValidity()){
			this.saveSettings();
			super.isValide = true;
			this.dispose();
		}else {
			console.setText("Entrer un nom pour l'état");
		}
	}

	private void saveSettings() {
		//Sauvegard les configues général
		this.modelState.setName(this.fieldNameEtat.getText());
		this.modelState.setPlanName((PlanEnum) this.comboxPlan.getSelectedItem());
		
		for (Field field : mapFieldComp.keySet()) {
			JComponent dynamicComp = mapFieldComp.get(field);
			if(field.getType().equals(Boolean.class)){
				setFieldForBoolean(field, dynamicComp);
			}else if(field.getType().equals(Integer.class)){
				setFieldForInteger(field, dynamicComp);
			}else if(field.getType().equals(String.class)){
				setFieldForString(field, dynamicComp);
			}else if(field.getType().equals(Integer[].class)){
				setFieldForIntegerTab(field, dynamicComp);
			}else if(field.getType().equals(String[].class)){
				setFieldForStringTab(field, dynamicComp);
			}else if(field.getType().equals(WarAgentType[].class)){
				setFieldForAgentTypeTab(field, dynamicComp);
			}else
				System.err.println("Field " + field.getName() + " : " + field.getType() + " is not availlable for save dynamic interface");
		}
		
	}

	private void setFieldForBoolean(Field field, JComponent comp) {
		Boolean b = ((JCheckBox)comp).isSelected();
		try {
			field.set(planSettings, b);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void setFieldForInteger(Field field, JComponent comp) {
		Integer b;
		try{
			b = Integer.valueOf(((JTextField)comp).getText());
		} catch (NumberFormatException e) {
			b = null;
		}
		try {
			field.set(planSettings, b);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void setFieldForIntegerTab(Field field, JComponent comp) {
		//TODO à modifié si on veut sauvegarder plusieurs valeurs
		Integer b[] = new Integer[1];
		try{
			b[0] = (Integer) ((JComboBox<Integer>)comp).getSelectedItem();
		} catch (NumberFormatException e) {
			b = null;
		}
		try {
			field.set(planSettings, b);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void setFieldForStringTab(Field field, JComponent comp) {
		//TODO à modifié si on veut sauvegarder plusieurs valeurs
		String b[] = new String[1];
		try{
			b[0] = (String) ((JComboBox<Integer>)comp).getSelectedItem();
		} catch (NumberFormatException e) {
			b = null;
		}
		try {
			field.set(planSettings, b);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void setFieldForAgentTypeTab(Field field, JComponent comp) {
		//TODO à modifié si on veut sauvegarder plusieurs valeurs
		WarAgentType b[] = new WarAgentType[1];
		try{
			b[0] = WarAgentType.valueOf(String.valueOf(((JComboBox<Integer>)comp).getSelectedItem()));
		} catch (NumberFormatException e) {
			b = null;
		}
		try {
			field.set(planSettings, b);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void setFieldForString(Field field, JComponent comp) {
		String b = ((JTextField)comp).getText();
		try {
			field.set(planSettings, b);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private boolean checkValidity() {
		return !this.fieldNameEtat.getText().isEmpty();
	}

	public String getNom() {
		return this.fieldNameEtat.getText();
	}

	public PlanEnum getPlanName() {
		return (PlanEnum) comboxPlan.getSelectedItem();
	}

//	String[] planName = Settings.PLAN;
//	String[] planSimpleName = Settings.getSimpleName(planName);

	JComboBox<PlanEnum> comboxPlan;
	JTextField fieldNameEtat;

	JLabel console;
	JButton buttonOk;

}
