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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ class InputDialogCommitStep extends WaitingStep<edu.cmu.cs.dennisc.croquet.InputDialogOperation> {
	private static class InputDialogCommitFeature extends Feature {
		public InputDialogCommitFeature( edu.cmu.cs.dennisc.croquet.Resolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > trackableShapeResolver ) {
			super( trackableShapeResolver, Feature.ConnectionPreference.EAST_WEST );
		}
		@Override
		protected java.awt.Insets getContainsInsets() {
			return null;
		}
		@Override
		protected java.awt.Insets getPaintInsets() {
			return null;
		}
		@Override
		protected void paint( java.awt.Graphics2D g2, java.awt.Shape shape ) {
		}
	}
	public InputDialogCommitStep( String title, String text, final edu.cmu.cs.dennisc.croquet.Resolver<edu.cmu.cs.dennisc.croquet.InputDialogOperation> inputDialogOperationResolver ) {
		super( title, text, new InputDialogCommitFeature( new edu.cmu.cs.dennisc.croquet.Resolver< edu.cmu.cs.dennisc.croquet.TrackableShape >() {
			public edu.cmu.cs.dennisc.croquet.TrackableShape getResolved() {
				edu.cmu.cs.dennisc.croquet.InputDialogOperation inputDialogOperation = inputDialogOperationResolver.getResolved();
				if( inputDialogOperation != null ) {
					edu.cmu.cs.dennisc.croquet.Dialog activeDialog = inputDialogOperation.getActiveDialog();
					if( activeDialog != null ) {
						//todo
						return activeDialog;
					} else {
						return null;
					}
				} else {
					return null;
				}
			}
		} ), inputDialogOperationResolver );
	}
	@Override
	protected boolean isAlreadyInTheDesiredState() {
		return this.getModel().getActiveDialog() == null;
	}
	@Override
	public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryTreeNode<?> child ) {
		if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosedEvent ) {
			edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosedEvent windowClosedEvent = (edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosedEvent)child;
			edu.cmu.cs.dennisc.croquet.ModelContext<?> c = windowClosedEvent.getParent();
			while( c != null ) {
				if( c.getModel()  == this.getModel() ) {
					return true;
				}
				c = c.getParent();
			}
			return false;
		} else {
			return false;
		}
	}
	@Override
	protected void complete( edu.cmu.cs.dennisc.croquet.ModelContext< ? > context ) {
		edu.cmu.cs.dennisc.croquet.Dialog dialog = this.getModel().getActiveDialog();
		if( dialog != null ) {
			dialog.setVisible( false );
		}
	}
}
