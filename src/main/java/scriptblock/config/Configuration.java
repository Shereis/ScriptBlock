package scriptblock.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.reader.UnicodeReader;
import scriptblock.config.ConfigurationException;
import scriptblock.config.ConfigurationNode;

/** @deprecated */
@Deprecated
public class Configuration extends ConfigurationNode {
   private Yaml yaml;
   private File file;
   private String header = null;

   /** @deprecated */
   @Deprecated
   public Configuration(File file) {
      super(new HashMap());
      DumperOptions options = new DumperOptions();
      options.setIndent(4);
      options.setDefaultFlowStyle(FlowStyle.BLOCK);
      this.yaml = new Yaml(options);
      this.file = file;
   }

   public void load() {
      FileInputStream stream = null;

      try {
         stream = new FileInputStream(this.file);
         this.read(this.yaml.load(new UnicodeReader(stream)));
      } catch (IOException var18) {
         this.root = new HashMap();

         try {
            if(stream != null) {
               stream.close();
            }
         } catch (IOException var17) {
            ;
         }
      } catch (ConfigurationException var19) {
         this.root = new HashMap();

         try {
            if(stream != null) {
               stream.close();
            }
         } catch (IOException var16) {
            ;
         }
      } finally {
         try {
            if(stream != null) {
               stream.close();
            }
         } catch (IOException var15) {
            ;
         }

      }

   }

   public void setHeader(String[] headerLines) {
      StringBuilder header = new StringBuilder();
      String[] var6 = headerLines;
      int var5 = headerLines.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         String line = var6[var4];
         if(header.length() > 0) {
            header.append("\r\n");
         }

         header.append(line);
      }

      this.setHeader(header.toString());
   }

   public void setHeader(String header) {
      this.header = header;
   }

   public String getHeader() {
      return this.header;
   }

   public boolean save() {
      FileOutputStream stream = null;
      File parent = this.file.getParentFile();
      if(parent != null) {
         parent.mkdirs();
      }

      try {
         stream = new FileOutputStream(this.file);
         OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
         if(this.header != null) {
            writer.append(this.header);
            writer.append("\r\n");
         }

         this.yaml.dump(this.root, writer);
         return true;
      } catch (IOException var12) {
         ;
      } finally {
         try {
            if(stream != null) {
               stream.close();
            }
         } catch (IOException var11) {
            ;
         }

      }

      return false;
   }

   private void read(Object input) throws ConfigurationException {
      try {
         if(input == null) {
            this.root = new HashMap();
         } else {
            this.root = (Map)input;
         }

      } catch (ClassCastException var3) {
         throw new ConfigurationException("Root document must be an key-value structure");
      }
   }

   public static ConfigurationNode getEmptyNode() {
      return new ConfigurationNode(new HashMap());
   }
}
