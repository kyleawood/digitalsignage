/**
 * Digital Signage Software developed by Kyle Wood
 */

package DS.presentation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.InterruptedException;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import DS.business.AccessPlayList;
import DS.business.RemoteUpdate;
import DS.objects.Item;

public class MainWindow {
	/*
	 * Local Variables
	 */
	protected Shell shell;
	private Table table;
	private Display display;
	private TableColumn name, id, time, location, fileType;
	private DropTarget dropTarget;
	private Button btnAdd, btnBrowse, btnPlay, btnRemoveItem, btnUp, btnDown;
	private Table table_1;
	private TableColumn addFileType, addName, addDuration, addLoc;
	private AccessPlayList accessList;
	private ArrayList<Item> playList;
	private int to, from, count;
	private boolean dragDetect;
	private Player player;
	private MainWindow main;
	private Timer refreshTimer, updateTimer;
	private RemoteUpdate remoteUpdate;
	
	/**
	 * 
	 * @param activate tells the player whether or not to begin playing immediately on launch
	 */
	public MainWindow(Boolean activate){
		main = this;
		if(activate){
			display = Display.getDefault();
			createContents();
			loadItems();
			shell.layout();
			displaySettings();
			remoteUpdate = new RemoteUpdate();
			play();
			runWindow();
		}
		else{
			display = Display.getDefault();
			createContents();
			loadItems();
			shell.layout();
			displaySettings();
			remoteUpdate = new RemoteUpdate();
			runWindow();
		}
	}
	/**
	 * Keeps the window active until disposed
	 */
	public void runWindow() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(display,SWT.TITLE);
		shell.setSize(944, 600);
		shell.setText("Play List Edit Window");
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.addDragDetectListener(new Table_DragListener());
		table.addSelectionListener(new Table_SelectionListener());
		table.addMouseListener(new Table_MouseListener());

