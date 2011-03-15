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

package org.alice.ide.properties.uicontroller;

import java.awt.Color;
import java.util.Locale;

import org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState;
import org.alice.ide.properties.adapter.PropertyAdapter;
import org.alice.ide.swing.icons.ColorIcon;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.croquet.BorderPanel;
import edu.cmu.cs.dennisc.croquet.Label;
import edu.cmu.cs.dennisc.croquet.AbstractModel;
import edu.cmu.cs.dennisc.croquet.Panel;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;

public class Color4fPropertyController extends AbstractAdapterController<Color4f>
{
	private BorderPanel mainPanel;
	private Label colorLabel;
	
	private static final String BLANK_STRING = "NO COLOR";
	
	
	public Color4fPropertyController(PropertyAdapter<edu.cmu.cs.dennisc.color.Color4f, ?> propertyAdapter)
	{
		super(propertyAdapter);
	}

	@Override
	protected void initializeComponents() 
	{
		this.mainPanel = new BorderPanel();
		this.colorLabel = new Label();
		this.colorLabel.getAwtComponent().setOpaque(true);
	}
	
	@Override
	public Class<?> getPropertyType() 
	{
		return edu.cmu.cs.dennisc.color.Color4f.class;
	}
	
	@Override
	protected void setValueOnUI(edu.cmu.cs.dennisc.color.Color4f color)
	{
		if (color != null)
		{
			this.colorLabel.getAwtComponent().setOpaque(true);
			this.colorLabel.setText(null);
			this.colorLabel.setIcon(new ColorIcon( color.getAsAWTColor(), 50, 20 ) );
		}
		else
		{
			this.colorLabel.getAwtComponent().setOpaque(false);
			this.colorLabel.setText(BLANK_STRING);
			this.colorLabel.setIcon(null);
		}
		
	}

	@Override
	protected void updateUIFromNewAdapter() 
	{
		this.mainPanel.removeAllComponents();
		this.mainPanel.addComponent(this.colorLabel, BorderPanel.Constraint.CENTER);
		if (this.propertyAdapter != null)
		{
			this.mainPanel.addComponent(this.propertyAdapter.getEditOperation().createButton(), BorderPanel.Constraint.EAST);
		}
	}
	
	@Override
	public Panel getPanel() 
	{
		return this.mainPanel;
	}

}
