package ui;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
		
		this.setLayout(new FlowLayout());
		
		JPanel buttons = new JPanel(new GridLayout(0, 1));
		
		calibrate = new JButton("Calibrate");
		calibrate.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		calibrate.addActionListener(new ReCalibrate());
		buttons.add(calibrate);
		
		test = new JButton("Test");
		test.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		test.addActionListener(new TestString());
		buttons.add(test);
		
		visualization = new Visualization();
		visualization.setPreferredSize(new Dimension(500, 500));
		visualization.init();
		
		
		this.add(buttons);
		this.add(visualization);
		
		this.pack();
		this.setSize(1920, 1080);
		this.setVisible(true);
	}

}
