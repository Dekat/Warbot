package edu.warbot.brains;

public abstract class WarBrain<Adapter extends ControllableWarAgentAdapter> {

	protected Adapter adapter;
	
	public WarBrain() {
	}
	
	public void setAgentAdapter(Adapter adapter) {
		this.adapter = adapter;
	}
	
	public Adapter getAgent() {
		return adapter;
	}
	
    public abstract String action();
    
    public void activate() {}
	
}
