import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JComboBox;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class ApplicationDialog extends JDialog implements ActionListener {

	private JPanel portPanel = new JPanel();
	private JButton cancelButton;
	private JButton startButton;
	private JButton btnRefresh;
	private JComboBox portBox;
	private ArduinoRunnable arduRunnable;
	private JLabel lblStatus;
	private Thread arduThread;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ApplicationDialog dialog = new ApplicationDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ApplicationDialog() {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
			e.printStackTrace();
		}

		setTitle("PHS R.C.E. - Ability One Design Challenge 2013");
		setBounds(100, 100, 450, 100);
		getContentPane().setLayout(new BorderLayout());
		portPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(portPanel, BorderLayout.CENTER);

		//Initializing components.
		JLabel lblPort = new JLabel("Port:");
		lblStatus = new JLabel("Status: ");
		cancelButton = new JButton("Cancel");
		startButton = new JButton("Start");
		btnRefresh = new JButton("Refresh");
		portBox = new JComboBox();
		JPanel buttonPane = new JPanel();

		//Setting constraints on components
		GridBagLayout gbl_portPanel = new GridBagLayout();
		gbl_portPanel.columnWidths = new int[]{35, 0, 0, 0};
		gbl_portPanel.rowHeights = new int[]{0, 0};
		gbl_portPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_portPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		portPanel.setLayout(gbl_portPanel);

		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.insets = new Insets(0, 0, 0, 5);
		gbc_lblPort.anchor = GridBagConstraints.EAST;
		gbc_lblPort.gridx = 0;
		gbc_lblPort.gridy = 0;
		portPanel.add(lblPort, gbc_lblPort);

		GridBagConstraints gbc_portBox = new GridBagConstraints();
		gbc_portBox.insets = new Insets(0, 0, 0, 5);
		gbc_portBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_portBox.gridx = 1;
		gbc_portBox.gridy = 0;
		portPanel.add(portBox, gbc_portBox);

		GridBagConstraints gbc_btnRefresh = new GridBagConstraints();
		gbc_btnRefresh.gridx = 2;
		gbc_btnRefresh.gridy = 0;
		portPanel.add(btnRefresh, gbc_btnRefresh);

		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.add(lblStatus);
		buttonPane.add(startButton);
		getRootPane().setDefaultButton(startButton);
		buttonPane.add(cancelButton);

		//Adding ActionListeners to Buttons
		startButton.addActionListener(this);
		cancelButton.addActionListener(this);
		btnRefresh.addActionListener(this);

		this.refreshPortList();

		arduRunnable = new ArduinoRunnable();
		cancelButton.setEnabled(false);;

	}

	public void refreshPortList() {
		try {
			ArrayList<String> ports = ArduinoBoard.getOpenPorts();
			portBox.setModel(new DefaultComboBoxModel(ports.toArray(new String[ports.size()])));
		}
		catch (Exception e) {
			System.err.println("Could not list ports.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();

		if (src == btnRefresh) {
			this.refreshPortList();
		}
		else if (src == cancelButton) {
			arduThread.interrupt();
			cancelButton.setEnabled(false);
		}
		else if (src == startButton) {
			arduRunnable.setPort((String)portBox.getSelectedItem());
			lblStatus.setText("Status: Started.  ");
			arduThread = new Thread(arduRunnable);
			arduThread.start();
			cancelButton.setEnabled(true);

		}

	}

}
