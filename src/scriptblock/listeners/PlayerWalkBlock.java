package scriptblock.listeners;

import java.util.Hashtable;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.command.BindScript;
import scriptblock.managers.ScriptManager;
import scriptblock.options.OptionHandler;

public class PlayerWalkBlock extends ScriptManager implements Listener  {

	private Map <String, String> oldBlockCoords = new Hashtable<String, String>() ;
	
	private Player player ;
	private BlockCoords blockCoords ;
	
	public PlayerWalkBlock(ScriptBlock scriptBlock) 
		{ super(scriptBlock, "walk", "sb" ); }
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		player = event.getPlayer();	
		Block block = event.getClickedBlock();
				
		if ( event.hasBlock() )
		{
			String playerName = player.getName() ;
			BindScript blockInteract = mapManager.commandsWaitingMap.get( playerName ) ;
			
			if ( blockInteract != null ) {
				String world = player.getWorld().getName() ;
					
				int x = block.getX();
				int y = block.getY();
				int z = block.getZ();
				
				blockCoords = new BlockCoords(world, x, y, z) ;								
			
				if ( blockInteract.onEvent( blockCoords ) ) 
				{ 
					mapManager.commandsWaitingMap.remove(playerName) ; 
					log.info("["+plugin.getName()+"] "+playerName+" bound \""+blockInteract.getCommandType().name()+"\" "+getName()+" Script..." ) ;
					log.info("["+plugin.getName()+"] at "+blockCoords.getFullCoords() ) ;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{		
		player = event.getPlayer() ;
		
		World world = player.getWorld() ;
		int X = player.getLocation().getBlockX() ;
		int Y = player.getLocation().getBlockY() - 1;
		int Z = player.getLocation().getBlockZ() ;
		
		
		
		if (world.getBlockAt(X, Y, Z).getTypeId() != 0)
		{	
			String playerName = player.getName() ;
			String worldName = world.getName() ;
					
			blockCoords = new BlockCoords(worldName, X, Y, Z);
						
			if (!oldBlockCoords.containsKey(playerName) || !oldBlockCoords.get(playerName).equals( blockCoords.getFullCoords() ))
			{
				oldBlockCoords.put(playerName, blockCoords.getFullCoords());				
				if ( mapManager.blocksMap.containsKey( blockCoords.getFullCoords() ) && permManager.hasSBPerm(player, "use", false) ) 
				{
					if ( haveCoolDown(player, blockCoords) || isDelayed(player, blockCoords) ) return ;
					
					log.info("["+plugin.getName()+"] "+playerName+" triggered "+getName()+" ScriptBlock..." ) ;
					log.info("["+plugin.getName()+"] at coords "+blockCoords.getFullCoords() ) ;
					
					OptionHandler read = new OptionHandler(this, player, blockCoords) ;
					read.readScript(0) ;
				}
			}
		}		
	}	

}
