package socket.proxy.cloud.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;




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
		FileOutputStream fileOutputStream=new FileOutputStream(new File(uploadFileName));
		fileOutputStream.write(inputBytes,offset,length);
		fileOutputStream.flush();
		fileOutputStream.close();
	}


	@Override
	public byte[] downloadFile(String downloadFileName) throws FileNotFoundException,Exception {
		FileInputStream fileInputStream=new FileInputStream(new File(downloadFileName));
		byte[] bs=fileInputStream.readAllBytes();
		fileInputStream.close();
		return bs;
	}

	@Override
	public void deleteFile(String deleteFileName) throws Exception {
		new File(deleteFileName).delete();
	}

	@Override
	public boolean isFileExist(String deleteFileName) throws Exception {
		return new File(deleteFileName).exists();
	}
}
