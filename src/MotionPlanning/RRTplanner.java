package MotionPlanning;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RRTplanner {

	ArrayList<CarNode> graph;
	private Random rand;
	MobileRobot car;
	CarMaze maze;
	List<CarNode> path;
	Area rrtArea;
	double goalPrecision;
	
	public RRTplanner(CarNode startNode, CarNode goalNode, MobileRobot car, CarMaze maze, double goalPrecision){
		this.car = car;
		this.maze = maze;
		this.goalPrecision = goalPrecision;
		rand = new Random(125);	// set random seed
		
		graph = new ArrayList<CarNode>();
		
		// add startNode to the graph and expand the tree from the start point
		graph.add(startNode);
		
		rrtArea = new Area();
		path = new ArrayList<CarNode>(findPath(startNode, goalNode));
	}
	
	// randomly sample the map, check if a new position is collision-free, then find its nearest CarNode 
	// in the graph, generate 6 successors of the nearest node, find the closest one towards the new position
	// connect the nearest node and the selected successor, then check whether the successor reaches goal
	public List<CarNode> findPath(CarNode startNode, CarNode goalNode){
		CarNode successor = startNode;
		int iter = 0;
		while (!goalTest(successor, goalNode)){
//			// generate random samples but include goalNode inside
//			Position pos;
//			if (iter % 10 == 0){
//				iter++;
//				pos = new Position((int)(goalNode.state[0]), (int)(goalNode.state[1]));
//			}
//			else{
//				pos = randomGeneratePosition();
//			}
			
			// generate random samples 
			Position pos = randomGeneratePosition();
			
			//System.out.println("random generate pos: " + pos);
			double[] state = {(double)pos.x, (double)pos.y, 0};
			CarNode newNode = new CarNode(state, null);
			
			// check if a new position is collision-free
			if (isSafeState(newNode)){
				
				// find the nearest node in graph
				CarNode nearest = findNearestInGraph(newNode);
				
				successor = expandRRT(nearest, newNode);
				if (successor != null){
					graph.add(successor);
					System.out.println("add " + successor + " with parent = " + successor.parent + " to graph!");
				}
			}
		}

		return backTrace(successor);
	}
    
	private CarNode findNearestInGraph(CarNode newNode){
		double dist = Double.MAX_VALUE;
		CarNode nearest = null;
		for (CarNode node : graph){
			double computDist = node.eucliDistance(newNode);
			if (computDist < dist){
				dist = computDist;
				nearest = node;
			}
		}
		
		return nearest;
	}
	
	// generate 6 successors of the parent node and choose the one with minimal distance towards newNode
	private CarNode expandRRT(CarNode parent, CarNode newNode){	
		//System.out.println("expanding RRT from " + parent);
		double dist = Double.MAX_VALUE;
		CarNode nearest = null;
		// forwards, backwards, forwards turning counter-clockwise, backwards turning counter-clockwise, forwards turning clockwise, backwards turning clockwise 
		double[] v = {1, -1, 1, -1, 1, -1};	
		double[] w = {0, 0, 1, 1, -1, -1};
		double time = 5;
		
		double[] state = new double[3];
		CarNode node = null;
		
		// generate 6 candidates based on v & w control
		for (int i = 0; i < 6; i++){
			double theta = w[i] * time;
			double newX = parent.state[0] + time * v[i] * Math.cos(Math.toRadians(parent.state[2]));
			double newY = parent.state[1] + time * v[i] * Math.sin(Math.toRadians(parent.state[2]));
			double centerX = parent.state[0] - (v[i] / w[i]) * Math.sin(Math.toRadians(theta));
			double centerY = parent.state[1] + (v[i] / w[i]) * Math.cos(Math.toRadians(theta));
			
			if (theta == 0){	// forwards, backwards
				state[0] = newX;
				state[1] = newY;
				state[2] = parent.state[2];
			}
			else {	// turning
				double vector_origin_x = parent.state[0] - centerX;
				double vector_origin_y = parent.state[1] - centerY;
				double vector_new_x = Math.cos(Math.toRadians(theta)) * vector_origin_x - Math.sin(Math.toRadians(theta)) * vector_origin_y;
				double vector_new_y = Math.sin(Math.toRadians(theta)) * vector_origin_x + Math.cos(Math.toRadians(theta)) * vector_origin_y;
				state[0] = centerX + vector_new_x;
				state[1] = centerY + vector_new_y;
				state[2] = (parent.state[2] + theta) % 360;
			}
			
			node = new CarNode(state, parent);
			
			if (!isSafeState(node)){
				continue;
			}
			double computDist = node.eucliDistance(newNode);
			if (computDist < dist){
				dist = computDist;
				nearest = node;
			}
		}
		
		return nearest;
	}
	
	private boolean goalTest(CarNode node, CarNode goal) {
		if (node == null){
			return false;
		}
		double dist = node.eucliDistance(goal);
	
		System.out.println("distance = " + dist);
		if (dist < goalPrecision){
			return true;
		}
		return false;
	}
	
	private boolean isSafeState(CarNode node){
		car.moveTo(node);
		if (testCollision(car.carArea, maze.areaObstacles)){
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
	
	private Position randomGeneratePosition(){
		Position pos = new Position(0, 0);
		pos.x = rand.nextInt(500) - 250;	// randomly pick an position in a 500 * 500 map
		pos.y = rand.nextInt(500) - 250;
		return pos;
	}
	
	private List<CarNode> backTrace(CarNode node) {
		
		System.out.println("backtracking...");
		List<CarNode> result = new ArrayList<CarNode>(); 
		CarNode prevNode = node.parent;
		result.add(node);
		while (prevNode != null){
			node = prevNode;
			result.add(node);
			prevNode = node.parent;
		}
		
		Collections.reverse(result);
		return result;
	}
}
