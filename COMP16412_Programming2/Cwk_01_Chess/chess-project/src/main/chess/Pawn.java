package chess;
import java.util.*;

public class Pawn extends Piece{
	private PieceColour colour;
	private String symbol;

	public Pawn(PieceColour pc){
		if (pc.equals(PieceColour.WHITE)){
			this.colour=PieceColour.WHITE;
			this.symbol="♙";
		}
		else if (pc.equals(PieceColour.BLACK)){
			this.colour=PieceColour.BLACK;
			this.symbol="♟";
		}
	}

	public String getSymbol(){
		return symbol;
	}
	public PieceColour getColour(){
		return colour;
	}
 
	@Override
	public boolean isLegitMove(int i0, int j0, int i1, int j1) {

		//j is x and i is y ???

		//check if the piece doesn't move
		if ((j0 == j1) && (i0 == i1)) {return false;}

		//if j changes, check if destination has piece
		if (j0 != j1) {
			if (Math.abs(j0 - j1) != 1) {return false;}
			else {
				if (Board.hasPiece(i1, j1)) {
					if (Board.getPiece(i1, j1).getColour().equals(Board.getPiece(i0, j0).getColour())) {return false;}
					else {return true;}
				} else {return false;}
			}
		}

		//check for direction
		if (Board.getPiece(i0, j0).getColour().equals(PieceColour.WHITE)) {
			if (i1 > i0) {return false;}
		} else if (Board.getPiece(i0, j0).getColour().equals(PieceColour.BLACK)) {
			if (i0 > i1) {return false;}
		}

		//checking for range of movement, first move = 2, else = 1
		if (i0 == 1 || i0 == 6) {
			if (Math.abs(i0 - i1) > 2) {return false;}
		} else {
			if (Math.abs(i0 - i1) > 1) {return false;}
		}

		//checking if there is any blocking
		if (Board.getPiece(i0, j0).getColour().equals(PieceColour.WHITE)) {
			for (int i = 1; i < (i1 - i0); i++) {
				if(Board.hasPiece(i0 - i, j0)) {return false;}
			}
		} else if (Board.getPiece(i0, j0).getColour().equals(PieceColour.BLACK)) {
			for (int i = 1; i < (i0 - i1); i++) {
				if(Board.hasPiece(i0 + i, j0)) {return false;}
			}
		}

		//check if there is any piece at destination
		if (Board.hasPiece(i1, j1)) {return false;}

		//return true if all requirements met
		return true;
	}


}
