package com.styx.steer.Client.Connection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.R;
import com.styx.steer.Protocol.SteerConnection;
import com.styx.steer.Protocol.tcp.SteerConnectionTcp;

import java.io.IOException;


public class ConnectionWifi extends Connection
{
	private static final long serialVersionUID = 1L;
	private String host;
	private int port;
	
	public ConnectionWifi()
	{
		super();
		this.host = "";
		this.port = SteerConnectionTcp.DEFAULT_PORT;
        //this.address=getHost()+":"+this.port;
    }

    public static ConnectionWifi load(SharedPreferences preferences, int position) {
		ConnectionWifi connection = new ConnectionWifi();

        connection.host = preferences.getString("connection_" + position + "_host", null);

        connection.port = preferences.getInt("connection_" + position + "_port", 0);

        return connection;
    }

    public String getAddress() {
        return getHost();
    }

    public void save(Editor editor, int position) {
		super.save(editor, position);
		
		editor.putInt("connection_" + position + "_type", WIFI);
		
		editor.putString("connection_" + position + "_host", this.host);
		
		editor.putInt("connection_" + position + "_port", this.port);
	}
	
	public SteerConnection connect(Steer application) throws IOException
	{
		return SteerConnectionTcp.create(this.host, this.port);
	}
	
	public String getHost()
	{
		return host;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
}
