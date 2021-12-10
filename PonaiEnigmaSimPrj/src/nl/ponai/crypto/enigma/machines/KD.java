package nl.ponai.crypto.enigma.machines;

import nl.ponai.crypto.enigma.components.Rotor;
import nl.ponai.crypto.enigma.generic.EnigmaSimulator;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class KD extends EnigmaSimulator
{
	public KD(String description)
	{
		super(description);
	}
	
	public void configureMachine()
	{
		String alphabet=Tools.DEFAULT_ALPHABET;			
		Rotor retw=new Rotor(false,alphabet,"QWERTZUIOASDFGHJKPYXCVBNML");
		
		Rotor rI=new Rotor(true,alphabet,"VEZIOJCXKYDUNTWAPLQGBHSFMR");
		Rotor rII=new Rotor(true,alphabet,"HGRBSJZETDLVPMQYCXAOKINFUW");
		Rotor rIII=new Rotor(true,alphabet,"NWLHXGRBYOJSAZDVTPKFQMEUIC");
		
		rI.setHasPositioning(true);
		rII.setHasPositioning(true);
		rIII.setHasPositioning(true);

		rI.setHasRing(true);
		rII.setHasRing(true);
		rIII.setHasRing(true);
		
		String turnovers="SUYAEHLNQ";
		
		for(int i=0;i<turnovers.length();i++)
		{
			rI.addKnockOn(Tools.charToPosition(turnovers.charAt(i)));
			rII.addKnockOn(Tools.charToPosition(turnovers.charAt(i)));
			rIII.addKnockOn(Tools.charToPosition(turnovers.charAt(i)));
		}		 
						
		m_machine.setHasPlugboard(false);		
		m_machine.setDisplayAlphabet(true);
		m_machine.setDoublestep(true);
		m_machine.setHasUhr(false);
		m_machine.setGrouping(5);		
		m_machine.setHasUKWD(true);
		
		rI.setName("I");
		rII.setName("II");
		rIII.setName("III");
		
		m_machine.setEntryWheel(retw);
		
		m_machine.addRotor(rI.getName(),rI);
		m_machine.addRotor(rII.getName(),rII);
		m_machine.addRotor(rIII.getName(),rIII);
		
		m_machine.setupUKWD();
		
	}
	
	public boolean isEquipped()
	{
		if(m_machine.getReflector()==null)
		{
			System.out.println("You have not inserted the UKW-D reflector!");
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
		super.handleDisplayConfiguration("Militarisches Amt (Mil Amt)");		
	}
}
