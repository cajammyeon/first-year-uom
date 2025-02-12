package chess;

public class CheckInput {
	
	public static boolean checkCoordinateValidity(String input){
		//first value is from 1 to 8, representing i
		//second value is from a to h, representing j

		//return true if empty string passes
		if (input.equals("")) {return false;}

		//check if the length is correct
		if (input.length() != 2) {return false;}

		char i = input.charAt(0);
		char j = input.charAt(1);

		//check if the first char in range of 1 to 8
			//return false if out of boundary 
		//check if the second char in range of a to h
			//return false if out of boundary

		if ((i < '1') || (i > '8')) {return false;}
		if ((j < 'a') || (j > 'h')) {return false;}

		//return true as default
		return true;
	}
}
