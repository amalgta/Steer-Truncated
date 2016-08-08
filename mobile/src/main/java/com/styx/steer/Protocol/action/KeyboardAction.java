package com.styx.steer.Protocol.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class KeyboardAction extends SteerAction
{
	public static final int UNICODE_BACKSPACE = -1;
	public static final int UNICODE_PAGEUP = 1;
	public static final int UNICODE_PAGEDN = 2;
	public static final int UNICODE_TAB = 9;
	public static final int UNICODE_LEFTC = 15;
	public static final int UNICODE_ARROW_UP = 19;
	public static final int UNICODE_ARROW_DOWN = 20;
	public static final int UNICODE_ARROW_LEFT = 21;
	public static final int UNICODE_ARROW_RIGHT = 22;
	public static final int UNICODE_ESC = 23;
	public static final int UNICODE_CTRL = 24;
	public static final int UNICODE_F5 = 25;
	public static final int UNICODE_F = 26;
	public static final int UNICODE_SPACE = 27;
	public static final int UNICODE_S = 28;
	public static final int UNICODE_M = 29;
	public static final int UNICODE_N = 30;
	public static final int UNICODE_P = 31;
	public static final int UNICODE_L = 35;
	public static final int UNICODE_Z = 33;
	public static final int UNICODE_R = 34;
	
	public int unicode;
	
	public KeyboardAction(int unicode)
	{
		this.unicode = unicode;
	}
	
	public static KeyboardAction parse(DataInputStream dis) throws IOException
	{
		int unicode = dis.readInt();
		
		return new KeyboardAction(unicode);
	}
	
	public void toDataOutputStream(DataOutputStream dos) throws IOException
	{
		dos.writeByte(KEYBOARD);
		dos.writeInt(this.unicode);
	}
}
