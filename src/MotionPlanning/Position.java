package MotionPlanning;

public class Position{
	int x;
	int y;
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString(){
		String node_state ="(" + this.x + ", " + this.y + ")";
		return node_state;
	}
}
