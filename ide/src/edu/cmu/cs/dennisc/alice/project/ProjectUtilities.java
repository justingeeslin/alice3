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
package edu.cmu.cs.dennisc.alice.project;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProjectUtilities {
	private ProjectUtilities() {
	}

	public static java.util.Set< org.alice.virtualmachine.Resource > getReferencedResources( edu.cmu.cs.dennisc.alice.Project project ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> programType = project.getProgramType();
		java.util.Set< org.alice.virtualmachine.Resource > resources = project.getResources();
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ResourceExpression > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ResourceExpression >( edu.cmu.cs.dennisc.alice.ast.ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression ) {
				return true;
			}
		};
		programType.crawl( crawler, true );
		
		java.util.Set< org.alice.virtualmachine.Resource > rv = new java.util.HashSet< org.alice.virtualmachine.Resource >();
		for( edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression : crawler.getList() ) {
			org.alice.virtualmachine.Resource resource = resourceExpression.resource.getValue();
			if( resources.contains( resource ) ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: adding missing resource", resource );
				resources.add( resource );
			}
			rv.add( resource );
		}
		return rv;
	}

	public static final String PROJECT_EXTENSION = "a3p";
	public static final String TYPE_EXTENSION = "a3c";
	private static String PROPERTIES_ENTRY_NAME = "properties.bin";
	private static String PROGRAM_TYPE_ENTRY_NAME = "programType.xml";
	private static String VERSION_ENTRY_NAME = "version.txt";
	private static String TYPE_ENTRY_NAME = "type.xml";
	private static String RESOURCES_ENTRY_NAME = "resources.xml";

	private static String XML_RESOURCE_TAG_NAME = "resource";

	private static String XML_RESOURCE_CLASSNAME_ATTRIBUTE = "className";
	private static String XML_RESOURCE_UUID_ATTRIBUTE = "uuid";
	private static String XML_RESOURCE_ENTRY_NAME_ATTRIBUTE = "entryName";

	public static java.io.File getMyAliceDirectory( String applicationName ) {
		java.io.File rv = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), applicationName );
		rv.mkdirs();
		return rv;
	}
	public static java.io.File getMyProjectsDirectory( String applicationName ) {
		java.io.File rv = new java.io.File( getMyAliceDirectory( applicationName ), "MyProjects" );
		rv.mkdirs();
		return rv;
	}
	@Deprecated
	public static java.io.File getMyProjectsDirectory() {
		return getMyProjectsDirectory( "Alice3" );
	}
	
	public static java.io.File[] listProjectFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles( directory, PROJECT_EXTENSION );
	}
	public static java.io.File[] listTypeFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles( directory, TYPE_EXTENSION );
	}

	private static interface ZipEntryContainer {
		public java.io.InputStream getInputStream( String name ) throws java.io.IOException;
	}
	
	private static class ZipFileEntryContainer implements ZipEntryContainer {
		private final java.util.zip.ZipFile zipFile;
		public ZipFileEntryContainer( java.util.zip.ZipFile zipFile ) {
			this.zipFile = zipFile;
		}
		public java.io.InputStream getInputStream( String name ) throws java.io.IOException {
			java.util.zip.ZipEntry zipEntry = this.zipFile.getEntry( name );
			if( zipEntry != null ) {
				return this.zipFile.getInputStream( zipEntry );
			} else {
				return null;
			}
		}
	}
	private static class ZipInputStreamEntryContainer implements ZipEntryContainer {
		private final java.util.Map< String, byte[] > mapZipEntryToBuffer;
		public ZipInputStreamEntryContainer( java.util.zip.ZipInputStream zipInputStream ) {
			try {
				this.mapZipEntryToBuffer = edu.cmu.cs.dennisc.zip.ZipUtilities.extract( zipInputStream );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
		public java.io.InputStream getInputStream( String name ) throws java.io.IOException {
			byte[] buffer = this.mapZipEntryToBuffer.get( name );
			if( buffer != null ) {
				return new java.io.ByteArrayInputStream( buffer );
			} else {
				return null;
			}
		}
	}
	
	private static String readVersion( ZipEntryContainer zipEntryContainer ) throws java.io.IOException {
		assert zipEntryContainer != null;
		java.io.InputStream is = zipEntryContainer.getInputStream( VERSION_ENTRY_NAME );
		if( is != null ) {
			//todo?
			java.util.ArrayList< Byte > buffer = new java.util.ArrayList< Byte >( 32 );
			while( true ) {
				int b = is.read();
				if( b != -1 ) {
					buffer.add( (byte)b );
				} else {
					break;
				}
			}
			byte[] array = new byte[ buffer.size() ];
			int i = 0;
			for( Byte b : buffer ) {
				array[ i++ ] = b;
			}
			return new String( array );
		} else {
			throw new java.io.IOException( zipEntryContainer.toString() + " does not contain entry " + VERSION_ENTRY_NAME );
		}
	}
	private static edu.cmu.cs.dennisc.alice.Project readProperties( edu.cmu.cs.dennisc.alice.Project rv, ZipEntryContainer zipEntryContainer ) throws java.io.IOException {
		assert zipEntryContainer != null;
		java.io.InputStream is = zipEntryContainer.getInputStream( PROPERTIES_ENTRY_NAME );
		if( is != null ) {
			java.io.BufferedInputStream bis = new java.io.BufferedInputStream( is );
			rv.readProperties( bis );
		}
		return rv;
	}
	private static org.w3c.dom.Document readXML( ZipEntryContainer zipEntryContainer, String entryName ) throws java.io.IOException {
		assert zipEntryContainer != null;
		java.io.InputStream is = zipEntryContainer.getInputStream( entryName );
		return edu.cmu.cs.dennisc.xml.XMLUtilities.read( is );
	}

	private static edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice readType( ZipEntryContainer zipEntryContainer, String entryName ) throws java.io.IOException {
		String version = readVersion( zipEntryContainer );
		org.w3c.dom.Document xmlDocument = readXML( zipEntryContainer, entryName );
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)edu.cmu.cs.dennisc.alice.ast.AbstractNode.decode( xmlDocument, version );
	}
	private static java.util.Set< org.alice.virtualmachine.Resource > readResources( ZipEntryContainer zipEntryContainer ) throws java.io.IOException {
		java.util.Set< org.alice.virtualmachine.Resource > rv = new java.util.HashSet< org.alice.virtualmachine.Resource >();
		java.io.InputStream isResources = zipEntryContainer.getInputStream( RESOURCES_ENTRY_NAME );
		if( isResources != null ) {
			org.w3c.dom.Document xmlDocument = edu.cmu.cs.dennisc.xml.XMLUtilities.read( isResources );
			java.util.List< org.w3c.dom.Element > xmlElements = edu.cmu.cs.dennisc.xml.XMLUtilities.getChildElementsByTagName( xmlDocument.getDocumentElement(), XML_RESOURCE_TAG_NAME );
			for( org.w3c.dom.Element xmlElement : xmlElements ) {
				String className = xmlElement.getAttribute( XML_RESOURCE_CLASSNAME_ATTRIBUTE );
				String uuidText = xmlElement.getAttribute( XML_RESOURCE_UUID_ATTRIBUTE );
				String entryName = xmlElement.getAttribute( XML_RESOURCE_ENTRY_NAME_ATTRIBUTE );
				if( className != null && uuidText != null && entryName != null ) {
					byte[] data = edu.cmu.cs.dennisc.java.io.InputStreamUtilities.getBytes( zipEntryContainer.getInputStream( entryName ) );
					if( data != null ) {
						try {
							Class< ? extends org.alice.virtualmachine.Resource > resourceCls = (Class< ? extends org.alice.virtualmachine.Resource >)edu.cmu.cs.dennisc.java.lang.ClassUtilities.forName( className );
							org.alice.virtualmachine.Resource resource = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.valueOf( resourceCls, uuidText );
							resource.decodeAttributes( xmlElement, data );
							rv.add( resource );
						} catch( ClassNotFoundException cnfe ) {
							edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: no class for name:", className );
						}
					} else {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: no data for resource:", entryName );
					}
				}
			}
		}
		return rv;
	}

	private static edu.cmu.cs.dennisc.alice.Project readProject( ZipEntryContainer zipEntryContainer ) throws java.io.IOException {
		assert zipEntryContainer != null;
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = readType( zipEntryContainer, PROGRAM_TYPE_ENTRY_NAME );
		java.util.Set< org.alice.virtualmachine.Resource > resources = readResources( zipEntryContainer );
		edu.cmu.cs.dennisc.alice.Project rv = new edu.cmu.cs.dennisc.alice.Project( type, resources );
		readProperties( rv, zipEntryContainer );
		return rv;
	}
	public static edu.cmu.cs.dennisc.alice.Project readProject( java.util.zip.ZipInputStream zis ) throws java.io.IOException {
		return readProject( new ZipInputStreamEntryContainer( zis ) );
	}
	public static edu.cmu.cs.dennisc.alice.Project readProject( java.io.InputStream is ) throws java.io.IOException {
		return readProject( new java.util.zip.ZipInputStream( is ) );
	}
	public static edu.cmu.cs.dennisc.alice.Project readProject( java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		return readProject( new ZipFileEntryContainer( zipFile ) );
	}
	public static edu.cmu.cs.dennisc.alice.Project readProject( java.io.File file ) {
		assert file != null;
		assert file.exists();
		try {
			return readProject( new java.util.zip.ZipFile( file ) );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}
	public static edu.cmu.cs.dennisc.alice.Project readProject( String path ) {
		assert path != null;
		return readProject( new java.io.File( path ) );
	}

	private static edu.cmu.cs.dennisc.pattern.Tuple2< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, java.util.Set< org.alice.virtualmachine.Resource > > readType( ZipEntryContainer zipEntryContainer ) throws java.io.IOException {
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = readType( zipEntryContainer, TYPE_ENTRY_NAME );
		java.util.Set< org.alice.virtualmachine.Resource > resources = readResources( zipEntryContainer );
		return edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( type, resources );
	}

	public static edu.cmu.cs.dennisc.pattern.Tuple2< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, java.util.Set< org.alice.virtualmachine.Resource > > readType( java.util.zip.ZipInputStream zis ) throws java.io.IOException {
		return readType( new ZipInputStreamEntryContainer( zis ) );
	}
	public static edu.cmu.cs.dennisc.pattern.Tuple2< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, java.util.Set< org.alice.virtualmachine.Resource > > readType( java.io.InputStream is ) throws java.io.IOException {
		return readType( new java.util.zip.ZipInputStream( is ) );
	}

	public static edu.cmu.cs.dennisc.pattern.Tuple2< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, java.util.Set< org.alice.virtualmachine.Resource > > readType( java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		return readType( new ZipFileEntryContainer( zipFile ) );
	}
	public static edu.cmu.cs.dennisc.pattern.Tuple2< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, java.util.Set< org.alice.virtualmachine.Resource >> readType( java.io.File file ) {
		assert file != null;
		assert file.exists();
		try {
			return readType( new java.util.zip.ZipFile( file ) );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}
	public static edu.cmu.cs.dennisc.pattern.Tuple2< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, java.util.Set< org.alice.virtualmachine.Resource >> readType( String path ) {
		return readType( new java.io.File( path ) );
	}

	private static void writeVersion( java.util.zip.ZipOutputStream zos ) throws java.io.IOException {
		edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.DataSource() {
			public String getName() {
				return VERSION_ENTRY_NAME;
			}
			public void write( java.io.OutputStream os ) throws java.io.IOException {
				os.write( edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText().getBytes() );
			}
		} );
	}
	private static void writeXML( final org.w3c.dom.Document xmlDocument, java.util.zip.ZipOutputStream zos, final String entryName ) throws java.io.IOException {
		edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.DataSource() {
			public String getName() {
				return entryName;
			}
			public void write( java.io.OutputStream os ) throws java.io.IOException {
				edu.cmu.cs.dennisc.xml.XMLUtilities.write( xmlDocument, os );
			}
		} );
	}
	private static void writeType( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, java.util.zip.ZipOutputStream zos, String entryName ) throws java.io.IOException {
		writeXML( type.encode(), zos, entryName );
	}
	private static void writeDataSources( java.util.zip.ZipOutputStream zos, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		for( edu.cmu.cs.dennisc.zip.DataSource dataSource : dataSources ) {
			edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, dataSource );
		}
	}

	private static String getValidName( String name ) {
		//todo
		return name;
	}
	private static String generateEntryName( org.alice.virtualmachine.Resource resource, java.util.Set< String > usedEntryNames ) {
		String validFilename = getValidName( resource.getOriginalFileName() );
		final String DESIRED_DIRECTORY_NAME = "resources";
		int i = 1;
		while( true ) {
			StringBuffer sb = new StringBuffer();
			sb.append( DESIRED_DIRECTORY_NAME );
			if( i > 1 ) {
				sb.append( i );
			}
			sb.append( "/" );
			sb.append( validFilename );
			String potentialEntryName = sb.toString();
			if( usedEntryNames.contains( potentialEntryName ) ) {
				i += 1;
			} else {
				return potentialEntryName;
			}
		}
	}
	private static void writeResources( java.util.zip.ZipOutputStream zos, java.util.Set< org.alice.virtualmachine.Resource > resources ) throws java.io.IOException {
		if( resources.isEmpty() ) {
			//pass
		} else {
			org.w3c.dom.Document xmlDocument = edu.cmu.cs.dennisc.xml.XMLUtilities.createDocument();
			org.w3c.dom.Element xmlRootElement = xmlDocument.createElement( "root" );
			xmlDocument.appendChild( xmlRootElement );
			synchronized( resources ) {
				java.util.Set< String > usedEntryNames = new java.util.HashSet< String >();
				for( org.alice.virtualmachine.Resource resource : resources ) {
					org.w3c.dom.Element xmlElement = xmlDocument.createElement( XML_RESOURCE_TAG_NAME );
					resource.encodeAttributes( xmlElement );
					java.util.UUID uuid = resource.getUUID();
					assert uuid != null;

					xmlElement.setAttribute( XML_RESOURCE_CLASSNAME_ATTRIBUTE, resource.getClass().getName() );
					xmlElement.setAttribute( XML_RESOURCE_UUID_ATTRIBUTE, uuid.toString() );

					String entryName = generateEntryName( resource, usedEntryNames );
					usedEntryNames.add( entryName );
					xmlElement.setAttribute( XML_RESOURCE_ENTRY_NAME_ATTRIBUTE, entryName );
					xmlRootElement.appendChild( xmlElement );
				}
			}
			writeXML( xmlDocument, zos, RESOURCES_ENTRY_NAME );
			synchronized( resources ) {
				java.util.Set< String > usedEntryNames = new java.util.HashSet< String >();
				for( org.alice.virtualmachine.Resource resource : resources ) {
					String entryName = generateEntryName( resource, usedEntryNames );
					usedEntryNames.add( entryName );
					edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.ByteArrayDataSource( entryName, resource.getData() ) );
				}
			}
		}
	}

	public static void writeProject( java.io.OutputStream os, final edu.cmu.cs.dennisc.alice.Project project, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( os );
		writeVersion( zos );
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> programType = project.getProgramType();
		writeType( programType, zos, PROGRAM_TYPE_ENTRY_NAME );
		if( project.getPropertyKeySet().isEmpty() ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.DataSource() {
				public String getName() {
					return PROPERTIES_ENTRY_NAME;
				}
				public void write( java.io.OutputStream os ) throws java.io.IOException {
					assert os instanceof java.io.BufferedOutputStream;
					project.writeProperties( (java.io.BufferedOutputStream)os );
				}
			} );
		}
		writeDataSources( zos, dataSources );

		java.util.Set< org.alice.virtualmachine.Resource > resources = project.getResources();

		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ResourceExpression > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ResourceExpression >( edu.cmu.cs.dennisc.alice.ast.ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression ) {
				return true;
			}
		};
		programType.crawl( crawler, true );

		for( edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression : crawler.getList() ) {
			org.alice.virtualmachine.Resource resource = resourceExpression.resource.getValue();
			if( resources.contains( resource ) ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: adding missing resource", resource );
				resources.add( resource );
			}
		}

		writeResources( zos, resources );

		zos.flush();
		zos.close();
	}
	public static void writeProject( java.io.File file, edu.cmu.cs.dennisc.alice.Project project, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		writeProject( new java.io.FileOutputStream( file ), project, dataSources );
	}

	public static void writeProject( String path, edu.cmu.cs.dennisc.alice.Project project, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		writeProject( new java.io.File( path ), project, dataSources );
	}

	public static void writeType( java.io.OutputStream os, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( os );
		writeVersion( zos );
		writeType( type, zos, TYPE_ENTRY_NAME );
		writeDataSources( zos, dataSources );

		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ResourceExpression > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.ResourceExpression >( edu.cmu.cs.dennisc.alice.ast.ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression ) {
				return true;
			}
		};
		type.crawl( crawler, false );
		java.util.Set< org.alice.virtualmachine.Resource > resources = new java.util.HashSet< org.alice.virtualmachine.Resource >();
		for( edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression : crawler.getList() ) {
			resources.add( resourceExpression.resource.getValue() );
		}
		writeResources( zos, resources );

		zos.flush();
		zos.close();
	}
	public static void writeType( java.io.File file, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		writeType( new java.io.FileOutputStream( file ), type, dataSources );
	}
	public static void writeType( String path, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		writeType( new java.io.File( path ), type, dataSources );
	}

	public static <N extends edu.cmu.cs.dennisc.alice.ast.Node > N lookupNode( edu.cmu.cs.dennisc.alice.Project project, final java.util.UUID id ) {
		final edu.cmu.cs.dennisc.alice.ast.Node[] buffer = { null };
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = project.getProgramType();
		edu.cmu.cs.dennisc.pattern.Crawler crawler = new edu.cmu.cs.dennisc.pattern.Crawler() {
			public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
				if( crawlable instanceof edu.cmu.cs.dennisc.alice.ast.Node ) {
					edu.cmu.cs.dennisc.alice.ast.Node node = (edu.cmu.cs.dennisc.alice.ast.Node)crawlable;
					if( id.equals( node.getUUID() ) ) {
						buffer[ 0 ] = node;
					}
				}
			}
		};
		programType.crawl( crawler, true );
		return (N)buffer[ 0 ];
	}
	public static <N extends edu.cmu.cs.dennisc.alice.ast.Node > N decodeNode( edu.cmu.cs.dennisc.alice.Project project, edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		java.util.UUID id = binaryDecoder.decodeId();
		return (N)lookupNode( project, id );
	}
	public static void encodeNode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.alice.ast.Node node ) {
		binaryEncoder.encode( node.getUUID() );
	}
}
