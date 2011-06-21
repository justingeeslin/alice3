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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class CascadeBlank< B > extends Element {
	public CascadeBlank( java.util.UUID id ) {
		super( id );
	}
	protected abstract java.util.List< CascadeBlankChild > updateChildren( java.util.List< CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode<B> blankNode );
	private final Iterable< CascadeBlankChild > getChildren( org.lgna.croquet.cascade.BlankNode<B> blankNode ) {
		java.util.List< CascadeBlankChild > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.updateChildren( rv, blankNode );
		return rv;
	}

	private static boolean isEmptySeparator( CascadeBlankChild child ) {
		return child instanceof CascadeLineSeparator || ( (child instanceof CascadeLabelSeparator) && ((CascadeLabelSeparator)child).isValid() == false );
	}

	public final CascadeBlankChild[] getFilteredChildren( org.lgna.croquet.cascade.BlankNode<B> blankNode ) {
		java.util.List< CascadeBlankChild > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.updateChildren( rv, blankNode );

		java.util.ListIterator< CascadeBlankChild > listIterator = rv.listIterator();
		boolean isLineSeparatorAcceptable = false;
		while( listIterator.hasNext() ) {
			CascadeBlankChild child = listIterator.next();
			if( isEmptySeparator( child ) ) {
				if( isLineSeparatorAcceptable ) {
					//pass 
				} else {
					listIterator.remove();
				}
				isLineSeparatorAcceptable = false;
			} else {
				isLineSeparatorAcceptable = true;
			}
		}

		//remove separators at the end
		//there should be a maximum of only 1 but we loop anyway 
		final int N = rv.size();
		for( int i = 0; i < N; i++ ) {
			int index = N - i - 1;
			if( isEmptySeparator( rv.get( index ) ) ) {
				rv.remove( index );
			} else {
				break;
			}
		}

		return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( rv, CascadeBlankChild.class );
	}
}
