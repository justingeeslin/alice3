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
package org.alice.ide.croquet.models.ui.debug;

class HistoryTreeModel extends edu.cmu.cs.dennisc.javax.swing.models.AbstractMutableTreeModel< edu.cmu.cs.dennisc.croquet.HistoryNode > {
	private edu.cmu.cs.dennisc.croquet.HistoryNode root;
	public HistoryTreeModel( edu.cmu.cs.dennisc.croquet.HistoryNode root ) {
		this.root = root;
	}
	public edu.cmu.cs.dennisc.croquet.HistoryNode getRoot() {
		return this.root;
	}
	public boolean isLeaf( Object node ) {
		return ( node instanceof edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> ) == false;
	}
	public int getChildCount(Object parent) {
		if( parent instanceof edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> ) {
			edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> modelContext = (edu.cmu.cs.dennisc.croquet.AbstractModelContext<?>)parent;
			return modelContext.getChildCount();
		} else {
			return 0;
		}
	}
	public edu.cmu.cs.dennisc.croquet.HistoryNode getChild(Object parent, int index) {
		if( parent instanceof edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> ) {
			edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> modelContext = (edu.cmu.cs.dennisc.croquet.AbstractModelContext<?>)parent;
			edu.cmu.cs.dennisc.croquet.HistoryNode rv = modelContext.getChildAt( index );
			assert rv != null;
			return rv;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	public int getIndexOfChild(java.lang.Object parent, Object child) {
		if( parent instanceof edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> ) {
			edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> modelContext = (edu.cmu.cs.dennisc.croquet.AbstractModelContext<?>)parent;
			if( child instanceof edu.cmu.cs.dennisc.croquet.HistoryNode ) {
				return modelContext.getIndexOfChild( (edu.cmu.cs.dennisc.croquet.HistoryNode)child );
			} else {
				return -1;
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	private java.util.List< edu.cmu.cs.dennisc.croquet.HistoryNode > updatePath( java.util.List< edu.cmu.cs.dennisc.croquet.HistoryNode > rv, edu.cmu.cs.dennisc.croquet.HistoryNode node ) {
		edu.cmu.cs.dennisc.croquet.HistoryNode parent = node.getParent();
		if( parent != null ) {
			updatePath( rv, parent );
		}
		rv.add( node );
		return rv;
	}
	public javax.swing.tree.TreePath getTreePath( edu.cmu.cs.dennisc.croquet.HistoryNode node ) {
		java.util.List< edu.cmu.cs.dennisc.croquet.HistoryNode > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		updatePath( list, node );
		return new javax.swing.tree.TreePath( list.toArray() );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class IsInteractionTreeShowingState extends org.alice.ide.croquet.models.IsFrameShowingState {
	private static class SingletonHolder {
		private static IsInteractionTreeShowingState instance = new IsInteractionTreeShowingState();
	}
	public static IsInteractionTreeShowingState getInstance() {
		return SingletonHolder.instance;
	}
	private edu.cmu.cs.dennisc.croquet.AbstractModelContext< ? > context;
	private IsInteractionTreeShowingState() {
		this( edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext() );
	}
	public IsInteractionTreeShowingState( edu.cmu.cs.dennisc.croquet.AbstractModelContext< ? > context ) {
		super( org.alice.ide.ProjectApplication.INFORMATION_GROUP, java.util.UUID.fromString( "3fb1e733-1736-476d-b40c-7729c82f0b21" ), false );
		this.context = context;
	}
	@Override
	protected void localize() {
		super.localize();
		this.setTextForBothTrueAndFalse( "Interaction Tree" );
	}
	@Override
	protected javax.swing.JFrame createFrame() {
		javax.swing.JFrame rv = super.createFrame();
		rv.setLocation( -1211, -11 );
		rv.setSize( 1222, 1566 );
		return rv;
	}
	@Override
	protected java.awt.Component createPane() {
		final HistoryTreeModel treeModel = new HistoryTreeModel( this.context );
		final javax.swing.JTree tree = new javax.swing.JTree( treeModel );
		
		for( int i=0; i<tree.getRowCount(); i++ ) {
			tree.expandRow( i );
		}
		tree.setRootVisible( false );
		final javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( tree );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );

		context.addChildrenObserver( new edu.cmu.cs.dennisc.croquet.AbstractModelContext.ChildrenObserver() {
			public void addingChild( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
			}
			public void addedChild( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						treeModel.reload();
						int childCount = treeModel.getChildCount( treeModel.getRoot() );
						for( int i=0; i<tree.getRowCount(); i++ ) {
							if( i<childCount-1 ) {
								tree.collapseRow( i );
							} else {
								tree.expandRow( i );
							}
						}
						tree.scrollRowToVisible( tree.getRowCount()-1 );
						scrollPane.getHorizontalScrollBar().setValue( 0 );
					}
				} );
			}
		} );
		return scrollPane;
	}
	@Override
	protected void handleChanged( boolean value ) {
		super.handleChanged( value );
	}
}
