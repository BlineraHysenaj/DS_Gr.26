import java.util.Scanner;
import java.util.Arrays;

public class Frekuenca {

	public static void main(String[] args) {

		Scanner sr = new Scanner(System.in);
		String str;

		System.out.println("shkruaj diqka..");
		str = sr.nextLine().replace(" ", "");

		int[] freq = new int[str.length()];
		int i, j;

//pershendetje nga FIEK!
// Converts given string into character array

		char string[] = str.toCharArray();
		int n = 0;

		for (i = 0; i < str.length(); i++) {
			freq[i] = 1;

			for (j = i + 1; j < str.length(); j++) {

				if (string[i] == string[j]) {
					freq[i]++;

// Set string[j] to 0 to avoid printing visited character
					string[j] = '0';
				}
			}
		}

		for (i = 0; i < str.length(); i++) {

			n++;

		}

		int[] perc = new int[n];

		for (i = 0; i < freq.length; i++) {

			perc[i] = freq[i] * 100 / n;
		}

		String[] bar = new String[20];

		for (i = 0; i < perc.length; i++) {
			if ((perc[i] > 0) && (perc[i] <= 5)) {
				bar[i] = "#";

			}

			else if ((perc[i] > 5) && (perc[i] <= 10)) {

				bar[i] = "##";

			} else if ((perc[i] > 10) && (perc[i] <= 15)) {

				bar[i] = "###";
			} else if ((perc[i] > 15) && (perc[i] <= 20)) {

				bar[i] = "####";

			} else if ((perc[i] > 20) && (perc[i] <= 25)) {

				bar[i] = "#####";

			} else if ((perc[i] > 25) && (perc[i] <= 30)) {

				bar[i] = "######";

			} else if ((perc[i] > 35) && (perc[i] <= 40)) {
				bar[i] = "#######";

			} else if ((perc[i] > 40) && (perc[i] <= 45)) {

				bar[i] = "########";

			} else if ((perc[i] > 45) && (perc[i] <= 50)) {

				bar[i] = "#########";

			} else if ((perc[i] > 50) && (perc[i] <= 55)) {

				bar[i] = "##########";

			} else if ((perc[i] > 55) && (perc[i] <= 60)) {

				bar[i] = "###########";

			} else if ((perc[i] > 60) && (perc[i] <= 65)) {

				bar[i] = "############";

			} else if ((perc[i] > 65) && (perc[i] <= 70)) {

				bar[i] = "#############";

			} else if ((perc[i] > 70) && (perc[i] <= 75)) {

				bar[i] = "##############";

			} else if ((perc[i] > 80) && (perc[i] <= 85)) {

				bar[i] = "###############";

			} else if ((perc[i] > 85) && (perc[i] <= 90)) {

				bar[i] = "################";

			} else if ((perc[i] > 90) && (perc[i] <= 95)) {

				bar[i] = "#################";

			} else {

				bar[i] = "###################";

			}

		}

// Displays the each character and their corresponding frequency
		System.out.println(" \nTotal: " + n);
		for (i = 0; i < freq.length; i++) {
			if (string[i] != ' ' && string[i] != '0')
				System.out.println(string[i] + ":" + freq[i] + " (" + perc[i] + "% )     " + bar[i]);
			sr.close();
		}

	}
}
