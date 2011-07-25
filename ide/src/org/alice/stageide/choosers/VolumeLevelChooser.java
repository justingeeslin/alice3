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
public class VolumeLevelChooser extends org.alice.ide.choosers.AbstractRowsPaneChooser< edu.cmu.cs.dennisc.alice.ast.DoubleLiteral > {
	private org.alice.stageide.controls.VolumeLevelControl volumeLevelControl = new org.alice.stageide.controls.VolumeLevelControl();
	private org.lgna.croquet.components.Component< ? >[] components = { new org.lgna.croquet.components.SwingAdapter( this.volumeLevelControl ) };
	public VolumeLevelChooser() {
		//todo
	}
	@Override
	public void handlePrologue( org.lgna.croquet.history.InputDialogOperationStep step ) {
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		double volumeLevel;
		if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.DoubleLiteral ) {
			edu.cmu.cs.dennisc.alice.ast.DoubleLiteral doubleLiteral = (edu.cmu.cs.dennisc.alice.ast.DoubleLiteral)previousExpression;
			volumeLevel = doubleLiteral.value.getValue();
		} else {
			//todo?
			volumeLevel = 1.0;
		}
		this.volumeLevelControl.setVolumeLevel( volumeLevel );
	}
	
	@Override
	public org.lgna.croquet.components.Component< ? >[] getComponents() {
		return this.components;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.DoubleLiteral getValue() {
		edu.cmu.cs.dennisc.alice.ast.DoubleLiteral doubleLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( this.volumeLevelControl.getVolumeLevel() );
//		final boolean IS_LITERAL_DESIRED = true;
//		if( IS_LITERAL_DESIRED ) {
			return doubleLiteral;
//		} else {
//			return org.alice.ide.ast.NodeUtilities.createInstanceCreation( org.alice.apis.moveandturn.VolumeLevel.class, new Class<?>[] { Number.class }, doubleLiteral );
//		}
	}
	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		return null;
	}
}

