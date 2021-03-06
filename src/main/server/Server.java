package main.server;


import java.util.*;
import java.awt.AWTException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import main.server.VLCCommands;
import main.server.WindowsFunctions;

/**
 * This is a class for a basic HTTP server that listens for command requests.
 * Command requests are sent through the query string of a URL.
 * 
 */
public class Server {
	
	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLocalHost(), 8082), 0);
		System.out.println(server.getAddress());
		server.createContext("/ComputerController/", new MyHandler());
		server.setExecutor(null);
		server.start();
	}
	
	static class MyHandler implements HttpHandler {

		public void handle(HttpExchange exchange) throws IOException {
			byte [] response = "Alexa Computer Controller".getBytes();
			String queryString = exchange.getRequestURI().getQuery();
			
			if (queryString == null) { queryString = ""; }
			System.out.println(queryString);

			// Parse the Query String into a list of commands.
			// Make an ArrayList of the [parameter, value] pairs from the query string.
			String[] paramValuePairs = queryString.split("&");
			ArrayList<String[]> paramValueList = new ArrayList<String[]>();
			for (String arg : paramValuePairs) {
				String[] paramValuePair = arg.split("=");
				if (paramValuePair.length > 1 && !paramValuePair[1].equals("null")) {
					// Add [param, value] to the Array List.
					paramValueList.add(paramValuePair);
				} else if (paramValuePair.length >= 1){
					// Add [param, null] to the Array List.
					String[] pair = new String[2];
					pair[0] = paramValuePair[0];
					paramValueList.add(pair);
				}
			}
			
			for (String[] pair : paramValueList) {
				try {
					executeCommands(pair[0], pair[1]);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}

			
			exchange.sendResponseHeaders(200, response.length);
			OutputStream os = exchange.getResponseBody();
			os.write(response);
			os.close();
		}
	}

	/*
	 * executeCommands() links each query string to the corresponding command function.
	 */
	static public void executeCommands(String param, String value) throws InterruptedException, AWTException, IOException {
		
		switch (param) {
			case "VLCPause":
				VLCCommands.playPause();
				break;
			case "VLCPlay":
				VLCCommands.playPause();
				break;
			case "VLCVolumeUp":
				if (value == null) {
					VLCCommands.volumeUp();
				} else if (value != "?"){
					try {
						Integer amount = Integer.parseInt(value);
						VLCCommands.volumeUp(amount);
					} catch (java.lang.NumberFormatException e) {
						// NumberFormatException shouldn't be possible through Alexa because AMAZON.NUMBER only
						// accepts numbers. However, it is possible to receive the error if the query string is typed in 
						// manually (e.g. if typed into a browser's address bar). In this case, we can just ignore the request.
						break;
					}
				}
				break;
			case "VLCVolumeDown":
				if (value == null) {
					VLCCommands.volumeDown();
				} else if (value != "?"){
					try {
						Integer amount = Integer.parseInt(value);
						VLCCommands.volumeDown(amount);
					} catch (java.lang.NumberFormatException e) {
						break;
					}
				}
				break;
			case "VLCNext":
				VLCCommands.next();
				break;
			case "VLCPrevious":
				VLCCommands.previous();
				break;
			case "VLCStop":
				VLCCommands.stop();
				break;
			case "VLCFullscreen":
				VLCCommands.fullscreen();
				break;
			case "VLCAspectRatio":
				VLCCommands.changeAspectRatio();
				break;
			case "VLCZoom":
				VLCCommands.changeZoom();
				break;
			case "WinShutdown":
				WindowsFunctions.shutdown();
				break;
			case "WinRestart":
				WindowsFunctions.restart();
				break;
			case "WinShutdownAbort":
				WindowsFunctions.abortShutdown();
				break;
			case "WinLogOff":
				WindowsFunctions.logOff();
				break;
			case "WinLock":
				WindowsFunctions.lock();
				break;
			case "WinHibernate":
				WindowsFunctions.hibernate();
				break;
			case "WinMinimizeAll":
				WindowsFunctions.minimizeAll();
				break;
			case "WinMinimizeMost":
				WindowsFunctions.winHome();
				break;
				
			default:
				break;
		}
	}
	
	// Overloaded executeCommands to accept just param argument with no value.
	static public void executeCommands(String param) throws InterruptedException, AWTException, IOException {
		executeCommands(param, null);
	}


}