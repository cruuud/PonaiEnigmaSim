package nl.ponai.crypto.enigma.config;

import nl.ponai.crypto.enigma.generic.EnigmaSimulator;

/**
 * 
 * @author ponai
 *
 */
public class MachineConfiguration
{
	private String m_configName;
	private String m_description;
	private EnigmaSimulator m_simulator;
	
	public MachineConfiguration(String configName,String description,EnigmaSimulator simulator)
	{
		m_configName=configName;
		m_description=description;
		m_simulator=simulator;
		
	}	
	public EnigmaSimulator getSimulator()
	{
		return m_simulator;
	}
	
	public String getDescription()
	{
		return m_description;
	}
	
	public String getConfigName()
	{
		return m_configName;
	}
}
