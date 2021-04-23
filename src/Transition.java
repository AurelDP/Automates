
public class Transition {
	private String letter;
	private State arrivalState;
	
	public Transition(String letter, State arrivalState) {
		this.letter = letter;
		this.arrivalState = arrivalState;
	}
	
	public String getLetter() {
		return letter;
	}
	
	public State getArrivalState() {
		return arrivalState;
	}
	
}
