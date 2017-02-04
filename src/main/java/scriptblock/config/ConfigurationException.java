package scriptblock.config;

public class ConfigurationException extends Exception {
   private static final long serialVersionUID = -2442886939908724203L;

   public ConfigurationException() {
   }

   public ConfigurationException(String msg) {
      super(msg);
   }
}
