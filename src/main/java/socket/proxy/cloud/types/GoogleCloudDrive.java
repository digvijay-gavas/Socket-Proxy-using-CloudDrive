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

public class GoogleCloudDrive implements CloudDrive{
	private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleCloudDrive.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        
        
        // upload temp file
        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        java.io.File filePath = new java.io.File("C:\\Users\\49151\\Pictures\\Camera Roll\\WIN_20190622_13_15_22_Pro.jpg");
        

        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = service.files().create(fileMetadata, mediaContent)
            .setFields("id")
            .execute();
        System.out.println("File ID: " + file.getId());
        
        
        
        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file1 : files) {
                System.out.printf("%s (%s)\n", file1.getName(), file1.getId());
            }
        }
    }

    
    Drive service;

	@Override
	public void init(String APPLICATION_NAME) throws Exception {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();		
	}

	@Override
	public void uploadFile(String uploadFileName, byte[] inputBytes) throws Exception {
		uploadFile(uploadFileName, inputBytes,0,inputBytes.length);
	}

	@Override
	public void uploadFile(String uploadFileName, byte[] inputBytes, int offset, int length) throws Exception {
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
	public byte[] downloadFile(String downloadFileName) throws Exception {
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
	/*
	public  File insertFile(String title, String parentId, String mimeType, String filename, InputStream stream) {

	    try {
	             

	            // File's metadata.
	            File body = new File();
	            body.setName(title);
	            body.setMimeType(mimeType);

	            // Set the parent folder.
	            if (parentId != null && parentId.length() > 0) {
	              body.setParents(
	                  Arrays.asList(new ParentReference().setId(parentId)));
	            }

	            // File's content.
	            InputStreamContent mediaContent = new InputStreamContent(mimeType, new BufferedInputStream(stream));  
	            try {
	              File file = service.files().create(body, mediaContent).execute();

	              return file;
	            } catch (IOException e) {
	              logger.log(Level.WARNING, "un error en drive service: "+ e);
	              return null;
	            }

	    } catch (IOException e1) {
	           // TODO Auto-generated catch block
	           e1.printStackTrace();
	           return null;
	    }

	  }*/
}
