package org.pokenet.server.backend;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import org.pokenet.server.backend.entity.NonPlayerChar;
import org.pokenet.server.backend.entity.Positionable.Direction;

/**
 * Handles NPC and WarpTile Loading
 * @author shadowkanji
 *
 */
public class DataLoader implements Runnable {
	private File m_file;
	private ServerMap m_map;
	
	/**
	 * Constructor
	 * @param f
	 */
	public DataLoader(File f, ServerMap m) {
		m_file = f;
		new Thread(this).start();
	}

	/**
	 * Called by starting the thread
	 */
	public void run() {
		try {
			Scanner reader = new Scanner(m_file);
			NonPlayerChar npc = new NonPlayerChar();
			WarpTile warp = new WarpTile();
			String line;
			String [] details;
			while(reader.hasNextLine()) {
				line = reader.nextLine();
				if(line.equalsIgnoreCase("[npc]")) {
					npc = new NonPlayerChar();
					npc.setName(reader.nextLine());
					npc.setFacing(Direction.valueOf(reader.nextLine()));
					npc.setSprite(Integer.parseInt(reader.nextLine()));
					npc.setX(Integer.parseInt(reader.nextLine()) / 32);
					npc.setY((Integer.parseInt(reader.nextLine()) / 32) - 8);
					//Load possible Pokemons
					line = reader.nextLine();
					if(!line.equalsIgnoreCase("NULL")) {
						details = line.split(",");
						HashMap<String, Integer> pokes = new HashMap<String, Integer>();
						for(int i = 0; i < details.length; i = i + 2) {
							pokes.put(details[i], Integer.parseInt(details[i + 1]));
						}
						npc.setPossiblePokemon(pokes);
					}
					//Set minimum party level
					npc.setPartySize(Integer.parseInt(reader.nextLine()));
					npc.setBadge(Integer.parseInt(reader.nextLine()));
					//Add all speech, if any
					line = reader.nextLine();
					if(!line.equalsIgnoreCase("NULL")) {
						details = line.split(",");
						for(int i = 0; i < details.length; i++) {
							npc.addSpeech(Integer.parseInt(details[i]));
						}
					}
					npc.setHealer(Boolean.parseBoolean(reader.nextLine()));
					npc.setBox(Boolean.parseBoolean(reader.nextLine()));
					npc.setShopKeeper(Boolean.parseBoolean(reader.nextLine()));
				} else if(line.equalsIgnoreCase("[/npc]")) {
					m_map.addChar(npc);
				} else if(line.equalsIgnoreCase("[warp]")) {
					warp = new WarpTile();
					warp.setX(Integer.parseInt(reader.nextLine()));
					warp.setY(Integer.parseInt(reader.nextLine()));
					warp.setWarpX(Integer.parseInt(reader.nextLine()));
					warp.setWarpY(Integer.parseInt(reader.nextLine()));
					warp.setWarpMapX(Integer.parseInt(reader.nextLine()));
					warp.setWarpMapY(Integer.parseInt(reader.nextLine()));
				} else if(line.equalsIgnoreCase("[/warp]")) {
					m_map.addWarp(warp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
