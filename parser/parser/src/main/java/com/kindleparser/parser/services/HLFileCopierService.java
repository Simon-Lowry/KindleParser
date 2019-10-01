package com.kindleparser.parser.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;

import javax.xml.bind.DatatypeConverter;

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
	 * <p>
	 * Checks to see if my kindle is connected to the local device and 
	 * performs authentication to ensure it is my kindle connected.
	 * </p>
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
				
				// Authenticate the device using md5 checksum
				if (isMyKindleDevice()) {
					log.info("Device has been authenticated as correct kindle device");
					return true;
				}
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
	 * Authenticate the device is my kindle by checking the md5 of the authentication file
	 * against the expected md5.
	 * 
	 * @return true if it is the same md5 as expected.
	 */
	private boolean isMyKindleDevice() {
		String hexOfFileHash = "";
		
		try {
			
			byte[] b = Files.readAllBytes(Paths.get(env.getProperty("kindle.binaryLocation")));
			byte[] hash = MessageDigest.getInstance("MD5").digest(b);
			
			log.info("Attempting to authenticate device as my kindle device");
			hexOfFileHash = DatatypeConverter.printHexBinary(hash);
			
		} catch (Exception ex) {
			System.out.println("Exception occurred: " + ex.getMessage());
		}
		
		return hexOfFileHash.equals(env.getProperty("kindle.authFileMD5Value"));	// check md5 of file vs md5 in properties file
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
			copiedHLFile.setReadOnly();
			
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
		
		copyHLFile(numFilesInDir);
		return true;
	}

}
