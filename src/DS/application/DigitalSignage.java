package DS.application;

import DS.presentation.MainWindow;

public class DigitalSignage {
	public static final String localdbName = "DigitalSignageDB";
	
	public static void main(String[] args){
		System.setProperty("jna.library.path", "c:\\Program Files (x86)\\VideoLAN\\VLC\\");
		startUp();
		try{
			if(args[0].compareTo("play") == 0){
				new MainWindow(true);
			}
		}catch(Exception e){
			new MainWindow(false);
		}
		shutDown(); 
	}
	
	public static void startUp(){
		Services.createLocalDataAccess(localdbName);
	}
	public static void shutDown(){
		Services.closeDataAccess();
	}
}
