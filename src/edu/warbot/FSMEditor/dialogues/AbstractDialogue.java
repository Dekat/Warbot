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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jfree.ui.tabbedui.VerticalLayout;

import com.sun.javafx.binding.SelectBinding.AsBoolean;

import edu.warbot.FSM.WarGenericSettings.AbstractGenericAttributSettings;
import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.settings.EnumAction;
import edu.warbot.FSMEditor.views.ViewBrain;
import edu.warbot.agents.enums.WarAgentType;

public abstract class AbstractDialogue extends JDialog{
	
	private static final long serialVersionUID = 1L;

	public static String DEFAULT_CONDITION_NAME = "Condition_";
	public static String DEFAULT_STATE_NAME = "State_";
	
	AbstractGenericAttributSettings genericSettings;
	HashMap<Field, JComponent> mapFieldComp = new HashMap<>();

	boolean isValide = false;
	
	public AbstractDialogue() {
		super();
		this.setModal(true);
		//TODO il faut que le modal � true pour que la fenetre soit bloquante ?
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	public AbstractDialogue(ViewBrain f, AbstractGenericAttributSettings genericSettings) {
		this();
		this.genericSettings = genericSettings;
	}
	
	public void createDialog(){
		this.setTitle("General settings");
		this.setSize(new Dimension(400, 350));

		JPanel panel = new JPanel(new VerticalLayout());
		this.setContentPane(panel);

		//Panel propre aux states
		panel.add(getPanelMainSetting());

		//Panel Generic
		panel.add(getPanelGenericSettings());

		panel.add(getPanelBottom());

		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventValider();
			}
		});

		this.setVisible(true);
	}
	
	protected abstract JPanel getPanelMainSetting();
	
	/**
	 * Gestion des evenements
	 */
	protected void eventValider() {

		if (isValide()){
			saveSettings();
			isValide = true;
			this.dispose();
		}else {
			labelConsole.setText("Entrer un nom pour l'état");
		}
	}
	
	protected abstract boolean isValide();
	
	/**
	 * Creation dynamique des composant generique
	 */
	public JPanel getPanelGenericSettings(){
		JPanel panel = new JPanel(new VerticalLayout());
		panel.setBorder(new TitledBorder("Specific settings"));
		
		Field[] fields = genericSettings.getClass().getDeclaredFields();

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
			
		}else if (field.getType().equals(WarAgentType.class)) {
			panel.add(component = getComponentForAgentType(field));
			
		}else if (field.getType().equals(EnumAction.class)) {
			panel.add(component = getComponentForAction(field));
			
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

	private JCheckBox getComponentForBoolean(Field field) {
		Boolean b = null;
		try {
			b = (Boolean) field.get(genericSettings);
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

	private JTextField getComponentForInteger(Field field) {
		Integer b = null;
		try {
			b = (Integer) field.get(genericSettings);
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
			b = (String) field.get(genericSettings);
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
	
	//Ici et dans les autre du meme genre changer le type generic de la colection en WarAgent type
	private JComboBox<String> getComponentForAgentType(Field field) {
		JComboBox<String> cb = new JComboBox<>();
		for (WarAgentType at : WarAgentType.values()) {
			cb.addItem(at.name());
		}
		return cb;
	}
	
	private JComboBox<EnumAction> getComponentForAction(Field field) {
		return new JComboBox<>(EnumAction.values());
	}

	private JTextField getComponentForIntegerTab(Field field) {
		Integer[] integers = null;
		try {
			integers = Integer[].class.cast(field.get(genericSettings));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		String s = "";
		if(integers != null){
			for (Integer integer : integers) {
				s += integer + ", ";
			}
		}
		
		JTextField cb = new JTextField(s);
		cb.setToolTipText("Nombre séparés par des virgules : \",\"");
		
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
	
	private JLabel getLabelForField(Field field) {
		return new JLabel(field.getName());
	}
	
	/**
	 * Sauvegardes des parametres generique
	 */
	
	protected void saveSettings() {
		saveGenericSettings();
	}
	
	private void saveGenericSettings(){
		for (Field field : mapFieldComp.keySet()) {
			JComponent dynamicComp = mapFieldComp.get(field);
			
			if(field.getType().equals(Boolean.class)){
				setFieldForBoolean(field, dynamicComp);
				
			}else if(field.getType().equals(Integer.class)){
				setFieldForInteger(field, dynamicComp);
				
			}else if(field.getType().equals(String.class)){
				setFieldForString(field, dynamicComp);
				
			}else if(field.getType().equals(WarAgentType.class)){
				setFieldForAgentType(field, dynamicComp);
				
			}else if(field.getType().equals(EnumAction.class)){
				setFieldForAction(field, dynamicComp);
				
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
			field.set(genericSettings, b);
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
			field.set(genericSettings, b);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void setFieldForString(Field field, JComponent comp) {
		String b = ((JTextField)comp).getText();
		try {
			field.set(genericSettings, b);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void setFieldForAgentType(Field field, JComponent comp) {
		WarAgentType at = null;
		try{
			at = (WarAgentType) ((JComboBox<WarAgentType>)comp).getSelectedItem();
		} catch (NumberFormatException e) {
			at = null;
		}
		try {
			field.set(genericSettings, at);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void setFieldForAction(Field field, JComponent comp) {
		EnumAction at = null;
		try{
			at = (EnumAction) ((JComboBox<EnumAction>)comp).getSelectedItem();
		} catch (NumberFormatException e) {
			at = null;
		}
		try {
			field.set(genericSettings, at);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void setFieldForIntegerTab(Field field, JComponent comp) {
		String string = null;
		try{
			string = ((JTextField)comp).getText();
		} catch (NumberFormatException e) {
			string = null;
		}
		String sTab[] = string.replaceAll(" ", "").split(",");
		ArrayList<Integer> arrayInt = new ArrayList<>();
		
		for (int i = 0; i < sTab.length; i++) {
			try {
				arrayInt.add(Integer.valueOf(sTab[i]));
			} catch (NumberFormatException e) {
				
			}
		}
		Integer intTab[] = new Integer[arrayInt.size()];
		intTab = arrayInt.toArray(intTab);
		
		try {
			field.set(genericSettings, intTab);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
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
			field.set(genericSettings, b);
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
			field.set(genericSettings, b);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public JPanel getPanelBottom(){
		JPanel panel = new JPanel();
		labelConsole = new JLabel();
		buttonOk = new JButton("Ok");
		panel.add(labelConsole);
		panel.add(buttonOk);
		
		return panel;
	}
	
	public boolean isValideComponent(){
		return isValide;
	}

	JLabel labelConsole;
	JButton buttonOk;

}
