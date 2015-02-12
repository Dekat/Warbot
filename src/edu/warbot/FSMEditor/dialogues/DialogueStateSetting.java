package edu.warbot.FSMEditor.dialogues;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.settings.EnumPlan;
import edu.warbot.FSMEditor.views.ViewBrain;

public class DialogueStateSetting extends AbstractDialogue {

	private static final long serialVersionUID = 1L;

	public DialogueStateSetting(ViewBrain viewBrain, ModelState modelState) {
		this(viewBrain, modelState.getPlanSettings());
		
		this.fieldName = new JTextField(modelState.getName());
		this.comboxPlan.setSelectedItem(modelState.getPlanName());
	}

	public DialogueStateSetting(ViewBrain viewBrain, WarPlanSettings planSettings) {
		super(viewBrain, planSettings);
		
		if(genericSettings == null)
			genericSettings = new WarPlanSettings();
	}
	
	@Override
	public void createDialog() {
		super.createDialog();
	}
	
	protected JPanel getPanelMainSetting() {
		JPanel panelName = new JPanel(new GridLayout(2, 2));
		panelName.setBorder(new TitledBorder("State settings"));

		panelName.add(add(new JLabel("Name")));
		panelName.add(fieldName);

		panelName.add(new JLabel("Plan"));
		panelName.add(comboxPlan);

		return panelName;

	}

	@Override
	protected boolean isValide(){
		return !this.fieldName.getText().isEmpty();
	}

	public WarPlanSettings getPlanSettings(){
		return (WarPlanSettings)genericSettings;
	}
	
	public String getStateName() {
		return this.fieldName.getText();
	}

	public EnumPlan getPlanName() {
		return (EnumPlan) comboxPlan.getSelectedItem();
	}

	JTextField fieldName = new JTextField(DEFAULT_STATE_NAME);
	JComboBox<EnumPlan> comboxPlan = new JComboBox<EnumPlan>(EnumPlan.values());

}
