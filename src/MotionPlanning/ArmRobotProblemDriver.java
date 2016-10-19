package MotionPlanning;
import java.awt.Dimension;
import java.util.List;

public class ArmRobotProblemDriver {
	
	    public static void main(String s[]) {
	    	// generate maze for testing 
	    	Maze maze = new Maze(250, 250); // map 500 * 500
	    	maze.generateObstacles();
	    	
	    	// initiate arm robot 
	    	Position base = new Position(0, 0);	// locate at the center
	    	// 2R
//	    	int[] linkLength = {60, 60};
//	    	int[] start_state = {-90, -90};	// in degree
//	    	int[] goal_state = {0, 90};	// in degree
	    	
	    	
	    	// 3R
//	    	int[] linkLength = {50, 50, 50};	
//	    	int[] start_state = {-90, 0, -90};	// in degree
//	    	int[] goal_state = {0, 0, 90};	// in degree
	    	
	    	
	    	 //4R
	    	int[] linkLength = {60, 60, 60, 60};
	    	int[] start_state = {-90, 0, -90, 0};	// in degree
	    	int[] goal_state = {0, 0, 90, 0};	// in degree
	    	
	    	
	    	GraphNode startNode = new GraphNode(start_state);
	    	GraphNode goalNode = new GraphNode(goal_state);
	    	
	    	ArmRobot arm = new ArmRobot(base, linkLength, startNode);
	    	
	    	// PRM planner
	    	PRMplanner roadMap = new PRMplanner(arm, maze, 10, 100, 1000);// k, threshold, total samples
	    	
	    	ArmRobotProblem armRobotProb = new ArmRobotProblem(startNode, goalNode, roadMap);
	    	List<UUSearchProblem.UUSearchNode> path;
			
			path = armRobotProb.AStarSearch();
			System.out.println("A* search path length:  " + path.size() + " " + path);
			armRobotProb.printStats();
			System.out.println("");
	    	
			//draw
			ArmRobotFrame f = new ArmRobotFrame(arm, maze, path);
			f.setSize(new Dimension(500, 500)); // map 500 * 500
	        f.setVisible(true);
	        
			/*
	        JFrame f = new JFrame("ShapesDemo2D");
	        f.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {System.exit(0);}
	        });
	        JApplet applet = new DrawDriver();
	        f.getContentPane().add("Center", applet);
	        applet.init(arm, maze);
	        f.pack();
	        
			*/
	    }

}
