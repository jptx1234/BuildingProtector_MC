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
		// TODO ����
		super.onEnable();
		getServer().getPluginManager().registerEvents(this, this);
		scheduler = getServer().getScheduler();
		logger = getLogger();
		logger.info("����ɹ����أ�");
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
				sender.sendMessage("����ҿ���");
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
					player.sendMessage(ChatColor.RED+"��û���趨��ʼ��ͽ�����");
					return true;
				}
				if (playerWithBlockSave.isExistSave(args[1])) {
					if (args.length != 3 || !args[2].equals("f")) {
						player.sendMessage(ChatColor.RED+"�Ѵ�����Ϊ"+args[1]+"�Ľ������գ�Ҫ���Ǵ˿��գ���ʹ��"+ChatColor.WHITE+"/bp save "+args[1]+" f");
						return true;
					}else {
						playerWithBlockSave.delSave(args[1]);
					}
				}
				
				scheduler.scheduleSyncDelayedTask(this, new Runnable() {
					
					@Override
					public void run() {
						playerWithBlockSave.createNewSave(args[1]);
						player.sendMessage(ChatColor.YELLOW+"�ѽ�������:"+args[1]);
					}
				}, 1);
				break;
			case "undo":
				if (args.length != 2) {
					return false;
				}
				if (!playersMap.containsKey(player)) {
					player.sendMessage(ChatColor.RED+"��û�н����κο���");
					return true;
				}
				PlayerWithBlock playerWithBlockUndo = playersMap.get(player);
				if (!playerWithBlockUndo.isExistSave(args[1])) {
					player.sendMessage(ChatColor.RED+"��û�д˽����ݴ�:"+args[1]);
					return true;
				}
				scheduler.scheduleSyncDelayedTask(this, new Runnable() {
					
					@Override
					public void run() {
						playerWithBlockUndo.undo(args[1]);
						player.sendMessage(ChatColor.YELLOW+"�ѻָ�����:"+args[1]);
					}
				}, 1);
				break;
			case "update":
				if (args.length != 2) {
					return false;
				}
				if (!playersMap.containsKey(player)) {
					player.sendMessage(ChatColor.RED+"��û�н����κο���");
					return true;
				}
				PlayerWithBlock playerWithBlockUpdate = playersMap.get(player);
				if (!playerWithBlockUpdate.isExistSave(args[1])) {
					player.sendMessage(ChatColor.RED+"��û�д˽����ݴ�:"+args[1]);
					return true;
				}
				scheduler.scheduleSyncDelayedTask(this, new Runnable() {
					
					@Override
					public void run() {
						playerWithBlockUpdate.updateSave(args[1]);
						player.sendMessage(ChatColor.YELLOW+"�Ѹ��¿���:"+args[1]);
					}
				}, 1);
				break;
			case "del":
				if (args.length != 2) {
					return false;
				}
				if (!playersMap.containsKey(player)) {
					player.sendMessage(ChatColor.RED+"��û�н����κο���");
					return true;
				}
				PlayerWithBlock playerWithBlockDel = playersMap.get(player);
				if (!playerWithBlockDel.isExistSave(args[1])) {
					player.sendMessage(ChatColor.RED+"��û�д˽����ݴ�:"+args[1]);
					return true;
				}
				playerWithBlockDel.delSave(args[1]);
				player.sendMessage(ChatColor.YELLOW+"��ɾ������:"+args[1]);
				break;
			case "give":
				player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
				player.sendMessage(ChatColor.YELLOW+"ʯ���ѷ������ı���");
				break;
			case "help":
				player.sendMessage(ChatColor.GOLD+"====BuildingProtector v1.0====\n"
						+ChatColor.YELLOW+ "֧��ѡ�������ڷ���Ŀ��մ������ָ������µȲ�����\n"
								+ "����ѡ�з�����ʹ��ʯ���������һ�����飬�Ҽ�������һ�����飬��������֮������߼�Ϊ��ѡ�������Խ��ߡ�\n"
								+ "�÷���"+ChatColor.WHITE+"/bp <save|undo|update|del|help> <saveName>\n"
								+ ChatColor.WHITE+"/bp save <saveName> [f]    "+ChatColor.YELLOW+"����ѡ�����������գ�������Ϊ<saveName>\n"
								+ "����Ϊ<saveName>�Ŀ����Ѵ���ʱ������"+ChatColor.WHITE+"/bp save <saveName> f"+ChatColor.YELLOW+" ǿ�Ƹ���\n"
								+ ChatColor.WHITE+"/bp undo <saveName>      "+ChatColor.YELLOW+"��ԭ��Ϊ<saveName>�Ŀ���\n"
								+ ChatColor.WHITE+"/bp update <saveName>    "+ChatColor.YELLOW+"��֮ǰ�������Ϊ<saveName>�Ŀ��ո�������ǰ״̬\n"
								+ ChatColor.WHITE+"/bp del <saveName>       "+ChatColor.YELLOW+"ɾ����Ϊ<saveName>�Ŀ���\n"
								+ ChatColor.WHITE+"/bp give                "+ChatColor.YELLOW+"�����һ��ʯ��\n"
								+ ChatColor.WHITE+"/bp help                "+ChatColor.YELLOW+"��ʾ�˰�����Ϣ");
				break;
				
			default:
				return false;
			}
			return true;
		}
		return false;
		
	}
	
	
	
	//����ʼ��
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
			player.sendMessage(ChatColor.YELLOW+"��������ʼ�����趨:"+ChatColor.WHITE+location2String(startLocation));
		}
	}
	
	//���������
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
			player.sendMessage(ChatColor.YELLOW+"����������������趨:"+ChatColor.WHITE+location2String(endLocation));
		}
	}
	
	public String location2String(Location location){
		return "["+location.getBlockX()+", "+location.getBlockY()+", "+location.getBlockZ()+"]";
	}
}
