package scriptblock;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import scriptblock.listeners.PlayerInteractBlock;
import scriptblock.listeners.PlayerWalkBlock;
import scriptblock.managers.ConfigParameters;
import scriptblock.managers.ScriptManager;

public class ScriptBlock extends JavaPlugin {

	private static ScriptBlock instance ;
	
	public static Logger log = Logger.getLogger("Minecraft") ;		
	
	private ArrayList <ScriptManager> scriptManagerList = new ArrayList <ScriptManager>() ;	
	
	private PlayerInteractBlock playerInteractBlock ;
	private PlayerWalkBlock playerWalkBlock ;
	
	private PluginManager pm ;	
	private Permission perm;
	private Economy eco ;

	
	@Override
	public void onEnable() {

	//Initialization
		
		instance = this ;		
		pm = getServer().getPluginManager() ;		
		hookVault() ;
		
						
	 //Listeners Initialization...
		playerInteractBlock = new PlayerInteractBlock(this) ;
		playerWalkBlock = new PlayerWalkBlock(this) ;		
	        
	//Events Registration
		
		register(playerInteractBlock, this) ;
		register(playerWalkBlock, this) ;
							
		log.info("[ScriptBlock] Enabled !!!");	
	}
	
	@Override
	public void onDisable() 
	{		
	//Saving the Time at witch the server Shut Down.		
		
		long t = System.currentTimeMillis() ;
		
		for (ScriptManager scriptManager : scriptManagerList )
			{ scriptManager.getFileManager().saveDisabledTime(t) ; }

		instance = null ;		
		log.info("[ScriptBlock] Disabled !!!");	
	}
	
	public void register(ScriptManager scriptManager, JavaPlugin plugin)
	{
		scriptManagerList.add(scriptManager) ;
		pm.registerEvents(scriptManager, plugin);
	}
	
	
	public void hookVault()
	{
		if ( pm.isPluginEnabled("Vault") ){		//TODO remove this ?
			
		//Permission Plugin Registration	        
	        RegisteredServiceProvider < Permission > permissionProvider 
	        	= getServer().getServicesManager().getRegistration( net.milkbowl.vault.permission.Permission.class ) ;
	        
	        if (permissionProvider != null) {	        	
	            perm = permissionProvider.getProvider();
	            log.info("[ScriptBlock] "+perm.getName()+" found !");
	        }
			else {				
				log.info("[ScriptBlock] No Permissions Plugin found !");
				log.info("[ScriptBlock] Do Not use (@bypass:'group') Option !");
			}		
			
		//Economy Plugin Registration			
	        RegisteredServiceProvider < Economy > economyProvider 
	        	= getServer().getServicesManager().getRegistration( net.milkbowl.vault.economy.Economy.class ) ;
	        
	        if (economyProvider != null) {	        	
	            eco = economyProvider.getProvider();
				log.info("[ScriptBlock] "+eco.getName()+" found !");
	        }
	        
	        else {
	        	log.info("[ScriptBlock] NO Economy Plugin found !") ;
	        	log.info("[ScriptBlock] Do not use '$cost:' Option") ;
	        }
	      	
		}
		else {			
			log.info("[ScriptBlock] Vault is not Installed ! ");
			log.info("[ScriptBlock] Permission, and Economy Options won't work !") ;
		}
		
	}

	public Permission getPerm() {
		return perm;
	}

	public Economy getEco() {
		return eco;
	}

	public static ScriptBlock getInstance() {
		return instance;
	}

	public ArrayList<ScriptManager> getScriptManagerList() {
		return scriptManagerList;
	}


}
