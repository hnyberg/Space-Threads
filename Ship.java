import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.lang.Character;
import java.lang.Integer;
import java.lang.Math;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
*	Extends JLabel, implements Runnable, simulates a ship in SpaceThreads program
* 
*	Ship object (thread) has ID and home location, and inventory
*	and scoreboard to keep track of planets to visit.
*
*	@author Hannes Nyberg
*	@version 15.10.21
*/

public class Ship extends JLabel implements Runnable{

		//	constants

		private final int STEP = 5;
		private final int LEGEND_SIZE = 35;
		private final int LEGEND_FONT_SIZE = 20;
		private final int SHIP_FONT_SIZE = 30;
		private final int TIME_INTERVAL = 10;
		private final int PAUS_TIME_INTERVAL = 500;

		//	fields

		private boolean isAtLocation, inventoryFull, threadEnd;
		private boolean[] inventory;
		private double direction;
		private int homeX, homeY, targetX, targetY, score, 
					numberOfPlanets, targetPlanetIndex;
		private JLabel[] legends;
		private JLabel scoreBoard;
		private Planet[] planets;
		private Thread thread;
		private Timer timer;

		//	constructor

		/**
		*	Creates a Ship instance with ID and array of target planets
		*
		*	@param inputPlanets array of target Planets for Ship
		*	@param shipNumber Ship's ID-number
		*/

		public Ship(Planet[] inputPlanets, int shipNumber){

			//	set home location and reset target location

			homeX = this.getX();
			homeY = this.getY();
			targetX = homeX;
			targetY = homeY;

			//	save planet data

			numberOfPlanets = inputPlanets.length;
			planets = new Planet[numberOfPlanets];
			for (int i = 0; i < numberOfPlanets; i++){
				planets[i] = inputPlanets[i];
			}

			//	set and clear inventory

			inventory = new boolean[numberOfPlanets];
			for (int i = 0; i < planets.length; i++){
				inventory[i] = false;
			}

			//	set material legends

			legends = new JLabel[numberOfPlanets];
			for (int i = 0; i < numberOfPlanets; i++){
				legends[i] = new JLabel(
					shipNumber + "" + 
					Character.toString((char)(i + 65)));
				legends[i].setFont(new Font(
					Font.MONOSPACED, Font.BOLD, LEGEND_FONT_SIZE));
				legends[i].setPreferredSize(new Dimension(
					LEGEND_SIZE, LEGEND_SIZE));
				legends[i].setBackground(Color.BLACK);
				legends[i].setForeground(Color.BLACK);
				legends[i].setOpaque(true);
			}

			//	set scoreBoard

			score = 0;
			scoreBoard = new JLabel("" + score);
			scoreBoard.setFont(new Font(
				Font.MONOSPACED, Font.BOLD, LEGEND_FONT_SIZE));
			scoreBoard.setPreferredSize(new Dimension(
				LEGEND_SIZE, LEGEND_SIZE));
			scoreBoard.setBackground(Color.BLACK);
			scoreBoard.setForeground(Color.WHITE);
			scoreBoard.setOpaque(true);

			//	set icon, color (B&W) and text

			setIcon(new ImageIcon("astronaut2.png"));
			setText(Integer.toString(shipNumber));
			setFont(new Font(
				Font.MONOSPACED, Font.BOLD, SHIP_FONT_SIZE));
			setHorizontalTextPosition(JLabel.CENTER);
			setVerticalTextPosition(JLabel.CENTER);
			setBackground(Color.BLACK);
			setForeground(Color.BLACK);

			//	initiate variables

			threadEnd = false;
			isAtLocation = true;
			inventoryFull = false;
			direction = 0;
			targetPlanetIndex = 0;

			//	set timer for moving

			timer = new Timer(TIME_INTERVAL, new ActionListener(){
				public void actionPerformed(ActionEvent event){
					
					if (!isAtLocation){
						move();
					}
				}
			});
			timer.start();

			//	create thread
			thread = new Thread(this);

			setOpaque(true);
		}

		//	run method

		/**
		*	starts Runnable-thread
		*/

		public void run(){
			while(!threadEnd){

				//	before scavenge, check inventory

				inventoryFull = true;
				for (int i = numberOfPlanets - 1; i >= 0 ; i--){
					if (inventory[i] == false){
						targetPlanetIndex = i;
						inventoryFull = false;
					}
				}
				//	if full inventory, clear and scavenge first planet
				if (inventoryFull){
					pauseThread();	//	inventory visible for short time
					emptyInventory();
					addScore();
					targetPlanetIndex = 0;	//	start at 0
				}
				
				//	call synchronized method in Planet
				planets[targetPlanetIndex].letShipScavenge(this);
			}
		}

		//	getters

		/**	
		*	returns JLabel[] legends
		*
		*	@return JLabel[] legends
		*/

		public JLabel[] getLegends(){
			return legends;
		}

		/**	
		*	returns JLabel scoreBoard
		*
		*	@return JLabel scoreBoard
		*/

		public JLabel getScoreBoard(){
			return scoreBoard;
		}

		/**	
		*	returns Thread thread
		*
		*	@return Thread thread
		*/

		public Thread getThread(){
			return thread;
		}

		/**	
		*	returns boolean isAtLocations
		*
		*	@return boolean isAtLocations
		*/

		public boolean isShipAtLocation(){
			return isAtLocation;
		}

		//	setters

		/**	
		*	adds score and updates scoreBoard
		*/

		public void addScore(){
			score++;
			scoreBoard.setText("" + score);
		}

		/**	
		*	updates Ship's home location to current location
		*/

		public void setHomeLocation(){
			homeX = getX();
			homeY = getY();
		}

		/**	
		*	add Planet to inventory
		*
		*	@param index index of planet to be added to inventory
		*	@param color color of added Planet
		*/

		public void addToInventory(int index, Color color){
			inventory[index] = true;
			legends[index].setBackground(color);
		}

		/**	
		*	empties inventory
		*/

		public void emptyInventory(){
			for (int i = 0; i < numberOfPlanets; i++){
				inventory[i] = false;
				legends[i].setBackground(Color.BLACK);
			}
		}

		/**	
		*	pauses Thread for a short time
		*/

		public void pauseThread(){
			try {
				Thread.sleep(PAUS_TIME_INTERVAL);
			}
			catch (InterruptedException e){}
		}

		/**	
		*	sets Ship's next target Planet
		*
		*	@param inputPlanet ship's next targeted Planet-object
		*/

		public void setTargetPlanet(Planet inputPlanet){
			isAtLocation = false;
			targetX = 	inputPlanet.getX() 
						+ (inputPlanet.getWidth() / 2)
						- (getWidth() / 2);
			targetY = 	inputPlanet.getY() 
						- (getHeight());
		}

		/**	
		*	sets Ship's target location to return home
		*/

		public void setTargetHome(){
			isAtLocation = false;
			targetX = homeX;
			targetY = homeY;
		}

		/**	
		*	updates Ship's location towards target location till close enough 
		*/

		public void move(){

			//	if close to target, park ship and stop move
			if (	Math.abs(targetX - getX()) < STEP
				&&	Math.abs(targetY - getY()) < STEP)
			{
				setLocation(targetX, targetY);
				isAtLocation = true;
			}

			direction = Math.atan2(
				targetY - getY(), 
				targetX - getX());

			setLocation(
				getX() + (int)(STEP * Math.cos(direction)),
				getY() + (int)(STEP * Math.sin(direction)));
		}
	}