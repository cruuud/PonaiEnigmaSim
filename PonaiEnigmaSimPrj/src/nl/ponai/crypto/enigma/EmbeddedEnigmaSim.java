package nl.ponai.crypto.enigma;

import java.io.Console;

import nl.ponai.crypto.enigma.config.MachineConfiguration;
import nl.ponai.crypto.enigma.config.Machines;
import nl.ponai.crypto.enigma.generic.EnigmaMachine;
import nl.ponai.crypto.enigma.generic.EnigmaSimulator;
import nl.ponai.crypto.enigma.notifiers.IMachineStatus;

/**
 * 
 * @author ponai
 *
 */
public class EmbeddedEnigmaSim implements Runnable
{
	private EnigmaMachine m_machine;
	private Thread m_thread;
	private boolean m_bRunning;
	private IMachineStatus m_machineNotifier;
	private EnigmaSimulator m_simulator;
	
	public EmbeddedEnigmaSim()
	{			
	}
	
	public void startEmbeddedSim(IMachineStatus notifier)
	{
		m_thread=new Thread(this);
		m_machineNotifier=notifier;
		m_bRunning=true;		
		m_thread.start();
	}
	
	public void stopEmbeddedSim() throws InterruptedException
	{
		m_bRunning=false;
		m_thread.join(2000);
	}

	public EnigmaMachine getMachine()
	{
		return m_machine;
	}
	
	public EnigmaSimulator getSimulator()
	{
		return m_simulator;
	}
	
	public void setSimulator(EnigmaSimulator simulator)
	{
		m_simulator=simulator;
	}
	
	public void run()
	{
		Console console=System.console();
		if(console==null)
		{
			System.out.println("ERROR - No console!");
			return;
		}	
		
		while(m_bRunning)
		{
			System.out.println("The following Enigma machines are available:");
			for(int i=0;i<Machines.MACHINES.length;i++)
			{
				MachineConfiguration config=Machines.MACHINES[i];
				System.out.println("  "+(config.getConfigName()+"      ").substring(0,6)+" - "+config.getDescription());
			}
			System.out.print("\nWhich machine do you want to load: ");
			String machine=console.readLine();
			if(machine!=null)
			{
				machine=machine.trim().toUpperCase();
				if(machine.equals("QUIT"))
				{
					System.out.println("\nQuitting application ...");
					System.exit(0);
				}
			}
			else
			{
				m_bRunning=false;
				break;
			}
				
			m_simulator=null;
			
			if(machine!=null)
			{
				for(int i=0;i<Machines.MACHINES.length;i++)
				{
					MachineConfiguration config=Machines.MACHINES[i];
					if(config.getConfigName().toUpperCase().equals(machine))
					{
						m_simulator=config.getSimulator();
					}
				}
				if(m_simulator==null)
				{
					System.out.println("\nUnsupported Enigma machine '"+machine+"'!\n");
					continue;
				}				
			}
			if(m_simulator!=null)
			{
				this.m_machine=m_simulator.getMachine();
				this.m_machine.setMachineStatusNotifier(m_machineNotifier);				
				m_simulator.configureMachine();
				m_simulator.runMachine(false);
			}
		}
	}		
}
