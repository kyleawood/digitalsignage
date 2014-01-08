package DS.presentation;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

import DS.business.AccessPlayList;
import DS.objects.Item;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.list.MediaList;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;

public class Player {
	private MediaPlayerFactory mediaPlayerFactory;
	private EmbeddedMediaPlayer mediaPlayer;
	private MediaListPlayer mediaListPlayer;
	private MediaList mediaList;
	private AccessPlayList accessList;
	private ArrayList<Item> playList;
	private JPanel p;
	private JFrame f;
	private MainWindow parent;
	
	public Player(MainWindow mainWindow){
		parent = mainWindow;
		System.setProperty("jna.library.path", "\\lib\\");
		Canvas c = new Canvas();
	    c.setBackground(Color.black);

	    p = new JPanel();
	    p.setLayout(new BorderLayout());
	    p.add(c, BorderLayout.CENTER);
	    
	    f = new JFrame("VLCJ");
	    f.setContentPane(p);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setExtendedState(Frame.MAXIMIZED_BOTH);
	    f.setUndecorated(true);
	    f.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
					
			}

			public void keyReleased(KeyEvent e) {
				
			}

			public void keyPressed(KeyEvent e) {
				if(e.isAltDown() && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_SPACE && mediaPlayer.isPlaying()){
					parent.playerKill();
					mediaListPlayer.stop();
					mediaList.release();
				    mediaListPlayer.release();
				    mediaPlayer.release();
				    mediaPlayerFactory.release();
					f.dispose();
				}
				
			}
		});
	    
	    mediaPlayerFactory = new MediaPlayerFactory("--no-video-title-show", "-v");
	    mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();
	    mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new FullScreenStrategy() {

	      public void enterFullScreenMode() {
	    	  
	      }

	      public void exitFullScreenMode() {
	        
	      }

	      public boolean isFullScreenMode() {
	        return false;
	      }
	    });
	    
	    mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

	    f.setVisible(true);
	    mediaListPlayer.setMediaPlayer(mediaPlayer); 
	    mediaList = mediaPlayerFactory.newMediaList();
	    loadPlayer(); //grab a list from the DB and add each to the mediaList
	    mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventListener() {
			
			@Override
			public void titleChanged(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void timeChanged(MediaPlayer arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void subItemPlayed(MediaPlayer arg0, int arg1) {
				
			}
			
			@Override
			public void subItemFinished(MediaPlayer arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void stopped(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void snapshotTaken(MediaPlayer arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void seekableChanged(MediaPlayer arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void positionChanged(MediaPlayer arg0, float arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void playing(MediaPlayer arg0) {
				
			}
			
			@Override
			public void paused(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void pausableChanged(MediaPlayer arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void opening(MediaPlayer arg0) {
			
				
			}
			
			@Override
			public void newMedia(MediaPlayer arg0) {
				
			}
			
			@Override
			public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mediaStateChanged(MediaPlayer arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mediaParsedChanged(MediaPlayer arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mediaMetaChanged(MediaPlayer arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mediaFreed(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mediaDurationChanged(MediaPlayer arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mediaChanged(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void lengthChanged(MediaPlayer arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void forward(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void finished(MediaPlayer arg0) {
				synchronized (mediaPlayer) {
					mediaPlayer.notify();
				}				
			}
			
			@Override
			public void error(MediaPlayer arg0) {
				System.out.println("Error in video");
				
			}
			
			@Override
			public void endOfSubItems(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void buffering(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void backward(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    mediaListPlayer.setMediaList(mediaList);
	    mediaListPlayer.setMode(MediaListPlayerMode.LOOP);
	}
	public void play(){
		mediaListPlayer.play();
	}
	public void kill(){
		if(mediaList.size() != 0){
			synchronized (mediaPlayer) {
				try {
					mediaPlayer.wait();
				} catch (InterruptedException e) {
					System.err.println("There was an error when trying to wait for end of current object");
				}
			}
		}
		mediaListPlayer.stop();
		mediaList.release();
		mediaListPlayer.release();
		mediaPlayer.release();
		mediaPlayerFactory.release();
		f.dispose();
	}
	
	private int loadPlayer(){ 		
		if(accessList == null){
			accessList = new AccessPlayList();
		}
		playList = accessList.getPlayList();
	
		if(playList.size() == 0){				
			return 0;
		}
		Collections.sort(playList);
		for(int i = 0; i < playList.size(); i++){
			if(playList.get(i).getFileType().compareToIgnoreCase("jpg") == 0 || playList.get(i).getFileType().compareToIgnoreCase("jpeg") == 0 || playList.get(i).getFileType().compareToIgnoreCase("png") == 0){
				mediaList.addMedia("fake://" + playList.get(i).getLocation(), "fake-duration="+(playList.get(i).getPlayTime() * 1000));
			}
			else if(playList.get(i).getFileType().compareToIgnoreCase("mov") == 0 || playList.get(i).getFileType().compareToIgnoreCase("wmv") == 0
					|| playList.get(i).getFileType().compareToIgnoreCase("mp4") == 0 || playList.get(i).getFileType().compareToIgnoreCase("flv") == 0){
				mediaList.addMedia(playList.get(i).getLocation());
			}
			else{
				System.err.println("Error loading video to playlist");
			}
		}
		return 1;
	}
}


