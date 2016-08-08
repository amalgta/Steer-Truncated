package com.Steer.protocol.bluetooth;

import com.Steer.protocol.SteerConnection;

import java.io.IOException;

import javax.microedition.io.StreamConnection;

public class SteerConnectionBluetooth extends SteerConnection
{
	private StreamConnection streamConnection;
	
	public SteerConnectionBluetooth(StreamConnection streamConnection) throws IOException
	{
		super(streamConnection.openInputStream(), streamConnection.openOutputStream());
		
		this.streamConnection = streamConnection;
	}
	
	public void close() throws IOException
	{
		this.streamConnection.close();
		super.close();
	}
}
