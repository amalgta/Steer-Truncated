package com.styx.steer.Client.Protocol.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Looper;

import java.io.IOException;
import java.util.UUID;

import com.styx.steer.Client.App.Steer;
import com.styx.steer.Protocol.SteerConnection;


public class SteerConnectionBluetooth extends SteerConnection
{
	private BluetoothSocket socket;
	
	public SteerConnectionBluetooth(BluetoothSocket socket) throws IOException
	{
		super(socket.getInputStream(), socket.getOutputStream());
		
		this.socket = socket;
	}
	
	public static SteerConnectionBluetooth create(Steer application, String address) throws IOException
	{
		Looper.prepare();
		
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		
		if (adapter != null)
		{
			if (adapter.isEnabled())
			{
				try
				{
					BluetoothDevice device = adapter.getRemoteDevice(address);
					
					if (device != null)
					{
						BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(SteerConnection.BLUETOOTH_UUID));
						socket.connect();
						
						SteerConnectionBluetooth connection = new SteerConnectionBluetooth(socket);
						
						return connection;
					}
				}
				catch (IllegalArgumentException e)
				{
					throw new IOException();
				}
			}
			else
			{
				if (application.requestEnableBluetooth())
				{
					Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					application.startActivity(intent);
				}
			}
		}
		
		throw new IOException();
	}
	
	public void close() throws IOException
	{
		this.socket.close();
		super.close();
	}
}
