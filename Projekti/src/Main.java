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

				if (!rsa.checkFileIfExist(rsa.keysPath, PrivateKey) || !rsa.checkFileIfExist(rsa.keysPath, PublicKey)) {
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

				if (rsa.checkFileIfExist(rsa.keysPath, PrivateKey)) {
					if (rsa.deleteUser(rsa.keysPath, PrivateKey)) {
						if (rsa.deleteUser(rsa.keysPath, PublicKey)) {
							System.out.println("Eshte larguar celesi privat 'keys/" + PrivateKey + "'");
							System.out.println("Eshte larguar celesi publik 'keys/" + PublicKey + "'");
						} else {
							System.err.println("Ju nuk mund ta fshini celesin privat sepse nuk egziston celesi publik");
						}
					}
				} else if (rsa.checkFileIfExist(rsa.keysPath, PublicKey)) {
					if (rsa.deleteUser(rsa.keysPath, PublicKey)) {
						System.out.println("Eshte larguar celesi publik 'keys/" + PublicKey + "'");
					}
				} else {
					System.err.println("Gabim: Celesi '" + args[1] + "' nuk ekziston.");
				}
			}
// *******************************************--EXPORT-KEY--*****************************************	
			else if (args[0].equalsIgnoreCase("export-key")) {
				if (args[1].equalsIgnoreCase("private")) {
					String PrivateKey = args[2] + ".xml";
					if (args.length == 3) {
						if (rsa.checkFileIfExist(rsa.keysPath, PrivateKey)) {
							rsa.printKey(PrivateKey);
						} else {
							System.err.println("Gabim: Celesi " + args[1] + " '" + args[2] + "' nuk ekziston.");
						}
					} else {
						if (rsa.checkFileIfExist(rsa.keysPath, PrivateKey)) {
							rsa.exportKey(PrivateKey, args[3]);
							System.out.println("Celesi privat u ruajt ne fajllin '" + args[3] + "'.");
						}
					}
				} else if (args[1].equalsIgnoreCase("public")) {
					String PublicKey = args[2] + ".pub.xml";

					if (rsa.checkFileIfExist(rsa.keysPath, PublicKey)) {
						if (args.length == 3) {
							if ((rsa.checkFileIfExist(rsa.keysPath, PublicKey))) {
								rsa.printKey(PublicKey);
							} else {
								System.err.println("Gabim: Celesi " + args[1] + " '" + args[2] + "' nuk ekziston.");
							}
						} else {
							rsa.exportKey(PublicKey, args[3]);
							System.out.println("Celesi publik u ruajt ne fajllin '" + args[3] + "'.");
						}
					}
				} else {
					System.out.println("Ju duhet te zgjedheni nese doni ta beni export celsin public|private!");
				}
			}
// *******************************************--IMPORT-KEY--*****************************************
			else if (args[0].equalsIgnoreCase("import-key")) {
				// merre emrin e file qe do te ruhet
				String PrivateKey = args[1] + ".xml";
				String PublicKey = args[1] + ".pub.xml";
				
				String[] splitKey = args[2].split("_");

				String PrivateKeyImport = splitKey[0] + "_private.xml";
				String PublicKeyImport = splitKey[0] + "_public.xml";

				// kontrollo nese egziston export-key
				if (rsa.checkFileIfExist(rsa.exportKeyPath, PrivateKeyImport)) {// import-key import exportKey.txt
					if (rsa.checkTheFile(PrivateKeyImport) == "private") {
						if (rsa.checkFileIfExist(rsa.exportKeyPath, PublicKeyImport)) {
							rsa.importKey(PrivateKeyImport, PrivateKey);
							rsa.importKey(PublicKeyImport, PublicKey);
							System.out.println("Celesi privat u ruajt ne fajllin 'keys/" + PrivateKey + "'.");
							System.out.println("Celesi publik u ruajt ne fajllin 'keys/" + PublicKey + "'.");
						}
					}
				} else if (rsa.checkFileIfExist(rsa.exportKeyPath, PublicKeyImport)) {
					rsa.importKey(args[2], PublicKey);
					System.out.println("Celesi publik u ruajt ne fajllin 'keys/" + PublicKey + "'.");
				} else if (args[2].contains("http")) {
					rsa.importOnlineKey(PublicKey, args[2]);
					System.out.println("Celesi publik u ruajt ne fajllin 'keys/" + PublicKey +"'.");
				} else {
					System.err.println("Gabim: Celesi '" + args[1] + "' ekziston paraprakisht.");
				}
			}
// *******************************************--WRITE-MESSAGE--**************************************
			else if (args[0].equalsIgnoreCase("write-message")) {

				String PublicKey = args[1] + ".pub.xml";
				if (args.length == 3) {
					if (rsa.checkFileIfExist(rsa.keysPath, PublicKey)) {
						System.out.println(rsa.writeMessage(args[1], args[2]));
					}
				} else if (rsa.checkFileIfExist(rsa.keysPath, PublicKey)) {
					rsa.writeMessageIntoFile(args[1], args[2], args[3]);
					System.out.println("Mesazhi i enkriptuar u ruajt ne fajllin " + args[3]);
				} else {
					System.err.println("Gabim: Celesi '" + args[1] + "' nuk ekziston.");
				}
			}
// *******************************************--READ-MESSAGE--***************************************
			else if (args[0].equalsIgnoreCase("read-message")) {
				String message = args[1];
				// E bejme split mesazhin e enkriptuar ne baze te pikes(".")
				message = message.replace(" ", "");
				String[] parts = message.split("[.]");

				if (parts.length == 4) {
					String part1Split = parts[0];
					String part2Split = parts[1];
					String part3Split = parts[2];
					String part4Split = parts[3];
					rsa.readMessage(part1Split, part2Split, part3Split, part4Split);
				} else {
					rsa.readMessageIntoFile(args[1]);
				}
			}
// *******************************************--HELP--***********************************************
			else if (args[0].equalsIgnoreCase("help")) {
				System.out.println(" vigenere encrypy|decrypt key \"plaintext|cyphertext\" ");
				System.out.println(" palyfair encrypy|decrypt key \"plaintext|cyphertext\" ");
				System.out.println(" frekuenca \"teksti\" ");
				System.out.println(" create-user emri-i-fajllit ");
				System.out.println(" delete-user emri-i-fajllit ");
				System.out.println(" export-key <public|private> <name> [file]");
				System.out.println(" import-key <name> <path> ");
				System.out.println(" ds write-message <name> <message> [file] ");
				System.out.println(" read-message <encrypted-message> ");
			} else {
				// throw new IllegalArgumentException("Sort type undefined");
				System.err.println("Argumenti i zgjedhur " + args[0] + " eshte jo-valid");
				System.out.println("Mundesia e zgjedhjes se algoritmit eshte: vigenere , playfair ose frekuenca.");
				System.out.println("Nese nuk keni njohuri, shkruani \"HELP\" ");
			}

		} catch (Exception e) {
			System.err.println("Error Message: " + e.toString());
		}
	}
}
