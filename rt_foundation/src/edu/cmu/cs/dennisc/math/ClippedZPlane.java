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
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class ClippedZPlane implements java.io.Serializable {
	private static final double DEFAULT_Y_VALUE = 0.1;
	
	private double m_xMinimum;
	private double m_yMinimum;
	private double m_xMaximum;
	private double m_yMaximum;

	public ClippedZPlane() {
		this( Double.NaN, -DEFAULT_Y_VALUE, Double.NaN, +DEFAULT_Y_VALUE );
	}
	public ClippedZPlane( double xMinimum, double yMinimum, double xMaximum, double yMaximum ) {
		set( xMinimum, yMinimum, xMaximum, yMaximum );
	}
	public ClippedZPlane( ClippedZPlane other ) {
		set( other );
	}
	public ClippedZPlane( ClippedZPlane other, java.awt.Rectangle viewport ) {
		set( other, viewport );
	}
	
	public static ClippedZPlane createNaN() {
		return new ClippedZPlane( Double.NaN, Double.NaN, Double.NaN, Double.NaN );
	}

	public void set( double xMinimum, double yMinimum, double xMaximum, double yMaximum ) {
		setXMinimum( xMinimum );
		setYMinimum( yMinimum );
		setXMaximum( xMaximum );
		setYMaximum( yMaximum );
	}
	public void set( ClippedZPlane other ) {
		set( other.m_xMinimum, other.m_yMinimum, other.m_xMaximum, other.m_yMaximum );
	}
	public void set( ClippedZPlane other, java.awt.Rectangle viewport ) {
		double minX = other.getXMinimum();
		double minY = other.getYMinimum();
		double maxX = other.getXMaximum();
		double maxY = other.getYMaximum();
		if( Double.isNaN( minX ) || Double.isNaN( maxX ) ) {
			if( Double.isNaN( minY ) || Double.isNaN( maxY ) ) {
				minY = -DEFAULT_Y_VALUE;
				maxY = +DEFAULT_Y_VALUE;
			}
			double factor = viewport.width / (double) viewport.height;
			minX = factor * minY;
			maxX = factor * maxY;
		} else {
			if( Double.isNaN( minY ) || Double.isNaN( maxY ) ) {
				double factor = viewport.height / (double) viewport.width;
				minY = factor * minX;
				maxY = factor * maxX;
			}
		}
		setXMinimum( minX );
		setYMinimum( minY );
		setXMaximum( maxX );
		setYMaximum( maxY );
	}

	public double getXMinimum() {
		return m_xMinimum;
	}
	public void setXMinimum( double xMinimum ) {
		m_xMinimum = xMinimum;
	}
	public double getXMaximum() {
		return m_xMaximum;
	}
	public void setXMaximum( double xMaximum ) {
		m_xMaximum = xMaximum;
	}

	public double getYMinimum() {
		return m_yMinimum;
	}
	public void setYMinimum( double yMinimum ) {
		m_yMinimum = yMinimum;
	}
	public double getYMaximum() {
		return m_yMaximum;
	}
	public void setYMaximum( double yMaximum ) {
		m_yMaximum = yMaximum;
	}
}
