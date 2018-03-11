package main;

import java.util.ArrayList;

public class HelpR2D2 extends SearchProblem {
	static Grid g;
	static int minObstacles = 2;
	static int maxObstacles = 2;
	static int minRocksAndPads = 2;
	static int maxRocksAndPads = 2;
	static int obstacles;
	static int rocksAndPads;
	static int R2Row;
	static int R2Col;
	static int TeleCol;
	static int TeleRow;
	static int RockCol;
	static int RockRow;
	static int PadCol;
	static int PadRow;
	static ArrayList<Rock>rocks= new ArrayList<Rock>();
	static ArrayList<Rock>rootRocks= new ArrayList<Rock>();
	static ArrayList<PressurePad>pads= new ArrayList<PressurePad>();
	public HelpR2D2() {
		operators.add(Operator.NORTH);
		operators.add(Operator.EAST);
		operators.add(Operator.SOUTH);
		operators.add(Operator.WEST);
		genR2();
		genTele();
		genObstacles();
		genRocksAndPads();
		genRoot();
	}
	private static void genR2() {
		R2Row = Main.genRandom(Grid.n-1,0);
		R2Col = Main.genRandom(Grid.m-1,0);
		GridObject r = new R2D2(R2Row, R2Col);
		Grid.grid[R2Row][R2Col] = r;
		System.out.println("R2D2 Location: ("+R2Row+","+R2Col+")");
	}

	private static void genObstacles() {
		obstacles = Main.genRandom(maxObstacles, minObstacles);
		for (int i = 0; i < obstacles; i++) {
			int tempObjectCol = Main.genRandom(Grid.n-1,0);
			int tempObjectRow =  Main.genRandom(Grid.m-1,0);
			while(Grid.grid[tempObjectRow][tempObjectCol]!=null) {
				tempObjectCol = Main.genRandom(Grid.n-1,0);
				tempObjectRow = Main.genRandom(Grid.m-1,0);
			}
			GridObject o = new Obstacle(tempObjectRow, tempObjectCol);
			Grid.grid[tempObjectRow][tempObjectCol] = o;
			System.out.println("Obstacle #"+(i+1)+" Location: ("+tempObjectRow+","+tempObjectCol+")");
		}
		
	}

	private static void genTele() {
		TeleCol = Main.genRandom(Grid.n-1,0);
		TeleRow =Main.genRandom(Grid.m-1,0);
		while(Grid.grid[TeleRow][TeleCol]!=null) {
			TeleCol = Main.genRandom(Grid.n-1,0);
			TeleRow = Main.genRandom(Grid.m-1,0);
		}
		GridObject t = new Teleportal(TeleCol, TeleRow);
		Grid.grid[TeleRow][TeleCol] = t;
		System.out.println("Teleportal Location: ("+TeleRow+","+TeleCol+")");
		
	}

