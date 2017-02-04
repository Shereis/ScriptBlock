package scriptblock.options;

import java.util.Iterator;
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
import scriptblock.options.Option;

public class OptionHandler {
   protected ScriptBlock scriptBlock = ScriptBlock.getInstance();
   protected Logger log;
   private ScriptManager scriptManager;
   private JavaPlugin plugin;
   private LinkedList script;
   private BlockCoords blockCoords;
   private String scriptLine;
   private Player player;
   private int scriptLineIndex;
   private MapManager mapManager;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$scriptblock$options$OptionHandler$TextOpt;

   public OptionHandler(ScriptManager scriptManager, Player player, BlockCoords blockCoords) {
      this.log = ScriptBlock.log;
      this.scriptManager = scriptManager;
      this.plugin = scriptManager.getPlugin();
      this.mapManager = scriptManager.getMapManager();
      this.blockCoords = blockCoords;
      this.script = (LinkedList)this.mapManager.blocksMap.get(blockCoords.getFullCoords());
      this.player = player;
   }

   public void readScript(int index) {
      for(this.scriptLineIndex = index; this.scriptLineIndex < this.script.size(); ++this.scriptLineIndex) {
         boolean haveOption = false;
         this.scriptLine = (String)this.script.get(this.scriptLineIndex);
         if(this.scriptLineIndex != 0 || !((String)this.script.get(this.scriptLineIndex)).startsWith(CommandCreate.authorNode)) {
            OptionHandler.TextOpt[] var6;
            int var5 = (var6 = OptionHandler.TextOpt.values()).length;

            for(int var4 = 0; var4 < var5; ++var4) {
               OptionHandler.TextOpt option = var6[var4];
               if(this.scriptLine.contains(option.value)) {
                  ItemStack itemStack;
                  Material material;
                  switch($SWITCH_TABLE$scriptblock$options$OptionHandler$TextOpt()[option.ordinal()]) {
                  case 1:
                     this.scriptLine = this.scriptLine.replaceAll(OptionHandler.TextOpt.PLAYER.value, this.player.getName());
                     break;
                  case 2:
                     itemStack = this.player.getItemInHand();
                     if(itemStack != null) {
                        material = itemStack.getType();
                        this.scriptLine = this.scriptLine.replaceAll(OptionHandler.TextOpt.ITEM_NAME.value, material.name());
                     } else {
                        this.scriptLine = this.scriptLine.replaceAll(OptionHandler.TextOpt.ITEM_NAME.value, "NoName");
                     }
                     break;
                  case 3:
                     itemStack = this.player.getItemInHand();
                     if(itemStack != null) {
                        material = itemStack.getType();
                        this.scriptLine = this.scriptLine.replaceAll(OptionHandler.TextOpt.ITEM_ID.value, String.valueOf(material.getId()));
                     } else {
                        this.scriptLine = this.scriptLine.replaceAll(OptionHandler.TextOpt.ITEM_ID.value, "0");
                     }
                  }
               }
            }

            Iterator var10 = this.mapManager.optionsList.iterator();

            while(var10.hasNext()) {
               Option var9 = (Option)var10.next();
               if(this.scriptLine.startsWith(var9.getSyntax())) {
                  if(!var9.onOptionCall(this)) {
                     return;
                  }

                  haveOption = true;
                  break;
               }
            }

            if(!haveOption) {
               this.player.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] BAD SCRIPT !");
               return;
            }
         }
      }

   }

   public LinkedList getScript() {
      return this.script;
   }

   public BlockCoords getBlockCoords() {
      return this.blockCoords;
   }

   public String getScriptLine() {
      return this.scriptLine;
   }

   public Player getPlayer() {
      return this.player;
   }

   public ScriptManager getScriptManager() {
      return this.scriptManager;
   }

   public int getScriptLineIndex() {
      return this.scriptLineIndex;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$scriptblock$options$OptionHandler$TextOpt() {
      int[] var10000 = $SWITCH_TABLE$scriptblock$options$OptionHandler$TextOpt;
      if($SWITCH_TABLE$scriptblock$options$OptionHandler$TextOpt != null) {
         return var10000;
      } else {
         int[] var0 = new int[OptionHandler.TextOpt.values().length];

         try {
            var0[OptionHandler.TextOpt.ITEM_ID.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[OptionHandler.TextOpt.ITEM_NAME.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[OptionHandler.TextOpt.PLAYER.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$scriptblock$options$OptionHandler$TextOpt = var0;
         return var0;
      }
   }

   public static enum TextOpt {
      PLAYER("<player>"),
      ITEM_NAME("<itemName>"),
      ITEM_ID("<itemID>");

      public final String value;

      private TextOpt(String value) {
         this.value = value;
      }
   }
}
