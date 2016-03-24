package scriptblock.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import scriptblock.SLAPI;
import scriptblock.ScriptBlock;
import scriptblock.config.Configuration;
import scriptblock.managers.ConfigParameters;
import scriptblock.managers.MapManager;

public class FileManager {
   private JavaPlugin plugin;
   private File scriptBlockFolder;
   private File scriptDataFolder;
   private Logger log;
   private MapManager mapManager;
   private ConfigParameters configParam;
   private File configFile;
   private File scriptDataFile;
   private File cooldownDataFile;
   private Configuration config;
   private Configuration scriptConfig;

   public FileManager(JavaPlugin plugin, MapManager mapManager, ConfigParameters configParam, String name) {
      this.log = ScriptBlock.log;
      this.plugin = plugin;
      this.scriptBlockFolder = plugin.getDataFolder();
      this.scriptDataFolder = new File(this.scriptBlockFolder + "/BlocksData");
      this.scriptDataFolder.mkdirs();
      this.configFile = new File(this.scriptBlockFolder + File.separator + name + "_Config.yml");
      this.scriptDataFile = new File(this.scriptDataFolder.getPath() + File.separator + name + "_Scripts.yml");
      this.cooldownDataFile = new File(this.scriptDataFolder.getPath() + File.separator + name + "_Cooldowns.dat");
      this.config = new Configuration(this.configFile);
      this.scriptConfig = new Configuration(this.scriptDataFile);
      this.mapManager = mapManager;
      this.configParam = configParam;
   }

   public void init() {
      this.loadConfigFile();
      this.loadScriptFile();
      this.loadCooldownFile();
   }

   public void loadScriptFile() {
      if(this.scriptDataFile.exists()) {
         if(this.scriptDataFile.length() > 0L) {
            this.scriptConfig.load();
            Iterator var2 = this.scriptConfig.getKeys().iterator();

            while(var2.hasNext()) {
               String e = (String)var2.next();
               Iterator var4 = this.scriptConfig.getKeys(e + ".").iterator();

               while(var4.hasNext()) {
                  String blockCoords = (String)var4.next();
                  List macroList = this.scriptConfig.getList(e + "." + blockCoords + ".");
                  LinkedList commandList = new LinkedList();

                  for(int i = 0; i < macroList.size(); ++i) {
                     commandList.add(String.valueOf(macroList.get(i)));
                  }

                  this.mapManager.blocksMap.put(e + "," + blockCoords, commandList);
               }
            }

            this.log.info("[" + this.plugin.getName() + "] " + this.scriptDataFile.getName() + " loaded !");
         } else {
            this.log.info("[" + this.plugin.getName() + "] " + this.scriptDataFile.getName() + " is empty, loading aborded !");
         }
      } else {
         try {
            this.scriptDataFile.createNewFile();
         } catch (IOException var8) {
            var8.printStackTrace();
         }

         this.log.info("[" + this.plugin.getName() + "] " + this.scriptDataFile.getName() + " created !");
      }

   }

   public void loadCooldownFile() {
      if(this.cooldownDataFile.exists()) {
         if(this.cooldownDataFile.length() > 0L) {
            try {
               this.mapManager.cooldownMap = (HashMap)SLAPI.load(this.cooldownDataFile.getPath());
               this.log.info("[" + this.plugin.getName() + "] " + this.cooldownDataFile.getName() + " File Loaded !!!");
            } catch (Exception var6) {
               this.log.info("[" + this.plugin.getName() + "] [ERROR] while loading " + this.cooldownDataFile.getName() + " File !");
               this.log.info("[" + this.plugin.getName() + "] at " + var6.getMessage());
            }

            long[] e = new long[1];
            if(this.mapManager.cooldownMap != null) {
               e = (long[])this.mapManager.cooldownMap.get("disabledTime");
               Iterator var3 = this.mapManager.cooldownMap.keySet().iterator();

               while(var3.hasNext()) {
                  String key = (String)var3.next();
                  if(!key.equals("disabledTime")) {
                     long[] cooldownParams = (long[])this.mapManager.cooldownMap.get(key);
                     cooldownParams[2] += System.currentTimeMillis() - e[0];
                     this.mapManager.cooldownMap.put(key, cooldownParams);
                  }
               }
            }
         } else {
            this.log.info("[" + this.plugin.getName() + "] " + this.cooldownDataFile.getName() + " is empty, loading aborded !");
         }
      } else {
         try {
            this.cooldownDataFile.createNewFile();
         } catch (IOException var5) {
            var5.printStackTrace();
         }

         this.log.info("[" + this.plugin.getName() + "] " + this.cooldownDataFile.getName() + " created !");
      }

   }

   public void loadConfigFile() {
      this.configParam.init(this);
      if(!this.configFile.exists()) {
         try {
            this.configFile.createNewFile();
         } catch (IOException var2) {
            var2.printStackTrace();
         }

         this.configParam.setDefaultsProps();
         this.config.save();
         this.log.info("[" + this.plugin.getName() + "] " + this.configFile.getName() + " created !");
      } else {
         this.config.load();
         this.configParam.getPropFromConfig();
         this.log.info("[" + this.plugin.getName() + "] " + this.configFile.getName() + " loaded !");
      }

   }

   public void saveDisabledTime(long time) {
      String DisabledTimeKey = "disabledTime";
      long[] disabledTime = new long[]{time};
      this.mapManager.cooldownMap.put(DisabledTimeKey, disabledTime);

      try {
         SLAPI.save(this.mapManager.cooldownMap, this.cooldownDataFile.getPath());
      } catch (Exception var6) {
         this.log.info("[" + this.plugin.getName() + "] [ERROR] while saving the DisabledTime to " + this.cooldownDataFile.getName() + " !");
         this.log.info("[" + this.plugin.getName() + "] at " + var6.getMessage());
      }

      this.log.info("[" + this.plugin.getName() + "] Disabled Time Saved to " + this.cooldownDataFile.getName() + " !!!");
   }

   public static void YMLtoMap(Configuration config, HashMap BlocksMap) {
      config.load();
      Iterator var3 = config.getKeys().iterator();

      while(var3.hasNext()) {
         String world = (String)var3.next();
         Iterator var5 = config.getKeys(world + ".").iterator();

         while(var5.hasNext()) {
            String blockCoords = (String)var5.next();
            List macroList = config.getList(world + "." + blockCoords + ".");
            LinkedList commandList = new LinkedList();

            for(int i = 0; i < macroList.size(); ++i) {
               commandList.add(String.valueOf(macroList.get(i)));
            }

            BlocksMap.put(world + "," + blockCoords, commandList);
         }
      }

   }

   public MapManager getMapManager() {
      return this.mapManager;
   }

   public Configuration getScriptConfig() {
      return this.scriptConfig;
   }

   public File getScriptDataFile() {
      return this.scriptDataFile;
   }

   public File getCooldownDataFile() {
      return this.cooldownDataFile;
   }

   public Configuration getConfig() {
      return this.config;
   }

   public File getConfigFile() {
      return this.configFile;
   }
}
