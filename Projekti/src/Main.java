import java.io.File;
import java.security.KeyPair;

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
				String PrivateKey = args[1] + ".xml";
				String PublicKey = args[1] + ".pub.xml";

				if (!rsa.checkFileIfExist(PrivateKey) || !rsa.checkFileIfExist(PublicKey)) {
					if (!rsa.validateName(args[1]) && args.length <= 2) {
						rsa.createUser(args[1]);
						System.out.println(" Eshte krijuar celesi privat 'keys/" + PrivateKey + "'");
						System.out.println(" Eshte krijuar celesi public 'keys/" + PublicKey + "'");
					} else {
						System.err.println("Karakteret nuk jan valide!");
					}
				} else {
					System.err.println(" Celesi '" + args[1] + "' ekziston paraprakisht.");
				}

			}
// *******************************************--DELETE-USER--****************************************
			else if (args[0].equalsIgnoreCase("delete-user")) {
				String PrivateKey = args[1] + ".xml";
				String PublicKey = args[1] + ".pub.xml";

				if (rsa.checkFileIfExist(PrivateKey)) {
					if (rsa.deleteUser(PrivateKey)) {
						System.out.println("Eshte larguar celesi privat 'keys/" + PrivateKey + "'");
					}
				}
				if (rsa.checkFileIfExist(PublicKey)) {
					if (rsa.deleteUser(PublicKey)) {
						System.out.println("Eshte larguar celesi publik 'keys/" + PublicKey + "'");
					}
				} else {
					System.err.println("Gabim: Celesi '" + args[1] + "' nuk ekziston.");
				}
			}
// *******************************************--EXPORT-KEY--*****************************************	
			else if (args[0].equalsIgnoreCase("export-key")) {
				if (args[1].equalsIgnoreCase("private") || args[1].equalsIgnoreCase("public")) {
					String PrivateKey = args[2] + ".xml";
					String PublicKey = args[2] + ".pub.xml";
					if (args.length == 3) {
						if (rsa.checkFileIfExist(PrivateKey)) {
							rsa.PrintKey(PrivateKey);
						} else if (rsa.checkFileIfExist(PublicKey)) {
							rsa.PrintKey(PublicKey);
						} else {
							System.err.println("Gabim: Celesi " + args[1] + " '" + args[2] + "' nuk ekziston.");
						}
					} else {
						if (rsa.checkFileIfExist(PublicKey)) {
							rsa.exportKey(PublicKey, args[3]);
							System.out.println("Celesi publik u ruajt ne fajllin '" + args[3] + "'.");
						} else if (rsa.checkFileIfExist(PrivateKey)) {
							rsa.exportKey(PrivateKey, args[3]);
							System.out.println("Celesi privat u ruajt ne fajllin '" + args[3] + "'.");
						}
					}
				} else {
					System.out.println("Ju duhet te zgjedheni nese doni ta bani export celsin public|private !");
				}
			}
// *******************************************--IMPORT-KEY--*****************************************
			else if (args[0].equalsIgnoreCase("import-key")) {
//				String search  = "http";
//				if ( args[2].toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
//				   rsa.importGet(args[3]);
//
//				}
				String PrivateKey = args[1] + ".xml";
				String PublicKey = args[1] + ".pub.xml";
				if (rsa.checkFileIfExistIMPORT(args[2])) {
					rsa.importKey(PrivateKey, args[2]);
					rsa.importKey(PublicKey, args[2]);
					System.out.println("Celesi privat u ruajt ne fajllin 'keys/" + PrivateKey + "'.");
					System.out.println("Celesi publik u ruajt ne fajllin 'keys/" + PublicKey + "'.");
				} else if (rsa.checkFileIfExistIMPORT(args[2])) {
					rsa.importKey(PublicKey, args[2]);
					System.out.println("Celesi publik u ruajt ne fajllin 'keys/" + PublicKey + "'.");
				}
			}
// *******************************************--WRITE-MESSAGE--**************************************
			else if (args[0].equalsIgnoreCase("write-message")) {
				
				String PublicKey = args[1] + ".pub.xml";
				if (args.length == 3) {
					if (rsa.checkFileIfExist(PublicKey)) {
						rsa.writeMessage(PublicKey, args[2]);
					}
				}
				else if (rsa.checkFileIfExist(args[2])){
					
				}
				else {
					System.err.println("Gabim: Celesi '" + args[1] + "' nuk ekziston.");
				}
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
