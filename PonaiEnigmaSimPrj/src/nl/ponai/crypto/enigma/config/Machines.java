package nl.ponai.crypto.enigma.config;

import nl.ponai.crypto.enigma.machines.A865;
import nl.ponai.crypto.enigma.machines.D;
import nl.ponai.crypto.enigma.machines.G111;
import nl.ponai.crypto.enigma.machines.G260;
import nl.ponai.crypto.enigma.machines.G312;
import nl.ponai.crypto.enigma.machines.I;
import nl.ponai.crypto.enigma.machines.KD;
import nl.ponai.crypto.enigma.machines.M3;
import nl.ponai.crypto.enigma.machines.M4;
import nl.ponai.crypto.enigma.machines.Norenigma;
import nl.ponai.crypto.enigma.machines.Railway;
import nl.ponai.crypto.enigma.machines.SwissK;
import nl.ponai.crypto.enigma.machines.T;
import nl.ponai.crypto.enigma.machines.CustomEnigma;

/**
 * 
 * @author ponai
 *
 */
public class Machines
{
	public static final MachineConfiguration[] MACHINES=new MachineConfiguration[]
			{
				new MachineConfiguration("D","Enigma D (Commercial Enigma A26)",new D("Enigma D (Commercial Enigma A26)")),
				new MachineConfiguration("I","Enigma I",new I("Enigma I")),
				new MachineConfiguration("N","Norway Enigma (Norenigma)",new Norenigma("Norway Enigma (Norenigma)")),	
				new MachineConfiguration("M3","Enigma M3 (3-wheel Naval Enigma)",new M3("Enigma M3 (3-wheel Naval Enigma)")),
				new MachineConfiguration("M4","Enigma M4 (U-Boot Enigma)",new M4("Enigma M4 (U-Boot Enigma)")),
				new MachineConfiguration("R","Railway Enigma (Railway variant of Enigma K (A27))",new Railway("Railway Enigma (Railway variant of Enigma K (A27))")),
				new MachineConfiguration("SK","Swiss-K (Enigma K variant for Switzerland)",new SwissK("Swiss-K (Enigma K variant for Switzerland)")),
				new MachineConfiguration("T","Enigma T (Tirpitz, The Japanese Enigma)",new T("Enigma T (Tirpitz, The Japanese Enigma)")),
				new MachineConfiguration("KD","Enigma KD (Commercial Enigma (K) with UKW-D)",new KD("Enigma KD (Commercial Enigma (K) with UKW-D)")),
				new MachineConfiguration("A865","Zahlwerk Enigma (Commercial Enigma A28) serial A-865",new A865("Zahlwerk Enigma (Commercial Enigma A28) serial A-865")),
				new MachineConfiguration("G312","Enigma G aka Abwehr Enigma (Zahlwerk Enigma G31) serial G-312",new G312("Enigma G aka Abwehr Enigma (Zahlwerk Enigma G31) serial G-312")),
				new MachineConfiguration("G260","Enigma G aka Abwehr Enigma (Zahlwerk Enigma G31) serial G-260",new G260("Enigma G aka Abwehr Enigma (Zahlwerk Enigma G31) serial G-260")),
				new MachineConfiguration("G111","Enigma G aka Abwehr Enigma (Zahlwerk Enigma G31) serial G-111",new G111("Enigma G aka Abwehr Enigma (Zahlwerk Enigma G31) serial G-111")),				
			};
}
