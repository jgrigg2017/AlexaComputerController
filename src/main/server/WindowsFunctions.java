package main.server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

/** 
 * WindowsFunctions is a class containing basic Windows commands.
 * Functions were tested only on Windows 10.
 * It uses the Robot class to activate keyboard shortcuts and Runtime
 * for command-line interface commands.
*/

public class WindowsFunctions {
	
	static public void shutdown() throws IOException {
		String cmd = "shutdown /s";
		Runtime.getRuntime().exec(cmd);
	}
	
	static public void abortShutdown() throws IOException {
		String cmd = "shutdown /a";
		Runtime.getRuntime().exec(cmd);
	}
	
	/* Restarts computer and reopens currently open programs after reboot.
	 * To restart without reopening programs use argument /r instead of /g.
	 */
	static public void restart() throws IOException {
		String cmd = "shutdown /g";
		Runtime.getRuntime().exec(cmd);
	}
	
	static public void logOff() throws IOException {
		String cmd = "shutdown /l";
		Runtime.getRuntime().exec(cmd);
	}
	
	// Lock Windows (Go back to login page).
	static public void lock() throws IOException {
		String cmd = "rundll32 user32.dll,LockWorkStation";
		Runtime.getRuntime().exec(cmd);
	}
	
	static public void hibernate() throws IOException {
		String cmd = "shutdown /h";
		Runtime.getRuntime().exec(cmd);
	}
	
	// Minimize all windows except for the currently active window.
	static public void winHome() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		bot.keyPress(KeyEvent.VK_WINDOWS);
		bot.keyPress(KeyEvent.VK_HOME);
		bot.keyRelease(KeyEvent.VK_WINDOWS);
		bot.keyRelease(KeyEvent.VK_HOME);
	}
	
	// Minimize all windows.
	static public void minimizeAll() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		bot.keyPress(KeyEvent.VK_WINDOWS);
		bot.keyPress(KeyEvent.VK_M);
		bot.keyRelease(KeyEvent.VK_WINDOWS);
		bot.keyRelease(KeyEvent.VK_M);
	}
	
	// Switch to the last active window.
	static public void lastActive() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		bot.keyPress(KeyEvent.VK_ALT);
		bot.keyPress(KeyEvent.VK_TAB);
		bot.keyRelease(KeyEvent.VK_ALT);
		bot.keyRelease(KeyEvent.VK_TAB);
	}
	
	/**
	 * @Deprecated No longer needed since {@link #shutdownKB()} and {@link #restartKB()}
	 * are deprecated.
	 * Open Windows+X menu (aka Power User Menu).
	 */
	@Deprecated
	static public void winXmenu() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		bot.keyPress(KeyEvent.VK_WINDOWS);
		bot.keyPress(KeyEvent.VK_X);
		bot.keyRelease(KeyEvent.VK_WINDOWS);
		bot.keyRelease(KeyEvent.VK_X);
		Thread.sleep(1000);
	}
	
	/**
	 * @Deprecated Replaced by {@link #shutdown()}
	 */
	@Deprecated
	static public void shutdownKB() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		winXmenu();
		bot.keyPress(KeyEvent.VK_U);
		bot.keyRelease(KeyEvent.VK_U);
		bot.keyPress(KeyEvent.VK_U);
		bot.keyRelease(KeyEvent.VK_U);
	}
	
	/**
	 * @deprecated Replaced by {@link #restart()}
	 */
	@Deprecated
	static public void restartKB() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		winXmenu();
		bot.keyPress(KeyEvent.VK_U);
		bot.keyRelease(KeyEvent.VK_U);
		bot.keyPress(KeyEvent.VK_R);
		bot.keyRelease(KeyEvent.VK_R);
	}
	
}