import java.util.ArrayList;

public class State {
	private boolean isFinal;
	private boolean isInitial;
	private int nbrTransitions;
	private ArrayList<Transition> transitions;
	
	public State(boolean isFinal, boolean isInitial, int nbrTransitions, ArrayList<Transition> transitions) {
		this.isFinal = isFinal;
		this.isInitial = isInitial;
		this.nbrTransitions = nbrTransitions;
		this.transitions = transitions;
	}
	
}
