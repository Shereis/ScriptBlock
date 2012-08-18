package scriptblock.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import scriptblock.SLAPI;
import scriptblock.ScriptBlock;

@SuppressWarnings("deprecation")
public class FileManager {
	
	private JavaPlugin plugin ;
	
	private File scriptBlockFolder ;
	private File scriptDataFolder ;
	
	private Logger log = ScriptBlock.log ;
	
	private MapManager mapManager;
	private ConfigParameters configParam;
	private File configFile ;
	private File scriptDataFile;
	private File cooldownDataFile ;
	private Configuration config ;
	private Configuration scriptConfig ;

	/* TODO Remove disabled Time on cooldowns ! and recode it.
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	

	public FileManager(JavaPlugin plugin, MapManager mapManager ,ConfigParameters configParam,String name)
	{	
		this.plugin = plugin ;
		
//		if ( ScriptBlockFolder == null && scriptDataFolder == null)
		{
			scriptBlockFolder = plugin.getDataFolder() ;
			scriptDataFolder = new File( scriptBlockFolder+"/BlocksData" ) ;
			scriptDataFolder.mkdirs() ;
		}
		
		configFile = new File( scriptBlockFolder+File.separator+name+"_Config.yml") ;
		scriptDataFile = new File ( scriptDataFolder.getPath()+File.separator+name+"_Scripts.yml" ) ;
		cooldownDataFile = new File ( scriptDataFolder.getPath()+File.separator+name+"_Cooldowns.dat" );
		config = new Configuration(configFile) ;
		scriptConfig = new Configuration(scriptDataFile) ;
		
		this.mapManager = mapManager ;
		this.configParam = configParam ;
		
	}
	
	public void init()
	{
		loadConfigFile() ;
		loadScriptFile() ;
		loadCooldownFile() ;
	}
	
	public void loadScriptFile()
	{		
	// Checking if "scriptData".yml Save File content is null, and loading it if not.
		
		if ( scriptDataFile.exists() ){
			if ( scriptDataFile.length() > 0 ){
				
			//Loading Existent BlockMap.

				scriptConfig.load();
				
				for (String world : scriptConfig.getKeys()) {
					for (String blockCoords : scriptConfig.getKeys(world+".") ) 
					{						
						List <Object> macroList = scriptConfig.getList(world+"."+blockCoords+".") ;
						LinkedList<String> commandList = new LinkedList<String>() ;
						
						for( int i = 0; i < macroList.size() ; i++ )
							{ commandList.add( String.valueOf( macroList.get(i) ) ); }
						
						mapManager.blocksMap.put(world+","+blockCoords, commandList ) ;
					}
				}
				
				log.info("["+plugin.getName()+"] "+scriptDataFile.getName()+" loaded !");
			}
			else { log.info("["+plugin.getName()+"] "+scriptDataFile.getName()+" is empty, loading aborded !"); }
		}
		else {
			
			try { scriptDataFile.createNewFile() ; }
			catch (IOException e) { e.printStackTrace(); }
			
			log.info("["+plugin.getName()+"] "+scriptDataFile.getName()+" created !") ;					
		}

	}
	
	@SuppressWarnings("unchecked")
	public void loadCooldownFile()
	{
	// Checking if CoolDownData.dat Save File content is null, and loading it if not.
		
		if ( cooldownDataFile.exists() ){
			if ( cooldownDataFile.length() > 0 ) {
				
			//loading cooldownBlockMap			
				try {
					
					mapManager.cooldownMap = ( HashMap<String, long[]> ) SLAPI.load ( cooldownDataFile.getPath() );
					log.info("["+plugin.getName()+"] "+cooldownDataFile.getName()+" File Loaded !!!");
					
				} catch (Exception e) {
					
					log.info("["+plugin.getName()+"] [ERROR] while loading "+cooldownDataFile.getName()+" File !");
					log.info("["+plugin.getName()+"] at "+e.getMessage());
				}
				
			//Cooldowns Update.
				long[] disabledTime = new long[1] ;
				
				if (mapManager.cooldownMap != null){
					
					disabledTime = mapManager.cooldownMap.get("disabledTime");
					
					for (String key : mapManager.cooldownMap.keySet()){
						if ( !key.equals("disabledTime") ){
							
							long[] cooldownParams = mapManager.cooldownMap.get(key);
							cooldownParams[2] = cooldownParams[2] + (System.currentTimeMillis() - disabledTime[0] );
							mapManager.cooldownMap.put(key, cooldownParams);
						}
					}
				}
			}
			
			else { log.info("["+plugin.getName()+"] "+cooldownDataFile.getName()+" is empty, loading aborded !"); }			
		}
		else {
			
			try { cooldownDataFile.createNewFile() ; }
			catch (IOException e) { e.printStackTrace(); }
			
			log.info("["+plugin.getName()+"] "+cooldownDataFile.getName()+" created !") ;
		}	
	}
	
	public void loadConfigFile()
	{
		configParam.init(this) ;
		
		if ( !configFile.exists() )
		{
			try { configFile.createNewFile() ; }
			catch (IOException e) { e.printStackTrace(); }
			
			configParam.setDefaultsProps() ;
			config.save() ;
			log.info("["+plugin.getName()+"] "+configFile.getName()+" created !") ;
		}
		else {			
			config.load() ;			
			configParam.getPropFromConfig() ;
			
			log.info("["+plugin.getName()+"] "+configFile.getName()+" loaded !") ;
		}
	}
	
	public void saveDisabledTime(long time)
	{
		String DisabledTimeKey = "disabledTime";
		long[] disabledTime = new long[1] ;
		
		disabledTime[0] = time ;
		mapManager.cooldownMap.put(DisabledTimeKey, disabledTime);
		
		try { SLAPI.save( mapManager.cooldownMap , cooldownDataFile.getPath() ); }			
		catch (Exception e) {				
			log.info("["+plugin.getName()+"] [ERROR] while saving the DisabledTime to "+cooldownDataFile.getName()+" !");
			log.info("["+plugin.getName()+"] at "+ e.getMessage());
		}			
		log.info("["+plugin.getName()+"] Disabled Time Saved to "+cooldownDataFile.getName()+" !!!");
	}
	
	
	/** This method allow you to Merge yml Save to the desired Map. */
	
	public static void YMLtoMap(Configuration config, HashMap <String, LinkedList <String>> BlocksMap)
	{			
		config.load();
		
		for (String world : config.getKeys()){
			for (String blockCoords : config.getKeys(world+".") ){
				
				List <Object> macroList = config.getList(world+"."+blockCoords+".") ;
				LinkedList<String> commandList = new LinkedList<String>() ;
				
				for( int i = 0; i < macroList.size() ; i++ )
					{ commandList.add( String.valueOf( macroList.get(i) ) ); }
				
				BlocksMap.put(world+","+blockCoords, commandList ) ;
			}
		}
	}

	
	
	public MapManager getMapManager() 
		{ return mapManager; }

	public Configuration getScriptConfig() 
		{ return scriptConfig; }

	public File getScriptDataFile() 
		{ return scriptDataFile; }

	public File getCooldownDataFile() 
		{ return cooldownDataFile; }

	public Configuration getConfig() 
		{ return config; }

	public File getConfigFile() 
		{ return configFile; }
	
}
