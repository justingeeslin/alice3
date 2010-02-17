/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.media;

/**
 * @author Dennis Cosgrove
 */
public abstract class MediaFactory {
	public static final double DEFAULT_VOLUME = 1.0;
	public static final double DEFAULT_START_TIME = 0.0;
	public static final double DEFAULT_STOP_TIME = Double.NaN;
	public abstract Player createPlayer( org.alice.virtualmachine.resources.AudioResource audioResource, double volume, double startTime, double stopTime );
	public Player createPlayer( org.alice.virtualmachine.resources.AudioResource audioResource, double volume, double startTime ) {
		return createPlayer( audioResource, volume, startTime, DEFAULT_STOP_TIME );
	}
	public Player createPlayer( org.alice.virtualmachine.resources.AudioResource audioResource, double volume ) {
		return createPlayer( audioResource, volume, DEFAULT_START_TIME );
	}
	public Player createPlayer( org.alice.virtualmachine.resources.AudioResource audioResource ) {
		return createPlayer( audioResource, DEFAULT_VOLUME );
	}
}
