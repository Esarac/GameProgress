package model;

public class Achievement {
	
	//Attribute
	private String name;
	private boolean completed;
	
	//Constructor
	public Achievement(String name, boolean completed) {
		System.out.println("		"+name+" "+completed);
		this.name=name;
		this.completed=false;
		
	}
	
	//Method
	//Set
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	//Get
	public String getName() {
		return name;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public String toString() {
		return name;
	}
	
}
