/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class EditTypeOperation extends edu.cmu.cs.dennisc.croquet.InputDialogOperation<org.alice.ide.editorstabbedpane.EditTypePanel> {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;
	public EditTypeOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "027be381-1467-4ca4-82c1-67f908dc0297" ) );
		this.type = type;
		this.setName( "<html>Edit <strong>" + this.type.getName() + "</strong>...</html>" );
		//this.setSmallIcon( org.alice.ide.common.TypeIcon.getInstance( type ) );
	}
	@Override
	protected java.awt.Dimension getDesiredDialogSize( edu.cmu.cs.dennisc.croquet.Dialog dialog ) {
		return new java.awt.Dimension( 640, 480 );
	}
	@Override
	protected org.alice.ide.editorstabbedpane.EditTypePanel prologue( edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< org.alice.ide.editorstabbedpane.EditTypePanel > context ) {
		return new org.alice.ide.editorstabbedpane.EditTypePanel( this.type );
	}
	@Override
	protected void epilogue( edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< org.alice.ide.editorstabbedpane.EditTypePanel > context, boolean isOk ) {
		if( isOk ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: EditTypeOperation" );
		} else {
			context.cancel();
		}
	}
}
