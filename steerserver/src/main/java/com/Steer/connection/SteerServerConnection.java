package com.Steer.connection;

import com.Steer.protocol.SteerConnection;
import com.Steer.protocol.action.AuthentificationAction;
import com.Steer.protocol.action.AuthentificationResponseAction;
import com.Steer.protocol.action.Combination;
import com.Steer.protocol.action.Combinations;
import com.Steer.protocol.action.FileExploreRequestAction;
import com.Steer.protocol.action.FileExploreResponseAction;
import com.Steer.protocol.action.KeyboardAction;
import com.Steer.protocol.action.MouseClickAction;
import com.Steer.protocol.action.MouseMoveAction;
import com.Steer.protocol.action.MouseWheelAction;
import com.Steer.protocol.action.ScreenCaptureRequestAction;
import com.Steer.protocol.action.ScreenCaptureResponseAction;
import com.Steer.protocol.action.SteerAction;
import com.Steer.server.SteerServerApp;
import com.Steer.tools.UnicodeToSwingKeyCodeConverter;

import java.awt.Desktop;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SteerServerConnection implements Runnable
{
	private static final int[][] UNICODE_EXCEPTION = {
	        {
	                KeyboardAction.UNICODE_BACKSPACE, KeyEvent.VK_BACK_SPACE,
	        }, {
	                10, KeyEvent.VK_ENTER
	        }, {
	                KeyboardAction.UNICODE_PAGEDN, KeyEvent.VK_PAGE_DOWN
	        }, {
	                KeyboardAction.UNICODE_PAGEUP, KeyEvent.VK_PAGE_UP
	        }, {
	                KeyboardAction.UNICODE_TAB, KeyEvent.VK_TAB
	        }, {
	                KeyboardAction.UNICODE_ARROW_DOWN, KeyEvent.VK_DOWN
	        }, {
	                KeyboardAction.UNICODE_ARROW_UP, KeyEvent.VK_UP
	        }, {
	                KeyboardAction.UNICODE_ARROW_LEFT, KeyEvent.VK_LEFT
	        }, {
	                KeyboardAction.UNICODE_ARROW_RIGHT, KeyEvent.VK_RIGHT
	        }, {
	                KeyboardAction.UNICODE_ESC, KeyEvent.VK_ESCAPE
	        }, {
	                KeyboardAction.UNICODE_F5, KeyEvent.VK_F5
	        }, {
	                KeyboardAction.UNICODE_CTRL, KeyEvent.VK_CONTROL
	        }, {
	                KeyboardAction.UNICODE_F, KeyEvent.VK_F
	        }, {
	                KeyboardAction.UNICODE_SPACE, KeyEvent.VK_SPACE
	        }, {
	                KeyboardAction.UNICODE_S, KeyEvent.VK_S
	        }, {
	                KeyboardAction.UNICODE_M, KeyEvent.VK_M
	        }, {
	                KeyboardAction.UNICODE_N, KeyEvent.VK_N
	        }, {
	                KeyboardAction.UNICODE_P, KeyEvent.VK_P
	        }, {
	                KeyboardAction.UNICODE_L, KeyEvent.VK_L
	        }, {
	                KeyboardAction.UNICODE_R, KeyEvent.VK_R
	        }, {
	                KeyboardAction.UNICODE_Z, KeyEvent.VK_Z
	        }
	};
	
	private static final int[][] COMBINATION = {
	        {
	                Combination.UNICODE_CUT, KeyEvent.VK_CONTROL, KeyEvent.VK_X
	        }, {
	                Combination.UNICODE_COPY, KeyEvent.VK_CONTROL, KeyEvent.VK_C
	        }, {
	                Combination.UNICODE_PASTE, KeyEvent.VK_CONTROL, KeyEvent.VK_V
	        }, {
	                Combination.UNICODE_UNDO, KeyEvent.VK_CONTROL, KeyEvent.VK_Z
	        }, {
	                Combination.UNICODE_FIND, KeyEvent.VK_CONTROL, KeyEvent.VK_F
	        }, {
	                Combination.UNICODE_ALL, KeyEvent.VK_CONTROL, KeyEvent.VK_A
	        }, {
	                Combination.B_HIS, KeyEvent.VK_CONTROL, KeyEvent.VK_H
	        }, {
	                Combination.B_DL, KeyEvent.VK_CONTROL, KeyEvent.VK_J
	        }, {
	                Combination.B_TABC, KeyEvent.VK_CONTROL, KeyEvent.VK_TAB
	        }, {
	                Combination.B_TAB, KeyEvent.VK_CONTROL, KeyEvent.VK_T
	        }, {
	                Combination.B_BOOKM, KeyEvent.VK_CONTROL, KeyEvent.VK_D
	        }, {
	                Combination.B_SAVE, KeyEvent.VK_CONTROL, KeyEvent.VK_S
	        }, {
	                Combination.B_PRINT, KeyEvent.VK_CONTROL, KeyEvent.VK_P
	        }, {
	                Combination.EXIT, KeyEvent.VK_CONTROL, KeyEvent.VK_F4
	        }, {
	                Combination.B_LASER, KeyEvent.VK_CONTROL, KeyEvent.VK_L
	        }
	};
	
	private static final int[][] COMBINATIONS = {
		{
		        Combinations.UNICODE_TASK, KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_DELETE
		}
	};
	
	private SteerServerApp application;
	
	private SteerConnection connection;
	
	private boolean authentificated;
	
	private boolean useUnicodeWindowsAltTrick;
	
	public SteerServerConnection(SteerServerApp application, SteerConnection connection)
	{
		this.application = application;
		this.connection = connection;
		this.authentificated = false;
		
		this.useUnicodeWindowsAltTrick = SteerServerApp.IS_WINDOWS && !this.application.getPreferences().getBoolean("force_disable_unicode_windows_alt_trick", false);
		System.out.println("Connection Established :)");
		(new Thread(this)).start();
	}

	public static void pressUnicode(java.awt.Robot r, int key_code) {
		r.keyPress(KeyEvent.VK_ALT);
		boolean useHexKeypad = false;
		if (!useHexKeypad) {
			String s = String.format("%04d", key_code);
			// System.out.println("Now pressing key of dec unicode: " + s);
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				int keycode = c - '0' + KeyEvent.VK_NUMPAD0;

				r.keyPress(keycode);
				r.keyRelease(keycode);
			}
		} else {
			String s = Integer.toHexString(key_code);
			// System.out.println("Now pressing key of hex unicode: " + s);
			r.keyPress(0x6b);
			r.keyRelease(0x6b);
			for (int i = 0; i < s.length(); i++) {
				int keycode = UnicodeToSwingKeyCodeConverter.convert(s.charAt(i));

				r.keyPress(keycode);
				r.keyRelease(keycode);
			}
		}
		r.keyRelease(KeyEvent.VK_ALT);
	}
	
	public void run()
	{
		try
		{
			try
			{
				while (true)
				{
					SteerAction action = this.connection.receiveAction();

					this.action(action);
				}
			}
			catch (IOException e)
			{
				// Happens when the client disconnects
				this.application.getTrayIcon().notifyConnection(this.connection);
			}
			finally
			{
				this.connection.close();
			}
		}
		catch (ProtocolException e)
		{
			e.printStackTrace();

			this.application.getTrayIcon().notifyProtocolProblem();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void action(SteerAction action)
	{
		if (this.authentificated)
		{
			if (action instanceof MouseMoveAction)
			{
				this.moveMouse((MouseMoveAction) action);
			}
			else if (action instanceof MouseClickAction)
			{
				this.mouseClick((MouseClickAction) action);
			}
			else if (action instanceof MouseWheelAction)
			{
				this.mouseWheel((MouseWheelAction) action);
			}
			else if (action instanceof ScreenCaptureRequestAction)
			{
				this.screenCapture((ScreenCaptureRequestAction) action);
			}
			else if (action instanceof FileExploreRequestAction)
			{
				this.fileExplore((FileExploreRequestAction) action);
			}
			else if (action instanceof KeyboardAction)
			{
				this.keyboard((KeyboardAction) action);
			}
			else if (action instanceof Combination)
			{
				this.combination((Combination) action);
			}
			else if (action instanceof Combinations)
			{
				this.combinations((Combinations) action);
			}
		}
		else
		{
			if (action instanceof AuthentificationAction)
			{
				this.authentificate((AuthentificationAction) action);
			}

			if (!this.authentificated)
			{
				try
				{
					this.connection.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private void authentificate(AuthentificationAction action)
	{
		if (action.password.equals(this.application.getPreferences().get("password", SteerConnection.DEFAULT_PASSWORD)))
		{
			this.authentificated = true;

			this.application.getTrayIcon().notifyConnection(this.connection);
		}

		this.sendAction(new AuthentificationResponseAction(this.authentificated));
	}
	
	private void moveMouse(MouseMoveAction action)
	{
		PointerInfo pointerInfo = MouseInfo.getPointerInfo();

		if (pointerInfo != null)
		{
			Point mouseLocation = pointerInfo.getLocation();

			if (mouseLocation != null)
			{
				int x = mouseLocation.x + action.moveX;
				int y = mouseLocation.y + action.moveY;
				this.application.getRobot().mouseMove(x, y);
			}
		}
	}
	
	private void mouseClick(MouseClickAction action)
	{
		int button;

		switch (action.button)
		{
			case MouseClickAction.BUTTON_LEFT:
				button = InputEvent.BUTTON1_MASK;
				break;
			case MouseClickAction.BUTTON_RIGHT:
				button = InputEvent.BUTTON3_MASK;
				break;
			case MouseClickAction.BUTTON_MIDDLE:
				button = InputEvent.BUTTON2_MASK;
				break;
			default:
				return;
		}

		if (action.state == MouseClickAction.STATE_DOWN)
		{
			this.application.getRobot().mousePress(button);
		}
		else if (action.state == MouseClickAction.STATE_UP)
		{
			this.application.getRobot().mouseRelease(button);
		}

	}
	
	private void mouseWheel(MouseWheelAction action)
	{
		this.application.getRobot().mouseWheel(action.amount);
	}
	
	private void screenCapture(ScreenCaptureRequestAction action)
	{
		try
		{
			Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
			//Rectangle r = new Rectangle(mouseLocation.x - (action.width / 2), mouseLocation.y - (action.height / 2), action.width, action.height);
			Rectangle r = new Rectangle(mouseLocation.x - (action.width / 2), mouseLocation.y - (action.height / 2), action.width, action.height);
			BufferedImage capture = new BufferedImage(1, 1, 12);
			if (r.width + r.height > 0)
				capture = this.application.getRobot().createScreenCapture(r);
			String format = null;
			if (action.format == ScreenCaptureRequestAction.FORMAT_PNG)
			{
				format = "png";
			}
			else if (action.format == ScreenCaptureRequestAction.FORMAT_JPG)
			{
				format = "jpg";
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(capture, format, baos);
			byte[] data = baos.toByteArray();

			this.sendAction(new ScreenCaptureResponseAction(data));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void fileExplore(FileExploreRequestAction action)
	{
		if (action.directory.isEmpty() && action.file.isEmpty())
		{
			this.fileExploreRoots();
		}
		else
		{
			if (action.directory.isEmpty())
			{
				this.fileExplore(new File(action.file));
			}
			else
			{
				File directory = new File(action.directory);

				if (directory.getParent() == null && action.file.equals(".."))
				{
					this.fileExploreRoots();
				}
				else
				{
					try
					{
						this.fileExplore(new File(directory, action.file).getCanonicalFile());
					}
					catch (IOException e)
					{
						e.printStackTrace();

						this.fileExploreRoots();
					}
				}
			}
		}
	}
	
	private void fileExplore(File file)
	{
		if (file.exists() && file.canRead())
		{
			if (file.isDirectory())
			{
				this.sendFileExploreResponse(file.getAbsolutePath(), file.listFiles(), true);
			}
			else
			{
				if (Desktop.isDesktopSupported())
				{
					Desktop desktop = Desktop.getDesktop();

					if (desktop.isSupported(Desktop.Action.OPEN))
					{
						try
						{
							System.out.print("Trying to open [" + file + "]...");
							desktop.open(file);
							System.out.println(" OK.");
						}
						catch (IOException e)
						{
							// System.out.println("Failed that.");
							// e.printStackTrace();

							if (SteerServerApp.IS_WINDOWS)
							{
								System.out.println(" Failed. Using Windows Shell:");

								try
								{
									Process process = Runtime.getRuntime().exec("cmd /C \"" + file.getAbsolutePath() + "\"");
									BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

									String line;
									while ((line = br.readLine()) != null)
									{
										System.out.println(line);
									}
								}
								catch (IOException e1)
								{
									System.out.println("Sorry guys, I tried, but it just didn't work out!");
									e1.printStackTrace();
								}
							}
						}

					}
				}
			}
		}
		else
		{
			this.fileExploreRoots();
		}
	}
	
	private void fileExploreRoots()
	{
		String directory = "";

		File[] files = File.listRoots();

		this.sendFileExploreResponse(directory, files, false);
	}
	
	private void sendFileExploreResponse(String directory, File[] f, boolean parent)
	{
		if (f != null)
		{
			ArrayList<String> list = new ArrayList<String>();

			if (parent)
			{
				list.add("..");
			}

			for (int i = 0; i < f.length; i++)
			{
				String name = f[i].getName();

				if (!name.isEmpty())
				{
					if (f[i].isDirectory())
					{
						name += File.separator;
					}
				}
				else
				{
					name = f[i].getAbsolutePath();
				}

				list.add(name);
			}

			String[] files = new String[list.size()];

			files = list.toArray(files);

			this.sendAction(new FileExploreResponseAction(directory, files));
		}
	}
	
	private void keyboard(KeyboardAction action)
	{
		if (this.useUnicodeWindowsAltTrick)
		{
			this.keyboardUnicodeWindowsAltTrick(action);
		}
		else
		{
			this.keyboardClassic(action);
		}
	}
	
	private void combination(Combination action)
	{
		if (this.useUnicodeWindowsAltTrick)
		{
			this.keyboardCombination(action);
		}
	}
	
	private void combinations(Combinations action)
	{
		if (this.useUnicodeWindowsAltTrick)
		{
			this.keyboardCombinations(action);
		}
	}
	
	private void keyboardCombination(Combination action)
	{
		boolean exception = false;

		for (int i = 0; i < COMBINATION.length; i++)
		{
			if (action.unicode == COMBINATION[i][0])
			{
				exception = true;

				this.application.getRobot().keyPress(COMBINATION[i][1]);
				this.application.getRobot().keyPress(COMBINATION[i][2]);
				// this.application.getRobot().keyPress(COMBINATION[i][3]);
				this.application.getRobot().keyRelease(COMBINATION[i][1]);
				this.application.getRobot().keyRelease(COMBINATION[i][2]);
				// this.application.getRobot().keyRelease(COMBINATION[i][3]);
				break;
			}
		}
		if (!exception)
		{
			pressUnicode(this.application.getRobot(), action.unicode);
		}
	}
	
	private void keyboardCombinations(Combinations action)
	{
		boolean exception = false;

		for (int i = 0; i < COMBINATIONS.length; i++)
		{
			if (action.unicode == COMBINATIONS[i][0])
			{
				exception = true;

				this.application.getRobot().keyPress(COMBINATIONS[i][1]);
				this.application.getRobot().keyPress(COMBINATIONS[i][2]);
				this.application.getRobot().keyPress(COMBINATIONS[i][3]);
				this.application.getRobot().keyRelease(COMBINATIONS[i][1]);
				this.application.getRobot().keyRelease(COMBINATIONS[i][2]);
				this.application.getRobot().keyRelease(COMBINATIONS[i][3]);
				break;
			}
		}
		if (!exception)
		{
			pressUnicode(this.application.getRobot(), action.unicode);
		}
	}
	
	private void keyboardUnicodeWindowsAltTrick(KeyboardAction action)
	{
		boolean exception = false;

		for (int i = 0; i < UNICODE_EXCEPTION.length; i++)
		{
			if (action.unicode == UNICODE_EXCEPTION[i][0])
			{
				exception = true;

				this.application.getRobot().keyPress(UNICODE_EXCEPTION[i][1]);
				this.application.getRobot().keyRelease(UNICODE_EXCEPTION[i][1]);

				break;
			}
		}
		if (!exception)
		{
			pressUnicode(this.application.getRobot(), action.unicode);
		}
	}
	
	private void keyboardClassic(KeyboardAction action)
	{
		int keycode = UnicodeToSwingKeyCodeConverter.convert(action.unicode);
		if (keycode != UnicodeToSwingKeyCodeConverter.NO_SWING_KEYCODE)
		{
			// System.out.println("Now pressing key of code: " + keycode);

			boolean useShift = UnicodeToSwingKeyCodeConverter.useShift(action.unicode);

			if (useShift)
			{
				this.application.getRobot().keyPress(KeyEvent.VK_SHIFT);
			}

			this.application.getRobot().keyPress(keycode);
			this.application.getRobot().keyRelease(keycode);

			if (useShift)
			{
				this.application.getRobot().keyRelease(KeyEvent.VK_SHIFT);
			}
		}
		else
		{
			// Not normal character, use Unicode
			pressUnicode(this.application.getRobot(), action.unicode);
		}
	}
	
	private void sendAction(SteerAction action)
	{
		try
		{
			this.connection.sendAction(action);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
