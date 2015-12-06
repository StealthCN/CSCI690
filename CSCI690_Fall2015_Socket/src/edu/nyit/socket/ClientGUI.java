package edu.nyit.socket;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Bowen Song 
 * ID: 1035197 
 * CSCI 690
 */
public class ClientGUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSplitPane splitPane;
	private JPanel mainPanel;
	private JPanel topPanel;
	private JLabel ipLabel;
	private JTextField ipTextFiled;
	private JButton connectButton;
	private JTextArea mainTextArea;
	private JPanel bottomPanel;
	private JTextField inputField;
	private JButton sendButton;

	private static JTextArea console;

	private ConnectHandler getIPHandler;
	private SendMsgHandler sendMsgHandler;

	private String ipAddress;
	private int port;

	private Client c;
	private Logger l = Logger.getInstance();

	public static void main(String[] args)
	{
		new ClientGUI();
	}

	public ClientGUI()
	{
		String p = JOptionPane.showInputDialog("Enter port:");
		port = Integer.parseInt(p);
		layoutGUI();
		startServer(port);
	}

	/**
	 * Start listening server on specified port
	 * 
	 * @param port
	 */
	public void startServer(int port)
	{
		ServerSocket listener = null;
		try
		{
			listener = new ServerSocket(port);
			Server s = new Server(listener.accept(), mainTextArea);
			Thread t = new Thread(s);
			t.start();
		}
		catch (IOException e)
		{
			l.print(e.getMessage());
		}
		finally
		{
			try
			{
				listener.close();
			}
			catch (IOException e)
			{
				l.print(e.getMessage());
			}
		}
	}

	/**
	 * Layout GUI component
	 */
	public void layoutGUI()
	{
		ipLabel = new JLabel("IP:");
		ipTextFiled = new JTextField(10);
		connectButton = new JButton("Connect");
		mainTextArea = new JTextArea();
		mainTextArea.setEditable(false);
		inputField = new JTextField(10);
		sendButton = new JButton("Send");

		topPanel = new JPanel();
		topPanel.add(ipLabel);
		topPanel.add(ipTextFiled);
		topPanel.add(connectButton);

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(inputField, BorderLayout.CENTER);
		bottomPanel.add(sendButton, BorderLayout.EAST);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(mainTextArea, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		console = new JTextArea();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel,
				console);

		this.add(splitPane);

		getIPHandler = new ConnectHandler();
		connectButton.addActionListener(getIPHandler);

		sendMsgHandler = new SendMsgHandler();
		sendButton.addActionListener(sendMsgHandler);

		this.setSize(400, 300);
		this.setTitle("Chat");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Start a connection to another client
	 * 
	 * @param ip
	 *            IP Address
	 * @param port
	 */
	private void initConnection(String ip, int port)
	{
		c = new Client(ip, port, mainTextArea);
		Thread t = new Thread(c);
		t.start();
	}

	class ConnectHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			ipAddress = ipTextFiled.getText();
			initConnection(ipAddress, port);
		}
	}

	class SendMsgHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			c.send(inputField.getText());
			mainTextArea.selectAll();
			inputField.setText("");
		}
	}

	static class Logger
	{
		private static Logger instance = null;

		/**
		 * Singleton helper class for printing messages to console
		 */
		protected Logger(){}

		public static Logger getInstance()
		{
			if (instance == null)
			{
				instance = new Logger();
			}
			return instance;

		}

		public void print(String s)
		{
			console.append(s + "\n");
		}
	}
}
