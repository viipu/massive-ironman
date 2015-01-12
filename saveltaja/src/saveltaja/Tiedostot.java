package saveltaja;

import java.io.*;

public class Tiedostot {
	File tiedosto;
	
	FileOutputStream ulos;
	DataOutputStream dataUlos;
	
	FileInputStream sisaan;
	DataInputStream dataSisaan;
	
	public void uusiTiedosto(int indeksi) {
		tiedosto = new File("savellys"+indeksi+".dat");
		try {
			ulos = new FileOutputStream(tiedosto);
		} catch (IOException io) {
			System.err.println("OutputStreamin luonti ei onnaa!");
			System.exit(1);
		}
		dataUlos = new DataOutputStream(ulos);
		
	}
	
	public void kirjoitaIndeksi(int indeksi) {
		try {
			dataUlos.writeByte(indeksi);
		} catch (IOException io) {
			System.out.println("DataStreamiin kirjoitus ei onnaa!");
		}
	}
	
	public void suljeUlos() {
		try {
			ulos.close();
		} catch (IOException io) {
			System.out.println("Streamin sulkeminen ei onnaa!");
		}
	}
	
	public boolean lueTiedosto(int indeksi) {
		tiedosto = new File("savellys"+indeksi+".dat");
		try {
			sisaan = new FileInputStream(tiedosto);
			dataSisaan = new DataInputStream(sisaan);
		} catch (IOException io) {
			return false;
		}
		return true;
	}
	public int lueNuotti() {
		try {
			if (sisaan.available() > 0 ) {
				return dataSisaan.readByte();
			}
		} catch (IOException io) {
			System.out.println("Tiedoston luku ei onnaa!");
		}
		return -1;
	}

}
