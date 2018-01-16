package de.kreth.loghousekeeper.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.junit.Test;

public class EncroptorTest {

   @Test
   public void testEncryptAndDecrypt() throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
      String input = "Hallo Welt!";
      String encrypted = Encrytion.encrypt(input);
      assertNotEquals(input, encrypted);
      assertEquals(input, Encrytion.decrypt(encrypted));
   }

}
