package nl.ponai.crypto.enigma.machines;

import nl.ponai.crypto.enigma.components.Rotor;
import nl.ponai.crypto.enigma.config.ZahlwerkTurnoverConfig;
import nl.ponai.crypto.enigma.generic.EnigmaSimulator;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class G111 extends EnigmaSimulator
{
	public G111(String description)
	{
		super(description);
	}
	
	public void configureMachine()
	{
		String alphabet=Tools.DEFAULT_ALPHABET;			
		Rotor retw=new Rotor(false,alphabet,"QWERTZUIOASDFGHJKPYXCVBNML");		                                     
		
		Rotor rI=new Rotor(true,alphabet,"WLRHBQUNDKJCZSEXOTMAGYFPVI");
		Rotor rII=new Rotor(true,alphabet,"TFJQAZWMHLCUIXRDYGOEVBNSKP");
		Rotor rV=new Rotor(true,alphabet,"QTPIXWVDFRMUSLJOHCANEZKYBG");
		
		rI.setHasPositioning(true);
		rII.setHasPositioning(true);
		rV.setHasPositioning(true);

		rI.setHasRing(true);
		rII.setHasRing(true);
		rV.setHasRing(true);
				
		for(int i=0;i<ZahlwerkTurnoverConfig.I.length();i++)
		{
			rI.addKnockOn(Tools.charToPosition(ZahlwerkTurnoverConfig.I.charAt(i)));	
		}		 
		for(int i=0;i<ZahlwerkTurnoverConfig.II.length();i++)
		{
			rII.addKnockOn(Tools.charToPosition(ZahlwerkTurnoverConfig.II.charAt(i)));	
		}
		
		String turnoverV="SWZFHMQ";
		
		for(int i=0;i<turnoverV.length();i++)
		{
			rV.addKnockOn(Tools.charToPosition(turnoverV.charAt(i)));	
		}
				
		Rotor rUKW=new Rotor(true,alphabet,"IMETCGFRAYSQBZXWLHKDVUPOJN");
		
		rUKW.setIsReflector(true);
		rUKW.setHasPositioning(true);
		rUKW.setHasRing(true);		
		
		m_machine.setHasPlugboard(false);		
		m_machine.setDisplayAlphabet(true);
		m_machine.setDoublestep(false);
		m_machine.setHasUhr(false);
		m_machine.setGrouping(5);		
		m_machine.setHasUKWD(false);
		
		rI.setName("I");
		rII.setName("II");
		rV.setName("V");
		rUKW.setName("1");
		
		m_machine.setEntryWheel(retw);
		
		m_machine.addRotor(rI.getName(),rI);
		m_machine.addRotor(rII.getName(),rII);
		m_machine.addRotor(rV.getName(),rV);
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
		super.handleDisplayConfiguration("Possibly Hungarian Army, or maybe the Hungarian intelligence service");		
	}
}
