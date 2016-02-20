import java.awt.Color;
import java.awt.Font;
import java.lang.Character;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
*	Extends JLabel, simulates a planet in SpaceThreads program
* 
*	Planet object has ID and Text-color, and synchronized method to 
*	communicate with Ship-objects with threads.
*
*	@author Hannes Nyberg
*	@version 15.10.21
*/

public class Planet extends JLabel{

		//	constants

		private final int PLANET_FONT_SIZE = 65;

		//	fields

		private Color planetColor;
		private Random planetRand;
		private int planetNumber;

		//	constructor

		/**
		*	Creates a Planet instance with planet ID/index
		*
		*	@param planetNumberIndex planet ID/index
		*/

		public Planet(int planetNumberIndex){

			planetNumber = planetNumberIndex;

			//	set icon and color
			setIcon(new ImageIcon("planet2.png"));
			planetRand = new Random();
			planetColor = new Color(	//	make sure ot too dark
					0.2f + planetRand.nextFloat()*0.8f,
					0.2f + planetRand.nextFloat()*0.8f,
					0.2f + planetRand.nextFloat()*0.8f);
			setBackground(Color.BLACK);
			setForeground(planetColor);

			//	set name (convert to text with (char) + 65)
			setText(Character.toString((char)((planetNumber) + 65)));
			setFont(new Font(
				Font.MONOSPACED, Font.BOLD, PLANET_FONT_SIZE));
			setHorizontalTextPosition(JLabel.CENTER);
			setVerticalTextPosition(JLabel.CENTER);

			setOpaque(true);
		}

		//	getters

		/** 
		*	Returns planet's text color
		*
		*	@return planet foreground color
		*/

		public Color getColor(){
			return planetColor;
		}

		/**
		*	Calls Ship's methods to move to planet, pause thread, move back
		*	and add Planet's ID and color to inventory.
		*	
		*	@param ship Ship-object that will be manipulated
		*/

		public synchronized void letShipScavenge(Ship ship){
			ship.pauseThread();
			ship.setTargetPlanet(this);
			while (!ship.isShipAtLocation()){
				System.out.print("");	//	wait till location is reached
				}
			ship.pauseThread();
			ship.setTargetHome();
			ship.addToInventory(planetNumber, planetColor);
			while (!ship.isShipAtLocation()){
				System.out.print("");	//	wait till location is reached
				}
		}
	}