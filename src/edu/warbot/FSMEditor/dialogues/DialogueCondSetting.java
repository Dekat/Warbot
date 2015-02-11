package edu.warbot.FSMEditor.dialogues;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSMEditor.models.ModeleCondition;
import edu.warbot.FSMEditor.settings.ConditionEnum;
import edu.warbot.FSMEditor.views.ViewBrain;

public class DialogueCondSetting extends AbstractDialogue{
	
	private static final long serialVersionUID = 1L;
	
	
	public DialogueCondSetting(ViewBrain f, ModeleCondition modelCondition) {
		this(f, modelCondition.getConditionSettings());

		this.fieldName.setText(modelCondition.getName());
		this.comboTypeCond.setSelectedItem(modelCondition.getConditionType());
	}
	
	public DialogueCondSetting(ViewBrain f, WarConditionSettings conditionsSettings) {
		super(f, conditionsSettings);
		
		if(genericSettings == null)
			genericSettings = new WarConditionSettings();
	}
	
	@Override
	public void createDialog() {
		super.createDialog();
	}
	
	@Override
	protected JPanel getPanelMainSetting() {
		JPanel panel = new JPanel(new GridLayout(2, 2));
		panel.setBorder(new TitledBorder("General"));
		
		panel.add(new JLabel("Name "));
		panel.add(fieldName);
		
		panel.add(new JLabel("Type "));
		panel.add(comboTypeCond);
		
		return panel;
	}

	@Override
	protected boolean isValide(){
		return !this.fieldName.getText().isEmpty();
	}
	

	public WarConditionSettings getConditionSettings(){
		return (WarConditionSettings)genericSettings;
	}
	
	public String getConditionName(){
		return this.fieldName.getText();
	}
	
	public ConditionEnum getConditionType(){
		return (ConditionEnum) comboTypeCond.getSelectedItem();
	}
	
	JTextField fieldName = new JTextField(DEFAULT_CONDITION_NAME);
	JComboBox<ConditionEnum> comboTypeCond = new JComboBox<ConditionEnum>(ConditionEnum.values());
	
}
