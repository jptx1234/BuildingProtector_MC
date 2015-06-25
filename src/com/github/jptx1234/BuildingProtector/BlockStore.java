package com.github.jptx1234.BuildingProtector;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;

public class BlockStore{
	ArrayList<BlockState> blockStates;
	Location startLocation;
	Location endLocation;
	Location[] MinMax;		//0为最小点，1为最大点
	World world;
	public BlockStore(World world,Location startLocation,Location endLocation) {
		this.world = world;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.MinMax = sortLocation(startLocation, endLocation);
	}
	public void setBlockStates(ArrayList<BlockState> blockStates){
		this.blockStates = blockStates;
	}
	public Location[] sortLocation(Location L1,Location L2){
		int minX,maxX,minY,maxY,minZ,maxZ;
		if (L1.getBlockX() < L2.getBlockX()) {
			minX = L1.getBlockX();
			maxX = L2.getBlockX();
		}else {
			minX = L2.getBlockX();
			maxX = L1.getBlockX();
		}
		if (L1.getBlockY() < L2.getBlockY()) {
			minY = L1.getBlockY();
			maxY = L2.getBlockY();
		}else {
			minY = L2.getBlockY();
			maxY = L1.getBlockY();
		}
		if (L1.getBlockZ() < L2.getBlockZ()) {
			minZ = L1.getBlockZ();
			maxZ = L2.getBlockZ();
		}else {
			minZ = L2.getBlockZ();
			maxZ = L1.getBlockZ();
		}
		Location minLocation = new Location(world, minX, minY, minZ);
		Location maxLocation = new Location(world, maxX, maxY, maxZ);
		return new Location[]{minLocation,maxLocation};
	}
}
