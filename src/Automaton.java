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
			for (String car : lettersInLang) {
				Launcher.print("   " + car + "   ");
			}
			Launcher.print("\n");
			for (State s : states) {
				Launcher.print("   " + s.getName() + "   ");
				// After the name of each state, we go through the letters of the alphabet
				for (String car : lettersInLang) {
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
		Launcher.println("\nCorrespondance des �tats (apr�s et avant traitement) :");
		for (Integer i : links.keySet()) {
			Launcher.println(i + " = " + links.get(i));
		}
	}
	
	/*-----------------------------------------------------------------------------
	 * Getters
	 ----------------------------------------------------------------------------*/
	
	public ArrayList<State> getStates() {
		return states;
		
	}
	
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
		if (nbrInitialStates > 1) {
			Launcher.println("\nL'automate n'est pas d�terministe car il a plus d'un �tat initial !");
			return false;
		}
		else {
			for (State s : states) {
				for (String alpha : lettersInLang) {
					int counter = 0;
					for (Transition t : s.getTransiList()) {
						if (t.getLetter().equals(alpha))
							counter++;
						if (counter == 2) {
							Launcher.println("\nL'automate n'est pas d�terministe car un �tat contient deux �tats d'arriv�e pour une m�me lettre !");
							return false;
						}
					}
				}	
			}
		}
		return true;
	}
    
	public boolean isComplete() {
		if (nbrTransitions == 0)
			return true;
		else {
			for (State s : states) {
				for (String alpha : lettersInLang) {
					boolean alphaUsed = false;
					for (Transition t : s.getTransiList()) {
						if (t.getLetter().equals(alpha))
							alphaUsed = true;
					}
					if (!alphaUsed) {
						Launcher.println("\nL'automate n'est pas complet car une lettre de l'alphabet n'a pas �t� utilis�e dans l'une des transitions !");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean isStandard() {
		if (nbrInitialStates > 1)
			return false;			
		else {
			for (State S : states) {
				for (Transition T : S.getTransiList()) {
					if (getStateFromName(T.getArrivalStateName()).isInitial())
						return false;
				}
			}
		}
		return true;
	}
    
	public boolean isAsynchron() {
		for (State s : states) {
			for (Transition t : s.getTransiList()) {
				if (t.getLetter().equals("*")) {
					Launcher.println("\nL'automate est asynchrone car une transition epsilon a �t� trouv�e !\n\n");
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
		for (Transition t : next.getTransiList()) {
			current.setTransi(t.getLetter(), t.getArrivalStateName());
			current.incrementNbrTransi();
		}
	}
	
	public void updateFinalAndInitialNbr() {
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
	
	public void removeUnlinkedStates() {
		ArrayList<State> statesToRemove = new ArrayList<State>();
		
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
		
		for (State s : statesToRemove) {
			if (!s.isInitial()) {
				states.remove(s);
				nbrStates --;
			}
		}
	}
	
	public void supAsynchron() {
        for (State S : states) {
            for (State X : states) {
            	if (S.getName() != X.getName()) {
            		ArrayList<Transition> transitionsToRemove = new ArrayList<Transition>();
            		boolean copy = false;
            		
	            	for (Transition t : S.getTransiList()) {
	                    if ((t.getArrivalStateName() == X.getName()) && t.getLetter().equals("*")) {
	                    	if (X.isFinal())
	    	            		S.setFinal(true);
	    	            	if (X.isInitial())
	    	            		S.setInitial(true);
	                    	transitionsToRemove.add(t);
	                    	copy = true;
	                    }
	                }
	            	
	            	if (copy)
	            		copyAndAddTransi(S, X);
	            	
	            	for (Transition t : transitionsToRemove) {
	            		S.getTransiList().remove(t);
	            		S.decrementNbrTransi();
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
			Launcher.println("L'automate est d�j� standard !");
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
		states.add(new State(nbrStates, false, true, 0, new ArrayList<Transition>()));
		nbrStates ++;
		
		// We check all initialStates and add them in stateCollection
		for (State s : states) {
			if (s.isInitial() && !s.equals(states.get(nbrStates-1)))
				stateCollection.add(s);
		}
		// We sort stateCollection by state name
		stateCollection.sort(Comparator.comparing(State::getName));
		// We put new state as key and stateCollection as value in associatedStates
		associatedStates.put(states.get(nbrStates-1), stateCollection);
		links.put(states.get(nbrStates-1).getName(), nameListFromStateList(stateCollection));
		// We add the new state in statesToDo list
		statesToDo.add(states.get(nbrStates-1));
		
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
						states.add(new State(nbrStates, false, false, 0, new ArrayList<Transition>()));
						nbrStates ++;
						associatedStates.put(states.get(nbrStates-1), stateCollection);
						links.put(states.get(nbrStates-1).getName(), nameListFromStateList(stateCollection));
						// We also add this new merged state in statesToDo
						statesToDo.add(states.get(nbrStates-1));
					}
					// We create the new transition of mergedState with letter alpha to new state created
					currentMergedState.getTransiList().add(new Transition(alpha, states.get(nbrStates-1).getName()));
					currentMergedState.incrementNbrTransi();
					nbrTransitions ++;
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
		states.add(new State(nbrStates-1, false , false, nbrLettersInLang, new ArrayList<Transition>() ) );
		State sTrash = states.get(nbrStates-1);
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
		// returnParts is List in List in List because it will return all parts (that are List of List)
		ArrayList<ArrayList<State>> partsBis = new ArrayList<ArrayList<State>>();
		
		partsBis.add(getTerminalStates());
		partsBis.add(getNonTerminalStates());
		
		ArrayList<ArrayList<State>> parts = new ArrayList<ArrayList<State>>();
		
		// We reset numbers of transitions
		nbrTransitions = 0;
		
		int increment = 1;
		Launcher.println("\nD�tail des partitions :");
		
		do {
			// We copy partsBis in parts
			parts = new ArrayList<ArrayList<State>>(partsBis);
			// We reset partsBis (it will be the next parts)
			partsBis = new ArrayList<ArrayList<State>>();
			
			Launcher.print(increment + ") ");
			for (ArrayList<State> stateListForDisplay : parts) {
				ArrayList<Integer> integerPart = nameListFromStateList(stateListForDisplay);
				Launcher.print(integerPart + " ");
			}
			Launcher.print("\n");
			
			for (ArrayList<State> states : parts) {
				HashMap<State, ArrayList<Integer>> lines = new HashMap<State, ArrayList<Integer>>();
				
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
