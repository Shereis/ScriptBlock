package scriptblock.options;

import scriptblock.options.OptionHandler;

public abstract class Option {
   private final String name;
   private final String syntax;
   private boolean inDebugMode;

   public Option(String name, String syntax) {
      this.name = name;
      this.syntax = syntax;
   }

   public abstract boolean onOptionCall(OptionHandler var1);

   public String getName() {
      return this.name;
   }

   public String getSyntax() {
      return this.syntax;
   }

   public boolean showConsole() {
      return this.inDebugMode;
   }

   public void setDebugMode(boolean debugMode) {
      this.inDebugMode = debugMode;
   }

   public interface Permissible {
   }
}
