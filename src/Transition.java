
public class Transition {
	private String letter;
	private State arrivalState;
	
	public Transition(String letter, State arrivalState) {
		this.letter = letter;
		this.arrivalState = arrivalState;
	}
	
	public Transition(Transition transition) {
		this.letter = new String(transition.letter);
		// arrivalState is defined in "Automaton" class, after all new states are created
		this.arrivalState = null;
	}
	
	public String getLetter() {
		return letter;
	}
	
	public State getArrivalState() {
		return arrivalState;
	}
	
	public int getArrivalStateName() {
		return arrivalState.getName();
	}
	
	public void setLetter(String s) {
		this.letter = s;
	}
	
	public void setArrivalState(State state) {
		this.arrivalState = state;
	}
	
}