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

package org.alice.stageide.choosers;

/**
 * @author Dennis Cosgrove
 */
public class ColorChooser extends org.alice.ide.choosers.ValueChooser< org.lgna.project.ast.Expression > {
	private final javax.swing.JColorChooser jColorChooser = new javax.swing.JColorChooser();
	private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			org.lgna.croquet.history.TransactionManager.TODO_REMOVE_fireEvent( org.lgna.croquet.triggers.ChangeEventTrigger.createUserInstance( e ) );
		}
	};
	public ColorChooser() {
		org.lgna.project.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression != null ) {
			try {
				org.lgna.story.Color color = org.alice.ide.IDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForExpression( previousExpression, org.lgna.story.Color.class );
				if( color != null ) {
					edu.cmu.cs.dennisc.color.Color4f color4f = org.lgna.story.ImplementationAccessor.getColor4f( color );
					this.jColorChooser.setColor( color4f.getAsAWTColor() );
				}
			} catch( Throwable t ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t, previousExpression );
			}
		}
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.jColorChooser.getSelectionModel().addChangeListener( this.changeListener );
	}
	@Override
	protected void handleUndisplayable() {
		this.jColorChooser.getSelectionModel().removeChangeListener( this.changeListener );
		super.handleUndisplayable();
	}
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
		return new org.lgna.croquet.components.SwingAdapter( this.jColorChooser );
	}
	@Override
	public org.lgna.project.ast.Expression getValue() {
		java.awt.Color awtColor = this.jColorChooser.getColor();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.alice.ide.ast.ExpressionCreator expressionCreator = ide.getApiConfigurationManager().getExpressionCreator();
		org.lgna.story.Color color = org.lgna.story.ImplementationAccessor.createColor( new edu.cmu.cs.dennisc.color.Color4f( awtColor ) );
		try {
			return expressionCreator.createExpression( color );
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
			ccee.printStackTrace();
			return null;
		}
	}
	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		return null;
	}
}
