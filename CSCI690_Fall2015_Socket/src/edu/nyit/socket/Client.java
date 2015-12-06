package edu.nyit.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

import edu.nyit.socket.ClientGUI.Logger;

/**
 * @author Bowen Song 
 * ID: 1035197 
 * CSCI 690
 */
public class Client implements Runnable
{
	private String ipAddress;
	private int port;
	private Socket s;
	private PrintWriter out;
	private JTextArea area;
	private Logger l = Logger.getInstance();

	/**
	 * Message sending client
	 * 
	 * @param ipAddress
	 *            IP Address
	 * @param port
	 *            Port number
	 * @param area
	 *            Chat message area
	 */
	public Client(String ipAddress, int port, JTextArea area)
	{
		this.ipAddress = ipAddress;
		this.port = port;
		this.area = area;
	}

	@Override
	public void run()
	{
		try
		{
			l.print("Client connect to " + ipAddress + ":" + port + "...");
			s = new Socket(ipAddress, 9090);
			l.print(s.toString());
			out = new PrintWriter(s.getOutputStream(), true);
		}
		catch (IOException e)
		{
			l.print(e.getMessage());
		}
	}

	/**
	 * Send a message to other client
	 * 
	 * @param input
	 *            Message to be sent
	 */
	public void send(String input)
	{
		out.println(input);
		area.append(input + "\n");
		l.print("Sending:" + input);
	}

}
