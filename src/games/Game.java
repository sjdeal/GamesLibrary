/*
 * File: Game.java;
 */
package games;

import java.io.Serializable;

/**
 * This interface defines basic methods for a launchable game.
 * 
 * @author Sean Deal
 * @version November 1, 2013
 */
public interface Game extends Serializable{
  
  /**
   * Returns the filepath of this game.
   * 
   * @return the filepath of this game.
   */
  public String getFilepath();
  
  /**
   * Returns the name of this game.
   * 
   * @return the name of this game.
   */
  public String getName();
  
  /**
   * Returns the console of this game.
   * 
   * @return the console of this game.
   */
  public String getConsole();
  
  public String getGenre();
  
  public int getYear();
  
  public boolean isVisible();
  
  /**
   * Sets the filepath of this game to a given filepath.
   * 
   * @param  filepath  the filepath to set this game's filepath to.
   */
  public void setFilepath(String filepath);
  
  /**
   * Sets the name of this game to a given name.
   * 
   * @param  name  the name to set this game's name to.
   */
  public void setName(String name);
  
  public void setGenre(String genre);
  
  public void setYear(int year);
  
  public void setVisibility(boolean visible);
  /**
   * Launches this game.
   */
  public void launch();
  
  /**
   * Returns a String representation of this game.
   * 
   * @return the name of the game.
   */
  public String toString();
}