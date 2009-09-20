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
abstract class AbstractDeclareFieldOperation extends org.alice.ide.operations.ast.AbstractDeclareFieldOperation {
	@Override
	protected final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getOwnerType() {
		return this.getIDE().getSceneType();
	}

	protected abstract edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object> createFieldAndInstance(edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType);

	@Override
	protected final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice createField(edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType) {
		edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object> tuple = this.createFieldAndInstance(ownerType);
		if (tuple != null) {
			getIDE().getSceneEditor().handleFieldCreation(ownerType, tuple.getA(), tuple.getB());
			return tuple.getA();
		} else {
			return null;
		}
	}

}
