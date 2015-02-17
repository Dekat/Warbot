package edu.warbot.FSMEditor.dialogues;

import java.awt.BorderLayout;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jfree.ui.tabbedui.VerticalLayout;

import edu.warbot.FSM.genericSettings.AbstractGenericAttributSettings;
import edu.warbot.FSMEditor.settings.EnumAction;
import edu.warbot.FSMEditor.settings.EnumMessage;
import edu.warbot.FSMEditor.settings.EnumMethod;
import edu.warbot.FSMEditor.settings.EnumOperand;
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
		//TODO il faut que le modal à true pour que la fenetre soit bloquante ?
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	public AbstractDialogue(ViewBrain f, AbstractGenericAttributSettings genericSettings) {
		this();
		this.genericSettings = genericSettings;
	}
	
	public void createDialog(){
		this.setTitle("General settings");
		this.setSize(new Dimension(350, 400));

		JPanel panel = new JPanel(new BorderLayout());
		this.setContentPane(panel);

		//Panel propre aux states
		panel.add(getPanelMainSetting(), BorderLayout.NORTH);

		//Panel Generic
		panel.add(getPanelGenericSettings(), BorderLayout.CENTER);

		panel.add(getPanelBottom(), BorderLayout.SOUTH);

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
	public JScrollPane getPanelGenericSettings(){
		JPanel panel = new JPanel(new VerticalLayout());
		panel.setBorder(new TitledBorder("Specific settings"));

		JScrollPane panelS = new JScrollPane(panel);
		panelS.setWheelScrollingEnabled(true);
		
		Field[] fields = genericSettings.getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			panel.add(getComponentForField(fields[i]));
		}

		return panelS;
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

		}else if (field.getType().equals(EnumMessage.class)) {
			panel.add(component = getComponentForMessage(field));
			
		}else if (field.getType().equals(EnumOperand.class)) {
			panel.add(component = getComponentForOperand(field));
			
		}else if (field.getType().equals(EnumMethod.class)) {
			panel.add(component = getComponentForMethod(field));

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
	
	private JComboBox<WarAgentType> getComponentForAgentType(Field field) {
		JComboBox<WarAgentType> cb = new JComboBox<WarAgentType>(WarAgentType.values());
		try {
			cb.setSelectedItem(field.get(this.genericSettings));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return cb;
	}
	
	private JComboBox<EnumOperand> getComponentForOperand(Field field) {
		JComboBox<EnumOperand> cb = new JComboBox<>(EnumOperand.values());
		try {
			cb.setSelectedItem(field.get(this.genericSettings));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return cb;
	}
	
	private JComboBox<EnumMethod> getComponentForMethod(Field field) {
		JComboBox<EnumMethod> cb = new JComboBox<>(EnumMethod.values());
		try {
			cb.setSelectedItem(field.get(this.genericSettings));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return cb;
	}
	
	private JComboBox<EnumAction> getComponentForAction(Field field) {
		JComboBox<EnumAction> cb = new JComboBox<>(EnumAction.values());
		try {
			cb.setSelectedItem(field.get(this.genericSettings));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return cb;
	}
	
	private JComboBox<EnumMessage> getComponentForMessage(Field field) {
		JComboBox<EnumMessage> cb = new JComboBox<>(EnumMessage.values());
		try {
			cb.setSelectedItem(field.get(this.genericSettings));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
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
				
			}else if(field.getType().equals(EnumOperand.class)){
				setFieldForOperand(field, dynamicComp);
				
			}else if(field.getType().equals(EnumMethod.class)){
				setFieldForMethod(field, dynamicComp);
				
			}else if(field.getType().equals(EnumMessage.class)){
				setFieldForMessage(field, dynamicComp);
				
			}else{
				System.err.println("Field " + field.getName() + " : " + field.getType() + " is not availlable for save dynamic interface");
				JOptionPane.showMessageDialog(null, 
						"Field with name <" + field.getName() + "> and type <" + field.getType().getSimpleName() + "> is not availlable for save dynamic interface", "Attribut error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	private void setFieldForBoolean(Field field, JComponent comp) {
		Boolean b = ((JCheckBox)comp).isSelected();
		setField(field, b);
	}
	
	private void setFieldForInteger(Field field, JComponent comp) {
		Integer b;
		try{
			b = Integer.valueOf(((JTextField)comp).getText());
		} catch (NumberFormatException e) {
			b = null;
		}
		setField(field, b);
	}
	
	private void setFieldForString(Field field, JComponent comp) {
		String b = ((JTextField)comp).getText();
		setField(field, b);
	}
	
	private void setFieldForAgentType(Field field, JComponent comp) {
		WarAgentType at = null;
		try{
			at = (WarAgentType)((JComboBox<WarAgentType>)comp).getSelectedItem();
		} catch (NumberFormatException e) {
			at = null;
		}
		setField(field, at);
	}
	
	private void setFieldForAction(Field field, JComponent comp) {
		EnumAction at = null;
		try{
			at = (EnumAction) ((JComboBox<EnumAction>)comp).getSelectedItem();
		} catch (NumberFormatException e) {
			at = null;
		}
		setField(field, at);
	}
	
	private void setFieldForMethod(Field field, JComponent comp) {
		EnumMethod at = null;
		try{
			at = (EnumMethod) ((JComboBox<EnumMethod>)comp).getSelectedItem();
		} catch (NumberFormatException e) {
			at = null;
		}
		setField(field, at);
	}
	
	private void setFieldForMessage(Field field, JComponent comp) {
		EnumMessage at = null;
		try{
			at = (EnumMessage) ((JComboBox<EnumMessage>)comp).getSelectedItem();
		} catch (NumberFormatException e) {
			at = null;
		}
		setField(field, at);
	}
	
	private void setFieldForOperand(Field field, JComponent comp) {
		EnumOperand at = null;
		try{
			at = (EnumOperand) ((JComboBox<EnumOperand>)comp).getSelectedItem();
		} catch (NumberFormatException e) {
			at = null;
		}
		setField(field, at);
	}

	private void setField(Field f, Object o){
		try {
			f.set(genericSettings, o);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
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
