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
    
    Drive service;

	@Override
	public void init(String APPLICATION_NAME) throws Exception {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();		
	}

	@Override
	public void uploadFile(String uploadFileName, byte[] inputBytes) throws FileAlreadyExistsException,Exception {
		uploadFile(uploadFileName, inputBytes,0,inputBytes.length);
	}

	@Override
	public void uploadFile(String uploadFileName, byte[] inputBytes, int offset, int length) throws FileAlreadyExistsException,Exception {
		for (File file : service.files().list().execute().getFiles())
			if(file.getName().equalsIgnoreCase(uploadFileName))
				throw new FileAlreadyExistsException("File aready exist in drive " + uploadFileName);
		File fileMetadata = new File();
        fileMetadata.setName(uploadFileName);
        InputStreamContent mediaContent = new InputStreamContent("text/plain", new BufferedInputStream( new ByteArrayInputStream(inputBytes,offset,length)));  
        File file = service.files().create(fileMetadata, mediaContent)
            .setFields("id")
            .execute();
        System.out.println("uploaded   : "+uploadFileName+" " + file.getId());
		
	}


	@Override
	public byte[] downloadFile(String downloadFileName) throws FileNotFoundException,Exception {
		List<File> files =service.files().list().execute().getFiles();
		if (files == null || files.isEmpty()) {
            throw new FileNotFoundException("File not found "+downloadFileName);
        } else {
            for (File file : files)
            	if(file.getName().equalsIgnoreCase(downloadFileName))
	    		{
            		byte[] outputBytes= service.files().get(file.getId()).executeMediaAsInputStream().readAllBytes();
            		System.out.println("downloaded : "+downloadFileName+" " + file.getId());
            		return outputBytes;
	    		}
            throw new FileNotFoundException("File not found "+downloadFileName);
        }
	}

	@Override
	public void deleteFile(String deleteFileName) throws Exception {
		for (File file : service.files().list().execute().getFiles())
			if(file.getName().equalsIgnoreCase(deleteFileName))
			{
				service.files().delete(file.getId()).execute();
				System.out.println("deleted    : "+deleteFileName+" " + file.getId());
				break;
			}
	}
}
