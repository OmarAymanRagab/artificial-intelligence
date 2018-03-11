package main;

public class Grid {
	public static GridObject[][]grid;
	static int m;
	static int n;
	static int obstacles;
	static int rocksAndPads;
	int minM;
	int maxM;
	int minN;
	int maxN;
	public Grid(int minM2, int maxM2, int minN2, int maxN2) {
		this.minM = minM2;
		this.maxM = maxM2;
		this.minN = minN2;
		this.maxN = maxN2;
		m = Main.genRandom(maxM,minM);
		n = Main.genRandom(maxN,minN);
		grid = new GridObject[m][n];
		System.out.println("Grid size: "+m+"x"+n);
	}
}
