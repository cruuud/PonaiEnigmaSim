package nl.ponai.crypto.enigma.machines;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import nl.ponai.crypto.enigma.components.Rotor;
import nl.ponai.crypto.enigma.generic.EnigmaSimulator;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class CustomEnigma extends EnigmaSimulator
{
	private String customEnigmaConfigFilename;
	private String machineUsage;
	private int m_capacity;
	
	public CustomEnigma(String config)
	{		
		super("Custom Enigma");
		
		customEnigmaConfigFilename=config;
		File customConfig=new File(customEnigmaConfigFilename);
		String filename=customConfig.getName();			
		int indexOfDot=filename.indexOf(".");
		if(indexOfDot>0) {
			filename=filename.substring(0, indexOfDot);
		}
		setDescription(filename);			
	}
	
	public void configureMachine()
	{
		
		File customConfig=new File(customEnigmaConfigFilename);
		Properties customProps=new Properties();
		if(customConfig.exists()) {
						
			try {
				FileInputStream fisUltra=new FileInputStream(customConfig);
				customProps.load(fisUltra);
				fisUltra.close();
			}catch(Exception ex) {
				System.out.println("Failed to process the custom Enigma configuration from the file '"+ customEnigmaConfigFilename+"': "+ex.getMessage());
				System.exit(-1);
			}
		} else {
			try {
				System.out.println("The custom Enigma configuration file '"+customEnigmaConfigFilename+"' does not exist and will be created!");
				writeUltraConfigLayout(customConfig);
				System.out.println("The custom Enigma configuration file '"+customEnigmaConfigFilename+"' is created!");
				System.out.println("(You can edit this file later on to configure your machine)");
				try {
					FileInputStream fisUltra=new FileInputStream(customConfig);
					customProps.load(fisUltra);
					fisUltra.close();
				}catch(Exception ex) {
					System.out.println("Failed to process the custom Enigma configuration from the file '"+ customEnigmaConfigFilename+"': "+ex.getMessage());
					System.exit(-1);
				}

			}catch(IOException ex) {
				System.out.println("Failed to create the custom Enigma configuration file '"+customEnigmaConfigFilename+"': "+ex.getMessage());
				System.exit(-1);
			}			
		}
		System.out.println("Loaded custom Enigma configuration from file '"+customEnigmaConfigFilename+"'");
		
		boolean bConfigError=false;
		
		String alphabet=Tools.DEFAULT_ALPHABET;		
		String etwMapping=(String)customProps.getOrDefault("etw", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		etwMapping=etwMapping.toUpperCase();
		Rotor retw=new Rotor(false,alphabet,etwMapping);
		String usage=(String)customProps.getOrDefault("usage", "Unknown");
		machineUsage=usage;
		
		String capacity=customProps.getProperty("capacity");
		m_capacity=2;
		if(capacity!=null) {
			try {
				m_capacity=Integer.parseInt(capacity);
			}
			catch(NumberFormatException nfe) {
				m_capacity=2;
			}
		}
		
		List<String> reflectorNames=new ArrayList<String>();
		List<String> rotorNames=new ArrayList<String>();

		Enumeration<Object> keys=customProps.keys();
		while(keys.hasMoreElements()) {
			String key=(String)keys.nextElement();
			if(key.startsWith("reflector.")) {
				String[] items=key.split("\\.");
				if(items.length>2) {
					if(!reflectorNames.contains(items[1])) {
						if(items[1].length()>=1 && items[1].length()<=3)
						{
							reflectorNames.add(items[1]);
						}
						else
						{
							System.out.println("  Configuration error: name of reflector "+items[1]+" is too long (minimum=1, maximum=3 characters)!");
							bConfigError=true;
						}
					}
				}
			} else if(key.startsWith("rotor.")) {
				String[] items=key.split("\\.");
				if(items.length>2) {
					if(!rotorNames.contains(items[1])) {
						if(items[1].length()>=1 && items[1].length()<=7)
						{
							rotorNames.add(items[1]);
						}
						else
						{
							System.out.println("  Configuration error: name of rotor "+items[1]+" is too long (minimum=1, maximum=7 characters)!");
							bConfigError=true;
						}
					}
				}
			}
		}
		
		
		boolean bDoubleStep=Boolean.parseBoolean((String)customProps.getOrDefault("doublestep", "true"));
		boolean bHasPlugboard=Boolean.parseBoolean((String)customProps.getOrDefault("plugboard", "true"));		
		boolean bHasUhr=Boolean.parseBoolean((String)customProps.getOrDefault("uhr", "true"));
		boolean bHasUKWD=Boolean.parseBoolean((String)customProps.getOrDefault("ukwd", "true"));
		boolean bAlphaNumeric=Boolean.parseBoolean((String)customProps.getOrDefault("alphanumeric", "true"));
				
		String grouping=customProps.getProperty("grouping");
		int iGrouping=5;
		if(grouping!=null) {
			try {
				iGrouping=Integer.parseInt(grouping);
			}
			catch(NumberFormatException nfe) {
				iGrouping=5;
			}
		}
		if(iGrouping<0)iGrouping=-iGrouping;
		System.out.println("  Usage         : "+usage);
		System.out.println("  ETW           : "+etwMapping);
		System.out.println("  Capacity      : "+m_capacity+" (1 reflector, "+(m_capacity-1)+" rotors)");
		System.out.println("  Doublestepping: "+(bDoubleStep?"ENABLED":"DISABLED"));	
		System.out.println("  Uhr           : "+(bHasUhr?"ENABLED":"DISABLED"));
		System.out.println("  Plugboard     : "+(bHasPlugboard?"ENABLED":"DISABLED"));
		System.out.println("  UKW-D         : "+(bHasUKWD?"ENABLED":"DISABLED"));
		System.out.println("  Alphanumeric  : "+(bAlphaNumeric?"ENABLED":"DISABLED"));
		System.out.println("  Grouping      : "+iGrouping);

		if(!validateMapping(etwMapping, "the","ETW" ))
		{
			bConfigError=true;
		}

		if(m_capacity<2 || m_capacity>256) {
			System.out.println("  Configuration error: capacity must be a number in the range [2,256] (you entered "+m_capacity+")!");
			bConfigError=true;
		}
		
		if(bHasUhr && !bHasPlugboard)
		{
			
			System.out.println("  Configuration error: a machine with an Uhr needs a plugboard!");
			bConfigError=true;
		}
		
		for (String strReflector : reflectorNames) {
			String reflectorPropName="reflector."+strReflector;
			String reflectorMapping=(String)customProps.getOrDefault(reflectorPropName+".mapping","BADCFEHGJILKNMPORQTSVUXWZY");
			bConfigError=!validateMapping(reflectorMapping,"reflector",strReflector);
			if(!bConfigError)
			{
				bConfigError=!extraValidateReflector(reflectorMapping, strReflector);
			}
			boolean bReflectorRotates=Boolean.parseBoolean((String)customProps.getOrDefault(reflectorPropName+".rotates","false"));
			boolean bReflectorRingSetting=Boolean.parseBoolean((String)customProps.getOrDefault(reflectorPropName+".ringsetting","true"));
			boolean bReflectorPositioning=Boolean.parseBoolean((String)customProps.getOrDefault(reflectorPropName+".positioning","true"));
			
			Rotor rotorReflector=new Rotor(bReflectorRotates,alphabet,reflectorMapping);
			
			rotorReflector.setName(strReflector);
			rotorReflector.setIsReflector(true);
			rotorReflector.setHasPositioning(bReflectorPositioning);
			rotorReflector.setHasRing(bReflectorRingSetting);
			
			m_machine.addRotor(rotorReflector.getName(), rotorReflector);
		}
		
		if(reflectorNames.size()==0)
		{
			System.out.println("  Configuration error: there are no reflectors defined!");
			bConfigError=true;
		}
		
		for (String strRotor : rotorNames) {
			String rotorPropName="rotor."+strRotor;
			String rotorMapping=(String)customProps.getOrDefault(rotorPropName+".mapping",Tools.DEFAULT_ALPHABET);
			bConfigError=!validateMapping(rotorMapping,"rotor",strRotor);
			boolean bRotorRingSetting=Boolean.parseBoolean((String)customProps.getOrDefault(rotorPropName+".ringsetting","true"));
			boolean bRotorPositioning=Boolean.parseBoolean((String)customProps.getOrDefault(rotorPropName+".positioning","true"));
			
			Rotor rotor=new Rotor(true,alphabet,rotorMapping);
			
			rotor.setName(strRotor);
			rotor.setIsReflector(false);
			rotor.setHasPositioning(bRotorPositioning);
			rotor.setHasRing(bRotorRingSetting);
			
			String notches=(String)customProps.getOrDefault(rotorPropName+".notches","");
			for(int i=0;i<notches.length();i++)
			{
				rotor.addKnockOn(Tools.charToPosition(notches.charAt(i)));	
			}	
	
			m_machine.addRotor(rotor.getName(), rotor);
		}
	
		if(rotorNames.size()==0)
		{
			System.out.println("  Configuration error: there are no rotors defined!");
			bConfigError=true;
		}
		
		if(rotorNames.size()<(m_capacity-1))
		{
			System.out.println("  Configuration error: there aren't enough rotors defined to be inserted!");
			System.out.println("  The machine needs one reflector and "+(m_capacity-1)+" rotors, but only "+rotorNames.size()+" rotors are defined!");
			bConfigError=true;
		}
	
		if(bConfigError) 
		{
			System.out.println("Can't construct machine "+m_machine.getDescription()+": the configuration for this Custom Enigma contains errors!");
			System.exit(-1);
		}

		m_machine.setHasPlugboard(bHasPlugboard);		
		m_machine.setDisplayAlphabet(bAlphaNumeric);
		m_machine.setDoublestep(bDoubleStep);
		m_machine.setHasUhr(bHasUhr);
		m_machine.setGrouping(iGrouping);		
		m_machine.setHasUKWD(bHasUKWD);
			
		m_machine.setEntryWheel(retw);

		if(bHasUKWD)m_machine.setupUKWD();
	}
	
	private boolean validateMapping(String mapping,String rotorType,String rotorName) 
	{
		boolean retVal=true;
		Set<Character> mappingSet=new TreeSet<Character>();
		
		String alphabet=Tools.DEFAULT_ALPHABET;
		for(int i=0;i<26;i++) {
			mappingSet.add(new Character(alphabet.charAt(i)));
		}
		for(int i=0;i<mapping.length();i++) {
			mappingSet.remove(new Character(mapping.charAt(i)));
		}
		
		if(mappingSet.size()!=0)
		{
			System.out.println("  Configuration error for "+rotorType+" "+rotorName+": Number of uniquely mapped characters is "+(26-mappingSet.size())+" but must be 26!");
			retVal=false;
		}
		return retVal;
	}
	
	private boolean extraValidateReflector(String mapping,String reflectorName)
	{
		boolean retVal=true;
		int numSelf=0;
		for(int i=0;i<Tools.DEFAULT_ALPHABET.length();i++)
		{
			char src=Tools.DEFAULT_ALPHABET.charAt(i);
			char dst=mapping.charAt(i);
			if(src==dst)numSelf++;
		}
		if(numSelf!=0) 
		{
			System.out.println("  Configuration error for reflector "+reflectorName+": a character can't be mapped to itself (number of selfmapped characters: "+numSelf+")!");
			retVal=false;
		}
		return retVal;
	}
	
	private void writeUltraConfigLayout(File fOut) throws IOException {
		PrintStream ps=new PrintStream(new FileOutputStream(fOut));
		
		ps.println("#--------------------------------------------------------------------------------");
		ps.println("#This configuration file should contain the following 'name=value' pairs:");
		ps.println("#--------------------------------------------------------------------------------");
		ps.println("# Machine specifics:");
		ps.println("#--------------------------------------------------------------------------------");
		ps.println("# usage=usage of your machine");
		ps.println("#   This is the usage description of your machine, which will be displayed when");
		ps.println("#   the machine configuration is displayed.");
		ps.println("# etw=ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		ps.println("#   This is the mapping on the entrywheel. For a mapping you obviously need to");
		ps.println("#   know the from/to mapping, so in the case the 'from' part is the A-Z alphabet");
		ps.println("#   and the 'to' is the mapping you use as the value for the entrywheel property");
		ps.println("#   In case the 'to' part is also A-Z (the default value), A is mapped to A,");
		ps.println("#   B to B etcetera, which basically is no change at all (identity mapping)");
		ps.println("#   default=ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		ps.println("# capacity=number (minimum value=2, maximum value=256");
		ps.println("#   The total number of rotors (including reflector) this machine can hold");
		ps.println("#   default=2 (1 rotor and one reflector");
		ps.println("# doublestep=true/false");
		ps.println("#   Enable/disable double stepping effect (only occurs in the Enigma machines");
		ps.println("#   with levers, ratchets and notches. The Enigma machines with gear mechanisms");
		ps.println("#   (such as the A28 and the G) do not have this double stepping effect)");
		ps.println("#   default=true");
		ps.println("# plugboard=true/false");
		ps.println("#   enable/disable plugboard (default=true)");
		ps.println("# uhr=true/false");
		ps.println("#   enable/disable Uhr (default=true)");
		ps.println("# ukwd=true/false");
		ps.println("#   enable/disable UKW-D reflector (default=true)");
		ps.println("# grouping=number");
		ps.println("#   when enciphering/deciphering characters, add a space after this number of");
		ps.println("#   characters for displaying purposes. Example: in case of a value of 4,");
		ps.println("#   there will be a space between each group of 4 enciphered/deciphered");
		ps.println("#   characters default=5");
		ps.println("# alphanumeric=true/false");
		ps.println("#   enable/disable alphanumeric input of ring-setting and reflector/rotor");
		ps.println("#   positions. In case it's alphanumeric you type A-Z, otherwise it's 01-26.");
		ps.println("#   The Naval Enigma's (M3 and M4) displayed A-Z on the rotors, so it makes");
		ps.println("#   sense to use alphanumberic ring-settings/positions in that case (default=true)");
		ps.println("#--------------------------------------------------------------------------------");
		ps.println("# Reflector and rotor specifics");
		ps.println("#--------------------------------------------------------------------------------");
		ps.println("#Maximum name length of a reflector = 3 characters");
		ps.println("#Maximum name length of a rotor     = 7 characters");
		ps.println("# reflector/rotor.<name>.mapping=ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		ps.println("#   This is the mapping of a reflector/rotor with a given name 'name'. Mapping");
		ps.println("#   usage is the same as mentioned with the entrywheel.");
		ps.println("#   You may have as many rotors as you wish, as long as they have unique.");
		ps.println("#   names. The names are what you use when adding reflectors in the machine,");
		ps.println("#   displaying configuration etc.");
		ps.println("#   There can only be one reflector (for obvious reasons)");
		ps.println("#   default for rotor=ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		ps.println("#   default for reflector=BADCFEHGJILKNMPORQTSVUXWZY (a character can't be mapped");
		ps.println("#   to itself in a reflector, this would cause an electrical shortcircuit in a");
		ps.println("#   physical Enigma machine.)");
		ps.println("# reflector.<name>.rotates=true/false");
		ps.println("#   Whether or not the reflector can rotate (default=false)");
		ps.println("#   Mechanically it's not possible for rotors to be non-rotating, so you can only");
		ps.println("#   alter the rotate property for a reflector.");
		ps.println("#   If you want to simulate a non-rotating rotor, you need to add a rotor without");
		ps.println("#   any notches.");
		ps.println("#   Be aware that in case a rotor has no notches, it will not be able to rotate");
		ps.println("#   the rotor to the left of it. In that case, the rotor to the left of it can");
		ps.println("#   only rotate if it has notches AND its position can be set AND its position");
		ps.println("#   is set manually to a position where a pawl lands into its notch.");
		ps.println("#   In the case of an Enigma machine with the gear mechanism, rotors to the left");
		ps.println("#   will NEVER rotate, in case a rotor to the right does not have any notches.");
		ps.println("#   This is inherent to the gear mechanism involved that works like an odometer");
		ps.println("#   with multiple turnover points.");
		ps.println("# reflector/rotor.<name>.ringsetting=true/false");
		ps.println("#   Whether or not the reflector/rotor has a ring-setting (default=true)");
		ps.println("# reflector/rotor.<name>.positioning=true/false");
		ps.println("#   Whether or not the reflector/rotor can be set to a specific position");
		ps.println("#   default=true");
		ps.println("# rotor.<name>.notches=A-Z");
		ps.println("#   The notches are where pawls land on during movement of the rotors in the");
		ps.println("#   machine and when a pawl lands on a notch, the next rotor is basically engaged");
		ps.println("#   and will make a step too on the next keypress. For instance, if you have one");
		ps.println("#   notch on rotor A, the next rotor (rotor B, to the left of rotor A) will make");
		ps.println("#   a step once per 26 steps of rotor A. The position of the notch on rotor");
		ps.println("#   A will determine when rotor B makes a step.");
		ps.println("#   default value is empty, so no notches at all. This means that in operation,");
		ps.println("#   only the right-most rotor will make steps and the other rotors remain");
		ps.println("#   fixed.");
		ps.println("#   Example: to add a notch on position C (3), G (7) and K (11), you would use");
		ps.println("#   the value 'CGK'");
		ps.println("#   So when in an actual Enigma you would see the C,G or K in the display window");
		ps.println("#   for the specific rotor, the next keypress would advance the rotor to the");
		ps.println("#   left of it (and itself!).");
		ps.println("##############################################");
		ps.println("# MACHINE Settings");
		ps.println("##############################################");
		ps.println("usage=Sample custom Enigma machine (Enigma I in this example)");
		ps.println("etw=ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		ps.println("capacity=4");
		ps.println("doublestep=true");
		ps.println("plugboard=true");
		ps.println("uhr=true");
		ps.println("ukwd=true");
		ps.println("grouping=5");
		ps.println("alphanumeric=false");
		ps.println("##############################################");
		ps.println("# REFLECTOR Settings");
		ps.println("##############################################");
		ps.println("reflector.A.mapping=EJMZALYXVBWFCRQUONTSPIKHGD");
		ps.println("reflector.A.rotates=false");
		ps.println("reflector.A.positioning=false");
		ps.println("reflector.A.ringsetting=false");
		ps.println("#---------------------------------------------");
		ps.println("reflector.B.mapping=YRUHQSLDPXNGOKMIEBFZCWVJAT");
		ps.println("reflector.B.rotates=false");
		ps.println("reflector.B.positioning=false");
		ps.println("reflector.B.ringsetting=false");
		ps.println("#---------------------------------------------");
		ps.println("reflector.C.mapping=FVPJIAOYEDRZXWGCTKUQSBNMHL");
		ps.println("reflector.C.rotates=false");
		ps.println("reflector.C.positioning=false");
		ps.println("reflector.C.ringsetting=false");
		ps.println("##############################################");
		ps.println("# ROTOR Settings");
		ps.println("##############################################");
		ps.println("rotor.I.mapping=EKMFLGDQVZNTOWYHXUSPAIBRCJ");
		ps.println("rotor.I.positioning=true");
		ps.println("rotor.I.ringsetting=true");
		ps.println("rotor.I.notches=Q");
		ps.println("#---------------------------------------------");
		ps.println("rotor.II.mapping=AJDKSIRUXBLHWTMCQGZNPYFVOE");
		ps.println("rotor.II.positioning=true");
		ps.println("rotor.II.ringsetting=true");
		ps.println("rotor.II.notches=E");
		ps.println("#---------------------------------------------");
		ps.println("rotor.III.mapping=BDFHJLCPRTXVZNYEIWGAKMUSQO");
		ps.println("rotor.III.positioning=true");
		ps.println("rotor.III.ringsetting=true");
		ps.println("rotor.III.notches=V");
		ps.println("#---------------------------------------------");
		ps.println("rotor.IV.mapping=ESOVPZJAYQUIRHXLNFTGKDCMWB");
		ps.println("rotor.IV.positioning=true");
		ps.println("rotor.IV.ringsetting=true");
		ps.println("rotor.IV.notches=J");
		ps.println("#---------------------------------------------");
		ps.println("rotor.V.mapping=VZBRGITYUPSDNHLXAWMJQOFECK");
		ps.println("rotor.V.positioning=true");
		ps.println("rotor.V.ringsetting=true");
		ps.println("rotor.V.notches=Z");
		ps.println("#---------------------------------------------");
		ps.close();
	}
	public boolean isEquipped()
	{
		if(m_machine.getReflector()==null)
		{
			System.out.println("You have not inserted a reflector!");
			return false;
		}
		if(m_machine.getNumRotors()!=(m_capacity-1))
		{
			System.out.println("Not enough rotors inserted, you need " + (m_capacity-1)+ " but you inserted "+m_machine.getNumRotors());
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
		if(m_machine.getNumRotors()!=(m_capacity-1))
		{
			return false;
		}		
		return true;
	}	
	
	protected void handleDisplayConfiguration()
	{
		super.handleDisplayConfiguration(machineUsage);		
	}
}
