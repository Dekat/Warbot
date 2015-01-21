package edu.warbot.brains;

public abstract class WarBrainController {

	protected WarBrain _brain;
	
	public WarBrainController() {
	}
	
	public void setBrain(WarBrain brain) {
		this._brain = brain;
	}
	
	public abstract WarBrain getBrain();
	
    public abstract String action();
    
    public void activate() {}
	
}
