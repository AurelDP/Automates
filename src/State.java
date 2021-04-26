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
	
	public State(State state) {
		this.nameState = state.nameState;
		this.isFinal = state.isFinal;
		this.isInitial = state.isInitial;
		this.nbrTransitions = state.nbrTransitions;
		this.transitions = new ArrayList<Transition>();
		for (Transition t : state.transitions) {
			this.transitions.add(new Transition(t));
		}
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
	
	public String toStringTransi() {
		if (nbrTransitions == 1)
			return (nameState + String.valueOf(this.transitions.get(0).getLetter()) + this.transitions.get(0).getArrivalStateName());
		else if (nbrTransitions >= 2) {
			String r = "";
			int i = 0;
			for (Transition t : transitions) {
				if (i < nbrTransitions-1)
					r += (nameState + t.getLetter() + t.getArrivalStateName() + ",");
				else
					r += (nameState + t.getLetter() + t.getArrivalStateName());
				i ++;
			}
			return r;
		}
		return null;
	}
	
	public void displayTransi() {
		if (nbrTransitions == 1)
			System.out.print(nameState + this.transitions.get(0).getLetter() + this.transitions.get(0).getArrivalStateName());
		else if (nbrTransitions >= 2) {
			int i = 0;
			for (Transition t : transitions) {
				if (i < nbrTransitions-1)
					System.out.print(nameState + t.getLetter() + t.getArrivalStateName() + ",");
				else
					System.out.print(nameState + t.getLetter() + t.getArrivalStateName());
				i ++;
			}
		}
	}
}
