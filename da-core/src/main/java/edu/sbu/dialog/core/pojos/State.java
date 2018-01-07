package edu.sbu.dialog.core.pojos;

import edu.sbu.dialog.core.feature.Behaviour;

public class State<I,O> {
	
	private StateEnum state;
	private Behaviour<I,O> behaviour;
	
	public State(StateEnum state) {
		super();
		this.state = state;
	}
	
	public State(StateEnum state, Behaviour<I,O> behaviour) {
		super();
		this.state = state;
		this.behaviour = behaviour;
	}

	public StateEnum getState() {
		return state;
	}

	public Behaviour<I,O> getBehaviour() {
		return behaviour;
	}
	
}
