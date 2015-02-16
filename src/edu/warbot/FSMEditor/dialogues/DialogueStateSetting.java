package edu.warbot.FSMEditor.dialogues;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import edu.warbot.FSM.WarGenericSettings.PlanSettings;
import edu.warbot.FSMEditor.controleurs.ControleurBrain;
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

	public DialogueStateSetting(ViewBrain viewBrain, PlanSettings planSettings) {
		super(viewBrain, planSettings);
		
		if(genericSettings == null)
			genericSettings = new PlanSettings();
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
	public boolean isValide(){
		return !this.fieldName.getText().isEmpty();
	}

	public PlanSettings getPlanSettings(){
		return (PlanSettings)genericSettings;
	}
	
	public String getStateName() {
		return this.fieldName.getText();
	}

	public EnumPlan getPlanName() {
		return (EnumPlan) comboxPlan.getSelectedItem();
	}

	JTextField fieldName = new JTextField(DEFAULT_STATE_NAME);
	JComboBox<EnumPlan> comboxPlan = new JComboBox<EnumPlan>(EnumPlan.values());

	public JButton getValidationButton() {
		return this.buttonOk;
	}

}
