import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.Comparator;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};
	private static int NUM_DIRECTIONS = 4;

	private Maze maze;
	private int[][] fearCount;
	private boolean[][] visited;
	private PriorityQueue<Point> pq;

	public MazeSolver() {
		this.maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()];
		this.fearCount = new int[this.maze.getRows()][this.maze.getColumns()];
	}

	private class Point implements Comparator<Point> {
		private int x;
		private int y;
		private int fearLevel;

		public Point() {
		}

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
			this.fearLevel = 0;
		}

		public Point(int x, int y, int fearLevel) {
			this.x = x;
			this.y = y;
			this.fearLevel = fearLevel;
		}

		@Override
		public int compare(Point p1, Point p2) {
			return p1.fearLevel == p2.fearLevel ? 0 : p1.fearLevel > p2.fearLevel ? 1 : -1;
		}
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
				this.fearCount[i][j] = TRUE_WALL;
			}
		}

		Point startPoint = new Point(startRow, startCol);

		this.fearCount[startRow][startCol] = 0;
		int numOfPoints = this.maze.getRows() * this.maze.getColumns();
		// passing in the comparator to be used in the priority queue
		this.pq = new PriorityQueue<Point>(numOfPoints, new Point());

		this.pq.add(startPoint);
		while (!pq.isEmpty()) {
			Point current = this.pq.poll();
			if (!this.visited[current.x][current.y]) {
				this.visited[current.x][current.y] = true;
				relax(current.x, current.y);
			}
		}

		if (this.fearCount[endRow][endCol] == TRUE_WALL) {
			return null;
		} else {
			return this.fearCount[endRow][endCol];
		}
	}

	public void relax(int row, int col) {
		for (int dir = 0; dir < NUM_DIRECTIONS; dir += 1) {
			int nextX = row + DELTAS[dir][0];
			int nextY = col + DELTAS[dir][1];

			// we check if nextX and nextY are valid coordinates
			if (nextX >= 0 && nextX < this.maze.getRows()) {
				if (nextY >= 0 && nextY < this.maze.getColumns()) {
					Function<Room, Integer> f = WALL_FUNCTIONS.get(dir);
					Room currentRoom = this.maze.getRoom(row, col);
					int wallFear = f.apply(currentRoom);
					wallFear = wallFear == EMPTY_SPACE ? 1 : wallFear;

					if (!this.visited[nextX][nextY] && wallFear != TRUE_WALL) {
						int currentEstimate = this.fearCount[row][col];
						int nextEstimate = currentEstimate + wallFear;
						// decreasing the key
						this.fearCount[nextX][nextY] = Math.min(nextEstimate, this.fearCount[nextX][nextY]);
						Point nextPoint = new Point(nextX, nextY, this.fearCount[nextX][nextY]);
						pq.add(nextPoint);
					}
				}
			}
		}
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol)
			throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		return null;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 0, 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
