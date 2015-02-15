package edu.warbot.brains.capacities;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WallPercept;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.communications.WarMessage;
import edu.warbot.tools.CoordPolar;
import madkit.kernel.AbstractAgent.ReturnCode;

import java.awt.*;
import java.util.ArrayList;

public interface Alive extends CommonCapacities {

	public int getHealth();
	public int getMaxHealth();

}
