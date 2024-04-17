package chess;

public class King extends Piece{
	private PieceColour colour;
	private String symbol;

	public King(PieceColour pc){
		if (pc.equals(PieceColour.WHITE)){
			this.colour=PieceColour.WHITE;
			this.symbol="♔";
		}
		else if (pc.equals(PieceColour.BLACK)){
			this.colour=PieceColour.BLACK;
			this.symbol="♚";
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
		
		//check if the piece doesn't move
		if ((j0 == j1) && (i0 == i1)) {return false;}

		//get piece colour
		PieceColour color = Board.getPiece(i0, j0).getColour();

		//checking magnitude and direction
		if ((Math.abs(i0 - i1) > 1) || (Math.abs(j0 - j1) > 1)) {return false;}

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

		//return true as default
		return true;
	}

}
