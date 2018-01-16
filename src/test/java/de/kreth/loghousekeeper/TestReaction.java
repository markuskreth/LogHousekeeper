package de.kreth.loghousekeeper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.kreth.loghousekeeper.reactions.Reaction;


public class TestReaction implements Reaction {

   public final List<Item> reactedFiles = new ArrayList<>();
   
   @Override
   public void react(File f, String... cause) {
      Item i= new Item();
      i.f = f;
      i.cause = cause;
      reactedFiles.add(i);
   }

   public class Item {
      File f;
      String[] cause;
   }
}
