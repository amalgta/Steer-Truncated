package com.styx.steer.Protocol;


import com.styx.steer.Protocol.action.SteerAction;

public interface SteerActionReceiver
{
	public void receiveAction(SteerAction action);
}
