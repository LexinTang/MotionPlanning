package MotionPlanning;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class PRMplanner {
	
	public HashMap<GraphNode, ArrayList<GraphNode>> graph;
	private ArmRobot armRobot;
	private Maze maze;
	private int dim;
	private int k;
	private Random rand;
	
	public PRMplanner(ArmRobot armRobot, Maze maze, int k, int thresh, int totalSamples){ //k-Nearest Neighbors
		this.armRobot = armRobot;
		this.maze = maze;
		this.dim = armRobot.dimension;
		this.k = k;
		graph = new HashMap<GraphNode, ArrayList<GraphNode>>();
		rand = new Random(25);	// set random seed
		
		while (graph.keySet().size() < thresh){
			//System.out.println("generate origin samples");
			GraphNode newNode = new GraphNode(randomGenerateState());
			if (isSafeState(newNode) && !graph.containsKey(newNode)){
				ArrayList<GraphNode> neighbors = new ArrayList<GraphNode>();
				graph.put(newNode, neighbors);
			}
		}
		
		while (graph.keySet().size() < totalSamples){
			GraphNode newNode = new GraphNode(randomGenerateState());
			System.out.println("generate new sample: " + newNode);
			if (isSafeState(newNode) && !graph.containsKey(newNode)){
				// find k-NN and connect them with the new generated GraphNode, and add the newNode to graph
				localPlanner(newNode);
			}
		}
	}
	
	private int[] randomGenerateState(){
		int[] state = new int[dim];
		
		for (int i = 0; i < dim; i++){
			state[i] = rand.nextInt(360) - 180;	// randomly pick an angle from [-180, 180]
		}
		return state;
	}
	
	private boolean isSafeState(GraphNode node){
		
		armRobot.moveTo(node);
		if (testCollision(armRobot.linkArea, maze.areaObstacles)){
			return false;
		}
		
		return true;
	}
	
	// return true: collision exists
	// return false: no collision
	// refer to http://stackoverflow.com/questions/15690846/java-collision-detection-between-two-shape-objects
	private boolean testCollision(Area areaA, Area areaB) {
		areaA.intersect(areaB);
		return !areaA.isEmpty();
	}
	
	// LocalPlanner is responsible for checking collisions and connect two GraphNodes if they are closed to each other
	private void localPlanner(GraphNode newNode){
		ArrayList<GraphNode> nodeLists = new ArrayList<GraphNode>();
		for (GraphNode key : graph.keySet()){
			nodeLists.add(key);
		}
		
		// sort the nodes in graph based on their distance towards newNode
		Collections.sort(nodeLists, new Comparator<GraphNode>(){
			public int compare(GraphNode a, GraphNode b){
				int dist_a = 0;
				int dist_b = 0;
				for (int i = 0; i < dim; i++){
//					dist_a += Math.abs(a.state[i] - newNode.state[i]);	// use Manhattan distance
//					dist_b += Math.abs(b.state[i] - newNode.state[i]);
					dist_a += (a.state[i] - newNode.state[i]) * (a.state[i] - newNode.state[i]);	// use Euclidean distance
					dist_b += (b.state[i] - newNode.state[i]) * (b.state[i] - newNode.state[i]);
				}
				return dist_a - dist_b;
			}
		});
		
		// find K-NN
		ArrayList<GraphNode> neighbors = new ArrayList<GraphNode>();
		for (int i = 0; i < k; i++){
			// check along the path to ensure no collision between two nodes
			if (isSafeAlongPath(newNode, nodeLists.get(i), 50)){
				System.out.println("connect two nodes: " + nodeLists.get(i) + " and " + newNode);
				connectTwoNodes(newNode, nodeLists.get(i));
				neighbors.add(nodeLists.get(i));
			}
		}
		
		graph.put(newNode, neighbors);
	}
	
	private void connectTwoNodes(GraphNode newNode, GraphNode a){
		ArrayList<GraphNode> neighbors = graph.get(a);
		neighbors.add(newNode);
	}
	
	// check collisions along the path between start and end
	// sample at resolution = 1
	private boolean isSafeAlongPath(GraphNode a, GraphNode b, int sampleRate){
		GraphNode start, end;
		if (a.compareTo(b) < 0){
			start = new GraphNode(a.state);
			end = new GraphNode(b.state);
		}
		else{
			start = new GraphNode(b.state);
			end = new GraphNode(a.state);
		}
		
		double[] gap = new double[dim];
		for (int j = 0; j < dim; j++){
			gap[j] = (double)(end.state[j] - start.state[j]);
		}
		
		for (int i = 0; i < sampleRate; i++){
			GraphNode tempNode = new GraphNode(start.state);
			for (int j = 0; j < dim; j++){
				tempNode.state[j] +=  (int)(gap[j] * i / (sampleRate - 1));
			}
			
			if (!isSafeState(tempNode)){
				return false;
			}
		}
		return true;
	}
	
	// this function is used to add start node and goal node to the graph.
	public void addGraphNode(GraphNode node){
		localPlanner(node);
	}
}
