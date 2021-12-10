package nl.ponai.crypto.enigma.machines;

import nl.ponai.crypto.enigma.components.EnigmaIWheelset;
import nl.ponai.crypto.enigma.components.Rotor;
import nl.ponai.crypto.enigma.generic.EnigmaSimulator;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class I extends EnigmaSimulator
{
	public I(String description)
	{
		super(description);
	}
	
	public void configureMachine()
	{
		String alphabet=Tools.DEFAULT_ALPHABET;
		String etw=alphabet;		
		Rotor retw=new Rotor(false,alphabet,etw);
		
		Rotor rI=new Rotor(true,alphabet,EnigmaIWheelset.I);
		Rotor rII=new Rotor(true,alphabet,EnigmaIWheelset.II);
		Rotor rIII=new Rotor(true,alphabet,EnigmaIWheelset.III);
		Rotor rIV=new Rotor(true,alphabet,EnigmaIWheelset.IV);
		Rotor rV=new Rotor(true,alphabet,EnigmaIWheelset.V);
		
		rI.setHasPositioning(true);
		rII.setHasPositioning(true);
		rIII.setHasPositioning(true);
		rIV.setHasPositioning(true);
		rV.setHasPositioning(true);

		rI.setHasRing(true);
		rII.setHasRing(true);
		rIII.setHasRing(true);
		rIV.setHasRing(true);
		rV.setHasRing(true);
		
		rI.addKnockOn(Tools.charToPosition('Q'));
		rII.addKnockOn(Tools.charToPosition('E'));
		rIII.addKnockOn(Tools.charToPosition('V'));
		rIV.addKnockOn(Tools.charToPosition('J'));
		rV.addKnockOn(Tools.charToPosition('Z'));
				
		Rotor rUKWA=new Rotor(false,alphabet,EnigmaIWheelset.UKWA);
		Rotor rUKWB=new Rotor(false,alphabet,EnigmaIWheelset.UKWB);
		Rotor rUKWC=new Rotor(false,alphabet,EnigmaIWheelset.UKWC);
		
		rUKWA.setIsReflector(true);
		rUKWB.setIsReflector(true);
		rUKWC.setIsReflector(true);
		
		m_machine.setHasPlugboard(true);		
		m_machine.setDisplayAlphabet(false);
		m_machine.setDoublestep(true);
		m_machine.setHasUhr(true);
		m_machine.setGrouping(5);		
		m_machine.setHasUKWD(true);
		
		rI.setName("I");
		rII.setName("II");
		rIII.setName("III");
		rIV.setName("IV");
		rV.setName("V");
		rUKWA.setName("A");
		rUKWB.setName("B");
		rUKWC.setName("C");
		
		m_machine.setEntryWheel(retw);
		
		m_machine.addRotor(rI.getName(),rI);
		m_machine.addRotor(rII.getName(),rII);
		m_machine.addRotor(rIII.getName(),rIII);
		m_machine.addRotor(rIV.getName(),rIV);
		m_machine.addRotor(rV.getName(),rV);
		m_machine.addRotor(rUKWA.getName(),rUKWA);
		m_machine.addRotor(rUKWB.getName(),rUKWB);
		m_machine.addRotor(rUKWC.getName(),rUKWC);			
		
		m_machine.setupUKWD();
	}
	
	public boolean isEquipped()
	{
		if(m_machine.getReflector()==null)
		{
			System.out.println("You have not inserted a reflector!");
			return false;
		}
		if(m_machine.getNumRotors()!=3)
		{
			System.out.println("Not enough rotors inserted, you need 3 but you inserted "+m_machine.getNumRotors());
			return false;
		}
		
		return true;
	}
	
	protected boolean quietIsEquipped()
	{
		if(m_machine.getReflector()==null)
		{
			return false;
		}
		if(m_machine.getNumRotors()!=3)
		{
			return false;
		}		
		return true;
	}	
	
	protected void handleDisplayConfiguration()
	{
		super.handleDisplayConfiguration("German Army and Air Force (Wehrmacht, Luftwaffe)");		
	}
}
