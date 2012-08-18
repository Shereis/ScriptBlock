package scriptblock.options;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.command.CommandCreate;
import scriptblock.managers.MapManager;
import scriptblock.managers.ScriptManager;

public class OptionHandler {
	
	protected ScriptBlock scriptBlock = ScriptBlock.getInstance() ;
	protected Logger log = ScriptBlock.log ;
	
	private ScriptManager scriptManager;
	private JavaPlugin plugin;	
	
	private LinkedList <String> script;
	
	private BlockCoords blockCoords ;
	private String scriptLine;
	private Player player ;
	private int scriptLineIndex;
	
	private MapManager mapManager;
		
	
	public OptionHandler(ScriptManager scriptManager, Player player, BlockCoords blockCoords)
	{
		this.scriptManager = scriptManager ;
		this.plugin = scriptManager.getPlugin() ;
		this.mapManager = scriptManager.getMapManager() ;
		this.blockCoords = blockCoords ;
		this.script = mapManager.blocksMap.get( blockCoords.getFullCoords() ) ;
		this.player = player ;
	}
	
	public enum TextOpt {
		
		PLAYER("<player>"),
		ITEM_NAME("<itemName>"),
		ITEM_ID("<itemID>");
				
		public final String value ;
		
		private TextOpt(String value)
			{this.value = value ; }
	}
	
	
	public void readScript(int index)
	{	
		scriptLineIndex = index ;
		
		for (; scriptLineIndex < script.size() ; scriptLineIndex++) 
		{	
			boolean haveOption = false ;			
			scriptLine = script.get(scriptLineIndex) ;
			
			if (scriptLineIndex == 0 && script.get(scriptLineIndex).startsWith( CommandCreate.authorNode ) ) continue ; 
			
			for ( TextOpt textOpt : TextOpt.values() )
			{
				if ( scriptLine.contains(textOpt.value) )
				{
					switch(textOpt)
					{
						case PLAYER :
							
							scriptLine = scriptLine.replaceAll(TextOpt.PLAYER.value, player.getName() ) ;
							break ;
							
						case ITEM_NAME :
						{
							ItemStack itemStack = player.getItemInHand() ;
							if ( itemStack != null )
							{
								Material material = itemStack.getType() ;
								scriptLine = scriptLine.replaceAll(TextOpt.ITEM_NAME.value, material.name() ) ;
							}
							else {scriptLine = scriptLine.replaceAll(TextOpt.ITEM_NAME.value, "NoName" ) ; }
							
							break ;
						}
							
						case ITEM_ID :
						{
							ItemStack itemStack = player.getItemInHand() ;
							if ( itemStack != null )
							{
								Material material = itemStack.getType() ;
								scriptLine = scriptLine.replaceAll( TextOpt.ITEM_ID.value, String.valueOf( material.getId() )  ) ;
							}
							else { scriptLine = scriptLine.replaceAll( TextOpt.ITEM_ID.value, "0" ) ; }
							
							break ;
						}
						
						
					}
				}
			}
						
			for (Option option : mapManager.optionsList ) 
			{					
				if (scriptLine.startsWith( option.getSyntax() ) )
				{					
					if ( !option.onOptionCall( this ) ) return  ;
					
					haveOption = true ;
					break ;					
				} 
			}	
			
			if (!haveOption) {				
				player.sendMessage(ChatColor.RED+"["+plugin.getName()+"] BAD SCRIPT !");
				return ;					
			}
													
		}		
	}


	public LinkedList<String> getScript() 
		{ return script; }


	public BlockCoords getBlockCoords() 
		{ return blockCoords; }


	public String getScriptLine() 
		{ return scriptLine; }


	public Player getPlayer() 
		{ return player; }


	public ScriptManager getScriptManager() 
		{ return scriptManager; }


	public int getScriptLineIndex() 
		{ return scriptLineIndex; }
}
