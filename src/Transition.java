
public class Transition {
	private String letter;
	private int nameArrivalState;
	
	public Transition(String letter, int nameArrivalState) {
		this.letter = letter;
		this.nameArrivalState = nameArrivalState;
	}
	
	public Transition(Transition transition) {
		this.letter = new String(transition.letter);
		this.nameArrivalState = transition.nameArrivalState;
	}
	
	public String getLetter() {
		return letter;
	}
		
	public int getArrivalStateName() {
		return nameArrivalState;
	}
	
	public void setLetter(String s) {
		this.letter = s;
	}
	
	public void setArrivalStateName(int stateName) {
		this.nameArrivalState = stateName;
	}
	
}