package org.usfirst.frc.team1517.io;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class USBLogger {

	BufferedWriter log;
	
	public USBLogger(String logFilePath) {
		File logfile = new File(logFilePath);
		// If log file doesn't exist, try to create it.
		if (!logfile.exists()) {	
			try {
				logfile.createNewFile();
				// initialize buffered writer object
				log = new BufferedWriter(new FileWriter(logfile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void push(String message) {
		String buffer = new Date().toString() + message + "\n";
		try {
			log.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
