package saveltaja;

import lejos.nxt.*;
import lejos.util.Delay;

public class Saveltaja {
	Nuotit nuotit = new Nuotit();
	Tiedostot tiedostot = new Tiedostot();
	UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
	TouchSensor ts = new TouchSensor(SensorPort.S2);
	
	int tooni = 0; // ultraäänen lukema arvo, jonka perusteella valitaan soitettava nuotti 
	int korkeus = 0; // käyttäjän valitsema ääniala
	int offset = 10; // esteiden väistämisen raja-arvo
	int savellysnro = 0;
	int kesto = 0;
	int nopeus = 360;

	public static void main(String[] args) throws Exception {
        new Thread(new Runnable() { 
            public void run() {
                while(true) {
                    if (Button.waitForPress() == Button.ID_ESCAPE) {
                        NXT.shutDown();
                    }
                }
            }
        }).start();
        Saveltaja saveltaja = new Saveltaja();
		saveltaja.run();
	}

	public void run() {
		Sound.setVolume(50);
		Motor.A.setSpeed(nopeus);
		Motor.C.setSpeed(nopeus);
		Motor.B.setSpeed(180);
		boolean loppu = alku();
		while(!Button.ENTER.isPressed()) {
			if (loppu) {
				break;
			}
			valikko();
			sessio();	
			loppu = lopetus();
		}
		LCD.drawString("Kiitos soitosta!", 0, 3);
		Delay.msDelay(2000);
		LCD.clear();
	}
	public boolean alku() {
		LCD.drawString("Tervetuloa", 0, 0);
		LCD.drawString("tiluttelemaan!", 0, 1);
		LCD.drawString("Tarkistanko, ", 0, 2);
		LCD.drawString("onko muistissa", 0, 3);
		LCD.drawString("kappaleita?", 0, 4);
		LCD.drawString("Oikea: joo", 0, 5);
		LCD.drawString("Muu: ei", 0, 6);
		int nappula = Button.waitForPress();
		LCD.clear();
		if (nappula == 4) { 
			if (onkoVanhoja()) { //tarkistetaan onko vanhoja
				LCD.drawString("Vanhoja lauluja ", 0, 2);
				LCD.drawString("on " +savellysnro+" kappaletta", 0, 3);
				Delay.msDelay(2000);
				LCD.clear();
				return lopetus(); //jos on, mennään suoraan lopetus-valikkoon
			} 
		} 
		return false; //ei tarkisteta onko vanhoja; ei lopeteta
	}
	public void sessio() {
		tiedostot.uusiTiedosto(savellysnro);
		savellysnro++;
		tiedostot.kirjoitaIndeksi(korkeus); //talletetaan aluksi valittu korkeus
		while (!Button.ENTER.isPressed()) {
			liiku();
			kesto ++;
			if (kesto >=120){
				kesto = 0;
				break;
			}
		}
		Motor.A.stop();
		Motor.C.stop();
		tiedostot.suljeUlos();
		LCD.clear();
		LCD.drawString("Wau!", 0, 0);
		Delay.msDelay(2000);
		LCD.clear();
	}
	
	public void valikko() {
		LCD.drawString("Lauletaanko?", 0, 0);
		LCD.drawString("Paina vasenta, ", 0, 2);
		LCD.drawString("jos haluat ", 0, 3);
		LCD.drawString("matalan laulun, ", 0, 4);
		LCD.drawString("ja oikeaa, jos ", 0, 5);
		LCD.drawString("korkeammalta.", 0, 6);
		int nappula = Button.waitForPress(10000);
		LCD.clear();
		LCD.drawString("Mie laulan", 3, 2);
		if (nappula == 4) {
			korkeus = 2;
			LCD.drawString("korkealta!", 3, 3);
		} else	{
			korkeus = 1;
			LCD.drawString("matalalta!", 3, 3);
		}
		Delay.msDelay(2000);
		LCD.clear();
		LCD.drawString("Kappale on ", 0, 0);
		LCD.drawString("kestoltaan ", 0, 1);
		LCD.drawString("korkeintaan ", 0, 2);
		LCD.drawString("minuutin. ", 0, 3);
		LCD.drawString("Lopeta ", 0, 4);
		LCD.drawString("painamalla ", 0, 5);
		LCD.drawString("ENTER.", 0, 6);
		Delay.msDelay(3000);
		LCD.clear();
		LCD.drawString("3!", 7, 2);
		Sound.playTone(440, 500);
		Delay.msDelay(1000);
		LCD.drawString("2!", 7, 3);
		Sound.playTone(440, 500);
		Delay.msDelay(1000);
		LCD.drawString("1!", 7, 4);
		Sound.playTone(440, 500);
		Delay.msDelay(1000);
		LCD.clear();
		LCD.drawString("Sitten mentiin!", 0, 3);
		Sound.playTone(440, 1500);
		Delay.msDelay(2000);
		LCD.clear();
		
	}
	public boolean lopetus() {
		LCD.drawString("Soitanko ", 0, 1);
		LCD.drawString("laulusi? ", 0, 2);
		LCD.drawString("Vasen: ei, ", 0, 3);
		LCD.drawString("lopeta ohjelma ", 2, 4);
		LCD.drawString("Oikea: joo ", 0, 5);
		LCD.drawString("Muu: lauletaan ", 0, 6);
		LCD.drawString("uusi ", 5, 7);
		int nappula = Button.waitForPress();
		LCD.clear();
		if (nappula == 4) {
			soitaSavellykset();
			return lopetus();
		} else if (nappula == 2) {
			return true;
		} else {
			return false;
		}
	}

