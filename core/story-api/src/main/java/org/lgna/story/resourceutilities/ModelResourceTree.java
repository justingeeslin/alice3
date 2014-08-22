package org.lgna.story.resourceutilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserPackage;
import org.lgna.story.resources.ModelResource;

public class ModelResourceTree {

	private Map<Object, ModelResourceTreeNode> resourceClassToNodeMap = new HashMap<Object, ModelResourceTreeNode>();

	private final ModelResourceTreeNode galleryTree;
	private List<ModelResourceTreeNode> smodelBasedClasses = new ArrayList<ModelResourceTreeNode>();

	public ModelResourceTree( List<Class<? extends org.lgna.story.resources.ModelResource>> classes )
	{
		this.galleryTree = this.createClassTree( classes );
	}

	public ModelResourceTreeNode getTree()
	{
		return this.galleryTree;
	}

	public List<ModelResourceTreeNode> getSModelBasedNodes() {
		return this.smodelBasedClasses;
	}

	public List<ModelResourceTreeNode> getRootNodes()
	{
		LinkedList<ModelResourceTreeNode> rootNodes = new LinkedList<ModelResourceTreeNode>();
		if( this.galleryTree != null )
		{
			for( int i = 0; i < this.galleryTree.getChildCount(); i++ )
			{
				rootNodes.add( (ModelResourceTreeNode)this.galleryTree.getChildAt( i ) );
			}
		}
		return rootNodes;
	}

	public ModelResourceTreeNode getGalleryResourceTreeNodeForUserType( org.lgna.project.ast.AbstractType<?, ?, ?> type )
	{
		return this.galleryTree.getDescendantOfUserType( type );
	}

	public ModelResourceTreeNode getGalleryResourceTreeNodeForJavaType( org.lgna.project.ast.AbstractType<?, ?, ?> type )
	{
		return this.galleryTree.getDescendantOfJavaType( type );
	}

	public List<? extends ModelResourceTreeNode> getGalleryResourceChildrenForJavaType( org.lgna.project.ast.AbstractType<?, ?, ?> type )
	{
		ModelResourceTreeNode node = this.galleryTree.getDescendantOfJavaType( type );
		if( node != null )
		{
			return node.childrenList();
		}
		return new LinkedList<ModelResourceTreeNode>();
	}

	//The Stack<Class<?>> classes is a stack of classes representing the hierarchy of the classes, with the parent class at the top of the stack
	private ModelResourceTreeNode addNodes( ModelResourceTreeNode root, Stack<Class<? extends org.lgna.story.resources.ModelResource>> classes )
	{
		Class<?> rootClass = null;
		ModelResourceTreeNode currentNode = root;
		while( !classes.isEmpty() )
		{
			Class<? extends org.lgna.story.resources.ModelResource> currentClass = classes.pop();
			if( currentClass.isAnnotationPresent( Deprecated.class ) ) {
				continue;
			}

			//The root class is the one at the top of the stack, so grab it the first time around
			if( rootClass == null )
			{
				rootClass = currentClass;
			}
			ModelResourceTreeNode parentNode = currentNode;
			ModelResourceTreeNode classNode = null;
			if( resourceClassToNodeMap.containsKey( currentClass ) )
			{
				classNode = resourceClassToNodeMap.get( currentClass );
			}
			//Build a new ModelResourceTreeNode for the current class
			if( classNode == null )
			{

				NamedUserType aliceType = null;
				String aliceClassName = org.lgna.story.implementation.alice.AliceResourceClassUtilities.getAliceClassName( currentClass );
				UserPackage packageName = org.lgna.story.implementation.alice.AliceResourceClassUtilities.getAlicePackage( currentClass, rootClass );

				UserMethod[] noMethods = {};
				UserField[] noFields = {};
				Field[] resourceConstants = org.lgna.story.implementation.alice.AliceResourceClassUtilities.getFieldsOfType( currentClass, org.lgna.story.resources.ModelResource.class );
				Class<? extends org.lgna.story.SModel> modelClass = org.lgna.story.implementation.alice.AliceResourceClassUtilities.getModelClassForResourceClass( currentClass );
				org.lgna.project.ast.AbstractType parentType = null;
				if( ( parentNode == null ) || ( parentNode.getUserType() == null ) )
				{
					parentType = JavaType.getInstance( modelClass );
				}
				else
				{
					parentType = parentNode.getUserType();
				}
				NamedUserConstructor[] noConstructors = {};
				aliceType = new NamedUserType( aliceClassName, packageName, parentType, noConstructors, noMethods, noFields );

				classNode = new ModelResourceTreeNode( aliceType, currentClass, modelClass );
				resourceClassToNodeMap.put( currentClass, classNode );
				if( root == null ) //if the root node passed in is null, assign it to be the node from the first class we process
				{
					root = classNode;
				}
				if( classNode.hasModelClass() ) {
					this.smodelBasedClasses.add( classNode );
				}
				if( resourceConstants.length != 0 )
				{
					for( Field f : resourceConstants )
					{
						String fieldClassName = org.lgna.story.implementation.alice.AliceResourceClassUtilities.getClassNameFromName( f.getName() ) + aliceClassName;
						NamedUserType subParentType = classNode.getUserType();
						NamedUserType fieldType = new NamedUserType( fieldClassName, packageName, subParentType, noConstructors, noMethods, noFields );
						ModelResourceTreeNode fieldNode = new ModelResourceTreeNode( fieldType, currentClass, null );
						try
						{
							ModelResource resource = (ModelResource)f.get( null );
							fieldNode.setModelResourceInstance( resource );
							JavaField javaField = JavaField.getInstance( f );
							fieldNode.setJavaField( javaField );
						} catch( Exception e )
						{
							e.printStackTrace();
						}
						fieldNode.setParent( classNode );
						resourceClassToNodeMap.put( f, fieldNode );
					}
				}

			}
			classNode.setParent( parentNode );
			currentNode = classNode;
		}
		return root;
	}

	private ModelResourceTreeNode createClassTree( List<Class<? extends org.lgna.story.resources.ModelResource>> classes )
	{
		ModelResourceTreeNode topNode = new ModelResourceTreeNode( null, null, null );

		for( Class<? extends org.lgna.story.resources.ModelResource> cls : classes )
		{
			Class<? extends org.lgna.story.resources.ModelResource> currentClass = cls;
			Stack<Class<? extends org.lgna.story.resources.ModelResource>> classStack = new Stack<Class<? extends org.lgna.story.resources.ModelResource>>();
			Class<?>[] interfaces = null;
			while( currentClass != null )
			{
				classStack.push( currentClass );
				boolean isTopLevelResource = org.lgna.story.implementation.alice.AliceResourceClassUtilities.isTopLevelResource( currentClass );
				if( isTopLevelResource )
				{
					break;
				}

				interfaces = currentClass.getInterfaces();
				currentClass = null;
				if( ( interfaces != null ) && ( interfaces.length > 0 ) )
				{
					for( Class<?> intrfc : interfaces ) {
						if( org.lgna.story.resources.ModelResource.class.isAssignableFrom( intrfc ) ) {
							currentClass = (Class<? extends org.lgna.story.resources.ModelResource>)intrfc;
							break;
						}
					}
				}
			}
			addNodes( topNode, classStack );
		}
		return topNode;
	}

}
