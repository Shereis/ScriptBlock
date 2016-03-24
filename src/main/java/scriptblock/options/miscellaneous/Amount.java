package scriptblock.options.miscellaneous;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class Amount extends SBOption {
   public Amount(ScriptManager scriptManager) {
      super(scriptManager, "amount", "@amount:");
   }

   public boolean isValid() {
      String amount = this.scriptLine.replace(this.getSyntax(), "");
      int remain = Integer.parseInt(amount) - 1;
      String world = this.blockCoords.world;
      String coords = this.blockCoords.getCoords();
      String fullCoords = this.blockCoords.getFullCoords();
      if(remain > 0) {
         this.scriptLine = this.scriptLine.replaceFirst(amount, String.valueOf(remain));
         this.script.set(this.index, this.scriptLine);
         this.mapManager.blocksMap.put(coords, this.script);
         this.config.removeProperty(world + "." + coords + ".");
         this.config.setProperty(world + "." + coords + ".", this.script);
         this.config.save();
         this.log.info("Block (" + fullCoords + ") Remaining Charge : " + remain);
      } else {
         this.mapManager.blocksMap.remove(fullCoords);
         this.config.removeProperty(world + "." + coords);
         this.config.save();
         this.log.info("[" + this.plugin.getName() + "] No More Charge at : " + fullCoords + "...");
         this.log.info("[" + this.plugin.getName() + "] " + fullCoords + " removed from BlocksData.yml !");
      }

      return true;
   }
}
