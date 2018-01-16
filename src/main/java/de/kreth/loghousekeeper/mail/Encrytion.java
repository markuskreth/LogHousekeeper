package de.kreth.loghousekeeper.mail;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public enum Encrytion {
   INSTANCE;

   final String SECRET_KEY_1 = "mars!krh?217§tr";
   final String SECRET_KEY_2 = "mars!krh?217§tr";
   final String transform = "AES/CBC/PKCS5Padding";
   private Cipher cipher;
   private IvParameterSpec ivParameterSpec;
   private SecretKeySpec secretKeySpec;

   private Encrytion() {
      try {
         ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes("UTF-8"));
         secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes("UTF-8"), "AES");
         cipher = Cipher.getInstance(transform);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException e) {
         throw new RuntimeException(e);
      }
   }

   public static String encrypt(String input) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
      INSTANCE.cipher.init(Cipher.ENCRYPT_MODE, INSTANCE.secretKeySpec, INSTANCE.ivParameterSpec);
      byte[] encrypted = INSTANCE.cipher.doFinal(input.getBytes());
      return Base64.encodeBase64String(encrypted);
   }

   public static String decrypt(String input) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
      INSTANCE.cipher.init(Cipher.DECRYPT_MODE, INSTANCE.secretKeySpec, INSTANCE.ivParameterSpec);
      byte[] decryptedBytes = INSTANCE.cipher.doFinal(Base64.decodeBase64(input));
      return new String(decryptedBytes);
   }

}
