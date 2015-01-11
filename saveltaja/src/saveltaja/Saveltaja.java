package saveltaja;

import lejos.nxt.*;
import lejos.util.Delay;

public class Saveltaja {
	UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
	int tooni = 0;
	int korkeus = 0;

	public static void main(String[] args) throws Exception {
		Sound.setVolume(50);
		Motor.A.setSpeed(360);
		Motor.C.setSpeed(360);
		Motor.B.setSpeed(180);

		Saveltaja saveltaja = new Saveltaja();
		saveltaja.run();

	}

	public void run() {
		System.out.println("Mitä tänään lauletaan?");
		System.out.println("Paina vasenta nuolta, jos haluat matalan äänen, ja oikeaa, jos haluat korkeamman äänen.");
		int nappula = Button.waitForPress();
		if (nappula == 2) {
			korkeus = 0;
		} else if (nappula == 2) {
			korkeus = 100;
		} else {
			System.out.println("Minä laulan matalalta!");
			Delay.msDelay(500);
		}
		LCD.clear();
		System.out.println("3");
		Sound.playTone(440, 500);
		Sound.pause(100);
		System.out.println("2");
		Sound.playTone(440, 500);
		Sound.pause(100);
		System.out.println("1");
		Sound.playTone(440, 500);
		Sound.pause(100);
		System.out.println("Sitten mennään!");
		Sound.playTone(440, 1000);
		Sound.pause(100);
		LCD.clear();
		while (!Button.ENTER.isPressed()) {
			liiku();
		}
	}

	public void soita() {
		// Sound.playNote(Sound.PIANO, distance*20, 500);
		Sound.playTone(tooni + 100 * 5, 500);
		System.out.println(tooni);
	}

	public void suuntaa(int suunta) {
		Motor.A.stop();
		Motor.C.stop();
		if (suunta <= 0 ) {
			Motor.B.rotate(90); //sensori vasemmalle
			int etaisyys = us.getDistance();
			Motor.B.rotate(-180); //sensori oikealle
			if (etaisyys < us.getDistance()) { //oikealla enemmän tilaa
				tooni = us.getDistance();
				soita();
				System.out.println("oikealle");
				Motor.A.backward();
				Motor.C.rotate(200); //oikea moottori peruuttaa
			} else { //vasemmalla enemmän tilaa
				tooni = etaisyys;
				soita();
				System.out.println("vasemmalle");
				Motor.C.backward();
				Motor.A.rotate(200); //vasen moottori peruuttaa
			}
			Motor.A.stop();
			Motor.C.stop();
			Motor.B.rotate(90); //palautetaan sensori
		}
	}

	public void liiku() {
		Motor.A.backward();
		Motor.C.backward();
		if (us.getDistance() < 20) {
			suuntaa(0);
		}
		tooni = us.getDistance();
		soita();
	}

}
