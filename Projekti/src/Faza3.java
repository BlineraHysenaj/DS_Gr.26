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
	