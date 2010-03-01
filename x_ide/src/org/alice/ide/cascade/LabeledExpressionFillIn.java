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
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class LabeledExpressionFillIn< E extends edu.cmu.cs.dennisc.alice.ast.Expression > extends SimpleExpressionFillIn< E > {
	private String text;
	public LabeledExpressionFillIn( E model, String text ) {
		super( model );
		this.text = text;
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		javax.swing.JLabel label = edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( this.text, edu.cmu.cs.dennisc.zoot.font.ZTextPosture.OBLIQUE, edu.cmu.cs.dennisc.zoot.font.ZTextWeight.LIGHT );
		return new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane( super.createMenuProxy(), javax.swing.Box.createHorizontalStrut( 16 ), label );
	}
}
