import java.util.ArrayList;

public class Automaton {
	private int nbrLettersInLang;
	private ArrayList<String> lettersInLang;
	private int nbrStates;
	private int nbrInitialStates;
	private int nbrFinalStates;
	private ArrayList<State> states;
	private int nbrTransitions;
	
	public Automaton(int nbrLettersInLang, int nbrStates, int nbrInitialStates, int nbrFinalStates, int nbrTransitions, ArrayList<State> states) {
		this.nbrLettersInLang = nbrLettersInLang;
		this.lettersInLang = getLettersFromNbr(nbrLettersInLang);
		this.nbrStates = nbrStates;
		this.nbrInitialStates = nbrInitialStates;
		this.nbrFinalStates = nbrFinalStates;
		this.nbrTransitions = nbrTransitions;
		this.states = states;
	}
	
	public Automaton(Automaton automaton) {
		this.nbrLettersInLang = automaton.nbrLettersInLang;
		this.lettersInLang = getLettersFromNbr(nbrLettersInLang);
		this.nbrStates = automaton.nbrStates;
		this.nbrInitialStates = automaton.nbrInitialStates;
		this.nbrFinalStates = automaton.nbrFinalStates;
		this.nbrTransitions = automaton.nbrTransitions;
		this.states = new ArrayList<State>(); 
		for (State s : automaton.states) {
			this.states.add(new State(s));
		}
		
		// Transitions are completed here (and not in "Transition" class), after generating all the states
		// For each old state
		for (State oldS : automaton.states) {
			
			// We recover information from the old state
			int oldNameState = oldS.getName();
			
			// For each old transition of these old states
			for (Transition oldTransi : oldS.getTransiList()) {
				
				// We recover information from the old transition
				int nameOldArrivalState = oldTransi.getArrivalStateName();
				String oldLetter = oldTransi.getLetter();
				
				// We define two references to state (one which is the state containing the transition, the other which is the arrival state of the transition)
				State newStateWithNewTransi = null;
				State newArrivalState = null;
				
				// For each new state
				for (State newS : this.states) {
					
					// If the name of the state corresponds to an arrival state of the old transition
					if (newS.getName() == nameOldArrivalState)
						newArrivalState = newS;
					
					// If the name of the state corresponds to the name of the old state with old transition in it
					if (newS.getName() == oldNameState)
						newStateWithNewTransi = newS;
				}
				
				// In the newStateWithNewTransi
				for (Transition newT : newStateWithNewTransi.getTransiList()) {
					// We look for the new transition that has the same letter as the old transition
					if (newT.getLetter() == oldLetter) {
						newT.setArrivalState(newArrivalState);
						break;
					}
				}
				
			}
		}
	}
	
	private ArrayList<String> getLettersFromNbr(int nbrLettersInLang) {
		lettersInLang = new ArrayList<String>();
		for (int i = 0; i < nbrLettersInLang; i++) {
			lettersInLang.add(String.valueOf((char)(i + 97)));
		}
		return lettersInLang;
	}

}
