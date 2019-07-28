package socket.proxy.cloud.types;

import java.io.IOException;

public interface CloudDrive {
	
    /**
     * Should initialize CloudDrive file system.
     *
     * @param      APPLICATION_NAME     For logging purpose and can be anything.
     * @exception  <code>Exception</code>should be thrown for any CloudDrive communication error.
     */
	public void init(String APPLICATION_NAME) throws Exception;
	
    /**
     * Should upload file to drive.
     *
     * @param      uploadFileName     file name to upload.
     * @param      inputBytes         file content as byte array.
     * @exception  <code>Exception</code>should be thrown for any CloudDrive communication error.
     */
	public void uploadFile(String uploadFileName,byte[] inputBytes)  throws Exception;
	
    /**
     * Should upload file to drive.
     *
     * @param      uploadFileName     file name to upload.
     * @param      inputBytes         file content as byte array.
     * @param      offset             the start offset in the data.
     * @param      length             the number of bytes to write.
     * @exception  <code>Exception</code>should be thrown for any CloudDrive communication error.
     */
	public void uploadFile(String uploadFileName,byte[] inputBytes, int offset, int length)  throws Exception;
	 
	/**
     * Should download file from drive.
     *
     * @param      downloadFileName   file name to download.
     * @return     byte read.
     * @exception  <code>Exception</code>should be thrown for any CloudDrive communication error.
     */
	public byte[] downloadFile(String downloadFileName)  throws Exception;
	
	/**
     * Should delete file from drive.
     *
     * @param      deleteFileName     file name to delete.
     * @exception  <code>Exception</code>should be thrown for any CloudDrive communication error.
     */
	public void deleteFile(String deleteFileName) throws Exception;

	/**
     * Should tell weather file exist in drive or not.
     *
     * @param      fileName           file name to check.
     * @return     boolean true if file exist.
     * @exception  <code>Exception</code>should be thrown for any CloudDrive communication error.
     */
	public boolean isFileExist(String fileName) throws Exception;
}
