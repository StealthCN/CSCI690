package edu.nyit.socket;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI extends JFrame
{
	private JSplitPane splitPane;
	private JPanel mainPanel;
	private JPanel topPanel;
	private JTextField ipTextFiled;
	private JButton ipApplyButton;
	private JTextArea mainTextArea;
	private JPanel bottomPanel;
	private JTextField inputField;
	private JButton sendButton;

	private static JTextArea console;

	private GetIPHandler getIPHandler;
	private SendMsgHandler sendMsgHandler;

	private String ipAddress;

	private Client c;
	private Logger l = Logger.getInstance();

	public static void main(String[] args)
	{
		ClientGUI cg = new ClientGUI();
		// cg.setVisible(true);
	}

	public ClientGUI()
	{
		layoutGUI();
		initData();
	}

	public void initData()
	{
		ServerSocket listener = null;
		try
		{
			listener = new ServerSocket(9090);
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

	public void layoutGUI()
	{
		ipTextFiled = new JTextField(10);
		ipApplyButton = new JButton("Connect");
		mainTextArea = new JTextArea();
		mainTextArea.setEditable(false);
		inputField = new JTextField(10);
		sendButton = new JButton("Send");

		topPanel = new JPanel();
		topPanel.add(ipTextFiled);
		topPanel.add(ipApplyButton);

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

		getIPHandler = new GetIPHandler();
		ipApplyButton.addActionListener(getIPHandler);

		sendMsgHandler = new SendMsgHandler();
		sendButton.addActionListener(sendMsgHandler);

		this.setSize(400, 300);
		this.setTitle("Chat");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void initConnection(String ip)
	{
		c = new Client(ip, mainTextArea);
		Thread t = new Thread(c);
		t.start();
	}

	class GetIPHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			initConnection(ipTextFiled.getText());
		}
	}

	class SendMsgHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			c.send(inputField.getText());
			/*
			 * String response; try { response = in.readLine(); if (response ==
			 * null || response.equals("")) { System.exit(0); } } catch
			 * (IOException ex) { response = "Error: " + ex; }
			 */
			mainTextArea.selectAll();
			inputField.setText("");
		}
	}

	static class Logger
	{
		private static Logger instance = null;

		protected Logger()
		{}

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
