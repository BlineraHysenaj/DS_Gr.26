import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

	private final String keysPath = "C:\\Users\\Fehmi Hysenaj\\Desktop\\github\\DS_Gr.26\\Projekti\\src\\Keys\\";

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

	public void importKey() {
		// TO DO
	}

	public void exportKey(String setKey,String path,String args[]) throws IOException {
		final String exportPath="C:\\Users\\Fehmi Hysenaj\\Desktop\\github\\DS_Gr.26\\Projekti\\src\\Keys\\";
		FileWriter out;
		String readFile = readFile(keysPath + setKey);
		File file = new File(exportPath + path);
		if (args[1].equals("public"))
        {
            System.out.println("Celesi publik u ruajt ne filen  "+ args[3] );
        }
		else {
			
			 System.out.println("Celesi privat eshte ruajtur ne filen  "+args[3] );
		}
		out = new FileWriter(file, false);
		out.write(readFile);
		out.close();
	}

	public void writeMessage() {
		// TO DO
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
	public String readFile(String filename) throws IOException {
		String content = null;
		File file = new File(filename); // For example, foo.txt
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

	public void PrintKey(String privateKey) throws FileNotFoundException {
		Scanner input = new Scanner(new File("foo.txt"));

		while (input.hasNextLine())
		{
		   System.out.println(input.nextLine());
		   input.close();
		}
		 }

	public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }
  
  public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
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
}