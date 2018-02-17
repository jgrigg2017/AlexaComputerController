package main.server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;


/* TODO: Consider creating just one Robot in Server.java to be used and passing reference to it 
 * as an argument in these command functions rather than creating a new Robot during each command execution.
 * 
*/
public class VLCCommands {
	
	static public void playPause() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		Thread.sleep(2000);
		bot.keyPress(KeyEvent.VK_SPACE);
		bot.keyRelease(KeyEvent.VK_SPACE);
	}
	
	static public void fullscreen() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		Thread.sleep(2000);
		bot.keyPress(KeyEvent.VK_F);
		bot.keyRelease(KeyEvent.VK_F);
	}
	
	static public void stop() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		Thread.sleep(2000);
		bot.keyPress(KeyEvent.VK_S);
		bot.keyRelease(KeyEvent.VK_S);
	}
	
	static public void next() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		Thread.sleep(2000);
		bot.keyPress(KeyEvent.VK_N);
		bot.keyRelease(KeyEvent.VK_N);
	}
	
	static public void previous() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		Thread.sleep(2000);
		bot.keyPress(KeyEvent.VK_P);
		bot.keyRelease(KeyEvent.VK_P);
	}
	
	static public void volumeUp(Integer amount) throws AWTException, InterruptedException {
		Robot bot = new Robot();
		Thread.sleep(2000);
		for (int i = 0 ; i < amount && i <= 200 ; i+=5) {
			bot.keyPress(KeyEvent.VK_CONTROL);
			bot.keyPress(KeyEvent.VK_UP);
			bot.keyRelease(KeyEvent.VK_UP);
			bot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(100);
		}
	}
	
	static public void volumeUp() throws AWTException, InterruptedException {
		volumeUp(5);
	}
	
	static public void volumeDown(Integer amount) throws AWTException, InterruptedException {
		Robot bot = new Robot();
		Thread.sleep(2000);
		for (int i = 0 ; i < amount && i <= 200 ; i+=5) {
			bot.keyPress(KeyEvent.VK_CONTROL);
			bot.keyPress(KeyEvent.VK_DOWN);
			bot.keyRelease(KeyEvent.VK_DOWN);
			bot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(100);
		}
	}
	
	static public void volumeDown() throws AWTException, InterruptedException {
		volumeDown(5);
	}
	
	static public void changeAspectRatio() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		Thread.sleep(2000);
		bot.keyPress(KeyEvent.VK_A);
		bot.keyRelease(KeyEvent.VK_A);
	}
	
	static public void changeZoom() throws AWTException, InterruptedException {
		Robot bot = new Robot();
		Thread.sleep(2000);
		bot.keyPress(KeyEvent.VK_Z);
		bot.keyRelease(KeyEvent.VK_Z);
	}

}
