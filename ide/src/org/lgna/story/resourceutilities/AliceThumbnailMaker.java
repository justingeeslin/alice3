/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.story.resourceutilities;

import java.util.HashMap;
import java.util.Map;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author Dave Culyba
 */
public class AliceThumbnailMaker extends AbstractThumbnailMaker {
	private static Map<String, AliceThumbnailMaker> instanceMap = new HashMap<String, AliceThumbnailMaker>();

	public static AliceThumbnailMaker getInstance() {
		return getInstance( AbstractThumbnailMaker.DEFAULT_THUMBNAIL_WIDTH, AbstractThumbnailMaker.DEFAULT_THUMBNAIL_HEIGHT );
	}

	public static AliceThumbnailMaker getInstance( int width, int height ) {
		String key = Integer.toString( width ) + "x" + Integer.toString( height );
		if( instanceMap.containsKey( key ) )
		{
			AliceThumbnailMaker instance = instanceMap.get( key );
			instance.clear();
			return instance;
		}
		else
		{
			AliceThumbnailMaker instance = new AliceThumbnailMaker( width, height );
			instanceMap.put( key, instance );
			return instance;
		}
	}

	private AliceThumbnailMaker( int width, int height )
	{
		super( width, height );
	}

	private AliceThumbnailMaker()
	{
		super( AbstractThumbnailMaker.DEFAULT_THUMBNAIL_WIDTH, AbstractThumbnailMaker.DEFAULT_THUMBNAIL_HEIGHT, AbstractThumbnailMaker.DEFAULT_ANTI_ALIAS_FACTOR );
	}

	private AffineMatrix4x4 getThumbnailCameraOrientationForLifeStage( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( ( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) ||
				( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) ||
				( lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) ) {
			return getThumbnailCameraOrientation( new AxisAlignedBox( -.4, 0, -.4, .4, 1.6, .5 ) );
		}
		else {
			return getThumbnailCameraOrientation( new AxisAlignedBox( -.2, 0, -.2, .2, 1.1, .2 ) );
		}
	}

	private AffineMatrix4x4 getThumbnailCameraOrientationForPerson( org.lgna.story.resources.sims2.PersonResource personResource ) {
		return getThumbnailCameraOrientationForLifeStage( personResource.getLifeStage() );
	}

