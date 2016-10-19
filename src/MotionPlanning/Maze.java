package MotionPlanning;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Maze {
	public int rangeX;
	public int rangeY;	// x coordinate in [-rangeX, rangeX], y coordinate in [-rangeY, rangeY]
	//public ArrayList<Shape> obstacles;
	public Area areaObstacles;
	
	public Maze(int rangeX, int rangeY){
		this.rangeX = rangeX;
		this.rangeY = rangeY;
		//obstacles = new ArrayList<Shape>();
	}
	
	private Shape transformCoordinates(Shape a){
    	AffineTransform at = new AffineTransform();
    	at.translate(250, 250);
    	at.scale(1, -1);	// last in, first out order!
    	return at.createTransformedShape(a);
    }
	
	public void generateObstacles(){
		//Shape square1 = new Rectangle2D.Float(-10, -10, 0, 0);
		Shape square1 = new Rectangle2D.Float(100, 100, 20, 20);
		Shape square2 = new Rectangle2D.Float(-125, 100, 20, 20);
		Shape square3 = new Rectangle2D.Float(-125, -155, 20, 20);
		Shape square4 = new Rectangle2D.Float(100, -155, 20, 20);
		Shape square5 = new Rectangle2D.Float(50, -50, 20, 20);
//		obstacles.add(square1);
//		obstacles.add(square2);
//		obstacles.add(square3);
//		obstacles.add(square4);
		areaObstacles = new Area(transformCoordinates(square1));
		areaObstacles.add(new Area(transformCoordinates(square2)));
		areaObstacles.add(new Area(transformCoordinates(square3)));
		areaObstacles.add(new Area(transformCoordinates(square4)));
		areaObstacles.add(new Area(transformCoordinates(square5)));
	}
	
}
