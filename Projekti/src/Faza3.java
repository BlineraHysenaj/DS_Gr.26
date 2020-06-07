import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Faza3 {
	private String algorithmHash = "SHA-512";
	private Scanner input = new Scanner(System.in);
	public FileWriter out;
	public File fileShfrytezuesit = new File("src\\Shfrytezuesit");
	public String getShfrytezuesitPath = fileShfrytezuesit.getAbsolutePath();
	public String shfrytezuesitPath = getShfrytezuesitPath + "\\";
	RSA rsa = new RSA();
	JwtRSA jwtrsa = new JwtRSA();

	public boolean checkPassword(String shfrytezuesi)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		System.out.println("Jepni fjalekalimin: ");
		String password1 = input.nextLine();
		if ((password1.length() > 6) && (checkStringForNumbers(password1) == true | checkPasswordForSymbols(password1) == true)) {
			System.out.println("Perserit fjalekalimin: ");
			String password2 = input.nextLine();
			if (password1.compareTo(password2) == 0) {
				// Merr file ku deshiron ta ruaj tekstin e enkriptuar
				FileWriter myWriter = new FileWriter(shfrytezuesitPath + shfrytezuesi + ".txt");
				// Shkruaj ne ate file tekstin
				// myWriter.write(hashPassword(password1).toString());
				myWriter.write(get_SHA_512_SecurePassword(password1, algorithmHash));
				myWriter.close();
				System.out.println("Eshte krijuar shfrytezuesi '" + shfrytezuesi + "'.");
				return true;
			} else {
				System.err.println("Gabim: Fjalekalimet nuk perputhen.");
			}
		} else {
			System.err.println("Gabim: Fjalekalimi duhet te permbaje se paku nje numer ose simbol.");
		}
		input.close();
		return false;
	}

	public void login(String shfrytezuesi) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println("Jepni fjalekalimin: ");
		String fjalkalimi = input.nextLine();

		String checkPass = get_SHA_512_SecurePassword(fjalkalimi, algorithmHash);
		// Lexo file-in
		String readFile = readFile(shfrytezuesitPath + shfrytezuesi + ".txt");
		if (checkPass.compareTo(readFile) == 0) {
			System.out.println("Token: " + generateToken(shfrytezuesi));
		} else {
			System.err.println("Gabim: Shfrytezuesi ose fjalekalimi i gabuar.");
		}

	}

	public String generateToken(String shfrytezuesi)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		PrivateKey privateKey = rsa.getPrivateKeyFromXml(shfrytezuesi);

		String token = jwtrsa.generateToken(privateKey, shfrytezuesi);
		// System.out.println("Token: " + token);

		return token;
	}

	public boolean statusToken(String token, String shfrytezuesi)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

		PublicKey publicKey = rsa.getPublicKeyFromXml(shfrytezuesi);
		ArrayList<String> claims = jwtrsa.verifyToken(token, publicKey);

		String str = claims.get(2);
		String[] arrOfStr = str.split(":");
		long skadimi = Long.parseLong(arrOfStr[1].trim()) / 60000;
		long kohaAktuale = System.currentTimeMillis() / 60000;

		long difMin = (kohaAktuale - skadimi);
		if (difMin > 20) {
			return false;
		} else {
			return true;
		}
	}

	public String get_SHA_512_SecurePassword(String passwordToHash, String salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes(StandardCharsets.UTF_8));
			byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	private static boolean checkStringForNumbers(String str) {
		char ch;		
		boolean numberFlag = false;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (Character.isDigit(ch)) {
				numberFlag = true;
			}
		}
		return numberFlag;
	}

	public String readFile(String fileName) throws IOException {
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

	public void writeMessage(String name, String message, String sender, String token) throws Exception {
		String faza2 = rsa.writeMessage(name, message);

		if (statusToken(token, sender) == true) {
			String part5 = Base64.getEncoder().encodeToString(rsa.utf8(sender));
			String part6 = Base64.getEncoder().encodeToString(signatureMessage(rsa.encryptBase64DES(message), sender));
			String faza3 = faza2 + "." + part5 + "." + part6;
			System.out.println(faza3);
		} else {
			System.out.println("Token-i eshte jo-valid!");
		}
	}

	private byte[] signatureMessage(String message, String sender) throws IOException, NoSuchAlgorithmException,
			InvalidKeyException, InvalidKeySpecException, SignatureException {

		if (rsa.checkFileIfExist(shfrytezuesitPath, sender + ".txt")) {
			if (rsa.checkFileIfExist(rsa.keysPath, sender + ".xml")) {
				PrivateKey privateKey = rsa.getPrivateKeyFromXml(sender);

				// Creating a Signature object
				Signature sign = Signature.getInstance("SHA1withRSA");// SHA256withRSA

				// Initialize the signature
				sign.initSign(privateKey);
				byte[] bytes = message.getBytes();

				// Adding data to the signature
				sign.update(bytes);

				// Calculating the signature
				byte[] signature = sign.sign();
				return signature;
			} else {
				System.err.println("Fajlli i juaj per PrivateKey nuk u gjend!");
			}
		} else {
			System.err.println("Shfrytezuesi nuk u gjend!");
		}
		return null;
	}

	public String readMessage1(String part1Split, String part2Split, String part3Split, String part4Split, String sender,
			String token) throws Exception {

		String message = rsa.readMessage(part1Split, part2Split, part3Split, part4Split);
		byte[] senderpart1B64 = Base64.getDecoder().decode(sender.getBytes());
		String senderUFT8 = new String(senderpart1B64, StandardCharsets.UTF_8);

		System.out.println(message);
		System.out.println("Derguesi: " + senderUFT8);
		System.out.println("Nenshkrimi :" + verifySignature(part4Split, senderUFT8));
		return null;
	}

	public String verifySignature(String message, String senderUFT8) throws NoSuchAlgorithmException,
			InvalidKeySpecException, IOException, InvalidKeyException, SignatureException {

		if (rsa.checkFileIfExist(rsa.keysPath, senderUFT8 + ".pub.xml")) {

			// Creating a Signature object
			Signature sign = Signature.getInstance("SHA1withRSA");
			PrivateKey privateKey = rsa.getPrivateKeyFromXml(senderUFT8);
			// Initialize the signature
			sign.initSign(privateKey);

			byte[] bytes = message.getBytes();

			// Adding data to the signature
			sign.update(bytes);

			// Calculating the signature
			byte[] signature = sign.sign();

			PublicKey publicKey = rsa.getPublicKeyFromXml(senderUFT8);
			// Initializing the signature
			sign.initVerify(publicKey);

			// Verify the signature
			boolean bool = sign.verify(signature);

			if (bool) {
				System.out.println("Signature verified");
			} else {
				System.out.println("Signature failed");
			}
		}else {
			return "mungon celesi publik '" + senderUFT8 + "'";
		}
		return null;		
	}

	private boolean checkPasswordForSymbols(String password) {

		String PASSWORD_PATTERN = "([@#$%^&-+=()]+)";
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		if(matcher.find() == true) {
			return true;
		}
		else {
			return false;
		}
	}

public String  readMessage(String part1Split, String part2Split, String part3Split, String part4Split, String sender, String token) throws Exception {
		String message = rsa.readMessage(part1Split, part2Split, part3Split, part4Split);
		
	        //Creating a Signature object
		Signature sign = Signature.getInstance("SHA256withDSA");
		PrivateKey privateKey = rsa.getPrivateKeyFromXml(rsa.keysPath + sender + ".xml");
		//Initialize the signature
		sign.initSign(privateKey);
		
		byte[] bytes = message.getBytes();      

	        //Adding data to the signature
		sign.update(bytes);
	
	       //Calculating the signature
		byte[] signature = sign.sign();
		
	
		
		return null;
	}
}
public void writeMessage(String name, String message, String sender, String token) throws Exception {
	String faza2 = rsa.writeMessage(name, message);
	
	String part5 = Base64.getEncoder().encodeToString(rsa.utf8(sender));
	
	
	
}
