package com.styx.steer.Client.Activity.drawer;

/**
 * 
 * Model per item menu
 * 
 * 
 */
public class NsMenuItemModel
{
	
	public int title;
	public int iconRes;
	public int counter;
    public int color;
	public boolean isHeader;
	
	public NsMenuItemModel(int title, int iconRes, int color, boolean header, int counter)
	{
		this.title = title;
		this.iconRes = iconRes;
		this.isHeader = header;
        this.color = color;
		this.counter = counter;
	}
	
	public NsMenuItemModel(int title, int iconRes, int color, boolean header)
	{
		this(title, iconRes, color, header, 0);
	}
	
	public NsMenuItemModel(int title, int iconRes, int color)
	{
		this(title, iconRes,color, false);
	}
	
}
