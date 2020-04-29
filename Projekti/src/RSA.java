package Projekti;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

//import ushtrime.RSAUtil;

//import java.security.*;
//import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;

public class RSA {

	private final String keysPath = "C:\\Users\\Gentrina\\Documents\\GitHub\\DS_Gr.26\\Projekti\\src\\Keys\\";
	private final String exportkeysPath="C:\\Users\\Gentrina\\Documents\\GitHub\\DS_Gr.26\\Projekti\\src\\export\\"

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

		final String exportKeyPath= "C:\\Users\\Gentrina\\Desktop\\Bleta\\DS_Gr.26-master\\Projekti\\src\\export\\";
		File file = new File(exportKeyPath + path);

		out = new FileWriter(file, false);
		out.write(readFile);
		out.close();
	}



    	public void PrintKey(String printKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, IOException {

			Scanner input = new Scanner(new File(keysPath + printKey));

			while (input.hasNextLine()) {
				System.out.println(input.nextLine());
			}
    	}
	

public void importKey(String name, String path) throws IOException {
		
		FileWriter out;
		String readFile = readFile( exportkeysPath+path );

		File file = new File(keysPath + name);
		out = new FileWriter(file, false);

		out.write(readFile);
		out.close();

	}

//public void exportKey(String setKey, String path) throws IOException {
//
//	FileWriter out;
//	String readFile = readFile(keysPath + setKey);
//
//	final String exportKeyPath= "C:\\Users\\Gentrina\\Desktop\\Bleta\\DS_Gr.26-master\\Projekti\\src\\export\\";
//	File file = new File(exportKeyPath + path);
//
//	out = new FileWriter(file, false);
//	out.write(readFile);
//	out.close();
//}
public static byte[] encrypt(String data, String  publicKey) 
   		throws Exception
    {  String pubkey = publicKey + ".pub.xml";
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, get(pubkey));
        return cipher.doFinal(data.getBytes());
    }
public static PublicKey get(String filename)
throws Exception {
String file = "C:\\Users\\Gentrina\\Desktop\\Bleta\\DS_Gr.26-master\\Projekti\\src\\Keys\\"+filename;
    byte[] keyBytes = Files.readAllBytes(Paths.get(file));

    X509EncodedKeySpec spec =
      new X509EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePublic(spec);
  }


public void writeMessage(String publicKey, String text)  throws Exception {
//	 byte[] message = text.getBytes("UTF8");
	 String enctext = Base64.getEncoder().encodeToString(encrypt(text, publicKey));

	 System.out.println(enctext);
}
	
	

	public void readMessage() {
		// TO DO
	}

	public String readFile(String filename) throws IOException {
		String content = null;
		File file = new File(filename); // For example, foo.txt
//		System.out.println(filename);
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
	
//	public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException
//	{
//		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//		cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyAsXML(publicKey));
//		return cipher.doFinal(data.getBytes());
//	}
	
	  public static byte[] encrypt(String data, String publicKey) 
		   		throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException
		    {
		        Cipher cipher = Cipher.getInstance("RSA");
		        cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyAsXML(publicKey));
		        return cipher.doFinal(data.getBytes());
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
	public boolean checkFileIfExist1(String name) {

		String filePathString = exportkeysPath + name;

		File file = new File(filePathString);
		if (file.exists())
			return true;

		return false;
	
}
	
	
	
	
	
}