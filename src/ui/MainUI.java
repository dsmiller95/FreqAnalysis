package ui;

import java.applet.Applet;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

/**
 * Main window which will control the flow of the program when under manual control
 * @author millerds
 *
 */

/*
 * TODO:
 * add in buttons for calibration and test initiation
 * Aggregate into Main
 * Rewrite calibration method in Main to work on event(button) triggers
 */

public class MainUI extends JFrame{
	
	public class ReCalibrate implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		}
	}
	
	public class TestString implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		}
	}
	
	private JButton calibrate, test;
	private Applet visualization;
	
	public MainUI() {
		this.setTitle("Frequency Scale");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setLayout(new MigLayout());

		calibrate = new JButton("Calibrate");
		calibrate.addActionListener(new ReCalibrate());
		this.add(calibrate, "cell 0 0");
		
		test = new JButton("Test");
		test.addActionListener(new TestString());
		this.add(calibrate, "cell 0 1");
		
		visualization = new Visualization();
		visualization.init();
		this.add(visualization, "cell 1 0 1 2");
		
		this.pack();
		this.setVisible(true);
	}

}
