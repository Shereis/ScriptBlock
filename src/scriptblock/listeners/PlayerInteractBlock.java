package scriptblock.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.command.BindScript;
import scriptblock.managers.ScriptManager;
import scriptblock.options.OptionHandler;

public class PlayerInteractBlock extends ScriptManager implements Listener{

	public PlayerInteractBlock(ScriptBlock scriptBlock) 
		{ super(scriptBlock, "interact","sb"); }
	
	private Player player ;
	private BlockCoords blockCoords ;	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{		
		player = event.getPlayer();		
		Block block = event.getClickedBlock();
		
	//Getting the Block Coordinates.		
		if ( event.hasBlock() )
		{
			String world = player.getWorld().getName() ;
		
			int x = block.getX();
			int y = block.getY();
			int z = block.getZ();
			
			blockCoords = new BlockCoords(world, x, y, z) ;
			String playerName = player.getName() ;
			
			BindScript blockInteract = mapManager.commandsWaitingMap.get( playerName ) ;
			
			if ( blockInteract != null ) {			
				if ( blockInteract.onEvent( blockCoords ) ) 
				{
					mapManager.commandsWaitingMap.remove(playerName) ;
					log.info("["+plugin.getName()+"] "+playerName+" bound \""+blockInteract.getCommandType().name()+"\" "+getName()+" Script..." ) ;
					log.info("["+plugin.getName()+"] at "+blockCoords.getFullCoords() ) ;
				}
			}
			
			else if ( mapManager.blocksMap.containsKey( blockCoords.getFullCoords() ) && permManager.hasSBPerm(player, "use", true) ) 
			{				
				if ( haveCoolDown(player, blockCoords) || isDelayed(player, blockCoords) ) return ;
				
				else {
					
					log.info("["+plugin.getName()+"] "+playerName+" triggered "+getName()+" ScriptBlock..." ) ;
					log.info("["+plugin.getName()+"] at coords "+blockCoords.getFullCoords() ) ;
					
					OptionHandler read = new OptionHandler(this, player, blockCoords) ; 
					read.readScript(0) ;
				}			
			}			
		}	
	}

}
