package saveltaja;

import lejos.nxt.Sound;

public class Nuotit {
	/**
	 * Thanks to Erik N. at http://metatek.blogspot.fi/ whose work I
	 * cannibalized and expanded on for this project. 
	 * Sources:
	 * http://metatek.blogspot.fi/2013/10/music-class-for-use-with-lejos-nxj.html
	 * http://www.phy.mtu.edu/~suits/notefreqs.html
	 * (Accessed 11/01/2015)
	 */
	private int kesto = 500; 
	private static String[] nuotit = { 
		"C0", "C#0", "D0", "D#0", "E0", "F0", "F#0", "G0", "G#0", "A0", "A#0", "B0", 
		"C1", "C#1", "D1", "D#1", "E1", "F1", "F#1", "G1", "G#1", "A1", "A#1", "B1", 
		"C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2", "G#2", "A2", "A#2", "B2", 
		"C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", 
		"C4", "C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4", 
		"C5", "C#5", "D5", "D#5", "E5", "F5", "F#5", "G5", "G#5", "A5", "A#5", "B5", 
		"C6", "C#6", "D6", "D#6", "E6", "F6", "F#6", "G6", "G#6", "A6", "A#6", "B6", 
		"C7", "C#7", "D7", "D#7", "E7", "F7", "F#7", "G7", "G#7", "A7", "A#7", "B7", 
		"C8", "C#8", "D8", "D#8", "E8", "F8", "F#8", "G8", "G#8", "A8", "A#8", "B8" 
		};
	private static float[] taajuudet = { 
		16.35f, 17.32f, 18.35f, 19.45f, 20.6f, 21.83f, 23.12f, 24.5f, 25.96f, 27.5f, 29.14f, 30.87f, 
		32.7f, 34.65f, 36.71f, 38.89f, 41.2f, 43.65f, 46.25f, 49.0f, 51.91f, 55.0f, 58.27f, 61.74f, 
		65.41f, 69.3f, 73.42f, 77.78f, 82.41f, 87.31f, 92.5f, 98.0f, 103.83f, 110.0f, 116.54f, 123.47f, 
		130.81f, 138.59f, 146.83f, 155.56f, 164.81f, 174.61f, 185.0f, 196.0f, 207.65f, 220.0f, 233.08f, 246.94f, 
		261.63f, 277.18f, 293.66f, 311.13f, 329.63f, 349.23f, 369.99f, 392.0f, 415.3f, 440.0f, 466.16f, 493.88f, 
		523.25f, 554.37f, 587.33f, 622.25f, 659.25f, 698.46f, 739.99f, 783.99f, 830.61f, 880.0f, 932.33f, 987.77f,
		1046.5f, 1108.73f, 1174.66f, 1244.51f, 1318.51f, 1396.91f, 1479.98f, 1567.98f, 1661.22f, 1760.0f, 1864.66f, 1975.53f, 
		2093.0f, 2217.46f, 2349.32f, 2489.02f, 2637.02f, 2793.83f, 2959.96f, 3135.96f, 3322.44f, 3520.0f, 3729.31f, 3951.07f, 
		4186.01f, 4434.92f, 4698.63f, 4978.03f, 5274.04f, 5587.65f, 5919.91f, 6271.93f, 6644.88f, 7040.0f, 7458.62f, 7902.13f 
		};

	public String soitaNuotti(int monesko) {
		if (monesko > taajuudet.length - 1) {
			return soitaNuotti(taajuudet.length - 1);
		} else if (monesko < 0) {
			return soitaNuotti(0);
		}
		Sound.playTone((int) taajuudet[monesko], kesto);
		return nuotit[monesko];
	}
	public float soitaNuotti(String nuotti) {
		float taajuus = getTaajuus(nuotti);
		Sound.playTone((int)taajuus, kesto);
		return taajuus;
	}
	public String soitaNuotti(float taajuus) {
		Sound.playTone((int)taajuus, kesto);
		return getNuotti(taajuus);
	}

	public String getNuotti(int monesko) {
		if (monesko > nuotit.length - 1) {
			return getNuotti(nuotit.length - 1);
		} else if (monesko < 0) {
			return getNuotti(0);
		}
		return nuotit[monesko];
	}

	public String getNuotti(float taajuus) {
		for (int i = 0; i < taajuudet.length; i++) {
			if (taajuus == taajuudet[i]) {
				return nuotit[i];
			}
		}
		return "NA";
	}

	public float getTaajuus(int monesko) {
		if (monesko > taajuudet.length - 1) {
			return getTaajuus(taajuudet.length - 1);
		} else if (monesko < 0) {
			return getTaajuus(0);
		}
		return taajuudet[monesko];

	}

	public float getTaajuus(String nuotti) {
		for (int i = 0; i < nuotit.length; i++) {
			if (nuotti.equals(nuotit[i])) {
				return taajuudet[i];
			}
		}
		return 0.0f;
	}
	
	public int getIndeksi(String nuotti) {
		for (int i = 0; i < nuotit.length; i++) {
			if (nuotti.equals(nuotit[i])) {
				return i;
			}
		}
		return -1;
	}
	public int getIndeksi(float taajuus) {
		for (int i = 0; i < taajuudet.length; i++) {
			if (taajuus == taajuudet[i]) {
				return i;
			}
		}
		return -1;
	}

}
