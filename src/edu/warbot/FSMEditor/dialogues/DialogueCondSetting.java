package edu.warbot.FSMEditor.dialogues;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSMEditor.FSMSettings.ConditionEnum;
import edu.warbot.FSMEditor.FSMSettings.PlanEnum;
import edu.warbot.FSMEditor.models.ModeleCondition;
import edu.warbot.FSMEditor.views.ViewBrain;

public class DialogueCondSetting extends AbstractDialogue{
	
	private static final long serialVersionUID = 1L;
	
	private ModeleCondition modelCondition;
	
	public DialogueCondSetting(ViewBrain f, WarConditionSettings conditionsSettings) {
		super(f, conditionsSettings);
		
		if(conditionsSettings == null)
			conditionsSettings = new WarConditionSettings();
		
	}
	
	@Override
	public void createDialog() {
		super.createDialog();
	}
	
	@Override
	protected JPanel getPanelMainSetting() {
		JPanel panel = new JPanel(new GridLayout(2, 2));
		panel.setBorder(new TitledBorder("General"));
		
		String name;
		if(modelCondition != null)
			name = modelCondition.getName();
		else 
			name = DEFAULT_CONDITION_NAME;
			
		fieldName = new JTextField(name);
		comboTypeCond = new JComboBox<>(ConditionEnum.values());
		
		if(modelCondition != null)
			comboTypeCond.setSelectedItem(modelCondition.getType());
		
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
	
	@Override
	protected void saveGeneralSettings() {
		if(modelCondition != null){
			this.modelCondition.setName(this.fieldName.getText());
			this.modelCondition.setConditionType((ConditionEnum) this.comboTypeCond.getSelectedItem());	
		}
	}

	public String getConditionName(){
		return this.fieldName.getText();
	}
	
	public ConditionEnum getConditionType(){
		return (ConditionEnum) comboTypeCond.getSelectedItem();
	}
	
	JTextField fieldName;
	JComboBox<ConditionEnum> comboTypeCond;
	
}
