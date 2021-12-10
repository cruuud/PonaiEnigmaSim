package nl.ponai.crypto.enigma.components;

/**
 * 
 * @author ponai
 *
 */
public class Uhr
{	
	private static int[] redToBlack=new int[]{6,31,4,29,18,39,16,25,30,23,28,1,38,11,36,37,26,27,24,21,14,3,12,17,2,7,0,33,10,35,8,5,22,19,20,13,34,15,32,9};
	private static int[] blackToRed=new int[]{26,11,24,21,2,31,0,25,30,39,28,13,22,35,20,37,6,23,4,33,34,19,32,9,18,7,16,17,10,3,8,1,38,27,36,29,14,15,12,5};
	
	private static int[] redPositions=new int[]{0,1,2,3,4,5,6,7,8,9};
	private static int[] blackPositions=new int[]{1,4,7,9,6,3,0,2,5,8};

	private int[] m_redPlugs;
	private int[] m_blackPlugs;
	private int m_uhrPos=0;
	
	public Uhr()
	{
		clear();
		m_uhrPos=0;
	}
	
	public void setUhrPosition(int position)
	{
		if(position>39)position=39;
		if(position<0)position=0;
		m_uhrPos=position;
	}	
	
	public int getUhrPosition()
	{
		return m_uhrPos;
	}
		
	public boolean addRed(int position,int value)
	{
		if(position<0 || position>9)return false;
		if(m_redPlugs[position]!=0)return false;
		
		for(int i=0;i<m_redPlugs.length;i++)
		{
			if(m_redPlugs[position]==value)return false;
			if(m_blackPlugs[position]==value)return false;
		}
		m_redPlugs[position]=value;
		return true;
	}

	public boolean addBlack(int position,int value)
	{
		if(position<0 || position>9)return false;
		if(m_blackPlugs[position]!=0)return false;
		for(int i=0;i<m_blackPlugs.length;i++)
		{
			if(m_redPlugs[position]==value)return false;
			if(m_blackPlugs[position]==value)return false;
		}		
		m_blackPlugs[position]=value;
		return true;
	}
	
	public void clear()
	{
		m_redPlugs=new int[10];
		m_blackPlugs=new int[10];		
	}
	
	public int getUhred(boolean bInput,int value)
	{		
		int pos=0;
		boolean bRed=false;
		boolean bBlack=false;
		for(int i=0;i<m_redPlugs.length;i++)
		{
			if(m_redPlugs[i]==value)
			{
				bRed=true;
				pos=i;
				break;
			}
			if(m_blackPlugs[i]==value)
			{
				bBlack=true;
				pos=i;
				break;
			}
		}
		
		if(bRed)
		{
			int index=redPositions[pos];
			int connectionRed=index*4+(bInput?0:2)+m_uhrPos;
			connectionRed%=40;
			int connectionBlack=redToBlack[connectionRed];
			connectionBlack-=m_uhrPos;
			if(connectionBlack<0)connectionBlack+=40;								
			for(int i=0;i<blackPositions.length;i++)
			{
				int testPos=i*4+(bInput?2:0);
				if(testPos==connectionBlack)
				{
					for(int j=0;j<blackPositions.length;j++)
					{
						if(blackPositions[j]==i)return m_blackPlugs[j];
					}
				}
			}			
			return value;
		}
		else if(bBlack)
		{
			int index=blackPositions[pos];
			int connectionBlack=index*4+(bInput?0:2)+m_uhrPos;
			connectionBlack%=40;
			int connectionRed=blackToRed[connectionBlack];
			connectionRed-=m_uhrPos;			
			if(connectionRed<0)connectionRed+=40;
			for(int i=0;i<redPositions.length;i++)
			{
				int testPos=i*4+(bInput?2:0);
				if(testPos==connectionRed)
				{
					for(int j=0;j<redPositions.length;j++)
					{
						if(redPositions[j]==i)return m_redPlugs[j];
					}					
				}
				
			}			
			return value;			
		}
		return value;
	}	
}
