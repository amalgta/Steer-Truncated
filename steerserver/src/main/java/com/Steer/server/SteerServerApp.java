package com.Steer.server;

import com.Steer.connection.SteerServerBluetooth;
import com.Steer.connection.SteerServerTcp;
import com.Steer.gui.SteerServerTrayIcon;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.prefs.Preferences;

public class SteerServerApp
{
    public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    private Preferences preferences;
    private SteerServerTrayIcon trayIcon;
    private Robot robot;

    private SteerServerTcp serverTcp;
    private SteerServerBluetooth serverBluetooth;

    public SteerServerApp() throws AWTException, IOException
    {
        this.preferences = Preferences.userNodeForPackage(this.getClass());

        this.robot = new Robot();

        this.trayIcon = new SteerServerTrayIcon(this);

        try
        {
            this.serverTcp = new SteerServerTcp(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            this.serverBluetooth = new SteerServerBluetooth(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Preferences getPreferences()
    {
        return preferences;
    }

    public SteerServerTrayIcon getTrayIcon()
    {
        return trayIcon;
    }

    public Robot getRobot()
    {
        return robot;
    }

    public SteerServerTcp getServerTcp()
    {
        return serverTcp;
    }

    public SteerServerBluetooth getServerBluetooth()
    {
        return serverBluetooth;
    }

    public void exit()
    {
        this.trayIcon.close();

        if (this.serverTcp != null)
        {
            this.serverTcp.close();
        }

        if (this.serverBluetooth != null)
        {
            this.serverBluetooth.close();
        }

        System.exit(0);
    }

    public static void main(String[] args)
    {
        try
        {
            @SuppressWarnings("unused")
            SteerServerApp application = new SteerServerApp();
        }
        catch (AWTException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
