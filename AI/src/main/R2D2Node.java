package main;

public class R2D2Node extends Node {
	public R2D2State state;
	public R2D2Node(R2D2State st, Node pn, Operator op, int d, int p) {
		super(st, pn, op, d, p);
		state = st;
	}
}
