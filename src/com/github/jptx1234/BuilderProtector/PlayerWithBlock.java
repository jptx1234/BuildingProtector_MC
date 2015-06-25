package com.github.jptx1234.BuilderProtector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class PlayerWithBlock {
	private World world;
	private Player player;
	private Location tempStartLocation;
	private Location tempEndLocation;
	private HashMap<String, BlockStore> saves;
	
	
	public PlayerWithBlock(Player player){
		this.player = player;
		world = player.getWorld();
		saves = new HashMap<>();
	}
	
	public void setTempStartLocation(Location startLocation){
		this.tempStartLocation = startLocation;
	}
	public void setTempEndLocation(Location endLocation){
		this.tempEndLocation = endLocation;
	}
	public boolean isExistSave(String saveName){
		return saves.containsKey(saveName);
	}
	
	public void createNewSave(String saveName){
		BlockStore blocks = new BlockStore(world,tempStartLocation,tempEndLocation);
		blocks.setBlockStates(saveBlocks(blocks));
		saves.put(saveName, blocks);
	}
	public void updateSave(String saveName){
		BlockStore blockstore = saves.get(saveName);
		if (blockstore != null) {
			blockstore.setBlockStates(saveBlocks(blockstore));
			saves.put(saveName, blockstore);
		}
	}
	public void delSave(String saveName){
		saves.remove(saveName);
	}
	private ArrayList<BlockState> saveBlocks(BlockStore blockstore){
		ArrayList<BlockState> blockStates = new ArrayList<>();
		Location minLocation = blockstore.MinMax[0];
		Location maxLocation = blockstore.MinMax[1];
		for(int x = minLocation.getBlockX();x <= maxLocation.getBlockX(); x++){
			for (int y = minLocation.getBlockY();y <= maxLocation.getBlockY(); y++){
				for (int z = minLocation.getBlockZ(); z <= maxLocation.getBlockZ();z++){
					blockStates.add(world.getBlockAt(x, y, z).getState());
				}
			}
		}
		return blockStates;
	}
	public void undo(String saveName){
		BlockStore blockStore = saves.get(saveName);
		if (blockStore != null) {
			Iterator<BlockState> iterator = blockStore.blockStates.iterator();
			while (iterator.hasNext()) {
				iterator.next().update(true, false);
			}
		}
	}
}
