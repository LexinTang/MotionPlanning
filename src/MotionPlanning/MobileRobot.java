package MotionPlanning;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class MobileRobot {
	
	double w = 15;
	double h = 10;
	public Area carArea;
	
	public MobileRobot(CarNode startNode){
		moveTo(startNode);
	}
	
	public void moveTo(CarNode node){
		
		Shape rect = new Rectangle2D.Double(node.state[0] - w / 2, node.state[1] - h / w, w, h);
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(node.state[2]), node.state[0], node.state[1]);	// rotate is clockwise
		Shape carShape = at.createTransformedShape(rect);
		carArea = new Area(transformCoordinates(carShape));
	}
	
    private Shape transformCoordinates(Shape a){
    	AffineTransform at = new AffineTransform();
    	at.translate(250, 250);
    	at.scale(1, -1);	// last in, first out order!
    	return at.createTransformedShape(a);
    }
}
