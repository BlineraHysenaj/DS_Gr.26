public class Vigenere_Cipher {
	enum Alfabeti {
		A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
	};
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
	public String removeSpaces(String text) {
		return text.replaceAll("\\s", "");
	}

	public int Gjej_Vlerat_Shkronjave(char a) {
		int VleraKarakterit = 0;
		if (Character.isLetter(a)) {
			String b = String.valueOf(a);
			for (int i = 0; i < b.length(); i++) {
				for (int j = 0; j < 26; j++) {
					Alfabeti Shkronja = Alfabeti.values()[j];
					if (b.equalsIgnoreCase(Shkronja.name())) {
						VleraKarakterit = VleraKarakterit + Alfabeti.valueOf(Shkronja.toString()).ordinal();
					}
				}
			}
		} else {
			VleraKarakterit = -1;
		}
		return VleraKarakterit;
	}

	public String Enkript(String PlainTexti, String Celesi) {
		String CipherTexti = "";

		int ShkronjaEnkriptuar;
		int j = 0;
		int i = 0;

		Labela1: do {
			if (j < Celesi.length()) {
				if (Gjej_Vlerat_Shkronjave(Celesi.charAt(j)) != -1) {
					if (Gjej_Vlerat_Shkronjave(PlainTexti.charAt(i)) != -1) {
						ShkronjaEnkriptuar = (Gjej_Vlerat_Shkronjave(PlainTexti.charAt(i))
								+ Gjej_Vlerat_Shkronjave(Celesi.charAt(j))) % 26;
						Alfabeti Shkronja = Alfabeti.values()[ShkronjaEnkriptuar];
						CipherTexti = CipherTexti + Shkronja;
						j++;
					} else {
						CipherTexti = CipherTexti + " ";
					}
				} else {
					// Kur Celesi ka hapesira bane qkapo menon
					j++;
					continue Labela1;
				}
			} else {
				j = 0;
				continue Labela1;
			}
			i++;
		} while (i < PlainTexti.length());

		return CipherTexti;
	}

	public String Dekript(String CipherTexti, String Celesi) {
		String PlainTexti = "";
		int ShkronjaEnkriptuar;
		int j = 0;
		int i = 0;

		Labela1: do {
			if (j < Celesi.length()) {
				if (Gjej_Vlerat_Shkronjave(Celesi.charAt(j)) != -1) {
					if (Gjej_Vlerat_Shkronjave(CipherTexti.charAt(i)) != -1) {
						ShkronjaEnkriptuar = (Gjej_Vlerat_Shkronjave(CipherTexti.charAt(i))
								- Gjej_Vlerat_Shkronjave(Celesi.charAt(j))) % 26;
						if ((Gjej_Vlerat_Shkronjave(CipherTexti.charAt(i))
								- Gjej_Vlerat_Shkronjave(Celesi.charAt(j))) < 0) {
							Alfabeti Shkronja = Alfabeti.values()[ShkronjaEnkriptuar + 26];
							PlainTexti = PlainTexti + Shkronja;
						} else {
							Alfabeti Shkronja = Alfabeti.values()[ShkronjaEnkriptuar];
							PlainTexti = PlainTexti + Shkronja;
						}
						j++;
					} else {
						PlainTexti = PlainTexti + " ";
					}
				} else {
					j++;
					continue Labela1;
				}
			} else {
				j = 0;
				continue Labela1;
			}
			i++;
		} while (i < CipherTexti.length());

		return PlainTexti;
	}
}
//Referencat:
//https://drive.google.com/drive/folders/0BxtEA9vzHIY-THpJVkQ3ck1SMGc
//https://www.tutorialspoint.com/compile_java_online.php
//https://www.youtube.com/watch?v=nQ8CThqEfQU
