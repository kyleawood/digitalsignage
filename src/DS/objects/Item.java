package DS.objects;

public class Item implements Comparable<Item>{
	String fileName;
	String fileType;
	String location;
	int playTime;
	int playOrder;
	int id;
	
	public Item(String location, String fileName, String fileType, int playTime, int playOrder){
		this.location = location;
		this.fileName = fileName;
		this.fileType = fileType;
		this.playTime = playTime;
		this.playOrder = playOrder;
	}
	public Item(String location, String fileName, String fileType, int playTime, int playOrder, int id){
		this.location = location;
		this.fileName = fileName;
		this.fileType = fileType;
		this.playTime = playTime;
		this.playOrder = playOrder;
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public int getPlayTime() {
		return playTime;
	}
	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}
	public int getId() {
		return id;
	}
	public int getPlayOrder() {
		return playOrder;
	}
	public int compareTo(Item o) {
		return (playOrder - o.getPlayOrder());
	}
	public String getLocation() {
		return location;
	}
	public void setLoc(int playOrder){
		this.playOrder = playOrder;
	}
}