	private AffineMatrix4x4 getHeadThumbnailCameraOrientationForLifeStage( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( ( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) ||
				( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) ) {
			return getThumbnailCameraOrientation( new Point3( 0, 1.58, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .8 );
		}
		else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) {
			return getThumbnailCameraOrientation( new Point3( 0, 1.48, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .8 );
		}
		else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TODDLER ) {
			return getThumbnailCameraOrientation( new Point3( 0, .7, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .6 );
		}
		else {
			return getThumbnailCameraOrientation( new Point3( 0, 1.1, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .8 );
		}
	}

	private AffineMatrix4x4 getBodyThumbnailCameraOrientationForLifeStage( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( ( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) ||
				( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) ) {
			return getThumbnailCameraOrientation( new Point3( 0, .72, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 3.3 );
		}
		if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) {
			return getThumbnailCameraOrientation( new Point3( 0, .68, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 3.25 );
		}
		else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TODDLER ) {
			return getThumbnailCameraOrientation( new Point3( 0, .3, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 2 );
		}
		else {
			return getThumbnailCameraOrientation( new Point3( 0, .5, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 2.3 );
		}
	}

	private AffineMatrix4x4 getTopBodyThumbnailCameraOrientationForLifeStage( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( ( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) ||
				( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) ) {
			return getThumbnailCameraOrientation( new Point3( 0, 1.07, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 1.8 );
		}
		else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) {
			return getThumbnailCameraOrientation( new Point3( 0, 1.02, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 1.6 );
		}
		else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TODDLER ) {
			return getThumbnailCameraOrientation( new Point3( 0, .5, 0 ), new Vector3( 1.0, 0.0, 3.0 ), .8 );
		}
		else {
			return getThumbnailCameraOrientation( new Point3( 0, .75, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 1.2 );
		}
	}

	private AffineMatrix4x4 getBottomBodyThumbnailCameraOrientationForLifeStage( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( ( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) ||
				( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) ) {
			return getThumbnailCameraOrientation( new Point3( 0, .5, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 2.4 );
		}
		else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) {
			return getThumbnailCameraOrientation( new Point3( 0, .48, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 2.3 );
		}
		else {
			return getThumbnailCameraOrientation( new Point3( 0, .33, 0 ), new Vector3( 1.0, 0.0, 3.0 ), 1.6 );
		}
	}

	private AffineMatrix4x4 getFaceThumbnailCameraOrientationForLifeStageAndGender( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender ) {
		if( ( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) ||
				( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) ) {
			if( gender == org.lgna.story.resources.sims2.Gender.FEMALE ) {
				return getThumbnailCameraOrientation( new Point3( 0, 1.58, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .56 );
			}
			else {
				return getThumbnailCameraOrientation( new Point3( 0, 1.569, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .569 );
			}
		}
		else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) {
			if( gender == org.lgna.story.resources.sims2.Gender.FEMALE ) {
				return getThumbnailCameraOrientation( new Point3( 0, 1.48, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .56 );
			}
			else {
				return getThumbnailCameraOrientation( new Point3( 0, 1.469, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .569 );
			}
		}
		else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TODDLER ) {
			return getThumbnailCameraOrientation( new Point3( 0, .685, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .4 );
		}
		else {
			return getThumbnailCameraOrientation( new Point3( 0, 1.09, 0 ), new Vector3( 1.0, 0.0, 4.0 ), .48 );
		}
	}

	public synchronized java.awt.image.BufferedImage createThumbnailFromPersonVisualData( org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData, boolean trim ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		java.awt.image.BufferedImage returnImage = takePicture( getThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim );
		visualData.setSGParent( null );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized java.awt.image.BufferedImage createThumbnailFromPersonResource( org.lgna.story.resources.sims2.PersonResource resource ) throws Exception {
		org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory factory = org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory.getInstance( resource );
		org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData = (org.lgna.story.implementation.sims2.NebulousPersonVisualData)factory.createVisualData();
		java.awt.image.BufferedImage thumbnail = createThumbnailFromPersonVisualData( visualData, true );
		( (org.lgna.story.implementation.sims2.NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public synchronized java.awt.image.BufferedImage createBodyThumbnailFromPersonVisualData( org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData, boolean trim ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		java.awt.image.BufferedImage returnImage = takePicture( getBodyThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim );
		visualData.setSGParent( null );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized java.awt.image.BufferedImage createBodyThumbnailFromPersonResource( org.lgna.story.resources.sims2.PersonResource resource, boolean trim ) throws Exception {
		org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory factory = org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory.getInstance( resource );
		org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData = (org.lgna.story.implementation.sims2.NebulousPersonVisualData)factory.createVisualData();
		java.awt.image.BufferedImage thumbnail = createBodyThumbnailFromPersonVisualData( visualData, trim );
		( (org.lgna.story.implementation.sims2.NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public synchronized java.awt.image.BufferedImage createTopBodyThumbnailFromPersonVisualData( org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData, boolean trim ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		java.awt.image.BufferedImage returnImage = takePicture( getTopBodyThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim );
		visualData.setSGParent( null );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized java.awt.image.BufferedImage createTopBodyThumbnailFromPersonResource( org.lgna.story.resources.sims2.PersonResource resource, boolean trim ) throws Exception {
		org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory factory = org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory.getInstance( resource );
		org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData = (org.lgna.story.implementation.sims2.NebulousPersonVisualData)factory.createVisualData();
		java.awt.image.BufferedImage thumbnail = createTopBodyThumbnailFromPersonVisualData( visualData, trim );
		( (org.lgna.story.implementation.sims2.NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public synchronized java.awt.image.BufferedImage createBottomBodyThumbnailFromPersonVisualData( org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData, boolean trim ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		java.awt.image.BufferedImage returnImage = takePicture( getBottomBodyThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim );
		visualData.setSGParent( null );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized java.awt.image.BufferedImage createBottomBodyThumbnailFromPersonResource( org.lgna.story.resources.sims2.PersonResource resource, boolean trim ) throws Exception {
		org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory factory = org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory.getInstance( resource );
		org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData = (org.lgna.story.implementation.sims2.NebulousPersonVisualData)factory.createVisualData();
		java.awt.image.BufferedImage thumbnail = createBottomBodyThumbnailFromPersonVisualData( visualData, trim );
		( (org.lgna.story.implementation.sims2.NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public synchronized java.awt.image.BufferedImage createHeadThumbnailFromPersonVisualData( org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData, boolean trim ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		java.awt.image.BufferedImage returnImage = takePicture( getHeadThumbnailCameraOrientationForLifeStage( visualData.getLifeStage() ), trim );
		visualData.setSGParent( null );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized java.awt.image.BufferedImage createHeadThumbnailFromPersonResource( org.lgna.story.resources.sims2.PersonResource resource, boolean trim ) throws Exception {
		org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory factory = org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory.getInstance( resource );
		org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData = (org.lgna.story.implementation.sims2.NebulousPersonVisualData)factory.createVisualData();
		java.awt.image.BufferedImage thumbnail = createHeadThumbnailFromPersonVisualData( visualData, trim );
		( (org.lgna.story.implementation.sims2.NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	public synchronized java.awt.image.BufferedImage createFaceThumbnailFromPersonVisualData( org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData, boolean trim ) {
		visualData.setSGParent( this.getModelTransformable() );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getModelTransformable() );
		}
		java.awt.image.BufferedImage returnImage = takePicture( getFaceThumbnailCameraOrientationForLifeStageAndGender( visualData.getLifeStage(), visualData.getGender() ), trim );
		visualData.setSGParent( null );
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : visualData.getSgVisuals() ) {
			sgVisual.setParent( null );
			this.removeComponent( sgVisual );
		}
		return returnImage;
	}

	public synchronized java.awt.image.BufferedImage createFaceThumbnailFromPersonResource( org.lgna.story.resources.sims2.PersonResource resource, boolean trim ) throws Exception {
		org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory factory = org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory.getInstance( resource );
		org.lgna.story.implementation.sims2.NebulousPersonVisualData visualData = (org.lgna.story.implementation.sims2.NebulousPersonVisualData)factory.createVisualData();
		java.awt.image.BufferedImage thumbnail = createFaceThumbnailFromPersonVisualData( visualData, trim );
		( (org.lgna.story.implementation.sims2.NebulousVisualData<?>)visualData ).unload();
		this.clear();
		return thumbnail;
	}

	@Override
	protected AffineMatrix4x4 getThumbnailTransform( edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox ) {
		return getThumbnailCameraOrientation( bbox );
	}
}
