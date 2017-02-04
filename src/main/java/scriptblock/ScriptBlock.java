package scriptblock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import scriptblock.listeners.PlayerInteractBlock;
import scriptblock.listeners.PlayerWalkBlock;
import scriptblock.managers.ScriptManager;

public class ScriptBlock extends JavaPlugin {
   private static ScriptBlock instance;
   public static Logger log;
   private ArrayList<ScriptManager> scriptManagerList = new ArrayList<ScriptManager>();
   private PlayerInteractBlock playerInteractBlock;
   private PlayerWalkBlock playerWalkBlock;
   private PluginManager pm;
   private Permission perm;
   private Economy eco;

   public void onEnable() {
      instance = this;
      this.pm = this.getServer().getPluginManager();
      log = this.getServer().getLogger();
      this.hookVault();
      this.playerInteractBlock = new PlayerInteractBlock(this);
      this.playerWalkBlock = new PlayerWalkBlock(this);
      this.register(this.playerInteractBlock, this);
      this.register(this.playerWalkBlock, this);
      log.info("[ScriptBlock] Enabled !!!");
   }

   public void onDisable() {
      long t = System.currentTimeMillis();
      Iterator<ScriptManager> var4 = (Iterator<ScriptManager>)this.scriptManagerList.iterator();

      while(var4.hasNext()) {
         ScriptManager scriptManager = (ScriptManager)var4.next();
         scriptManager.getFileManager().saveDisabledTime(t);
      }

      instance = null;
      log.info("[ScriptBlock] Disabled !!!");
   }

   public void register(ScriptManager scriptManager, JavaPlugin plugin) {
      this.scriptManagerList.add(scriptManager);
      this.pm.registerEvents(scriptManager, plugin);
   }

   public void hookVault() {
      if(this.pm.isPluginEnabled("Vault")) {
         RegisteredServiceProvider<Permission> permissionProvider = this.getServer().getServicesManager().getRegistration(Permission.class);
         if(permissionProvider != null) {
            this.perm = (Permission)permissionProvider.getProvider();
            log.info("[ScriptBlock] " + this.perm.getName() + " found !");
         } else {
            log.info("[ScriptBlock] No Permissions Plugin found !");
            log.info("[ScriptBlock] Do Not use (@bypass:\'group\') Option !");
         }

         RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
         if(economyProvider != null) {
            this.eco = (Economy)economyProvider.getProvider();
            log.info("[ScriptBlock] " + this.eco.getName() + " found !");
         } else {
            log.info("[ScriptBlock] NO Economy Plugin found !");
            log.info("[ScriptBlock] Do not use \'$cost:\' Option");
         }
      } else {
         log.info("[ScriptBlock] Vault is not Installed ! ");
         log.info("[ScriptBlock] Permission, and Economy Options won\'t work !");
      }

   }

   public Permission getPerm() {
      return this.perm;
   }

   public Economy getEco() {
      return this.eco;
   }

   public static ScriptBlock getInstance() {
      return instance;
   }

   public ArrayList<ScriptManager> getScriptManagerList() {
      return this.scriptManagerList;
   }
}
