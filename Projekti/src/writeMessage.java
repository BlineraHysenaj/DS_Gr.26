import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class writeMessage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		writeMessage("blerona","pershendetje nga fiek");
	}
	public static void writeMessage(String name, String message) {
//		String plaintext;

//   	name = emri i marresit te mesazhit
//		pubkey = celesi publik i <name>
//		iv = random 8 bytes
//		key = random 8 bytes
		
//		encryptedKey = rsa(key, me pubkey)
//		encryptedMessage = des(message, me key)
//
//		part1 = base64.encode( utf8.encode(name) )
//		part2 = base64.encode(iv)
//		part3 = base64.encode(encryptedKey)
//		part4 = base64.encode(encryptedMessage)
//
//		return part1 + "." + part2 + "." + part3 + "." + part4
		
		SecureRandom random = new SecureRandom();
		byte[] iv = new byte[8];
		random.nextBytes(iv);
		
		byte[] key = new byte[8];
		random.nextBytes(key);
		String pubkey = "nQw3mK25q1DNMFE3j6UbkEldPlXKXuD0gSQ3HtZIfCA/AiOehzXbkXVpfsPTSqcekMzpqli+daNjHM+eFvlTcFdA1QjmTLUfTKe/DjVKIva9KKJpBKQ3SOdyI0i8uVRBahI9wENuIoi0L2RWjyn0fczsSTbzj+XTKaAn43iKBN7LamruadM0axSHRkx678OaTEPm4cM9w+37HyCBGcdqxlrZtpCcbK1GgntNYLfaLssxfPYtHQPHYNONisrkgMzxShPnfgp92fDijyvlxnNjLMF3Si0QNrU3/5c2de6vk+tPd5/4SVU64hU83ZE/GHlcZWV0s+gFRPKLlY9nCC4gBw==";
		byte[] encryptedKey = rsa(key, me pubkey);
		
		String part1 = Base64.getEncoder().encodeToString(utf8(name));
		String part2 = Base64.getEncoder().encodeToString(iv);
		
		
	}
	public static byte[] rsa(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }
	
	public static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }
	
	public static byte[] utf8(String name) {
		String s = "some text here";
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		return b;
	}

}
