package edu.warbot.FSMEditor.dialogues;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSMEditor.FSMSettings.PlanEnum;
import edu.warbot.FSMEditor.models.ModeleState;
import edu.warbot.FSMEditor.views.ViewBrain;

public class DialogueStateSetting extends AbstractDialogue {

	private static final long serialVersionUID = 1L;

	private ModeleState modelState;
	
	public DialogueStateSetting(ViewBrain viewBrain, ModeleState modelState) {
		this(viewBrain, modelState.getPlanSettings());
		this.modelState = modelState;
	}
	public DialogueStateSetting(ViewBrain viewBrain, WarPlanSettings planSettings) {
		super(viewBrain, planSettings);
		
		if(planSettings == null)
			planSettings = new WarPlanSettings();
	}
	
	@Override
	public void createDialog() {
		super.createDialog();
	}
	
	protected JPanel getPanelMainSetting() {
		JPanel panelName = new JPanel(new GridLayout(2, 2));
		panelName.setBorder(new TitledBorder("State settings"));

		String name;
		if(modelState != null)
			name = modelState.getName();
		else
			name = DEFAULT_STATE_NAME;
		
		fieldName = new JTextField(name);
		comboxPlan = new JComboBox<>(PlanEnum.values());
		
		if(modelState != null)
			comboxPlan.setSelectedItem(this.modelState.getPlanName());

		panelName.add(add(new JLabel("Name")));
		panelName.add(fieldName);

		panelName.add(new JLabel("Plan"));
		panelName.add(comboxPlan);

		return panelName;

	}

	@Override 
	protected void saveGeneralSettings() {
		if(modelState != null){
			this.modelState.setName(this.fieldName.getText());
			this.modelState.setPlanName((PlanEnum) this.comboxPlan.getSelectedItem());	
		}
	}

	@Override
	protected boolean isValide(){
		return !this.fieldName.getText().isEmpty();
	}

	public String getStateName() {
		return this.fieldName.getText();
	}

	public PlanEnum getPlanName() {
		return (PlanEnum) comboxPlan.getSelectedItem();
	}

	JTextField fieldName;
	JComboBox<PlanEnum> comboxPlan;

}
