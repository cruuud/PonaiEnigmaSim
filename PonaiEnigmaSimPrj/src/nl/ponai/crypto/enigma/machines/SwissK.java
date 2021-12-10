package nl.ponai.crypto.enigma.machines;

import nl.ponai.crypto.enigma.components.Rotor;
import nl.ponai.crypto.enigma.generic.EnigmaSimulator;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class SwissK extends EnigmaSimulator
{
	public SwissK(String description)
	{
		super(description);
	}
	
	public void configureMachine()
	{
		String alphabet=Tools.DEFAULT_ALPHABET;				
		Rotor retw=new Rotor(false,alphabet,"QWERTZUIOASDFGHJKPYXCVBNML");
		
		Rotor rI=new Rotor(true,alphabet,"PEZUOHXSCVFMTBGLRINQJWAYDK");
		Rotor rII=new Rotor(true,alphabet,"ZOUESYDKFWPCIQXHMVBLGNJRAT");
		Rotor rIII=new Rotor(true,alphabet,"EHRVXGAOBQUSIMZFLYNWKTPDJC");
		
		rI.setHasPositioning(true);
		rII.setHasPositioning(true);
		rIII.setHasPositioning(true);

		rI.setHasRing(true);
		rII.setHasRing(true);
		rIII.setHasRing(true);
		
		rI.addKnockOn(Tools.charToPosition('Y'));
		rII.addKnockOn(Tools.charToPosition('E'));
		rIII.addKnockOn(Tools.charToPosition('N'));		
				
		Rotor rUKW=new Rotor(false,alphabet,"IMETCGFRAYSQBZXWLHKDVUPOJN");
		
		rUKW.setIsReflector(true);
		rUKW.setHasPositioning(true);
		rUKW.setHasRing(true);
		
		m_machine.setHasPlugboard(false);		
		m_machine.setDisplayAlphabet(true);
		m_machine.setDoublestep(true);
		m_machine.setHasUhr(false);
		m_machine.setGrouping(5);		
		m_machine.setHasUKWD(false);
		
		rI.setName("I");
		rII.setName("II");
		rIII.setName("III");
		rUKW.setName("1");
		
		m_machine.setEntryWheel(retw);
		
		m_machine.addRotor(rI.getName(),rI);
		m_machine.addRotor(rII.getName(),rII);
		m_machine.addRotor(rIII.getName(),rIII);
		m_machine.addRotor(rUKW.getName(),rUKW);
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
		super.handleDisplayConfiguration("Swiss Army, the Air Force and the Foreign Ministry (diplomatic service).\nPLEASE NOTE: The only known wiring is the Swiss Air Force variant, which is implemented in this simulator.\nThe UKW wiring was the same for all three Swiss users.");		
	}
}
