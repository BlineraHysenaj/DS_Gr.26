import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
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

	public File fileKeys = new File("src\\Keys");
	public String getKeysPath = fileKeys.getAbsolutePath();
	public String keysPath = getKeysPath + "\\";

	public File fileEnkriptimi = new File("src\\Enkriptimi");
	public String getEnkriptimiPath = fileEnkriptimi.getAbsolutePath();
	public String enkriptimiPath = getEnkriptimiPath + "\\";

	public File fileExport = new File("src\\Export");
	public String getExportKeyPath = fileExport.getAbsolutePath();
	public String exportKeyPath = getExportKeyPath + "\\";

	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	public void createUser(String name) throws IOException, InvalidKeySpecException {
		try {
			FileWriter out;

			String pathPrivatetKey = keysPath + name + ".xml";
			String pathPublicKey = keysPath + name + ".pub.xml";

			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048, new SecureRandom());
			KeyPair kp = kpg.generateKeyPair();

			File outPrivatetKey = new File(pathPrivatetKey);
			File outPublicKey = new File(pathPublicKey);

			// Ruaj ne File Private Key
			out = new FileWriter(outPrivatetKey, false);
			out.write(getPrivateKeyAsXML(kp));
			out.close();

			// Ruaj ne File Public Key
			out = new FileWriter(outPublicKey, false);
			out.write(getPublicKeyAsXML(kp));
			out.close();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public boolean deleteUser(String keysPath, String path) {

		File file = new File(keysPath + path);
		if (file.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public void exportKey(String keysName, String exportName) throws IOException {

		FileWriter out;
		// Lexo file-in
		String readFile = readFile(keysPath + keysName);

		// Export-o file-in
		File file = new File(exportKeyPath + exportName);
		out = new FileWriter(file, false);
		out.write(readFile);
		out.close();
	}

	public void importKey(String exportName, String keysName) throws IOException {

		// Lexo file-in
		String readFile = readFile(exportKeyPath + exportName);

		// Export-o file-in
		FileWriter out;
		File file = new File(keysPath + keysName);
		out = new FileWriter(file, false);
		out.write(readFile);
		out.close();

	}

	public void importOnlineKey(String exportName, String URLName) throws IOException {
		if (URLName.contains("http")) {
			String key = "";
			// E merr URL-n e nje webfaqe-je
	        URL url = new URL(URLName);
	        URLConnection con = url.openConnection();
	        InputStream is =con.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String line = null;
	        // lexon cdo rresht dhe e ruan ne variablen 'keys' 
	        while ((line = br.readLine()) != null) {
	        	key += line + "";
	        }
	        // Export-o file-in
			FileWriter out;
			File file = new File(keysPath + exportName);
			out = new FileWriter(file, false);
			out.write(key);
			out.close();	        
			
		}else {
			System.out.println("Gabim: URL-ja e dhene nuk permban celes valid.");
		}
	}

	public String writeMessage(String name, String message) throws Exception {
		// Inicializojm variablen iv, si 8 bajteshe
		byte[] iv = new byte[8];
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);

		SecureRandom randomKey = new SecureRandom();
		// Inicializojm variablen key, si 8 bajteshe random
		byte[] key = new byte[8];
		randomKey.nextBytes(key);

		// Merr PublicKey nga xml File-i
		PublicKey publicKey = getPublicKeyFromXml(name);
		// E enkriptojm me algoritmin RSA 'key' me PublicKey
		byte[] encryptedKey = rsaEncrypt(key, publicKey);

		// DES algoritmi
		DESKeySpec keySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

		// Enkriptojm ne Base64 mesazhin e dhene nga user-i
		String encryptedMessage = encryptBase64DES(message);

		// I kthejm vlerat e gjeneruara ne Base64
		String part1 = Base64.getEncoder().encodeToString(utf8(name));
		String part2 = Base64.getEncoder().encodeToString(iv);
		String part3 = Base64.getEncoder().encodeToString(encryptedKey);
		String part4 = encryptedMessage;
		String plaintext = part1 + "." + part2 + "." + part3 + "." + part4;

		return plaintext;
	}

	public void writeMessageIntoFile(String name, String message, String path) throws Exception {

		// Merr file ku deshiron ta ruaj tekstin e enkriptuar
		FileWriter myWriter = new FileWriter(enkriptimiPath + path);
		// Shkruaj ne ate file tekstin e enkriptuar
		myWriter.write(writeMessage(name, message));
		myWriter.close();
	}

	public void readMessage(String part1Split, String part2Split, String part3Split, String part4Split)
			throws Exception {
		String decryptedMessage = "";

		// Kthejm mesazhin e marrur qe e kemi ndar e kthejm ne Base64, e pastaj ne utf-8
		byte[] part1B64 = Base64.getDecoder().decode(part1Split.getBytes());
		String part1 = new String(part1B64, StandardCharsets.UTF_8);

		if (!checkFileIfExist(keysPath, part1 + ".xml")) { // kontrollojm nese egziston PrivateKey ne File
			System.out.println("Gabim: Celesi privat 'keys/" + part1 + ".xml' nuk ekziston");
		} else {
			// Merr PrivateKey nga xml File-i
			PrivateKey privateKey = getPrivateKeyFromXml(part1);

			byte[] part2B64 = Base64.getDecoder().decode(part2Split.getBytes());
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(part2B64);
			byte[] part3B64 = Base64.getDecoder().decode(part3Split.getBytes());

			// E dekriptojm 'part3B64' me privateKey
			byte[] key = rsaDecrypt(part3B64, privateKey);

			// DES algoritmi
			DESKeySpec keySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

			// E dekriptojm ne Base64 mesazhin e ndare te pjeses se katert
			decryptedMessage = decryptBase64DES(part4Split);

			System.out.println("Marresi: " + part1);
			System.out.println("Mesazhi: " + decryptedMessage);
		}
	}

	public void readMessageIntoFile(String path) throws Exception {

		// E merr qka ka brenda File-it
		String readFile = readFile(enkriptimiPath + path);
		readFile = readFile.replace(" ", "");
		String[] parts = readFile.split("[.]"); // e bejem split kontentin e File-it
		String part1Split = parts[0];
		String part2Split = parts[1];
		String part3Split = parts[2];
		String part4Split = parts[3];
		readMessage(part1Split, part2Split, part3Split, part4Split);
	}

	public String encryptBase64DES(String unencryptedString) throws Exception {

		// Merr bajtat e Mesazhit te pa enkriptuar
		byte[] unencryptedByteArray = unencryptedString.getBytes("UTF8");
		// Enkripton bajtat e dhene
		byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);
		// Kthen bajtat e marrur ne Base64
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

	public PublicKey getPublicKeyFromXml(String name)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		// E marrim PublicKey nga File-i i dhene
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

	public PrivateKey getPrivateKeyFromXml(String name)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		// E marrim PrivateKey nga File-i i dhene
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

	private String formatString(String name, BigInteger bigInt) {
		return "<" + name + ">" + bigInt + "</" + name + ">";
	}

	public byte[] utf8(String name) {
		// Kthen emrin e File-it te dhene ne byte
		byte[] b = name.getBytes(StandardCharsets.UTF_8);
		return b;
	}

	public String getPrivateKeyAsXML(KeyPair kp) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PrivateKey key = kp.getPrivate();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateCrtKeySpec spec = keyFactory.getKeySpec(key, RSAPrivateCrtKeySpec.class);
		StringBuilder sb = new StringBuilder();
		sb.append("<RSAKeyValue>");
		sb.append(formatString("Modulus", spec.getModulus()));
		sb.append(formatString("Exponent", spec.getPublicExponent()));
		sb.append(formatString("P", spec.getPrimeP()));
		sb.append(formatString("Q", spec.getPrimeQ()));
		sb.append(formatString("DP", spec.getPrimeExponentP()));
		sb.append(formatString("DQ", spec.getPrimeExponentQ()));
		sb.append(formatString("InverseQ", spec.getCrtCoefficient()));
		sb.append(formatString("D", spec.getPrivateExponent()));
		sb.append("</RSAKeyValue>");

		return sb.toString();
	}

	private String getPublicKeyAsXML(KeyPair kp) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PublicKey key = kp.getPublic();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec spec = keyFactory.getKeySpec(key, RSAPublicKeySpec.class);
		StringBuilder sb = new StringBuilder();
		sb.append("<RSAKeyValue>");
		sb.append(formatString("Modulus", spec.getModulus()));
		sb.append(formatString("Exponent", spec.getPublicExponent()));
		sb.append("</RSAKeyValue>");

		return sb.toString();
	}

	private String readFile(String fileName) throws IOException {
		String content = null;
		File file = new File(fileName);
		FileReader reader = null;
		try {
			reader = new FileReader(file); // e merr File-in
			char[] chars = new char[(int) file.length()];
			reader.read(chars); // i lexon cdo karakter ne File
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

	public void printKey(String fileName)
			throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, IOException {

		Scanner input = new Scanner(new File(keysPath + fileName));
		while (input.hasNextLine()) {
			System.out.println(input.nextLine());
		}
		input.close();
	}

	public boolean validateName(String name) {
		Pattern p = Pattern.compile("[^a-zA-Z0-9_.]");
		boolean hasSpecialChar = p.matcher(name).find();
		return hasSpecialChar;
	}

	public boolean checkFileIfExist(String path, String name) {

		File file = new File(path + name);
		if (file.exists())
			return true;
		return false;
	}

	public String checkTheFile(String exportName) throws IOException {

		String search = "P";
		String readFile = readFile(exportKeyPath + exportName);

		if (readFile.toLowerCase().indexOf(search.toLowerCase()) != -1) {
			return "private";
		} else {
			return "public";
		}
	}
}