	public void liiku() {
		Motor.A.backward();
		Motor.C.backward();
		koskettaako();
		int matka = us.getDistance();
		if (matka < offset) {
			suuntaa();
		} else if (matka < offset*2){ //hidastetaan seinänä lähestyessä
			Motor.A.setSpeed(nopeus/3);
			Motor.C.setSpeed(nopeus/3);
		} else if (matka < offset*5) {
			Motor.A.setSpeed(nopeus/3*2);
			Motor.C.setSpeed(nopeus/3*2);
		}
		tooni = us.getDistance();
		soita();
		
	}

	public void soita() {
		tooni = tooni -offset;
		tooni = nuotit.soitaNuotti(tooni);
		tiedostot.kirjoitaIndeksi(tooni);
		System.out.println(nuotit.getNuotti(tooni));
		Delay.msDelay(nuotit.kesto);
	}

	public void suuntaa() {
		Motor.A.stop();
		Motor.C.stop();
		Motor.A.setSpeed(nopeus);
		Motor.C.setSpeed(nopeus);
		
		Motor.B.rotate(90); //sensori vasemmalle
		int etaisyys = us.getDistance();
		Motor.B.rotate(-180); //sensori oikealle
		if (etaisyys < us.getDistance()) { //oikealla enemmän tilaa
			Motor.A.backward();
			Motor.C.rotate(240); //oikea moottori peruuttaa
			Motor.A.stop();
		} else { //vasemmalla enemmän tilaa
			Motor.C.backward();
			Motor.A.rotate(240); //vasen moottori peruuttaa
			Motor.C.stop();
		}
		Motor.B.rotate(90); //palautetaan sensori
	}
	
	public void koskettaako() {
		if (ts.isPressed()) {
			Motor.A.forward(); //peruuta
			Motor.C.forward();
			Delay.msDelay(1000);
			suuntaa();
		} else return;
	}
	
	public void soitaSavellykset() {
		int indeksi = 0;
		for (int i=0; i<savellysnro; i++) {
			tiedostot.lueTiedosto(i);
			LCD.clear();
			LCD.drawString("Savellys " + i, 3, 3);
			korkeus = tiedostot.lueNuotti(); //korkeus talletettu alkuun
			while (true) {
				indeksi = tiedostot.lueNuotti();
				if (indeksi >= 0) {
					Sound.playTone((int)(nuotit.getTaajuus(indeksi)*korkeus), 500);
					Delay.msDelay(500);
				} else {
					break;
				}
			}
			LCD.clear();
			LCD.drawString("Aika hieno!", 2, 3);
			Delay.msDelay(2000);
			LCD.clear();
		}
	}
	public boolean onkoVanhoja() {
		while (true) {
			if (!tiedostot.lueTiedosto(savellysnro)) {
				break;
			} else {
				savellysnro++;
			}
		}
		if (savellysnro == 0 ) {
			return false;
		} else {
			return true;
		}
	}
	public void kaantymistesti(int asteet) {
		Motor.A.backward();
		Motor.C.rotate(asteet); //oikea moottori peruuttaa
		Motor.A.stop();		
	}

}
