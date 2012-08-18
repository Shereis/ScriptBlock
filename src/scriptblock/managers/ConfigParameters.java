package scriptblock.managers;

import org.bukkit.util.config.Configuration;

import scriptblock.options.Option;

@SuppressWarnings("deprecation")
public class ConfigParameters {

	private final String showConsole = "ShowConsole" ;
	private final String sep = "." ;
	
	private ScriptManager scriptManager;
	private MapManager mapManager;
	
	private FileManager fileManager;
	private Configuration config;
	

	public ConfigParameters(ScriptManager scriptManager)
	{ 
		this.scriptManager = scriptManager ; 
		this.mapManager = scriptManager.mapManager ;
	}
		
	public void init(FileManager fileManager) 
	{
		this.fileManager = fileManager ;
		this.config = fileManager.getConfig() ;
	}
	
	public void getPropFromConfig()
	{
		for (Option option : mapManager.optionsList )
		{
			boolean debugMode = config.getBoolean(showConsole+sep+option.getName(), false) ;
			option.setDebugMode(debugMode) ;
		}
	}
	
	public void setDefaultsProps()
	{
		for (Option option : mapManager.optionsList )
		{
			config.setProperty(showConsole+sep+option.getName(), false) ;
		}
	}

}
