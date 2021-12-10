package nl.ponai.crypto.enigma.components;

import nl.ponai.crypto.enigma.tools.Tools;

/**
 * 
 * @author ponai
 *
 */
public class UKWD
{	                                            
	private static final String bpAlphabet   ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String deAlphabet   ="AYZXWVUTSRQPONJMLKIHGFEDCB";
		
	private String m_mapping;
	private Rotor m_rotor;
	private boolean m_bUseBPNotation;
	
	public UKWD()
	{					
		String alphabet=Tools.DEFAULT_ALPHABET;
		m_rotor=new Rotor(false,alphabet,alphabet);		
		m_rotor.setHasPositioning(false);
		m_rotor.setHasRing(false);
		m_rotor.setIsReflector(true);		
		m_rotor.setName("D");
		m_rotor.setIsUKWD(true);
		m_bUseBPNotation=false;
		reset();
	}
		
	public Rotor getRotor()
	{		
		return m_rotor;
	}
	
	public void reset()
	{
		m_mapping="KOTVPNLMJIAGHFBEWYXCZDQSRU";
		if(m_rotor!=null)m_rotor.setWiringOutput(m_mapping);
	}
	
	public void setDENotation(boolean bIsDE) {
		m_bUseBPNotation=!bIsDE;
	}
	public void setBPNotation(boolean bIsBP) {
		m_bUseBPNotation=bIsBP;
	}
	
	public boolean isBPNotation() {
		return m_bUseBPNotation;
	}
	
	public static String generateRandomWiring(boolean isBPNotation)
	{
		char[] available=(isBPNotation?"ACDEFGHIJKLMNPQRSTUVWXYZ":"ABCDEFGHIKLMNOPQRSTUVWXZ").toCharArray();
		
		String pairs="";
		int numPairs=0;
		while(numPairs!=12)
		{
			int index1=(int)(Math.random()*24);
			int index2=(int)(Math.random()*24);
			while(available[index1]==' ' || available[index2]==' ' || index1==index2)
			{
				index1=(int)(Math.random()*24);
				index2=(int)(Math.random()*24);				
			}
			
			pairs+=available[index1];
			pairs+=available[index2];
			
			available[index1]=' ';
			available[index2]=' ';
			
			numPairs++;
			if(numPairs!=12)
			{
				pairs+=" ";
			}
		}
		return pairs;		
	}
	
	public String getWiring()
	{
		String pairs="";
		char[] result=new char[26];
		
		char fixedLeft='J';
		char fixedRight='Y';
		if(isBPNotation()) {
			fixedLeft='B';
			fixedRight='O';
		}
		
		for(int i=0;i<m_mapping.length();i++)
		{				
			char cLeft=Tools.DEFAULT_ALPHABET.charAt(i);				
			char cRight=m_mapping.charAt(i);				
			
			int indexLeft=Tools.charToPosition(cLeft)-1;
			int indexRight=Tools.charToPosition(cRight)-1;
			
			if(result[indexLeft]==0 && result[indexRight]==0)
			{
				result[indexLeft]=cRight;
				result[indexRight]=cLeft;	
				
				char convertedLeft=(isBPNotation()?bpAlphabet:deAlphabet).charAt(indexLeft);
				char convertedRight=(isBPNotation()?bpAlphabet:deAlphabet).charAt(indexRight);
				
				if(convertedLeft!=fixedLeft && convertedLeft!=fixedRight && convertedRight!=fixedRight && convertedRight!=fixedLeft) {
					pairs+=convertedLeft+""+convertedRight+" ";
				}
			}
		}
		pairs+=fixedLeft+""+fixedRight+" ";
		return pairs;
	}
	
	public String getMapping()
	{
		return m_mapping;
	}
			
	public String generateMapping(String pairs)
	{
		if(pairs==null)return "No pairs given!";
		
		String[] pairsSplitted=pairs.toUpperCase().split(" ");
		if(pairsSplitted.length!=12)
		{
			return "The number of pairs given to wire the UKW-D is incorrect (12 are needed, you gave "+pairsSplitted.length+")";			
		}
		
		char[] table=new char[26];
		
		table[1]='O';
		table[14]='B';
		
		char fixedLeft='J';
		char fixedRight='Y';
		if(isBPNotation()) {
			fixedLeft='B';
			fixedRight='O';
		}
		
		for(int i=0;i<pairsSplitted.length;i++)
		{
			String pair=pairsSplitted[i];
			if(pair.length()!=2)
			{
				return "Invalid pair '"+pair+"'";				
			}
			
			char left=pair.charAt(0);
			char right=pair.charAt(1);
			
			if(left==fixedLeft || left == fixedRight || right==fixedLeft || right==fixedRight)
			{
				return "You can't rewire the " + fixedLeft + " and " + fixedRight+" in the " + (isBPNotation()?"BP":"DE")+ " notation!";							
			}
			
			if(left==right)
			{
				return "You can't wire a plug to itself!"; 
			}
			
			int indexLeft=(isBPNotation()?bpAlphabet:deAlphabet).indexOf(left);
			int indexRight=(isBPNotation()?bpAlphabet:deAlphabet).indexOf(right);
			
			if(table[indexRight]!=0 || table[indexLeft]!=0)
			{
				return "The pair/items in the pair '"+pair+"' is/are already wired!";				
			}
			
			table[indexRight]=Tools.positionToChar(indexLeft+1);
			table[indexLeft]=Tools.positionToChar(indexRight+1);			
		}
				
		m_mapping=new String(table);	
		m_rotor.setWiringOutput(m_mapping);		
		
		return null;
	}	
}
