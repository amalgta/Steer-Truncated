package com.styx.steer.Protocol.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Combinations extends SteerAction
{
	public static final int UNICODE_TASK = 40;
	
	public int unicode;
	
	public Combinations(int unicode)
	{
		this.unicode = unicode;
	}
	
	public static Combinations parse(DataInputStream dis) throws IOException
	{
		int unicode = dis.readInt();
		
		return new Combinations(unicode);
	}
	
	public void toDataOutputStream(DataOutputStream dos) throws IOException
	{
		dos.writeByte(COMBINATIONS);
		dos.writeInt(this.unicode);
	}
}
