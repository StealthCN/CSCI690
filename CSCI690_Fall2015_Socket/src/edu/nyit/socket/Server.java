package edu.nyit.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

import edu.nyit.socket.ClientGUI.Logger;

public class Server implements Runnable
{
	private Socket socket;
	private JTextArea area;
	private Logger l = Logger.getInstance();

	public Server(Socket socket, JTextArea area)
	{
		this.socket = socket;
		this.area = area;
	}
	
	public void run()
	{
		try
		{
			l.print("Server Running...");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			while (true)
			{
				String line = in.readLine();
				if (line == null || line.equals("."))
				{
					break;
				}
				l.print("Recieving:"+line);
				//out.println(input);
				area.append(line + "\n");
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
