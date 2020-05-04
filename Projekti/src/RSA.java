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
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class RSA {

	private final String keysPath = "C:\\Sources\\DS_Gr.26\\Projekti\\src\\Keys\\";
	private final String exportKeyPath = "C:\\Sources\\DS_Gr.26\\Projekti\\src\\Export\\";
	private final String enkriptimiPath = "C:\\Sources\\DS_Gr.26\\Projekti\\src\\Enkriptimi\\";	
	
	private Cipher encryptCipher = null;
	//private Cipher decryptCipher = null;  -> um vyn per dektiptim te des-it

	public void createUser(String name) throws IOException, InvalidKeySpecException {
		try {
			FileWriter out;

			String pathPvtKey = keysPath + name + ".xml";
			String pathPubKey = keysPath + name + ".pub.xml";

			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048, new SecureRandom());
			KeyPair kp = kpg.generateKeyPair();

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

	public String writeMessage(String name, String message) throws Exception {

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
		
		PublicKey publicKey = getPublicKeyFromXml(name);
		byte[] ciperKey = encrypt(key, publicKey);

		// DES
		DESKeySpec key1 = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// Instantiate the encrypter
		RSA crypt = new RSA(keyFactory.generateSecret(key1));		
		String encryptedMessage = crypt.encryptBase64(message);
		// Encrypted String:8dKft9vkZ4I=

		String part1 = Base64.getEncoder().encodeToString(utf8(name));
		String part2 = Base64.getEncoder().encodeToString(iv);
		String part3 = Base64.getEncoder().encodeToString(ciperKey);
		String part4 = encryptedMessage;
		return part1 + "." + part2 + "." + part3 + "." + part4;
	}
	public void writeMessage1(String name, String message, String path) throws Exception {
		writeMessage(name,message);
		FileWriter myWriter = new FileWriter(enkriptimiPath + path);
        myWriter.write(writeMessage(name,message));
        myWriter.close();
		
	}

	public RSA(SecretKey key) throws Exception {
		encryptCipher = Cipher.getInstance("DES");
		// decryptCipher = Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);
		// decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}

	public RSA() {
		// TODO Auto-generated constructor stub
	}

	public String encryptBase64(String unencryptedString) throws Exception {
		// Encode the string into bytes using utf-8
		byte[] unencryptedByteArray = unencryptedString.getBytes("UTF8");

		// Encrypt
		byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);

		// Encode bytes to base64 to get a string
		byte[] encodedBytes = Base64.getEncoder().encode(encryptedBytes);

		return new String(encodedBytes);
	}

	public PublicKey getPublicKeyFromXml(String name)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		String readfile = readFile(keysPath + name + ".pub.xml").replaceAll("\\s", "");
		final Pattern pattern = Pattern.compile("<Modulus>(.+?)</Modulus><Exponent>(.+?)</Exponent>", Pattern.DOTALL);
		final Matcher matcher = pattern.matcher(readfile);
		matcher.find();
		BigInteger moduls = new BigInteger(matcher.group(1));
		BigInteger exponent = new BigInteger(matcher.group(2));

		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(moduls, exponent);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey publicKey = kf.generatePublic(keySpec);

		return publicKey;
	}

	public static byte[] encrypt(byte[] data, PublicKey publicKey)
			throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException,
			NoSuchAlgorithmException, UnsupportedEncodingException {

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] utf8(String name) {
		byte[] b = name.getBytes(StandardCharsets.UTF_8);
		return b;
	}

	public void readMessage() {
		// TO DO
	}

	private String convertFromBigIntToString(String name, BigInteger bigInt) {
//		byte[] bytesFromBigInt = getBytesFromBigInt(bigInt);
//		String elementContent = Base64.getEncoder().encodeToString(bytesFromBigInt);
//
//		byte[] bytesFromString = Base64.getDecoder().decode(elementContent);
//		
//		
//		System.out.println("Key: " + elementContent + "\n");

		return "<" + name + ">" + bigInt + "</" + name + ">";
	}

	private byte[] getBytesFromBigInt(BigInteger bigInt) {
		byte[] bytes = bigInt.toByteArray();
		int length = bytes.length;

		// In any case, it creates arrays of 129 bytes rather than the
		// expected 128 bytes. So if the array's length is odd and the
		// leading byte is zero then trim the leading byte.
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

		sb.append("<RSAKeyValue>");
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

		sb.append("<RSAKeyValue>");
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