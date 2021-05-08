import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;


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
	}
	
	// The number of letters is converted into an alphabet, by the ASCII code and a loop
	private ArrayList<String> getLettersFromNbr(int nbrLettersInLang) {
		lettersInLang = new ArrayList<String>();
		for (int i = 0; i < nbrLettersInLang; i++) {
			lettersInLang.add(String.valueOf((char)(i + 97)));
		}
		return lettersInLang;
	}
	
	/*-----------------------------------------------------------------------------
	 * Display methods
	 ----------------------------------------------------------------------------*/
	
	public void display() {
		// If the automaton is asynchronous, we copy lettersInLang and we add in the copy "*"
		// in order to display "*" in the table
		ArrayList<String> copyLettersInLang = new ArrayList<String>(lettersInLang);
		for (State s : states) {
			for (Transition t : s.getTransiList()) {
				if (t.getLetter().equals("*")) {
					copyLettersInLang.add("*");
					break;
				}
			}
			if (copyLettersInLang.contains("*"))
				break;
		}
		
		Launcher.println("\nAffichage de l'automate :\n");
		
		if (nbrInitialStates == 1) {
			Launcher.print("Etat initial : ");
			// We loop all the states and if a state is initial, we display it
			for (State s : states) {
				if (s.isInitial())
					Launcher.println("[" + s.getName() + "]");
			}
		// If there are more than 2 initial states, we apply a slightly different display
		} else if (nbrInitialStates >= 2) {
			Launcher.print("Etats initiaux : [");
			int i = 0;
			for (State s : states) {
				if (s.isInitial() && i < nbrInitialStates-1) {
					Launcher.print(s.getName() + ",");
					i ++;
				} else if (s.isInitial() && i == nbrInitialStates-1) {
					Launcher.println(s.getName() + "]");
					i ++;
				}
			}
		}
		
		// Same principle as for the initial states
		if (nbrFinalStates == 1) {
			Launcher.print("Etat final : ");
			for (State s : states) {
				if (s.isFinal())
					Launcher.println("[" + s.getName() + "]");
			}
		} else if (nbrFinalStates >= 2) {
			Launcher.print("Etats finaux : [");
			int i = 0;
			for (State s : states) {
				if (s.isFinal() && i < nbrFinalStates-1) {
					Launcher.print(s.getName() + ",");
					i ++;
				} else if (s.isFinal() && i == nbrFinalStates-1) {
					Launcher.println(s.getName() + "]");
					i ++;
				}
			}
		}
		
		if (nbrTransitions >= 1) {
			Launcher.print("Table de transitions :\n       ");
			for (String car : copyLettersInLang) {
				Launcher.print("   " + car + "   ");
			}
			Launcher.print("\n");
			for (State s : states) {
				Launcher.print("   " + s.getName() + "   ");
				// After the name of each state, we go through the letters of the alphabet
				for (String car : copyLettersInLang) {
					Launcher.print("   ");
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
					Launcher.print(arrivalState + "   ");
				}
				Launcher.print("\n");
			}
		} else {
			Launcher.println("Pas de transitions pour cet automate !");
		}
		
	}
	
	public void displayDeterministOrMinimalistLinks(HashMap<Integer, ArrayList<Integer>> links) {
		Launcher.println("\nCorrespondance des états (après et avant traitement) :");
		// We simply loop through the HashMap to display new state linked with old states
		for (Integer i : links.keySet()) {
			Launcher.println(i + " = " + links.get(i));
		}
	}
	
	/*-----------------------------------------------------------------------------
	 * Getters
	 ----------------------------------------------------------------------------*/
	
	public ArrayList<State> getStates() { return states; }
	
	// This method return the state associate at the name given in entry 
	public State getStateFromName(int name) {
		for (State s : states) {
			if (s.getName() == name)
				return s;
		}
		return null;
	}
	
	/*-----------------------------------------------------------------------------
	 * Tests on automatons
	 ----------------------------------------------------------------------------*/
    
	public boolean isDeterminist() {
		// If there is more than one initial state, the automaton is not determinist
		if (nbrInitialStates > 1) {
			Launcher.println("\nL'automate n'est pas déterministe car il a plus d'un état initial !");
			return false;
		} else {
			// Else, if for one state, there is more than one transition with the same letter
			// the automaton is not determinist either
			for (State s : states) {
				for (String alpha : lettersInLang) {
					int counter = 0;
					for (Transition t : s.getTransiList()) {
						if (t.getLetter().equals(alpha))
							counter++;
						if (counter == 2) {
							Launcher.println("\nL'automate n'est pas déterministe car un état contient deux états d'arrivée pour une même lettre !");
							return false;
						}
					}
				}	
			}
		}
		return true;
	}
    
	public boolean isComplete() {
		// For each state, we check if all letters are used in transition. If there is a letter unused, then we return false
		for (State s : states) {
			for (String alpha : lettersInLang) {
				boolean alphaUsed = false;
				for (Transition t : s.getTransiList()) {
					if (t.getLetter().equals(alpha))
						alphaUsed = true;
				}
				if (!alphaUsed) {
					Launcher.println("\nL'automate n'est pas complet car une lettre de l'alphabet n'a pas été utilisée dans l'une des transitions !");
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isStandard() {
		// If there is more than one initial state, then the automaton is not standard
		if (nbrInitialStates > 1)
			return false;			
		else {
			// Else we simply check if there is a transition that point on the initial state
			// If there is, we return false
			for (State S : states) {
				for (Transition T : S.getTransiList()) {
					if (getStateFromName(T.getArrivalStateName()).isInitial())
						return false;
				}
			}
		}
		return true;
	}
    
	public boolean isAsynchronous() {
		// If we find transition with letter "*", then the automaton is asynchronous
		for (State s : states) {
			for (Transition t : s.getTransiList()) {
				if (t.getLetter().equals("*")) {
					Launcher.println("\nL'automate est asynchrone car une transition epsilon a été trouvée !\n\n");
					return true;
				}
			}
		}
		return false;
	}
	
	/*-----------------------------------------------------------------------------
	 * Other methods
	 ----------------------------------------------------------------------------*/
	
	private void copyAndAddTransi(State current, State next) {
		// This method is used to copy all transitions of nextState and add them in currentStante transitions list
		for (Transition t : next.getTransiList()) {
			current.setTransi(t.getLetter(), t.getArrivalStateName());
			current.incrementNbrTransi();
		}
	}
	
	public void updateFinalAndInitialNbr() {
		// This method is called when the number of final and initial states must be updated 
		nbrInitialStates = 0;
		nbrFinalStates = 0;
		for (State s : states) {
			if (s.isFinal())
				nbrFinalStates ++;
			if (s.isInitial())
				nbrInitialStates ++;
		}
	}
	
	/*-----------------------------------------------------------------------------
	 * Methods used by asynchronous system
	 ----------------------------------------------------------------------------*/
	
	// This method is used in supAsynchronous to delete all states that are not pointed
	// by any transition (there is no way to go in this state from the initial state)
	private void removeUnlinkedStates() {
		ArrayList<State> statesToRemove = new ArrayList<State>();
		
		// We check, for a currentState, if there is an other state that is pointed to this currentState
		// If there is no state pointed on it, we add currentState to the list statesToRemove
		for (State currentState : states) {
			boolean unlinked = true;
			for (State other : states) {
            	if (currentState.getName() != other.getName()) {
            		for (Transition t : other.getTransiList()) {
            			if (t.getArrivalStateName() == currentState.getName()) {
            				unlinked = false;
            				break;
            			}
            		}
            	}
            	if (!unlinked)
            		break;
            }
			if (unlinked)
				statesToRemove.add(currentState);
		}
		
		// Then, for each state in statesToRemove, if the state is not initial (because initial state can be pointed by 0 states
		// but is still accessible), we remove it
		for (State s : statesToRemove) {
			if (!s.isInitial()) {
				states.remove(s);
				nbrStates --;
			}
		}
	}
	
	public void supAsynchronous() {
		// For each state of the automaton
        for (State currentState : states) {
        	// We look all others states
            for (State other : states) {
            	if (currentState.getName() != other.getName()) {
            		ArrayList<Transition> transitionsToRemove = new ArrayList<Transition>();
            		boolean copy = false;
            		
            		// For all other state, if there is a transition epsilon from currentState to other, we break it
            		// and we copy all transitions of other and we add them in currentState
            		// If other was final or initial, currentState will be final or initial too
	            	for (Transition t : currentState.getTransiList()) {
	                    if ((t.getArrivalStateName() == other.getName()) && t.getLetter().equals("*")) {
	                    	if (other.isFinal())
	                    		currentState.setFinal(true);
	    	            	if (other.isInitial())
	    	            		currentState.setInitial(true);
	                    	transitionsToRemove.add(t);
	                    	copy = true;
	                    }
	                }
	            	
	            	if (copy)
	            		copyAndAddTransi(currentState, other);
	            	
	            	for (Transition t : transitionsToRemove) {
	            		currentState.getTransiList().remove(t);
	            		currentState.decrementNbrTransi();
	            	}

            	}
            }
        }
        removeUnlinkedStates();
        updateFinalAndInitialNbr();
	}
	
	/*-----------------------------------------------------------------------------
	 * Complementary method
	 ----------------------------------------------------------------------------*/
	
	public void complementaryAutomaton() {
		// If the automaton is not complete, we have to complete it before make it's complementary
		if (!this.isComplete())
			this.completion();
		// Then we change finals states to non-finals and non-finals ones to finals
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
	
	/*-----------------------------------------------------------------------------
	 * Standardization method
	 ----------------------------------------------------------------------------*/
	
	public void standardization() {
		if (!isStandard()) {
			ArrayList<Transition> listTrans = new ArrayList<Transition>();
			boolean sFinal = false;
			for (State S : states) {
				if (S.isInitial()) {
					if (S.isFinal()) 
						sFinal = true;
					for (Transition T : S.getTransiList()) {
						listTrans.add(new Transition(T));
						nbrTransitions ++;
					}
					S.setInitial(false);
				}
			}
			states.add(new State(nbrStates, sFinal, true, nbrLettersInLang, listTrans));
			nbrInitialStates ++;
			nbrStates ++;
			updateFinalAndInitialNbr();
		} else
			Launcher.println("L'automate est déjà standard !");
	}
	
	/*-----------------------------------------------------------------------------
	 * Methods used with determinization
	 ----------------------------------------------------------------------------*/
	
	// This method is called when the state is supposed to be not in any transition
	// (especially called in determinization)
	private void removeStateFromName(int name) {
		int i = 0;
		for (State s : states) {
			if (s.getName() == name) {
				states.remove(i);
				nbrStates --;
				break;
			}
			i ++;
		}
	}
	
	private ArrayList<Integer> nameListFromStateList(ArrayList<State> states) {
		ArrayList<Integer> names = new ArrayList<Integer>();
		for (State s : states) {
			names.add(s.getName());
		}
		return names;
	}
	
	public HashMap<Integer, ArrayList<Integer>> determinization() {
		HashMap<State, ArrayList<State>> associatedStates = new HashMap<State, ArrayList<State>>();
		HashMap<Integer, ArrayList<Integer>> links = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<State> statesToDo = new ArrayList<State>();
		
		// We reset numbers of transitions
		nbrTransitions = 0;
		
		ArrayList<State> stateCollection = new ArrayList<State>();
		states.add(new State(getMaxStateName()+1, false, true, 0, new ArrayList<Transition>()));
		nbrStates ++;
		
		// We check all initialStates and add them in stateCollection
		for (State s : states) {
			if (s.isInitial() && !s.equals(states.get(states.size()-1)))
				stateCollection.add(s);
		}
		// We sort stateCollection by state name
		stateCollection.sort(Comparator.comparing(State::getName));
		// We put new state as key and stateCollection as value in associatedStates
		associatedStates.put(states.get(states.size()-1), stateCollection);
		links.put(states.get(states.size()-1).getName(), nameListFromStateList(stateCollection));
		// We add the new state in statesToDo list
		statesToDo.add(states.get(states.size()-1));
		
		// While there are states in statesToDo list, we recover the first state of the list
		while (statesToDo.size() != 0) {
			State currentMergedState = statesToDo.get(0);
			
			// For each letter of the alphabet
			for (String alpha : lettersInLang) {
				// We reset the stateCollection
				stateCollection = new ArrayList<State>();
				
				// And for each old state of the new merged state
				for (State stateBeforeMerge : associatedStates.get(currentMergedState)) {
					
					// If the letter of the transition is the same than alpha, we add the arrivalState to the stateCollection list
					for (Transition t : stateBeforeMerge.getTransiList()) {
						if (alpha.equals(t.getLetter()) && !stateCollection.contains(getStateFromName(t.getArrivalStateName())))
							stateCollection.add(getStateFromName(t.getArrivalStateName()));
					}
				}
				
				// We sort stateCollection by state name (it allows us to compare easily stateCollection with
				// values in associatedStates HashMap
				stateCollection.sort(Comparator.comparing(State::getName));
				
				// If stateCollection is not already in values of HashMap, then we can add a new merged state and put it with stateCollection in associatedStates
				boolean isAlreadyIn = false;
				for (State s : associatedStates.keySet()) {
					if (associatedStates.get(s).equals(stateCollection))
						isAlreadyIn = true;
				}
				
				// If there is something in stateCollection, we create a new state that is the merge of states in stateCollection
				if (stateCollection.size() > 0) {
					if (!isAlreadyIn) {
						states.add(new State(getMaxStateName()+1, false, false, 0, new ArrayList<Transition>()));
						nbrStates ++;
						associatedStates.put(states.get(states.size()-1), stateCollection);
						links.put(states.get(states.size()-1).getName(), nameListFromStateList(stateCollection));
						// We also add this new merged state in statesToDo
						statesToDo.add(states.get(states.size()-1));
						
						// If the new state is not in the list, we add it as new transition (because the state is created by the transition itself)
						currentMergedState.getTransiList().add(new Transition(alpha, states.get(states.size()-1).getName()));
						currentMergedState.incrementNbrTransi();
						nbrTransitions ++;
					} else {
						// If the new state is already in the list, then we have to find the key associated with stateCollection and add this key as a new transition
						for (State s : associatedStates.keySet()) {
							if (associatedStates.get(s).equals(stateCollection)) {
								currentMergedState.getTransiList().add(new Transition(alpha, s.getName()));
								currentMergedState.incrementNbrTransi();
								nbrTransitions ++;
								break;
							}
						}
						
					}
				}
			}
			
			// We can finally remove the first index of statesToDo
			statesToDo.remove(0);
		}
		
		// We put final states in associatedStates
		for (State mergedState : associatedStates.keySet()) {
			for (State s : associatedStates.get(mergedState)) {
				if (s.isFinal() && !mergedState.isFinal()) {
					mergedState.setFinal(true);
				}
				// Then, we remove all value states in the automaton to only keep key states
				this.removeStateFromName(s.getName());
			}
		}
		
		updateFinalAndInitialNbr();
		
		return links;
	}
	
	/*-----------------------------------------------------------------------------
	 * Completion method
	 ----------------------------------------------------------------------------*/
    
	public void completion() {
		nbrStates ++;
		states.add(new State(getMaxStateName()+1, false , false, nbrLettersInLang, new ArrayList<Transition>() ) );
		State sTrash = states.get(states.size()-1);
		for (int i = 0 ; i<nbrLettersInLang ; i++ ) {
			sTrash.getTransiList().add(new Transition(lettersInLang.get(i), sTrash.getName()));
		}
		
		for (State s : states) {
			ArrayList<String> copyAlpha = new ArrayList<String>(lettersInLang);
			if (s.getNbrTransitions() != nbrLettersInLang) {
				for (Transition t : s.getTransiList()) {
					Iterator<String> it  = copyAlpha.iterator();
					
					while (it.hasNext()) { // loop the list copyAlpha and remove during it
						String i = it.next();
						if (t.getLetter().equals(i)) {
							it.remove();
						}
					}
				}
				
				for (String x : copyAlpha) {
					s.getTransiList().add(new Transition(x, sTrash.getName()));
				}
			}
		}
	}
	
	/*-----------------------------------------------------------------------------
	 * Methods used in minimization
	 ----------------------------------------------------------------------------*/
	
	private ArrayList<State> getTerminalStates() {
		ArrayList<State> terminalStates = new ArrayList<State>();
		
		for (State s : states) {
			if (s.isFinal())
				terminalStates.add(s);
		}
		
		return terminalStates;
	}
	
	private ArrayList<State> getNonTerminalStates() {
		ArrayList<State> nonTerminalStates = new ArrayList<State>();
		
		for (State s : states) {
			if (!s.isFinal())
				nonTerminalStates.add(s);
		}
		
		return nonTerminalStates;
	}
	
	private int getMaxStateName() {
		int maxName = 0;
		
		for (State s : states) {
			if (s.getName() > maxName)
				maxName = s.getName();
		}
		
		return maxName;
	}
	
	public HashMap<Integer, ArrayList<Integer>> minimization() {
		// parts and partsBis are List of List of states and they will be used to save groupes of states that we can potentialy merge
		ArrayList<ArrayList<State>> partsBis = new ArrayList<ArrayList<State>>();
		
		partsBis.add(getTerminalStates());
		partsBis.add(getNonTerminalStates());
		
		ArrayList<ArrayList<State>> parts = new ArrayList<ArrayList<State>>();
		
		// We reset numbers of transitions
		nbrTransitions = 0;
		
		// Increment is only used to display groups of states during the minimization
		int increment = 1;
		Launcher.println("\nDétail des partitions :");
		
		// While parts and partsBis (parts is the old group, partsBis is the current group) are different
		// then we do the algorithm
		do {
			// We copy partsBis in parts
			parts = new ArrayList<ArrayList<State>>(partsBis);
			// We reset partsBis (it will be the next parts)
			partsBis = new ArrayList<ArrayList<State>>();
			
			// Simply display groups of potentialy merged states
			Launcher.print(increment + ") ");
			for (ArrayList<State> stateListForDisplay : parts) {
				ArrayList<Integer> integerPart = nameListFromStateList(stateListForDisplay);
				Launcher.print(integerPart + " ");
			}
			Launcher.print("\n");
			
			// For each list of states in part
			for (ArrayList<State> states : parts) {
				// We recover the line
				HashMap<State, ArrayList<Integer>> lines = new HashMap<State, ArrayList<Integer>>();
				
				// The line will be
				for (State state : states) {
					ArrayList<Integer> integerList = new ArrayList<Integer>();
					
					for (String alpha : lettersInLang) {
						Integer indexArrivalList = 0;
						
						for (Transition t : state.getTransiList()) {
							
							if (t.getLetter().equals(alpha)) {
								
								for (ArrayList<State> statesOfParts : parts) {
									if (statesOfParts.contains(getStateFromName(t.getArrivalStateName()))) {
										indexArrivalList = parts.indexOf(statesOfParts);
										break;
									}
								}
								break;
							}
							
						}
						integerList.add(indexArrivalList);
					}
					lines.put(state, integerList);
				}
				
				while (lines.size() > 0) {
					
					ArrayList<Integer> tempIndexList = new ArrayList<Integer>();
					tempIndexList = lines.get(lines.keySet().toArray()[0]);
					
					ArrayList<State> tempState = new ArrayList<State>();
					ArrayList<State> tempStateToRemove = new ArrayList<State>();
					
					for (State s : lines.keySet()) {
						if (lines.get(s).equals(tempIndexList) && s != lines.keySet().toArray()[0]) {
							tempState.add(s);
							tempStateToRemove.add(s);
						}
					}
					
					for (State s : tempStateToRemove) {
						lines.remove(s);
					}
					
					tempState.add((State) lines.keySet().toArray()[0]);
					lines.remove(lines.keySet().toArray()[0]);
					partsBis.add(tempState);
				}
			}
			
			increment ++;
			
			// We sort lists in parts and partsBis by state name in order to compare them
			for (ArrayList<State> list : parts) {
				list.sort(Comparator.comparing(State::getName));
			}
			for (ArrayList<State> list : partsBis) {
				list.sort(Comparator.comparing(State::getName));
			}
			
			
		} while (!parts.equals(partsBis));
		
		HashMap<Integer, ArrayList<Integer>> linkedStates = new HashMap<Integer, ArrayList<Integer>>();
		
		for (ArrayList<State> statesList : partsBis) {
			ArrayList<Integer> namesOfStates = nameListFromStateList(statesList);
			boolean isInitial = false;
			boolean isFinal = false;
			
			for (State s : statesList) {
				if (s.isFinal()) {
					isFinal = true;
					break;
				}
			}
			
			for (State s : statesList) {
				if (s.isInitial()) {
					isInitial = true;
					break;
				}
			}
			
			// The automaton is complete so nbrTransitions = nbrLettersInLang for each state
			states.add(new State(getMaxStateName()+1, isFinal, isInitial, nbrLettersInLang, new ArrayList<Transition>()));
			nbrStates ++;
			
			linkedStates.put(getMaxStateName(), namesOfStates);
		}
		
		for (State s : states) {
			for (Integer i : linkedStates.keySet()) {
				
				if (s.getName() == i) {
					int value = (int) linkedStates.get(i).toArray()[0];
					State oldState = getStateFromName(value);
					
					for (String alpha : lettersInLang) {
						for (Transition t : oldState.getTransiList()) {
							
							if (t.getLetter().equals(alpha)) {
								
								for (Integer key : linkedStates.keySet()) {
									if (linkedStates.get(key).contains(t.getArrivalStateName())) {
										s.setTransi(alpha, key);
										nbrTransitions ++;
										break;
									}
								}
								
							}
							
						}
					}
					
				}
				
			}
		}
		
		for (ArrayList<State> sList : partsBis) {
			for (State s : sList) {
				removeStateFromName(s.getName());
			}
		}
		
		updateFinalAndInitialNbr();
				
		return linkedStates;
	}
}
