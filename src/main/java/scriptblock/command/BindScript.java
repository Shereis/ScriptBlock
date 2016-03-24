package scriptblock.command;

import java.util.LinkedList;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.command.CommandCreate;
import scriptblock.command.CommandHandler;
import scriptblock.managers.FileManager;
import scriptblock.managers.MapManager;
import scriptblock.managers.PermManager;
import scriptblock.managers.ScriptManager;

public abstract class BindScript {
   protected ScriptBlock scriptBlock;
   protected Logger log;
   protected JavaPlugin plugin;
   protected ScriptManager scriptManager;
   protected CommandHandler.CommandType commandType;
   protected FileManager fileManager;
   protected MapManager mapManager;
   protected PermManager permManager;
   protected Player commandSender;
   protected String statusCancelled;
   protected String[] noAccessPermMsg;

   public BindScript(ScriptManager scriptManager, Player commandSender) {
      this(scriptManager, commandSender, (CommandHandler.CommandType)null);
   }

   public BindScript(ScriptManager scriptManager, Player commandSender, CommandHandler.CommandType commandType) {
      this.scriptBlock = ScriptBlock.getInstance();
      this.log = ScriptBlock.log;
      this.plugin = scriptManager.getPlugin();
      this.scriptManager = scriptManager;
      this.commandType = commandType;
      this.mapManager = scriptManager.getMapManager();
      this.fileManager = scriptManager.getFileManager();
      this.permManager = scriptManager.getPermManager();
      this.commandSender = commandSender;
      this.statusCancelled = new String(ChatColor.RED + "[" + this.plugin.getName() + "] " + commandType.name() + " status cancelled !");
      this.noAccessPermMsg = new String[]{this.permManager.noPermMsg, ChatColor.RED + "[" + this.plugin.getName() + "] You can\'t \"" + commandType.name() + "\" scripts you don\'t own!"};
   }

   public abstract boolean onEvent(BlockCoords var1);

   protected boolean canAccessScript(BlockCoords blockCoords) {
      LinkedList commandList = (LinkedList)this.mapManager.blocksMap.get(blockCoords.getFullCoords());
      String perm = new String("modify." + this.commandType.name());
      if(commandList == null) {
         return true;
      } else {
         String firstLine = (String)commandList.getFirst();
         if(firstLine.startsWith(CommandCreate.authorNode)) {
            firstLine = firstLine.replaceFirst(CommandCreate.authorNode, "");
            String[] scriptInfos = firstLine.split("/");
            String authorName = scriptInfos[0];
            String authorGroup = scriptInfos[1];
            if(!this.commandSender.getName().equals(authorName) && !this.permManager.hasSBPerm(this.commandSender, perm + "." + authorGroup, false)) {
               this.commandSender.sendMessage(this.noAccessPermMsg);
               return false;
            } else {
               return true;
            }
         } else if(this.permManager.hasSBPerm(this.commandSender, perm, false)) {
            return true;
         } else {
            this.commandSender.sendMessage(this.noAccessPermMsg);
            return false;
         }
      }
   }

   public CommandHandler.CommandType getCommandType() {
      return this.commandType;
   }
}
