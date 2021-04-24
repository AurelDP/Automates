import java.util.ArrayList;

public class State {
	private int nameState;
	private boolean isFinal;
	private boolean isInitial;
	private int nbrTransitions;
	private ArrayList<Transition> transitions;
	
	public State(int nameState, boolean isFinal, boolean isInitial, int nbrTransitions, ArrayList<Transition> transitions) {
		this.nameState = nameState;
		this.isFinal = isFinal;
		this.isInitial = isInitial;
		this.nbrTransitions = nbrTransitions;
		this.transitions = transitions;
	}
	
	public int getName() {
		return nameState;
	}
	
	public boolean isFinal() {
		return isFinal;
	}
	
	public boolean isInitial() {
		return isInitial;
	}
	
	public int getNbrTransitions() {
		return nbrTransitions;
	}
	
	public ArrayList<Transition> getTransiList() {
		return transitions;
	}
		
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	public void setInitial(boolean isInitial) {
		this.isInitial = isInitial;
	}
	
	public void incrementNbrTransi() {
		this.nbrTransitions ++;
	}
	
	public void setTransi(String letter, State arrivalState) {
		transitions.add(new Transition(letter, arrivalState));
	}
}
