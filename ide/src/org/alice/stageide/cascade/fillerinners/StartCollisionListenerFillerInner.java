package org.alice.stageide.cascade.fillerinners;

public class StartCollisionListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public StartCollisionListenerFillerInner() {
		super( org.lgna.story.event.StartCollisionListener.class );
	}
	@Override
	public java.util.List< org.lgna.croquet.CascadeBlankChild > addItems( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.project.annotations.ValueDetails< ? > details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		rv.add( org.alice.stageide.croquet.models.cascade.adapters.StartCollisionEventAdapterFillIn.getInstance() );
		return rv;
	}
}
