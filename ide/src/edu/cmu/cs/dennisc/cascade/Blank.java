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
package edu.cmu.cs.dennisc.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class Blank extends Node {
	private edu.cmu.cs.dennisc.task.TaskObserver taskObserver;
	private FillIn selectedFillIn;

	public FillIn getFillInAt( int index ) {
		return (FillIn)getChildren().get( index );
	}
	public FillIn getParentFillIn() {
		return (FillIn)getParent();
	}
	
	public void addFillIn( edu.cmu.cs.dennisc.cascade.FillIn fillIn ) {
		super.addChild( fillIn );
	}

	@Override
	protected Blank getNearestBlank() {
		return this;
	}
	@Override
	protected Node getNextNode() {
		return this;
	}
	public FillIn getSelectedFillIn() {
		return this.selectedFillIn;
	}
	public void setSelectedFillIn( FillIn fillIn ) {
		this.selectedFillIn = fillIn;
		Node parent = this.getParent();
		if( parent instanceof FillIn ) {
			FillIn parentFillIn = (FillIn)parent;
			for( Node child : parent.getChildren() ) {
				Blank blank = (Blank)child;
				if( blank.selectedFillIn != null ) {
					//pass
				} else {
					return;
				}
			}
			parentFillIn.select();
		}
	}

	public void addSeparator() {
		this.addSeparator( null );
	}
	public void addSeparator( String text ) {
		this.addFillIn( new SeparatorFillIn( text ) );
	}
	public void showPopupMenu( java.awt.Component invoker, int x, int y, final edu.cmu.cs.dennisc.task.TaskObserver< ? extends Object > taskObserver ) {
		this.taskObserver = taskObserver;
		final javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
		//
		//popupMenu.setLightWeightPopupEnabled( false );
		//
		popupMenu.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
			public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
				Blank.this.addNextNodeMenuItems( popupMenu );
			}
			public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
				popupMenu.removeAll();
			}
			public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
				Blank.this.handleCancel( e );
			}
		} );
		edu.cmu.cs.dennisc.swing.PopupMenuUtilities.showModal( popupMenu, invoker, x, y );
	}

	protected void handleActionPerformed() {
		try {
			Object value = this.getSelectedFillIn().getValue();
			if( this.taskObserver != null ) {
				this.taskObserver.handleCompletion( value );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleCompleted (no taskObserver):", this.getSelectedFillIn().getValue() );
			}
		} catch( CancelException ce ) {
			if( this.taskObserver != null ) {
				this.taskObserver.handleCancelation();
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleCancelation (no taskObserver)" );
			}
		}
	}
	protected void handleCancel( javax.swing.event.PopupMenuEvent e ) {
		if( this.taskObserver != null ) {
			this.taskObserver.handleCancelation();
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleCancelation (no taskObserver)" );
		}
	}

}
