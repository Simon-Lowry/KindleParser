package com.kindleparser.parser.services;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.kindleparser.parser.servicesInterfaces.IHLFileCopier;


@Service
public class HLFileCopierService implements IHLFileCopier {
	private static Logger log = LogManager.getLogger(HLFileCopierService.class.getName());
	
	@Autowired
	Environment env;
	
	/**
	 * <p>Checks to see if my kindle is connected to the local device based on certain files contained on the kindle</p>
	 * 
	 * @return true if the kindle is confirmed to be connected.
	 */
	public boolean isKindleConnected() {
		File kindleFile1 = new File(env.getProperty("kindle.authentication1"));
		File kindleFile2 = new File(env.getProperty("kindle.authentication2"));
		File kindleFile3 = new File(env.getProperty("kindle.authentication3"));
		File kindleFile4 = new File(env.getProperty("kindle.binaryLocation"));
		
		while (true) {
			
			log.info("Checking to see if kindle is connected");
			if (kindleFile1.exists() && kindleFile2.exists() && kindleFile3.exists() && kindleFile4.exists() ) {
				
				log.info("Kindle is connected.");
				return true;
			} else {
				log.info("Kindle is not connected.");
			}
			
			try {
			Thread.sleep(1000);
			} catch (InterruptedException ex) {
				log.error("Service has been interrupted!\n" + ex);
				return false;
			}
		}
	}
	
	
	/**
	 * <p>
	 * Attempts to copy the highlight file from the kindle to local storage.
	 * </p>
	 * 
	 * @return - true - file has been copied to local storage
	 */
	public boolean copyHLFile(int numFiles) {
		File currentHLFile = new File(env.getProperty("kindle.HLFileLocation"));
		String newHLFileName = "ClippingsX" + (numFiles + 1);
		File copiedHLFile = new File(env.getProperty("local.HLFileLocation") + newHLFileName + ".txt");
		
		try {
			log.info("Attempting to copy highlights file and create copied file");
			FileUtils.copyFile(currentHLFile, copiedHLFile);
			copiedHLFile.setReadOnly(); 		// need to check if this works.
			log.info("Copied highlight file from kindle to project folder for parsing and storage.");
		} catch(IOException ex) {
			log.error("Unable to copy file: " + ex);
		}
		return true;
	}
	
	
	/**
	 * <p>
	 * HLFileDirectory contains the kindle highlight file as well up to four of the most recent versions
	 * of the file as backup. When a fifth file is added to the directory, the oldest backup will be deleted
	 * </p>
	 * @return - true - directory has been successfully updated
	 */
	public boolean updateHLFileDirectory()  {
		String directoryPath = env.getProperty("local.HLFileLocation");
		File HLFileDirectory = new File(directoryPath);
		File[] filesInDirectory = HLFileDirectory.listFiles();
		int numFilesInDir = filesInDirectory.length - 1;
		
		if (filesInDirectory.length > 4) {
			// check to see which is the oldest file
			Arrays.sort(filesInDirectory, Comparator.comparingLong(File::lastModified).reversed());
			
			// delete the oldest file
			filesInDirectory[numFilesInDir].delete();
			log.info("Deleted file: " + filesInDirectory[numFilesInDir].getName() + " to make space for new HL file");
		}
		
		// add new file here
		copyHLFile(numFilesInDir);
		return true;
	}

}
