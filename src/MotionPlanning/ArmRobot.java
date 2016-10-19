package MotionPlanning;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ArmRobot {
	public int dimension;	// 2R, 3R, 4R
	public int[] linkLength;
	public int linkRadius = 5;
	public Position base;
	public Position[] endpoints;
	public Area linkArea;
	
	public ArmRobot(Position base, int[] linkLength, GraphNode start){
		this.base = new Position(base.x, base.y);
		this.dimension = linkLength.length;
		this.linkLength = new int[dimension];
		for (int i = 0; i < dimension; i++){
			this.linkLength[i] = linkLength[i];
		}
		
		this.endpoints = new Position[dimension];
		moveTo(start);
	}
	
	public void moveTo(GraphNode node){
		int x = base.x;
		int y = base.y;
		int theta = 0;
		for (int i = 0; i < dimension; i++){
			theta += node.state[i];
			x += (int)(linkLength[i] * Math.cos(Math.toRadians(theta)));
			y += (int)(linkLength[i] * Math.sin(Math.toRadians(theta)));
			this.endpoints[i] = new Position(x, y);
		}
		
		
		Shape[] links = new Shape[dimension];
		Shape rect = new Rectangle2D.Float(base.x, base.y - linkRadius, linkLength[0], linkRadius * 2);
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(node.state[0]), base.x, base.y + linkRadius);	// rotate is clockwise
		links[0] = at.createTransformedShape(rect);
		theta = node.state[0];
		linkArea = new Area(transformCoordinates(links[0]));
		for (int i = 1; i < dimension; i++){
			theta += node.state[i];
			//System.out.println("link " + i + " theta = " + theta);
			//System.out.println("endpoint " + i + ": " + endpoints[i - 1].x + " " + endpoints[i - 1].y);
			rect = new Rectangle2D.Float(endpoints[i - 1].x, endpoints[i - 1].y - linkRadius, linkLength[i], linkRadius * 2);	// x1, y1, x2, y2
			AffineTransform newAt = new AffineTransform();
			newAt.rotate(Math.toRadians(theta), endpoints[i - 1].x, endpoints[i - 1].y + linkRadius);
			links[i] = newAt.createTransformedShape(rect);
			linkArea.add(new Area(transformCoordinates(links[i])));
		}
	}
	
    private Shape transformCoordinates(Shape a){
    	AffineTransform at = new AffineTransform();
    	at.translate(250, 250);
    	at.scale(1, -1);	// last in, first out order!
    	return at.createTransformedShape(a);
    }
}
