import java.util.ArrayList;

public class BellmanFord {
    public static int INF = 20000000;
    public static int NEGINF = -20000000;

    private ArrayList<ArrayList<IntPair>> adjList;
    private int[] distances;
    private int numOfNodes;

    public BellmanFord(ArrayList<ArrayList<IntPair>> adjList) {
        this.adjList = adjList;
        this.numOfNodes = adjList.size();
        this.distances = new int[this.numOfNodes];
    }

    public void computeShortestPaths(int source) {
        this.distances[source] = 0;
        for (int i = 0; i < this.numOfNodes; i += 1) {
            if (i != source) {
                this.distances[i] = INF;
            }
        }

        // we need to relax everything more than once
        for (int i = 0; i < this.numOfNodes - 1; i += 1) {
            for (int src = 0; src < this.numOfNodes; src += 1) {
                // then we iterate every edge 
                ArrayList<IntPair> currentAdjList = this.adjList.get(src);
                for (IntPair edge : currentAdjList) {
                    if (this.distances[src] != INF) {
                        int dest = edge.first;
                        int weight = edge.second;
                        if (weight != NEGINF) {
                            this.distances[dest] = Math.min(this.distances[dest], 
                            this.distances[src] + weight);
                        }
                    }
                }
            }
        }

        // now we need to check the graph for negative cycles
        for (int vertex = 0; vertex < this.numOfNodes; vertex += 1) {
            ArrayList<IntPair> currentAdjList = this.adjList.get(vertex);
            for (IntPair pair : currentAdjList) {
                if (this.distances[pair.first] != NEGINF && this.distances[pair.first] > 
                this.distances[vertex] + pair.second) {
                    // we basically do everything again to check if there is a even shorter path
                    // if each time we relax we get an even lower value there is a negative weight cycle
                    for (int i = 0; i < this.numOfNodes - 1; i += 1) {
                        for (int src = 0; src < this.numOfNodes; src += 1) {
                            ArrayList<IntPair> currAdjList = this.adjList.get(src);
                            for (IntPair edge : currAdjList) {
                                if (this.distances[src] != INF) {
                                    int dest = edge.first;
                                    int weight = edge.second;
                                    // remember that here the distance at each vertex should no longer be INF
                                    if (this.distances[src] != INF && weight != NEGINF) {
                                        if (this.distances[dest] > this.distances[src] + weight) {
                                            this.distances[dest] = NEGINF;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }   
            }
        }
    }

    public int getDistance(int node) { 
        return node >= 0 && node < this.numOfNodes ? this.distances[node] : INF;
    }
}