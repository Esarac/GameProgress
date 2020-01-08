package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import exception.ExistingElementException;
import exception.ImpossiblePercentageException;

public class Manager implements Listable<Console>{
	
	//Attribute
	public static final String CONSOLES_PATH="dat/info/";
	public static final String STEAM_PATH="C:/Program Files (x86)/Steam/steamapps/common/";
	
	private String style;
	private ArrayList<Console> consoles;
	
	//Constructor
	public Manager() throws ExistingElementException, IOException, ImpossiblePercentageException {
		
		this.consoles=new ArrayList<Console>();
		loadConsoles();
	}
	
	//Method
	public void addConsole(String name) throws ExistingElementException{
		if(searchConsole(name)==null){
			consoles.add(new Console(name));
		}
		else{
			throw new ExistingElementException();
		}
	}
	
	public void addConsole(Console console) throws ExistingElementException{
		if(searchConsole(console.getName())==null){
			consoles.add(console);
		}
		else{
			throw new ExistingElementException();
		}
	}
	
	public void deleteConsole(String name) {
		boolean found=false;
		
		for(int i=0; (i<consoles.size()) && (!found); i++){
			if(name.equals(consoles.get(i).getName())){
				consoles.remove(i);
				found=true;
			}
		}
	}
	
	public void editNameConsole(String actualName, String newName) throws ExistingElementException{
		Console console=searchConsole(actualName);
		if(searchConsole(newName)==null){
			console.setName(newName);
		}
		else{
			throw new ExistingElementException();
		}
	}
	
	public Console searchConsole(String name) {
		Console console=null;
		
		boolean found=false;
		for(int i=0; (i<consoles.size()) && (!found); i++){
			if(name.equals(consoles.get(i).getName())){
				console=consoles.get(i);
				found=true;
			}
		}
		
		return console;
	}
	
	//Load
	public void loadConsoles() throws ExistingElementException, IOException, ImpossiblePercentageException{
		
		File folder = new File(CONSOLES_PATH);
		for (final File fileEntry : folder.listFiles()) {
			Console console=new Console(fileEntry.getName());
			
			console.loadGames(CONSOLES_PATH+console.getName()+"/");
			addConsole(console);
	    }
		
	}
	
	//Get
	public ArrayList<Console> getList(){
		return consoles;
	}
	
}
