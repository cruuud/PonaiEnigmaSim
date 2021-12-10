package nl.ponai.crypto.enigma.machines;

import nl.ponai.crypto.enigma.components.EnigmaIWheelset;
import nl.ponai.crypto.enigma.components.EnigmaMxWheelset;
import nl.ponai.crypto.enigma.components.Rotor;
import nl.ponai.crypto.enigma.generic.EnigmaSimulator;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class M4 extends EnigmaSimulator
{
	private static final String BETA_ROTOR="Beta";
	private static final String GAMMA_ROTOR="Gamma";
	
	public M4(String description)
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
		Rotor rVI=new Rotor(true,alphabet,EnigmaMxWheelset.VI);
		Rotor rVII=new Rotor(true,alphabet,EnigmaMxWheelset.VII);
		Rotor rVIII=new Rotor(true,alphabet,EnigmaMxWheelset.VIII);
		
		String Beta="LEYJVCNIXWPBQMDRTAKZGFUHOS";
		String Gamma="FSOKANUERHMBTIYCWLQPZXVGJD";
		
		Rotor rBeta=new Rotor(false,alphabet,Beta);
		Rotor rGamma=new Rotor(false,alphabet,Gamma);		
				
		rI.setHasPositioning(true);
		rII.setHasPositioning(true);
		rIII.setHasPositioning(true);
		rIV.setHasPositioning(true);
		rV.setHasPositioning(true);
		rVI.setHasPositioning(true);
		rVII.setHasPositioning(true);
		rVIII.setHasPositioning(true);
		rBeta.setHasPositioning(true);
		rGamma.setHasPositioning(true);
		
		rI.setHasRing(true);
		rII.setHasRing(true);
		rIII.setHasRing(true);
		rIV.setHasRing(true);
		rV.setHasRing(true);
		rVI.setHasRing(true);
		rVII.setHasRing(true);
		rVIII.setHasRing(true);
		rBeta.setHasRing(true);
		rGamma.setHasRing(true);		
		
		rI.addKnockOn(Tools.charToPosition('Q'));
		rII.addKnockOn(Tools.charToPosition('E'));
		rIII.addKnockOn(Tools.charToPosition('V'));
		rIV.addKnockOn(Tools.charToPosition('J'));
		rV.addKnockOn(Tools.charToPosition('Z'));
		
		rVI.addKnockOn(Tools.charToPosition('Z'));
		rVI.addKnockOn(Tools.charToPosition('M'));
		rVII.addKnockOn(Tools.charToPosition('Z'));
		rVII.addKnockOn(Tools.charToPosition('M'));
		rVIII.addKnockOn(Tools.charToPosition('Z'));
		rVIII.addKnockOn(Tools.charToPosition('M'));
						
		Rotor rUKWB=new Rotor(false,alphabet,"ENKQAUYWJICOPBLMDXZVFTHRGS");
		Rotor rUKWC=new Rotor(false,alphabet,"RDOBJNTKVEHMLFCWZAXGYIPSUQ");
				
		rUKWB.setIsReflector(true);
		rUKWC.setIsReflector(true);
		
		m_machine.setHasPlugboard(true);		
		m_machine.setDisplayAlphabet(true);
		m_machine.setDoublestep(true);
		m_machine.setHasUhr(true);
		m_machine.setGrouping(4);		
		m_machine.setHasUKWD(true);
		
		rI.setName("I");
		rII.setName("II");
		rIII.setName("III");
		rIV.setName("IV");
		rV.setName("V");
		rVI.setName("VI");		
		rVII.setName("VII");
		rVIII.setName("VIII");
		rBeta.setName(BETA_ROTOR);
		rGamma.setName(GAMMA_ROTOR);				
		rUKWB.setName("b");
		rUKWC.setName("c");
		
		m_machine.setEntryWheel(retw);
		
		m_machine.addRotor(rI.getName(),rI);
		m_machine.addRotor(rII.getName(),rII);
		m_machine.addRotor(rIII.getName(),rIII);
		m_machine.addRotor(rIV.getName(),rIV);
		m_machine.addRotor(rV.getName(),rV);		
		m_machine.addRotor(rVI.getName(),rVI);
		m_machine.addRotor(rVII.getName(),rVII);
		m_machine.addRotor(rVIII.getName(),rVIII);
		m_machine.addRotor(rBeta.getName(),rBeta);
		m_machine.addRotor(rGamma.getName(),rGamma);		
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
		if(m_machine.getReflector().isUKWD())
		{
			if(m_machine.getNumRotors()!=3)
			{
				System.out.println("Not enough rotors inserted, you need 3 with the UKW-D in place but you inserted "+m_machine.getNumRotors());
				return false;
			}
		}
		else
		{
			if(m_machine.getNumRotors()!=4)
			{
				System.out.println("Not enough rotors inserted, you need 4 with the thin reflector in place, but you inserted "+m_machine.getNumRotors());
				return false;
			}			
		}			
		return true;
	}
	
	protected boolean quietIsEquipped()
	{
		if(m_machine.getReflector()==null)
		{
			return false;
		}
		if(m_machine.getReflector().isUKWD())
		{
			if(m_machine.getNumRotors()!=3)
			{			
				return false;
			}
		}
		else
		{
			if(m_machine.getNumRotors()!=4)
			{		
				return false;
			}			
		}			
		return true;
	}	
	
	protected boolean validateRotorInsert(String rotorName)
	{
		rotorName=rotorName.toUpperCase();
		if(rotorName.equals(GAMMA_ROTOR.toUpperCase()) || rotorName.equals(BETA_ROTOR.toUpperCase()))
		{
			if(m_machine.getNumRotors()==0)
			{
				if(m_machine.getReflector().isUKWD())
				{
					System.out.println("You can't insert the Beta/Gamma rotor when the UKW-D is inserted!");
					return false;
				}
				else
				{
					return true;
				}
			}			
			else
			{
				System.out.println("You can only insert the Beta/Gamma rotor directly after the reflector!");
				return false;
			}
		}
		else
		{
			if(m_machine.getReflector().isUKWD())
			{
				return true;
			}
			else
			{
				for(int i=0;i<m_machine.getNumRotors();i++)
				{
					Rotor r=m_machine.getRotor(i+1);
					String name=r.getName().toUpperCase();
					if(name.equals(GAMMA_ROTOR.toUpperCase()) || name.equals(BETA_ROTOR.toUpperCase()))
					{
						return true;
					}
				}
			}
			System.out.println("You need to insert the Beta/Gamma rotor first!");
			return false;
		}
	}
	
	
	protected void handleDisplayConfiguration()
	{
		super.handleDisplayConfiguration("U-Boot division of German Navy (Kriegsmarine)");				
	}
}
