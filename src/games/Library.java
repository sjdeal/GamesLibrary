package games;

import java.util.*;
import java.io.Serializable;

public class Library implements Serializable{
  
  private Vector<Game> games;
  private Vector<Emulator> emulators;
  
  //Constructor
  public Library(){
    games = new Vector<Game>();
    emulators = new Vector<Emulator>();
  }
  
  //Methods to get the lists themselves
  public Vector<Game> getGamesList(){
    return this.games;
  }
  
  public Vector<Emulator> getEmulatorsList(){
    return this.emulators;
  }
  
  //Methods to add games and emulators
  public boolean addPCGame(String filepath, String name, String genre, int year){
    return games.add(new PCGame(filepath, name, genre, year));
  }
  
  public boolean addPCGame(PCGame game){
    return games.add(game);
  }
  
  public boolean addEmulatedGame(String filepath, String name, Emulator emulator,
                              String genre, int year){
    return games.add(new EmulatedGame(filepath, name, emulator, genre, year));
  }
  
  public boolean addEmulatedGame(EmulatedGame game){
    return games.add(game);
  }
  
  public boolean addEmulator(String console, String filepath, String arguments){
    return emulators.add(new Emulator(console, filepath, arguments));
  }
  
  public boolean addEmulator(Emulator emulator){
    return emulators.add(emulator);
  }
  
  //Methods to remove games and emulators
  public boolean removeGame(String name){
    return games.remove(getGame(name));
  }
  
  public boolean removeEmulator(String name){
    return emulators.remove(getEmulator(name));
  }
  
  //Methods to get individual games and emulators
  public Game getGame(String name){
    for(int i = 0; i < games.size(); i++){
      if(games.get(i).getName().equals(name)){
        return games.get(i);
      }
    }
    return null;
  }
  
  public Game getGameAt(int index){
    return games.get(index);
  }
  
  public Emulator getEmulator(String name){
    for(int i = 0; i < emulators.size(); i++){
      if(emulators.get(i).getConsole().equals(name)){
        return emulators.get(i);
      }
    }
    return null;
  }
  
  public Emulator getEmulatorAt(int index){
    return emulators.get(index);
  }
  
  //Methods to clear the lists
  public void clearGames(){
    games.clear();
  }
  
  public void clearEmulators(){
    emulators.clear();
  }
  
  //Methods to get the sizes of the lists
  public int getGamesSize(){
    return games.size();
  }
  
  public int getEmulatorsSize(){
    return emulators.size();
  }
  
  public boolean gamesIsEmpty(){
    return games.isEmpty();
  }
  
  public boolean emulatorsIsEmpty(){
    return emulators.isEmpty();
  }
}