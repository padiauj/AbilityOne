import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;


public class Marker {
	
	private ArrayList<Point> pts;
	private Color color;
	private Point[] myCorners; //(point[0] = top left, 1 = top right, 3=bottom right, 3=bottom left
	private int vertices;
	private Point myCentroid;
	private int sizeInPixels;
	private Point[] myImgCorners;
	
	public static Color GREEN = new Color(18,255,1);
	public static Color RED = Color.RED;
	public static Color BLUE = Color.BLUE;
	
	
	public Marker (ArrayList<Point> pts, int imgHeight, int imgWidth, Color c) {
		this.pts = pts;
		this.color = c;
		
		sizeInPixels = pts.size();
		myImgCorners = new Point[4];
		myImgCorners[0] = new Point(0, 0);
		myImgCorners[1] = new Point(imgWidth, 0);
		myImgCorners[2] = new Point(imgWidth, imgHeight);
		myImgCorners[3] = new Point(0, imgHeight);
		myCorners = calculateCorners(pts);
		vertices = myCorners.length;
		calcCentroid();
	}
	
	private Point[] calculateCorners(ArrayList<Point> points) {

		Point[] corners = { points.get(0), points.get(0), points.get(0),
				points.get(0) };
		for (Point pt : points) {
			if (pt.distance(myImgCorners[0]) < corners[0]
					.distance(myImgCorners[0]))
				corners[0] = pt;
			if (pt.distance(myImgCorners[1]) < corners[1]
					.distance(myImgCorners[1]))
				corners[1] = pt;
			if (pt.distance(myImgCorners[2]) < corners[2]
					.distance(myImgCorners[2]))
				corners[2] = pt;
			if (pt.distance(myImgCorners[3]) < corners[3]
					.distance(myImgCorners[3]))
				corners[3] = pt;
		}
		return corners;
	}
	//Returns the number of pixels consumed by the target.
	public int getSize() {
		return sizeInPixels;
	}
	//Calculates the area of the polygon created by the corners.
	public double getPolygonalArea() {
		int area = 0;
		int j=vertices-1;
		for (int i=0; i<vertices; i++){
			area = area +  (myCorners[j].x+myCorners[i].x) * (myCorners[j].y-myCorners[i].y); 
			j = i;
		}
		return area/(double) 2;
	}
	//Calculates the centroid by averaging the coordinates of the corners.
	public void calcCentroid() {
		double cX=0,cY=0;
		for (int i=0; i<vertices;i++) {
			cX += myCorners[i].x;
		}
		for (int i=0; i<vertices;i++) {
			cY += myCorners[i].y;
		}
		cX /= vertices;
		cY /= vertices;
		myCentroid = new Point((int)cX,(int)cY);
	}
	public Point[] getCorners() {
		return myCorners;
	}
	public void setCorners(Point[] corners) {
		this.myCorners = new Point[vertices];
		System.arraycopy(corners, 0, this.myCorners, 0, corners.length);
	}
	public Point getCentroid() {
		return myCentroid;
	}
	public void setCentroid(Point centroid) {
		this.myCentroid = new Point(centroid);
	}
	public String toString() {
		return "Corners: " + Arrays.toString(myCorners) + " Size: " + sizeInPixels;
	}
}
