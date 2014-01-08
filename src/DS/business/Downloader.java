package DS.business;

import java.io.*;
import java.net.*;

public class Downloader {
	final static int size = 1024;
	final private String fAddress = "http://murrayhummer.com/projects/digital-signage/content/";
	final private String destinationDir = "/home/signage/Digital Signage/content/";
	private String[] children;
	
	public boolean fileUrl(String localFileName) {
		int attempt = 0;
		OutputStream outStream = null;
		URLConnection uCon = null;
		boolean success = false;
		
		InputStream is = null;
		try {
			URL Url;
			byte[] buf;
			int ByteRead, ByteWritten = 0;
			Url = new URL(fAddress + localFileName);
			

			uCon = Url.openConnection();
			is = uCon.getInputStream();
			buf = new byte[size];
			int size = uCon.getContentLength();

			if(verifyFile(destinationDir, localFileName, size)){
				//Files exists and is the same size
				success = true;
			}
			else {
				do{
					attempt++;
					ByteWritten = 0;
					ByteRead = 0;
					outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + "/" + localFileName));
					while ((ByteRead = is.read(buf)) != -1) {
						outStream.write(buf, 0, ByteRead);
						ByteWritten += ByteRead;
					}
					//File Downloaded
				}
				while(ByteWritten != size && attempt <= 3);
				if(ByteWritten == size){
					/*try {
					    // Construct data
					    String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
					    data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");

					    // Send data
					    URL url = new URL("http://murrayhummer.com/projects/digital-signage/");
					    URLConnection conn = url.openConnection();
					    conn.setDoOutput(true);
					    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
					    wr.write(data);
					    wr.flush();

					    // Get the response
					    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					    String line;
					    while ((line = rd.readLine()) != null) {
					        System.out.println(line);
					    }
					    wr.close();
					    rd.close();
					} catch (Exception e) {
					}*/
					success = true;
				}
			}
		} catch (Exception e) {
			System.err.println("Error in playlist");
		} finally {
			try {
				is.close();
				outStream.close();
			} catch (IOException e) {
				
			} catch(NullPointerException e){
				
			}
		}
		return success;
	}
	/**
	 * This method determines if the current object in the play list is available in the content folder
	 * it returns a boolean telling the downloader whether or not the file needs to be downloaded
	 * 
	 * @param destinationDir path to the directory all downloads will be saved to
	 * @param localFileName name that the download will be given, same as the current files name
	 * @param size size of the file on the server
	 * @return boolean determining if the file is in local memory
	 */
	private boolean verifyFile(String destinationDir, String localFileName, int size) {
		for(String child : children){
			if(child.compareTo(localFileName) == 0){
				File localFile = new File(destinationDir + "/" + localFileName);
				if(localFile.length() == size){
					return true;
				}
				break;
			}
		}
		return false;
	}
	/**
	 * Called externally and given the name of a file to check for in local content folder, if it doesnt exist it downloads the file
	 * 
	 * @param fileName the name of the file to check for in local contents folder
	 * @return true if file exists or was successfully downloaded, false if there was an error
	 */
	public boolean fileDownload(String fileName) {	
		children = new File(destinationDir).list();

		return fileUrl(fileName);
	}
}
