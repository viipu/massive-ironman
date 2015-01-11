package saveltaja;

import lejos.nxt.*;

public class Saveltaja {
	public static void main(String[] args) throws Exception{
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		//Sound.setVolume(10);
	
		while (!Button.ENTER.isPressed()) {
			Sound.playNote(Sound.PIANO, us.getDistance()*20, 500);
			System.out.println(us.getDistance()*20);
			Sound.pause(100);
		}
	}

}
