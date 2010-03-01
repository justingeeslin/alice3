package edu.cmu.cs.dennisc.memory;

public class MemoryPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	private static final long K = 1024;
	private static final long M = K*K;

	private MemoryView memoryView = new MemoryView();
	private javax.swing.JLabel label0 = edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( "0" );
	private javax.swing.JLabel labelMax = edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel();
	
	public MemoryPane() {
		java.lang.management.MemoryMXBean memory = java.lang.management.ManagementFactory.getMemoryMXBean();
		java.lang.management.MemoryUsage heapUsage = memory.getHeapMemoryUsage();
		long maxMB = heapUsage.getMax()/M;
		labelMax.setText( maxMB + "MB" );

		this.add( memoryView, java.awt.BorderLayout.CENTER );
		edu.cmu.cs.dennisc.croquet.swing.BorderPane labels = new edu.cmu.cs.dennisc.croquet.swing.BorderPane();
		labels.add( label0, java.awt.BorderLayout.WEST );
		labels.add( labelMax, java.awt.BorderLayout.EAST );
		this.add( labels, java.awt.BorderLayout.SOUTH );
		this.setPreferredSize( new java.awt.Dimension( 300, 80 ) );
	}
	
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( new MemoryPane() );
		frame.setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );
		frame.setVisible( true );
	}
}
