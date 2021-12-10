package nl.ponai.crypto.enigma.generic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import nl.ponai.crypto.enigma.Version;
import nl.ponai.crypto.enigma.components.Plugboard;
import nl.ponai.crypto.enigma.components.Rotor;
import nl.ponai.crypto.enigma.components.UKWD;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class EnigmaSimulator
{
	protected EnigmaMachine m_machine;
	protected boolean m_bHideInput=false;
	protected EnigmaKeyboard m_keyboard;
	
	public EnigmaSimulator(String description)
	{
		m_machine=new EnigmaMachine();
		m_machine.setDescription(description);
		if(m_machine.hasUKWD())
		{
			if(m_machine.getUKWD()==null)
			{
				m_machine.setupUKWD();
			}
		}		
	}
	
	public void setDescription(String description) {
		m_machine.setDescription(description);
	}
	
	public void configureMachine()
	{
		//TODO Implement the characteristics such as wheels etc. in a subclass
	}
	
	public EnigmaMachine getMachine()
	{
		return m_machine;
	}

	public void setMachine(EnigmaMachine machine)
	{
		m_machine=machine;
	}
	
	public void runMachine(boolean bStandalone)
	{
		System.out.println("Machine: "+m_machine.getDescription());		
				
		System.out.println("Enter your command (? for help)\n");		
				
		Console console=System.console();
		if(console==null)
		{
			quitMachine("No console!");
			return;
		}			
		
		boolean bQuit=false;

		while(!bQuit)
		{
			System.out.print("> ");			
			String command=console.readLine().trim();			
			if(command!=null)
			{				
				String lc=command.toLowerCase();
				if(lc.equals("q"))
				{
					bQuit=true;
				}
				else if(lc.equals("?"))
				{
					System.out.println("v     Display simulator version");
					if(bStandalone)
					{
						System.out.println("q     Exit simulator");						
					}
					else
					{
						System.out.println("q     Return to machine selection screen");
					}
					System.out.println("tv    Toggles hide input (hide text input from user, default is no hide)");
					System.out.println("cls   Clear screen (simulated, a bunch of empty lines are printed)");
					System.out.println("om    Open machine");					
					System.out.println("irf   Insert the reflector");
					System.out.println("iro   Insert a rotor (from left to right)");
					System.out.println("cc    Display current inserted components");					
					System.out.println("gc    Get available components (excluding inserted components!)");
					System.out.println("cm    Close the machine");
					System.out.println("ld    Load a complete configuration in format 'reflector|rotor(s)|ring-setting(s)|position(s)|plugboard pair(s)|UKW-D wiring'");
					System.out.println("cfg   Display machine configuration");					
					System.out.println("clr   Remove all components from the machine");
					System.out.println("rstp  Set reflector and all rotors to position 1");
					System.out.println("rstr  Set ring-setting of reflector and all rotors to 1");
					System.out.println("msg   Encipher/decipher a message");
					System.out.println("file  Encipher/decipher a file (line per line, lines starting with # are ignored)");
					System.out.println("kbd   Open a GUI window where you can enter text to encipher/decipher");
					System.out.println("gp    Display current order and positions of inserted reflector/rotors (left to right)");
					System.out.println("sp    Set position of inserted reflector/rotors");
					System.out.println("spa   Set positions of all inserted reflector/rotors in one command");
					System.out.println("gr    Display current ring-settings of reflector/rotors");
					System.out.println("sr    Set ring-setting of reflector/rotor");	
					System.out.println("sra   Set ring-setting of all inserted reflector/rotors in one command");	
					if(m_machine.hasPlugboard())
					{
						System.out.println("gpb   Get plugboard");
						System.out.println("cpb   Clear plugboard");
						System.out.println("spb   Set plugboard");
					}
					if(m_machine.hasUhr())
					{
						System.out.println("du    Disable Uhr (default=disabled)");
						System.out.println("eu    Enable Uhr (default=disabled)");
						System.out.println("su    Set Uhr to position <position> (range [0,39])");
						System.out.println("gu    Get Uhr status (enabled/disabled) and position if enabled");
					}
					if(m_machine.hasUKWD())
					{
						System.out.println("gd    Get UKW-D wiring");
						System.out.println("sd    Set UKW-D wiring");
						System.out.println("srnd  Set UKW-D to random wiring");
						System.out.println("tdn   Toggle UKWD-D wiring notation to DE (Deutsch, notation used in German keysheets), or BP (Bletchley Park notation)");
						System.out.println("gdn   Get UKWD-D wiring notation");
						System.out.println("rstd  Reset UKW-D to wiring such as found in the Enigma KD at the FRA");
					}
					System.out.println("--------------------------------------------------------------------------");
					System.out.println("One attempt to improve the security of the Enigma dramatically was the");
					System.out.println("so called 'Luckenfullerwalze'. Instead of fixed notches on each rotor that");
					System.out.println("controlled the stepping mechanism of rotors, with this rotor you could set");
					System.out.println("1-26 of the notches manually before putting the rotors in the machine.");
					System.out.println("This type of rotor has never been used though, as it came too late.");
					System.out.println("However, you can enable/disable this mechanism, which will turn all of");
					System.out.println("the movable rotors into 'Luckenfullerwalzen'");
					System.out.println("Enabling this functionality will make the emulation non-authentic as you");
					System.out.println("can imagine.");
					System.out.println("--------------------------------------------------------------------------");
					System.out.println("tlf   Toggle 'Luckenfullerwalze' mode (default=disable)");
					System.out.println("slf   Set the notches for a specific 'Luckenfullerwalze'");
					System.out.println("glf   Get the notches for a specific 'Luckenfullerwalze'");
					
					showAvailableCommands();
				}
				else if(lc.equals("v"))
				{
					handleGetVersion();
				}
				else if(lc.equals("tv"))
				{
					handleToggleVisibility();
				}
				else if(lc.equals("cls"))
				{
					handleClearScreen();
				}
				else if(lc.equals("om"))
				{
					handleOpenMachine();
				}
				else if(lc.equals("cm"))
				{
					handleCloseMachine();
				}
				else if(lc.equals("ld"))
				{					
					handleLoadConfiguration(console);
				}				
				else if(lc.equals("cfg"))
				{
					handleDisplayConfiguration();
				}								
				else if(lc.equals("irf"))
				{
					if(m_machine.isClosed())
					{
						System.out.println("You can't insert components in a closed machine!");
					}
					else
					{
						if(m_machine.getNumRotors()>0)
						{
							System.out.println("Remove the rotors first!");
						}
						else
						{
							List<String> rotorNames=m_machine.getRotorNames();
							int numReflectors=0;
							Rotor reflectorRotor=null;
							for (String rotorName : rotorNames)
							{								
								Rotor r=m_machine.getRotorByKey(rotorName);
								if(r.isReflector())
								{
									reflectorRotor=r;
									numReflectors++;
								}
							}
							if(numReflectors==1)
							{
								m_machine.setReflector(reflectorRotor);
								System.out.println("Inserted reflector (there is only one)!");
								if(quietIsEquipped())
								{
									System.out.println("All necessary components are inserted, you can close the machine now!");
								}				
							}
							else
							{
								System.out.print("Which reflector do you want to insert: ");
								handleInsertReflector(Tools.getUserInput(console,m_bHideInput));
							}
						}
					}
				}
				else if(lc.equals("iro"))
				{
					if(m_machine.isClosed())
					{
						System.out.println("You can't insert components in a closed machine!");
					}
					else
					{	
						if(quietIsEquipped())
						{
							System.out.println("You can't add anymore components!");
						}						
						else
						{
							if(m_machine.getReflector()==null)
							{
								System.out.println("Insert a reflector first!");
							}
							else
							{								
								System.out.print("Which rotor do you want to insert: ");
								handleInsertRotor(Tools.getUserInput(console,m_bHideInput));
							}
						}
					}
				}	
				else if(lc.equals("cc"))
				{
					handleDisplayInserted();
				}
				else if(lc.equals("gc"))
				{
					handleGetAvailableComponents();
				}				
				else if(lc.equals("clr"))
				{
					handleClearMachine();
				}
				else if(lc.equals("rstp"))
				{
					handleResetPositions();
				}
				else if(lc.equals("rstr"))
				{
					handleResetRings();
				}								
				else if(lc.equals("msg"))
				{
					if(m_machine.isClosed())
					{
						System.out.print("Enter your message: ");
						handleEnteredMessage(Tools.getUserInput(console,m_bHideInput));						
					}
					else
					{
						System.out.println("The machine is open, you need to close it first!");
					}
				}
				else if(lc.equals("file"))
				{
					if(m_machine.isClosed())
					{
						System.out.print("Enter the file you want to encipher/decipher: ");
						
				
						String fileName=Tools.getUserInput(console,m_bHideInput);
						File inputFile=new File(fileName);
						if(inputFile.exists()) {
							System.out.print("Enciphering/deciphering the file '"+fileName+"' ... ");
							handleEnteredFile(inputFile);		
						} else {
							System.out.println("The file '"+fileName+"' does not exist!");
						}
					}
					else
					{
						System.out.println("The machine is open, you need to close it first!");
					}
				}
				else if(lc.equals("kbd"))
				{					
					if(m_machine.isClosed())
					{
						if(m_keyboard==null)
						{
							m_keyboard=new EnigmaKeyboard(this,m_machine);
						}
					}
					else
					{
						System.out.println("The machine is open, you need to close it first!");
					}
				}				
				else if(lc.equals("gp"))
				{
					if(!m_machine.isClosed())
					{
						System.out.println("No positions available, the machine is not closed yet!");
					}
					else
					{
						handleGetPositions();
					}
				}
				else if(lc.equals("sp"))
				{
					if(!m_machine.isClosed())
					{
						System.out.println("Can't set positions, the machine is not closed yet!");
					}
					else						
					{						
						System.out.print("Enter name of reflector/rotor to set the position: ");						
						handleSetPosition(console,Tools.getUserInput(console,m_bHideInput));
						if(m_keyboard!=null)m_keyboard.updatePosition(m_machine.getPositionString());
					}
				}
				else if(lc.equals("spa"))
				{
					if(!m_machine.isClosed())
					{
						System.out.println("Can't set positions, the machine is not closed yet!");
					}
					else						
					{						
						System.out.println("Enter space-separated list of positions in order from left to right");
						System.out.println("Use a '.' to ignore a certain reflector/rotor");
						System.out.print("Example: ");
						System.out.println("A . C or 01 . 03");
						System.out.println("This will set the positions of the reflector and the rightmost rotor");
						System.out.print("\nEnter the positions: ");
						String rotor=Tools.getUserInput(console,m_bHideInput).toUpperCase();
						
						String[] rotors=rotor.split(" ");
						if(rotor.trim().equals(""))
						{
							System.out.println("No positions given!");
						}
						else
						{
							handleSetMultiplePosition(console,rotors);
						}
						if(m_keyboard!=null)m_keyboard.updatePosition(m_machine.getPositionString());
					}					
				}
				else if(lc.equals("gr"))
				{
					handleGetRings();
				}
				else if(lc.equals("sr"))
				{
					System.out.print("Enter name of reflector/rotor: ");					
					String rotor=Tools.getUserInput(console,m_bHideInput).toUpperCase();
					handleSetRingSetting(console,rotor);					
				}
				else if(lc.equals("sra"))
				{
					System.out.println("Enter space-separated list of ring-setting in order from left to right");
					System.out.println("Use a '.' to ignore a certain reflector/rotor");
					System.out.print("Example: ");
					System.out.println("A . C or 01 . 03");
					System.out.println("This will set the positions of the reflector and the rightmost rotor");
					System.out.print("\nEnter the ring-settings: ");
					String rotor=Tools.getUserInput(console,m_bHideInput).toUpperCase();
					
					String[] rotors=rotor.split(" ");
					if(rotor.trim().equals(""))
					{
						System.out.println("No ring-settings given!");
					}
					else
					{
						handleSetMultipleRingSettings(console,rotors);
					}
				}
				else if(lc.equals("du") && m_machine.hasUhr())
				{	
					handleEnableUhr(false);
				}	
				else if(lc.equals("eu") && m_machine.hasUhr())
				{	
					handleEnableUhr(true);
				}			
				else if(lc.equals("gu") && m_machine.hasUhr())
				{
					handleGetUhr();
				}
				else if(lc.equals("su") && m_machine.hasUhr())
				{
					if(!m_machine.getUhrEnabled())
					{
						System.out.println("Uhr is disabled, can't set position!");
					}
					else
					{
						System.out.print("Enter new Uhr position (range [0-39]): ");
						String strPosition=Tools.getUserInput(console,m_bHideInput);
						int position=0;					
						try
						{
							position=Integer.parseInt(strPosition);						
						}
						catch(NumberFormatException nfe)
						{
							position=-1;
						}										
						handleSetUhr(position);
					}
				}	
				else if(lc.equals("gpb") && m_machine.hasPlugboard())
				{
					handleGetPlugboard();
				}
				else if(lc.equals("cpb") && m_machine.hasPlugboard())
				{
					handleClearPlugboard();
				}				
				else if(lc.equals("spb") && m_machine.hasPlugboard())
				{					
					System.out.print("Enter plug pairs to add (AB CD EF etc.): ");
					String pairs=Tools.getUserInput(console,m_bHideInput);
					handleSetPlugboard(pairs);
				}				
				else if(lc.equals("gd") && m_machine.hasUKWD())
				{
					handleGetUKWDWiring();
				}
				else if(lc.equals("sd") && m_machine.hasUKWD())
				{
					if(m_machine.getReflector()!=null && m_machine.getReflector().isUKWD())
					{
						System.out.println("You must take the UKW-D reflector out of the machine first!");
					}
					else
					{
						System.out.print("Enter UKWD-D mapping as mapping pairs (AB CD EF etc.): ");
						String pairs=Tools.getUserInput(console,m_bHideInput);
						handleSetUKWDWiring(pairs);
					}
				}				
				else if(lc.equals("srnd") && m_machine.hasUKWD())
				{
					if(m_machine.getReflector()!=null && m_machine.getReflector().isUKWD())
					{
						System.out.println("You must take the UKW-D reflector out of the machine first!");
					}
					else
					{
						handleSetRandomUKWDWiring();
					}				
				}
				else if(lc.equals("tdn") && m_machine.hasUKWD())
				{
					handleToggleUKWDWiringNotation();
				}
				else if(lc.equals("gdn") && m_machine.hasUKWD())
				{
					handleGetUKWDWiringNotation();
				}
				else if(lc.equals("rstd") && m_machine.hasUKWD())
				{
					if(m_machine.getReflector()!=null && m_machine.getReflector().isUKWD())
					{
						System.out.println("You must take the UKW-D reflector out of the machine first!");
					}
					else
					{
						handleResetUKWDWiring();
					}				
				}
				else if(lc.equals("tlf"))
				{
					handleToggleLuckenfuller();
				}
				else if(lc.equals("slf"))
				{
					handleSetLuckenfuller(console);
				}
				else if(lc.equals("glf"))
				{
					handleGetLuckenfuller(console);
				}
				else if(!handleSpecificCommands(command))
				{
					System.out.println("Invalid command '"+command+"'");
				}
			}
		}
		
		if(m_machine!=null)
		{
			m_machine.openMachine();
		}
		
		if(m_keyboard!=null)
		{
			m_keyboard.dispose();
			m_keyboard=null;
		}
	}

	private void handleToggleUKWDWiringNotation() {
		UKWD ukwd=m_machine.getUKWD();
		ukwd.setBPNotation(!ukwd.isBPNotation());
		System.out.println("UKW-D notation set to "+(ukwd.isBPNotation()?"BP (Bletchley Park notation)":"DE (Deutsch, notation as used in German keysheets)"));
	}
	
	private void handleGetUKWDWiringNotation() {
		UKWD ukwd=m_machine.getUKWD();
		System.out.println("UKW-D notation set to "+(ukwd.isBPNotation()?"BP (Bletchley Park notation)":"DE (Deutsch, notation as used in German keysheets)"));
	}

	public void closedKeyboard()
	{
		m_keyboard=null;
	}
	
	private void handleResetUKWDWiring() 
	{
		m_machine.getUKWD().reset();
		System.out.println("UKW-D wiring reset to default!");
	}

	private void handleSetRandomUKWDWiring() 
	{
		UKWD ukwd=m_machine.getUKWD();
		if(ukwd.generateMapping(UKWD.generateRandomWiring(ukwd.isBPNotation()))==null) {
			System.out.println("UKWD-D is rewired!");
		}
	}
	
	private void handleClearScreen()
	{
		for(int i=0;i<100;i++)System.out.println("");
	}

	private void handleLoadConfiguration(Console console)
	{
		if(m_machine.isClosed())
		{
			System.out.println("You need to open the machine first!");
			return;
		}
		
		System.out.print("Enter the configuration to load: ");
		String config=Tools.getUserInput(console, m_bHideInput);
		
		if(config.trim().equals(""))
		{
			System.out.println("No configuration entered!");
		}
		else
		{
			String result=m_machine.parseConfiguration(config);
			if(result!=null)
			{
				System.out.println(result);
			}
			else
			{
				if(isEquipped())
				{
					m_machine.closeMachine();
					m_machine.prepareForProcessing();
					System.out.println("Machine config loaded, machine is fully equipped and ready to encipher/decipher messages!");
				}			
				else
				{
					System.out.println("Machine config is not complete, you need to manually add the necessary components!");
				}
			}
		}
	}

	private void handleGetAvailableComponents()
	{ 
		System.out.println("------------------------------------");
		System.out.println("Available components (not inserted):");
		System.out.println("------------------------------------");
		System.out.println("Reflectors: "+m_machine.getAvailableReflectors()+"\nRotors    : "+m_machine.getAvailableRotors());
	}

	private void handleGetLuckenfuller(Console console)
	{
		if(m_machine.isLuckenfuller())
		{
			System.out.print("Enter the reflector/rotor: ");
			String rotorName=Tools.getUserInput(console, m_bHideInput);
			Rotor rotor=m_machine.getRotorByKey(rotorName);
			if(rotor==null)
			{
				System.out.println("The given rotor/reflector does not exist!");
				return;
			}
			
			if(rotor.rotates())
			{
				System.out.println(rotor.getLuckenfullerConfig());
				
			}
			else
			{
				System.out.println("This is a non movable "+(rotor.isReflector()?"reflector":"rotor")+"!");
			}
		}		
		else
		{
			System.out.println("Luckenfuller is not enabled!");
		}
	}

	private void handleSetLuckenfuller(Console console) 
	{
		if(m_machine.isLuckenfuller())
		{
			System.out.print("Enter the reflector/rotor: ");
			String rotorName=Tools.getUserInput(console, m_bHideInput);
			if(m_machine.rotorInserted(rotorName))
			{
				System.out.println("You need to remove this rotor from the machine first!");
			}
			else
			{
				Rotor rotor=m_machine.getRotorByKey(rotorName);
				if(rotor==null)
				{
					System.out.println("The given rotor/reflector does not exist!");
					return;
				}
				
				if(rotor.rotates() && rotor.isReflector() && m_machine.getReflector()==rotor)
				{
					System.out.println("You need to remove this reflector from the machine first!");
					return;
				}
					
				if(rotor.rotates())
				{
					System.out.print("Enter the notches to set (for example: ABC to set notches A,B and C): ");
					String notches=Tools.getUserInput(console, m_bHideInput).toUpperCase();
					
					char[] tbl=new char[26];
					for(int n=0;n<notches.length();n++)
					{
						char c=notches.charAt(n);
						if(c>='A' && c<='Z')
						{
							int index=Tools.charToPosition(c)-1;
							if(tbl[index]==0)tbl[index]=1;
						}
					}
					notches="";
					for(int n=0;n<tbl.length;n++)
					{
						if(tbl[n]!=0)
						{
							notches+=Tools.positionToChar(n+1);
						}
					}
					
					if(notches.length()>26)
					{
						System.out.println("You can't set more than 26 notches!");
					}
					else
					{
						for(int i=0;i<notches.length();i++)
						{
							if(notches.charAt(i)<'A' || notches.charAt(i)>'Z')
							{
								System.out.println("Invalid notches, each notch must be in range [A-Z]!");
								return;
							}
						}
						rotor.setLuckenfullerNotches(notches);
						if(notches.length()==0)
						{
							System.out.println("All notches deactivated!");
						}
						else
						{
							System.out.println(notches.length()+" notch"+(notches.length()>1?"es":"")+" activated!");
						}
					}
					
				}
				else
				{
					System.out.println("This is a non movable "+(rotor.isReflector()?"reflector":"rotor"));
				}
			}
		}		
		else
		{
			System.out.println("Luckenfuller is not enabled!");
		}
	}

	private void handleToggleLuckenfuller()
	{
		if(m_machine.isClosed())
		{
			System.out.println("You must open the machine first!");
			return;
		}
		if(m_machine.getNumRotors()>0)
		{
			System.out.println("You must first take out the rotors!");
			return;
		}
		
		Rotor reflector=m_machine.getReflector();
		if(reflector!=null)
		{
			if(reflector.rotates())
			{
				System.out.println("You must first take out the movable reflector!");
				return;
			}
		}
		m_machine.setLuckenfuller(!m_machine.isLuckenfuller());
		
		if(m_machine.isLuckenfuller())
		{
			System.out.println("Luckenfuller for all moving rotors is enabled!");
		}
		else
		{
			System.out.println("Luckenfuller mode disabled, rotors behaviour is back to normal!");
		}
	}

	private void handleToggleVisibility() 
	{
		m_bHideInput=!m_bHideInput;
		if(m_bHideInput)
		{
			System.out.println("User input is hidden as of now!");
		}
		else
		{
			System.out.println("User input is visible as of now! ");
		}
	}

	protected boolean validateRotorInsert(String rotorName)
	{
		//TODO Override in subclass for determining valid rotor insert if needed
		return true;		
	}
	
	public boolean isEquipped()
	{
		//TODO Override in subclass for determining valid state of machine
		return false;
	}
	
	protected boolean quietIsEquipped()
	{
		//TODO Override in subclass for determining valid state of machine (don't output equipped status!!)
		return false;
	}
	
	protected void showAvailableCommands()
	{
		//TODO Override in subclass for extra Enigma specific commands
	}
	
	protected boolean handleSpecificCommands(String command)
	{
		//TODO Override in subclass for extra Enigma specific commands
		return false;
	}
	
	
	private void handleOpenMachine()
	{
		if(m_machine.isOpen())
		{
			System.out.println("The machine is already opened!");
		}
		else
		{
			m_machine.openMachine();
			if(m_keyboard!=null)m_keyboard.disableTyping();
			System.out.println("Machine is opened!");
			if(m_keyboard!=null)m_keyboard.updatePosition("? ? ? ?");
		}
	}
	private void handleCloseMachine()
	{
		if(m_machine.isClosed())
		{
			System.out.println("The machine is already closed!");
		}
		else
		{
			if(isEquipped())
			{	
				m_machine.closeMachine();
				m_machine.prepareForProcessing();
				System.out.println("Machine is fully equipped and ready to encipher/decipher messages!");
				if(m_keyboard!=null)m_keyboard.enableTyping();
				if(m_keyboard!=null)m_keyboard.updatePosition(m_machine.getPositionString());
			}
			else
			{
				System.out.println("Machine can not be closed yet!");
			}
		}		
	}
	
	protected void handleDisplayConfiguration()
	{
		//TODO Override in subclass to display available components
	}
	
	private void handleInsertReflector(String reflectorName)
	{
		Rotor reflector=m_machine.getRotorByKey(reflectorName);
		
		if(reflector==null)
		{
			if(!reflectorName.trim().equals(""))System.out.println("This reflector does not exist!");
			System.out.println("Available reflectors: "+m_machine.getAvailableReflectors());
		}
		else
		{
			if(!reflector.isReflector())
			{
				System.out.println("You can't use a rotor as a reflector!");
			}
			else
			{
				m_machine.setReflector(reflector);
				System.out.println("Inserted reflector!");
				if(quietIsEquipped())
				{
					System.out.println("All necessary components are inserted, you can close the machine now!");
				}				
			}
		}
	}
	
	private void handleInsertRotor(String rotorName)
	{
		Rotor rotor=m_machine.getRotorByKey(rotorName);
		if(rotor==null)
		{
			if(!rotorName.trim().equals(""))
			System.out.println("This rotor does not exist!");
			System.out.println("Available rotors: "+m_machine.getAvailableRotors());
		}
		else
		{
			if(rotor.isReflector())
			{
				System.out.println("You can't use a reflector as a rotor!");
			}
			else
			{				
				if(!m_machine.rotorInserted(rotorName))
				{
					if(validateRotorInsert(rotorName))
					{
						m_machine.insertRotorLeftToRight(rotor);		
						System.out.println("Inserted rotor!");
						if(quietIsEquipped())
						{
							System.out.println("All necessary components are inserted, you can close the machine now!");
						}
					}
				}
				else
				{
					System.out.println("You already inserted this rotor!");
				}
			}
		}		
	}
	private void handleDisplayInserted()
	{
		Rotor reflector=m_machine.getReflector();
		if(reflector!=null)
		{
			System.out.println("--------------------");
			System.out.println("Inserted components:");
			System.out.println("--------------------");
			System.out.println("Reflector             : "+reflector.getName());
			int numRotors=m_machine.getNumRotors();
			System.out.print("Rotors (left to right): ");
			if(numRotors>0)
			{				
				for(int i=0;i<numRotors;i++)
				{
					Rotor rotor=m_machine.getRotor(i+1);
					System.out.print(rotor.getName()+" ");
				}
				System.out.println("");
			}
			else
			{
				System.out.println("None inserted yet!");
			}
			
			if(isEquipped())
			{
				System.out.println("All necessary components are inserted!");
			}
			
			return;
		}
		System.out.println("You have not inserted any components yet!");
	}
	private void handleClearMachine()
	{
		if(!m_machine.isOpen())
		{
			System.out.println("Machine is closed, open it first!");
			return;
		}
		m_machine.resetPositions();
		m_machine.removeRotors();
		System.out.println("Reflector and rotors are removed!");
	}
	
	private void handleResetPositions()
	{
		if(m_machine.isOpen())
		{
			System.out.println("Machine is open, close it first!");
			return;
		}
		m_machine.resetPositions();
		System.out.println("All positions set to "+(m_machine.isDisplayAlphabet()?"A (01)":"01 (A)")+"!");		
		if(m_keyboard!=null)m_keyboard.updatePosition(m_machine.getPositionString());
	}

	private void handleResetRings()
	{
		if(m_machine.isClosed())
		{
			System.out.println("Machine is closed, open it first!");
			return;
		}
		m_machine.resetRings();
		System.out.println("All ring-settings of reflectors/rotors in the machine are set to 01 (A)!");					
	}
	
	private void handleEnableUhr(boolean bEnabled)
	{
		if(bEnabled)
		{
			int numPluggedIn=m_machine.getPlugboard().getNumPairs();
			
			if(numPluggedIn<10)
			{
				System.out.println("Can't enable Uhr, at least 10 pairs must be plugged in ("+numPluggedIn+" pairs plugged in)");
				return;
			}
		}
		m_machine.setUhrEnabled(bEnabled);
		System.out.println("Uhr is now "+(bEnabled?"enabled":"disabled")+"!");
	}
	
	private void handleSetUhr(int position)
	{
		if(position <0 || position>39)
		{
			System.out.println("Invalid Uhr position!");
		}
		else
		{
			m_machine.getUhr().setUhrPosition(position);
			System.out.println("New Uhr position is set!");
		}
	}
	private void handleGetUhr()
	{
		if(!m_machine.getUhrEnabled())
		{
			System.out.println("Uhr is disabled!");
		}
		else
		{			
			int position=m_machine.getUhr().getUhrPosition();
			System.out.println("Uhr is enabled and set to position "+position);			
		}
	}
	
	private void handleEnteredMessage(String enteredMessage)
	{
		if(enteredMessage.length()==0)return;
		String msg=enteredMessage.toUpperCase();
		StringBuffer result=new StringBuffer();
		int counter=0;
		int length=msg.length();
		for(int i=0;i<length;i++)
		{
			char ch=msg.charAt(i);
			if(ch<='Z' && ch>='A')
			{
				ch=m_machine.processChar(ch);
				result.append(ch);
				counter++;
				counter%=m_machine.getGrouping();
				if(counter==0 && i<(length-1))
				{
					result.append(" ");
				}										
			}
		}
		System.out.println(result.toString());	
		if(m_keyboard!=null)m_keyboard.updatePosition(m_machine.getPositionString());
	}
	
	public void handleEnteredFile(File inputFile)
	{ 
		BufferedReader br=null;
		BufferedWriter bw=null;
		File outputFile=null;
		
		try {
			br=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			
			String line=br.readLine();
			int numGroups=0;
			int counter=0;
			while(line!=null) {
				
				line=line.trim();
				if(line.startsWith("#")) {
					line=br.readLine();
					continue;
				}
				line=line.toUpperCase();
				int length=line.length();
				StringBuffer sb=new StringBuffer();
				int numKarakters=0;
				for(int i=0;i<length;i++)
				{
					char ch=line.charAt(i);
					if(ch<='Z' && ch>='A')
					{
						ch=m_machine.processChar(ch);
						sb.append(ch);
						numKarakters++;
						counter++;
						counter%=m_machine.getGrouping();
						if(counter==0)
						{
							sb.append(" ");
							numGroups++;
							numGroups%=16;
							if(numGroups==0) {
								sb.append("\n");
							}
						}										
					}
				}
				if(numKarakters>0) {
					if(bw==null) {
						outputFile=new File(inputFile.getAbsolutePath()+".egm");
						bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
					}
					bw.write(sb.toString());	
				}
				if(m_keyboard!=null)m_keyboard.updatePosition(m_machine.getPositionString());
				line=br.readLine();
			}
			
			if(outputFile!=null) {
				System.out.println(" OK");
				System.out.println("Enciphered/deciphered file is stored as '"+outputFile.getAbsolutePath()+"'");
			} else {
				System.out.println(" NO RESULT -> File contains no suitable data to encipher/decipher (no A-Z characters in non-comment lines)");
			}
		}catch(Exception ex) {
			System.out.println(" FAIL -> "+ex.getMessage());
		}
		finally
		{
			try{
				if(bw!=null) {
					bw.close();
				}
				if(br!=null) {
					br.close();
				}
			}
			catch(IOException ex)
			{
				
			}
		}
	}
		
	private void handleGetPositions()
	{						
			
		Rotor reflector=m_machine.getReflector();			
		String colName="";
		
		if(reflector!=null)
		{
			colName="UKW-"+reflector.getName()+"        ";
			colName=colName.substring(0,8);
			System.out.print(colName);
		}
		
		int numRotors=m_machine.getNumRotors();
		for(int i=0;i<numRotors;i++)
		{
			Rotor rotor=m_machine.getRotor(i+1);
			colName=rotor.getName()+"        ";
			colName=colName.substring(0,8);
			System.out.print(colName);			
		}
		
		if(numRotors>0 || reflector!=null)
		{
			System.out.println("");
			if(reflector!=null)System.out.print("--------");
			for(int i=0;i<numRotors;i++)
			{
				System.out.print("--------");
			}
			System.out.println("");
		}
		
		if(reflector!=null)System.out.print(getPosition(reflector,!m_machine.isDisplayAlphabet()));
		
		for(int i=0;i<numRotors;i++)
		{
			Rotor r=m_machine.getRotor(i+1);
			System.out.print(getPosition(r,!m_machine.isDisplayAlphabet()));
		}
		System.out.println("");
		
		if(reflector!=null)System.out.print(getPosition(reflector,m_machine.isDisplayAlphabet()));
		
		for(int i=0;i<numRotors;i++)
		{
			Rotor r=m_machine.getRotor(i+1);
			System.out.print(getPosition(r,m_machine.isDisplayAlphabet()));
		}
		System.out.println("");
	}
	
	private void handleGetRings()
	{												
		int numRotors=0;
		List<String> rotors=m_machine.getRotorNames();
		
		String colName="";
		
		if(!quietIsEquipped())
		{					
			numRotors=rotors.size();
			
			for (String rotorName : rotors)
			{	
				Rotor r=m_machine.getRotorByKey(rotorName);
				if(r.isReflector())
				{
					colName="UKW-"+r.getName()+"        ";
					colName=colName.substring(0,8);
					System.out.print(colName);
				}
			}
			for (String rotorName : rotors)
			{		
				Rotor r=m_machine.getRotorByKey(rotorName);
				if(!r.isReflector())
				{
					colName=r.getName()+"        ";
					colName=colName.substring(0,8);
					System.out.print(colName);
				}
			}			
		}
		else
		{
			Rotor reflector=m_machine.getReflector();
			colName="UKW-"+reflector.getName()+"        ";
			colName=colName.substring(0,8);
			System.out.print(colName);							
			
			numRotors=m_machine.getNumRotors();
			for (int i=0;i<numRotors;i++)
			{				
				Rotor rotor=m_machine.getRotor(i+1);
				if(!rotor.getName().equals(reflector.getName()))
				{
					colName=rotor.getName()+"        ";
					colName=colName.substring(0,8);
					System.out.print(colName);
				}
			}			
		}
		
		System.out.println("");
		
		if(quietIsEquipped())
		{
			System.out.print("--------");
		}
		
		for(int i=0;i<numRotors;i++)
		{
			System.out.print("--------");
		}
		System.out.println("");
				
		if(!quietIsEquipped())
		{		
			
			for (String rotorName : rotors)
			{		
				Rotor r=m_machine.getRotorByKey(rotorName);
				if(r.isReflector())
				{
					System.out.print(getRing(r,true));
				}
			}
			for (String rotorName : rotors)
			{		
				Rotor r=m_machine.getRotorByKey(rotorName);
				if(!r.isReflector())
				{				
					System.out.print(getRing(r,true));
				}
			}	
			System.out.println("");
			
			for (String rotorName : rotors)
			{		
				Rotor r=m_machine.getRotorByKey(rotorName);
				if(r.isReflector())
				{
					System.out.print(getRing(r,false));
				}
			}
			for (String rotorName : rotors)
			{		
				Rotor r=m_machine.getRotorByKey(rotorName);
				if(!r.isReflector())
				{				
					System.out.print(getRing(r,false));
				}
			}	
		}
		else
		{
			Rotor reflector=m_machine.getReflector();
			System.out.print(getRing(reflector,true));					
			
			numRotors=m_machine.getNumRotors();
			for (int i=0;i<numRotors;i++)
			{
				Rotor rotor=m_machine.getRotor(i+1);
				if(!rotor.getName().equals(reflector.getName()))
				{
					System.out.print(getRing(rotor,true));
				}
			}	
			
			System.out.println("");
					
			System.out.print(getRing(reflector,false));					
			
			numRotors=m_machine.getNumRotors();
			for (int i=0;i<numRotors;i++)
			{
				Rotor rotor=m_machine.getRotor(i+1);
				if(!rotor.getName().equals(reflector.getName()))
				{
					System.out.print(getRing(rotor,false));
				}
			}	
		}
		
		System.out.println("");
	}
		
	private String getPosition(Rotor r,boolean bNumeric)
	{
		String str="";
		
		if(r.hasPositioning())
		{
			String strNumeric=""+r.getPosition();
			if(strNumeric.length()==1)strNumeric="0"+strNumeric;
			String strAlpha=""+Tools.positionToChar(r.getPosition());
			
			str=bNumeric?strNumeric:strAlpha;
		}
		else
		{
			str="-";
		}
		return (str+"        ").substring(0,8);
	}

	private String getRing(Rotor r,boolean bNumeric)
	{
		String str="";
		
		if(r.hasRing())
		{
			String strNumeric=""+r.getRingPosition();
			if(strNumeric.length()==1)strNumeric="0"+strNumeric;
			String strAlpha=""+Tools.positionToChar(r.getRingPosition());
			
			str=bNumeric?strNumeric:strAlpha;
		}
		else
		{
			str="-";
		}
		return (str+"        ").substring(0,8);
	}
		
	private void handleSetPosition(Console console,String rotorName)
	{		
		Rotor rotor=m_machine.getRotorByKey(rotorName);
		
		if(rotor==null)
		{
			System.out.println("This reflector/rotor does not exist!");
			return;
		}
		
		if(!m_machine.rotorInserted(rotorName) && !rotor.isReflector())
		{							
			System.out.println("Can't set the position of a reflector/rotor which is not currently in the machine!");
			return;
		}
		
		
		if(!rotor.hasPositioning())
		{
			System.out.println("This "+(rotor.isReflector()?"reflector":"rotor")+" can't be set to a position manually!");			
		}
		else
		{
			System.out.print("Enter position: ");
			int position=0;
			String strPosition=Tools.getUserInput(console,m_bHideInput).toUpperCase();
			try
			{
				position=Integer.parseInt(strPosition);
				if(position<1 || position>26)
				{
					System.out.println("Invalid position!");
					return;						
				}
			}
			catch(NumberFormatException nfe)
			{
				char p=strPosition.toUpperCase().charAt(0);
				if((strPosition.length()!=1) || (p<'A' || p>'Z'))
				{
					System.out.println("Invalid position!");
					return;
				}		
				position=Tools.charToPosition(p);
			}
			rotor.setPosition(position);
			System.out.println("New position is set!");			
		}		
	}
	
	private void handleSetMultiplePosition(Console console,String[] rotorList)
	{			
		int numCorrect=0;
		int numMovable=m_machine.getNumRotors()+1;
		Rotor reflector=m_machine.getReflector();
				
		if(rotorList.length!=numMovable)
		{
			System.out.println("You entered an incorrect number of positions ("+numMovable+" settings are needed, you entered "+(rotorList.length+" setting(s))!"));
			return;
		}
		
		int offset=0;
		if(reflector!=null)
		{
			offset=1;
			if(!rotorList[0].equals(".")) 
			{
				if(!reflector.hasPositioning()) 
				{
					System.out.println("Can't set position of reflector "+reflector.getName()+" manually!");
				} 
				else
				{
					if(setRotorPosition(reflector,rotorList[0]))
					{
						numCorrect++;
					}
				}
			}
		}
				
		for(int i=0;i<rotorList.length-offset;i++)
		{
			Rotor r=m_machine.getRotor(i+1);
			if(!rotorList[i+offset].equals("."))
			{
				if(!r.hasPositioning()) 
				{
					System.out.println("Can't set position of rotor "+r.getName()+" manually!");
				} 
				else
				{
					if(setRotorPosition(r,rotorList[i+offset]))
					{
						numCorrect++;
					}
				}
			}
		}
		
		if(numCorrect>0)
		{
			System.out.println("Successfully set the position of "+numCorrect+"/"+numMovable+" reflectors/rotors!");
		}
		else
		{
			System.out.println("Failed to set any positions!");
		}
	}
	
	
	private void handleSetMultipleRingSettings(Console console,String[] rotorList)
	{			
		int numCorrect=0;
		int numMovable=m_machine.getNumRotors()+1;
		Rotor reflector=m_machine.getReflector();
				
		if(rotorList.length!=numMovable)
		{
			System.out.println("You entered an incorrect number of ring-settings ("+numMovable+" settings are needed, you entered "+(rotorList.length+" setting(s))!"));
			return;
		}
		
		int offset=0;
		if(reflector!=null)
		{
			offset=1;
			if(!rotorList[0].equals(".")) 
			{
				if(!reflector.hasRing()) 
				{
					System.out.println("Can't set ring-setting of reflector "+reflector.getName()+" manually!");
				} 
				else
				{
					if(setRotorRingSetting(reflector,rotorList[0]))
					{
						numCorrect++;
					}
				}
			}
		}
				
		for(int i=0;i<rotorList.length-offset;i++)
		{
			Rotor r=m_machine.getRotor(i+1);
			if(!rotorList[i+offset].equals("."))
			{
				if(!r.hasRing()) 
				{
					System.out.println("Can't set ring-setting of rotor "+r.getName()+" manually!");
				} 
				else
				{
					if(setRotorRingSetting(r,rotorList[i+offset]))
					{
						numCorrect++;
					}
				}
			}
		}
		
		if(numCorrect>0)
		{
			System.out.println("Successfully set the ring-settings of "+numCorrect+"/"+numMovable+" reflectors/rotors!");
		}
		else
		{
			System.out.println("Failed to set any ring-settings!");
		}
	}
	
	private boolean setRotorPosition(Rotor rotor,String position)
	{
		int iPosition=0;
		String strPosition=position.toUpperCase();
								
		try
		{
			iPosition=Integer.parseInt(strPosition);
			if(iPosition<1 || iPosition>26)
			{
				System.out.println("Invalid position '"+strPosition+"'");
				return false;						
			}
		}
		catch(NumberFormatException nfe)
		{
			char p=strPosition.toUpperCase().charAt(0);
			if((strPosition.length()!=1) || (p<'A' || p>'Z'))
			{
				System.out.println("Invalid position '"+strPosition+"'");
				return false;
			}		
			iPosition=Tools.charToPosition(p);
		}
	
		rotor.setPosition(iPosition);
		return true;		
	}
	
	private void handleSetRingSetting(Console console,String rotorName)
	{		
		Rotor rotor=m_machine.getRotorByKey(rotorName);
		if(rotor==null)
		{
			System.out.println("This reflector/rotor does not exist!");
			return;
		}
		if(!rotor.hasRing())
		{
			System.out.println("This "+(rotor.isReflector()?"reflector":"rotor")+" has no ring-setting!");			
		}
		else
		{
			System.out.print("Enter ring-setting: ");
			int position=0;
			String strPosition=Tools.getUserInput(console,m_bHideInput).toUpperCase();
			try
			{
				position=Integer.parseInt(strPosition);
				if(position<1 || position>26)
				{
					System.out.println("Invalid ring-setting!");
					return;						
				}
			}
			catch(NumberFormatException nfe)
			{
				char p=strPosition.toUpperCase().charAt(0);
				if((strPosition.length()!=1) || (p<'A' || p>'Z'))
				{
					System.out.println("Invalid ring-setting!");
					return;
				}		
				position=Tools.charToPosition(p);					
			}
			rotor.setRingPosition(position);
			System.out.println("New ring-setting is set!");					
		}		
	}
	
	private boolean setRotorRingSetting(Rotor rotor,String position)
	{
		int iPosition=0;
		String strPosition=position.toUpperCase();
		try
		{
			iPosition=Integer.parseInt(strPosition);
			if(iPosition<1 || iPosition>26)
			{
				System.out.println("Invalid ring-setting '"+strPosition+"'");
				return false;						
			}
		}
		catch(NumberFormatException nfe)
		{
			char p=strPosition.toUpperCase().charAt(0);
			if((strPosition.length()!=1) || (p<'A' || p>'Z'))
			{
				System.out.println("Invalid ring-setting '"+strPosition+"'");
				return false;
			}		
			iPosition=Tools.charToPosition(p);
		}
		rotor.setRingPosition(iPosition);
		return true;	
	}
	
	private void handleSetPlugboard(String positions)
	{
		String[] pairs=positions.toUpperCase().split(" ");
		
		Plugboard pb=m_machine.getPlugboard();
		
		pb.backupBoard();
		
		for (String pair : pairs)
		{
			if(pair.length()!=2)
			{
				pb.restoreBoard();
				System.out.println("Plugboard not changed: Invalid pair '"+pair+"'!");
				return;
			}
			
			int a=Tools.charToPosition(pair.charAt(0));
			int b=Tools.charToPosition(pair.charAt(1));
			if(a<1 || a>26 || b<1 || b>26)
			{
				pb.restoreBoard();						
				System.out.println("Plugboard not changed: Invalid pair '"+pair+"'!");
				return;				
			}
			
			if(pb.getNumPairs()==13)
			{
				pb.restoreBoard();
				System.out.println("Plugboard not changed: Number of pairs exceeds the maximum of 13!");
				return;
			}
				
			String result;
			result=pb.addPair(a, b);
			if(result!=null)
			{
				pb.restoreBoard();						
				System.out.println("Plugboard not changed: "+result);
				return;								
			}
		}
		
		m_machine.getUhr().clear();
		for(int i=0;i<pb.getNumPairs();i++)
		{
			m_machine.getUhr().addRed(i,pb.getA(i));
			m_machine.getUhr().addBlack(i,pb.getB(i));
		}
		System.out.println("New connections are made on the plugboard!");
		if(m_machine.getUhrEnabled())
		{
			System.out.println("Uhr enabled: the first 10 pairs are overridden by the Uhr!");
		}
	}
	
	
	private void handleGetPlugboard()
	{		
		Plugboard pb=m_machine.getPlugboard();
		int numPairs=pb.getNumPairs();
		if(numPairs==0)
		{
			System.out.println("No connections made on plugboard!");
			return;
		}
		for(int i=0;i<numPairs;i++)
		{
			System.out.print(Tools.positionToChar(pb.getA(i))+""+Tools.positionToChar(pb.getB(i))+" ");
		}
		System.out.println("");
	}
	
	private void handleClearPlugboard()
	{
		Plugboard pb=m_machine.getPlugboard();
		pb.clear();
		m_machine.getUhr().clear();
		System.out.println("Plugboard is cleared!");
	}
	
	private void handleGetUKWDWiring()
	{
		UKWD ukwd=m_machine.getUKWD();
		
		System.out.println(ukwd.getWiring()+"-> "+(ukwd.isBPNotation()?"(BP (Bletchley Park) notation)":"(DE (Deutsch, as used in German keysheets) notation)"));
	}

	private void handleSetUKWDWiring(String pairs)
	{
		String result=m_machine.getUKWD().generateMapping(pairs);
		
		if(result==null)
		{
			System.out.println("UKWD-D is rewired!");
		} else {
			System.out.println(result);
		}
	}
	
	private void quitMachine(String error)
	{
		System.out.println("Enigma error: "+error+"\n");
		return;		
	}
	
	private void handleGetVersion()
	{
		System.out.println("Ponai Enigma Sim "+Version.ENIGMA_SIM_VERSION);
	}
	protected void handleDisplayConfiguration(String usage)
	{
		System.out.println(m_machine.getDescription());
		if(m_machine.isLuckenfuller())
		{
			System.out.println("PLEASE NOTE!");
			System.out.println("Non-authentic emulation active (Luckenfuller is used)\n");
		}
		System.out.println("--------------");
		System.out.println("Configuration:");
		System.out.println("--------------");
		System.out.println("Usage       : "+usage);		
		System.out.println("Plugboard   : "+(m_machine.hasPlugboard()?"yes":"no"));
		System.out.println("Uhr         : "+(m_machine.hasUhr()?"supported":"no"));
		
		int numReflectors=m_machine.getNumAvailableReflectors();
		
		System.out.println("UKW-D       : "+(m_machine.hasUKWD()?(numReflectors==1?"yes":"supported"):"no"));
		System.out.println("Luckenfuller: "+(m_machine.isLuckenfuller()?"enabled":"disabled"));
		System.out.println("----------------------------------------------------------------------------");
		System.out.println(" Reflectors and options ([rotates=Y/N][has_ring=Y/N][has_position=Y/N]):");
		System.out.println("----------------------------------------------------------------------------");
		
		List<String> rotorNames=m_machine.getRotorNames();
				
		Rotor reflector=null;
		for (String key : rotorNames)
		{
			Rotor rotor=m_machine.getRotorByKey(key);
			if(rotor.isReflector())
			{
				reflector=rotor;
			}
		}

		if(numReflectors==1)
		{		
			if(m_machine.hasUKWD())
			{
				System.out.print("This Enigma is equipped with the Field rewirable UKW-D reflector!");
			}
			else
			{
				System.out.print("This Enigma has only one reflector (UKW) with options: ");
				System.out.print(getRotorOptions(reflector));
			}					
		}
		else
		{
			for (String key : rotorNames)
			{
				Rotor rotor=m_machine.getRotorByKey(key);
				
				if(rotor.isReflector())
				{					
					String str="UKW-"+rotor.getName()+"        ";
					str+="        ";
					str=str.substring(0,8);
					System.out.print(str);
				}
			}
			System.out.println("\n----------------------------------------------------------------------------");
			for (String key : rotorNames)
			{
				Rotor rotor=m_machine.getRotorByKey(key);
				if(rotor.isReflector())
				{
					String str=getRotorOptions(rotor);
					str+="        ";
					str=str.substring(0,8);
					System.out.print(str);
				}
			}				
		}
		System.out.println("\n----------------------------------------------------------------------------\n");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println(" Rotors and options ([rotates=Y/N][has_ring=Y/N][has_position=Y/N]):");
		System.out.println("----------------------------------------------------------------------------");
		
		for (String key : rotorNames)
		{
			Rotor rotor=m_machine.getRotorByKey(key);
			if(!rotor.isReflector())
			{
				String str=rotor.getName();
				str+="        ";
				str=str.substring(0,8);
				System.out.print(str);
			}
		}
		System.out.println("\n----------------------------------------------------------------------------");		
		for (String key : rotorNames)
		{
			Rotor rotor=m_machine.getRotorByKey(key);
			if(!rotor.isReflector())
			{
				String str=getRotorOptions(rotor);
				str+="        ";
				str=str.substring(0,8);
				System.out.print(str);
			}
		}	
		System.out.println("");
	}	
	private String getRotorOptions(Rotor rotor)
	{
		return (rotor.rotates()?"Y":"N")+(rotor.hasRing()?"Y":"N")+(rotor.hasPositioning()?"Y":"N");	
	}	
}