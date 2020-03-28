public class Main {

	public static void main(String[] args) {		
		try {
// ******************************************--VIEGENERE--************************************
			if (args[0].equalsIgnoreCase("vigenere")) {
				Vigenere_Cipher Vigenere = new Vigenere_Cipher();
				String PlainText_Vigenere = "";
				String CipherTexti_Vigenere = "";
				String Keyword_Vigenere = args[2];

				if (args[1].equalsIgnoreCase("encrypt")) {
					PlainText_Vigenere = args[3];
					if (!Vigenere.isAlphaAndSpaces(Keyword_Vigenere)) {
						System.err.println("Keywordi-i duhet te permbajt vetem shkronja");
						return;
					}
					System.out.println("Teksti i enkriptuar: " + Vigenere.Enkript(PlainText_Vigenere, Keyword_Vigenere));
				} 
				else if (args[1].equalsIgnoreCase("decrypt")) {
					CipherTexti_Vigenere = args[3];

					if (!Vigenere.isAlphaAndSpaces(Keyword_Vigenere)) {
						System.err.println("Keywordi-i duhet te permbajt vetem shkronja");
						return;
					}

					System.out.println(
							"Teksti i dekriptuar: " + Vigenere.Dekript(CipherTexti_Vigenere, Keyword_Vigenere));
				} else {
					System.out.println("Kodi i ,ju keni shkruar zgjedhjen gabim: " + args[1]);
				}

			}		
			
			if (args[0].equalsIgnoreCase("playfair")) {
				
// *******************************************--PLAYFAIR--***************************************
				PlayFair_Cipher PlayFair_Cipher = new PlayFair_Cipher();

				String PlainText_PlayFair = "";
				String CipherTexti_PlayFair = "";
				String Keyword_PlayFair = args[2];
				String Keyword_PlayFair_Decrypt=args[2];

				if (args[1].equalsIgnoreCase("encrypt")) {
					PlainText_PlayFair = args[3];
					if (!PlayFair_Cipher.isAlphaAndSpaces(Keyword_PlayFair)) {
						System.err.println("Keywordi-i duhet te permbajt vetem shkronja");
						return;
					}
					PlayFair_Cipher.setKeyWord(Keyword_PlayFair);
					PlayFair_Cipher.genMatrix();
					PlayFair_Cipher.encrypt(PlainText_PlayFair);
				}
				else if (args[1].equals("decrypt")) {
					if (!PlayFair_Cipher.isAlphaAndSpaces(Keyword_PlayFair_Decrypt)) {
						System.err.println("Keywordi-i duhet te permbajt vetem shkronja");
						return;
					}
					CipherTexti_PlayFair = args[3];
					
					PlayFair_Cipher.decrypt(CipherTexti_PlayFair);
				}
				else {
					System.out.println("Kodi i ,ju keni shkruar zgjedhjen gabim: " + args[1]);
				}
			}
			
			if (args[0].equalsIgnoreCase("frekuenca")){
// *******************************************--PLAYFAIR--***************************************
				Frekuenca frekuenca = new Frekuenca();
				frekuenca.Vepro(args[1]);

			} 
//			else {
//				System.out.println("kod i panjohur!");
//			}
		}
		catch (Exception e) {
			// Gotta catch 'em all!
			System.out.println("ERROR!");
			e.printStackTrace();
		}
	}
}
