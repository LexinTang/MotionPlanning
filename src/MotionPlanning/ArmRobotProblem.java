package MotionPlanning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import MotionPlanning.UUSearchProblem;

public class ArmRobotProblem extends UUSearchProblem{
	private int[] goal;
	private int dimension;
	HashMap<GraphNode, ArrayList<GraphNode>> graph;
	
	public ArmRobotProblem(GraphNode start, GraphNode goal, PRMplanner roadMap){
		dimension = goal.state.length;
		this.goal = new int[dimension];
		for (int i = 0; i < dimension; i++){
			this.goal[i] = goal.state[i];
 		}
		roadMap.addGraphNode(start);
		roadMap.addGraphNode(goal);
		this.graph = roadMap.graph;
		startNode = new ArmRobotNode(start, 0, null);
	}
	
	class ArmRobotNode implements UUSearchNode, Comparable<ArmRobotNode>{
		private int[] state;
		private int depth;
		private int heuristic;
		private int cost;
		private ArmRobotNode parent;
		
		public ArmRobotNode(GraphNode currentNode, int d, ArmRobotNode p){
			state = new int[dimension];
			heuristic = 0;
			for (int i = 0; i < dimension; i++){
				this.state[i] = currentNode.state[i];
				//heuristic += Math.abs(goal[i] - currentNode.state[i]);
				heuristic += (goal[i] - currentNode.state[i]) * (goal[i] - currentNode.state[i]);
			}
			heuristic = (int)(Math.sqrt(heuristic));
			
			depth = d;
			parent = p;
			cost = depth + heuristic;
		}
		
		public ArrayList<UUSearchNode> getSuccessors() {
			ArrayList<UUSearchNode> reachableStates = new ArrayList<UUSearchNode>();
			GraphNode currentNode = new GraphNode(state);
			
			ArrayList<GraphNode> neighbors = graph.get(currentNode);
			for (int i = 0; i < neighbors.size(); i++){
				GraphNode next = neighbors.get(i);
				//System.out.println("neighbor next: " + next);
				reachableStates.add(new ArmRobotNode(next, depth + 1, this));
			}
			
			return reachableStates;
		}
		
		@Override
		public boolean goalTest() {
			return Arrays.equals(this.state, goal);
		}
		
		@Override
		public boolean equals(Object other) {
			return Arrays.equals(state, ((ArmRobotNode) other).state);
		}

		@Override 
		public int compareTo(ArmRobotNode other){
			return this.cost - other.getCost();
		}
		
		@Override
		public int hashCode() {
			return Arrays.hashCode(state);
		}
		
		@Override
		public String toString() {
			String node_state ="(";
			for (int i = 0; i < dimension; i++){	
				node_state += this.state[i];
				if (i < dimension - 1){
					node_state += ", ";
				}
			}
			node_state += ")";
			return node_state;
		}
		
		@Override
		public ArmRobotNode getParent() {
			return parent;
		}
		
		@Override
		public int getDepth() {
			return depth;
		}
		
		@Override
		public int getCost() {
			return cost;
		}
		
		@Override
		public int[] getState() {
			return state;
		}
		
	}
}
