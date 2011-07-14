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

package test;

/**
 * @author Dennis Cosgrove
 */
public class CreateBootstrap {
	private static edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice createType( String name, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > superType ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice rv = new edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice();
		rv.setName( name );
		rv.superType.setValue( superType );
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice constructor = new edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice();
		edu.cmu.cs.dennisc.alice.ast.ConstructorBlockStatement constructorBlockStatement = new edu.cmu.cs.dennisc.alice.ast.ConstructorBlockStatement();
		edu.cmu.cs.dennisc.alice.ast.SuperConstructorInvocationStatement superConstructorInvocationStatement = new edu.cmu.cs.dennisc.alice.ast.SuperConstructorInvocationStatement();
		superConstructorInvocationStatement.contructor.setValue( superType.getDeclaredConstructor() );
		constructorBlockStatement.constructorInvocationStatement.setValue( superConstructorInvocationStatement );
		constructor.body.setValue( constructorBlockStatement );
		rv.constructors.add( constructor );
		return rv;
	}
	private static edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice createType( String name, Class<?> superCls ) {
		return createType( name, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( superCls ) );
	}
	
	private static edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice createPrivateFinalField( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > valueType, String name ) {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rv = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice();
		rv.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );
		rv.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		rv.valueType.setValue( valueType );
		rv.name.setValue( name );
		rv.initializer.setValue( org.alice.ide.ast.NodeUtilities.createInstanceCreation( valueType ) );
		return rv;
	}
	private static edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice createPrivateFinalField( Class< ? > cls, String name ) {
		return createPrivateFinalField( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ), name );
	}
	
	private static edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice createMethod( edu.cmu.cs.dennisc.alice.ast.Access access, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > returnType, String name ) {
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice rv = new edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice();
		rv.access.setValue( access );
		rv.returnType.setValue( returnType );
		rv.name.setValue( name );
		rv.body.setValue( new edu.cmu.cs.dennisc.alice.ast.BlockStatement() );
		return rv;
	}
	private static edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice createMethod( edu.cmu.cs.dennisc.alice.ast.Access access, Class< ? > cls, String name ) {
		return createMethod( access, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ), name );
	}
	
	private static edu.cmu.cs.dennisc.alice.ast.FieldAccess createThisFieldAccess( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), field );
	}
	private static edu.cmu.cs.dennisc.alice.ast.ExpressionStatement createMethodInvocationStatement( edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, edu.cmu.cs.dennisc.alice.ast.Expression... argumentExpressions ) {
		return org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( expression, method, argumentExpressions );
	}
	
	private static edu.cmu.cs.dennisc.alice.ast.FieldAccess createFieldAccess( Enum<?> value ) {
		return org.alice.ide.ast.NodeUtilities.createStaticFieldAccess( value.getClass(), value.name() );
	}
	
	private static edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice createProgramType( org.lookingglassandalice.storytelling.Ground.Appearance appearance ) {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice cameraField = createPrivateFinalField( org.lookingglassandalice.storytelling.Camera.class, "camera" );
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sunField = createPrivateFinalField( org.lookingglassandalice.storytelling.Sun.class, "sun" );
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice groundField = createPrivateFinalField( org.lookingglassandalice.storytelling.Ground.class, "ground" );

		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice performGeneratedSetupMethod = createMethod( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE, Void.TYPE, "performGeneratedSetup" );
		edu.cmu.cs.dennisc.alice.ast.BlockStatement performGeneratedSetupBody = performGeneratedSetupMethod.body.getValue();
		
		for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice[] { cameraField, sunField, groundField } ) {
			java.lang.reflect.Method mthd;
			try {
				mthd = ((edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava)field.getValueType()).getClassReflectionProxy().getReification().getMethod( "setVehicle", org.lookingglassandalice.storytelling.Entity.class );
			} catch( NoSuchMethodException nsme ) {
				throw new RuntimeException( nsme );
			}
			edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava.get( mthd );
			//edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = field.getValueType().findMethod( "setVehicle", org.lookingglassandalice.storytelling.Entity.class );

			performGeneratedSetupBody.statements.add( 
					createMethodInvocationStatement( 
							createThisFieldAccess( field ), 
							method, 
							new edu.cmu.cs.dennisc.alice.ast.ThisExpression() 
					) 
			);
		}
		
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava setAppearanceMethod = edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava.get( org.lookingglassandalice.storytelling.Ground.class, "setAppearance", org.lookingglassandalice.storytelling.Ground.Appearance.class );
		//edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava setAppearanceMethod = groundField.getValueType().findMethod( "setAppearance", org.lookingglassandalice.storytelling.Ground.Appearance.class )
		
		performGeneratedSetupBody.statements.add( createMethodInvocationStatement( createThisFieldAccess( groundField ), setAppearanceMethod, createFieldAccess( appearance ) ) );

		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice performCustomSetupMethod = createMethod( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE, Void.TYPE, "performCustomSetup" );

		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice handleActiveChangedMethod = createMethod( edu.cmu.cs.dennisc.alice.ast.Access.PROTECTED, Void.TYPE, "handleActiveChanged" );
		edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice isActiveParameter = new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice( "isActive", Boolean.class );
		edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice activeCountParameter = new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice( "activeCount", Integer.class );
		handleActiveChangedMethod.parameters.add( isActiveParameter );
		handleActiveChangedMethod.parameters.add( activeCountParameter );

		edu.cmu.cs.dennisc.alice.ast.BlockStatement handleActiveChangedBody = handleActiveChangedMethod.body.getValue();
		
		edu.cmu.cs.dennisc.alice.ast.ConditionalStatement ifOuter = org.alice.ide.ast.NodeUtilities.createConditionalStatement( 
				new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( isActiveParameter ) 
		);
		edu.cmu.cs.dennisc.alice.ast.ConditionalStatement ifInner = org.alice.ide.ast.NodeUtilities.createConditionalStatement( 
				new edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression( 
						new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( activeCountParameter ), 
						edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.EQUALS, 
						new edu.cmu.cs.dennisc.alice.ast.IntegerLiteral( 1 ), 
						Integer.class, 
						Integer.class 
				)
		);
		edu.cmu.cs.dennisc.alice.ast.BlockStatement ifOuterTrueBody = ifOuter.booleanExpressionBodyPairs.get( 0 ).body.getValue(); 
		edu.cmu.cs.dennisc.alice.ast.BlockStatement ifInnerTrueBody = ifInner.booleanExpressionBodyPairs.get( 0 ).body.getValue(); 
		edu.cmu.cs.dennisc.alice.ast.BlockStatement ifInnerFalseBody = ifInner.elseBody.getValue(); 
		edu.cmu.cs.dennisc.alice.ast.BlockStatement ifOuterFalseBody = ifOuter.elseBody.getValue(); 

		ifOuterTrueBody.statements.add( ifInner );

		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), performGeneratedSetupMethod ) );
		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), performGeneratedSetupMethod ) );

		//Class< ? > sceneCls = org.lookingglassandalice.storytelling.Scene.class;
		Class< ? > sceneCls = SceneAdapter.class;
		System.err.println( "todo: add vm support for protected methods via adapter?" );
		
		
		
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava preserveVehiclesAndVantagePointsMethod = edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava.get( sceneCls, "preserveVehiclesAndVantagePoints" );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava restoreVehiclesAndVantagePointsMethod = edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava.get( sceneCls, "restoreVehiclesAndVantagePoints" );
		ifInnerFalseBody.statements.add( createMethodInvocationStatement( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), restoreVehiclesAndVantagePointsMethod ) );
		ifOuterFalseBody.statements.add( createMethodInvocationStatement( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), preserveVehiclesAndVantagePointsMethod ) );

		handleActiveChangedBody.statements.add( ifOuter );
		
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = createType( "MyScene", org.lookingglassandalice.storytelling.Scene.class );
		sceneType.fields.add( cameraField );
		sceneType.fields.add( sunField );
		sceneType.fields.add( groundField );
		sceneType.methods.add( performCustomSetupMethod );
		sceneType.methods.add( performGeneratedSetupMethod );
		sceneType.methods.add( handleActiveChangedMethod );

		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField = createPrivateFinalField( sceneType, "myScene" );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice playOutStoryMethod = createMethod( edu.cmu.cs.dennisc.alice.ast.Access.PUBLIC, Void.TYPE, "playOutStory" );
		edu.cmu.cs.dennisc.alice.ast.BlockStatement playOutStoryBody = playOutStoryMethod.body.getValue();
		playOutStoryBody.statements.add( 
				createMethodInvocationStatement( 
						new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), 
						edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava.get( org.lookingglassandalice.storytelling.Program.class, "setActiveScene", org.lookingglassandalice.storytelling.Scene.class ),
						createThisFieldAccess( sceneField )
				)
		);
		
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = createType( "MyProgram", org.lookingglassandalice.storytelling.Program.class );
		programType.fields.add( sceneField );
		programType.methods.add( playOutStoryMethod );
		return programType;
	}
	
	public static void main( String[] args ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = createProgramType( org.lookingglassandalice.storytelling.Ground.Appearance.GRASS );
		edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
		vm.registerAnonymousAdapter( org.lookingglassandalice.storytelling.Scene.class, SceneAdapter.class );
		Object programInstance = vm.createInstanceEntryPoint( programType );
		vm.invokeEntryPoint( programType.findMethod( "initializeInFrame", String[].class ), programInstance, (Object)args );
		vm.invokeEntryPoint( programType.methods.get( 0 ), programInstance );
	}
}
