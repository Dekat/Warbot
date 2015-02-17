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
		}catch(NullPointerException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, 
					"Error FSM use incorrect settings, please check <GenericSettings>, <settings> in the editor, and your settings in the Editor"
					, "FSM intern error caused by wrong configuration", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
	}

	public WarFSM getFSM() {
		return fsm;
	}

}
