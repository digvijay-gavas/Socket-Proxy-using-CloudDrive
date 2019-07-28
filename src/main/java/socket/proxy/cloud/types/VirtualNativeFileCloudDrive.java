package socket.proxy.cloud.types;

import java.io.FileNotFoundException;




public class VirtualNativeFileCloudDrive implements CloudDrive{
    

	@Override
	public void init(String APPLICATION_NAME) throws Exception {	
	}

	@Override
	public void uploadFile(String uploadFileName, byte[] inputBytes) throws Exception {
		uploadFile(uploadFileName, inputBytes,0,inputBytes.length);
	}

	@Override
	public void uploadFile(String uploadFileName, byte[] inputBytes, int offset, int length) throws Exception {

	}


	@Override
	public byte[] downloadFile(String downloadFileName) throws FileNotFoundException,Exception {
		return  null;
	}

	@Override
	public void deleteFile(String deleteFileName) throws Exception {
		
	}

	@Override
	public boolean isFileExist(String deleteFileName) throws Exception {
		return false;
	}
}
