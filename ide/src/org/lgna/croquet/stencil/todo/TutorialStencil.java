package org.lgna.croquet.stencil.todo;

// TODO: <kjh/> THIS IS NOT DONE!!!
/**
 * @author Kyle J. Harms
 */
public abstract class TutorialStencil extends org.lgna.croquet.components.Stencil {

	public static final java.awt.Color STENCIL_BASE_COLOR =  new java.awt.Color( 181, 140, 140, 150 );
	public static final java.awt.Color STENCIL_LINE_COLOR =  new java.awt.Color( 92, 48, 24, 63 );

	private final java.awt.Paint stencilPaint;
	private final org.lgna.croquet.stencil.ScrollRenderer scrollRenderer;
	private Feature enteredFeature;

	public TutorialStencil( org.lgna.croquet.components.AbstractWindow<?> window ) {
		super( window );
		this.scrollRenderer = new org.lgna.croquet.stencil.BasicScrollRenderer();
		this.stencilPaint = this.getStencilPaint();
	}

	// TODO: <kjh/> Do I want this here?
	protected abstract boolean isPaintingStencilEnabled();
	protected abstract Page getCurrentPage();

	@Override
	protected void redispatchMouseEvent(java.awt.event.MouseEvent eSrc) {
		Page page = this.getCurrentPage();

		boolean isInterceptable = page == null || page.isEventInterceptable( eSrc );
		java.awt.Point pSrc = eSrc.getPoint();
		java.awt.Component componentSrc = eSrc.getComponent();
		if( isInterceptable && eSrc.getComponent().contains( pSrc.x, pSrc.y ) ) {
			// pass
		} else {
			javax.swing.MenuSelectionManager menuSelectionManager = javax.swing.MenuSelectionManager.defaultManager();
			if( menuSelectionManager.getSelectedPath().length > 0 ) {
				java.awt.Component componentDst = menuSelectionManager.componentForPoint( componentSrc, pSrc );
				if( componentDst != null ) {
					java.awt.Point pDst = javax.swing.SwingUtilities.convertPoint( componentSrc, pSrc, componentDst );
					menuSelectionManager.processMouseEvent( eSrc );
				}
			} else {
				javax.swing.JFrame jFrame = org.lgna.croquet.Application.getActiveInstance().getFrame().getAwtComponent();
				java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt(jFrame.getContentPane(), pSrc.x, pSrc.y);
				if (component != null) {
					java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint(componentSrc, pSrc, component);
					component.dispatchEvent(new java.awt.event.MouseEvent(component, eSrc.getID(), eSrc.getWhen(), eSrc.getModifiers() + eSrc.getModifiersEx(), pComponent.x, pComponent.y, eSrc.getClickCount(), eSrc.isPopupTrigger()));
				}
			}
		}
	}

	@Override
	protected void handleMouseMoved(java.awt.event.MouseEvent e) {
		Page page = this.getCurrentPage();
		if( page != null ) {
			for( Note note : page.getNotes() ) {
				if( note.isActive() ) {
					for( Feature feature : note.getFeatures() ) {
						java.awt.Shape shape = feature.getShape( TutorialStencil.this, null );
						if( shape != null ) {
							if( shape.contains( e.getX(), e.getY() ) ) {
								this.setEnteredFeature(feature);
								return;
							}
						}
					}
				}
			}
		}
		this.setEnteredFeature( null );
	}

	@Override
	protected void paintComponentPrologue( java.awt.Graphics2D g2 ) {
		Page page = getCurrentPage();
		if( isPaintingStencilEnabled() ) {
			if( page != null ) {
				if( page.isStencilRenderingDesired() ) {
					java.awt.geom.Area area = new java.awt.geom.Area(g2.getClip());
					for( Note note : page.getNotes() ) {
						if( note.isActive() ) {
							for( Feature feature : note.getFeatures() ) {
								org.lgna.croquet.components.TrackableShape trackableShape = feature.getTrackableShape();
								if( trackableShape != null ) {
									if( trackableShape.isInView() ) {
										java.awt.geom.Area featureArea = feature.getAreaToSubstractForPaint( TutorialStencil.this );
										if( featureArea != null ) {
											area.subtract( featureArea );
										}
									} else {
										if( feature.isPotentiallyScrollable() ) {
											org.lgna.croquet.components.ScrollPane scrollPane = trackableShape.getScrollPaneAncestor();
											if( scrollPane != null ) {
												javax.swing.JScrollBar scrollBar = scrollPane.getAwtComponent().getVerticalScrollBar();
												java.awt.Rectangle rect = javax.swing.SwingUtilities.convertRectangle(scrollBar.getParent(), scrollBar.getBounds(), TutorialStencil.this.getAwtComponent() );
												area.subtract( new java.awt.geom.Area( rect ) );
											} else {
												System.err.println( "cannot find scroll pane for: " + feature );
											}
										}
									}
								} else {
									feature.unbind();
									feature.bind();
								}
							}
						}
					}
					g2.setPaint( stencilPaint );
					g2.fill(area);
				}
			}
		}
	}

