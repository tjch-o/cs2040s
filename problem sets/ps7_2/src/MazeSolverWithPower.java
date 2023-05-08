import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};
	private static final int NUM_DIRECTIONS = 4;

	private Maze maze;
	private boolean solved;
	private boolean[][] visited;
	private boolean[][][] superpowerVisited;
	private int superpowers = 0;
	private Queue<Point> points;
	private int shortestPath;
	private ArrayList<Integer> reachable;

	public MazeSolverWithPower() {
		this.maze = null;
		this.points = new LinkedList<>();
		this.reachable = new ArrayList<>();
	}

	private class Point {
		private int x;
		private int y;
		private Point parent;
		private int superpowers;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
			this.parent = null;
		}

		public Point(int x, int y, int superpowers) {
			this.x = x;
			this.y = y;
			this.parent = null;
			this.superpowers = superpowers;
		}

		public void setParent(Point p) {
			this.parent = p;
		}
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()];
		this.superpowerVisited = new boolean[this.maze.getRows()][this.maze.getColumns()][superpowers];
		this.solved = false;
	}

	private boolean canGo(int row, int col, int dir) {
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows())
			return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns())
			return false;

		switch (dir) {
			case NORTH:
				return !maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !maze.getRoom(row, col).hasWestWall();
		}
		return false;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		for (int i = 0; i < this.maze.getRows(); i += 1) {
			for (int j = 0; j < this.maze.getColumns(); j += 1) {
				this.visited[i][j] = false;
				this.maze.getRoom(i, j).onPath = false;
			}
		}

		this.solved = false;
		this.shortestPath = 0;
		this.reachable = new ArrayList<>();
		this.points = new LinkedList<>();

		this.visited[startRow][startCol] = true;
		this.maze.getRoom(startRow, startCol).onPath = true;
		Point startingPoint = new Point(startRow, startCol);
		points.add(startingPoint);

		Point currentPoint;
		Point flag = null;
		int count = 0;

		while (!points.isEmpty()) {
			int numberOfRemainingPoints = points.size();
			this.reachable.add(numberOfRemainingPoints);
			for (int i = 0; i < numberOfRemainingPoints; i += 1) {
				currentPoint = points.poll();
				int currentPointX = currentPoint.x;
				int currentPointY = currentPoint.y;
				if (currentPointX == endRow && currentPointY == endCol) {
					this.solved = true;
					shortestPath = count;
					flag = currentPoint;
				}

				for (int j = 0; j < NUM_DIRECTIONS; j += 1) {
					if (canGo(currentPointX, currentPointY, j)) {
						Point nextPoint = new Point(currentPointX + DELTAS[j][0], currentPointY
								+ DELTAS[j][1]);
						nextPoint.setParent(currentPoint);

						if (!this.visited[nextPoint.x][nextPoint.y]) {
							points.add(nextPoint);
							this.visited[nextPoint.x][nextPoint.y] = true;
						}
					}
				}
			}
			count += 1;
		}

		if (!this.solved) {
			return null;
		} else {
			while (flag.parent != null) {
				this.maze.getRoom(flag.x, flag.y).onPath = true;
				flag = flag.parent;
			}
			return shortestPath;
		}
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (k < 0) {
			return 0;
		} else if (k > this.reachable.size() - 1) {
			return 0;
		} else {
			return this.reachable.get(k);
		}
	}

	// we can only explode walls if they exist and you still have superpowers left
	private boolean canExplodeWalls(int row, int col, int dir, int superpowers) {
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows())
			return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns())
			return false;

		switch (dir) {
			case NORTH:
				return maze.getRoom(row, col).hasNorthWall() && superpowers > 0;
			case SOUTH:
				return maze.getRoom(row, col).hasSouthWall() && superpowers > 0;
			case EAST:
				return maze.getRoom(row, col).hasEastWall() && superpowers > 0;
			case WEST:
				return maze.getRoom(row, col).hasWestWall() && superpowers > 0;
		}
		return false;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol, int superpowers) 
	    throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		this.superpowerVisited = new boolean[this.maze.getRows()][this.maze.getColumns()][superpowers + 1];

		for (int i = 0; i < this.maze.getRows(); i += 1) {
			for (int j = 0; j < this.maze.getColumns(); j += 1) {
				this.visited[i][j] = false;
				this.maze.getRoom(i, j).onPath = false;
			}
		}

		this.solved = false;
		this.shortestPath = 0;
		this.reachable = new ArrayList<>();
		this.points = new LinkedList<>();

		this.superpowerVisited[startRow][startCol][superpowers] = true;
		this.visited[startRow][startCol] = true;
		this.maze.getRoom(startRow, startCol).onPath = true;
		Point startingPoint = new Point(startRow, startCol, superpowers);
		points.add(startingPoint);

		Point currentPoint;
		Point flag = null;
		int count = 0;

		this.reachable.add(1);
		while (!points.isEmpty()) {
			int numberOfRemainingPoints = points.size();
			this.reachable.add(0);
			for (int i = 0; i < numberOfRemainingPoints; i += 1) {
				currentPoint = points.poll();
				int currentX = currentPoint.x;
				int currentY = currentPoint.y;
				int currentSp = currentPoint.superpowers;

				if (currentX == endRow && currentY == endCol && !solved) {
					this.solved = true;
					shortestPath = count;
					flag = currentPoint;
				}

				for (int dir = 0; dir < NUM_DIRECTIONS; dir += 1) {
					if (canGo(currentX, currentY, dir)) {
						Point nextPoint = new Point(currentX + DELTAS[dir][0], currentY + DELTAS[dir][1], currentSp);
						nextPoint.setParent(currentPoint);

						if (!this.superpowerVisited[nextPoint.x][nextPoint.y][currentSp]) {
							points.add(nextPoint);
							this.superpowerVisited[nextPoint.x][nextPoint.y][currentSp] = true;
						}

						if (!visited[nextPoint.x][nextPoint.y]) {
							visited[nextPoint.x][nextPoint.y] = true;
							// updating the number of rooms reachable only if we have not visited the room before to prevent double counting
							this.reachable.set(count + 1, this.reachable.get(count + 1) + 1);
						}
					} else if (canExplodeWalls(currentX, currentY, dir, currentSp)) {
						Point nextPoint = new Point(currentX + DELTAS[dir][0], currentY + DELTAS[dir][1], currentSp - 1);
						// checking if there was a previous path passing by this point with this certain superpower level
						if (!this.superpowerVisited[nextPoint.x][nextPoint.y][currentSp - 1]) {
							nextPoint.setParent(currentPoint);
							points.add(nextPoint);
							this.superpowerVisited[nextPoint.x][nextPoint.y][currentSp - 1] = true;
						}

						if (!visited[nextPoint.x][nextPoint.y]) {
							visited[nextPoint.x][nextPoint.y] = true;
							this.reachable.set(count + 1, this.reachable.get(count + 1) + 1);
						}
					}
				}

			}
			count += 1;
		}

		if (!this.solved) {
			return null;
		} else {
			while (flag.parent != null) {
				this.maze.getRoom(flag.x, flag.y).onPath = true;
				flag = flag.parent;
			}
			return shortestPath;
		}
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 4, 1, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}