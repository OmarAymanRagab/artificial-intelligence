package main;

public class Main {

	static int minM=4;
	static int maxM=4;
	static int minN=4;
	static int maxN=4;
	public static Grid GenGrid() {
		//Grid g = new Grid(minM,maxM,minN,maxN,minObstacles,maxObstacles,minRocksAndPads,maxRocksAndPads);
		Grid g = new Grid(minM,maxM,minN,maxN);
		return g;
	}
	public static void Search(Grid g,Strategy s,Boolean visualize) {
		SearchProblem sp =new HelpR2D2();
		SearchAlgorithm sa = new SearchAlgorithm(sp, s,visualize); 
		sa.run();
	}
	public static int genRandom(int max, int min) {
		return (int) (Math.random()*(max-min+1))+min;
	}
	public static void main(String[] args) {
		Grid g = Main.GenGrid();
		Main.Search(g, Strategy.ID, true);
	}
}
