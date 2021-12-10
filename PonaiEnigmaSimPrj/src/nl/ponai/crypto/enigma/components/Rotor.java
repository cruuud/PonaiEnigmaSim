package nl.ponai.crypto.enigma.components;

import java.util.ArrayList;

import nl.ponai.crypto.enigma.notifiers.IPositionUpdater;
import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class Rotor implements Comparable<Rotor>
{
	private int m_position;
	private int m_ringSetting;	
	private boolean m_bRotates;	
	
	private ArrayList<Integer> m_knockPositions,m_luckenfullerPositions;
	private String m_luckenfullerNotches="";
	private String m_luckenfullerTurnovers="";
	private ArrayList<Integer> m_inputs;
	private ArrayList<Integer> m_outputs;
	
	private boolean m_bHasRing=false;
	private boolean m_bHasPositioning=false;
	private String m_name="DEFAULT";
	private boolean m_bIsReflector=false;
	private boolean m_bIsUKWD=false;
	private IPositionUpdater m_updater;
	
	public Rotor(boolean bRotates,String inputs,String outputs)
	{		
		m_knockPositions=new ArrayList<Integer>();
		m_luckenfullerPositions=new ArrayList<Integer>();
		setWiring(inputs, outputs);
		m_bRotates=bRotates;
		m_position=1;
		m_ringSetting=1;
	}
	
	public void setPositionUpdater(IPositionUpdater updater)
	{
		m_updater=updater;
	}
	
	public boolean rotates()
	{
		return m_bRotates;
	}
	
	public boolean isReflector()
	{
		return m_bIsReflector;
	}
	
	public void setIsReflector(boolean bIsReflector)
	{
		m_bIsReflector=bIsReflector;
	}
					
	public String getName()
	{
		return m_name;
	}
	
	public void setName(String name)
	{
		m_name=name;
	}
	
	public boolean hasRing()
	{
		return m_bHasRing;
	}
	
	public boolean hasPositioning()
	{
		return m_bHasPositioning;
	}
	
	public void setHasRing(boolean bHasRing)
	{
		m_bHasRing=bHasRing;
	}
	
	public void setHasPositioning(boolean bHasPositioning)
	{
		m_bHasPositioning=bHasPositioning;		
	}
	
	public void setWiring(String inputs,String outputs)
	{
		if(inputs==null || outputs==null || inputs.length()!=outputs.length())
		{
			m_inputs=null;
			m_outputs=null;
			return;
		}
		
		m_inputs=new ArrayList<Integer>();
		m_outputs=new ArrayList<Integer>();
		
		for(int i=0;i<inputs.length();i++)
		{
			m_inputs.add(Tools.charToPosition(inputs.charAt(i)));
			m_outputs.add(Tools.charToPosition(outputs.charAt(i)));
		}
	}
	
	public void setWiringInput(String inputs)
	{
		if(inputs==null)
		{
			m_inputs=null;				
			return;
		}
		
		m_inputs=new ArrayList<Integer>();		
		
		for(int i=0;i<inputs.length();i++)
		{
			m_inputs.add(Tools.charToPosition(inputs.charAt(i)));			
		}
	}
		
	public boolean isUKWD()
	{
		return m_bIsUKWD;
	}
	
	public void setIsUKWD(boolean bIsUKWD)
	{
		m_bIsUKWD=bIsUKWD;		
	}
	
	public void setWiringOutput(String outputs)
	{
		if(outputs==null)
		{
			m_outputs=null;				
			return;
		}
		
		m_outputs=new ArrayList<Integer>();		
		
		for(int i=0;i<outputs.length();i++)
		{
			m_outputs.add(Tools.charToPosition(outputs.charAt(i)));			
		}
	}
	
	public int getOutput(int input)
	{
		if(m_outputs==null)return input;		
		return m_outputs.get(input-1);
	}

	public int getInput(int output)
	{		
		if(m_inputs==null || m_outputs==null)
		{
			return output;
		}
		
		for(int i=0;i<m_outputs.size();i++)
		{
			int pos=m_outputs.get(i);
			if(pos==output)return i+1;
		}				
		return output;
	}
	
	public void setLuckenfullerNotches(String notches)
	{
		m_luckenfullerNotches=notches;
		m_luckenfullerTurnovers="";
		m_luckenfullerPositions=new ArrayList<Integer>();
		for(int i=0;i<notches.length();i++)
		{
			int pos=Tools.charToPosition(notches.charAt(i));
			pos-=8;
			if(pos<1)pos+=26;
			m_luckenfullerTurnovers+=Tools.positionToChar(pos);
			
			m_luckenfullerPositions.add(pos);
		}
	}
	
	public String getLuckenfullerConfig()
	{
		if(m_luckenfullerPositions.size()>0)
		{
			return "Notches: "+m_luckenfullerNotches+" (Turnovers: "+m_luckenfullerTurnovers+")";
		}
		else
		{
			return "No notches set!";
		}
	}
	
	
	public void addKnockOn(int knockPosition)
	{
		if(!m_knockPositions.contains(knockPosition))m_knockPositions.add(knockPosition);
	}
	
	public void setRingPosition(int newPosition)
	{
		m_ringSetting=newPosition;
	}
	
	public int getRingPosition()
	{
		return m_ringSetting;
	}	
	
	public void setPosition(int position)
	{
		m_position=position;		
		if(m_updater!=null)m_updater.positionUpdate(m_position);
	}
	
	public int getPosition()
	{
		return m_position;
	}
	
	public boolean isKnockOn(boolean bLuckenfuller)
	{
		if(bLuckenfuller)
		{
			for(int i=0;i<m_luckenfullerPositions.size();i++)
			{
				if(m_position==m_luckenfullerPositions.get(i))return true;
			}			
		}
		else
		{
			for(int i=0;i<m_knockPositions.size();i++)
			{
				if(m_position==m_knockPositions.get(i))return true;
			}			
		}
		return false;
	}
	
	public void rotate()
	{				
		if(!m_bRotates)return;	
		m_position++;		
		if(m_position>26)m_position=1;
		if(m_updater!=null)m_updater.positionUpdate(m_position);
	}

	@Override
	public int compareTo(Rotor o) {
		if(this.getName().equals(o.getName())) {
			return 0;
		} else {
			return 1;
		}
	}	
}
