/*
 * File: Emulator.java
 */

package games;

import java.io.Serializable;

/**
 * This class contains methods for an emulator, which is
 * used to play ROM files for games of a certain console.
 * 
 * @author Sean Deal
 * @version November 2, 2013
 */
public class Emulator implements Serializable{
  
  /**
   * The console this emulator can play games of.
   */
  public String console;
  
  /**
   * The filepath of the emulator itself.
   */
  public String filepath;
  
  /**
   * Any arguments needed to launch a game directly.
   * In some cases, this may be an empty String.
   */
  public String[] arguments;
  
  /**
   * Creates a new Emulator with all instance variables
   * set to be empty.
   */
  public Emulator(){
    console = "";
    filepath = "";
    arguments = new String[]{};
  }
  
  /**
   * Creates a new Emulator with a given console, filepath,
   * and argument String.
   * 
   * @param  console  the console of this emulator
   * @param  filepath  the filepath of the emulator
   * @param  arguments  any launch arguments
   */
  public Emulator(String console, String filepath, String arguments){
    this.console = console;
    this.filepath = filepath;
    setArguments(arguments);
  }
  
  /**
   * Returns the console of this emulator.
   * 
   * @return the console of this emulator.
   */
  public String getConsole(){
    return this.console;
  }
  
  /**
   * Returns the filepath of this emulator.
   * 
   * @return the filepath of this emulator.
   */
  public String getFilepath(){
    return this.filepath;
  }
  
  /**
   * Returns the arguments for this emulator.
   * 
   * @return the arguments for this emulator.
   */
  public String[] getArguments(){
    return this.arguments;
  }
  
  /**
   * Sets the emulator's console to a given console name.
   * 
   * @param  console  the console of this emulator
   */
  public void setConsole(String console){
    this.console = console;
  }
  
  /**
   * Sets the emulator's filepath to a given filepath.
   * 
   * @param  filepath  the filepath of this emulator
   */
  public void setFilepath(String filepath){
    this.filepath = filepath;
  }
  
  /**
   * Sets the emulator's arguments to a given String of arguments.
   * 
   * @param  arguments  the launch arguments for this emulator
   */
  public void setArguments(String arguments){
    this.arguments = arguments.split(" ");
  }
  
  /**
   * Returns a String representation of this emulator.
   * 
   * @return the console of the emulator.
   */
  public String toString(){
    return this.getConsole();
  }
}