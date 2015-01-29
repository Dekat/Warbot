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
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

public class WarRocketLauncherBrainController extends WarBrain<WarRocketLauncherAdapter> {

	WarFSM<WarRocketLauncherAdapter> fsm;

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
		fsm = new WarFSM<WarRocketLauncherAdapter>();

		/*** Refelxes ***/
		WarCondition<WarRocketLauncherAdapter> condReflex = new WarConditionPerceptCounter<WarRocketLauncherAdapter>(getAgent(), WarAgentType.WarBase, true, ">", 0);
		fsm.addReflexe(new WarReflexeWarnWithCondition<WarRocketLauncherAdapter>(getAgent(), condReflex, WarAgentType.WarRocketLauncher, WarFSMMessage.enemyBaseHere));

		/*** Etats ***/
		WarEtat<WarRocketLauncherAdapter> etatPatrouille = new WarEtat<WarRocketLauncherAdapter>("Etat patrouiler", new WarPlanPatrouiller(getAgent(), true));
		fsm.addEtat(etatPatrouille);

		WarEtat<WarRocketLauncherAdapter> etatAtt = new WarEtat<WarRocketLauncherAdapter>("Etat Attaquer", new WarPlanAttaquer(getAgent(), WarAgentType.WarBase));
		fsm.addEtat(etatAtt);

		WarEtat<WarRocketLauncherAdapter> etatDef = new WarEtat<WarRocketLauncherAdapter>("Etat Defendre", new WarPlanDefendre(getAgent()));
		fsm.addEtat(etatDef);

		/*** Conditions ***/
		WarCondition<WarRocketLauncherAdapter> cond1 = new WarConditionPerceptCounter<WarRocketLauncherAdapter>(getAgent(), WarAgentType.WarBase, true, ">", 0);
		cond1.setDestination(etatAtt);
		etatPatrouille.addCondition(cond1);

		WarCondition<WarRocketLauncherAdapter> cond2 = new WarConditionPerceptCounter<WarRocketLauncherAdapter>(getAgent(), WarAgentType.WarBase, true, "==", 0);
		cond2.setDestination(etatAtt);
		etatAtt.addCondition(cond2);

		WarCondition<WarRocketLauncherAdapter> cond3 = new WarConditionMessageChecker<WarRocketLauncherAdapter>(getAgent(), WarAgentType.WarBase, WarFSMMessage.baseIsAttack);
		cond3.setDestination(etatDef);
		etatPatrouille.addCondition(cond3);
		/*
		WarCondition cond4 = new WarConditionPerceptCounter(getAgent(), WarAgentType.WarRocketLauncher, true, "==", 0);
		cond4.setDestination(etatAtt);
		etatDef.addCondition(cond4);
		*/
		fsm.setFirstEtat(etatPatrouille);

		fsm.initFSM();
	}

}