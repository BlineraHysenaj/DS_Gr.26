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
// *******************************************--IMPORT-KEY--*****************************************
			else if (args[0].equalsIgnoreCase("import-key")) {
				System.out.println("Komanda per import-key ");
			}
// *******************************************--EXPORT-KEY--*****************************************	
else if (args[0].equalsIgnoreCase("export-key")) {
				
				if(args[1].equalsIgnoreCase("private")|| args[1].equalsIgnoreCase("public")) 
				{
				 //rsa e kom shkru me 1 kta qe e zgjedhim na me 2 kta qe tdel kompjuterit
				String PublicKey = args[2] + ".pub.xml";
				String PrivateKey = args[2] + ".xml";
                 if(args.length==3) {
                if (rsa.checkFileIfExist(PrivateKey)) {
				rsa.PrintKey(PrivateKey);
				
				}
                
				else if (rsa.checkFileIfExist(PublicKey)) {
					
					rsa.PrintKey(PublicKey);
				}
				else  {
					System.out.println("Gabim: Celesi publik "+args[2]+ " nuk egziston");
					}
				}
			
			else {
               if(rsa.checkFileIfExist(PublicKey)) {
                    	  rsa.exportKey(PublicKey, args[3], args);
	}	
	  }
         }
			
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
