package scriptblock.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.command.BindScript;
import scriptblock.managers.ScriptManager;
import scriptblock.options.OptionHandler;

public class PlayerInteractBlock extends ScriptManager implements Listener {
   private Player player;
   private BlockCoords blockCoords;

   public PlayerInteractBlock(ScriptBlock scriptBlock) {
      super(scriptBlock, "interact", "sb");
   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      this.player = event.getPlayer();
      Block block;
      
      if(event.hasBlock()) {
    	 block = event.getClickedBlock();
         String world = this.player.getWorld().getName();
         int x = block.getX();
         int y = block.getY();
         int z = block.getZ();
         this.blockCoords = new BlockCoords(world, x, y, z);
         String playerName = this.player.getName();
         BindScript blockInteract = (BindScript)this.mapManager.commandsWaitingMap.get(playerName);
         if(blockInteract != null) {
            if(blockInteract.onEvent(this.blockCoords)) {
               this.mapManager.commandsWaitingMap.remove(playerName);
               this.log.info("[" + this.plugin.getName() + "] " + playerName + " bound \"" + blockInteract.getCommandType().name() + "\" " + this.getName() + " Script...");
               this.log.info("[" + this.plugin.getName() + "] at " + this.blockCoords.getFullCoords());
               if(event.isBlockInHand()){
            	   event.setUseItemInHand(Result.DENY);
               }
            }
         } else if(this.mapManager.blocksMap.containsKey(this.blockCoords.getFullCoords()) && this.permManager.hasSBPerm(this.player, "use", true)) {
            if(this.haveCoolDown(this.player, this.blockCoords) || this.isDelayed(this.player, this.blockCoords)) {
               return;
            }

            this.log.info("[" + this.plugin.getName() + "] " + playerName + " triggered " + this.getName() + " ScriptBlock...");
            this.log.info("[" + this.plugin.getName() + "] at coords " + this.blockCoords.getFullCoords());
            OptionHandler read = new OptionHandler(this, this.player, this.blockCoords);
            read.readScript(0);
            if(event.isBlockInHand()){
            	event.setUseItemInHand(Result.DENY);
            }
         }
      }

   }
}
