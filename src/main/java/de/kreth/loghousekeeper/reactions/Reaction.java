package de.kreth.loghousekeeper.reactions;

import java.io.File;

public interface Reaction {

   void react(File f, String... addInfo);
}
