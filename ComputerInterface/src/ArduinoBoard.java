import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ArduinoBoard {

	private CommPortIdentifier portIdentifier;
	private CommPort port;
	private SerialPort serialPort;
	private InputStream in;
	boolean connected;

	public ArduinoBoard(String port) {
		try{
			this.portIdentifier = CommPortIdentifier.getPortIdentifier(port);
		}
		catch (Exception e) {
			System.err.println("RXTX Library issues.");
		}
	}

	public static void printOpenComputerPorts() {
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			System.out.println(portIdentifier.getName() + " - "
					+ getPortTypeName(portIdentifier.getPortType()));
		}
	}

	public static ArrayList<String> getOpenPorts() {
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		ArrayList<String> ports = new ArrayList<String>();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			ports.add(portIdentifier.getName());
		}
		return ports;
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

	public boolean connect() throws UnsupportedCommOperationException, PortInUseException, IOException {
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
		BufferedImage screen = ImageIO.read(new File("test.jpg"));
		Graphics2D g = screen.createGraphics();
		MarkerDetector md = null;
		md = new MarkerDetector(screen, Marker.GREEN);
		ArrayList<Marker> markers = md.detect();
		g.setColor(Color.BLACK);
		System.out.println(markers.size());
		for (Marker m : markers) {
			System.out.println(m);
			if (m != null) {
				g.fillOval((int)m.getCentroid().getX()-2, (int)m.getCentroid().getY()+2, 2, 2);
				System.out.println(m);
			}
		}
		showIcon(screen);
	}
}
