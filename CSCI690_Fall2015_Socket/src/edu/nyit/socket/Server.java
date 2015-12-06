package edu.nyit.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JTextArea;

import edu.nyit.socket.ClientGUI.Logger;

/**
 * @author Bowen Song 
 * ID: 1035197 
 * CSCI 690
 */
public class Server implements Runnable
{
	private Socket socket;
	private JTextArea area;
	private Logger l = Logger.getInstance();

	/**
	 * Listening server
	 * 
	 * @param socket
	 *            Accepting socket
	 * @param area
	 *            Chat message area
	 */
	public Server(Socket socket, JTextArea area)
	{
		this.socket = socket;
		this.area = area;
	}

	public void run()
	{
		try
		{
			l.print("Started Server on port " + socket.getLocalPort());
			BufferedReader in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			while (true)
			{
				String line = in.readLine();
				if (line == null || line.equals("."))
				{
					break;
				}
				l.print("Recieving:" + line);
				area.append(line + "\n");
			}
		}
		catch (IOException e)
		{
			l.print(e.getMessage());
		}
		finally
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				l.print(e.getMessage());
			}
		}

	}
}
