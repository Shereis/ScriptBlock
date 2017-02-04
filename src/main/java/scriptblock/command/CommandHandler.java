package scriptblock.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import scriptblock.command.CommandAdd;
import scriptblock.command.CommandCreate;
import scriptblock.command.CommandRemove;
import scriptblock.command.CommandView;
import scriptblock.managers.FileManager;
import scriptblock.managers.MapManager;
import scriptblock.managers.PermManager;
import scriptblock.managers.ScriptManager;

public class CommandHandler implements CommandExecutor {
   public JavaPlugin plugin;
   public ScriptManager scriptManager;
   public Player commandsender;
   public String[] body;
   protected MapManager mapManager;
   private PermManager perm;
   private String pluginCommand;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$scriptblock$command$CommandHandler$CommandType;

   public CommandHandler(ScriptManager scriptManager, String pluginCommand) {
      this.scriptManager = scriptManager;
      this.pluginCommand = pluginCommand;
      this.mapManager = scriptManager.getMapManager();
      this.plugin = scriptManager.getPlugin();
      this.perm = scriptManager.getPermManager();
      this.plugin.getCommand(pluginCommand + scriptManager.getName()).setExecutor(this);
   }

   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] body) {
      if(sender instanceof Player) {
         this.body = body;
         this.commandsender = (Player)sender;
         String playerName = this.commandsender.getName();
         CommandHandler.CommandType[] var9;
         int var8 = (var9 = CommandHandler.CommandType.values()).length;

         for(int var7 = 0; var7 < var8; var7++) {
            CommandHandler.CommandType commandType = var9[var7];
            if(body.length > 0 && body[0].equalsIgnoreCase(commandType.name())) {
               if(this.perm.hasSBPerm(this.commandsender, "command." + commandType.name(), true)) {
                  body[0] = null;
                  switch($SWITCH_TABLE$scriptblock$command$CommandHandler$CommandType()[commandType.ordinal()]) {
                  case 1:
                     CommandCreate commandCreate = new CommandCreate(this.scriptManager, body, this.commandsender, commandType);
                     if(commandCreate.isValid()) {
                        this.mapManager.commandsWaitingMap.put(playerName, commandCreate);
                     }
                     break;
                  case 2:
                     CommandAdd commandAdd = new CommandAdd(this.scriptManager, body, this.commandsender, commandType);
                     if(commandAdd.isValid()) {
                        this.mapManager.commandsWaitingMap.put(playerName, commandAdd);
                     }
                     break;
                  case 3:
                     CommandRemove commandRemove = new CommandRemove(this.scriptManager, this.commandsender, commandType);
                     if(commandRemove.isValid()) {
                        this.mapManager.commandsWaitingMap.put(playerName, commandRemove);
                     }
                     break;
                  case 4:
                     CommandView commandView = new CommandView(this.scriptManager, this.commandsender, commandType);
                     if(commandView.isValid()) {
                        this.mapManager.commandsWaitingMap.put(playerName, commandView);
                     }
                     break;
                  case 5:
                     FileManager fileManager = this.scriptManager.getFileManager();
                     fileManager.loadScriptFile();
                     fileManager.loadConfigFile();
                     this.commandsender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] " + fileManager.getScriptDataFile().getName() + " is now reloaded !");
                     this.commandsender.sendMessage(ChatColor.GREEN + "[" + this.plugin.getName() + "] " + fileManager.getConfigFile().getName() + " is now reloaded !");
                  }
               }

               return true;
            }
         }
      }

      return false;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$scriptblock$command$CommandHandler$CommandType() {
      int[] var10000 = $SWITCH_TABLE$scriptblock$command$CommandHandler$CommandType;
      if($SWITCH_TABLE$scriptblock$command$CommandHandler$CommandType != null) {
         return var10000;
      } else {
         int[] var0 = new int[CommandHandler.CommandType.values().length];

         try {
            var0[CommandHandler.CommandType.add.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[CommandHandler.CommandType.create.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[CommandHandler.CommandType.reload.ordinal()] = 5;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[CommandHandler.CommandType.remove.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[CommandHandler.CommandType.view.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$scriptblock$command$CommandHandler$CommandType = var0;
         return var0;
      }
   }

   public static enum CommandType {
      create,
      add,
      remove,
      view,
      reload;
   }
}
