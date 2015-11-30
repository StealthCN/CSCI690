package edu.nyit.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

import edu.nyit.socket.ClientGUI.Logger;

public class Client implements Runnable
{
	private String ipAddress;
	private BufferedReader in;
	private PrintWriter out;
	private JTextArea area;
	private Logger l = Logger.getInstance();

	public Client(String ipAddress, JTextArea area)
	{
		this.ipAddress = ipAddress;
		this.area = area;
	}

	@Override
	public void run()
	{
		try
		{
			l.print("Client connecting to " + ipAddress + "...");
			Socket s = new Socket(ipAddress, 9090);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
		}
		catch (IOException e)
		{
			l.print(e.getMessage());
		}
	}

	public void send(String input)
	{
		out.println(input);
		area.append(input + "\n");
		l.print("Sending:" + input);
	}

}
