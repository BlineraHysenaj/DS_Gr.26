import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		System.out.println("Ju lutem shkruani se cilen nga keto opsione doni ta ekzekutoni(PlayFair,Viegenere):");
		try {
				String zgjedhja = input.nextLine();
				if (zgjedhja.equalsIgnoreCase("PlayFair")) {
		// *********************************************************************************************
					// PLAYFAIR
					PlayFair PlayFair = new PlayFair();
					Scanner fjalia_PlayFair = new Scanner(System.in);
		
					System.out.println("Shtypeni keyword:");
					String KeyWord_PlayFair = fjalia_PlayFair.nextLine();
					while (!PlayFair.isAlphaAndSpaces(KeyWord_PlayFair)) {
						System.err.println("Keyword-i duhet te permbaj vetem shkronja!");
						System.out.println("Shtypeni keyword perseri:");
						KeyWord_PlayFair = fjalia_PlayFair.nextLine();
					}
					PlayFair.setKeyWord(KeyWord_PlayFair);
					PlayFair.genMatrix();
		
					System.out.println("Shtypeni tekstin per ta enkriptuar:");
					String PlainText = fjalia_PlayFair.nextLine();
					while (!PlayFair.isAlphaAndSpaces(PlainText)) {
						System.err.println("Plaintext-i duhet te permbajt vetem shkronja");
						System.out.println("Shtypeni plaintext-in perseri:");
						PlainText = fjalia_PlayFair.nextLine();
					}
					PlayFair.encrypt(PlainText);
		
					System.out.println("Shtypeni tekstin per ta dektipruar:");
					String cipertext = fjalia_PlayFair.nextLine();
		
					while (!PlayFair.isAlphaAndSpaces(cipertext)) {
						System.err.println("cipertext-i duhet te permbajt vetem shkronja");
						System.out.println("Shtypeni cipertext-in perseri:");
						cipertext = fjalia_PlayFair.nextLine();
					}
		
					PlayFair.decrypt(cipertext);
		
					fjalia_PlayFair.close();
				}
		// **********************************************************************************************
				// VIEGENERE
				if (zgjedhja.equalsIgnoreCase("Viegenere")) {
					Vigenere_Cipher Vigenere = new Vigenere_Cipher();
					Scanner Fjalia_Viegenere = new Scanner(System.in);
		
					System.out.println("Shtypeni keyword:");
					String Keyword_Viegenere = Fjalia_Viegenere.nextLine();
					while (!Vigenere.isAlphaAndSpaces(Keyword_Viegenere)) {
						System.err.println("Keywordi-i duhet te permbajt vetem shkronja");
						System.out.println("Shtypeni keywordin-in perseri:");
						Keyword_Viegenere = Fjalia_Viegenere.nextLine();
					}
		
					System.out.println("Shtypeni tekstin per ta enkriptuar:");
					String PlainText = Fjalia_Viegenere.nextLine();
		
					System.out.println("Teksti i enkriptuar: " + Vigenere.Enkript(PlainText, Keyword_Viegenere));
		
					System.out.println("Shtypeni tekstin per ta dekriptuar:");
					String CipherText = Fjalia_Viegenere.nextLine();
		
					System.out.println("Teksti i dekriptuar: " + Vigenere.Dekript(CipherText, Keyword_Viegenere));
		
					Fjalia_Viegenere.close();
					input.close();
				}
		}catch (java.util.InputMismatchException err) {
            System.out.println("\nINVALID INPUT!");
        }
	}

}
