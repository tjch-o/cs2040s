import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};

	private Maze maze;
	private boolean solved;
	private boolean[][] visited;
	private Queue<Point> points;
	private int shortestPath;
	private ArrayList<Integer> reachable;

	public MazeSolver() {
		this.maze = null;
		this.points = new LinkedList<>();
		this.reachable = new ArrayList<>();
	}

	private class Point {
		private int x;
		private int y;
		private Point parent;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
			this.parent = null;
		}

		public void setParent(Point p) {
			this.parent = p;
		}
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()];
		this.solved = false;
	}

	// we reused code from MazeNaiveSolver to check if we can go in a certain direction
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

		// initialise this.visited
		for (int i = 0; i < this.maze.getRows(); i += 1) {
			for (int j = 0; j < this.maze.getColumns(); j += 1) {
				this.visited[i][j] = false;
				this.maze.getRoom(i, j).onPath = false;
			}
		}

		// we have to reset everything everytime we call pathSearch
		this.solved = false;
		this.shortestPath = 0;
		this.reachable = new ArrayList<>();
		this.points = new LinkedList<>();

		// we begin the search starting from starting point
		this.visited[startRow][startCol] = true;
		this.maze.getRoom(startRow, startCol).onPath = true;
		Point startingPoint = new Point(startRow, startCol);
		points.add(startingPoint);

		Point currentPoint;
		Point flag = null;
		// count is only for the specific path
		int count = 0;

		while (!points.isEmpty()) {
			int numberOfRemainingPoints = points.size();
			// we add to the reachable arraylist because at this index this is the number of
			// moves we can make
			this.reachable.add(numberOfRemainingPoints);
			for (int i = 0; i < numberOfRemainingPoints; i += 1) {
				currentPoint = points.poll();
				int currentPointX = currentPoint.x;
				int currentPointY = currentPoint.y;
				if (currentPointX == endRow && currentPointY == endCol) {
					this.solved = true;
					shortestPath = count;
					// we will later use this flag to retrace our steps on the path and mark
					// everyone on it to be true
					flag = currentPoint;
				}

				// so we dont know what direction the current point can go and even if it can go
				// anywhere
				for (int j = 0; j < 4; j += 1) {
					if (canGo(currentPointX, currentPointY, j)) {
						// remember that j is a direction so we have to use the j value and take x y
						// values from the DELTA
						Point nextPoint = new Point(currentPointX + DELTAS[j][0], currentPointY
								+ DELTAS[j][1]);
						nextPoint.setParent(currentPoint);

						// we cannot just add the nextPoint to queue straight away
						// we need to check if it has already been visited; no point visiting a visited
						// node again
						if (!this.visited[nextPoint.x][nextPoint.y]) {
							// each time we add into the queue it is a possible move / room reachable
							points.add(nextPoint);
							this.visited[nextPoint.x][nextPoint.y] = true;
						}
					}
				}
			}
			count += 1;
		}

		// if we reach this step we already know if we have a shortestPath
		if (!this.solved) {
			return null;
		} else {
			// we just have to mark all the points on the path to be true and everything
			// else is default false
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
			// queue size starts from 1 but k starts from 0 so we check
			// this.reachable.size() - 1
			return 0;
		} else {
			return this.reachable.get(k);
		}
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 3, 3));

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
