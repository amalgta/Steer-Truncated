package com.styx.steer.Client.App;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.Reference;
import java.util.HashSet;

import com.styx.steer.Client.Connection.Connection;
import com.styx.steer.Client.Connection.ConnectionList;
import com.styx.steer.Client.R;
import com.styx.steer.Protocol.SteerActionReceiver;
import com.styx.steer.Protocol.SteerConnection;
import com.styx.steer.Protocol.action.AuthentificationAction;
import com.styx.steer.Protocol.action.AuthentificationResponseAction;
import com.styx.steer.Protocol.action.SteerAction;


public class Steer extends Application implements Runnable
{
	private static final long CONNECTION_CLOSE_DELAY = 3000;
	
	private SharedPreferences preferences;
	private Vibrator vibrator;
    private Reference<Activity> context;
	
	private SteerConnection[] connection;
	
	private HashSet<SteerActionReceiver> actionReceivers;
	
	private Handler handler;
	
	private CloseConnectionScheduler closeConnectionScheduler;
	
	private ConnectionList connections;
	
	private boolean requestEnableBluetooth;
	
	public void onCreate()
	{
		super.onCreate();
		
		this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
		PreferenceManager.setDefaultValues(this, R.xml.settings, true);
		
		this.vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		
		this.actionReceivers = new HashSet<SteerActionReceiver>();
		
		this.handler = new Handler();
		
		this.connection = new SteerConnection[1];
		
		this.closeConnectionScheduler = new CloseConnectionScheduler();
		
		this.connections = new ConnectionList(this.preferences);
		
		this.requestEnableBluetooth = true;

        Context  mcontext= getApplicationContext();
	}
	
	public SharedPreferences getPreferences()
	{
		return this.preferences;
	}
	
	public ConnectionList getConnections()
	{
		return this.connections;
	}
	
	public void vibrate(long l)
	{
		if (this.preferences.getBoolean("feedback_vibration", true))
		{
			this.vibrator.vibrate(l);
		}
	}
	
	public boolean requestEnableBluetooth()
	{
		boolean b = this.requestEnableBluetooth;
		
		this.requestEnableBluetooth = false;
		
		return b;
	}

	public synchronized void run()
	{
		Connection co = this.connections.getUsed();
		
		if (co != null)
		{
			SteerConnection c = null;
			
			try
			{
				c = co.connect(this);
				
				synchronized (this.connection)
				{
					this.connection[0] = c;
				}
				
				try
				{
					this.showInternalToast(R.string.text_connection_established);
					
					String password = co.getPassword();
					this.sendAction(new AuthentificationAction(password));
					
					while (true)
					{
						SteerAction action = c.receiveAction();

						this.receiveAction(action);
					}
				}
				finally
				{
					synchronized (this.connection)
					{
						this.connection[0] = null;
					}
					
					c.close();
				}
			}
			catch (IOException e)
			{
				this.debug(e);
				
				if (c == null)
				{
					this.showInternalToast(R.string.text_connection_refused);
				}
				else
				{
					this.showInternalToast(R.string.text_connection_closed);
				}
			}
			catch (IllegalArgumentException e)
			{
				this.debug(e);
				
				this.showInternalToast(R.string.text_illegal_connection_parameter);
			}
		}
		else
		{
			this.showInternalToast(R.string.text_no_connection_selected);
		}
	}
	
	public void sendAction(SteerAction Action)
	{
		synchronized (this.connection)
		{
			if (this.connection[0] != null)
			{
				try
				{
					this.connection[0].sendAction(Action);
				}
				catch (IOException e)
				{
					this.debug(e);
				}
			}
		}
	}
	
	public void showInternalToast(int resId)
	{
		if (this.isInternalToast())
		{
			this.showToast(resId);
		}
	}
	
	public void showInternalToast(String message)
	{
		if (this.isInternalToast())
		{
			this.showToast(message);
		}
	}
	
	public boolean isInternalToast()
	{
		synchronized (this.actionReceivers)
		{
			return !this.actionReceivers.isEmpty();
		}
	}
	
	public void showToast(int resId)
	{
		this.showToast(this.getResources().getString(resId));
	}
	
	public void showToast(final String message)
	{
		this.handler.post(new Runnable() {
            public void run() {
                Toast.makeText(Steer.this, message, Toast.LENGTH_SHORT).show();
            }
        });
	}

	private void receiveAction(SteerAction action)
	{
		synchronized (this.actionReceivers)
		{
			for (SteerActionReceiver actionReceiver : this.actionReceivers)
			{
				actionReceiver.receiveAction(action);
			}
		}
		
		if (action instanceof AuthentificationResponseAction)
		{
			this.receiveAuthentificationResponseAction((AuthentificationResponseAction) action);
		}
	}
	
	private void receiveAuthentificationResponseAction(AuthentificationResponseAction action)
	{
		if (action.authentificated)
		{
			this.showInternalToast(R.string.text_authentificated);
		}
		else
		{
			this.showInternalToast(R.string.text_not_authentificated);
		}
	}
	
	public void registerActionReceiver(SteerActionReceiver actionReceiver)
	{
		synchronized (this.actionReceivers)
		{
			this.actionReceivers.add(actionReceiver);
			
			if (this.actionReceivers.size() > 0)
			{
				synchronized (this.connection)
				{
					if (this.connection[0] == null)
					{
						(new Thread(this)).start();
					}
				}
			}
		}
	}
	
	public void unregisterActionReceiver(SteerActionReceiver actionReceiver)
	{
		synchronized (this.actionReceivers)
		{
			this.actionReceivers.remove(actionReceiver);
			
			if (this.actionReceivers.size() == 0)
			{
				this.closeConnectionScheduler.schedule();
			}
		}
	}
	
	public void debug(Exception e)
	{
		if (this.preferences.getBoolean("debug_enabled", false))
		{
			Log.d(this.getResources().getString(R.string.app_name), null, e);
		}
	}
	
	private class CloseConnectionScheduler implements Runnable
	{
		private Thread currentThread;
		
		public synchronized void run()
		{
			try
			{
				this.wait(Steer.CONNECTION_CLOSE_DELAY);
				
				synchronized (Steer.this.actionReceivers)
				{
					if (Steer.this.actionReceivers.size() == 0)
					{
						synchronized (Steer.this.connection)
						{
							if (Steer.this.connection[0] != null)
							{
								Steer.this.connection[0].close();
								
								Steer.this.connection[0] = null;
							}
						}
					}
				}
				
				this.currentThread = null;
			}
			catch (InterruptedException e)
			{
				Steer.this.debug(e);
			}
			catch (IOException e)
			{
				Steer.this.debug(e);
			}
		}
		
		public synchronized void schedule()
		{
			if (this.currentThread != null)
			{
				this.currentThread.interrupt();
			}
			
			this.currentThread = new Thread(this);
			
			this.currentThread.start();
		}
	}
	
}
