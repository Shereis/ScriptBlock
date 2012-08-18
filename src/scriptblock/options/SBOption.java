package scriptblock.options;

import java.util.LinkedList;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.managers.FileManager;
import scriptblock.managers.MapManager;
import scriptblock.managers.PermManager;
import scriptblock.managers.ScriptManager;

@SuppressWarnings("deprecation")
public abstract class SBOption extends Option {
		
	protected Logger log = ScriptBlock.log ;
	protected ScriptBlock scriptBlock;
	protected JavaPlugin plugin;
	
	protected ScriptManager scriptManager;
	protected OptionHandler optionHandler;
		
	protected PermManager permManager;
	protected FileManager fileManager;
	protected MapManager mapManager;
	
	protected Configuration config;

	protected Permission vaultPerm;
	protected Economy eco;
	
	protected Player player ;
	protected BlockCoords blockCoords;
	protected LinkedList<String> script;
	protected String scriptLine;
	protected int index ;
	
	
	public SBOption(ScriptManager scriptManager, String name, String syntax) 
	{
		super(name, syntax);

		this.scriptBlock = ScriptBlock.getInstance() ;
		this.scriptManager = scriptManager ;
				
		this.plugin = scriptManager.getPlugin() ;
		this.fileManager = scriptManager.getFileManager() ;
		this.mapManager = scriptManager.getMapManager() ;
		this.permManager = scriptManager.getPermManager() ;
		this.vaultPerm = scriptBlock.getPerm() ;
		this.eco = scriptBlock.getEco() ;
		this.config = fileManager.getScriptConfig() ;	
	}

	@Override
	public boolean onOptionCall(OptionHandler optionHandler) 
	{
		this.optionHandler = optionHandler ;
		
		player = optionHandler.getPlayer() ;
		blockCoords = optionHandler.getBlockCoords() ;
		script = optionHandler.getScript() ;
		scriptLine = optionHandler.getScriptLine() ;
		index = optionHandler.getScriptLineIndex() ;
		
		return isValid(); 
	}
	
	public abstract boolean isValid() ;

}
