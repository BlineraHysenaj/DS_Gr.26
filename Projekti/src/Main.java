import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) {
		try {
			RSA rsa = new RSA();
// ******************************************--VIEGENERE--*******************************************
			if (args[0].equalsIgnoreCase("vigenere")) {
				Vigenere_Cipher Vigenere = new Vigenere_Cipher();
				String PlainText_Vigenere = "";
				String CipherTexti_Vigenere = "";
				String Keyword_Vigenere = args[2];

				if (args[1].equalsIgnoreCase("encrypt")) {
					PlainText_Vigenere = args[3];
					if (!Vigenere.isAlphaAndSpaces(Keyword_Vigenere)) {
						System.err.println("Argument jo-valid.");
						System.out.println(
								"Ju lutem perseritni hapat perseri dhe Keywordi duhet te permbaj vetem shkronja!! ");
						return;
					}
					System.out
							.println("Teksti i enkriptuar: " + Vigenere.Enkript(PlainText_Vigenere, Keyword_Vigenere));
				} else if (args[1].equalsIgnoreCase("decrypt")) {
					CipherTexti_Vigenere = args[3];

					if (!Vigenere.isAlphaAndSpaces(Keyword_Vigenere)) {
						System.err.println("Keywordi eshte jo-valid.");
						System.out.println(
								"Ju lutem perseritni hapat perseri dhe Keywordi duhet te permbaj vetem shkronja!! ");
						return;
					}

					System.out.println(
							"Teksti i dekriptuar: " + Vigenere.Dekript(CipherTexti_Vigenere, Keyword_Vigenere));
				} else {
					System.err.println("Argumenti i zgjedhur " + args[1] + " eshte jo-valid \n");
					System.out.println("Mundesia e zgjedhjes eshte: encrypt ose decrypt.");
				}

			}

			else if (args[0].equalsIgnoreCase("playfair")) {

// *******************************************--PLAYFAIR--*******************************************
				PlayFair_Cipher PlayFair_Cipher = new PlayFair_Cipher();

				String PlainText_PlayFair = "";
				String CipherTexti_PlayFair = "";
				String Keyword_PlayFair = args[2];

				if (args[1].equalsIgnoreCase("encrypt")) {
					PlainText_PlayFair = args[3];
					if (!PlayFair_Cipher.isAlphaAndSpaces(Keyword_PlayFair)) {
						System.err.println("Argument jo-valid.");
						System.out.println(
								"Ju lutem perseritni hapat perseri dhe Keywordi duhet te permbaj vetem shkronja!! ");
						return;
					}
					PlayFair_Cipher.setKeyWord(Keyword_PlayFair);
					PlayFair_Cipher.genMatrix();
					PlayFair_Cipher.encrypt(PlainText_PlayFair);
				} else if (args[1].equalsIgnoreCase("decrypt")) {
					CipherTexti_PlayFair = args[3];
					if (!PlayFair_Cipher.isAlphaAndSpaces(Keyword_PlayFair)) {
						System.err.println("Keywordi eshte jo-valid.");
						System.out.println(//
								"Ju lutem perseritni hapat perseri dhe Keywordi duhet te permbaj vetem shkronja!!!");
						return;
					}

					PlayFair_Cipher.setKeyWord(Keyword_PlayFair);
					PlayFair_Cipher.genMatrix();
					PlayFair_Cipher.decrypt(CipherTexti_PlayFair);
				} else {
					// throw new IllegalArgumentException("Sort type undefined");
					System.err.println("Argumenti i zgjedhur " + args[1] + " eshte jo-valid.\n");
					System.out.println("Mundesia e zgjedhjes eshte: encrypt ose decrypt.");
				}
			}

			else if (args[0].equalsIgnoreCase("frekuenca")) {
// *******************************************--FREKUENCA--******************************************
				Frekuenca frekuenca = new Frekuenca();
				frekuenca.Vepro(args[1]);
			}
// *******************************************--CREATE-USER--****************************************
			else if (args[0].equalsIgnoreCase("create-user")) {
				if (rsa.checkFileIfExist(args[1])) {
					//Duhet me shiku edhe nihere, se normal space nese e ban 
					//vjen si argumenti 2
					if (!rsa.validateName(args[1])) {
						rsa.createUser(args[1]);
						System.err.println(" Eshte krijuar celesi privat 'keys/" + args[1] + ".xml'");
						System.err.println(" Eshte krijuar celesi public 'keys/" + args[1] + ".pub.xml'");
					} else {
						System.err.println("Karakteret nuk jan valide!");
					}
				} else {
					System.err.println(" Celesi '" + args[1] + "' ekziston paraprakisht.");
				}

			}
// *******************************************--DELETE-USER--****************************************
			else if (args[0].equalsIgnoreCase("delete-user")) {
				System.out.println("Komanda per delete-user ");
			}
// *******************************************--IMPORT-KEY--*****************************************
			else if (args[0].equalsIgnoreCase("import-key")) {
				System.out.println("Komanda per import-key ");
			}
// *******************************************--EXPORT-KEY--*****************************************	
			else if (args[0].equalsIgnoreCase("export-key")) {
				System.out.println("Komanda per export-key ");
			}
// *******************************************--WRITE-MESSAGE--**************************************
			else if (args[0].equalsIgnoreCase("write-message")) {
				System.out.println("Komanda per write-message ");
			}
// *******************************************--READ-MESSAGE--***************************************
			else if (args[0].equalsIgnoreCase("read-message")) {
				System.out.println("Komanda per read-message ");
			}
// *******************************************--HELP--***********************************************
			else if (args[0].equalsIgnoreCase("help")) {
				System.out.println("vigenere encrypy|decrypt key \"plaintext|cyphertext\" ");
				System.out.println("palyfair encrypy|decrypt key \"plaintext|cyphertext\" ");
				System.out.println("frekuenca \"teksti\" ");
			} else {
				// throw new IllegalArgumentException("Sort type undefined");
				System.err.println("Argumenti i zgjedhur " + args[0] + " eshte jo-valid");
				System.out.println("Mundesia e zgjedhjes se algoritmit eshte: vigenere , playfair ose frekuenca.");
				System.out.println("Nese nuk keni njohuri, shkruani \"HELP\" ");
			}

		} catch (

		Exception e) {
			// Gotta catch 'em all!
			System.err.println("ERROR!");
		}
	}
}
