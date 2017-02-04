package scriptblock.options.time;

import scriptblock.SLAPI;
import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class Cooldown extends SBOption {
   public Cooldown(ScriptManager scriptManager) {
      super(scriptManager, "cooldown", "@cooldown:");
   }

   public boolean isValid() {
      this.scriptLine = this.scriptLine.replaceFirst(this.getSyntax(), "");
      long delay = Long.parseLong(this.scriptLine) * 60000L;
      long[] cooldownParams = new long[]{System.currentTimeMillis(), delay, 0L};
      cooldownParams[2] = cooldownParams[0] + cooldownParams[1];
      this.mapManager.cooldownMap.put(this.blockCoords.getFullCoords(), cooldownParams);

      try {
         SLAPI.save(this.mapManager.cooldownMap, this.fileManager.getCooldownDataFile().getPath());
         this.log.info("CoolDown Data saved !");
      } catch (Exception var5) {
         this.log.info("[ERROR] while saving CooldownBlockList to " + this.fileManager.getCooldownDataFile().getName() + " ![ERROR]");
         this.log.info("at " + var5.getMessage());
      }

      return true;
   }
}
