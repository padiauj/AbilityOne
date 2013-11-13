import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

	public static void main(String[] args) throws Exception {
		ArduinoBoard ardu = new ArduinoBoard("COM7");
		ardu.connect();

		if (ardu.isConnected()) {
			if (ardu.readSerialLine().equals("B1") ) {
				
				//Java Robot stuff here...
			}

		}
		System.exit(0);
	}
}
