package edu.warbot.FSMEditor.dialogues;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
		
		Field[] fields = planSettings.getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			panel.add(getEntryForField(fields[i]));
		}

		return panel;
	}

	private JPanel getEntryForField(Field field) {
		JPanel panel = new JPanel(new GridLayout(1, 2));

		panel.add(getLabelForField(field));

		if (field.getType().equals(Boolean.class)) {
			panel.add(getComponentForBoolean(field));
		} else if (field.getType().equals(Integer.class)) {
			panel.add(getComponentForInteger(field));
		} else if (field.getType().equals(ArrayList.class)) {
			panel.add(getComponentForArrayList(field));
		}

		return panel;
	}

	private JPanel getComponentForArrayList(Field field) {
		ArrayList<?> b = null;
		try {
			b = (ArrayList<?>) field.get(planSettings);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (b != null) {

			if (b.size() > 0) {
				if (b.get(0).getClass().equals(Integer.class))
					return getComponentForArrayListOfInteger();
				else if (b.get(0).getClass().equals(WarAgentType.class))
					return getComponentForArrayListOfAgentType();
				else {
					System.err.println("ERRER ArrayList for name "
							+ field.getName() + " is unknow generic type");

					return null;
				}
			} else {
				System.err
						.println("ERRER ArrayList for name "
								+ field.getName()
								+ " is empty (impossible to find generic type of the collection, plz add one item in the collection)");
				return null;
			}
		} else {
			System.err.println("ERRER ArrayList for name " + field.getName()
					+ " is not accessible");

			return null;
		}
	}

	private JPanel getComponentForArrayListOfAgentType() {
		JComboBox<String> cb = new JComboBox<>();

		for (WarAgentType at : WarAgentType.values()) {
			JCheckBox cbmi = new JCheckBox(at.name());
			cb.addItem(at.name());
		}

		JPanel p = new JPanel();
		p.add(cb);

		return p;
	}

	private JPanel getComponentForArrayListOfInteger() {

		JComboBox<Integer> cb = new JComboBox<>();

		for (int i = 0; i < 11; i++) {
			JCheckBox cbmi = new JCheckBox(String.valueOf(i));
			cb.addItem(i);
		}

		JPanel p = new JPanel();
		p.add(cb);

		return p;
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
			cb.setText(String.valueOf(b.intValue()));

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

		if (this.fieldNameEtat.getText().isEmpty())
			console.setText("Entrer un nom pour l'�tat");
		else {
			checkValidity();
			this.dispose();
		}
	}

	private void checkValidity() {
		if (this.fieldNameEtat.getText().isEmpty() == false
				&& this.comboxPlan.getSelectedIndex() != -1)
			super.isValide = true;
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
