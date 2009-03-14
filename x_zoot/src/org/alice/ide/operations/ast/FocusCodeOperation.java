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
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class FocusCodeOperation extends org.alice.ide.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.AbstractCode nextCode;
	private edu.cmu.cs.dennisc.alice.ast.AbstractCode prevCode;
	public FocusCodeOperation( edu.cmu.cs.dennisc.alice.ast.AbstractCode nextCode ) {
		this.nextCode = nextCode;
		String name;
		if( nextCode instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice ) {
			name = "edit constructor";
		} else {
			name = "edit";
		}
		this.putValue( javax.swing.Action.NAME, name );
	}
	public void perform( zoot.ActionContext actionContext ) {
		this.prevCode = getIDE().getFocusedCode();
		this.redo();
		actionContext.commit();
	}
	public void redo() {
		getIDE().setFocusedCode( this.nextCode );
	}
	public void undo() {
		getIDE().setFocusedCode( this.prevCode );
	}
}
