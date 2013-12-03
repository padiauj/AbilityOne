import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.crypto.Data;

public class ArduinoBoard {

	private CommPortIdentifier portIdentifier;
	private CommPort port;
	private SerialPort serialPort;
	private InputStream in;
	boolean connected;



	public ArduinoBoard(String port) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
		this.portIdentifier = CommPortIdentifier.getPortIdentifier(port);
	}

	public static void listOpenComputerPorts() {
		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			System.out.println(portIdentifier.getName() + " - "
					+ getPortTypeName(portIdentifier.getPortType()));
		}
	}

	public static String getPortTypeName(int portType) {
		switch (portType) {
		case CommPortIdentifier.PORT_I2C:
			return "I2C";
		case CommPortIdentifier.PORT_PARALLEL:
			return "Parallel";
		case CommPortIdentifier.PORT_RAW:
			return "Raw";
		case CommPortIdentifier.PORT_RS485:
			return "RS485";
		case CommPortIdentifier.PORT_SERIAL:
			return "Serial";
		default:
			return "unknown type";
		}
	}

	public boolean connect() {
		try {

			if (portIdentifier.isCurrentlyOwned()) {
				System.err.println("Error: Port is currently in use");
			} else {
				this.port = portIdentifier.open(this.getClass().getName(), 2000);
			}

			if (this.port instanceof SerialPort) {
				this.serialPort = (SerialPort) this.port;
				this.serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			}
			this.in = this.port.getInputStream();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		this.setConnected(true);
		return true;

	}

	public boolean hasSerial() {
		try {
			return in.available() > 0;
		} catch (IOException e) {
			return false;
		}		
	}

	public void setConnected(boolean state) {
		this.connected = state;
	}

	public boolean isConnected() {
		return this.connected;
	}

	public String readSerialLine() {
		StringBuilder sb = new StringBuilder();
		try {
			byte data;
			do {
				data = (byte) this.in.read();
				if (data != -1) {
					sb.append((char) data);
				}
			} while (data != '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().trim();
	}
	public static void showIcon(BufferedImage image) {
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon, JLabel.CENTER);
		JOptionPane.showMessageDialog(null, label,
				"AbilityOne Image Processing", -1);
	}



	public static void main(String[] args) throws Exception {

		if (args.length == 1) {

			if (args[0].indexOf("PORTLIST") != -1) {
				listOpenComputerPorts();	
			}
			else {
				ArduinoBoard ardu = new ArduinoBoard(args[0]);
				ardu.connect();
				Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

				Robot rbt = new Robot();

				while (ardu.isConnected()) {
					String decision = ardu.readSerialLine();
					if (decision.equals("BL") ) {
						BufferedImage screen = rbt.createScreenCapture(screenRect);
						MarkerDetector md = new MarkerDetector(screen, Marker.RED); 
						md.detect();
						Marker m = md.getLargestMarker();

						System.out.println(m);
						rbt.mouseMove((int)m.getCentroid().getX(), (int)m.getCentroid().getY());
						rbt.mousePress(InputEvent.BUTTON1_MASK);
						rbt.mouseRelease(InputEvent.BUTTON1_MASK);


						System.out.println(m.toString());

					}
					else if (decision.equals("BM")) {

						BufferedImage screen = rbt.createScreenCapture(screenRect);
						MarkerDetector md = new MarkerDetector(screen, Marker.BLUE); 
						md.detect();
						Marker m = md.getLargestMarker();

						rbt.mouseMove((int)m.getCentroid().getX(), (int)m.getCentroid().getY());
						rbt.mousePress(InputEvent.BUTTON1_MASK);
						rbt.mouseRelease(InputEvent.BUTTON1_MASK);
						System.out.println(m.toString());

					}
					else if (decision.equals("BR")) {

						BufferedImage screen = rbt.createScreenCapture(screenRect);
						//showIcon(screen);
						MarkerDetector md = new MarkerDetector(screen, Marker.GREEN); 
						md.detect();
						Marker m = md.getLargestMarker();

						rbt.mouseMove((int)m.getCentroid().getX(), (int)m.getCentroid().getY());
						rbt.mousePress(InputEvent.BUTTON1_MASK);
						rbt.mouseRelease(InputEvent.BUTTON1_MASK);
						System.out.println(m.toString());


					}


				}
			}
		}
		else {
			System.out.println("USAGE: dashboard.jar [PORT/COMMAND]");
		}
		System.exit(0);
	}
}
