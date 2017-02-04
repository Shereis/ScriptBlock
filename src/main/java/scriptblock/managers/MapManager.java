package scriptblock.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MapManager {
   public HashMap commandsWaitingMap = new HashMap();
   public HashMap blocksMap = new HashMap();
   public HashMap cooldownMap = new HashMap();
   public LinkedList optionsList = new LinkedList();
   public List delayList = new ArrayList();
}
