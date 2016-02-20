import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
*	Space Threads simulates how a number of Ship-objects share
*	and explore Planet-objects. A planet only handles one Ship
*	at a time.
*
* @author Hannes Nyberg
* @version 15.10.21
*/

public class SpaceThreads extends JFrame{

	//	constants

	private final int 	WINDOW_WIDTH = 800;
	private final int	WINDOW_HEIGHT = 600;
	private final int	NUMBER_OF_SHIPS = 4;
	private final int	NUMBER_OF_PLANETS = 3;
	private final int	SPACE_V_GAP = 90;
	private final int	SPACE_H_GAP = 100;
	private final int	MAT_V_GAP = 20;
	private final int	MAT_H_GAP = 30;

	//	declarations

	private int shipDimension;
	private JPanel 	topPanel, mainPanel, materialStripPanel, 
					spacePanel, bottomPanel;
	private JPanel[] materialPanels;
	private Planet[] planets;
	private Ship[] ships;

	// constructor

	private SpaceThreads()
	{
		initSpaceThreads();
	}

	// initializer

	private void initSpaceThreads()
	{
		//	set window

		setTitle("Space Threads");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setResizable(false);
		setFocusable(true);

		//	set panels

		mainPanel = new JPanel();
		mainPanel.setLayout(
			new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		add(mainPanel);

		materialStripPanel = new JPanel();
		materialStripPanel.setLayout(
			new FlowLayout(
				FlowLayout.CENTER, MAT_H_GAP, MAT_V_GAP));
		materialStripPanel.setBackground(Color.BLACK);
		mainPanel.add(materialStripPanel);

		spacePanel = new JPanel();
		spacePanel.setLayout(
			new FlowLayout(
				FlowLayout.CENTER, SPACE_H_GAP, SPACE_V_GAP));
		spacePanel.setBackground(Color.BLACK);
		mainPanel.add(spacePanel);

		//	set material panels

		materialPanels = new JPanel[NUMBER_OF_SHIPS];

		for (int i = 0; i < NUMBER_OF_SHIPS; i++){

			materialPanels[i] = new JPanel();
			materialPanels[i].setBackground(Color.BLACK);
			materialStripPanel.add(materialPanels[i]);
		}

		//	create planets

		planets = new Planet[NUMBER_OF_PLANETS];
		for (int i = 0; i < NUMBER_OF_PLANETS; i++){
			planets[i] = new Planet(i);
		}

		//	create and set out ships

		ships = new Ship[NUMBER_OF_SHIPS];
		for (int i = 0; i < NUMBER_OF_SHIPS; i++){
			ships[i] = new Ship(planets, (i + 1));
			spacePanel.add(ships[i]);
		}

		//	set out planets

		for (int i = 0; i < NUMBER_OF_PLANETS; i++){
			spacePanel.add(planets[i]);
		}

		//	set out material legends and score board

		for (int i = 0; i < NUMBER_OF_SHIPS; i++){
			for (int j = 0; j < NUMBER_OF_PLANETS; j++){
				materialPanels[i].add(ships[i].getLegends()[j]);
			}
			// materialPanels[i].add(ships[i].getScoreBoard());
		}

		//	activate ships (start threads)

		for (int i = 0; i < NUMBER_OF_SHIPS; i++){
			
			ships[i].getThread().start();
		}
				
		setVisible(true);

		for (int i = 0; i < NUMBER_OF_SHIPS; i++){
			ships[i].setHomeLocation();
		}
	}

	//	main

	/**
	*	Runs Space Threads program
	*
	*	@param args string arguments
	*/ 
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new SpaceThreads();
			}
		});
	}
}