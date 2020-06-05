import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
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
				;
				// Merr file ku deshiron ta ruaj tekstin e enkriptuar
				FileWriter myWriter = new FileWriter(shfrytezuesitPath + shfrytezuesi + ".xml");
				// Shkruaj ne ate file tekstin
				//myWriter.write(hashPassword(password1).toString());
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
		String readFile = readFile(shfrytezuesitPath + shfrytezuesi);
		if (checkPass.compareTo(readFile) == 0) {
			System.out.println(readFile);
		}
		else {
			System.err.println("Gabim: Shfrytezuesi ose fjalekalimi i gabuar.");
		}
		
	}
	public String get_SHA_512_SecurePassword(String passwordToHash, String salt){
	    String generatedPassword = null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-512");
	        md.update(salt.getBytes(StandardCharsets.UTF_8));
	        byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++){
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
		//byte[] pjesa5 = rsa.utf8(sender);
		String part5 = Base64.getEncoder().encodeToString(rsa.utf8(sender));
		
		// qitu sosht e bane pjesa e 6
		String faza3 = faza2 + "." + part5 + ".";
	}
//	public void writeMessageIntoFile(String name, String message, String path, String sender, String token) {
//		String faza2 = rsa.writeMessageIntoFile(name, message, path);
//	}
}
//public String createJWT(String id, String issuer, String subject, long ttlMillis) {
//
////The JWT signature algorithm we will be using to sign the token
//SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//long nowMillis = System.currentTimeMillis();
//Date now = new Date(nowMillis);
//
////We will sign our JWT with our ApiKey secret
//byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
//Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
////Let's set the JWT Claims
//JwtBuilder builder = Jwts.builder().setId(id)
//      .setIssuedAt(now)
//      .setSubject(subject)
//      .setIssuer(issuer)
//      .signWith(signatureAlgorithm, signingKey);
//
////if it has been specified, let's add the expiration
//if (ttlMillis > 0) {
//  long expMillis = nowMillis + ttlMillis;
//  Date exp = new Date(expMillis);
//  builder.setExpiration(exp);
//}  
//
////Builds the JWT and serializes it to a compact, URL-safe string
//return builder.compact();
//}
