package teams.fsm;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.WarFSMMessage;
import edu.warbot.FSM.Reflexe.WarReflexeWarnWithCondition;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionAttributCheck;
import edu.warbot.FSM.condition.WarConditionPerceptAttributCheck;
import edu.warbot.FSM.condition.WarConditionPerceptCounter;
import edu.warbot.FSM.plan.WarPlanBeSecure;
import edu.warbot.FSM.plan.WarPlanHealer;
import edu.warbot.FSM.plan.WarPlanRamasserNouriture;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private WarFSM fsm;

	public WarExplorerBrainController() {
		super();
	}

	@Override
	public String action() {
		return fsm.executeFSM();
	}

	@Override
	public void activate() {
		initialisation();
	}

	private void initialisation() {
		fsm = new WarFSM();

		/*** Reflexes ***/
		WarCondition condReflex = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarBase, true, ">", 0);
		fsm.addReflexe(new WarReflexeWarnWithCondition(getBrain(), condReflex, WarAgentType.WarRocketLauncher, WarFSMMessage.enemyBaseHere));

		/*** Etats ***/
		WarEtat etatGetFood = new WarEtat("Etat get Food", new WarPlanRamasserNouriture(getBrain()));
		fsm.addEtat(etatGetFood);

		WarEtat etatHeal = new WarEtat("Etat heal", new WarPlanHealer(getBrain(), 50, 50));
		fsm.addEtat(etatHeal);

		WarEtat etatSecure = new WarEtat("Etat be secure", new WarPlanBeSecure(getBrain()));
		fsm.addEtat(etatSecure);

		/*** Conditions ***/
		WarCondition cond1 = new WarConditionAttributCheck(getBrain(), WarCondition.HEALTH, "<", WarExplorer.MAX_HEALTH, 20);
		cond1.setDestination(etatHeal);

		etatGetFood.addCondition(cond1);

		WarCondition cond2 = new WarConditionPerceptAttributCheck(getBrain(), WarCondition.HEALTH, "<", WarExplorer.MAX_HEALTH, 20, false, null, true);
		cond2.setDestination(etatHeal);
		etatGetFood.addCondition(cond2);

		WarCondition cond3 = new WarConditionAttributCheck(getBrain(), WarCondition.HEALTH, ">", WarExplorer.MAX_HEALTH, 80);
		cond3.setDestination(etatGetFood);
		etatHeal.addCondition(cond3);

		WarCondition cond4 = new WarConditionPerceptAttributCheck(getBrain(), WarCondition.HEALTH, ">", WarExplorer.MAX_HEALTH, 80, false, null, true);
		cond4.setDestination(etatGetFood);
		etatHeal.addCondition(cond4);

		WarCondition cond5 = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarRocketLauncher, true, ">", 3);
		cond5.setDestination(etatSecure);
		etatHeal.addCondition(cond5);

		WarCondition cond6 = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarRocketLauncher, true, "==", 0);
		cond6.setDestination(etatHeal);
		etatSecure.addCondition(cond6);

		WarCondition cond7 = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarRocketLauncher, true, ">", 3);
		cond7.setDestination(etatSecure);
		etatGetFood.addCondition(cond7);

		WarCondition cond8 = new WarConditionAttributCheck(getBrain(), WarCondition.HEALTH, "==", WarExplorer.MAX_HEALTH, 80);
		cond8.setDestination(etatGetFood);
		etatSecure.addCondition(cond8);

		fsm.setFirstEtat(etatGetFood);

		fsm.initFSM();
	}
}
