package org.pokenet.client.ui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.pokenet.client.GameClient;
import org.pokenet.client.ui.base.ImageButton;
import org.pokenet.client.ui.frames.ChatDialog;
import org.pokenet.client.ui.frames.FriendListDialog;

import mdes.slick.sui.Button;
import mdes.slick.sui.Display;
import mdes.slick.sui.Frame;

/**
 * The main ui on screen
 * @author shadowkanji
 *
 */
public class Ui extends Frame {
	private FriendListDialog m_friendList;
	private ChatDialog m_localChat;
	private ArrayList<ChatDialog> m_privateChat;
	private ImageButton [] m_buttons;
	private Display m_display;
	
	/**
	 * Default constructor
	 */
	public Ui(Display display) {
		this.setSize(48, 256);
		this.setLocation(0, -24);
		this.setBackground(new Color(0, 0, 0, 75));
		this.setResizable(false);
		this.setDraggable(false);
		
		m_display = display;
		
		m_localChat = new ChatDialog("Cl", "Chat: Local");
		m_privateChat = new ArrayList<ChatDialog>();
		
		m_buttons = new ImageButton[6];
		for(int i = 0; i < m_buttons.length; i++) {
			m_buttons[i] = new ImageButton();
			m_buttons[i].setSize(32, 32);
			m_buttons[i].setLocation(8, (32 * i + 1) + 4);
		}
		this.add(GameClient.getInstance().getTimeService());
		
		this.getTitleBar().setVisible(false);
		
		m_display.add(m_localChat);
		m_display.add(this);
	}
	
	/**
	 * Adds a message to its appropriate chat window
	 * @param m
	 */
	public void messageReceived(String m) {
		switch(m.charAt(0)) {
		case 'l':
			//Local Chat
			m_localChat.appendText(m.substring(1));
			break;
		case 'p':
			//Private Chat
			String [] details = m.substring(1).split(",");
			//Find the private chat and add the text to it
			for(int i = 0; i < m_privateChat.size(); i++) {
				if(m_privateChat.get(i).getName().equalsIgnoreCase(details[0])) {
					m_privateChat.get(i).appendText(details[1]);
					/*
					 * If the private chat is visible on screen, exit this method
					 * Else, add a popup notification about the new message
					 */
					if(m_privateChat.get(i).isVisible())
						return;
					else {
						NotificationManager.addNotification("Message from " + details[0]);
						return;
					}
				}
			}
			//If not found, open up a new chat window
			ChatDialog c = new ChatDialog("Cp" + details[0] + ",", "Chat: " + details[0]);
			m_privateChat.add(c);
			m_display.add(c);
			break;
		}
	}
	
	/**
	 * Sets all components visible/invisible
	 * @param b
	 */
	public void setAllVisible(boolean b) {
		this.setVisible(b);
		m_localChat.setVisible(b);
		for(int i = 0; i < m_privateChat.size(); i++) {
			m_privateChat.get(i).setVisible(b);
		}
	}
	
	/**
	 * Opens up all private chats
	 */
	public void showPrivateChatWindows() {
		for(int i = 0; i < m_privateChat.size(); i++) {
			m_privateChat.get(i).setVisible(true);
		}
	}
	
	/**
	 * Returns the local chat
	 * @return
	 */
	public ChatDialog getLocalChat() {
		return m_localChat;
	}
}
