package com.Steer.protocol;

import com.Steer.protocol.action.SteerAction;

public interface SteerActionReceiver
{
	void receiveAction(SteerAction action);
}
