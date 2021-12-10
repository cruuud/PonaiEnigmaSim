package nl.ponai.crypto.enigma.machines;

import nl.ponai.crypto.enigma.components.Rotor;
import nl.ponai.crypto.enigma.generic.EnigmaSimulator;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class T extends EnigmaSimulator
{
	public T(String description)
	{
		super(description);
	}
	
	public void configureMachine()
	{
		String alphabet=Tools.DEFAULT_ALPHABET;			
		Rotor retw=new Rotor(false,alphabet,"KZROUQHYAIGBLWVSTDXFPNMCJE");
		
		Rotor rI=new Rotor(true,alphabet,"KPTYUELOCVGRFQDANJMBSWHZXI");
		Rotor rII=new Rotor(true,alphabet,"UPHZLWEQMTDJXCAKSOIGVBYFNR");
		Rotor rIII=new Rotor(true,alphabet,"QUDLYRFEKONVZAXWHMGPJBSICT");
		Rotor rIV=new Rotor(true,alphabet,"CIWTBKXNRESPFLYDAGVHQUOJZM");
		Rotor rV=new Rotor(true,alphabet,"UAXGISNJBVERDYLFZWTPCKOHMQ");
		Rotor rVI=new Rotor(true,alphabet,"XFUZGALVHCNYSEWQTDMRBKPIOJ");
		Rotor rVII=new Rotor(true,alphabet,"BJVFTXPLNAYOZIKWGDQERUCHSM");
		Rotor rVIII=new Rotor(true,alphabet,"YMTPNZHWKODAJXELUQVGCBISFR");
		
		rI.setHasPositioning(true);
		rII.setHasPositioning(true);
		rIII.setHasPositioning(true);
		rIV.setHasPositioning(true);
		rV.setHasPositioning(true);
		rVI.setHasPositioning(true);
		rVII.setHasPositioning(true);
		rVIII.setHasPositioning(true);

		rI.setHasRing(true);
		rII.setHasRing(true);
		rIII.setHasRing(true);
		rIV.setHasRing(true);
		rV.setHasRing(true);
		rVI.setHasRing(true);
		rVII.setHasRing(true);
		rVIII.setHasRing(true);
				
		rI.addKnockOn(Tools.charToPosition('W'));
		rI.addKnockOn(Tools.charToPosition('Z'));
		rI.addKnockOn(Tools.charToPosition('E'));
		rI.addKnockOn(Tools.charToPosition('K'));
		rI.addKnockOn(Tools.charToPosition('Q'));
		
		rII.addKnockOn(Tools.charToPosition('W'));
		rII.addKnockOn(Tools.charToPosition('Z'));
		rII.addKnockOn(Tools.charToPosition('F'));
		rII.addKnockOn(Tools.charToPosition('L'));
		rII.addKnockOn(Tools.charToPosition('R'));

		rIII.addKnockOn(Tools.charToPosition('W'));
		rIII.addKnockOn(Tools.charToPosition('Z'));
		rIII.addKnockOn(Tools.charToPosition('E'));
		rIII.addKnockOn(Tools.charToPosition('K'));
		rIII.addKnockOn(Tools.charToPosition('Q'));

		rIV.addKnockOn(Tools.charToPosition('W'));
		rIV.addKnockOn(Tools.charToPosition('Z'));
		rIV.addKnockOn(Tools.charToPosition('F'));
		rIV.addKnockOn(Tools.charToPosition('L'));
		rIV.addKnockOn(Tools.charToPosition('R'));

		rV.addKnockOn(Tools.charToPosition('Y'));
		rV.addKnockOn(Tools.charToPosition('C'));
		rV.addKnockOn(Tools.charToPosition('F'));
		rV.addKnockOn(Tools.charToPosition('K'));
		rV.addKnockOn(Tools.charToPosition('R'));
		
		rVI.addKnockOn(Tools.charToPosition('X'));
		rVI.addKnockOn(Tools.charToPosition('E'));
		rVI.addKnockOn(Tools.charToPosition('I'));
		rVI.addKnockOn(Tools.charToPosition('M'));
		rVI.addKnockOn(Tools.charToPosition('Q'));

		rVII.addKnockOn(Tools.charToPosition('Y'));
		rVII.addKnockOn(Tools.charToPosition('C'));
		rVII.addKnockOn(Tools.charToPosition('F'));
		rVII.addKnockOn(Tools.charToPosition('K'));
		rVII.addKnockOn(Tools.charToPosition('R'));
		
		rVIII.addKnockOn(Tools.charToPosition('X'));
		rVIII.addKnockOn(Tools.charToPosition('E'));
		rVIII.addKnockOn(Tools.charToPosition('I'));
		rVIII.addKnockOn(Tools.charToPosition('M'));
		rVIII.addKnockOn(Tools.charToPosition('Q'));
		
		Rotor rUKW=new Rotor(false,alphabet,"GEKPBTAUMOCNILJDXZYFHWVQSR");
		
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
		rIV.setName("IV");
		rV.setName("V");
		rVI.setName("VI");
		rVII.setName("VII");
		rVIII.setName("VIII");
		
		rUKW.setName("1");
		
		m_machine.setEntryWheel(retw);
		
		m_machine.addRotor(rI.getName(),rI);
		m_machine.addRotor(rII.getName(),rII);
		m_machine.addRotor(rIII.getName(),rIII);
		m_machine.addRotor(rIV.getName(),rIV);
		m_machine.addRotor(rV.getName(),rV);
		m_machine.addRotor(rVI.getName(),rVI);
		m_machine.addRotor(rVII.getName(),rVII);
		m_machine.addRotor(rVIII.getName(),rVIII);
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
		super.handleDisplayConfiguration("Communication between German and Japanese Navy");		
	}
}