	private static void genRocksAndPads() {
		rocksAndPads = Main.genRandom(maxRocksAndPads,minRocksAndPads);
		for (int i = 0; i < rocksAndPads; i++) {
			RockCol = Main.genRandom(Grid.n-1,0);
			RockRow = Main.genRandom(Grid.m-1,0);
			while(Grid.grid[RockRow][RockCol]!=null) {
				RockCol = Main.genRandom(Grid.n-1,0);
				RockRow = Main.genRandom(Grid.m-1,0);
			}
			GridObject o = new Rock(RockRow, RockCol);
			Grid.grid[RockRow][RockCol] = o;
			rocks.add((Rock) o);
			rootRocks.add((Rock) o);
			System.out.println("Rock #"+(i+1)+" Location: ("+RockRow+","+RockCol+")");
		}
		for (int i = 0; i < rocksAndPads; i++) {
			PadCol = Main.genRandom(Grid.n-1,0);
			PadRow = Main.genRandom(Grid.m-1,0);
			while(Grid.grid[PadRow][PadCol]!=null) {
				PadCol = Main.genRandom(Grid.n-1,0);
				PadRow = Main.genRandom(Grid.m-1,0);
			}
			GridObject o = new PressurePad(PadRow, PadCol);
			Grid.grid[PadRow][PadCol] = o;
			pads.add((PressurePad) o);
			System.out.println("Pad #"+(i+1)+" Location: ("+PadRow+","+PadCol+")");
		}
		
	}
	@Override
	public boolean goalTest(Node n1) {
		R2D2Node n = (R2D2Node) n1;
		boolean rocksOnPads=false;
		ArrayList<Rock> rocks = n.state.rocks;
		for(int i =0 ;i < rocks.size();i++){
			for(int j =0; j<pads.size();j++) {
				if(rocks.get(i).row == pads.get(j).row && rocks.get(i).col == pads.get(j).col){
					rocksOnPads=true;
					break;
				}
				if(j==pads.size()-1) {
					return false;
				}
			}
		}
		if(( n.state.R2D2Row == TeleRow && n.state.R2D2Col == TeleCol) && rocksOnPads ==true ){
			return true;
		}
		return false;
	}
	public R2D2State moveNorth(R2D2State s){
		int newRow = s.R2D2Row+1;
		if(newRow == Grid.m){
			return null;
		}
		if( Grid.grid[newRow][s.R2D2Col] instanceof Obstacle ){//If at the end of the grid or there is an obstacle then don't expand
				return null;
		}
		ArrayList<Rock> newRocks = new ArrayList<Rock>();
		for(int i = 0;i<s.rocks.size();i++) {//Copying parent's array of rocks in a new array for the child node
			int row = s.rocks.get(i).row;
			int col = s.rocks.get(i).col;
			newRocks.add(new Rock(row, col));
		}
		for(int i = 0 ; i< s.rocks.size();i++) {
			if(s.rocks.get(i).row == newRow && s.rocks.get(i).col == s.R2D2Col) {//If there is a rock in front of R2D2
				if(newRow+1 == Grid.m|| Grid.grid[newRow+1][s.R2D2Col] instanceof Obstacle|| rockInRocks(newRow+1,s.R2D2Col ,s.rocks)) {//If there is an obstacle in front of the rock -> set node to null and it will not be added to the queue in SearchAlgorithm
					return null;
				}
				//There is a rock infront of R2D2 and an empty space in front of the rock so it can be pushed
				newRocks.get(i).row++;//Change the rock's location for the child node. Parent's array of rocks remains unchanged
			}
		}
		//R2D2 will move north
		R2D2State newState = new R2D2State(newRow, s.R2D2Col,newRocks);//newRocks will either be the same as the parent's rocks or it will change; depending on if there is a rock in the way or not
		return newState;
	}
	public R2D2State moveEast(R2D2State s){
		int newCol = s.R2D2Col+1;
		if(newCol == Grid.n){
			return null;
		}
		if(Grid.grid[s.R2D2Row][newCol] instanceof Obstacle){
			return null;
		}
		ArrayList<Rock> newRocks = new ArrayList<Rock>();
		for(int i = 0;i<s.rocks.size();i++) {
			int row = s.rocks.get(i).row;
			int col = s.rocks.get(i).col;
			newRocks.add(new Rock(row, col));
		}

		for(int i = 0 ; i< s.rocks.size();i++) {
			if(s.rocks.get(i).row == s.R2D2Row && s.rocks.get(i).col == newCol) {
				if(newCol+1 == Grid.n|| Grid.grid[s.R2D2Row][newCol+1] instanceof Obstacle || rockInRocks(s.R2D2Row,newCol+1 ,s.rocks)) {
					return null;
				}
				newRocks.get(i).col++;
				break;
			}
		}
		R2D2State newState = new R2D2State(s.R2D2Row, newCol,newRocks);
		return newState;
	}
	public R2D2State moveSouth(R2D2State s){
		int newRow = s.R2D2Row-1;
		if(newRow == -1) {
			return null;
		}
			if (Grid.grid[newRow][s.R2D2Col] instanceof Obstacle){
			return null;
		}
		ArrayList<Rock> newRocks = new ArrayList<Rock>();
		for(int i = 0;i<s.rocks.size();i++) {
			int row = s.rocks.get(i).row;
			int col = s.rocks.get(i).col;
			newRocks.add(new Rock(row, col));
		}

		for(int i = 0 ; i< s.rocks.size();i++) {
			if(s.rocks.get(i).row == newRow && s.rocks.get(i).col == s.R2D2Col) {
				if(newRow-1 == -1){
					return null;
				}
					if( Grid.grid[newRow-1][s.R2D2Col] instanceof Obstacle|| rockInRocks(newRow-1,s.R2D2Col ,s.rocks)) {
					return null;
				}
				newRocks.get(i).row--;
				break;
			}
		}
		R2D2State newState = new R2D2State(newRow, s.R2D2Col,newRocks);
		return newState;
	}
	public R2D2State moveWest(R2D2State s){
		int newCol = s.R2D2Col-1;
		if(newCol == -1) {
			return null;
		}
		if(Grid.grid[s.R2D2Row][s.R2D2Col-1] instanceof Obstacle){
			return null;
		}
		ArrayList<Rock> newRocks = new ArrayList<Rock>();
		for(int i = 0;i<s.rocks.size();i++) {
			int row = s.rocks.get(i).row;
			int col = s.rocks.get(i).col;
			newRocks.add(new Rock(row, col));
		}

		for(int i = 0 ; i< s.rocks.size();i++) {
			if(s.rocks.get(i).row == s.R2D2Row && s.rocks.get(i).col == newCol) {
				if(newCol-1 == -1|| Grid.grid[s.R2D2Row][newCol-1] instanceof Obstacle||rockInRocks(s.R2D2Row,newCol-1 ,s.rocks)) {
					return null;
				}
				newRocks.get(i).col--;
				break;
			}
		}
		R2D2State newState = new R2D2State(s.R2D2Row, newCol,newRocks);
		return newState;
	}
	@Override
	public ArrayList<Node> expandNode(Node node) {
		ArrayList<Node> nodes =new ArrayList<Node>();
		R2D2State newState = moveNorth((R2D2State) node.state);
		Node n = new R2D2Node(newState,node,Operator.NORTH,node.depth+1,1);
		nodes.add(n);
		
		newState = moveEast((R2D2State) node.state);
		n = new R2D2Node(newState,node,Operator.EAST,node.depth+1,2);
		nodes.add(n);
		
		newState = moveSouth((R2D2State) node.state);
		n = new R2D2Node(newState,node,Operator.SOUTH,node.depth+1,3);
		nodes.add(n);
		
		newState = moveWest((R2D2State) node.state);
		n = new R2D2Node(newState,node,Operator.WEST,node.depth+1,4);
		nodes.add(n);
		return nodes;
	}
	private boolean rockInRocks(int row,int col,ArrayList<Rock> rocks) {
		for(int i = 0 ; i <rocks.size();i++) {
			if(rocks.get(i).row==row &&rocks.get(i).col==col) {
				return true;
			}
		}
		return false;
	}
	public boolean equalStates(State s1, State s2) {
		R2D2State state = (R2D2State) s1;
		R2D2State newState = (R2D2State) s2;
		if(state.R2D2Col == newState.R2D2Col && state.R2D2Row == newState.R2D2Row && rocksEqual(state.rocks,newState.rocks)){
			return true;

		}
		return false;
	}
	private boolean rocksEqual(ArrayList<Rock> rocks1,ArrayList<Rock> rocks2) {
		for(int i = 0; i<rocks1.size();i++) {
			if(rocks1.get(i).row!=rocks2.get(i).row||rocks1.get(i).col!=rocks2.get(i).col) {
				return false;
			}
		}
		return true;
	}
	@Override
	public Node genRoot() {
		R2D2Node root = new R2D2Node(new R2D2State(R2Row, R2Col, rootRocks), null, null, 0, 0);
	    clearMemory();
		return root;
	}
	@Override
	public void visualize(Node n1) {
		R2D2Node n = (R2D2Node) n1;
		for( int i = Grid.m-1 ; i>= 0;i--) {
			String line = "|";
			for(int j = 0 ; j< Grid.n;j++) {
				boolean rock = false;
				if(Grid.grid[i][j] instanceof Obstacle) {
					line+="OBST|";
					continue;
				}
				if(n.state.R2D2Row == i && n.state.R2D2Col ==j) {
					line+="R2D2|";
					continue;
				}
				if(Grid.grid[i][j] instanceof Teleportal) {
					line+="TELE|";
					continue;
				}
				if(Grid.grid[i][j] instanceof PressurePad) {
					line+="PAD_|";
					continue;
				}
				for(int k = 0 ;k<n.state.rocks.size();k++) {
					if(n.state.rocks.get(k).row==i && n.state.rocks.get(k).col==j) {
						line+="ROCK|";
						rock = true;
						break;
					}
				}
				if(!rock) {
					line+="EMPT|";
				}
			}
			System.out.println(line);
		}
		System.out.println("---------------------------------------");
	}
	@Override
	public void clearMemory() {
		ArrayList<Rock> newRocks = new ArrayList<Rock>();
		for(int i = 0;i<rootRocks.size();i++) {
			int row = rootRocks.get(i).row;
			int col = rootRocks.get(i).col;
			newRocks.add(new Rock(row, col));
		}
		rocks = newRocks;
		states.clear();		
	}
}
