package edu.warbot.FSM;

import javax.swing.JOptionPane;

import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarExplorerAdapter;

//Ici plutot mettre ControllableWarAgentAdapter ?
public class WarFSMBrainController extends WarBrain<WarExplorerAdapter>{
	
	private WarFSM<ControllableWarAgentAdapter> fsm;
	
	public void setFSM(WarFSM<ControllableWarAgentAdapter> fsm){
		this.fsm = fsm;
	}
	
	@Override
	public String action() {
		try{
			return fsm.executeFSM();
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, 
					"Error FSM use incorrect settings, please check <GenericSettings>, <settings> in the editor, your Xml configuration file for FSM and attrbiut you use inside the editor ! Game will crash soon", "FSM intern error caused by wrong configuration", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
	}

	public WarFSM getFSM() {
		return fsm;
	}

}
