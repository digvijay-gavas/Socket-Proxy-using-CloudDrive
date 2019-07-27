package socket.proxy.cloud;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import socket.proxy.cloud.types.GoogleCloudDrive;

public class Test {
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket=new ServerSocket(8223);
		Socket socket=serverSocket.accept();
		
		CloudDriveSocket cloudSocket=new CloudDriveSocket(new GoogleCloudDrive(),"TestSocketUID",CloudDriveSocket.SERVER);
		SocketBindThread.bind(socket,cloudSocket,true);
		
		
		
		/*Socket socket=new Socket("localhost",5355);
		
		CloudDriveSocket cloudSocket=new CloudDriveSocket(new GoogleCloudDrive(),"socket",CloudDriveSocket.CLIENT);
		SocketBindThread.bind(socket,cloudSocket,true);*/
		
		//GoogleCloudDrive cloudDrive=new GoogleCloudDrive();
		//cloudDrive.init("Drive");
		
		//cloudDrive.uploadFile("Test79",new byte[] {10,65,66,67} );
		//byte[] array=cloudDrive.downloadFile("Test12");
		
		//System.out.write(array);
		
		
	}
}
