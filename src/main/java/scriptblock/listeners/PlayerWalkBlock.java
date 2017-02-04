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

public class PlayerWalkBlock extends ScriptManager implements Listener {
   private Map oldBlockCoords = new Hashtable();
   private Player player;
   private BlockCoords blockCoords;

   public PlayerWalkBlock(ScriptBlock scriptBlock) {
      super(scriptBlock, "walk", "sb");
   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      this.player = event.getPlayer();
      Block block = event.getClickedBlock();
      if(event.hasBlock()) {
         String playerName = this.player.getName();
         BindScript blockInteract = (BindScript)this.mapManager.commandsWaitingMap.get(playerName);
         if(blockInteract != null) {
            String world = this.player.getWorld().getName();
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            this.blockCoords = new BlockCoords(world, x, y, z);
            if(blockInteract.onEvent(this.blockCoords)) {
               this.mapManager.commandsWaitingMap.remove(playerName);
               this.log.info("[" + this.plugin.getName() + "] " + playerName + " bound \"" + blockInteract.getCommandType().name() + "\" " + this.getName() + " Script...");
               this.log.info("[" + this.plugin.getName() + "] at " + this.blockCoords.getFullCoords());
            }
         }
      }

   }

   @EventHandler
   public void onPlayerMove(PlayerMoveEvent event) {
      this.player = event.getPlayer();
      World world = this.player.getWorld();
      int X = this.player.getLocation().getBlockX();
      int Y = this.player.getLocation().getBlockY() - 1;
      int Z = this.player.getLocation().getBlockZ();
      if(world.getBlockAt(X, Y, Z).getTypeId() != 0) {
         String playerName = this.player.getName();
         String worldName = world.getName();
         this.blockCoords = new BlockCoords(worldName, X, Y, Z);
         if(!this.oldBlockCoords.containsKey(playerName) || !((String)this.oldBlockCoords.get(playerName)).equals(this.blockCoords.getFullCoords())) {
            this.oldBlockCoords.put(playerName, this.blockCoords.getFullCoords());
            if(this.mapManager.blocksMap.containsKey(this.blockCoords.getFullCoords()) && this.permManager.hasSBPerm(this.player, "use", false)) {
               if(this.haveCoolDown(this.player, this.blockCoords) || this.isDelayed(this.player, this.blockCoords)) {
                  return;
               }

               this.log.info("[" + this.plugin.getName() + "] " + playerName + " triggered " + this.getName() + " ScriptBlock...");
               this.log.info("[" + this.plugin.getName() + "] at coords " + this.blockCoords.getFullCoords());
               OptionHandler read = new OptionHandler(this, this.player, this.blockCoords);
               read.readScript(0);
            }
         }
      }

   }
}
