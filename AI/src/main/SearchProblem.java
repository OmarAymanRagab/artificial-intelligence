package main;

import java.util.ArrayList;

public abstract class SearchProblem {
	public static ArrayList<Operator> operators;
	public ArrayList<State> states;
	public SearchProblem(){
		operators = new ArrayList<Operator>();
		states = new ArrayList<State>();
	}
	public abstract boolean goalTest(Node n);
	public abstract Node genRoot();
	public abstract ArrayList<Node> expandNode(Node node);
	public abstract void visualize(Node n);
	public abstract void clearMemory();
	public boolean arrayContains(ArrayList<State> states, State newState) {
		for(int i = 0; i<states.size();i++) {
			if(equalStates(states.get(i),newState)){
				return true;
			}
		}
		return false;
	}
	public abstract boolean equalStates(State s1, State s2);
}
