package socket.proxy.cloud;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;

import socket.proxy.cloud.types.CloudDrive;

public class CloudDriveOutputStream extends OutputStream {
	int buffer_size=32768;
	byte[] buffer = new byte[buffer_size];
	int buffer_pointer=0;
	CloudDrive cloudDrive;
	String uid;
	
	public CloudDriveOutputStream(CloudDrive cloudDrive,String uid) {
		this.cloudDrive=cloudDrive;
		this.uid=uid;
	}
	
	@Override
	public void write(int b) throws IOException {
		if (buffer_pointer>=buffer_size)
			flush();
		buffer[buffer_pointer]=(byte)b;
		buffer_pointer++;
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		//System.out.write(b, off, len);
		for (int i = off; i < len; i++) {
			write(b[i]);
		}
	}
	
	@Override
	public void flush() throws IOException {
	
		try {
			while (true)
				if(!cloudDrive.isFileExist(uid))
				{
						cloudDrive.uploadFile(uid, buffer,0,buffer_pointer);
						break;
				}
				else
				{
					Thread.sleep(1000);
				}
		} catch (Exception e) {
			throw new IOException("Unable to send data");
		}
		buffer_pointer=0;
		
		//System.out.println("Flushed ");
	}

}
