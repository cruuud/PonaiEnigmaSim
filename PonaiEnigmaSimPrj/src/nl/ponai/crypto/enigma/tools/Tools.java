package nl.ponai.crypto.enigma.tools;

import java.io.Console;

/**
 * 
 * @author ponai
 *
 */
public class Tools
{
	public static final String DEFAULT_ALPHABET="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static int charToPosition(char ch)
	{
		return ch-'A'+1;
	}
	
	public static char positionToChar(int position)
	{
		return (char)(position-1+'A');
	}
	
	public static String getUserInput(Console console,boolean bHide)
	{
		if(bHide)
		{
			return new String(console.readPassword());
		}
		else
		{
			return console.readLine();
		}
	}
}