	@Override
	protected void paintComponentEpilogue( java.awt.Graphics2D g2 ) {
		Page page = getCurrentPage();
		if( isPaintingStencilEnabled() ) {
			if( page != null ) {
				for( Note note : page.getNotes() ) {
					if( note.isActive() ) {
						for( Feature feature : note.getFeatures() ) {
							feature.paint( g2, TutorialStencil.this, note );
						}
					}
				}
			}
		}
	}

	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2 ) {
		Page page = getCurrentPage();
		if( isPaintingStencilEnabled() ) {
			if( page != null ) {
				for( Note note : page.getNotes() ) {
					if( note.isActive() ) {
						for( Feature feature : note.getFeatures() ) {
							org.lgna.croquet.components.TrackableShape trackableShape = feature.getTrackableShape();
							if( trackableShape != null ) {
								if( trackableShape.isInView() ) {
									//pass
								} else {
									if( scrollRenderer != null ) {
										java.awt.Shape repaintShape = scrollRenderer.renderScrollIndicators( g2, TutorialStencil.this, trackableShape );
										if( repaintShape != null ) {
											//todo: repaint?
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected boolean contains( int x, int y, boolean superContains ) {
		Page page = getCurrentPage();
		if( page != null ) {
			java.awt.geom.Area area = new java.awt.geom.Area(new java.awt.Rectangle(0, 0, this.getWidth(), this.getHeight()));
			for( Note note : page.getNotes() ) {
				if( note.isActive() ) {
					for( Feature feature : note.getFeatures() ) {
						org.lgna.croquet.components.TrackableShape trackableShape = feature.getTrackableShape();
						if( trackableShape != null ) {
							if( trackableShape.isInView() ) {
								java.awt.geom.Area featureArea = feature.getAreaToSubstractForContains( TutorialStencil.this );
								if( featureArea != null ) {
									area.subtract( featureArea );
								}
							} else {
								if( feature.isPotentiallyScrollable() ) {
									org.lgna.croquet.components.ScrollPane scrollPane = trackableShape.getScrollPaneAncestor();
									if( scrollPane != null ) {
										javax.swing.JScrollBar scrollBar = scrollPane.getAwtComponent().getVerticalScrollBar();
										java.awt.Rectangle rect = javax.swing.SwingUtilities.convertRectangle(scrollBar.getParent(), scrollBar.getBounds(), TutorialStencil.this.getAwtComponent() );
										area.subtract( new java.awt.geom.Area( rect ) );
									} else {
										System.err.println( "cannot find scroll pane for: " + feature );
									}
								}
							}
						}
					}
				}
			}
			return area.contains(x, y);
		} else {
			return superContains;
		}
	}

	public java.awt.Paint getStencilPaint() {
		int width = 8;
		int height = 8;
		java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF );
		g2.setColor( STENCIL_BASE_COLOR );
		g2.fillRect( 0, 0, width, height );
		g2.setColor( STENCIL_LINE_COLOR );
		g2.drawLine( 0, height, width, 0 );
		g2.fillRect( 0, 0, 1, 1 );
		g2.dispose();
		return new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
	}

	public void setEnteredFeature(Feature enteredFeature) {
		if( this.enteredFeature != enteredFeature ) {
			if( this.enteredFeature != null ) {
				this.enteredFeature.setEntered( false );
				java.awt.Rectangle bounds = this.enteredFeature.getBoundsForRepaint( this );
				if( bounds != null ) {
					this.getAwtComponent().repaint( bounds );
				}
			}
			this.enteredFeature = enteredFeature;
			if( this.enteredFeature != null ) {
				this.enteredFeature.setEntered( true );
				java.awt.Rectangle bounds = this.enteredFeature.getBoundsForRepaint( this );
				if( bounds != null ) {
					this.getAwtComponent().repaint( bounds );
				}
			}
		}
	}
}
