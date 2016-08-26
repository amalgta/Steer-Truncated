package com.styx.steer.Client.Connection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.Protocol.bluetooth.SteerConnectionBluetooth;
import com.styx.steer.Client.R;
import com.styx.steer.Protocol.SteerConnection;

import java.io.IOException;


public class ConnectionBluetooth extends Connection
{
	private static final long serialVersionUID = 1L;
	private String address;
    private int thumbnail=R.drawable.album1;
	
	public ConnectionBluetooth()
	{
		super();
		this.address = "";

    }

	public static ConnectionBluetooth load(SharedPreferences preferences, int position)
	{
		ConnectionBluetooth connection = new ConnectionBluetooth();

        connection.address = preferences.getString("connection_" + position + "_address", null);

        return connection;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void save(Editor editor, int position) {
		super.save(editor, position);

        editor.putInt("connection_" + position + "_type", BLUETOOTH);

        editor.putString("connection_" + position + "_address", this.address);
    }

    public SteerConnection connect(Steer application) throws IOException {
		return SteerConnectionBluetooth.create(application, this.address);
	}

}
