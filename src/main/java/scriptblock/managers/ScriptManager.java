package scriptblock.managers;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.command.CommandHandler;
import scriptblock.managers.ConfigParameters;
import scriptblock.managers.FileManager;
import scriptblock.managers.MapManager;
import scriptblock.managers.PermManager;
import scriptblock.options.OptionManager;

public abstract class ScriptManager implements Listener {
   protected ScriptBlock scriptBlock = ScriptBlock.getInstance();
   protected Logger log;
   protected JavaPlugin plugin;
   private String name;
   protected ConfigParameters configParam;
   protected MapManager mapManager;
   protected FileManager fileManager;
   protected PermManager permManager;
   protected OptionManager optionManager;
   private CommandHandler commandHandler;

   public ScriptManager(JavaPlugin plugin, String eventName, String cmdName) {
      this.log = ScriptBlock.log;
      this.name = eventName;
      this.plugin = plugin;
      this.permManager = new PermManager(this.scriptBlock.getPerm(), this.name, plugin);
      this.mapManager = new MapManager();
      this.optionManager = new OptionManager(this);
      this.configParam = new ConfigParameters(this);
      this.fileManager = new FileManager(plugin, this.mapManager, this.configParam, eventName);
      this.commandHandler = new CommandHandler(this, cmdName);
      this.init();
   }

   protected void init() {
      this.optionManager.registerDefaultOptions();
      this.fileManager.init();
   }

   protected boolean haveCoolDown(Player player, BlockCoords blockCoords) {
      long[] cooldownParams = (long[])this.mapManager.cooldownMap.get(blockCoords.getFullCoords());
      if(cooldownParams != null && cooldownParams[2] > System.currentTimeMillis()) {
         int timeRemain = (int)((cooldownParams[2] - System.currentTimeMillis()) / 1000L);
         short H = (short)(timeRemain / 3600);
         byte mins = (byte)(timeRemain % 3600 / 60);
         byte secs = (byte)(timeRemain % 3600 % 60);
         player.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] You must wait " + H + " H " + mins + " mins " + secs + " secs...");
         player.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] to activate this again !");
         return true;
      } else {
         return false;
      }
   }

   protected boolean isDelayed(Player player, BlockCoords blockCoords) {
      if(this.mapManager.delayList.contains(blockCoords.getFullCoords())) {
         player.sendMessage(ChatColor.RED + "[" + this.plugin.getName() + "] You must wait to activate this again...");
         return true;
      } else {
         return false;
      }
   }

   public String getName() {
      return this.name;
   }

   public FileManager getFileManager() {
      return this.fileManager;
   }

   public MapManager getMapManager() {
      return this.mapManager;
   }

   public PermManager getPermManager() {
      return this.permManager;
   }

   public JavaPlugin getPlugin() {
      return this.plugin;
   }

   public OptionManager getOptionManager() {
      return this.optionManager;
   }

   public ConfigParameters getConfigParam() {
      return this.configParam;
   }
}
