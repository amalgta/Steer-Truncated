package com.Steer.protocol;

import com.Steer.protocol.action.SteerAction;

public interface SteerActionReceiver
{
	public void receiveAction(SteerAction action);
}
