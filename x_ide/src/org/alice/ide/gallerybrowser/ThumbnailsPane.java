/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.gallerybrowser;

class SingleOrDoubleClickListUI extends javax.swing.plaf.basic.BasicListUI {
	@Override
	protected javax.swing.event.MouseInputListener createMouseInputListener() {
		return new javax.swing.event.MouseInputListener() {
			private long tPrevious = System.currentTimeMillis();
			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
				long tCurrent = e.getWhen();
				long tDelta = tCurrent - tPrevious;
				if( tDelta > 400 ) {
					int row = SingleOrDoubleClickListUI.this.locationToIndex( list, e.getPoint() );
	                list.setValueIsAdjusting( true );
	                list.setSelectionInterval(row, row);
					tPrevious = tCurrent;
				}
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
                list.setValueIsAdjusting( false );
			}
			public void mouseMoved( java.awt.event.MouseEvent e ) {
			}
			public void mouseDragged( java.awt.event.MouseEvent e ) {
			}
		};
	}
}

class SingleOrDoubleClickList extends javax.swing.JList {
	@Override
	public void updateUI() {
		this.setUI( new SingleOrDoubleClickListUI() );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class ThumbnailsPane extends swing.LineAxisPane {
	class ThumbnailSnapshotListCellRenderer extends org.alice.ide.swing.SnapshotListCellRenderer {
		private javax.swing.Icon folderIcon;

		public void setFolderIcon( javax.swing.Icon folderIcon ) {
			this.folderIcon = folderIcon;
		}
		@Override
		protected javax.swing.JLabel updateLabel( javax.swing.JLabel rv, Object value ) {
			java.io.File file = (java.io.File)value;
			if( file != null ) {
				String text = ThumbnailsPane.this.getTextFor( file );
				java.io.File iconFile;
				if( file.isDirectory() ) {
					iconFile = new java.io.File( file, "directoryThumbnail.png" );
				} else {
					iconFile = file;
				}
				rv.setText( text );
				javax.swing.Icon icon;
				if( iconFile.exists() ) {
					String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( iconFile );
					icon = new javax.swing.ImageIcon( path );
				} else {
					icon = null;
				}
				if( file.isDirectory() ) {
					if( this.folderIcon != null ) {
						if( icon != null ) {
							icon = new edu.cmu.cs.dennisc.swing.CompositeIcon( this.folderIcon, icon );
						} else {
							icon = this.folderIcon;
						}
					}
				}
				rv.setIcon( icon );
			}
			return rv;
		}
	}
	private SingleOrDoubleClickList list = new SingleOrDoubleClickList();
	private ThumbnailSnapshotListCellRenderer thumbnailSnapshotListCellRenderer = new ThumbnailSnapshotListCellRenderer();

	public ThumbnailsPane() {
		this.list.setCellRenderer( this.thumbnailSnapshotListCellRenderer );
		this.list.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.list.setVisibleRowCount( 1 );
		this.list.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					java.io.File file = (java.io.File)ThumbnailsPane.this.list.getSelectedValue();
					if( file != null ) {
						ThumbnailsPane.this.handleFileActivation( file );
						ThumbnailsPane.this.list.clearSelection();
					}
				}
			}
		} );
		this.list.addKeyListener( new java.awt.event.KeyListener() {
			public void keyPressed(java.awt.event.KeyEvent e) {
				if( e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ) {
					ThumbnailsPane.this.handleBackSpaceKey();
				}
			}
			public void keyReleased(java.awt.event.KeyEvent e) {
			}
			public void keyTyped(java.awt.event.KeyEvent e) {
			}
		} );
		
		this.add( new javax.swing.JScrollPane( this.list ) );
	}

	protected abstract String getTextFor( java.io.File file );
	protected abstract void handleFileActivation( java.io.File file );
	protected abstract void handleBackSpaceKey();
	public void setFolderIcon( javax.swing.Icon folderIcon ) {
		this.thumbnailSnapshotListCellRenderer.setFolderIcon( folderIcon );
	}
	
	/*package private*/ static java.io.File[] listPackages( java.io.File directory ) {
		return edu.cmu.cs.dennisc.io.FileUtilities.listDirectories( directory );
	}
	/*package private*/ static java.io.File[] listClasses( java.io.File directory ) {
		return edu.cmu.cs.dennisc.io.FileUtilities.listFiles( directory, new java.io.FileFilter() {
			public boolean accept( java.io.File file ) {
				String lcFilename = file.getName().toLowerCase();
				return file.isFile() && lcFilename.equals( "directorythumbnail.png" ) == false;
			}
		} );
	}
	public void setDirectory( java.io.File directory ) {
		java.io.File[] directories = ThumbnailsPane.listPackages( directory );
		java.io.File[] thumbnails = ThumbnailsPane.listClasses( directory );

		java.util.Vector< java.io.File > data = new java.util.Vector< java.io.File >();
		data.ensureCapacity( directories.length + thumbnails.length );
		for( java.io.File file : directories ) {
			data.add( file );
		}
		for( java.io.File file : thumbnails ) {
			data.add( file );
		}
		this.list.setListData( data );
	}
}
