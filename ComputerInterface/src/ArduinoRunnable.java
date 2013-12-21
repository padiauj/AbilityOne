import gnu.io.PortInUseException;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JOptionPane;


public class ArduinoRunnable implements Runnable {

	private String port;
	private JDialog parent;
	public ArduinoRunnable(String port, JDialog dialog) {
		this.port = port;
		this.parent = dialog;
	}
	public ArduinoRunnable() {	
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void run() {
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		Robot rbt = null;
		try{
			rbt = new Robot();
		}
		catch (AWTException awte) {
			System.err.println("Could not create Robot.");
		}
		ArduinoBoard ardu = null;
		try {
			ardu = new ArduinoBoard(port);
			ardu.connect();
		} 
		catch (Exception e) {
			if (e instanceof PortInUseException) {
				JOptionPane.showMessageDialog(this.parent,
						"Port is in use, please select an unused port",
						"Inane error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		while (ardu.isConnected()) {
			String decision = ardu.readSerialLine();
			BufferedImage screen = rbt.createScreenCapture(screenRect);

			MarkerDetector md = null;
			if (decision.equals("A")) {
				md = new MarkerDetector(screen, Marker.RED);
			} else {
				md = new MarkerDetector(screen, Marker.GREEN);
			}

			md.detect();

			Marker m = md.getLargestMarker();
			if (m != null) {
				rbt.mouseMove((int)m.getCentroid().getX(), (int)m.getCentroid().getY());
				rbt.mousePress(InputEvent.BUTTON1_MASK);
				rbt.mouseRelease(InputEvent.BUTTON1_MASK);
			}

		}
	}
}
