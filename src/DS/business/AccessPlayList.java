package DS.business;

import java.util.ArrayList;

import DS.application.Services;
import DS.objects.Item;
import DS.persistence.DataAccess;

public class AccessPlayList {
	private DataAccess dataAccess;
	
	public AccessPlayList(){
		dataAccess = Services.getLocalDataAccess();
	}
	
	public ArrayList<Item> getPlayList(){
		return dataAccess.getPlayList();
	}
	
	public void addItem(String fileName, String filePath, String fileType, int playTime, int order){
		dataAccess.addItem(fileName, filePath, fileType, playTime, order);
	}

	public void deleteItem(int itemID) {
		dataAccess.deleteItem(itemID);
		
	}

	public void update(int itemID1, int pos1, int itemID2, int pos2) {
		dataAccess.update(itemID1, pos1, itemID2, pos2);
		
	}
	public void emptyDB(){
		dataAccess.emptyDB();
	}
}
