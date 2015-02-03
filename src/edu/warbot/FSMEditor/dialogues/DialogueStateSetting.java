package edu.warbot.FSMEditor.dialogues;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
import edu.warbot.FSMEditor.Configuration;
import edu.warbot.FSMEditor.Views.ViewBrain;
import edu.warbot.agents.enums.WarAgentType;

public class DialogueStateSetting extends AbstractDialogue {

	private static final long serialVersionUID = 1L;

	WarPlanSettings planSettings;
	Field fields[];
	HashMap<Field, JComponent> mapFieldComp = new HashMap<>();

	public DialogueStateSetting(ViewBrain f, WarPlanSettings planSettings) {
		super();

		this.planSettings = planSettings;

		this.setTitle("State settings");
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
		panel.setBorder(new TitledBorder("Settings"));
		
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
		} else if (field.getType().equals(ArrayList.class)) {
			panel.add(component = getComponentForArrayList(field));
		}else{
			System.err.println("The type " + field.getType() + " is not available for de the generated dynamic interface");
		}

		mapFieldComp.put(field, component);
		return panel;
	}

	private JComboBox<?> getComponentForArrayList(Field field) {
		ArrayList<?> b = null;
		try {
			b = (ArrayList<?>) field.get(planSettings);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (b != null && b.size() > 0) {
			if (b.get(0).getClass().equals(Integer.class))
				return getComponentForArrayListOfInteger();
			else if (b.get(0).getClass().equals(WarAgentType.class))
				return getComponentForArrayListOfAgentType();
			else if (b.get(0).getClass().equals(String.class))
				return getComponentForArrayListOfString();
			else {
				System.err.println("Unknown generic type " + b.get(0).getClass() + " of arrayList for attribut in WarPlanSettings");
			}
		} else {
			System.err.println("Attribut in class WarPlanSettings must be initiate and containe at least one value");
		}
		return null;
	}

	private JComboBox<String> getComponentForArrayListOfAgentType() {
		JComboBox<String> cb = new JComboBox<>();

		for (WarAgentType at : WarAgentType.values()) {
//			JCheckBox cbmi = new JCheckBox(at.name());
			cb.addItem(at.name());
		}
		return cb;
	}

	private JComboBox<Integer> getComponentForArrayListOfInteger() {
		JComboBox<Integer> cb = new JComboBox<>();
		for (int i = 0; i < 11; i++) {
//			JCheckBox cbmi = new JCheckBox(String.valueOf(i));
			cb.addItem(i);
		}
		return cb;
	}
	
	private JComboBox<String> getComponentForArrayListOfString() {
		JComboBox<String> cb = new JComboBox<>();
		for (int i = 0; i < 11; i++) {
//			JCheckBox cbmi = new JCheckBox(String.valueOf(i));
			cb.addItem("string_" + i);
		}
		return cb;
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

		fieldNameEtat = new JTextField();
		comboxPlan = new JComboBox<>(planSimpleName);

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
		for (Field field : mapFieldComp.keySet()) {
			JComponent dynamicComp = mapFieldComp.get(field);
			try{
				if(field.getType().equals(Boolean.class)){
					setFieldForBoolean(field, dynamicComp);
				}else if(field.getType().equals(Integer.class)){
					setFieldForInteger(field, dynamicComp);
				}else if(field.getType().equals(String.class)){
					setFieldForString(field, dynamicComp);
				}else if(field.getType().equals(ArrayList.class)){
					ArrayList<?> array = (ArrayList<?>) field.get(planSettings);
					if(array != null && array.size() > 0){
						if(array.get(0).getClass().equals(Integer.class)){
							setFieldForArrayOfInteger(field, dynamicComp);
						}else if(array.get(0).getClass().equals(String.class)){
							setFieldForArrayOfString(field, dynamicComp);
						}else if(array.get(0).getClass().equals(WarAgentType.class)){
							setFieldForArrayOfWarAgentType(field, dynamicComp);
						}
					}else{
						System.err.println("Attribut in class WarPlanSettings must be initiate and containe at least one value");
					}
				}
			}catch(IllegalAccessException e){
				e.printStackTrace();
			}
		}
		
	}

	private void setFieldForArrayOfWarAgentType(Field field, JComponent comp) {
		WarAgentType integer = WarAgentType.valueOf((String) ((JComboBox<?>)comp).getSelectedItem());
		ArrayList<WarAgentType> arrayRes = new ArrayList<>();
		arrayRes.add(integer);
		try {
			field.set(planSettings, arrayRes);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}		
	}

	private void setFieldForArrayOfString(Field field, JComponent dynamicComp) {
		String integer = (String) ((JComboBox<?>)dynamicComp).getSelectedItem();
		ArrayList<String> arrayRes = new ArrayList<>();
		arrayRes.add(integer);
		try {
			field.set(planSettings, arrayRes);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}		
	}

	private void setFieldForArrayOfInteger(Field field, JComponent comp) {
		Integer integer = (Integer) ((JComboBox<?>)comp).getSelectedItem();
		ArrayList<Integer> arrayRes = new ArrayList<>();
		arrayRes.add(integer);
		try {
			field.set(planSettings, arrayRes);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
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

	public String getPlanName() {// TODO warning
		return (String) planName[comboxPlan.getSelectedIndex()];
	}

	String[] planName = Configuration.PLAN;
	String[] planSimpleName = Configuration.getSimpleName(planName);

	JComboBox<String> comboxPlan;
	JTextField fieldNameEtat;

	JLabel console;
	JButton buttonOk;

}
