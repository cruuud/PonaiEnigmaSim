package nl.ponai.crypto.enigma.components;

import java.util.ArrayList;

/**
 * 
 * @author ponai
 *
 */
public class Plugboard
{
	public class Pair
	{
		public int a,b;
	}
	
	private ArrayList<Pair> m_pairs,m_pairsBackup;
	
	public Plugboard()
	{
		m_pairs=new ArrayList<Plugboard.Pair>();
	}
	
	public String addPair(int a,int b)
	{
		Pair pair=new Pair();
		pair.a=a;
		pair.b=b;
		
		for(int i=0;i<m_pairs.size();i++)
		{
			Pair p=m_pairs.get(i);
			if(a==b)
			{
				return "You can't wire a plug to itself!";
			}
			if(p.a==a || p.a==b || p.b==a || p.b==b || a==b)
			{
				return "One of the plugs is already paired!";
			}
		}
		m_pairs.add(pair);
		return null;
	}
	
	public int getTo(int from)
	{
		for(int i=0;i<m_pairs.size();i++)
		{
			Pair pair=m_pairs.get(i);
			if(pair.a==from)
			{
				return pair.b;
			}
			else if(pair.b==from)
			{
				return pair.a;
			}
		}
		return from;
	}
	
	public int getNumPairs()
	{
		return m_pairs.size();
	}
	
	public int getA(int index)
	{
		return m_pairs.get(index).a;
	}

	public int getB(int index)
	{
		return m_pairs.get(index).b;
	}
		
	public void clear()
	{
		m_pairs=new ArrayList<Pair>();
	}
	
	public void backupBoard()
	{
		m_pairsBackup=new ArrayList<Plugboard.Pair>();
		for(int i=0;i<m_pairs.size();i++)
		{
			Pair newPair=new Pair();
			Pair p=m_pairs.get(i);
			newPair.a=p.a;
			newPair.b=p.b;
			m_pairsBackup.add(newPair);
		}
	}
	
	public void restoreBoard()
	{
		m_pairs=new ArrayList<Plugboard.Pair>();
		for(int i=0;i<m_pairsBackup.size();i++)
		{
			Pair newPair=new Pair();
			Pair p=m_pairsBackup.get(i);
			newPair.a=p.a;
			newPair.b=p.b;
			m_pairs.add(newPair);
		}
	}
}
