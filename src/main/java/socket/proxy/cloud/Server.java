package socket.proxy.cloud;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import logging.ColorLogger;

public class Server {

	public static void start() throws Exception{
		ServerSocket 
		access_serverSocket=null,
		serverSocket=null;		
		try {
			access_serverSocket=new ServerSocket(Config.access_port);
			serverSocket=new ServerSocket(Config.port);
			
			while(true)
			{
				ColorLogger.logln("<warn>Ready on "+Config.access_port+"</warn>");
				Socket access_socket=access_serverSocket.accept();			
				//SocketBindThread.bind(access_socket, socket,Config.printSocketComunication);
				
			}
		}
		catch(IOException e)
		{
			serverSocket.close();
			access_serverSocket.close();
			e.printStackTrace();
		}
	}

}
