package MotionPlanning;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class CarMaze {
	public int rangeX;
	public int rangeY;	// x coordinate in [-rangeX, rangeX], y coordinate in [-rangeY, rangeY]
	public Area areaObstacles;
	
	public CarMaze(int rangeX, int rangeY){
		this.rangeX = rangeX;
		this.rangeY = rangeY;
		areaObstacles = new Area();
	}
	
	public void generateMaze1(){
		
		for (int x = - rangeX + 10; x < rangeX - 10; x += 10){
			Shape square1 = new Rectangle2D.Float(x, - rangeY + 30, 10, 10);
			Shape square2 = new Rectangle2D.Float(x, rangeY - 10, 10, 10);
			areaObstacles.add(new Area(transformCoordinates(square1)));
			areaObstacles.add(new Area(transformCoordinates(square2)));
		}
		
		for (int y = - rangeY + 30; y < rangeY - 10; y += 10){
			Shape square1 = new Rectangle2D.Float( - rangeX + 10, y, 10, 10);
			Shape square2 = new Rectangle2D.Float(rangeX - 20, y, 10, 10);
			areaObstacles.add(new Area(transformCoordinates(square1)));
			areaObstacles.add(new Area(transformCoordinates(square2)));
		}
		
		for (int x = - rangeX + 100; x < rangeX - 100; x += 10){
			Shape square1 = new Rectangle2D.Float(x, 100, 10, 10);
			Shape square2 = new Rectangle2D.Float(x, -100, 10, 10);
			areaObstacles.add(new Area(transformCoordinates(square1)));
			areaObstacles.add(new Area(transformCoordinates(square2)));
		}
		
	}
	
	public void generateMaze2(){
		for (int x = - rangeX + 10; x < rangeX - 10; x += 10){
			Shape square1 = new Rectangle2D.Float(x, - rangeY + 30, 10, 10);
			Shape square2 = new Rectangle2D.Float(x, rangeY - 10, 10, 10);
			areaObstacles.add(new Area(transformCoordinates(square1)));
			areaObstacles.add(new Area(transformCoordinates(square2)));
		}
		
		for (int y = - rangeY + 30; y < rangeY - 10; y += 10){
			Shape square1 = new Rectangle2D.Float( - rangeX + 10, y, 10, 10);
			Shape square2 = new Rectangle2D.Float(rangeX - 20, y, 10, 10);
			areaObstacles.add(new Area(transformCoordinates(square1)));
			areaObstacles.add(new Area(transformCoordinates(square2)));
		}
		
		for (int x = - rangeX + 10; x < rangeX - 100; x += 10){
			Shape square1 = new Rectangle2D.Float(x, -150, 10, 10);
			areaObstacles.add(new Area(transformCoordinates(square1)));
		}
		
		for (int x = - rangeX + 100; x < rangeX - 10; x += 10){
			Shape square1 = new Rectangle2D.Float(x, -80, 10, 10);
			areaObstacles.add(new Area(transformCoordinates(square1)));
		}
		
		for (int y = 0; y < rangeY - 10; y += 10){
			Shape square1 = new Rectangle2D.Float(-100, y, 10, 10);
			Shape square2 = new Rectangle2D.Float(100, y, 10, 10);
			areaObstacles.add(new Area(transformCoordinates(square1)));
			areaObstacles.add(new Area(transformCoordinates(square2)));
		}
	}
	
	private Shape transformCoordinates(Shape a){
    	AffineTransform at = new AffineTransform();
    	at.translate(250, 250);
    	at.scale(1, -1);	// last in, first out order!
    	return at.createTransformedShape(a);
    }
	
}
