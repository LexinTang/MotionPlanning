package MotionPlanning;

import java.util.Arrays;

public class GraphNode implements Comparable<GraphNode>{

	public int[] state;	// 2R, 3R, 4R
	
	public GraphNode(int[] state){
		this.state  = new int[state.length];
		for (int i = 0; i < state.length; i++){
			this.state[i] = state[i];
		}
	}
	
	@Override
	public boolean equals(Object other){
		return Arrays.equals(this.state, ((GraphNode) other).state);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(state);
	}
	
	@Override
	public int compareTo(GraphNode other){
		return this.state[0] - other.state[0];
	}
	
	@Override
	public String toString(){
		String node_state ="(";
		for (int i = 0; i < state.length; i++){
			node_state += this.state[i];
			if (i < state.length - 1){
				node_state += ", ";
			}
		}
		node_state += ")";
		return node_state;
	}
}
