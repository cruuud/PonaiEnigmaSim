package nl.ponai.crypto.enigma.notifiers;

import java.util.ArrayList;

import nl.ponai.crypto.enigma.components.Rotor;

public interface IMachineStatus 
{
	public abstract void machineOpened();
	public abstract void machineClosed(ArrayList<Rotor> movableRotors);
}
