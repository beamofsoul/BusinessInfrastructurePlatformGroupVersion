package com.beamofsoul.bip.management.util;

@FunctionalInterface
public interface Callback {
	
	public void doCallback(Object... objects); 
}
