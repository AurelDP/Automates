import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;

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
					if (newT.getLetter().equals(oldLetter)) {
						newT.setArrivalState(newArrivalState);
						break;
					}
				}
				
			}
		}
	}
	
	// The number of letters is converted into an alphabet, by the ASCII code and a loop
	private ArrayList<String> getLettersFromNbr(int nbrLettersInLang) {
		lettersInLang = new ArrayList<String>();
		for (int i = 0; i < nbrLettersInLang; i++) {
			lettersInLang.add(String.valueOf((char)(i + 97)));
		}
		return lettersInLang;
	}
	
	public void display() {
		System.out.println("\nAffichage de l'automate :\n");
		
		if (nbrInitialStates == 1) {
			System.out.print("Etat initial : ");
			// We loop all the states and if a state is initial, we display it
			for (State s : states) {
				if (s.isInitial())
					System.out.println("[" + s.getName() + "]");
			}
		// If there are more than 2 initial states, we apply a slightly different display
		} else if (nbrInitialStates >= 2) {
			System.out.print("Etats initiaux : [");
			int i = 0;
			for (State s : states) {
				if (s.isInitial() && i < nbrInitialStates-1) {
					System.out.print(s.getName() + ",");
					i ++;
				} else if (s.isInitial() && i == nbrInitialStates-1) {
					System.out.println(s.getName() + "]");
					i ++;
				}
			}
		}
		
		// Same principle as for the initial states
		if (nbrFinalStates == 1) {
			System.out.print("Etat final : ");
			for (State s : states) {
				if (s.isFinal())
					System.out.println("[" + s.getName() + "]");
			}
		} else if (nbrFinalStates >= 2) {
			System.out.print("Etats finaux : [");
			int i = 0;
			for (State s : states) {
				if (s.isFinal() && i < nbrFinalStates-1) {
					System.out.print(s.getName() + ",");
					i ++;
				} else if (s.isFinal() && i == nbrFinalStates-1) {
					System.out.println(s.getName() + "]");
					i ++;
				}
			}
		}
		
		if (nbrTransitions >= 1) {
			System.out.print("Table de transitions :\n       ");
			for (String car : lettersInLang) {
				System.out.print("   " + car + "   ");
			}
			System.out.print("\n");
			for (State s : states) {
				System.out.print("   " + s.getName() + "   ");
				// After the name of each state, we go through the letters of the alphabet
				for (String car : lettersInLang) {
					System.out.print("   ");
					String arrivalState = "-";
					int i = 0;
					// For each transition of the state
					for (Transition t : s.getTransiList()) {
						// If a transition has the same letter as the character in the transition table
						if (t.getLetter().equals(car)) {
							// The number of the arrival state is displayed
							if (i == 0)
								arrivalState = String.valueOf(t.getArrivalStateName());
							// If there is more than one transition with the same letter, then we put it in the arrivalState string
							else
								arrivalState += ("," + String.valueOf(t.getArrivalStateName()));
							i ++;
						}
					}
					System.out.print(arrivalState + "   ");
				}
				System.out.print("\n");
			}
		} else {
			System.out.println("Pas de transitions pour cet automate !");
		}
		
	}
	
	public ArrayList<State> getStates() {
		return states;
	}
	
	
	public void complementaryAutomaton() {
		// faire complet 
		for (State S : states) {
			if (S.isFinal()) {
				S.setFinal(false);
				nbrFinalStates --;
			} else {
				S.setFinal(true);
				nbrFinalStates ++;
			}
		}
	}
	
	public boolean isStandard() {
		if (nbrInitialStates > 1)
			return false;			
		else {
			for (State S : states) {
				for (Transition T : S.getTransiList()) {
					if (T.getArrivalState().isInitial())
						return false;
				}
			}
		}
		return true;
	}
	
	public void standardization() {
		if (!isStandard()) {
			ArrayList<Transition> listTrans = new ArrayList<Transition>();
			boolean sFinal = false;
			for (State S : states) {
				if (S.isInitial()) {
					if (S.isFinal()) 
						sFinal = true;
					for (Transition T : S.getTransiList()) {
						listTrans.add(new Transition(T.getLetter(), T.getArrivalState()));
						nbrTransitions ++;
					}
					S.setInitial(false);
					nbrInitialStates --;
				}
			}
			if (sFinal)
				nbrFinalStates ++;
			states.add(new State(nbrStates, sFinal, true, nbrLettersInLang, listTrans));
			nbrStates ++;
		} else
			System.out.println("L'automate est déjà standard !");
	}
	
	public void determinization() {
		HashMap<State, ArrayList<State>> associatedStates = new HashMap<State, ArrayList<State>>();
		
		ArrayList<State> stateCollection = new ArrayList<State>();
		states.add(new State(nbrStates, false, true, 0, new ArrayList<Transition>()));
		nbrStates ++;
		
		// We check all initialStates
		for (State s : states) {
			if (s.isInitial() && !s.equals(states.get(nbrStates-1)))
				stateCollection.add(s);
		}
		associatedStates.put(states.get(nbrStates-1), stateCollection);
		
		// For each state that is key in HashMap (which is supposed to be a new state)
		for (State s : associatedStates.keySet()) {
			// For each letter in alphabet
			for (String c : lettersInLang) {
				// For each old state associated with the new one
				stateCollection = new ArrayList<State>();
				int i = 0;
				for (State mixedState : associatedStates.get(s)) {
					// For each transition in this old state
					for (Transition t : mixedState.getTransiList()) {
						// If letters are equals, we increment i
						if (t.getLetter().equals(c))
							i ++;
					}
				}
				// If there is more than one transition with same letter, we create
				// a new state and we link it with arrivalStates in these transitions
				if (i > 1) {
					states.add(new State(nbrStates, false, false, 0, new ArrayList<Transition>()));
					nbrStates ++;
					
					for (State mixedState : associatedStates.get(s)) {
						for (Transition t : mixedState.getTransiList()) {
							if (t.getLetter().equals(c))
								stateCollection.add(t.getArrivalState());
						}
					}
					associatedStates.put(states.get(nbrStates-1), stateCollection);
				// Else, we simply add the arrivalState to associatedStates (because arrivalState is already
				// created and doesn't need to be mixed with other state)
				} else {
					for (State mixedState : associatedStates.get(s)) {
						for (Transition t : mixedState.getTransiList()) {
							if (t.getLetter().equals(c)) {
								stateCollection.add(t.getArrivalState());
								// If the new state is not already in the associatedStates
								// (make the "while" stop when there is no new state in associatedStates)
								if (!associatedStates.containsKey(t.getArrivalState()))
									associatedStates.put(t.getArrivalState(), stateCollection);
								break;
							}
						}
					}
				}
			}
		}
	}
}
