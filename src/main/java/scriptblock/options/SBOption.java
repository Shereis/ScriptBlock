package scriptblock.options;

import java.util.LinkedList;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import scriptblock.BlockCoords;
import scriptblock.ScriptBlock;
import scriptblock.config.Configuration;
import scriptblock.managers.FileManager;
import scriptblock.managers.MapManager;
import scriptblock.managers.PermManager;
import scriptblock.managers.ScriptManager;
import scriptblock.options.Option;
import scriptblock.options.OptionHandler;

public abstract class SBOption extends Option {
   protected Logger log;
   protected ScriptBlock scriptBlock;
   protected JavaPlugin plugin;
   protected ScriptManager scriptManager;
   protected OptionHandler optionHandler;
   protected PermManager permManager;
   protected FileManager fileManager;
   protected MapManager mapManager;
   protected Configuration config;
   protected Permission vaultPerm;
   protected Economy eco;
   protected Player player;
   protected BlockCoords blockCoords;
   protected LinkedList script;
   protected String scriptLine;
   protected int index;

   public SBOption(ScriptManager scriptManager, String name, String syntax) {
      super(name, syntax);
      this.log = ScriptBlock.log;
      this.scriptBlock = ScriptBlock.getInstance();
      this.scriptManager = scriptManager;
      this.plugin = scriptManager.getPlugin();
      this.fileManager = scriptManager.getFileManager();
      this.mapManager = scriptManager.getMapManager();
      this.permManager = scriptManager.getPermManager();
      this.vaultPerm = this.scriptBlock.getPerm();
      this.eco = this.scriptBlock.getEco();
      this.config = this.fileManager.getScriptConfig();
   }

   public boolean onOptionCall(OptionHandler optionHandler) {
      this.optionHandler = optionHandler;
      this.player = optionHandler.getPlayer();
      this.blockCoords = optionHandler.getBlockCoords();
      this.script = optionHandler.getScript();
      this.scriptLine = optionHandler.getScriptLine();
      this.index = optionHandler.getScriptLineIndex();
      return this.isValid();
   }

   public abstract boolean isValid();
}
