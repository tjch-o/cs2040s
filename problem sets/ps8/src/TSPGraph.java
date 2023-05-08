import java.util.ArrayList;
import java.util.HashSet;

public class TSPGraph implements IApproximateTSP {
    @Override
    public void MST(TSPMap map) {
        int numOfPoints = map.getCount();
        TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();
        HashSet<Integer> pointSet = new HashSet<>();

        // adding start point into queue
        pq.add(0, 0.0);
        for (int i = 1; i < numOfPoints; i += 1) {
            pq.add(i, Double.POSITIVE_INFINITY);
        }

        while (!pq.isEmpty()) {
            int currentPoint = pq.extractMin();
            pointSet.add(currentPoint);
            for (int otherPoint = 0; otherPoint < numOfPoints; otherPoint += 1) {
                // order of currentPoint and otherPoint as parameters doesnt matter since they just measuring distance
                double weightOfEdge = map.pointDistance(currentPoint, otherPoint);
                if (!(currentPoint == otherPoint || pointSet.contains(otherPoint))) {
                    if (weightOfEdge < pq.lookup(otherPoint)) {
                        pq.decreasePriority(otherPoint, weightOfEdge);
                        // setting the parent 
                        map.setLink(otherPoint, currentPoint, false);
                    }
                }
            }
        }
        map.redraw();
    }

    public static void DFS(TSPMap map, int index, ArrayList<Integer> arr) {
        for (int point = 0; point < map.getCount(); point += 1) {
            // getLink is directed even though the graph is undirected
            // the dfs does not need a visited array because of the MST property 
            if (map.getLink(point) == index) {
                arr.add(point);
                DFS(map, point, arr);
            }
        }
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        // basically while we already have a MST which has all the points and no cycle we might have visited nodes more than once
        int numOfPoints = map.getCount();
        ArrayList<Integer> arr = new ArrayList<>();

        arr.add(0);
        for (int i = 0; i < numOfPoints - 1; i += 1) {
            if (map.getLink(i) == 0) {
                arr.add(i);
                TSPGraph.DFS(map, i, arr);
            }
        }

        // now we start reconnecting by traversing the arraylist
        for (int j = 0; j < numOfPoints - 1; j += 1) {
            int from = arr.get(j);
            int to = arr.get(j + 1);
            map.setLink(from, to, false);
        }

        // it is a tour so its a cycle we need to reconnect 
        map.setLink(arr.get(arr.size() - 1), 0, false);
        map.redraw();
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        if (map == null) {
            return false;
        }

        int numOfPoints = map.getCount();
        boolean[] visited = new boolean[numOfPoints];
        int currentPoint = 0;
            
        for (int i = 0; i < numOfPoints; i += 1) {
            currentPoint = map.getPoint(currentPoint).getLink();
            if (currentPoint == -1 || visited[currentPoint] == true) {
                return false;
            }
            visited[currentPoint] = true;
        }
        return true;
    }

    @Override
    public double tourDistance(TSPMap map) {
        if (!isValidTour(map)) {
            return -1;
        }

        double tourDistance = 0;
        int previousVertex = 0;
        int numOfPoints = map.getCount();
        for (int i = 0; i < numOfPoints; i += 1) {
            int nextVertex = map.getPoint(previousVertex).getLink();
            tourDistance += map.pointDistance(previousVertex, nextVertex);
            previousVertex = nextVertex;
        }
        return tourDistance;
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "../hundredpoints.txt");
        TSPGraph graph = new TSPGraph();

        graph.MST(map);
        // graph.TSP(map);
        // System.out.println(graph.isValidTour(map));
        // System.out.println(graph.tourDistance(map));
    }
}
