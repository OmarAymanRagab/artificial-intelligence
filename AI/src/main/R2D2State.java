package main;

import java.util.ArrayList;

public class R2D2State extends State{
	int R2D2Row;
	int R2D2Col;
	ArrayList<Rock> rocks;
	public R2D2State(int row, int col , ArrayList<Rock> rocks) {
		this.R2D2Col = col;
		this.R2D2Row = row;
		this.rocks=rocks;
	}
	
	

}
