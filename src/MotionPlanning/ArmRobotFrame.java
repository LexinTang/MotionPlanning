package MotionPlanning;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ArmRobotFrame extends JFrame{

	private ArmRobot arm;
	private Maze maze;
	List<UUSearchProblem.UUSearchNode> path;
    public ArmRobotFrame(ArmRobot arm, Maze maze, List<UUSearchProblem.UUSearchNode> path){
         super("Arm Robot Motion Planning");
         
         this.arm = arm;
         this.maze = maze;
         this.path = path;
         
         //Initialize drawing colors
         setBackground(Color.WHITE);
         setForeground(Color.BLACK);
         
         //you can set the content pane of the frame 
         //to your custom class.

         setContentPane(new DrawPane());

         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

     //create a component that you can actually draw on.
     class DrawPane extends JPanel{
    	 
    	 public void paintComponent(Graphics g){
    		 Graphics2D g2 = (Graphics2D) g;
			
    		 g2.fill(maze.areaObstacles);
    		 g2.setColor(new Color(255, 0, 0));
    		 for (int i = 0; i < path.size(); i++){
    			 GraphNode node = new GraphNode(path.get(i).getState());
    			 arm.moveTo(node);
    			 g2.draw(arm.linkArea);
    		 }
 
    	 }
    }
 }