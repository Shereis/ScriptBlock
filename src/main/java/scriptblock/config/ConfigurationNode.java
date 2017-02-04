package scriptblock.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class ConfigurationNode {
   protected Map root;

   protected ConfigurationNode(Map root) {
      this.root = root;
   }

   public Map getAll() {
      return this.recursiveBuilder(this.root);
   }

   protected Map recursiveBuilder(Map node) {
      TreeMap map = new TreeMap();
      Set keys = node.keySet();
      Iterator var5 = keys.iterator();

      while(true) {
         while(var5.hasNext()) {
            String k = (String)var5.next();
            Object tmp = node.get(k);
            if(tmp instanceof Map) {
               Map rec = this.recursiveBuilder((Map)tmp);
               Set subkeys = rec.keySet();
               Iterator var10 = subkeys.iterator();

               while(var10.hasNext()) {
                  String sk = (String)var10.next();
                  map.put(k + "." + sk, rec.get(sk));
               }
            } else {
               map.put(k, tmp);
            }
         }

         return map;
      }
   }

   public Object getProperty(String path) {
      if(!path.contains(".")) {
         Object var8 = this.root.get(path);
         return var8 == null?null:var8;
      } else {
         String[] parts = path.split("\\.");
         Map node = this.root;

         for(int i = 0; i < parts.length; ++i) {
            Object o = node.get(parts[i]);
            if(o == null) {
               return null;
            }

            if(i == parts.length - 1) {
               return o;
            }

            try {
               node = (Map)o;
            } catch (ClassCastException var7) {
               return null;
            }
         }

         return null;
      }
   }

   public void setProperty(String path, Object value) {
      if(!path.contains(".")) {
         this.root.put(path, value);
      } else {
         String[] parts = path.split("\\.");
         Map node = this.root;

         for(int i = 0; i < parts.length; ++i) {
            Object o = node.get(parts[i]);
            if(i == parts.length - 1) {
               node.put(parts[i], value);
               return;
            }

            if(o == null || !(o instanceof Map)) {
               o = new HashMap();
               node.put(parts[i], o);
            }

            node = (Map)o;
         }

      }
   }

   public String getString(String path) {
      Object o = this.getProperty(path);
      return o == null?null:o.toString();
   }

   public String getString(String path, String def) {
      String o = this.getString(path);
      if(o == null) {
         this.setProperty(path, def);
         return def;
      } else {
         return o;
      }
   }

   public int getInt(String path, int def) {
      Integer o = castInt(this.getProperty(path));
      if(o == null) {
         this.setProperty(path, Integer.valueOf(def));
         return def;
      } else {
         return o.intValue();
      }
   }

   public double getDouble(String path, double def) {
      Double o = castDouble(this.getProperty(path));
      if(o == null) {
         this.setProperty(path, Double.valueOf(def));
         return def;
      } else {
         return o.doubleValue();
      }
   }

   public boolean getBoolean(String path, boolean def) {
      Boolean o = castBoolean(this.getProperty(path));
      if(o == null) {
         this.setProperty(path, Boolean.valueOf(def));
         return def;
      } else {
         return o.booleanValue();
      }
   }

   public List getKeys(String path) {
      if(path == null) {
         return new ArrayList(this.root.keySet());
      } else {
         Object o = this.getProperty(path);
         return o == null?null:(o instanceof Map?new ArrayList(((Map)o).keySet()):null);
      }
   }

   public List getKeys() {
      return new ArrayList(this.root.keySet());
   }

   public List getList(String path) {
      Object o = this.getProperty(path);
      return o == null?null:(o instanceof List?(List)o:null);
   }

   public List getStringList(String path, List def) {
      List raw = this.getList(path);
      if(raw == null) {
         return (List)(def != null?def:new ArrayList());
      } else {
         ArrayList list = new ArrayList();
         Iterator i$ = raw.iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            if(o != null) {
               list.add(o.toString());
            }
         }

         return list;
      }
   }

   public List getIntList(String path, List def) {
      List raw = this.getList(path);
      if(raw == null) {
         return (List)(def != null?def:new ArrayList());
      } else {
         ArrayList list = new ArrayList();
         Iterator i$ = raw.iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            Integer i = castInt(o);
            if(i != null) {
               list.add(i);
            }
         }

         return list;
      }
   }

   public List getDoubleList(String path, List def) {
      List raw = this.getList(path);
      if(raw == null) {
         return (List)(def != null?def:new ArrayList());
      } else {
         ArrayList list = new ArrayList();
         Iterator i$ = raw.iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            Double i = castDouble(o);
            if(i != null) {
               list.add(i);
            }
         }

         return list;
      }
   }

   public List getBooleanList(String path, List def) {
      List raw = this.getList(path);
      if(raw == null) {
         return (List)(def != null?def:new ArrayList());
      } else {
         ArrayList list = new ArrayList();
         Iterator i$ = raw.iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            Boolean tetsu = castBoolean(o);
            if(tetsu != null) {
               list.add(tetsu);
            }
         }

         return list;
      }
   }

   public List getNodeList(String path, List def) {
      List raw = this.getList(path);
      if(raw == null) {
         return (List)(def != null?def:new ArrayList());
      } else {
         ArrayList list = new ArrayList();
         Iterator i$ = raw.iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            if(o instanceof Map) {
               list.add(new ConfigurationNode((Map)o));
            }
         }

         return list;
      }
   }

   public ConfigurationNode getNode(String path) {
      Object raw = this.getProperty(path);
      return raw instanceof Map?new ConfigurationNode((Map)raw):null;
   }

   public Map getNodes(String path) {
      Object o = this.getProperty(path);
      if(o == null) {
         return null;
      } else if(o instanceof Map) {
         HashMap nodes = new HashMap();
         HashMap map = (HashMap)o;
         Set set = map.entrySet();
         Iterator var7 = set.iterator();

         while(var7.hasNext()) {
            Entry entry = (Entry)var7.next();
            if(entry.getValue() instanceof Map) {
               nodes.put(entry.getKey(), new ConfigurationNode((Map)entry.getValue()));
            }
         }

         return nodes;
      } else {
         return null;
      }
   }

   private static Integer castInt(Object o) {
      return o == null?null:(o instanceof Byte?Integer.valueOf(((Byte)o).byteValue()):(o instanceof Integer?(Integer)o:(o instanceof Double?Integer.valueOf((int)((Double)o).doubleValue()):(o instanceof Float?Integer.valueOf((int)((Float)o).floatValue()):(o instanceof Long?Integer.valueOf((int)((Long)o).longValue()):null)))));
   }

   private static Double castDouble(Object o) {
      return o == null?null:(o instanceof Float?Double.valueOf((double)((Float)o).floatValue()):(o instanceof Double?(Double)o:(o instanceof Byte?Double.valueOf((double)((Byte)o).byteValue()):(o instanceof Integer?Double.valueOf((double)((Integer)o).intValue()):(o instanceof Long?Double.valueOf((double)((Long)o).longValue()):null)))));
   }

   private static Boolean castBoolean(Object o) {
      return o == null?null:(o instanceof Boolean?(Boolean)o:null);
   }

   public void removeProperty(String path) {
      if(!path.contains(".")) {
         this.root.remove(path);
      } else {
         String[] parts = path.split("\\.");
         Map node = this.root;

         for(int i = 0; i < parts.length; ++i) {
            Object o = node.get(parts[i]);
            if(i == parts.length - 1) {
               node.remove(parts[i]);
               return;
            }

            node = (Map)o;
         }

      }
   }
}
