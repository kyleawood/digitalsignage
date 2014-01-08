package DS.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import DS.objects.Item;

public class RemoteUpdate {
	private AccessPlayList accessList;
	private URL boxMaster;
	private int updateTime;
	private BufferedReader in;
    private URLConnection con;
    private ArrayList<Item> items;
    private Downloader downloader;
    private boolean success;
    String inputLine, fileLocation;
	
	public RemoteUpdate(){
	}
	
	public boolean getData(){
		try{
			boxMaster = new URL("http://www.murrayhummer.com/projects/digital-signage/getPlaylist.php?playerid=1");
			con = boxMaster.openConnection();
	    	in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (MalformedURLException e1) {
			System.err.println("Update URL error");
			return false;
		} catch(IOException e){
	    	System.out.println("IO Error in RemoteUpdate");
	    	return false;
	    }
		return true;
	}
	
	public boolean runUpdate(){
		fileLocation = "/home/signage/Digital Signage/content/";
	    
		
	    items = new ArrayList<Item>();
	    downloader = new Downloader();
	    success = true;
	    
	    if(accessList == null){
			accessList = new AccessPlayList();
		}
	    System.out.println("Running update");
	    if(getData()){
	    	try{
			    while ((inputLine = in.readLine()) != null){
			    	String[] result = inputLine.split("[,;]");
			    	int count = 0;
			    	for(int i = 0; i < result.length-1; i+=3){
			    		try{
			    			items.add(new Item(fileLocation + result[i+0], result[i+0], result[i+1], Integer.parseInt(result[i+2]), count));
			    		} catch(ArrayIndexOutOfBoundsException e){
			    			System.err.println("Out of bounds error");
			    			return false;
			    		}
			    		count++;
			    	}
			    	try{
			    		//TODO
			    		updateTime = Integer.parseInt(result[result.length-1]);
			    	} catch(ArrayIndexOutOfBoundsException e){
			    		System.err.println("Array out of bounds error when trying to get playtime");
			    		return false;
			    	}
			    	
			    }
			    in.close();
			    
			    for(int i = 0; i < items.size(); i++){
			    	if(!downloader.fileDownload(items.get(i).getFileName())){
			    		success = false;
			    	}
			    }
	    	} catch(IOException e){
	    		System.err.println("Error while trying to parse the data from the server");
	    	}
		    
	    }
	    if(success){
	    	accessList.emptyDB();
	    	for(int i = 0; i < items.size(); i++){
	    		accessList.addItem(items.get(i).getLocation(), items.get(i).getFileName(), items.get(i).getFileType(), items.get(i).getPlayTime(), i);
	    	}
	    }
	    return success;
	}
	public boolean checkForUpdate(){
		if(getData()){
			try{
				inputLine = in.readLine();
			
		    	if(inputLine.equalsIgnoreCase("Playlist is Current")){
		    		return false;
		    	}
		    	else if(compareLists()){
		    		System.out.println("lists the same");
		    		return false;
		    	}
			} catch(IOException e){
				System.err.println("There was an error trying to parse the data from the server");
			}
		}
		System.out.println("lists different");
		return true;
	}
	/**
	 * 
	 * @return true if lists match, false if they do not match
	 */
	public boolean compareLists(){
		if(accessList == null){
			accessList = new AccessPlayList();
		}
		items = new ArrayList<Item>();
		ArrayList<Item> playList = accessList.getPlayList();
		String[] result = inputLine.split("[,;]");
    	int count = 0;
    	for(int i = 0; i < result.length-1; i+=3){
    		try{
    			items.add(new Item(fileLocation + result[i+0], result[i+0], result[i+1], Integer.parseInt(result[i+2]), count));
    		} catch(ArrayIndexOutOfBoundsException e){
    			System.err.println("Out of bounds error");
    			return false;
    		}
    		count++;
    	}
    	if(playList.size() != items.size()){
    		System.out.println("size difference");
    		return false;
    	}
    	for(int i = 0; i < playList.size(); i++){
    		if(playList.get(i).getFileName().compareTo(items.get(i).getFileName()) != 0){
    			System.out.println("file name difference");
    			return false;
    		}
    	}
		return true;
	}
}
