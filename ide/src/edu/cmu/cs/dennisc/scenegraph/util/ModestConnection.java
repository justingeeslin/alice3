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
package edu.cmu.cs.dennisc.scenegraph.util;

import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.LineStrip;
import edu.cmu.cs.dennisc.scenegraph.ShadingStyle;
import edu.cmu.cs.dennisc.scenegraph.Vertex;

/**
 * @author Dennis Cosgrove
 */
public class ModestConnection extends Connection {
	private LineStrip m_sgLineStrip = new LineStrip();

	public ModestConnection() {
		Vertex[] vertices = new Vertex[ 32 ];
		for( int i = 0; i < vertices.length; i++ ) {
			vertices[ i ] = Vertex.createXYZ( 0, 0, 0 );
		}
		m_sgLineStrip.vertices.setValue( vertices );
		geometries.setValue( new Geometry[] { m_sgLineStrip } );
		getSGFrontFacingAppearance().setShadingStyle( ShadingStyle.NONE );
	}

	public void update() {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getTarget().getTransformation( this );
		double s = m.translation.calculateMagnitude();
		s *= 2;
		edu.cmu.cs.dennisc.math.polynomial.HermiteCubic x = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( 0, m.translation.x, 0, s * m.orientation.backward.x );
		edu.cmu.cs.dennisc.math.polynomial.HermiteCubic y = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( 0, m.translation.y, 0, s * m.orientation.backward.y );
		edu.cmu.cs.dennisc.math.polynomial.HermiteCubic z = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( 0, m.translation.z, -s, -s * m.orientation.backward.z );
		Vertex[] vertices = m_sgLineStrip.vertices.getValue();
		synchronized( vertices ) {
			double tDelta = 1.0 / ( vertices.length - 1 );
			double t = tDelta;
			for( int i = 1; i < vertices.length; i++ ) {
				vertices[ i ].position.set( x.evaluate( t ), y.evaluate( t ), z.evaluate( t ) );
				t += tDelta;
			}
			m_sgLineStrip.vertices.touch();
		}
	}
}
