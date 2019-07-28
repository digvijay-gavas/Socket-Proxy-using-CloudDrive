package socket.proxy.cloud.types;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class VirtualNativeFileCloudDrive implements CloudDrive{
    
    Drive service;

	@Override
	public void init(String APPLICATION_NAME) throws Exception {	
	}

	@Override
	public void uploadFile(String uploadFileName, byte[] inputBytes) throws FileAlreadyExistsException,Exception {
		uploadFile(uploadFileName, inputBytes,0,inputBytes.length);
	}

	@Override
	public void uploadFile(String uploadFileName, byte[] inputBytes, int offset, int length) throws FileAlreadyExistsException,Exception {
		boolean isFileExistInDrive=true;
		while(isFileExistInDrive)
		{
			isFileExistInDrive=false;
			List<File> files =service.files().list().execute().getFiles();
			
			if (files == null || files.isEmpty()) {
	            System.out.println(uploadFileName+" not exist already");
	        } else {
	            for (File file1 : files)
	            {
	            	//System.out.println("---"+file1.getName()+ " "+file1.getId());
	            	if(file1.getName().equalsIgnoreCase(uploadFileName))
		    		{
	            		System.out.println(uploadFileName+" exist already");
	            		System.out.println(uploadFileName+" waiting");
	            		isFileExistInDrive=true;
	            		Thread.sleep(2000);
		    		}
	            	
	            }
	            files.clear();
	            
	        }
		}
		File fileMetadata = new File();
        fileMetadata.setName(uploadFileName);
        InputStreamContent mediaContent = new InputStreamContent("text/plain", new BufferedInputStream( new ByteArrayInputStream(inputBytes,offset,length)));  
        File file = service.files().create(fileMetadata, mediaContent)
            .setFields("id")
            .execute();
        
        System.out.println("File:"+file.getName()+" " + file.getId());
		
	}


	@Override
	public byte[] downloadFile(String downloadFileName) throws FileNotFoundException,Exception {
		boolean isFileExistInDrive=false;
		OutputStream outputStream = new ByteArrayOutputStream();
		while(!isFileExistInDrive)
		{
			isFileExistInDrive=false;
			List<File> files =service.files().list().execute().getFiles();
			
			if (files == null || files.isEmpty()) {
	            System.out.println(downloadFileName+" not exist");
	            System.out.println(downloadFileName+" waiting");
	            Thread.sleep(2000);
	        } else {
	            for (File file1 : files)
	            {
	            	if(file1.getName().equalsIgnoreCase(downloadFileName))
		    		{
	            		System.out.println(downloadFileName+" exist");
	            		byte[] outputBytes= service.files().get(file1.getId()).executeMediaAsInputStream().readAllBytes();
	            		service.files().delete(file1.getId()).execute();
	            		return outputBytes;
	            		
		    		}
	            	
	            }
	            files.clear();
	            
	        }
		}
		return null;
	}

	@Override
	public void deleteFile(String deleteFileName) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
