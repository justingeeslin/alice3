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
package org.alice.ide.preferencesinputpane;

/**
 * @author Dennis Cosgrove
 */
public abstract class PreferenceLabeledPaneProxy< E > extends PreferenceProxy< E > {
	private edu.cmu.cs.dennisc.croquet.swing.LineAxisPane pane;

	public PreferenceLabeledPaneProxy( edu.cmu.cs.dennisc.preference.Preference< E > preference ) {
		super( preference );
	}

	protected void createPane( java.awt.Component control ) {
		this.pane = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane(
				edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( this.getPreference().getKey() + ":" ),
				javax.swing.Box.createHorizontalStrut( 8 ),
				control 
		);
	}
	@Override
	public final java.awt.Component getAWTComponent() {
		assert this.pane != null;
		return this.pane;
	}
}
