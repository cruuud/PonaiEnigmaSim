package nl.ponai.crypto.enigma;

import java.io.File;

import nl.ponai.crypto.enigma.config.MachineConfiguration;
import nl.ponai.crypto.enigma.config.Machines;
import nl.ponai.crypto.enigma.generic.EnigmaMachine;
import nl.ponai.crypto.enigma.generic.EnigmaSimulator;
import nl.ponai.crypto.enigma.machines.CustomEnigma;

/**
 * ----------------
 * Ponai Enigma Sim
 * ----------------
 * 
 * This application was created by someone with a passion for cryptography that can still be grasped by the common
 * human mind.
 * 
 * All Enigma information necessary to create this application was retrieved from the following sources and I want to
 * thank them all for helping me out:
 * 
 * http://www.cryptomuseum.com/ (thanks to Paul Reuvers for answering my questions)
 * Daniel Palloks -  Helped me to fix my Uhr implementation bug! :-)
 *    His awesome Javascript emulator can be found here: https://people.physik.hu-berlin.de/~palloks/js/enigma/index_en.html
 * David Hamer - Helping me out with questions regarding the stepping mechanism of the rotors   
 * 
 * You can contact me at ponai@ziggo.nl for any questions/remarks/bug etc.
 * Be aware that I tried my best to emulate everything as precise as I could, based on the information I could find,
 * but this application will definitely contain bugs and you will come across situations where a message deciphered by this
 * emulator could not be deciphered by some other emulator and vice versa.
 * I gave this application to the general public, so anyone can fiddle around with it. You may copy/paste/use anything you
 * find in this application, better yet: you can do anything you want with it.
 * I just hope that you will like it.
 * If you want to use anything from this application for commercial purposes, I will be last one to stop you, but I will not like you.
 * 
 * @author ponai
 *
 */
public class PonaiEnigmaSim
{	
	public static void main(String[] args)
	{
		if(args.length==0)
		{
			usage();
			System.exit(-1);
		}
		else
		{
			EnigmaSimulator simulator=null;
			String machine=args[0].trim().toUpperCase();
			
			for(int i=0;i<Machines.MACHINES.length;i++)
			{
				MachineConfiguration config=Machines.MACHINES[i];
				if(config.getConfigName().toUpperCase().equals(machine))
				{
					simulator=config.getSimulator();
				}
			}
			if(simulator==null)
			{
				simulator=new CustomEnigma(machine);
			}
			
			simulator.configureMachine();
			if(args.length>1)
			{
				String config=args[1];
				EnigmaMachine theMachine=simulator.getMachine();
				String result=theMachine.parseConfiguration(config);
				if(result!=null)
				{
					System.out.println(result);
				}
				else
				{
					if(simulator.isEquipped())
					{
						theMachine.closeMachine();
						theMachine.prepareForProcessing();
						System.out.println("Machine config loaded, machine is fully equipped and ready to encipher/decipher messages!");
						if(args.length>2) {
							String file=args[2];
							System.out.print("Enciphering/deciphering file '"+file+"' ...");
							simulator.handleEnteredFile(new File(file));
							System.exit(0);
						}
					}			
					else
					{
						System.out.println("Machine config is not complete, you need to manually add the necessary components!");
					}
				}
			}

			simulator.runMachine(true);			

		}
	}
	
	private static void usage()
	{
		System.out.println("Ponai Enigma Sim "+Version.ENIGMA_SIM_VERSION);
		System.out.print("-----------------");
		for(int i=0;i<Version.ENIGMA_SIM_VERSION.length();i++)
		{
			System.out.print("-");
		}
		System.out.println("");
		System.out.println("Usage: PonaiEnigmaSim <machine> \"[config]\" \"[file_to_encipher/decipher]\n");
		System.out.println(" Machine is one of the following supported machines:");		
		for(int i=0;i<Machines.MACHINES.length;i++)
		{
			MachineConfiguration config=Machines.MACHINES[i];			
			System.out.println("  "+(config.getConfigName()+"      ").substring(0,6)+" - "+config.getDescription());
		}
		System.out.println("  [X]    - Custom-designed Enigma");
		System.out.println("\n Config is optional (type between double-quotes!) and can be used to load the machine directly in a given state:");
		System.out.println("  config format: reflector|rotor(s)|ring-setting(s)|position(s)|plugboard pair(s)|UKW-D wiring|UKWD-D wiring notation");
		System.out.println("  example: B|I II III|1 2 3|4 5 6|AB CD EF|AK CT DV EP FN GL HM IJ QW RY SX UZ|BP\n");
		System.out.println("  Just leave fields between the | characters empty to skip that specific configuration item");
		System.out.println("  If given, the UKWD-D wiring notation determines how your interpreted mapping pairs are interpreted.");
		System.out.println("  If not given, the default setting DE (Deutsch) is used, which is the notation used on the German keysheets.");
		System.out.println("  As the UKW-D wiring notation you can use the values BP (Bletchley Park), or DE (Deutsch, as used in German keysheets).");
		System.out.println("\nIf you use the last parameter too (file_to_encipher/decipher), you can quickly encipher/decipher a file");
		System.out.println("with the given configuration, without any user-interaction in the simulator.");
		System.out.println("After enciphering/deciphering the given file, the program will exit. This functionality is quite handy");
		System.out.println("for enciphering/deciphering files in scripts where you don't want any user-interaction.\n");
		System.out.println("----------------");
		System.out.println("The [X] machine:");
		System.out.println("----------------");
		System.out.println("You can create your own Enigma machine, using any number of rotors, but of course only one reflector.");
		System.out.println("To use such a 'custom' Enigma, instead of the machine name, you type the full path to a file containing");
		System.out.println("the configuration of the machine. The config and file_to_encipher/decipher parameters also work");
		System.out.println("with your custom machine.");
		System.out.println("The configuration of your machine will be validated to make sure an actual mechanical working machine");
		System.out.println("complying to your configuration could potentially be made in real-life! This means that you can't construct");
		System.out.println("a machine, which can't be physically constructed using the existing Enigma mechanisms.");
		System.out.println("Type the full path to a non-existing file and the file will be created containing a sample configuration.");
		
	}

}
