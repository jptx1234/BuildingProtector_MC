package com.github.jptx1234.BuildingProtector;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class BuildingProtector extends JavaPlugin implements Listener{
	Logger logger;
	BukkitScheduler scheduler;
	final String pointerTool = "STONE_AXE";
	HashMap<Player, PlayerWithBlock> playersMap = new HashMap<>();
	
	
	
	@Override
	public void onEnable() {
		// TODO 载入
		super.onEnable();
		getServer().getPluginManager().registerEvents(this, this);
		scheduler = getServer().getScheduler();
		logger = getLogger();
		logger.info("插件成功加载！");
	}
	
	public void loadYML(){
		
	}
	
	public void saveYML(){
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (command.getName().equalsIgnoreCase("bp")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("仅玩家可用");
				return true;
			}
			if (args.length == 0) {
				return false;
			}
			Player player = (Player) sender;
			switch (args[0]) {
			case "save":
				if (args.length < 2 || args.length >3) {
					return false;
				}
				PlayerWithBlock playerWithBlockSave = playersMap.get(player);
				if (playerWithBlockSave == null) {
					player.sendMessage(ChatColor.RED+"还没有设定开始点和结束点");
					return true;
				}
				if (playerWithBlockSave.isExistSave(args[1])) {
					if (args.length != 3 || !args[2].equals("f")) {
						player.sendMessage(ChatColor.RED+"已存在名为"+args[1]+"的建筑快照，要覆盖此快照，请使用"+ChatColor.WHITE+"/bp save "+args[1]+" f");
						return true;
					}else {
						playerWithBlockSave.delSave(args[1]);
					}
				}
				
				scheduler.scheduleSyncDelayedTask(this, new Runnable() {
					
					@Override
					public void run() {
						playerWithBlockSave.createNewSave(args[1]);
						player.sendMessage(ChatColor.YELLOW+"已建立快照:"+args[1]);
					}
				}, 1);
				break;
			case "undo":
				if (args.length != 2) {
					return false;
				}
				if (!playersMap.containsKey(player)) {
					player.sendMessage(ChatColor.RED+"您没有建立任何快照");
					return true;
				}
				PlayerWithBlock playerWithBlockUndo = playersMap.get(player);
				if (!playerWithBlockUndo.isExistSave(args[1])) {
					player.sendMessage(ChatColor.RED+"您没有此建筑暂存:"+args[1]);
					return true;
				}
				scheduler.scheduleSyncDelayedTask(this, new Runnable() {
					
					@Override
					public void run() {
						playerWithBlockUndo.undo(args[1]);
						player.sendMessage(ChatColor.YELLOW+"已恢复快照:"+args[1]);
					}
				}, 1);
				break;
			case "update":
				if (args.length != 2) {
					return false;
				}
				if (!playersMap.containsKey(player)) {
					player.sendMessage(ChatColor.RED+"您没有建立任何快照");
					return true;
				}
				PlayerWithBlock playerWithBlockUpdate = playersMap.get(player);
				if (!playerWithBlockUpdate.isExistSave(args[1])) {
					player.sendMessage(ChatColor.RED+"您没有此建筑暂存:"+args[1]);
					return true;
				}
				scheduler.scheduleSyncDelayedTask(this, new Runnable() {
					
					@Override
					public void run() {
						playerWithBlockUpdate.updateSave(args[1]);
						player.sendMessage(ChatColor.YELLOW+"已更新快照:"+args[1]);
					}
				}, 1);
				break;
			case "del":
				if (args.length != 2) {
					return false;
				}
				if (!playersMap.containsKey(player)) {
					player.sendMessage(ChatColor.RED+"您没有建立任何快照");
					return true;
				}
				PlayerWithBlock playerWithBlockDel = playersMap.get(player);
				if (!playerWithBlockDel.isExistSave(args[1])) {
					player.sendMessage(ChatColor.RED+"您没有此建筑暂存:"+args[1]);
					return true;
				}
				playerWithBlockDel.delSave(args[1]);
				player.sendMessage(ChatColor.YELLOW+"已删除快照:"+args[1]);
				break;
			case "give":
				player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
				player.sendMessage(ChatColor.YELLOW+"石斧已放入您的背包");
				break;
			case "help":
				player.sendMessage(ChatColor.GOLD+"====BuildingProtector v1.0====\n"
						+ChatColor.YELLOW+ "支持选中区域内方块的快照创建、恢复、更新等操作。\n"
								+ "区域选中方法：使用石斧左键单击一个方块，右键单击另一个方块，两个方块之间的连线即为所选区域的体对角线。\n"
								+ "用法："+ChatColor.WHITE+"/bp <save|undo|update|del|help> <saveName>\n"
								+ ChatColor.WHITE+"/bp save <saveName> [f]    "+ChatColor.YELLOW+"将所选区域制作快照，快照名为<saveName>\n"
								+ "当名为<saveName>的快照已存在时，可用"+ChatColor.WHITE+"/bp save <saveName> f"+ChatColor.YELLOW+" 强制覆盖\n"
								+ ChatColor.WHITE+"/bp undo <saveName>      "+ChatColor.YELLOW+"还原名为<saveName>的快照\n"
								+ ChatColor.WHITE+"/bp update <saveName>    "+ChatColor.YELLOW+"将之前保存的名为<saveName>的快照更新至当前状态\n"
								+ ChatColor.WHITE+"/bp del <saveName>       "+ChatColor.YELLOW+"删除名为<saveName>的快照\n"
								+ ChatColor.WHITE+"/bp give                "+ChatColor.YELLOW+"给玩家一个石斧\n"
								+ ChatColor.WHITE+"/bp help                "+ChatColor.YELLOW+"显示此帮助信息");
				break;
				
			default:
				return false;
			}
			return true;
		}
		return false;
		
	}
	
	
	
	//区域开始点
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		if (player.getItemInHand().getType().name().equals(pointerTool)) {
			Block block = e.getBlock();
			final BlockState state = block.getState();
			Location startLocation = block.getLocation();
			PlayerWithBlock playerWithBlock;
			if (!playersMap.containsKey(player)) {
				playerWithBlock = new PlayerWithBlock(player);
				playersMap.put(player, playerWithBlock);
			}else {
				playerWithBlock = playersMap.get(player);
			}
			playerWithBlock.setTempStartLocation(startLocation);
			scheduler.scheduleSyncDelayedTask(this, new Runnable() {
				
				@Override
				public void run() {
					String.valueOf(state.update(true, false));
				}
			}, 1);
			player.sendMessage(ChatColor.YELLOW+"保护区域开始点已设定:"+ChatColor.WHITE+location2String(startLocation));
		}
	}
	
	//区域结束点
	@EventHandler
	public void onRightClick(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if (player.getItemInHand().getType().name().equals(pointerTool) && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Location endLocation = e.getClickedBlock().getLocation();
			PlayerWithBlock playerWithBlock = playersMap.get(player);
			if (playerWithBlock == null) {
				playerWithBlock = new PlayerWithBlock(player);
				playersMap.put(player, playerWithBlock);
			}
			playerWithBlock.setTempEndLocation(endLocation);
			player.sendMessage(ChatColor.YELLOW+"保护区域结束点已设定:"+ChatColor.WHITE+location2String(endLocation));
		}
	}
	
	public String location2String(Location location){
		return "["+location.getBlockX()+", "+location.getBlockY()+", "+location.getBlockZ()+"]";
	}
}
