package socket.proxy.cloud;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import socket.proxy.cloud.types.CloudDrive;

public class CloudDriveSocket extends Socket {
	CloudDriveInputStream cloudDriveInputStream;
	CloudDriveOutputStream cloudOutputSteam;
	CloudDrive cloudDrive;
	String uid;
	static String SERVER="SERVER";
	static String CLIENT="CLIENT";
	
	public CloudDriveSocket(CloudDrive cloudDrive,String uid,String type) {
		this.cloudDrive=cloudDrive;
		this.uid=uid;
		try {
			this.cloudDrive.init(uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(type.equalsIgnoreCase(SERVER))
		{
			cloudDriveInputStream=new CloudDriveInputStream(cloudDrive,uid+"0");
			cloudOutputSteam=new CloudDriveOutputStream(cloudDrive,uid+"1");
		} 
		else if(type.equalsIgnoreCase(CLIENT))
		{
			cloudDriveInputStream=new CloudDriveInputStream(cloudDrive,uid+"1");
			cloudOutputSteam=new CloudDriveOutputStream(cloudDrive,uid+"0");
		} 
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return cloudDriveInputStream;
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return cloudOutputSteam;
	}
	
	@Override
	public synchronized void close() throws IOException {
		super.close();
	}
}
