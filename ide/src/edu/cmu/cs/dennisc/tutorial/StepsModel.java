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
/*package-private*/ class StepsModel {
	public static interface SelectionObserver {
		public void selectionChanging( StepsModel source, int fromIndex, int toIndex );
		public void selectionChanged( StepsModel source, int fromIndex, int toIndex );
	}
	private int selectedIndex = -1;
	private int furthestCompletedIndex = -1;
	private java.util.List<Step> steps = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy stepAccessPolicy = edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy.ALLOW_ACCESS_UP_TO_AND_INCLUDING_FURTHEST_COMPLETED_STEP;
	
	private java.util.List< SelectionObserver > selectionObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy getStepAccessPolicy() {
		return this.stepAccessPolicy;
	}
	public void setStepAccessPolicy( edu.cmu.cs.dennisc.croquet.guide.StepAccessPolicy stepAccessPolicy ) {
		this.stepAccessPolicy = stepAccessPolicy;
	}
	
	public void addSelectionObserver( SelectionObserver selectionObserver ) {
		this.selectionObservers.add( selectionObserver );
	}
	public void removeSelectionObserver( SelectionObserver selectionObserver ) {
		this.selectionObservers.add( selectionObserver );
	}
	public boolean isStepAccessible( int nextIndex ) {
		return this.stepAccessPolicy.isStepAccessible( nextIndex, this.furthestCompletedIndex );
	}
	public Step getStepAt(int index) {
		return this.steps.get(index);
	}
	public int getSize() {
		return this.steps.size();
	}

	/*package-private*/ int getSelectedIndex() {
		return this.selectedIndex;
	}
	/*package-private*/ void setSelectedIndex(int nextSelectedIndex) {
		int prevSelectedIndex = this.selectedIndex;
		if( this.selectedIndex != nextSelectedIndex ) {
			for( SelectionObserver selectionObserver : this.selectionObservers ) {
				selectionObserver.selectionChanging( this, prevSelectedIndex, nextSelectedIndex );
			}
			this.selectedIndex = nextSelectedIndex;
			for( SelectionObserver selectionObserver : this.selectionObservers ) {
				selectionObserver.selectionChanged( this, prevSelectedIndex, nextSelectedIndex );
			}
		}
		this.furthestCompletedIndex = Math.max( this.furthestCompletedIndex, nextSelectedIndex );
	}

	/*package-private*/ void decrementSelectedIndex() {
		this.setSelectedIndex(this.selectedIndex - 1);
	}

	/*package-private*/ void incrementSelectedIndex() {
		this.setSelectedIndex(this.selectedIndex + 1);
	}

	/*package-private*/ Step getSelectedStep() {
		if (this.selectedIndex >= 0) {
			return this.getStepAt(this.selectedIndex);
		} else {
			return null;
		}
	}
	/*package-private*/ void setSelectedStep(Step item) {
		int prevSelectedIndex = this.selectedIndex;
		int nextSelectedIndex = -1;
		final int N = this.steps.size();
		for (int i = 0; i < N; i++) {
			if (this.steps.get(i) == item) {
				nextSelectedIndex = i;
				break;
			}
		}
		
		if( this.stepAccessPolicy.isStepAccessible( nextSelectedIndex, this.furthestCompletedIndex ) || nextSelectedIndex < prevSelectedIndex ) {
			this.setSelectedIndex( nextSelectedIndex );
		}
	}

	/*package-private*/ void addStep(Step step) {
		this.steps.add(step);
	}
}
