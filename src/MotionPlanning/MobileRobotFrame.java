package MotionPlanning;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MobileRobotFrame extends JFrame {
	private CarMaze maze;
	MobileRobot car;
	List<CarNode> tree;
	List<CarNode> path;
	
	public MobileRobotFrame(CarMaze maze, MobileRobot car, RRTplanner rrt){ 
        super("Mobile Robot Motion Planning");

        this.maze = maze;
        this.car = car;
        this.tree = rrt.graph;
        this.path = rrt.path;
        System.out.println("path length:  " + path.size() + " " + path);
        
        //Initialize drawing colors
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        
        //you can set the content pane of the frame 
        //to your custom class.

        setContentPane(new DrawPane());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

	public MobileRobotFrame(){ 
        super("Mobile Robot Motion Planning");

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

			// draw RRT 
			for (CarNode node : tree){
				if (node.parent == null){
					continue;
				}
				Shape edge = new Line2D.Double(node.parent.state[0], node.parent.state[1], node.state[0], node.state[1]);
				g2.draw(transformCoordinates(edge));
			}
			
			g2.setColor(Color.green);
			// draw car trajectory
			for (int i = 0; i < path.size(); i++){
				CarNode node = path.get(i);
				car.moveTo(node);
				g2.draw(car.carArea);
			}
			
		}
		
	    private Shape transformCoordinates(Shape a){
	    	AffineTransform at = new AffineTransform();
	    	at.translate(250, 250);
	    	at.scale(1, -1);	// last in, first out order!
	    	return at.createTransformedShape(a);
	    }
   }
}
