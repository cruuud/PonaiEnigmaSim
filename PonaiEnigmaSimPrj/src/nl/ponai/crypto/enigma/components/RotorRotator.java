package nl.ponai.crypto.enigma.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author ponai
 *
 */
public class RotorRotator
{
	private ArrayList<Rotor> m_rotors;
	
	private boolean m_bDoubleStep;
	private boolean m_bLuckenfuller=false;
	
	public RotorRotator(boolean bDoubleStep)
	{
		m_bDoubleStep=bDoubleStep;
		m_rotors=new ArrayList<Rotor>();
	}
	
	public void setLuckenfuller(boolean bLuckenfuller)
	{
		m_bLuckenfuller=bLuckenfuller;
	}
	
	public void addRotor(Rotor rotor)
	{
		m_rotors.add(rotor);
	}
	
	public void doRotation()
	{	
		if(m_rotors.size()==0)return;
	
		//Rotation of rotors is a bit tricky, as it does not work like a normal odometer such as in a car
		//If you consider each segment of a car odometer to be a rotor, then you could say that the right-most
		//segment steps from 0 to 9 continuously and each time a segment reaches the 9, the segment to the left
		//of it makes a step on the next turn (when the segment to the right of goes from 9 to 0).
		//On an Enigma it does not work like that.
		//There are two mechanisms used on Enigma machines, one uses ratchets, pawls and notches and the other one
		//uses gears. Most of the Enigma machines use the ratchets, pawls and notches mechanism, but only the so called
		//Enigma G variants and the Zählwerk Enigma use the gear variant.
		//When reading descriptions of the rotation intrinsics, it has always puzzled me why it's explained in such
		//a difficult way and because of that, I also had a hard time understanding it.
		//People talk about the 'double-step' effect, which in my opinion makes things complicated.
		//How I see it in general, is that there is a technical mechanism involved, which has an effect on the way rotors move.
		//But, basically, this is just a logical consequence of the mechanism and should not be treated as a specific
		//side-effect (double-stepping).
		//Basically, it's quite easy to understand, but you need to differ between the two variants, which makes programming
		//the mechanism much easier.
		//--------------------------------------------------------------------------------------------------------------
		//Mechanism with gears (no double-step)
		//--------------------------------------------------------------------------------------------------------------
		//This is the easiest to understand, basically it works like an odometer, with the difference that instead of
		//the turnover point being from 9 to 0 (so the next segment turns), there are multiple turnover points.
		//Simple as that. So, to implement this, you do the following:
		//WHILE we still have rotors to examine (start with rightmost rotor)
		//  CAN rotor rotate?
		//   IF yes --> continue
		//   IF no --> jump out of loop
		//  IS turnover point reached
		//   IF yes --> rotate current rotor and examine the next rotor (to the left of it)
		//   IF no  --> rotate current rotor and jump out of loop
		//END WHILE
		//--------------------------------------------------------------------------------------------------------------
		//Mechanism with ratchets, pawls and notches (with double-stepping effect, yuck, forget I said that ;-))
		//--------------------------------------------------------------------------------------------------------------
		//This one is a bit more difficult to understand, but actually not that difficult AND we can make a generic
		//implementation, which works for any number of rotors.
		//Basically, we need to know how the mechanism works. So, on each rotor we have one or more notches.
		//There are pawls that fall between two given rotors, one half of the pawl in the notch of the rotor to the right
		//and the other half of the pawl falls into the ratched on the left-side of the other rotor.
		//This means, that in case we have reached a notch and the pawl falls into it during one step of a rotor, the
		//NEXT step, that rotor will make another step AND the rotor to the left of it will do that too. It's really
		//as simple as that.
		//And now for the generic implementation:
		//MARK rightmost rotor to be rotated (it rotates with every keypress)
		//FOR rotor right TO rotor left (minus 1)
		//  CAN rotor rotate?
		//   IF yes --> continue
		//   IF no  --> take next rotor
		//  IS turnover point reached (pawl falls into notch) AND left rotor can rotate too (VERY important ;-))
		//    MARK rotor to be rotated
		//    MARK rotor to the left of it to be rotated TOO
		//NEXT rotor  
		//That simple?? Yes, it's that simple.
		
		int index=0;
		if(!m_bDoubleStep) {
			while(index<m_rotors.size()) {
				Rotor rotor=m_rotors.get(index);
				if(rotor.rotates()) {
					boolean bKnockOn=rotor.isKnockOn(m_bLuckenfuller);
					rotor.rotate();
					if(bKnockOn) {
						index++;
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} else {
			Set<Rotor> rotateThemSet = new TreeSet<Rotor>();
			Rotor rightRotor=m_rotors.get(0);
			if(rightRotor.rotates()) {
				rotateThemSet.add(rightRotor);
			}
			for(int i=0;i<m_rotors.size()-1;i++) {
				Rotor rotor=m_rotors.get(i);
				if(rotor.rotates()) {
					if(rotor.isKnockOn(m_bLuckenfuller)) {
						Rotor left=m_rotors.get(i+1);
						if(left.rotates()) {
							rotateThemSet.add(rotor);
							rotateThemSet.add(left);
						}
					}
				}
			}
			
			//We have marked all rotors to be rotated, so now rotate them.
			Iterator<Rotor> rotors=rotateThemSet.iterator();
			while(rotors.hasNext()) {
				rotors.next().rotate();
			}
		}
	}
}
