package com.styx.steer.Protocol.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.styx.steer.Protocol.SteerConnection;


public class SteerConnectionTcp extends SteerConnection
{
	public final static int DEFAULT_PORT = 64788;
	
	private Socket socket;
	
	public SteerConnectionTcp(Socket socket) throws IOException
	{
		super(socket.getInputStream(), socket.getOutputStream());
		
		this.socket = socket;
		this.socket.setPerformancePreferences(0, 2, 1);
		this.socket.setTcpNoDelay(true);
		this.socket.setReceiveBufferSize(1024 * 1024);
		this.socket.setSendBufferSize(1024 * 1024);
	}
	
	public static SteerConnectionTcp create(String host, int port) throws IOException
	{
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(host, port), 1000);
		
		SteerConnectionTcp connection = new SteerConnectionTcp(socket);
		
		return connection;
	}
	
	public InetAddress getInetAddress()
	{
		return this.socket.getInetAddress();
	}
	
	public int getPort()
	{
		return this.socket.getPort();
	}
	
	public void close() throws IOException
	{
		this.socket.shutdownInput();
		this.socket.shutdownOutput();
		super.close();
		this.socket.close();
	}
}
