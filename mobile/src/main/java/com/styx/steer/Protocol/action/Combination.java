package com.styx.steer.Protocol.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Combination extends SteerAction
{
	public static final int UNICODE_CUT = 35;
	public static final int UNICODE_COPY = 36;
	public static final int UNICODE_PASTE = 37;
	public static final int UNICODE_UNDO = 38;
	public static final int UNICODE_FIND = 39;
	public static final int UNICODE_ANIM = 40;
	public static final int B_HIS = 41;
	public static final int B_DL = 42;
	public static final int B_TAB = 43;
	public static final int B_TABC = 44;
	public static final int B_BOOKM = 45;
	public static final int EXIT = 50;
	public static final int B_SAVE = 46;
	public static final int B_PRINT = 47;
	public static final int B_LASER = 48;
	
	public int unicode;
	
	public Combination(int unicode)
	{
		this.unicode = unicode;
	}
	
	public static Combination parse(DataInputStream dis) throws IOException
	{
		int unicode = dis.readInt();
		
		return new Combination(unicode);
	}
	
	public void toDataOutputStream(DataOutputStream dos) throws IOException
	{
		dos.writeByte(COMBINATION);
		dos.writeInt(this.unicode);
	}
}
