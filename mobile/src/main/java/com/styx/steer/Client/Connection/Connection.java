package com.styx.steer.Client.Connection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.styx.steer.Client.App.Steer;
import com.styx.steer.Protocol.SteerConnection;

import java.io.IOException;
import java.io.Serializable;


public abstract class Connection implements Comparable<Connection>, Serializable
{
	public static final int TYPE_COUNT = 2;
	public static final int WIFI = 0;
	public static final int BLUETOOTH = 1;
    private static final long serialVersionUID = 1L;
    private String name;
	private String password;

    public Connection()
	{
		this.name = "";
		this.password = SteerConnection.DEFAULT_PASSWORD;
        // this.address = "";
        //this.thumbnail=R.drawable.home_connections;
    }

	public static Connection load(SharedPreferences preferences, ConnectionList list, int position)
	{
		Connection connection = null;
		int type = preferences.getInt("connection_" + position + "_type", -1);

		switch (type)
		{
			case WIFI:
				connection = ConnectionWifi.load(preferences, position);
                // connection.address = ConnectionWifi.load(preferences, position).getHost();
                break;
			case BLUETOOTH:
				connection = ConnectionBluetooth.load(preferences, position);
                //  connection.address = ConnectionBluetooth.load(preferences, position).getAddress();
                break;
        }

        connection.name = preferences.getString("connection_" + position + "_name", null);

        connection.password = preferences.getString("connection_" + position + "_password", null);

		return connection;
	}

    public abstract String getAddress();

    public void save(Editor editor, int position) {
		editor.putString("connection_" + position + "_name", this.name);
		
		editor.putString("connection_" + position + "_password", this.password);
	}
	
	public abstract SteerConnection connect(Steer application) throws IOException;

    //GTA

    public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public int compareTo(Connection c)
	{
		return this.name.compareTo(c.name);
	}
}
