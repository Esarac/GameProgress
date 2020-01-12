package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import exception.ExistingElementException;
import exception.ImpossiblePercentageException;
import exception.InvalidCharException;

public class Manager implements Listable<Console>{
	
	//Attribute
	public static final String DATA_PATH="dat/";
	public static final String CONSOLES_PATH=DATA_PATH+"info/";
	public static final String STEAM_PATH="C:/Program Files (x86)/Steam/steamapps/common/";
	
	private String style;
	private ArrayList<Console> consoles;
	
	//Constructor
	public Manager() throws ExistingElementException, IOException, ImpossiblePercentageException, InvalidCharException {
		
		this.consoles=new ArrayList<Console>();
		loadConsoles();
	}
	
	//Method
	public void addConsole(String name) throws ExistingElementException, InvalidCharException{
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
	
	public void updateNameConsole(String actualName, String newName) throws ExistingElementException, InvalidCharException{
		Console console=searchConsole(actualName);
		
		if(console!=null) {
			if(searchConsole(newName)==null || actualName.equals(newName)){
				console.setName(newName);
			}
			else{
				throw new ExistingElementException();
			}
		}
		
	}
	
	public Console searchConsole(String name) {
		Console console=null;
		
		boolean found=false;
		for(int i=0; (i<consoles.size()) && (!found); i++){
			if(name.equalsIgnoreCase(consoles.get(i).getName())){
				console=consoles.get(i);
				found=true;
			}
		}
		
		return console;
	}
	
	//Save
	public void saveApp(){//[FILE]
		try{
			savePrevApps();
			for(int i=0; i<consoles.size(); i++) {
				consoles.get(i).saveConsole();
			}
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
	}
	
	public void savePrevApps(){//[FILE]
		File data=new File(DATA_PATH);
		
		File prevInfoFile=new File(DATA_PATH+"info-prev");
		if(prevInfoFile.exists()) {
			deleteFolder(prevInfoFile.getPath());
		}
		
		File prevInfo=new File(DATA_PATH+"info");
		File prevInfoNew=new File(DATA_PATH+"info-prev");
		System.out.println(prevInfo.renameTo(prevInfoNew));
		
		File info=new File(DATA_PATH+"info");
		info.mkdir();
	}
	
	//Delete[FILE]
	public void deleteFolder(String folderPath) {//[FILE]
		File folder=new File(folderPath);
		if(!folder.delete()){
			for (final File file : folder.listFiles()) {
				if(file.isFile()){
					file.delete();
				}
				else if(file.isDirectory()){
					deleteFolder(file.getPath());
				}
			}
			folder.delete();
		}
	}
	
	//Load
	public void loadConsoles() throws ExistingElementException, IOException, ImpossiblePercentageException, InvalidCharException{//[FILE]
		
		File folder = new File(CONSOLES_PATH);
		folder.mkdir();
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
