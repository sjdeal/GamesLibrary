/*
 * File: PCGame.java
 */

package games;

import java.lang.Runtime;
import java.lang.Process;
/**
 * This class, which implements the Game interface, contains methods
 * and instance variables for a PC game.
 * 
 * @author Sean Deal
 * @version November 3, 2013
 */
public class PCGame implements Game{
  
  /**
   * The filepath of the game's EXE file.
   */
  public String filepath;
  
  /**
   * The name of the game.
   */
  public String name;
  
  public String genre;
  
  public int year;
  
  public boolean visible = true;
  
  /**
   * Creates a new PCGame with all instance variables
   * set to be empty.
   */
  public PCGame(){
    filepath = "";
    name = "";
    genre = "";
    year = 0;
  }
 
  public PCGame(String filepath, String name, String genre, int year){
    this.filepath = filepath;
    this.name = name;
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
    return "PC";
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
   * game's EXE file.
   */
  public void launch(){
    try{
      Runtime.getRuntime().exec(this.getFilepath());
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