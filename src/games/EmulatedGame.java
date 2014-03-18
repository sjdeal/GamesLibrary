/*
 * File: EmulatedGame.java
 */

package games;

import java.lang.Runtime;
import java.lang.Process;
import java.util.ArrayList;
/**
 * This class, which implements the Game interface, contains methods
 * and instance variables for an emulated game.
 * 
 * @author Sean Deal
 * @version November 1, 2013
 */
public class EmulatedGame implements Game{
  
  /**
   * The filepath of the game's ROM file.
   */
  public String filepath;
  
  /**
   * The name of the game.
   */
  public String name;
  
  /**
   * The emulator that this game is played on.
   */
  public Emulator emulator;
  
  /**
   * The console of this game.
   */
  public String console;
  
  public String genre;
  
  public int year;
  
  public boolean visible = true;
  /**
   * Creates a new EmulatedGame with all instance variables
   * set to either be empty or null.
   */
  public EmulatedGame(){
    filepath = "";
    name = "";
    emulator = null;
    console = "";
    genre = "";
    year = 0;
  }
 
  public EmulatedGame(String filepath, String name, Emulator emulator,
                      String genre, int year){
    this.filepath = filepath;
    this.name = name;
    this.emulator = emulator;
    this.console = emulator.getConsole();
    this.genre = genre;
    this.year = year;
  }
  
  /**
   * Returns the filepath of this game.
   * 
   * @return the filepath of this game.
   */
  public String getFilepath(){
    return this.filepath;
  }
  /**
   * Returns the name of this game.
   * 
   * @return the name of this game.
   */
  public String getName(){
    return this.name;
  }
  
  /**
   * Returns the console of this game.
   * 
   * @return the console of this game.
   */
  public String getConsole(){
    return this.console;
  }
  
  /**
   * Returns the emulator used to run this game.
   * 
   * @return the emulator used to run this game.
   */
  public Emulator getEmulator(){
    return this.emulator;
  }
  
  public String getGenre(){
    return this.genre;
  }
  
  public int getYear(){
    return this.year;
  }
  
  public boolean isVisible(){
	  return this.visible;
  }
  
  /**
   * Sets the filepath of this game to a given filepath.
   * 
   * @param  filepath  the filepath to set this game's filepath to.
   */
  public void setFilepath(String filepath){
    this.filepath = filepath;
  }
  
  /**
   * Sets the name of this game to a given name.
   * 
   * @param  name  the name to set this game's name to.
   */
  public void setName(String name){
    this.name = name;
  }
  
  /**
   * Sets the emulator of this game to a given emulator.
   * 
   * @param  emulator  the emulator to set this game's emulator to.
   */
  public void setEmulator(Emulator emulator){
    this.emulator = emulator;
  }
  
  /**
   * Sets the game's console to a given console name.
   * 
   * @param  console  the console of this game
   */
  public void setConsole(String console){
    this.console = console;
  }
  
  public void setGenre(String genre){
    this.genre = genre;
  }
  
  public void setYear(int year){
    this.year = year;
  }
  
  public void setVisibility(boolean visible){
	  this.visible = visible;
  }
  
  /**
   * Launches the game by creating a new Process using the filepath of the
   * emulator, the emulator's required arguments, and the filepath of the
   * game's ROM file.
   */
  public void launch(){
    try{
    	String[] arguments = this.emulator.getArguments();
    	String s = "";
    	s += this.emulator.getFilepath();
    	for(int i = 0; i < arguments.length; i++){
    		s += " ";
    		if(arguments[i].equals("%ROM%")){
    			s += this.getFilepath();
    		}else if(arguments[i].equals("\"%ROM%\"")){
    			s += "\"" + this.getFilepath() + "\"";
    		}else{
    			s += arguments[i];
    		}
    	}
    	Runtime.getRuntime().exec(s);
    }catch(Exception e){
      System.out.println(e);
    }
  }
    
  /**
   * Returns a String representation of this game.
   * 
   * @return the name of the game.
   */
  public String toString(){
    return this.name;
  }
}