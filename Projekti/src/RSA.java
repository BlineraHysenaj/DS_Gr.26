import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

	private final String keysPath = "C:\\Sources\\DS_Gr.26\\Projekti\\src\\Keys\\";
	private final String exportKeyPath = "C:\\Sources\\DS_Gr.26\\Projekti\\src\\Export\\";
	private final String enkriptimiPath = "C:\\Sources\\DS_Gr.26\\Projekti\\src\\Enkriptimi\\";
	private final static String keysPathEncrypt = "C:\\Sources\\DS_Gr.26\\Projekti\\src\\Enkriptimi\\";
	private final static SecureRandom random = new SecureRandom();

	public void createUser(String name) throws IOException, InvalidKeySpecException {
		try {
			FileWriter out;

			String pathPvtKey = keysPath + name + ".xml";
			String pathPubKey = keysPath + name + ".pub.xml";

			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048, new SecureRandom());
			KeyPair kp = kpg.generateKeyPair();

//			System.out.println("Private Key" + getPrivateKeyAsXML(kp));
//			System.out.println("Private Key" + getPublicKeyAsXML(kp));

			File outPvtKey = new File(pathPvtKey);
			File outPubKey = new File(pathPubKey);

			// Save Private Key
			out = new FileWriter(outPvtKey, false);
			out.write(getPrivateKeyAsXML(kp));
			out.close();

			// Save Public Key
			out = new FileWriter(outPubKey, false);
			out.write(getPublicKeyAsXML(kp));
			out.close();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public boolean deleteUser(String path) {

		File file = new File(keysPath + path);

		if (file.delete()) {
			return true;
		} else {
			return false;
		}

	}

	public void exportKey(String setKey, String path) throws IOException {

		FileWriter out;
		String readFile = readFile(keysPath + setKey);

		File file = new File(exportKeyPath + path);

		out = new FileWriter(file, false);
		out.write(readFile);
		out.close();
	}

	public void importKey(String setKey, String path) throws IOException {
		FileWriter out;
		String readFile = readFile(exportKeyPath + path);

		File file = new File(keysPath + setKey);

		out = new FileWriter(file, false);
		out.write(readFile);
		out.close();
	}

	public void writeMessage(String name, String message) throws IOException {
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
		//byte[] encryptedKey = rsa(key, me pubkey);
		
		String part1 = Base64.getEncoder().encodeToString(utf8(name));
		String part2 = Base64.getEncoder().encodeToString(iv);
		
		//the way how we kann write a file IN A PATH
		/////////////////////////////////////////////////////////////////////////
		String blerona = "hallo fiek";
		Scanner input = new Scanner(System.in);
		String in = input.nextLine();
		
		String path = keysPath + in +".pub.xml";
		
		File outPvtKey = new File(path);
		FileWriter out;
		out = new FileWriter(outPvtKey, false);
		out.write(blerona);
		out.close();
/////////////////////////////////////////////////////////////////////////

		
		
	}
	
	public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
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

	public void readMessage() {
		// TO DO
	}

	private String convertFromBigIntToString(String name, BigInteger bigInt) {
		byte[] bytesFromBigInt = getBytesFromBigInt(bigInt);
		String elementContent = Base64.getEncoder().encodeToString(bytesFromBigInt);

		return String.format("  <%s>%s</%s>%s", name, elementContent, name, "\n");
	}

	private byte[] getBytesFromBigInt(BigInteger bigInt) {
		byte[] bytes = bigInt.toByteArray();
		int length = bytes.length;
		if (length % 2 != 0 && bytes[0] == 0) {
			bytes = Arrays.copyOfRange(bytes, 1, length);
		}

		return bytes;
	}

	public String getPrivateKeyAsXML(KeyPair kp) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PrivateKey key = kp.getPrivate();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateCrtKeySpec spec = keyFactory.getKeySpec(key, RSAPrivateCrtKeySpec.class);

		StringBuilder sb = new StringBuilder();

		sb.append("<RSAKeyValue>" + "\n");
		sb.append(convertFromBigIntToString("Modulus", spec.getModulus()));
		sb.append(convertFromBigIntToString("Exponent", spec.getPublicExponent()));
		sb.append(convertFromBigIntToString("P", spec.getPrimeP()));
		sb.append(convertFromBigIntToString("Q", spec.getPrimeQ()));
		sb.append(convertFromBigIntToString("DP", spec.getPrimeExponentP()));
		sb.append(convertFromBigIntToString("DQ", spec.getPrimeExponentQ()));
		sb.append(convertFromBigIntToString("InverseQ", spec.getCrtCoefficient()));
		sb.append(convertFromBigIntToString("D", spec.getPrivateExponent()));
		sb.append("</RSAKeyValue>");

		return sb.toString();
	}

	public String getPublicKeyAsXML(KeyPair kp) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PublicKey key = kp.getPublic();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec spec = keyFactory.getKeySpec(key, RSAPublicKeySpec.class);

		StringBuilder sb = new StringBuilder();

		sb.append("<RSAKeyValue>" + "\n");
		sb.append(convertFromBigIntToString("Modulus", spec.getModulus()));
		sb.append(convertFromBigIntToString("Exponent", spec.getPublicExponent()));
		sb.append("</RSAKeyValue>");

		return sb.toString();
	}

	public boolean validateName(String name) {

		Pattern p = Pattern.compile("[^a-zA-Z0-9_.]");
		boolean hasSpecialChar = p.matcher(name).find();

		return hasSpecialChar;
	}

	public boolean checkFileIfExist(String name) {

		String filePathString = keysPath + name;

		File file = new File(filePathString);
		if (file.exists())
			return true;

		return false;
	}

	public String readFile(String filename) throws IOException {
		String content = null;
		File file = new File(filename);
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return content;
	}

	public void PrintKey(String printKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, IOException {

		Scanner input = new Scanner(new File(keysPath + printKey));

		while (input.hasNextLine()) {
			System.out.println(input.nextLine());
		}

	}

	public boolean checkFileIfExistIMPORT(String name) {

		String filePathString = exportKeyPath + name;

		File file = new File(filePathString);
		if (file.exists())
			return true;

		return false;
	}
	


}