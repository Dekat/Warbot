package teams.fsm;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.WarFSMMessage;
import edu.warbot.FSM.Reflexe.WarReflexeAnswerMessage;
import edu.warbot.FSM.Reflexe.WarReflexeWarnWithCondition;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionPerceptCounter;
import edu.warbot.FSM.plan.WarPlanCreateUnit;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.braincontrollers.WarBaseAbstractBrainController;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

	WarFSM fsm;

	public WarBaseBrainController() {
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

		/*** Refelexes ***/
		fsm.addReflexe(new WarReflexeAnswerMessage(getBrain(), WarFSMMessage.whereAreYou, WarFSMMessage.here));

		WarCondition condReflex = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarRocketLauncher, true, ">", 0);
		fsm.addReflexe(new WarReflexeWarnWithCondition(getBrain(), condReflex, WarAgentType.WarRocketLauncher, WarFSMMessage.baseIsAttack));

		/*** Etats ***/
		WarAgentType agentType[] = {WarAgentType.WarExplorer, WarAgentType.WarRocketLauncher};
		int nombre[] = {1, 1};
		WarEtat etatCreerUnite = new WarEtat("Etat creer unité", new WarPlanCreateUnit(getBrain(), agentType, nombre, WarBase.MAX_HEALTH, 50));
		fsm.addEtat(etatCreerUnite);

		fsm.setFirstEtat(etatCreerUnite);

		fsm.initFSM();

	}

}
