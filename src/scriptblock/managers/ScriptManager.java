package scriptblock.managers;


import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.command.CommandHandler;
import scriptblock.options.OptionManager;

public abstract class ScriptManager implements Listener {
	
	protected ScriptBlock scriptBlock = ScriptBlock.getInstance() ;
	protected Logger log = ScriptBlock.log ;
	
	protected JavaPlugin plugin;
	
	private String name ;
	
	protected ConfigParameters configParam ;
	protected MapManager mapManager ;
	protected FileManager fileManager ;
	protected PermManager permManager ;
	protected OptionManager optionManager;
	
	private CommandHandler commandHandler ;
	
	public ScriptManager(JavaPlugin plugin, String eventName,String cmdName) 
	{  
		this.name = eventName ;
		this.plugin = plugin ;
		this.permManager = new PermManager(scriptBlock.getPerm(), this.name , plugin ) ;
		this.mapManager = new MapManager() ;
		this.optionManager = new OptionManager(this) ;
		this.configParam = new ConfigParameters(this) ;
		this.fileManager = new FileManager(plugin, mapManager, configParam ,eventName) ;		
		this.commandHandler = new CommandHandler(this, cmdName) ;
		init() ;
	}
	
	protected void init()
	{
		optionManager.registerDefaultOptions() ;
		fileManager.init() ;		
	}
	
	protected boolean haveCoolDown(Player player, BlockCoords blockCoords)
	{
		long[] cooldownParams = mapManager.cooldownMap.get( blockCoords.getFullCoords() );
		
		if ( cooldownParams != null ) {						
			if ( cooldownParams[2] > System.currentTimeMillis() ) {
				
				int timeRemain = (int) ((cooldownParams[2] - System.currentTimeMillis()) /1000) ;
				
				short H = (short) (timeRemain / 3600) ;
				byte mins = (byte) ( (timeRemain % 3600) / 60);
				byte secs =  (byte) ( (timeRemain % 3600) % 60) ;
				
				player.sendMessage(ChatColor.RED+"["+plugin.getName()+"] You must wait "+H+" H "+mins+" mins "+secs+" secs...");
				player.sendMessage(ChatColor.RED+"["+plugin.getName()+"] to activate this again !");
				
				return true;
			} 
		}
		return false ;
	}
	
	protected boolean isDelayed(Player player,BlockCoords blockCoords)
	{
		if (  mapManager.delayList.contains( blockCoords.getFullCoords() )  ) { 
			
			player.sendMessage(ChatColor.RED+"["+plugin.getName()+"] You must wait to activate this again...");
			return true ; 												
		}
		else {	return false ;	}
	}

	public String getName() 
		{ return name; }

	public FileManager getFileManager() 
		{ return fileManager; }

	public MapManager getMapManager() 
		{ return mapManager; }

	public PermManager getPermManager() 
		{ return permManager; }
	
	public JavaPlugin getPlugin() 
		{ return plugin; }

	public OptionManager getOptionManager() 
		{ return optionManager; }

	public ConfigParameters getConfigParam() 
		{ return configParam; }
}

