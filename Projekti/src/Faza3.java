import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.PBEKeySpec;
public class Faza3 {
	private String algorithmHash = "SHA-512";
	private Scanner input = new Scanner(System.in);
	public FileWriter out;
	public File fileShfrytezuesit = new File("src\\Shfrytezuesit");
	public String getShfrytezuesitPath = fileShfrytezuesit.getAbsolutePath();
	public String shfrytezuesitPath = getShfrytezuesitPath + "\\";
	RSA rsa = new RSA();

	public boolean checkPassword(String shfrytezuesi)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		System.out.println("Jepni fjalekalimin: ");
		String password1 = input.nextLine();
		if (password1.length() > 6 && checkString(password1) != 0) {
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
			System.out.println("Token: " + GenerateToken(shfrytezuesi));
		} else {
			System.err.println("Gabim: Shfrytezuesi ose fjalekalimi i gabuar.");
		}

	}

	public String GenerateToken(String shfrytezuesi)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

//		String privateKey = readFile(rsa.keysPath + shfrytezuesi + ".xml");
//		System.out.println("privateKey" + privateKey);
		PrivateKey privateKey = rsa.getPrivateKeyFromXml(shfrytezuesi);
		PublicKey publicKey = rsa.getPublicKeyFromXml(shfrytezuesi);
		
		return "cc5793fcce9cdd9f33ba817a85b5a95be29b74ddd1d811a81dc109066cb73d8d93708a5a6cada7b";
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

	private static int checkString(String str) {
		char ch;
		int count = 0;
		boolean numberFlag = false;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (Character.isDigit(ch)) {
				numberFlag = true;
				count++;
			}
			if (numberFlag)
				return count;
		}
		return 0;
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
		// byte[] pjesa5 = rsa.utf8(sender);
		String part5 = Base64.getEncoder().encodeToString(rsa.utf8(sender));

		// qitu sosht e bane pjesa e 6
		String faza3 = faza2 + "." + part5 + ".";
	}
}
public void login(String shfrytezuesi) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	System.out.println("Jepni fjalekalimin: ");
	String fjalkalimi = input.nextLine();

	String checkPass = get_SHA_512_SecurePassword(fjalkalimi, algorithmHash);
	
	String readFile = readFile(shfrytezuesitPath + shfrytezuesi + ".txt");
	if (checkPass.compareTo(readFile) == 0) {
		System.out.println("Token: " + generateToken(shfrytezuesi));
	} else {
		System.err.println("Gabim: Shfrytezuesi ose fjalekalimi i gabuar.");
	}

}
public void statusToken(String token, String shfrytezuesi)
		throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

	PublicKey publicKey = rsa.getPublicKeyFromXml(shfrytezuesi);
	ArrayList<String> claims = jwtrsa.verifyToken(token, publicKey);

	String str = claims.get(2);
	String[] arrOfStr = str.split(":");
	long skadimi = Long.parseLong(arrOfStr[1].trim()) / 60000;
	long kohaAktuale = System.currentTimeMillis() / 60000;

	
	long difMin = (kohaAktuale - skadimi);

	
	if (difMin > 20) {
		System.out.println("Token-i nuk eshte valid, ai ka skaduar!");
	} else {
		System.out.println("Token-i eshte ende valid");
	}
}