		table.setBounds(10, 10, 875, 460);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("File");
		
		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);
		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new Exit_SelectionAdapter());
		mntmExit.setText("Exit");
		
		id = new TableColumn(table, SWT.NONE);
		id.setText("ID");
		id.setResizable(false);
		
		location = new TableColumn(table, SWT.NONE);
		location.setWidth(405);
		location.setText("File Location");
		location.setResizable(false);
		
		name = new TableColumn(table, SWT.NONE);
		name.setWidth(295);
		name.setText("File Name");
		name.setResizable(false);
		
		fileType = new TableColumn(table, SWT.NONE);
		fileType.setWidth(88);
		fileType.setText("File Type");
		fileType.setResizable(false);
		
		time = new TableColumn(table, SWT.NONE);
		time.setWidth(78);
		time.setText("Play Time");
		time.setResizable(false);
		
		dropTarget = new DropTarget(table, DND.DROP_DEFAULT | DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[]{FileTransfer.getInstance()});
		dropTarget.addDropListener(new Drop_DropTargetAdapter());
		
		btnAdd = new Button(shell, SWT.NONE);
		btnAdd.setBounds(684, 486, 75, 25);
		btnAdd.setText("Add");
		
		btnBrowse = new Button(shell, SWT.NONE);
		btnBrowse.addSelectionListener(new BrowseSelectionListener());
		btnBrowse.setBounds(10, 505, 75, 25);
		btnBrowse.setText("Browse");
				
		table_1 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setBounds(91, 485, 575, 50);
		table_1.addSelectionListener(new Table_SelectionListener());
		
		addLoc = new TableColumn(table_1, SWT.NONE);
		addLoc.setWidth(0);
		addLoc.setText("File Location");
		addLoc.setResizable(false);
		
		addName = new TableColumn(table_1, SWT.NONE);
		addName.setWidth(387);
		addName.setText("Name");
		addName.setResizable(false);
		
		addFileType = new TableColumn(table_1, SWT.NONE);
		addFileType.setWidth(108);
		addFileType.setText("File Type");
		addFileType.setResizable(false);
		
		addDuration = new TableColumn(table_1, SWT.NONE);
		addDuration.setWidth(75);
		addDuration.setText("Duration");
		addDuration.setResizable(false);
				
		btnAdd.addSelectionListener(new AddSelectionListener());
		btnPlay = new Button(shell, SWT.NONE);
		btnPlay.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				play();
			}
		});

		btnPlay.setBounds(797, 489, 97, 57);
		btnPlay.setText("Play");
		
		btnRemoveItem = new Button(shell, SWT.NONE);
		btnRemoveItem.setBounds(684, 517, 75, 25);
		btnRemoveItem.setText("Delete");
		
		btnUp = new Button(shell, SWT.NONE);
		/*
		 * Selection Listener for the 'Up' button, moves
		 * the currently selected video up in the play list
		 */
		btnUp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				int curr = table.getSelectionIndex();
				if(accessList == null){
					accessList = new AccessPlayList();
				}
				accessList.update(Integer.parseInt(table.getItem(curr).getText(0)),
						table.getSelectionIndex()-1,
						Integer.parseInt(table.getItem(curr-1).getText(0)),
						table.getSelectionIndex());
				
				playList.get(curr).setLoc(curr-1);
				playList.get(curr-1).setLoc(curr);
				loadItems();
				table.select(curr-1);
				displaySettings();
			}
		});
		btnUp.setBounds(891, 73, 41, 27);
		btnUp.setText("Up");
		
		btnDown = new Button(shell, SWT.NONE);
		/*
		 * Selection Listener for the 'Down' button, moves
		 * the currently selected video down in the play list
		 */
		btnDown.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				int curr = table.getSelectionIndex();
				if(accessList == null){
					accessList = new AccessPlayList();
				}
				accessList.update(Integer.parseInt(table.getItem(curr).getText(0)),
						table.getSelectionIndex()+1,
						Integer.parseInt(table.getItem(curr+1).getText(0)),
						table.getSelectionIndex());
				
				playList.get(curr).setLoc(curr+1);
				playList.get(curr+1).setLoc(curr);
				loadItems();
				displaySettings();
				table.select(curr+1);
			}
		});
		btnDown.setBounds(891, 111, 45, 27);
		btnDown.setText("Down");
		btnRemoveItem.addSelectionListener(new DeleteSelectionListener());
	
		shell.open();
	}
	/*
	 * Calls the VLC player window, as well as setting up the timers for
	 * the VLC midnight reset and the update play list timer
	 */
	private void play(){
		refreshTimer = new Timer();
		updateTimer = new Timer();
		// both timers are in milliseconds, refresh is set to midnight, update is set for every 20 minutes
		refreshTimer.scheduleAtFixedRate(new refreshTask(), getRefreshTime() * 1000, 86400000);
		//TODO
		updateTimer.scheduleAtFixedRate(new updateTask(), getUpdateTime() * 1000, 1200000);
		player = new Player(main);
		player.play();
	}
	/**
	 * Get the current system time and determine remaining minutes to the next 1/3 of an hour
	 * @return amount of seconds until the next 1/3 of an hour
	 */
	private long getUpdateTime() {
		long time = 0;
		int timeCase;
		DateFormat dateFormat;
		Date date = new Date();
		
        dateFormat = new SimpleDateFormat("mm");
        time += Integer.parseInt(dateFormat.format(date));
        //determine in which 1/3 the current time falls
        timeCase = (int) (time / 20);
        
        //based on which 1/3 the current time falls in, figure out how many minutes until the next 1/3 hour
        switch(timeCase){
        	case 0:
        		time = 20-time;
        		break;
        	case 1:
        		time = 40-time;
        		break;
        	case 2:
        		time = 60-time;
        		break;
        }
        return time*60;
	}
	/**
	 * Get the current System time and determine how long until midnight
	 * @return amount of seconds until midnight
	 */
	private long getRefreshTime() {
		long time = 0;
        DateFormat dateFormat;
        Date date = new Date();
        
        dateFormat = new SimpleDateFormat("HH");
        time += Integer.parseInt(dateFormat.format(date)) * 3600;
        dateFormat = new SimpleDateFormat("mm");
        time += Integer.parseInt(dateFormat.format(date)) * 60;
        
        return (86400 - time);
    }
	/**
	 * called every time something happens with the window
	 * updates the current states of buttons
	 */
	private void displaySettings(){
		if (table.getSelectionCount() > 0)
        {
            btnRemoveItem.setEnabled(true);
            btnRemoveItem.setText("Delete");
            btnUp.setEnabled(false);
            btnDown.setEnabled(false);
            if(table.getSelectionIndex() != 0){
            	btnUp.setEnabled(true);
            }
            if(table.getSelectionIndex() != count-1){
            	btnDown.setEnabled(true);
            }
            table_1.clearAll();
            table_1.deselectAll();
        }
		else if(table_1.getSelectionCount() > 0){
			btnAdd.setEnabled(true);
			btnRemoveItem.setEnabled(true);
			btnRemoveItem.setText("Cancel");
			btnUp.setEnabled(false);
			btnDown.setEnabled(false);
			table.deselectAll();
		}
        else
        {
        	btnAdd.setEnabled(false);
            btnRemoveItem.setEnabled(false);
            btnUp.setEnabled(false);
            btnDown.setEnabled(false);
            btnRemoveItem.setText("Delete");
        }
	}
	/**
	 * SelectionListener for the main play list table.
	 * Call displaySettings() to update the current state of buttons
	 *
	 */
	private class Table_SelectionListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			displaySettings();
		}
	}
	/**
	 * SelectionListener for the delete button
	 * Calls accessList to delete the currently selected item from the DB
	 */
	private class DeleteSelectionListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			int curr;
			if(btnRemoveItem.getText().equalsIgnoreCase("Cancel")){
				table_1.clearAll();
				table_1.deselectAll();
				loadItems();
				displaySettings();
			}
			else{
				curr = table.getSelectionIndex();
				if(accessList == null){
					accessList = new AccessPlayList();
				}
				accessList.deleteItem(Integer.parseInt(table.getItem(table.getSelectionIndex()).getText(0)));
				loadItems();
				displaySettings();
				if(curr == table.getItemCount()){
					table.select(curr-1);
				}
				else{
					table.select(curr);
				}
				playList = null;
			}
			
			
			
		}
	}
	/**
	 * SelectionListener for the 'Add" button
	 * Calls accessList to add the item currently in the file browser table to the DB
	 * Also refreshes the table and calls DisplaySettings();
	 *
	 */
	private class AddSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e){
			if(table_1.getItem(0).getText(0) == "" || table_1.getItem(0).getText(1) == "" || 
					table_1.getItem(0).getText(2) == "" || table_1.getItem(0).getText(3) == ""){
				
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);

                messageBox.setText("Error");
                messageBox.setMessage("There was an error");
                messageBox.open();
                return;
			}
			int loc = playList.size();
			if(accessList == null){
				accessList = new AccessPlayList();
			}
			accessList.addItem(table_1.getItem(0).getText(0), table_1.getItem(0).getText(1), 
					table_1.getItem(0).getText(2), Integer.parseInt(table_1.getItem(0).getText(3)), loc);
			
			table_1.removeAll();
			table_1.deselectAll();
			displaySettings();
			playList = null;
			loadItems();
		}
	}
	private class Exit_SelectionAdapter extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			display.close();
			display.dispose();
		}
	}
	private class Table_MouseListener extends MouseAdapter{

		public void mouseUp(MouseEvent event) {		
			/*try{
				to = Integer.parseInt(table.getItem(new Point(event.x, event.y)).getText());
				System.out.println("Not NULL");
			}catch(NullPointerException e){
				System.out.println("Its NULL");
			} 
			System.out.println("drag stopped");
			dragDetect = false;*/
		}
		public void mouseDown(MouseEvent event){
			/*try{
				from = Integer.parseInt(table.getItem(new Point(event.x, event.y)).getText());
				System.out.println("Not NULL");
			}catch(NullPointerException e){
				System.out.println("Its NULL");
			}*/
		}

	}
	private class Table_DragListener implements DragDetectListener{

		public void dragDetected(DragDetectEvent e) {
			dragDetect = true;
			while(dragDetect){
				System.out.println("Dragging");
			}
		}
		
	}
	private class BrowseSelectionListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			table.deselectAll();
			displaySettings();
			FileDialog fd = new FileDialog(shell, SWT.OPEN);
			String[] filterExt = {"*.*"}, tokens;
			int count = 0;
			TableItem newItem;
			fd.setFilterExtensions(filterExt);
			String selected = fd.open();
			
			if(selected != null){
				table_1.removeAll();
				newItem = new TableItem(table_1, SWT.NONE);
				newItem.setText(0, selected);
				StringTokenizer st = new StringTokenizer(selected, "/");
				tokens = new String[st.countTokens()];
				while(st.hasMoreTokens()){
					tokens[count] = st.nextToken();
					count++;			
				}	
				newItem.setText(1, tokens[tokens.length-1]);
				st = new StringTokenizer(tokens[tokens.length-1], ".");
				count = 0;
				tokens = new String[st.countTokens()];
				while(st.hasMoreTokens()){
					tokens[count] = st.nextToken();
					count++;			
				}
				newItem.setText(2, tokens[tokens.length-1]);
				if(tokens[tokens.length-1].compareTo("jpg") == 0 || tokens[tokens.length-1].compareTo("jpeg") == 0 || tokens[tokens.length-1].compareTo("png") == 0 || tokens[tokens.length-1].compareTo("JPG") == 0){
					newItem.setText(3, "10");
				}
				else if(tokens[tokens.length-1].compareTo("avi") == 0 || tokens[tokens.length-1].compareTo("mov") == 0 || tokens[tokens.length-1].compareTo("mp4") == 0){
					newItem.setText(3,"99");
				}
				else{
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);

	                messageBox.setText("Error");
	                messageBox.setMessage("Unsupported file type");
	                messageBox.open();
	                table_1.removeAll();
	                return;
				}
			}
			table_1.select(0);
			displaySettings();
		}
	}

	private class Drop_DropTargetAdapter extends DropTargetAdapter{
		public void drop(DropTargetEvent event){
			String fileList[] = null;
			FileTransfer ft = FileTransfer.getInstance();
			TableItem newItem;
			String[] tokens;
			int count = 0;
			String selected;
			if(ft.isSupportedType(event.currentDataType)){
				fileList = (String[])event.data;
			}
			selected = fileList[0];
			
			table_1.removeAll();
			newItem = new TableItem(table_1, SWT.NONE);
			newItem.setText(0, selected);
			StringTokenizer st = new StringTokenizer(selected, "\\/");
			tokens = new String[st.countTokens()];
			while(st.hasMoreTokens()){
				tokens[count] = st.nextToken();
				count++;			
			}	
			newItem.setText(1, tokens[tokens.length-1]);
			st = new StringTokenizer(tokens[tokens.length-1], ".");
			count = 0;
			tokens = new String[st.countTokens()];
			while(st.hasMoreTokens()){
				tokens[count] = st.nextToken();
				count++;			
			}
			newItem.setText(2, tokens[tokens.length-1]);
			if(tokens[tokens.length-1].compareTo("jpg") == 0){
				newItem.setText(3, "10");
			}
			else{
				newItem.setText(3,"99");
			}
			table_1.select(0);
			displaySettings();
		}
	}
	public void getPlayList(){
		if(accessList == null){
			accessList = new AccessPlayList();
		}
		playList = accessList.getPlayList();
	}
	public void loadItems(){
		TableItem listItem;
		if(playList == null){
				getPlayList();
		}
		
		Collections.sort(playList);
		count = playList.size();
		if(playList != null){
			table.removeAll();
			
			for(int i = 0; i < playList.size(); i++){
				listItem = new TableItem(table, SWT.NONE);
				listItem.setText(0, Integer.toString(playList.get(i).getId()));
				listItem.setText(1, playList.get(i).getLocation());
				listItem.setText(2, playList.get(i).getFileName());
				listItem.setText(3, playList.get(i).getFileType());
				listItem.setText(4, Integer.toString(playList.get(i).getPlayTime()));
			}
		}
	}
	public class refreshTask extends TimerTask {
		/*
		* sends a kill command to the current player and restarts a new one
		*/
		public void run() {
			player.kill();
			player = null;
			try{
				Process p = Runtime.getRuntime().exec("killall -9 xdg-screensaver");
				p.waitFor();
			} catch(InterruptedException e){
				System.err.println("error waiting for process while killing XDG");
			} catch (IOException e){
				System.err.println("error killing XDG");
			}
			player = new Player(main);
			player.play();
		}
	}
	/**
	 * Timer Listener for the update timer
	 * Checks to verify it has the newest play list, if not it pauses the update timer
	 * and runs a remote update.  Once the update is finished it resumes the timer and calls
	 * player.kill()
	 */
	public class updateTask extends TimerTask {
		public void run() {
			if(remoteUpdate.checkForUpdate()){
				updateTimer.cancel();
				if(remoteUpdate.runUpdate()){
					updateTimer = new Timer();
					updateTimer.scheduleAtFixedRate(new updateTask(), getUpdateTime() * 1000, 1200000);
					player.kill();
					player = null;
					try{
						Process p = Runtime.getRuntime().exec("killall -9 xdg-screensaver");
						p.waitFor();
					} catch(InterruptedException e){
						System.err.println("error waiting for process while killing XDG");
					} catch (IOException e){
						System.err.println("error killing XDG");
					}
					player = new Player(main);
					player.play();
				}
				else{
					System.err.println("Playlist update error");
				}
			}
		}
	}
	public void playerKill() {
		refreshTimer.cancel();
		updateTimer.cancel();
	}
}

