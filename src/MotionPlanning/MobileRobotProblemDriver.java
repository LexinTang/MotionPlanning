package MotionPlanning;

import java.awt.Dimension;

public class MobileRobotProblemDriver {
	 public static void main(String s[]) {
	    	// generate maze for testing 
	    	CarMaze maze = new CarMaze(250, 250); // map 500 * 500
	    	maze.generateMaze1();
	    	//maze.generateMaze2();
	    	
	    	double[] start_state = {-200, -200, 0}; //x, y, heading theta (in degree)
	    	double[] goal_state = {220, 220, 0};
	    	CarNode startNode = new CarNode(start_state, null);
	    	CarNode goalNode = new CarNode(goal_state, null);
	    	MobileRobot car = new MobileRobot(startNode);
	    	
	    	double goalPrecision = 50;
	    	RRTplanner rrt = new RRTplanner(startNode, goalNode, car, maze, goalPrecision);
			
	    	//draw
			MobileRobotFrame f = new MobileRobotFrame(maze, car, rrt);
			f.setSize(new Dimension(500, 500)); // map 500 * 500
	        f.setVisible(true);
	 }
}
