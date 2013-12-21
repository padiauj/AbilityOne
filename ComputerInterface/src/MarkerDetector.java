import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class MarkerDetector {

	private BufferedImage target;
	private Color color;
	private ArrayList<Marker> markers;
	public MarkerDetector(BufferedImage target, Color markerType) {
		this.target = target;
		this.color = markerType;
	}

	public boolean isMarkerColor(int x,int y){
		int thresh = 30;
		int myred = this.color.getRed();
		int mygreen = this.color.getGreen();
		int myblue = this.color.getBlue();

		Color targetColor = new Color(target.getRGB(x, y));

		int tred = targetColor.getRed();
		int tgreen = targetColor.getGreen();
		int tblue = targetColor.getBlue();

		if ((tred + thresh >= myred && tred - thresh <= myred)
				&& (tgreen + thresh >= mygreen && tgreen - thresh <= mygreen)
				&& (tblue + thresh >= myblue && tblue - thresh <= myblue)) {
			return true;
		} else {
			return false;
		}

	}

	public ArrayList<Marker> detect() {
		ArrayList<Marker> markers = new ArrayList<Marker>();
		ArrayList<Point> marker = new ArrayList<Point>();

		boolean[][] painted =
				new boolean[target.getHeight()][target.getWidth()];

		for(int i = 0 ; i < target.getHeight() ; i++){
			for(int j = 0 ; j < target.getWidth() ; j++) {
				if(isMarkerColor(j,i) && !painted[i][j]){
					Queue<Point> queue = new LinkedList<Point>();
					queue.add(new Point(j,i));
					int pixelCount = 0;
					while(!queue.isEmpty()){
						Point p = queue.remove();
						if((p.x >= 0) && (p.x < target.getWidth() && (p.y >= 0) && (p.y < target.getHeight()))){
							if(!painted[p.y][p.x] && isMarkerColor(p.x,p.y)){
								painted[p.y][p.x] = true;
								marker.add(p);
								queue.add(new Point(p.x + 1,p.y)); queue.add(new Point(p.x - 1,p.y));
								queue.add(new Point(p.x,p.y + 1)); queue.add(new Point(p.x,p.y - 1));
							}
						}
					}
					markers.add(new Marker(marker, target.getHeight(), target.getWidth(), this.color));
					marker.clear();
				}

			}
		}

		this.markers = markers;
		return markers;
	}

	public Marker getLargestMarker() {
		if (markers == null || markers.size() == 0) {
			return null;
		}

		int recordIndex = 0;
		for (int i=0; i<markers.size(); i++) {
			if (markers.get(i).getSize() > markers.get(recordIndex).getSize()) {
				recordIndex = i;
			}
		}

		return markers.get(recordIndex);
	}
}