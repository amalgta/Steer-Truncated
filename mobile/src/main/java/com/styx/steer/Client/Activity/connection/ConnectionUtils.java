package com.styx.steer.Client.Activity.connection;

import com.styx.steer.Client.Connection.Connection;

/**
 * Created by fly on 12/8/16.
 */
public class ConnectionUtils {
    public static Connection editWifiConnection(Connection editedConnection) {
        editedConnection.setName("SASI");
        return editedConnection;
    }
}
