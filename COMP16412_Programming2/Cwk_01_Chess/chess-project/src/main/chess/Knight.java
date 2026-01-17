package chess;

public class Knight extends Piece{
	private PieceColour colour;
	private String symbol;

 	public Knight(PieceColour pc){
		if (pc.equals(PieceColour.WHITE)){
			this.colour=PieceColour.WHITE;
			this.symbol="♘";
		}
		else if (pc.equals(PieceColour.BLACK)){
			this.colour=PieceColour.BLACK;
			this.symbol="♞";
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
		//check of piece doesn't move
		if ((i0 == i1) && (j0 == j1)) {return false;}

		//get piece colour
		PieceColour color = Board.getPiece(i0, j0).getColour();

		//checking for range of movement
		//if vertical == 2, horizontal == 1
		//if vertical ==1, horizontal == 2
		if ((Math.abs(j0 - j1) > 2) || (Math.abs(i0 - i1) > 2)) {return false;}
		if ((Math.abs(j0 - j1) == 2) &&  (Math.abs(i0 - i1) != 1)) {return false;}
		if ((Math.abs(j0 - j1) == 1) &&  (Math.abs(i0 - i1) != 2)) {return false;}
		if ((Math.abs(i0 - i1) == 2) &&  (Math.abs(j0 - j1) != 1)) {return false;}
		if ((Math.abs(i0 - i1) == 1) &&  (Math.abs(j0 - j1) != 2)) {return false;}

		//check if destination has piece
		//if same colour then false
		if (color.equals(PieceColour.WHITE)) {
			if (Board.hasPiece(i1, j1)) {
				if (Board.getPiece(i1, j1).getColour().equals(PieceColour.WHITE)){return false;}
			}
		} else if (color.equals(PieceColour.BLACK)) {
			if (Board.hasPiece(i1, j1)) {
				if (Board.getPiece(i1, j1).getColour().equals(PieceColour.BLACK)){return false;}
			}
		}

		//return true if all requirements passed
		return true;
	}
}
