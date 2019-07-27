package socket.proxy.cloud.types;

import java.io.File;
import java.io.InputStream;

public interface CloudDrive {
	
	public void init(String APPLICATION_NAME) throws Exception;
	
	public void uploadFile(String uploadFileName,byte[] inputBytes)  throws Exception;
	
	public void uploadFile(String uploadFileName,byte[] inputBytes, int offset, int length)  throws Exception;
	 
	public byte[] downloadFile(String downloadFileName)  throws Exception;
}
