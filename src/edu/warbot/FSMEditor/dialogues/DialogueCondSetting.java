package edu.warbot.FSMEditor.dialogues;

import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.settings.EditorSettings;
import edu.warbot.FSMEditor.settings.GenericConditionSettings;
import edu.warbot.FSMEditor.views.ViewBrain;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class DialogueCondSetting extends AbstractDialogue{
	
	private static final long serialVersionUID = 1L;
	
	
	public DialogueCondSetting(ViewBrain f, ModelCondition modelCondition) {
		this(f, modelCondition.getConditionSettings());

		this.fieldName.setText(modelCondition.getName());
		this.comboTypeCond.setSelectedItem(modelCondition.getConditionType());
	}
	
	public DialogueCondSetting(ViewBrain f, GenericConditionSettings conditionsSettings) {
		super(f, conditionsSettings);
		
		if(genericSettings == null)
			genericSettings = new GenericConditionSettings();
	}
	
	@Override
	public void createDialog() {
		super.createDialog();
		setSize(400, 500);
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
	

	public String getConditionName(){
		return this.fieldName.getText();
	}
	
	public String getConditionType(){
		return EditorSettings.getConditionFullName((String) comboTypeCond.getSelectedItem());
	}
	
	public GenericConditionSettings getConditionSettings(){
		return (GenericConditionSettings)genericSettings;
	}

	JTextField fieldName = new JTextField(DEFAULT_CONDITION_NAME);
	JComboBox<String> comboTypeCond = new JComboBox<>(EditorSettings.getConditionClassSimpleName());
	
}
