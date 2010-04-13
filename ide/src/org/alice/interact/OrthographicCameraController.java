package org.alice.interact;

import java.awt.Rectangle;

import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;

public class OrthographicCameraController 
{
	protected edu.cmu.cs.dennisc.scenegraph.OrthographicCamera camera;
	
	public OrthographicCameraController(OrthographicCamera camera)
	{
		this.camera = camera;
	}
	
	public void fitToRectangle(Rectangle rect)
	{
		
	}
	
	public void zoom(double amount)
	{
		ClippedZPlane picturePlane = this.camera.picturePlane.getValue();
		picturePlane.setXMaximum(picturePlane.getXMaximum() + amount);
		picturePlane.setXMinimum(picturePlane.getXMinimum() - amount);
		picturePlane.setYMaximum(picturePlane.getYMaximum() + amount);
		picturePlane.setYMinimum(picturePlane.getYMinimum() - amount);
		this.camera.picturePlane.setValue(picturePlane);
	}
	
	

}
