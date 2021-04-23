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
	
	/*
	public Automaton(Automaton old) {
		Automaton newer = new Automaton(old.nbrLettersInLang, old.nbrStates, old.nbrInitialStates, old.nbrFinalStates, old.nbrTransitions, old.states);
	}
	*/
	
	private ArrayList<String> getLettersFromNbr(int nbrLettersInLang) {
		lettersInLang = new ArrayList<String>();
		for (int i = 0; i < nbrLettersInLang; i++) {
			lettersInLang.add(String.valueOf((char)(i + 97)));
		}
		System.out.println(lettersInLang);
		return lettersInLang;
	}

}
