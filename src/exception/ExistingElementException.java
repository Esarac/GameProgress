package exception;

public class ExistingElementException extends Exception{

	//Constructor
	public ExistingElementException() {
		super("This element already exist.");
	}
	
	public ExistingElementException(String message) {
		super(message);
	}
	
}
