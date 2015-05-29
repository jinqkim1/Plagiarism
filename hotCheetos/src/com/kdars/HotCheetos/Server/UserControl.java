package com.kdars.HotCheetos.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class UserControl {
	private Socket	socket;
	private BufferedReader input;
	private BufferedWriter output;
	
	public void run() {
		while (true) {
			String commandFromUser = commandFromUser();
			if (commandFromUser == null) {
				break;
			}
			//lastHeartBeat = System.currentTimeMillis();
			//commandParser(commandFromUser);

		}
	}
	
	private String commandFromUser() {
		String command = " ";
		try {
			command = input.readLine();
    //} catch (NullPointerException | IOException e) {
		} catch (Exception e) {
			// bValidConnection = false;
		}
		return command;
	}
}
