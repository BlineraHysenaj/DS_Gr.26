import java.util.ArrayList;

//******************************************************************************************************************
// 'J' is replaced with 'I' to fit 5x5 square
// 'X' is used as substitution in case you need to fill second letter in the digram, or replace in case by two identical letters
// Playfair square is filled row-by-row, starting with the keyword.
//******************************************************************************************************************

public class PlayFair_Cipher {

	private String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
	private char[][] matrix = new char[5][5];
	private char[] keyWord;

	public boolean isAlphaAndSpaces(String name) {
		name = removeSpaces(name);
		char[] chars = name.toCharArray();

		for (char c : chars) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

	public void setKeyWord(String k) {
		String key = removeDuplicate(k.toUpperCase().replace('J', 'I'));
		keyWord = key.toCharArray();
	}

	public String removeDuplicate(String text) {
		char[] temp = text.toCharArray();
		ArrayList<Character> response = new ArrayList<>();

		for (int i = 0; i < temp.length; i++) {
			if (!response.contains(temp[i])) {
				response.add(temp[i]);
			}
		}

		String withoutDuplicate = "";
		for (int i = 0; i < response.size(); i++) {
			withoutDuplicate += response.get(i);
		}
		
		return withoutDuplicate;
	}

	public String removeSpaces(String text) {
		return text.replaceAll("\\s", "");
	}

	public void genMatrix() {
		for (Character character : this.keyWord)
			this.alphabet = this.alphabet.replace(character.toString(), "");

		int length = this.keyWord.length;
		int keyPosition = 0;
		int alfaPosition = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (length != 0) {
					matrix[i][j] = this.keyWord[keyPosition];
					length--;
					keyPosition++;
				} else {
					matrix[i][j] = this.alphabet.charAt(alfaPosition);
					alfaPosition++;
				}
			}
		}
		System.out.println("Matrica e gjeneruar.");

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(matrix[i][j] + " ");
			}

			System.out.println();
		}
	}

	public void encrypt(String plaintext) {
		String cypertext = new String();

		plaintext = plaintext.toUpperCase().replace("J", "I");
		plaintext = removeSpaces(plaintext);

		if (plaintext.length() % 2 != 0)
			plaintext = plaintext + "X";

		for (int a = 0; a < plaintext.length(); a += 2) {
			char chA = plaintext.charAt(a);
			char chB = plaintext.charAt(a + 1);

			if (chA == chB)
				chB = 'X';

			int pozitaAX = 0, pozitaAY = 0, pozitaBX = 0, pozitaBY = 0;
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					if (chA == this.matrix[i][j]) {

						pozitaAX = i;
						pozitaAY = j;
					}
					if (chB == this.matrix[i][j]) {
						pozitaBX = i;
						pozitaBY = j;
					}
				}
			}

			char encChA, encChB;
			if (pozitaAX == pozitaBX) {
				// nese jane ne nje rresht
				encChA = this.matrix[pozitaAX][(pozitaAY + 1) % 5];
				encChB = this.matrix[pozitaBX][(pozitaBY + 1) % 5];
			} else if (pozitaAY == pozitaBY) {
				// nese jane ne nje kolone
				encChA = this.matrix[(pozitaAX + 1) % 5][pozitaAY];
				encChB = this.matrix[(pozitaBX + 1) % 5][pozitaBY];
			} else {
				// nese nuk jane ne rresht e as kolone
				encChA = this.matrix[pozitaAX][pozitaBY];
				encChB = this.matrix[pozitaBX][pozitaAY];
			}

			cypertext += encChA;
			cypertext += encChB;
			cypertext += " ";
		}

		System.out.println("teksti i enkriptuar eshte :" + cypertext);
	}

	public void decrypt(String ciphertext) {
		String plaintext = new String();
		ciphertext = removeSpaces(ciphertext).toUpperCase();

		for (int a = 0; a < ciphertext.length(); a += 2) {
			char encChA = ciphertext.charAt(a);
			char encChB = ciphertext.charAt(a + 1);

			int pozitaAX = 0, pozitaAY = 0, pozitaBX = 0, pozitaBY = 0;
			for (int i = 0; i < 5; i++)
				for (int j = 0; j < 5; j++) {
					if (encChA == matrix[i][j]) {
						pozitaAX = i;
						pozitaAY = j;
					}

					if (encChB == matrix[i][j]) {
						pozitaBX = i;
						pozitaBY = j;
					}
				}

			char decChA, decChB;
			if (pozitaAX == pozitaBX) {
				decChA = matrix[pozitaAX][(pozitaAY - 1 + 5) % 5];
				decChB = matrix[pozitaBX][(pozitaBY - 1 + 5) % 5];
			} else if (pozitaAY == pozitaBY) {
				decChA = matrix[(pozitaAX - 1 + 5) % 5][pozitaAY];
				decChB = matrix[(pozitaBX - 1 + 5) % 5][pozitaBY];
			} else {
				decChA = matrix[pozitaAX][pozitaBY];
				decChB = matrix[pozitaBX][pozitaAY];
			}

			plaintext += decChA;
			plaintext += decChB;
			plaintext += " ";
		}

		System.out.println("teksti i enkriptuar eshte:" + removeSpaces(plaintext.toUpperCase().replace("I", "J")));

	}

}
