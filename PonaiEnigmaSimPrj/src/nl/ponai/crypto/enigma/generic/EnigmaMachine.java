package nl.ponai.crypto.enigma.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.ponai.crypto.enigma.components.Plugboard;
import nl.ponai.crypto.enigma.components.Rotor;
import nl.ponai.crypto.enigma.components.RotorRotator;
import nl.ponai.crypto.enigma.components.UKWD;
import nl.ponai.crypto.enigma.components.Uhr;
import nl.ponai.crypto.enigma.notifiers.IMachineStatus;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class EnigmaMachine
{
	private Rotor m_entryWheel;
	private Rotor m_reflector;
	
	private Plugboard m_plugboard;
	private ArrayList<Rotor> m_rotors;
	
	private boolean m_bDoubleStep=true;
	private RotorRotator m_rotator;
	private String m_description="UNNAMED Enigma with doublestepping enabled";
	private boolean m_bDisplayAlphabet=true;
	
	private Map<String,Rotor> m_availableRotors;
	private Uhr m_uhr;
	
	private boolean m_bHasPlugboard=false;
	private boolean m_bUhrEnabled=false;
	private int m_grouping=5;
	private boolean m_bHasUhr=false;
	private boolean m_bOpen=true;
	private boolean m_bHasUKWD=false;
	private boolean m_bLuckenfuller=false;
	private IMachineStatus m_machineNotifier;
	
	private UKWD m_ukwd;
	
	public EnigmaMachine()
	{			
		m_availableRotors=new HashMap<String,Rotor>();
		String inputs=Tools.DEFAULT_ALPHABET;
		m_entryWheel=new Rotor(false, inputs,inputs);
		m_reflector=null;
		m_plugboard=new Plugboard();
		m_rotors=new ArrayList<Rotor>();	
		m_rotator=new RotorRotator(m_bDoubleStep);
		m_uhr=new Uhr();
	}
		
	public void setMachineStatusNotifier(IMachineStatus notifier)
	{	
		m_machineNotifier=notifier;
	}
	
	public void setLuckenfuller(boolean bLuckenfuller)
	{
		m_bLuckenfuller=bLuckenfuller;
		m_rotator.setLuckenfuller(m_bLuckenfuller);
	}
	
	public boolean isLuckenfuller()
	{
		return m_bLuckenfuller;
	}
	
	public void setupUKWD()
	{
		m_ukwd=new UKWD();		
		Rotor ukwdRotor=m_ukwd.getRotor();
		m_availableRotors.put(ukwdRotor.getName(),ukwdRotor);
	}
	
	public boolean isOpen()
	{
		return m_bOpen;
	}
	
	public boolean isClosed()
	{
		return !m_bOpen;
	}
	
	public void openMachine()
	{
		m_bOpen=true;
		if(m_machineNotifier!=null)m_machineNotifier.machineOpened();
	}
	
	public void closeMachine()
	{
		m_bOpen=false;		
		if(m_machineNotifier!=null)
		{
			ArrayList<Rotor> rotors=new ArrayList<Rotor>();
			if(m_reflector.hasPositioning())
			{
				rotors.add(m_reflector);
			}
			for (Rotor rotor : m_rotors) 
			{
				if(rotor.hasPositioning())
				{
					rotors.add(rotor);
				}
			}			
			m_machineNotifier.machineClosed(rotors);
		}
	}		
	
	public void forceMachineClosedNotification()
	{
		if(m_machineNotifier!=null)
		{
			ArrayList<Rotor> rotors=new ArrayList<Rotor>();
			if(m_reflector.hasPositioning())
			{
				rotors.add(m_reflector);
			}
			for (Rotor rotor : m_rotors) 
			{
				if(rotor.hasPositioning())
				{
					rotors.add(rotor);
				}
			}			
			m_machineNotifier.machineClosed(rotors);
		}		
	}
	
	public void setGrouping(int grouping)
	{
		m_grouping=grouping;
	}
	
	public int getGrouping()
	{
		return m_grouping;
	}
	
	public void setHasPlugboard(boolean bEnabled)
	{
		m_bHasPlugboard=bEnabled;
	}
	
	public boolean hasPlugboard()
	{
		return m_bHasPlugboard;
	}
	
	public void setUhrEnabled(boolean bEnabled)
	{
		m_bUhrEnabled=bEnabled;
	}

	public boolean getUhrEnabled()
	{
		return m_bUhrEnabled;
	}
	
	public boolean hasUKWD()
	{
		return m_bHasUKWD;
	}
	
	public UKWD getUKWD()
	{
		return m_ukwd;
	}
	
	public void setHasUKWD(boolean bHasUKWD)
	{
		m_bHasUKWD=bHasUKWD;
	}
	
	public Uhr getUhr()
	{
		return m_uhr;
	}
	
	public void setHasUhr(boolean bHasUhr)
	{
		m_bHasUhr=bHasUhr;
	}
	
	public boolean hasUhr()
	{
		return m_bHasUhr;
	}
	public boolean isDisplayAlphabet()
	{
		return m_bDisplayAlphabet;
	}

	public void setDisplayAlphabet(boolean bDisplayAlphabet)
	{
		m_bDisplayAlphabet = bDisplayAlphabet;
	}
	
	public void setDoublestep(boolean bDoublestep)
	{
		m_bDoubleStep=bDoublestep;
	}
	
	public String getDescription()
	{
		return m_description;
	}
	
	public void setDescription(String description)
	{
		m_description=description;
	}
	
	public Plugboard getPlugboard()
	{
		return m_plugboard;		
	}
	
	public Rotor getEntryWheel()
	{
		return m_entryWheel;
	}
		
	public void setEntryWheel(Rotor entryWheel)
	{
		m_entryWheel=entryWheel;
	}
	
	public Rotor getReflector()
	{
		return m_reflector;
	}

	public void setReflector(Rotor reflector)
	{
		m_reflector=reflector;
	}
	
	public void addRotor(String key,Rotor rotor)
	{
		m_availableRotors.put(key.toUpperCase(), rotor);
	}
	
	public Rotor getRotorByKey(String key)
	{
		return m_availableRotors.get(key.toUpperCase());
	}
		
	public int getNumRotors()
	{
		return m_rotors.size();
	}
	
	public String getPositionString()
	{
		String str="";

		if(m_reflector!=null)
		{
			if(m_reflector.hasPositioning())
			{
				str+=getPositionStringValue(m_reflector.getPosition());
			}		
		}
		
		str+=" ";
		
		for (Rotor rotor:m_rotors)
		{
			if(rotor.hasPositioning())
			{
				str+=getPositionStringValue(rotor.getPosition());
			}			
			str+=" ";
		}
		
		str=str.substring(0,str.length()-1);
		return str;
	}
	
	private String getPositionStringValue(int pos)
	{
		String returnValue=null;
		
		if(isDisplayAlphabet())
		{
			returnValue= ""+Tools.positionToChar(pos);
		}
		else
		{
			returnValue=""+pos;
			if(returnValue.length()!=2)returnValue="0"+returnValue;
		}			
		return returnValue;
	}

	public int getNumAvailableReflectors()
	{		
		List<String> names=getRotorNames();
		int numReflectors=0;
		for (String rotorName : names)
		{
			Rotor r=m_availableRotors.get(rotorName);
			if(r.isReflector())numReflectors++;
		}
		return numReflectors;
	}

	public Rotor getRotor(int position)
	{
		if(m_rotors.size()<position)return null;
		return m_rotors.get(position-1);
	}
	
	public List<String> getRotorNames()
	{
		Set<String> keys=m_availableRotors.keySet();
		
		List<String> lst=new ArrayList<String>();
		for (String name : keys)
		{
			lst.add(name);
		}
		
		Collections.sort(lst);
		
		return lst;
	}
	
	public boolean rotorInserted(String rotorName)
	{		
		for(int i=0;i<m_rotors.size();i++)
		{
			if(m_rotors.get(i).getName().toUpperCase().equals(rotorName.toUpperCase()))
			{
				return true;
			}
		}
		return false;
	}
	
	public void resetPositions()
	{
		for(int i=0;i<m_rotors.size();i++)
		{
			Rotor r=m_rotors.get(i);
			if(r.hasPositioning())
			{
				r.setPosition(1);
			}
		}
		
		if(m_reflector!=null)
		{
			if(m_reflector.hasPositioning())
			{
				m_reflector.setPosition(1);
			}
		}
	}

	public void resetRings()
	{
		List<String> rotorNames=getRotorNames();
		for (String rotorName : rotorNames)
		{							
			Rotor r=getRotorByKey(rotorName);
			if(r.hasRing())
			{
				r.setRingPosition(1);
			}						
		}
	}	
	
	public void insertRotorLeftToRight(Rotor rotor)
	{
		m_rotors.add(rotor);
	}
	
	public void removeRotors()
	{
		m_reflector=null;
		m_rotors=new ArrayList<Rotor>();
	}
	
	public void prepareForProcessing()
	{		
		m_rotator=new RotorRotator(m_bDoubleStep);
		for(int i=0;i<m_rotors.size();i++)
		{			
			m_rotator.addRotor(m_rotors.get(m_rotors.size()-i-1));
		}
		if(m_reflector!=null)
		{
			if(m_reflector.rotates())
			{
				m_rotator.addRotor(m_reflector);
			}
		}
		m_rotator.setLuckenfuller(m_bLuckenfuller);
	}
	
	public char processChar(char charFromKeyboard)
	{						
		int input=Tools.charToPosition(charFromKeyboard);		
					
		int toProcess=input;
				
		if(m_bUhrEnabled)
		{	
			toProcess=m_uhr.getUhred(true,input);
			if(toProcess==-1)
			{
				if(hasPlugboard())					
					toProcess=m_plugboard.getTo(input);
				else
					toProcess=input;
			}
		}
		else
		{
			if(hasPlugboard())		
				toProcess=m_plugboard.getTo(input);
			else
				toProcess=input;			
		}							
				
		toProcess=m_entryWheel.getInput(toProcess);			
		
		if(m_rotator!=null)m_rotator.doRotation();
		for(int i=0;i<m_rotors.size();i++)
		{			
			Rotor rotor=m_rotors.get(m_rotors.size()-i-1);				
			toProcess+=rotor.getPosition()-rotor.getRingPosition();
			if(toProcess>26)toProcess-=26;
			if(toProcess<1)toProcess+=26;					
			toProcess=rotor.getOutput(toProcess);				
			toProcess-=rotor.getPosition()-rotor.getRingPosition();
			if(toProcess>26)toProcess-=26;
			if(toProcess<1)toProcess+=26;			
		}
							
		toProcess+=m_reflector.getPosition()-m_reflector.getRingPosition();
		if(toProcess>26)toProcess-=26;
		if(toProcess<1)toProcess+=26;			
		toProcess=m_reflector.getInput(toProcess);					
		toProcess-=m_reflector.getPosition()-m_reflector.getRingPosition();

		for(int i=0;i<m_rotors.size();i++)
		{
			Rotor rotor=m_rotors.get(i);			
			toProcess+=rotor.getPosition()-rotor.getRingPosition();
			if(toProcess>26)toProcess-=26;
			if(toProcess<1)toProcess+=26;					
			toProcess=rotor.getInput(toProcess);				
			toProcess-=rotor.getPosition()-rotor.getRingPosition();
			if(toProcess>26)toProcess-=26;
			if(toProcess<1)toProcess+=26;			
		}
		
		toProcess=m_entryWheel.getOutput(toProcess);		
		
		int output=0;		
		if(m_bUhrEnabled)
		{								
			output=m_uhr.getUhred(false,toProcess);						
			if(output==-1)
			{					
				if(hasPlugboard())
					output=m_plugboard.getTo(toProcess);
				else
					output=toProcess;
			}
		}
		else
		{
			if(hasPlugboard())
				output=m_plugboard.getTo(toProcess);
			else
				output=toProcess;
		}				
		
		return Tools.positionToChar(output);		
	}
		
	protected String getAvailableReflectors()
	{
		String result="";
		List<String> rotors=this.getRotorNames();
					
		for (String rotorName : rotors)
		{
			Rotor rotor=getRotorByKey(rotorName);
			if(rotor.isReflector())
			{
				if(m_reflector==null || !m_reflector.getName().toUpperCase().equals(rotorName.toUpperCase()))
				{
					result+=rotor.getName()+" ";
				}
			}
		}
		return result;
	}

	protected String getAvailableRotors()
	{
		String result="";
		List<String> rotors=this.getRotorNames();
		for (String rotorName : rotors)
		{
			Rotor rotor=getRotorByKey(rotorName);
			if(!rotor.isReflector())
			{
				if(!rotorInserted(rotorName))
				{
					result+=rotor.getName()+" ";
				}
			}
		}
		
		return result;
	}
	
	public String parseConfiguration(String cfg)
	{		
		openMachine();
		removeRotors();
		
		List<Rotor> rotorList=new ArrayList<Rotor>();
		
		String items[]=cfg.split("\\|");
		String reflector=items[0].trim();
		Rotor actualReflector=null;
		int numPositionable=0;
		int numRingable=0;
		
		actualReflector=getRotorByKey(reflector);						
		if(actualReflector==null)
		{
			return configError(cfg, "Reflector '"+reflector+"' does not exist!");
		}			
		
		if(!actualReflector.isReflector())
		{
			return configError(cfg, "The component '"+reflector+"' is not a reflector, but a rotor!");			
		}
		if(actualReflector.hasRing())numRingable++;
		if(actualReflector.hasPositioning())numPositionable++;			
		
		boolean bIsM4=m_description.contains("M4");
		
		if(items.length>1 && !items[1].trim().equals(""))
		{
			String[] rotors=items[1].trim().split(" ");
									
			for(int i=0;i<rotors.length;i++)
			{
				Rotor actualRotor=getRotorByKey(rotors[i]);
				if(actualRotor==null)
				{
					return configError(cfg, "Rotor '"+rotors[i]+"' does not exist!");
				}
				
				if(actualRotor.isReflector())
				{
					return configError(cfg, "The component '"+rotors[i]+"' is not a rotor, but a reflector!");					
				}
				if(bIsM4)
				{
					if(rotors[i].toUpperCase().equals("BETA") || rotors[i].toUpperCase().equals("GAMMA"))
					{
						if(actualReflector.isUKWD())
						{
							return configError(cfg, "There is no room for the rotor '"+rotors[i]+"' when the UKW-D is inserted!");
						}	
						if(i>0)
						{
							return configError(cfg,"You can only insert the Beta/Gamma rotor directly after the reflector!");
						}
					}
					else
					{
						if(i==0 && !actualReflector.isUKWD())
						{
							return configError(cfg, "The Beta/Gamma rotor must be inserted first!");
						}
						
					}				
				}
				
				if(rotorInList(rotorList,actualRotor.getName()))
				{
					return configError(cfg,"You already inserted the rotor '"+actualRotor.getName()+"'!");
				}
				
				if(actualRotor.hasRing())numRingable++;
				if(actualRotor.hasPositioning())numPositionable++;
				rotorList.add(actualRotor);
			}
		}

		if(items.length>2 && !items[2].trim().equals(""))
		{
			String[] ringSettings=items[2].trim().split(" ");
			
			if(ringSettings.length!=numRingable)
			{
				return configError(cfg,"Incorrect number of ring-settings given, "+numRingable+" are required, you gave "+ringSettings.length);
			}
			
			if(actualReflector.hasRing())
			{
				int pos=getRingSetting(ringSettings[0]);
				if(pos==-1)return configError(cfg,"Invalid ring-setting '"+ringSettings[0]+"' for reflector '"+actualReflector.getName()+"'!");
				actualReflector.setRingPosition(pos);			
			}
			
			int offset=actualReflector.hasRing()?1:0;
			
			for(int i=0;i<ringSettings.length-offset;i++)
			{
				Rotor actualRotor=rotorList.get(i);
				int pos=getRingSetting(ringSettings[i+offset]);
				if(pos==-1)return configError(cfg,"Invalid ring-setting '"+ringSettings[i+offset]+"' for rotor '"+actualRotor.getName()+"'!");
				actualRotor.setRingPosition(pos);								
			}
		}
		
		if(items.length>3 && !items[3].trim().equals(""))
		{
			String[] positions=items[3].trim().split(" ");
			
			if(positions.length!=numPositionable)
			{
				return configError(cfg,"Incorrect number of positions given, "+numPositionable+" are required, you gave "+positions.length);
			}
			
			if(actualReflector.hasPositioning())
			{
				int pos=getPosition(positions[0]);
				if(pos==-1)return configError(cfg,"Invalid position '"+positions[0]+"' for reflector '"+actualReflector.getName()+"'!");
				actualReflector.setPosition(pos);
			}
			
			int offset=actualReflector.hasPositioning()?1:0;
			
			for(int i=0;i<positions.length-offset;i++)
			{
				Rotor actualRotor=rotorList.get(i);
				int pos=getPosition(positions[i+offset]);
				if(pos==-1)return configError(cfg,"Invalid position '"+positions[i+offset]+"' for rotor '"+actualRotor.getName()+"'!");
				actualRotor.setPosition(pos);
			}
		}
		
		if(items.length>4 && !items[4].trim().equals(""))
		{
			if(hasPlugboard())
			{
				Plugboard pb=getPlugboard();
				pb.backupBoard();
				pb.clear();
				
				String[] pairs=items[4].trim().toUpperCase().split(" ");
				
				if(pairs.length>13)
				{
					pb.restoreBoard();
					return configError(cfg,"Number of plugboard pairs ("+pairs.length+") exceeds the maximum of 13!"); 					
					
				}
				
				for(int i=0;i<pairs.length;i++)
				{
					char l=pairs[i].charAt(0);
					char r=pairs[i].charAt(1);
										
					if(pb.addPair(Tools.charToPosition(l),Tools.charToPosition(r))!=null)
					{
						pb.restoreBoard();
						return configError(cfg,"Invalid plugboard pair '"+pairs[i]+"'!");					
					}
				}
				
				getUhr().clear();
				for(int i=0;i<pb.getNumPairs();i++)
				{
					getUhr().addRed(i,pb.getA(i));
					getUhr().addBlack(i,pb.getB(i));
				}
			}
			else
			{
				System.out.println("WARNING - Ignoring plugboard pairs, because current machine does not have a plugboard!");
			}			
		}
		
		if(items.length>5 && !items[5].trim().equals(""))
		{
			if(hasUKWD())
			{
				boolean useBPNotation=false;
				if(items.length>6 && !items[6].trim().equals("")) {
					String notation=items[6].trim();
					if(notation.equalsIgnoreCase("DE")) {
						useBPNotation=false;
					} else if (notation.equalsIgnoreCase("BP")){
						useBPNotation=true;
					} else {
						return configError(cfg,"Invalid UKWD-D wiring notation ("+notation+"). Valid values are DE (default), or BP!");
					}
				}
				UKWD ukwd=getUKWD();
				ukwd.setBPNotation(useBPNotation);
				String result=ukwd.generateMapping(items[5].trim());			
				if(result!=null)
				{
					return configError(cfg,result);
				}
			}
			else
			{
				System.out.println("WARNING - Ignoring UKW-D wiring, because current machine does not have a UKW-D!");
			}
		}
		
		if(bIsM4 && !actualReflector.isUKWD())
		{			
			if(rotorList.size()!=4)
			{
				return configError(cfg,"Invalid number of rotors ("+rotorList.size()+"). You need 4 (one Beta/Gamma rotor and 3 'normal' rotors)!");
			}			
		}
		else
		{
			if(rotorList.size()!=3)
			{
				return configError(cfg,"Invalid number of rotors ("+rotorList.size()+"). You need 3!");
			}
		}
		
		setReflector(actualReflector);
						
		for (Rotor rotor:rotorList)
		{
			insertRotorLeftToRight(rotor);
		}
				
		return null;		
	}
	
	private boolean rotorInList(List<Rotor> rotors,String rotorName)
	{
		for (Rotor rotor : rotors) 
		{
			if(rotor.getName().toUpperCase().equals(rotorName.toUpperCase()))return true;
		}
		return false;
	}
	
	private int getPosition(String input)
	{
		int position=-1;
				
		try
		{
			position=Integer.parseInt(input);
			if(position<1 || position>26)
			{
				position=-1;
			}
		}
		catch(NumberFormatException nfe)
		{
			position=-1;
			char p=input.toUpperCase().charAt(0);
			if((input.length()==1) && (p>='A' && p<='Z'))
			{
				position =Tools.charToPosition(p);
			}
		}
		
		return position;
	}
	
	private int getRingSetting(String input)
	{
		int position=-1;
				
		try
		{
			position=Integer.parseInt(input);
			if(position<1 || position>26)
			{
				position=-1;
			}
		}
		catch(NumberFormatException nfe)
		{
			position=-1;
			char p=input.toUpperCase().charAt(0);
			if((input.length()==1) && (p>='A' && p<='Z'))
			{
				position =Tools.charToPosition(p);
			}
		}
		return position;
	}

	protected String configError(String cfg,String error)
	{
		return "Error in configuration '"+cfg+"': "+error;
	}
}
