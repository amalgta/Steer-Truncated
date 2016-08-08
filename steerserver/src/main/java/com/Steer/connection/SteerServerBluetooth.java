package com.Steer.connection;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.Steer.protocol.SteerConnection;
import com.Steer.protocol.bluetooth.SteerConnectionBluetooth;
import com.Steer.server.SteerServerApp;

public class SteerServerBluetooth extends SteerServer implements Runnable
{
	private StreamConnectionNotifier streamConnectionNotifier;
	
	public SteerServerBluetooth(SteerServerApp application) throws IOException
	{
		super(application);
		
		String uuid = SteerConnection.BLUETOOTH_UUID.replaceAll("-", "");
		
		try
		{
			this.streamConnectionNotifier = (StreamConnectionNotifier) Connector.open("btspp://localhost:" + uuid + ";name=Steer");
			
			if (this.streamConnectionNotifier != null)
			{
				(new Thread(this)).start();
			}
			else
			{
				throw new IOException();
			}
		}
		catch (Exception e)
		{
			// Could not start Bluetooth services.
			// TODO: Do something here to disable the bluetooth option, or
			// provide a nice error message.
			
		}
	}
	
	public void run()
	{
		try
		{
			while (true)
			{
				StreamConnection streamConnection = streamConnectionNotifier.acceptAndOpen();
				SteerConnectionBluetooth connection = new SteerConnectionBluetooth(streamConnection);
				new SteerServerConnection(this.application, connection);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try
		{
			if (this.streamConnectionNotifier != null)
				this.streamConnectionNotifier.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
