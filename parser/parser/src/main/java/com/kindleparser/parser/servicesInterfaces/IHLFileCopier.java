package com.kindleparser.parser.servicesInterfaces;


/**
 * <p>
 * 	A windows service which periodically checks to see if my kindle device is connected. 
 *  It performs authentication to ensure it is my kindle device connected and then
 *  it will copy the highlight file from the kindle onto my local machine for later parsing.
 * </p>
 * 
 */
public interface IHLFileCopier {
	
	/**
	 * <p>Checks to see if my kindle is connected to the laptop</p>
	 * @return true if connected, false otherwise
	 */
	public boolean isKindleConnected();

	/**
	 * 
	 * @param numFiles - the number of files currently in the Highlight file directory
	 * @return true if the file was successfully copied from the kindle
	 */
	public boolean copyHLFile(int numFiles);
	
	/**
	 * 
	 * @return
	 */
	public boolean updateHLFileDirectory();
	
}
