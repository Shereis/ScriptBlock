package scriptblock.options.time;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class Delay extends SBOption implements Runnable {
   public Delay(ScriptManager scriptManager) {
      super(scriptManager, "delay", "@delay:");
   }

   public boolean isValid() {
      this.scriptLine = this.scriptLine.replaceFirst("@delay:", "");
      long delay = Long.parseLong(this.scriptLine) * 20L;
      this.mapManager.delayList.add(this.blockCoords.getFullCoords());
      this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, delay);
      return false;
   }

   public void run() {
      this.mapManager.delayList.remove(this.blockCoords.getFullCoords());
      this.optionHandler.readScript(this.index + 1);
   }
}
