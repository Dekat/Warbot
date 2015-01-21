package teams.fsm;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.WarFSMMessage;
import edu.warbot.FSM.Reflexe.WarReflexeWarnWithCondition;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionMessageChecker;
import edu.warbot.FSM.condition.WarConditionPerceptCounter;
import edu.warbot.FSM.plan.WarPlanAttaquer;
import edu.warbot.FSM.plan.WarPlanDefendre;
import edu.warbot.FSM.plan.WarPlanPatrouiller;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {

	WarFSM fsm;

	public WarRocketLauncherBrainController() {
		super();
	}

	@Override
	public void activate() {
		initialisation();
	}

	@Override
	public String action() {
		return fsm.executeFSM();
	}

	private void initialisation() {
		fsm = new WarFSM();

		/*** Refelxes ***/
		WarCondition condReflex = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarBase, true, ">", 0);
		fsm.addReflexe(new WarReflexeWarnWithCondition(getBrain(), condReflex, WarAgentType.WarRocketLauncher, WarFSMMessage.enemyBaseHere));

		/*** Etats ***/
		WarEtat etatPatrouille = new WarEtat("Etat patrouiler", new WarPlanPatrouiller(getBrain(), true));
		fsm.addEtat(etatPatrouille);

		WarEtat etatAtt = new WarEtat("Etat Attaquer", new WarPlanAttaquer(getBrain(), WarAgentType.WarBase));
		fsm.addEtat(etatAtt);

		WarEtat etatDef = new WarEtat("Etat Defendre", new WarPlanDefendre(getBrain()));
		fsm.addEtat(etatDef);

		/*** Conditions ***/
		WarCondition cond1 = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarBase, true, ">", 0);
		cond1.setDestination(etatAtt);
		etatPatrouille.addCondition(cond1);

		WarCondition cond2 = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarBase, true, "==", 0);
		cond2.setDestination(etatAtt);
		etatAtt.addCondition(cond2);

		WarCondition cond3 = new WarConditionMessageChecker(getBrain(), WarAgentType.WarBase, WarFSMMessage.baseIsAttack);
		cond3.setDestination(etatDef);
		etatPatrouille.addCondition(cond3);
		/*
		WarCondition cond4 = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarRocketLauncher, true, "==", 0);
		cond4.setDestination(etatAtt);
		etatDef.addCondition(cond4);
		*/
		fsm.setFirstEtat(etatPatrouille);

		fsm.initFSM();
	}

}