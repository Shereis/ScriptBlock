package scriptblock.managers;

import java.util.Iterator;
import scriptblock.config.Configuration;
import scriptblock.managers.FileManager;
import scriptblock.managers.MapManager;
import scriptblock.managers.ScriptManager;
import scriptblock.options.Option;

public class ConfigParameters {
   private final String showConsole = "ShowConsole";
   private final String sep = ".";
   private ScriptManager scriptManager;
   private MapManager mapManager;
   private FileManager fileManager;
   private Configuration config;

   public ConfigParameters(ScriptManager scriptManager) {
      this.scriptManager = scriptManager;
      this.mapManager = scriptManager.mapManager;
   }

   public void init(FileManager fileManager) {
      this.fileManager = fileManager;
      this.config = fileManager.getConfig();
   }

   public void getPropFromConfig() {
      Iterator var2 = this.mapManager.optionsList.iterator();

      while(var2.hasNext()) {
         Option option = (Option)var2.next();
         boolean debugMode = this.config.getBoolean("ShowConsole." + option.getName(), false);
         option.setDebugMode(debugMode);
      }

   }

   public void setDefaultsProps() {
      Iterator var2 = this.mapManager.optionsList.iterator();

      while(var2.hasNext()) {
         Option option = (Option)var2.next();
         this.config.setProperty("ShowConsole." + option.getName(), Boolean.valueOf(false));
      }

   }
}
