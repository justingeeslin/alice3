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

/**
 * @author Dennis Cosgrove
 */
public class ThisPane extends AccessiblePane {
	private static final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava TYPE_FOR_NULL = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Void.class );
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type = TYPE_FOR_NULL;
	private org.alice.ide.event.IDEListener ideAdapter = new org.alice.ide.event.IDEListener() {
		public void fieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
		}
		public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		}
		public void focusedCodeChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		}
		public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
			ThisPane.this.updateBasedOnFocusedCode( e.getNextValue() );
		}
		public void localeChanging( org.alice.ide.event.LocaleEvent e ) {
		}
		public void localeChanged( org.alice.ide.event.LocaleEvent e ) {
		}
		public void projectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
		}
		public void projectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
		}
		public void transientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
		}
		public void transientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
		}
	};

	public ThisPane() {
		javax.swing.JLabel label = edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( getIDE().getTextForThis() );
		this.add( label );
		this.setBackground( getIDE().getColorFor( edu.cmu.cs.dennisc.alice.ast.ThisExpression.class ) );
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.updateBasedOnFocusedCode( org.alice.ide.IDE.getSingleton().getFocusedCode() );
		getIDE().addIDEListener( this.ideAdapter );
	}
	@Override
	public void removeNotify() {
		getIDE().removeIDEListener( this.ideAdapter );
		super.removeNotify();
	}
	private void updateBasedOnFocusedCode( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		if( code != null ) {
			this.type = code.getDeclaringType();
		} else {
			this.type = null;
		}
		if( this.type != null ) {
			this.setToolTipText( "the current instance of " + this.type.getName() + " is referred to as " + getIDE().getTextForThis() );
		} else {
			this.type = TYPE_FOR_NULL;
			this.setToolTipText( null );
		}
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return this.type;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		return new edu.cmu.cs.dennisc.alice.ast.ThisExpression();
	}

	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		if( this.type == TYPE_FOR_NULL ) {
			g2.setPaint( edu.cmu.cs.dennisc.zoot.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
	
}
