package teams.fsm;

import edu.warbot.FSM.WarEtat;
import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.WarFSMMessage;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionPerceptCounter;
import edu.warbot.FSM.plan.WarPlanCreateUnit;
import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSM.reflexe.WarReflexeAnswerMessage;
import edu.warbot.FSM.reflexe.WarReflexeWarnWithCondition;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarBaseAdapter;

public class WarBaseBrainController extends WarBrain<WarBaseAdapter> {

	WarFSM<WarBaseAdapter> fsm;

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
		fsm = new WarFSM<WarBaseAdapter>();

		/*** Refelexes ***/
		fsm.addReflexe(new WarReflexeAnswerMessage<WarBaseAdapter>(getAgent(), WarFSMMessage.whereAreYou, WarFSMMessage.here));

		WarCondition<WarBaseAdapter> condReflex = new WarConditionPerceptCounter<WarBaseAdapter>(getAgent(), WarAgentType.WarRocketLauncher, true, ">", 0);
		fsm.addReflexe(new WarReflexeWarnWithCondition<WarBaseAdapter>(getAgent(), condReflex, WarAgentType.WarRocketLauncher, WarFSMMessage.baseIsAttack));

		/*** Etats ***/
		//Création et remplissage du plan settings
		//Pas super beau après un montra un system d'accesseur jolie
		WarPlanSettings set1 = new WarPlanSettings();
		set1.Agent_type_destination = new WarAgentType[2];
		set1.Agent_type_destination[0] = WarAgentType.WarExplorer;
		set1.Agent_type_destination[1] = WarAgentType.WarRocketLauncher;
		set1.Number_agent_destination = new Integer[2];
		set1.Number_agent_destination[0] = 1;
		set1.Number_agent_destination[1] = 1;
		set1.Value_reference = WarBase.MAX_HEALTH;
		set1.Value_pourcentage = 50;
		
		WarEtat<WarBaseAdapter> etatCreerUnite = new WarEtat<WarBaseAdapter>("Etat creer unité",
				new WarPlanCreateUnit(getAgent(), set1));
		fsm.addEtat(etatCreerUnite);

		fsm.setFirstEtat(etatCreerUnite);

		fsm.initFSM();

	}

}
