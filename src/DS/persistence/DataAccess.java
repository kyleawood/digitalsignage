package DS.persistence;

import java.util.ArrayList;

import DS.objects.Item;

public interface DataAccess {
	public void openLocal(String dbName);
	public void close();

	public ArrayList<Item> getPlayList();

	public void addItem(String filePath, String fileName, String fileType, int playTime,
			int order);

	public void deleteItem(int itemID);
	
	public void update(int itemID1, int pos1, int itemID2, int pos2);
	
	public void emptyDB();
}
