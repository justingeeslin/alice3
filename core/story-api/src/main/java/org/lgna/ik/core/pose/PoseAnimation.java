/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.core.pose;

import edu.cmu.cs.dennisc.animation.DurationBasedAnimation;
import edu.cmu.cs.dennisc.animation.Style;

/**
 * @author Matt May
 */
public class PoseAnimation extends DurationBasedAnimation {
	private final org.lgna.story.implementation.JointedModelImp<?, ?> jointedModel;
	private final Pose<?> pose;
	private transient java.util.List<JointInfo> jointInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	private static class JointInfo {
		private final org.lgna.story.implementation.JointImp jointImp;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q0;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q1;

		public JointInfo( org.lgna.story.implementation.JointedModelImp<?, ?> jointedModel, JointIdQuaternionPair jointKey ) {
			this.jointImp = jointedModel.getJointImplementation( jointKey.getJointId() );
			this.q0 = this.jointImp.getLocalOrientation().createUnitQuaternion();
			this.q1 = jointKey.getQuaternion().createUnitQuaternion();
		}

		public void setPortion( double portion ) {
			edu.cmu.cs.dennisc.math.UnitQuaternion q = edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( this.q0, this.q1, portion );
			jointImp.setLocalOrientation( q.createOrthogonalMatrix3x3() );
		}

		public void epilogue() {
			jointImp.setLocalOrientation( this.q1.createOrthogonalMatrix3x3() );
		}
	}

	public PoseAnimation( double duration, Style style, org.lgna.story.implementation.JointedModelImp<?, ?> jointedModel, Pose pose ) {
		super( duration, style );
		this.jointedModel = jointedModel;
		this.pose = pose;
	}

	@Override
	protected void prologue() {
		this.jointInfos.clear();
		for( JointIdQuaternionPair jointKey : this.pose.getJointKeys() ) {
			appendJointInfos( this.jointInfos, this.jointedModel, jointKey );
		}
	}

	private static void appendJointInfos( java.util.List<JointInfo> jointInfos, org.lgna.story.implementation.JointedModelImp<?, ?> jointedModel, JointIdQuaternionPair jointKey ) {
		jointInfos.add( new JointInfo( jointedModel, jointKey ) );
	}

	@Override
	protected void setPortion( double portion ) {
		for( JointInfo jointInfo : this.jointInfos ) {
			jointInfo.setPortion( portion );
		}
	}

	@Override
	protected void epilogue() {
		for( JointInfo jointInfo : this.jointInfos ) {
			jointInfo.epilogue();
		}
	}

}
