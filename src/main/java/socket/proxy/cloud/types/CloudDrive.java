package socket.proxy.cloud.types;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;

public interface CloudDrive {
	
	public void init(String APPLICATION_NAME) throws Exception;
	/*
	 * should upload file to respective drive with inputBytes. It should wait until file exist in drive
	 */
	public void uploadFile(String uploadFileName,byte[] inputBytes)  throws FileAlreadyExistsException,Exception;
	
	public void uploadFile(String uploadFileName,byte[] inputBytes, int offset, int length)  throws FileAlreadyExistsException,Exception;
	 
	public byte[] downloadFile(String downloadFileName)  throws FileNotFoundException,Exception;
	
	public void deleteFile(String deleteFileName) throws Exception;
	
	public boolean isFileExist(String deleteFileName) throws Exception;
}
