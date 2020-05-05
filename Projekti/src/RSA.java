import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
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
import javax.crypto.spec.IvParameterSpec;

public class RSA {

	private File file1 = new File("src\\Keys");
	private String keysPath1 = file1.getAbsolutePath();
	private String keysPath = keysPath1 + "\\";

	private File file2 = new File("src\\Enkriptimi");
	private String enkriptimiPath1 = file2.getAbsolutePath();
	private String enkriptimiPath = enkriptimiPath1 + "\\";

	private File file3 = new File("src\\Export");
	private String exportKeyPath1 = file3.getAbsolutePath();
	private String exportKeyPath = exportKeyPath1 + "\\";

	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

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
		// initialization vector
		byte[] iv = new byte[8];
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);

		SecureRandom randomKey = new SecureRandom();
		byte[] key = new byte[8];
		randomKey.nextBytes(key);

		System.out.println("DES key Encrypt: " + Base64.getEncoder().encodeToString(key));

		PublicKey publicKey = getPublicKeyFromXml(name);
		byte[] encryptedKey = rsaEncrypt(key, publicKey);

		// DES
		DESKeySpec keySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(keySpec);

		System.out.println("DES SecretKey Encrypt: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));

		encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

		String encryptedMessage = encryptBase64DES(message);

		String part1 = Base64.getEncoder().encodeToString(utf8(name));
		String part2 = Base64.getEncoder().encodeToString(iv);
		String part3 = Base64.getEncoder().encodeToString(encryptedKey);
		String part4 = encryptedMessage;

		String plaintext = part1 + "." + part2 + "." + part3 + "." + part4;

		System.out.println("read-message \"" + plaintext + "\"");

		return plaintext;
	}

	public String readMessage(String message) throws Exception {
		String decryptedMessage = "";
		message = message.replace(" ", "");
		String[] parts = message.split("[.]");
		String part1Split = parts[0];
		byte[] part1B64 = Base64.getDecoder().decode(part1Split.getBytes());
		String part1 = new String(part1B64, StandardCharsets.UTF_8);

		String part2Split = parts[1];
		byte[] part2B64 = Base64.getDecoder().decode(part2Split.getBytes());
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(part2B64);

		if (parts.length == 4) {
			String part3Split = parts[2];
			byte[] part3B64 = Base64.getDecoder().decode(part3Split.getBytes());

			PrivateKey privateKey = getPrivateKeyFromXml(part1);
			byte[] key = rsaDecrypt(part3B64, privateKey);
			System.out.println("DES key Decrypt: " + Base64.getEncoder().encodeToString(key));

			String part4Split = parts[3];
			// DES
			DESKeySpec keySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);

			System.out.println("DES SecretKey Decrypt: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));

			decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

			decryptedMessage = decryptBase64DES(part4Split);

		} else {
			if (checkFileIfExist(enkriptimiPath + part1 + ".txt")) {
				String readfile = readFile(enkriptimiPath + part1 + ".txt");
			}
		}

		return decryptedMessage;
	}

	public void writeMessage1(String name, String message, String path) throws Exception {

		FileWriter myWriter = new FileWriter(enkriptimiPath + path);
		myWriter.write(writeMessage(name, message));
		myWriter.close();

	}

	private String encryptBase64DES(String unencryptedString) throws Exception {

		byte[] unencryptedByteArray = unencryptedString.getBytes("UTF8");
		byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);
		byte[] encodedBytes = Base64.getEncoder().encode(encryptedBytes);

		return new String(encodedBytes);
	}

	private String decryptBase64DES(String encryptedString) throws Exception {
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedString.getBytes());
		byte[] decryptedByteArray = decryptCipher.doFinal(decodedBytes);

		return new String(decryptedByteArray, "UTF8");
	}

	private byte[] rsaEncrypt(byte[] data, PublicKey publicKey) throws BadPaddingException, IllegalBlockSizeException,
			InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.PUBLIC_KEY, publicKey);
		return cipher.doFinal(data);
	}

	private byte[] rsaDecrypt(byte[] part3, PrivateKey privateKey) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.PRIVATE_KEY, privateKey);
		return cipher.doFinal(part3);

	}

	private PublicKey getPublicKeyFromXml(String name)
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

	private PrivateKey getPrivateKeyFromXml(String name)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String readfile = readFile(keysPath + name + ".xml").replaceAll("\\s", "");
		final Pattern pattern = Pattern.compile(
				"<Modulus>(.+?)</Modulus><Exponent>(.+?)</Exponent><P>(.+?)</P><Q>(.+?)</Q><DP>(.+?)</DP><DQ>(.+?)</DQ><InverseQ>(.+?)</InverseQ><D>(.+?)</D>",
				Pattern.DOTALL);
		final Matcher matcher = pattern.matcher(readfile);
		matcher.find();

		BigInteger moduls = new BigInteger(matcher.group(1));
		BigInteger exponent = new BigInteger(matcher.group(2));
		BigInteger P = new BigInteger(matcher.group(3));
		BigInteger Q = new BigInteger(matcher.group(4));
		BigInteger DP = new BigInteger(matcher.group(5));
		BigInteger DQ = new BigInteger(matcher.group(6));
		BigInteger InverseQ = new BigInteger(matcher.group(7));
		BigInteger D = new BigInteger(matcher.group(8));

		RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(moduls, exponent, D, P, Q, DP, DQ, InverseQ);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = kf.generatePrivate(keySpec);

		return privateKey;

	}

	private String FormatString(String name, BigInteger bigInt) {
		return "<" + name + ">" + bigInt + "</" + name + ">";
	}

	private byte[] utf8(String name) {
		byte[] b = name.getBytes(StandardCharsets.UTF_8);
		return b;
	}

	public String getPrivateKeyAsXML(KeyPair kp) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PrivateKey key = kp.getPrivate();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateCrtKeySpec spec = keyFactory.getKeySpec(key, RSAPrivateCrtKeySpec.class);
		StringBuilder sb = new StringBuilder();
		sb.append("<RSAKeyValue>");
		sb.append(FormatString("Modulus", spec.getModulus()));
		sb.append(FormatString("Exponent", spec.getPublicExponent()));
		sb.append(FormatString("P", spec.getPrimeP()));
		sb.append(FormatString("Q", spec.getPrimeQ()));
		sb.append(FormatString("DP", spec.getPrimeExponentP()));
		sb.append(FormatString("DQ", spec.getPrimeExponentQ()));
		sb.append(FormatString("InverseQ", spec.getCrtCoefficient()));
		sb.append(FormatString("D", spec.getPrivateExponent()));
		sb.append("</RSAKeyValue>");

		return sb.toString();
	}

	private String getPublicKeyAsXML(KeyPair kp) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PublicKey key = kp.getPublic();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec spec = keyFactory.getKeySpec(key, RSAPublicKeySpec.class);
		StringBuilder sb = new StringBuilder();
		sb.append("<RSAKeyValue>");
		sb.append(FormatString("Modulus", spec.getModulus()));
		sb.append(FormatString("Exponent", spec.getPublicExponent()));
		sb.append("</RSAKeyValue>");

		return sb.toString();
	}

	private String readFile(String filename) throws IOException {
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

	public boolean checkFileIfExistIMPORT(String name) {
		String filePathString = exportKeyPath + name;
		File file = new File(filePathString);
		if (file.exists())
			return true;

		return false;
	}
}