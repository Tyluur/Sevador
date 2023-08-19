package com.sevador.utility.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

/**
 * Pops up a loading bar.
 * @author Emperor
 *
 */
public class ProgressBar extends JFrame implements ActionListener {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 7813244613302059059L;

	/**
	 * The progress bar used.
	 */
	private	final JProgressBar progress;
	
	/**
	 * The button.
	 */
	private	final JButton button;
	
	/**
	 * The label.
	 */
	private	final JLabel label;
	
	/**
	 * The top panel.
	 */
	private	final JPanel topPanel;
	
	/**
	 * The start time in milliseconds.
	 */
	@SuppressWarnings("unused")
	private long start = System.currentTimeMillis();
	
	/**
	 * If the application has paused.
	 */
	public boolean paused = false;
	
	/**
	 * Constructs a new {@code ProgressBar} {@code Object}.
	 * @param name The application name.
	 * @param length The length.
	 */
	public ProgressBar(String name, int length) {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		setTitle(name);
		setSize(310, 130);
		setBackground(Color.gray);
		topPanel = new JPanel();
		topPanel.setPreferredSize( new Dimension( 310, 130 ) );
		getContentPane().add( topPanel );
		label = new JLabel("Commencing session...");
		label.setPreferredSize(new Dimension( 280, 24 ));
		topPanel.add(label);
		progress = new JProgressBar();
		progress.setPreferredSize(new Dimension(300, 20));
		progress.setMinimum(0);
		progress.setMaximum(length);
		progress.setValue(0);
		progress.setBounds(20, 35, 260, 20);
		topPanel.add(progress);
		button = new JButton("Pause");
		topPanel.add(button);
		button.addActionListener( this );
	}
	
	/**
	 * Updates the progress bar status.
	 * @param current The current amount.
	 */
	public void updateStatus(int current) {
		try {
			progress.setValue(current);
			progress.setString(new StringBuilder().
					append(formatDouble(progress.getPercentComplete() * 1000D)).append("%").toString());
			progress.setStringPainted(true);
				label.setText(new StringBuilder("Dumped ").append(progress.getValue()).
						append(" definitions...").toString());
			/*progress.setValue(current);
			double percent = progress.getPercentComplete();
			double estimatedTime = (System.currentTimeMillis() - start) * (100 / percent);
			int hours = (int) (estimatedTime / 3600000);
			int minutes = (int) ((estimatedTime / 60000) - (hours * 60));
			progress.setString(new StringBuilder().
					append(formatDouble(progress.getPercentComplete() * 1000D)).append("%").toString());
			progress.setStringPainted(true);
			if (hours > 0) {
				label.setText(new StringBuilder("Dumped ").append(progress.getValue()).
						append(" definitions, estimated time: ").append(hours).append("h ").append(minutes).append("m.").toString());
			} else {
				label.setText(new StringBuilder("Dumped ").append(progress.getValue()).
						append(" definitions, estimated time: ").append(minutes).append(" minutes.").toString());
			}*/
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event != null && event.getSource() == button) {
			paused = !paused;
			if (paused) {
				button.setText("Start");
			} else {
				button.setText("Pause");
			}
 		}
	}
	
	private double formatDouble(double d) {
		try {
			DecimalFormat df = new DecimalFormat("###,#");
			return Double.parseDouble(df.format(d));
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}
	
	public static void main(String[] args) {
		new ProgressBar("Lol", 10);
	}
	
}
