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
package org.alice.ide.common;

import edu.cmu.cs.dennisc.alice.ast.NodeListProperty;
import edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
public class ParameterPane extends AccessiblePane {
	private NodeListProperty< ParameterDeclaredInAlice > parametersProperty;
	private ParameterDeclaredInAlice parameter;

	public ParameterPane( NodeListProperty< ParameterDeclaredInAlice > parametersProperty, ParameterDeclaredInAlice parameter ) {
		this.parametersProperty = parametersProperty;
		this.parameter = parameter;
		this.add( new org.alice.ide.common.DeclarationNameLabel( this.parameter ) );
		this.setBackgroundColor( getIDE().getColorFor( edu.cmu.cs.dennisc.alice.ast.ParameterAccess.class ) );
		final org.alice.ide.operations.ast.RenameParameterOperation renameParameterOperation = new org.alice.ide.operations.ast.RenameParameterOperation( this.parameter );
		
		if( this.parametersProperty != null ) {
			final org.alice.ide.operations.ast.DeleteParameterOperation deleteParameterOperation = new org.alice.ide.operations.ast.DeleteParameterOperation( this.parametersProperty, this.parameter );
			final org.alice.ide.operations.ast.ForwardShiftParameterOperation forwardShiftCodeParameterOperation = new org.alice.ide.operations.ast.ForwardShiftParameterOperation( this.parametersProperty, this.parameter );
			final org.alice.ide.operations.ast.BackwardShiftParameterOperation backwardShiftCodeParameterOperation = new org.alice.ide.operations.ast.BackwardShiftParameterOperation( this.parametersProperty, this.parameter );
			this.setPopupOperation( new edu.cmu.cs.dennisc.zoot.AbstractPopupActionOperation() {
				@Override
				protected java.util.List< edu.cmu.cs.dennisc.zoot.Operation > getOperations() {
					java.util.List< edu.cmu.cs.dennisc.zoot.Operation > rv = new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.Operation >();
					rv.add( renameParameterOperation );
					if( forwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						rv.add( forwardShiftCodeParameterOperation );
					}
					if( backwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						rv.add( backwardShiftCodeParameterOperation );
					}
					rv.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
					rv.add( deleteParameterOperation );
					return rv;
				}
			} );
		} else {
			this.setPopupOperation( new edu.cmu.cs.dennisc.zoot.DefaultPopupActionOperation( renameParameterOperation ) );
		}
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return parameter.getValueType();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		return new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( this.parameter );
	}
}
