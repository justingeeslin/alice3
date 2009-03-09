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
package edu.cmu.cs.dennisc.alice.ide.operations;

/**
 * @author Dennis Cosgrove
 */
public class RenameLocalDeclarationOperation extends edu.cmu.cs.dennisc.alice.ide.AbstractUndoableOperation {
	private edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice localDeclaredInAlice;
	private String prevValue;
	private String nextValue;
	public RenameLocalDeclarationOperation( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice localDeclaredInAlice ) {
		this.localDeclaredInAlice = localDeclaredInAlice;
		this.putValue( javax.swing.Action.NAME, "rename..." );
	}
	public edu.cmu.cs.dennisc.alice.ide.Operation.PreparationResult prepare( java.util.EventObject e, edu.cmu.cs.dennisc.alice.ide.Operation.PreparationObserver observer ) {
		//todo
		this.nextValue = javax.swing.JOptionPane.showInputDialog( "name" );
		if( nextValue != null && nextValue.length() > 0 ) {
			return edu.cmu.cs.dennisc.alice.ide.Operation.PreparationResult.PERFORM;
		} else {
			return edu.cmu.cs.dennisc.alice.ide.Operation.PreparationResult.CANCEL;
		}
	}
	public void perform() {
		this.prevValue = this.localDeclaredInAlice.name.getValue();
		this.redo();
	}
	public void redo() {
		this.localDeclaredInAlice.name.setValue( this.nextValue );
	}
	public void undo() {
		this.localDeclaredInAlice.name.setValue( this.prevValue );
	}
}
