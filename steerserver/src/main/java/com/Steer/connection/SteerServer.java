package com.Steer.connection;

import com.Steer.server.SteerServerApp;

public abstract class SteerServer
{
	protected SteerServerApp application;

	//apple
	public SteerServer(SteerServerApp application)
	{
		this.application = application;
	}
	
	public abstract void close();
}
