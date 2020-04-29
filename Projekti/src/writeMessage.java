import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class writeMessage {
	final static  IvParameterSpec iv = new IvParameterSpec(new byte[8]);
	private final static String keysPath = "C:\\Sources\\DS_Gr.26\\Projekti\\src\\Keys\\";
	public static void main(String[] args) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		writeMessage("blerona","pershendetje nga fiek");
	}
	public static void writeMessage(String name, String message) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
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
		String string = Base64.getEncoder().encodeToString(key);
		
		
		
		
//		String xmlString = "<RSAKeyValue>\r\n"
//				+ "  <Modulus>2V77524NznSeBTJPrKQevi75Jwjs+TD2AIolWDY+RHxFRU1PLQ894AV/0gBYHlCFsls4ofjVk/gGGJtko10KeXXUMfFB52W8GeRmG2u0fY5+C/RGbJnFu16vLODk8P4jXv0I3c+aE+vJTXIYlCvw4WgFq4bwKbWRWfKMDZNqpwp1pLqEtwEFVYgPDTjrl55l1SgwfHxVU+QbB6+LchPhBzZYXRzAqc53yN/Q+cqp0/Ljit641F5rOWoGbUD373B+mbjenjLHDXvQIP/RRMf//Y4u+0gUQirBgMaKmmJ2f2YnKP19E+ZkdZi2x0370y5eLO4/7P/ojX2zKqDXeGtPnw==</Modulus>\r\n"
//					+ "  <Exponent>AQAB</Exponent>\r\n" + "</RSAKeyValue>";
		final Pattern pattern = Pattern.compile("<Modulus>(.+?)</Modulus>", Pattern.DOTALL);
		final Matcher matcher = pattern.matcher(keysPath + name);
		matcher.matches();
		matcher.find();
		String pubKey = matcher.group(1);
		
			
	
		byte[] encryptedKey = rsa(string, pubKey);
		
		String part1 = Base64.getEncoder().encodeToString(utf8(name));
		String part2 = Base64.getEncoder().encodeToString(iv);
		
		
	}
	public static byte[] utf8(String name) {
		String s = "some text here";
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		return b;
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
	
	
}
