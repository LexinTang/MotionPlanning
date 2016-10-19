package MotionPlanning;

import java.util.Arrays;

public class CarNode {
	double[] state; // x, y, heading theta (in degree)
	CarNode parent; 
	
	public CarNode(double[] state, CarNode p){
		this.state  = new double[3];
		for (int i = 0; i < 3; i++){
			this.state[i] = state[i];
		}
		this.parent = p;
	}
	
	public double eucliDistance(CarNode other){
		double dist = 0;
		for (int i = 0; i < 2; i++){
			dist += (this.state[i] - (other).state[i]) * (this.state[i] - (other).state[i]);
		}
		dist = Math.sqrt(dist);
		return dist;
	}
	
	@Override
	public boolean equals(Object other){	// since we only do backtracking, no need to fetch a node from hashMap
		return false;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(state);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("(%.2f, %.2f, %.0f)", this.state[0], this.state[1], this.state[2]));
		return sb.toString();
	}
}
