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
package org.alice.ide.ubiquitouspane;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractStatementTemplate extends org.alice.ide.templates.StatementTemplate {
	private Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls;
	public AbstractStatementTemplate( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls ) {
		super( null );
		this.cls = cls;
	}
	
}
