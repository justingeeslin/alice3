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
package org.alice.ide.croquet.edits.ast;

/**
 * @author Dennis Cosgrove
 */
public class AddExpressionEdit extends edu.cmu.cs.dennisc.croquet.OperationEdit< org.alice.ide.croquet.models.ast.cascade.AddExpressionCascadeOperation > {
	public static class AddExpressionEditMemento extends Memento<org.alice.ide.croquet.models.ast.cascade.AddExpressionCascadeOperation> {
		private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty;
		private edu.cmu.cs.dennisc.alice.ast.Expression expression;
		public AddExpressionEditMemento( AddExpressionEdit edit ) {
			super( edit );
			this.expressionListProperty = edit.expressionListProperty;
			this.expression = edit.expression;
		}
		public AddExpressionEditMemento( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: decode expressionListProperty" );
			this.expression = org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.Expression.class ).decode( binaryDecoder );
		}
		@Override
		protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: encode expressionListProperty" );
			org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.Expression.class ).encode( binaryEncoder, this.expression );
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.Edit< org.alice.ide.croquet.models.ast.cascade.AddExpressionCascadeOperation > createEdit() {
			return new AddExpressionEdit( this );
		}
	}
	private final edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty;
	private final edu.cmu.cs.dennisc.alice.ast.Expression expression;
	private transient int index;
	public AddExpressionEdit( edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty expressionListProperty, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		this.expressionListProperty = expressionListProperty;
		this.expression = expression;
	}
	private AddExpressionEdit( AddExpressionEditMemento memento ) {
		super( memento );
		this.expressionListProperty = memento.expressionListProperty;
		this.expression = memento.expression;
	}
	@Override
	public Memento< org.alice.ide.croquet.models.ast.cascade.AddExpressionCascadeOperation > createMemento() {
		return new AddExpressionEditMemento( this );
	}
	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		this.index = this.expressionListProperty.size();
		this.expressionListProperty.add( this.expression );
	}
	@Override
	protected final void undoInternal() {
		this.expressionListProperty.remove( this.index );
	}
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		rv.append( "add: " );
		edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr( rv, this.expression, locale );
		return rv;
	}
}
