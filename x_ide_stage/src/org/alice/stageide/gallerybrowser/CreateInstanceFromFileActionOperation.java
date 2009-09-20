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
package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
abstract class CreateInstanceFromFileActionOperation extends AbstractDeclareFieldOperation {
	protected abstract java.io.File getInitialDirectory();

	private void showMessageDialog(java.io.File file, boolean isValidZip) {
		StringBuffer sb = new StringBuffer();
		sb.append("Unable to create instance from file ");
		sb.append(edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible(file));
		sb.append(".\n\n");
		sb.append(getIDE().getApplicationName());
		sb.append(" is able to create instances from class files saved by ");
		sb.append(getIDE().getApplicationName());
		sb.append(".\n\nLook for files with an ");
		sb.append(edu.cmu.cs.dennisc.alice.io.FileUtilities.TYPE_EXTENSION);
		sb.append(" extension.");
		javax.swing.JOptionPane.showMessageDialog(org.alice.ide.IDE.getSingleton(), sb.toString(), "Cannot read file", javax.swing.JOptionPane.ERROR_MESSAGE);
	}

	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, java.lang.Object> createFieldAndInstance(edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType) {
		java.io.File directory = this.getInitialDirectory();
		java.io.File file = edu.cmu.cs.dennisc.awt.FileDialogUtilities.showOpenFileDialog(this.getIDE(), directory, null, edu.cmu.cs.dennisc.alice.io.FileUtilities.TYPE_EXTENSION, false);

		if (file != null) {
			String lcFilename = file.getName().toLowerCase();
			if (lcFilename.endsWith(".a2c")) {
				javax.swing.JOptionPane.showMessageDialog(org.alice.ide.IDE.getSingleton(), "Alice3 does not load Alice2 characters", "Cannot read file", javax.swing.JOptionPane.ERROR_MESSAGE);
			} else if (lcFilename.endsWith(edu.cmu.cs.dennisc.alice.io.FileUtilities.PROJECT_EXTENSION.toLowerCase())) {
				javax.swing.JOptionPane.showMessageDialog(this.getIDE(), file.getAbsolutePath() + " appears to be a project file and not a class file.\n\nLook for files with an " + edu.cmu.cs.dennisc.alice.io.FileUtilities.TYPE_EXTENSION + " extension.", "Incorrect File Type",
						javax.swing.JOptionPane.INFORMATION_MESSAGE);
			} else {
				boolean isWorthyOfException = lcFilename.endsWith(edu.cmu.cs.dennisc.alice.io.FileUtilities.TYPE_EXTENSION.toLowerCase());
				java.util.zip.ZipFile zipFile;
				try {
					zipFile = new java.util.zip.ZipFile(file);
				} catch (java.io.IOException ioe) {
					if (isWorthyOfException) {
						throw new RuntimeException(file.getAbsolutePath(), ioe);
					} else {
						this.showMessageDialog(file, false);
						zipFile = null;
					}
				}
				if (zipFile != null) {
					edu.cmu.cs.dennisc.alice.ast.AbstractType type;
					try {
						type = edu.cmu.cs.dennisc.alice.io.FileUtilities.readType(zipFile);
					} catch (java.io.IOException ioe) {
						if (isWorthyOfException) {
							throw new RuntimeException(file.getAbsolutePath(), ioe);
						} else {
							this.showMessageDialog(file, true);
							type = null;
						}
					}
					if (type != null) {
						org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane createFieldPane = new org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane(ownerType, type);
						edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldPane.showInJDialog(getIDE(), "Create New Instance", true);
						if (field != null) {
							Object instanceInJava = createFieldPane.createInstanceInJava();
							return new edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object>( field, instanceInJava );
						}
					}
				}
			}
		}
		return null;
	}
}
