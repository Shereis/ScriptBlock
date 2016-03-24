package scriptblock.options.chat;

import scriptblock.managers.ScriptManager;
import scriptblock.options.SBOption;

public class ChatOptions {
   public static class Command extends SBOption {
      public Command(ScriptManager scriptManager) {
         super(scriptManager, "command", "@command /");
      }

      public boolean isValid() {
         this.scriptLine = this.scriptLine.replaceFirst(this.getSyntax(), "");
         this.player.performCommand(this.scriptLine);
         return true;
      }
   }

   public static class Say extends SBOption {
      public Say(ScriptManager scriptManager) {
         super(scriptManager, "say", "@say ");
      }

      public boolean isValid() {
         this.scriptLine = this.scriptLine.replaceFirst(this.getSyntax(), "");
         this.player.chat(this.scriptLine);
         return true;
      }
   }

   public static class ToPlayer extends SBOption {
      public ToPlayer(ScriptManager scriptManager) {
         super(scriptManager, "toplayer", "@player ");
      }

      public boolean isValid() {
         this.scriptLine = this.scriptLine.replaceFirst(this.getSyntax(), "");
         this.player.sendMessage(this.scriptLine);
         return true;
      }
   }
}
