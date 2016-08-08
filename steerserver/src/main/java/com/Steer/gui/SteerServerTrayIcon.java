package com.Steer.gui;

import com.Steer.protocol.SteerConnection;
import com.Steer.protocol.bluetooth.SteerConnectionBluetooth;
import com.Steer.protocol.tcp.SteerConnectionTcp;
import com.Steer.server.SteerServerApp;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class SteerServerTrayIcon
{
	private Preferences preferences;
	private SteerServerApp application;
	private TrayIcon trayIcon;
	
	public SteerServerTrayIcon(SteerServerApp application) throws AWTException, IOException
	{
		this.application = application;
		
		this.preferences = this.application.getPreferences();
		
		this.initTrayIcon();
	}
	
	public void notifyConnection(SteerConnection connection)
	{
		String message = "";
		
		if (connection instanceof SteerConnectionTcp)
		{
			SteerConnectionTcp connectionTcp = (SteerConnectionTcp) connection;
			message = connectionTcp.getInetAddress().getHostAddress() + ":" + connectionTcp.getPort();
		}
		else if (connection instanceof SteerConnectionBluetooth)
		{
			message = "Bluetooth";
		}

		this.trayIcon.displayMessage("Steer", (connection.active ? "New connection : " : "Device Disconnected: ") + message, MessageType.INFO);
	}
	
	public void notifyProtocolProblem()
	{
		this.trayIcon.displayMessage("Steer", "Protocol problem. Please Download the server again", MessageType.INFO);
	}
	
	public void close()
	{
		SystemTray.getSystemTray().remove(this.trayIcon);
	}
	
	private void initTrayIcon() throws AWTException, IOException
	{
		PopupMenu menu = new PopupMenu();
		
		MenuItem menuItemPassword = new MenuItem("Password");
		menuItemPassword.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String password = SteerServerTrayIcon.this.preferences.get("password", SteerConnection.DEFAULT_PASSWORD);
				password = JOptionPane.showInputDialog("Password", password);
				if (password != null)
				{
					SteerServerTrayIcon.this.preferences.put("password", password);
				}
			}
		});
		menu.add(menuItemPassword);

		if (SteerServerApp.IS_WINDOWS)
		{
			final CheckboxMenuItem menuItemUnicodeWindows = new CheckboxMenuItem("Force disable Unicode Windows alt trick", this.preferences.getBoolean("force_disable_unicode_windows_alt_trick", false));
			menuItemUnicodeWindows.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					SteerServerTrayIcon.this.preferences.putBoolean("force_disable_unicode_windows_alt_trick", menuItemUnicodeWindows.getState());
					JOptionPane.showMessageDialog(null, "Restart the connection to apply this preference.");
				}
			});
			//menu.add(menuItemUnicodeWindows);
		}
		
		menu.addSeparator();
		
		MenuItem menuItemWifiServer = new MenuItem("Wifi Server");
		menuItemWifiServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuilder message = new StringBuilder();

				if (SteerServerTrayIcon.this.getTcpListenAddresses() != null) {
					message.append("Wifi server is listening on :\n");
					message.append(SteerServerTrayIcon.this.getTcpListenAddresses());
				} else {
					message.append("Wifi server is not running");
				}

				JOptionPane.showMessageDialog(null, message.toString());
			}
		});
		menu.add(menuItemWifiServer);
		
		MenuItem menuItemPort = new MenuItem("Port");
		menuItemPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int port = SteerServerTrayIcon.this.preferences.getInt("port", SteerConnectionTcp.DEFAULT_PORT);
					String newPortString = JOptionPane.showInputDialog("Port", port);
					int newPort = Integer.parseInt(newPortString);
					SteerServerTrayIcon.this.preferences.putInt("port", newPort);
					JOptionPane.showMessageDialog(null, "Restart the server to apply the new port.");
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
			}
		});
		menu.add(menuItemPort);
		
		menu.addSeparator();
		
		MenuItem menuItemBluetoothServer = new MenuItem("Bluetooth Server");
		menuItemBluetoothServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StringBuilder message = new StringBuilder();

				if (SteerServerTrayIcon.this.application.getServerBluetooth() != null) {
					message.append("Bluetooth server is running");
				} else {
					message.append("Bluetooth server is not running");
				}

				JOptionPane.showMessageDialog(null, message.toString());
			}
		});
		menu.add(menuItemBluetoothServer);
		
		menu.addSeparator();
		
		MenuItem menuItemExit = new MenuItem("Exit");
		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SteerServerTrayIcon.this.application.exit();
			}
		});
		menu.add(menuItemExit);

		this.trayIcon = new TrayIcon(ImageIO.read(this.getClass().getResourceAsStream("/images/icon.png")));
		this.trayIcon.setImageAutoSize(true);
		this.trayIcon.setToolTip("Steer Server");
		this.trayIcon.setPopupMenu(menu);

		SystemTray.getSystemTray().add(this.trayIcon);
		
		StringBuilder message = new StringBuilder("Server started\n");
		message.append(this.getTcpListenAddresses());
		
		this.trayIcon.displayMessage("Steer", message.toString(), MessageType.INFO);
	}
	
	private String getTcpListenAddresses()
	{
		int port = this.preferences.getInt("port", SteerConnectionTcp.DEFAULT_PORT);
		
		StringBuilder message = new StringBuilder();
		
		try
		{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements())
			{
				NetworkInterface currentInterface = interfaces.nextElement();
				
				Enumeration<InetAddress> addresses = currentInterface.getInetAddresses();
				
				while (addresses.hasMoreElements())
				{
					InetAddress currentAddress = addresses.nextElement();
					
					if (!currentAddress.isLoopbackAddress() && !(currentAddress instanceof Inet6Address))
					{
						message.append(currentAddress.getHostAddress() + ":" + port + "\n");
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return message.toString();
	}
}
