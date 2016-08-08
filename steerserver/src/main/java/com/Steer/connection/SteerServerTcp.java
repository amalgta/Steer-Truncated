package com.Steer.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.Steer.protocol.tcp.SteerConnectionTcp;
import com.Steer.server.SteerServerApp;

public class SteerServerTcp extends SteerServer implements Runnable
{
	private ServerSocket serverSocket;
	
	public SteerServerTcp(SteerServerApp application) throws IOException
	{
		super(application);
		
		int port = this.application.getPreferences().getInt("port", SteerConnectionTcp.DEFAULT_PORT);
		this.serverSocket = new ServerSocket(port);
		
		(new Thread(this)).start();
	}
	
	public void run()
	{
		try
		{
			while (true)
			{
				Socket socket = this.serverSocket.accept();
				SteerConnectionTcp connection = new SteerConnectionTcp(socket);
				new SteerServerConnection(this.application, connection);
			}
		}
		catch (IOException e)
		{
			System.out.println("LAN connection broke. This is normal if the server is shutting down.");
			// e.printStackTrace();
		}
	}
	
	public void close()
	{
		try
		{
			if (this.serverSocket != null)
				this.serverSocket.close();
		}
		catch (IOException e)
		{
			System.out.println("Couldn't close the LAN connection. :/ ");
			e.printStackTrace();
		}
	}
	
}
