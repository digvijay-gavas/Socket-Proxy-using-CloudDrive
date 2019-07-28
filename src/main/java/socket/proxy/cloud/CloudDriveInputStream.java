package socket.proxy.cloud;

import java.io.IOException;
import java.io.InputStream;

import socket.proxy.cloud.types.CloudDrive;

public class CloudDriveInputStream extends InputStream {
	
	int buffer_size=0;
	byte[] buffer = new byte[buffer_size];
	int buffer_pointer=buffer_size-1;
	
	int available=0;
	CloudDrive cloudDrive;
	String uid;
	
	public CloudDriveInputStream(CloudDrive cloudDrive,String uid) {
		this.cloudDrive=cloudDrive;
		this.uid=uid;
	}
	
	private void load() throws Exception {
		//boolean isFileExistInDrive=false;
		if(buffer_pointer>=(buffer_size-1))
			while(true)
				if(cloudDrive.isFileExist(uid))
				{
						buffer=cloudDrive.downloadFile(uid);
						cloudDrive.deleteFile(uid);
						//isFileExistInDrive=true;
						//System.out.println(buffer);
						buffer_size=buffer.length;
						buffer_pointer=0;
						available=buffer_size-buffer_pointer;
						break;
				} 
				else 
				{
					Thread.sleep(1000);
				}
				
	}
	
	@Override
	public int read() throws IOException {
		try {
			load();
		} catch (Exception e1) {
			throw new IOException("Unable to read data");
		}
		while(available<=0)
			try {
				load();
			} catch (Exception e1) {
				throw new IOException("Unable to read data");
			}
			
		buffer_pointer++;
		available--;
		return buffer[buffer_pointer-1];
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		for (int i = off; i < len; i++) {
			if(available<=0)
				return -1;
			b[i]=buffer[buffer_pointer];
			buffer_pointer++;
			available--;
		}
		return len;
	}
	@Override
	public int available() throws IOException {
		return available;
	}

}
