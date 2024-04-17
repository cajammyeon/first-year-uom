package chess;

public class Rook extends Piece{
	private PieceColour colour;
	private String symbol;

	public Rook(PieceColour pc){
		if (pc.equals(PieceColour.WHITE)){
			this.colour=PieceColour.WHITE;
			this.symbol="♖";
		}
		else if (pc.equals(PieceColour.BLACK)){
			this.colour=PieceColour.BLACK;
			this.symbol="♜";
		}
	}

	public String getSymbol(){
		return symbol;
	}
	public PieceColour getColour(){
		return colour;
	}

	public boolean isLegitMove(int i0, int j0, int i1, int j1) {

		//check if the piece doesn't move
		if ((j0 == j1) && (i0 == i1)) {return false;}

		//if both x and y changes then return false
		if ((i0 != i1) && (j0 != j1)) {return false;}

		//check if destination has piece
		//if same colour then false
		if (Board.getPiece(i0, j0).getColour().equals(PieceColour.WHITE)) {
			if (Board.hasPiece(i1, j1)) {
				if (Board.getPiece(i1, j1).getColour().equals(PieceColour.WHITE)) {return false;}
			}
		} else if (Board.getPiece(i0, j0).getColour().equals(PieceColour.BLACK)) {
			if (Board.hasPiece(i1, j1)) {
				if (Board.getPiece(i1, j1).getColour().equals(PieceColour.BLACK)) {return false;}
			}
		}

		//checking blocking
		if (j1 > j0) { //N
			for (int i = 1; i < Math.abs(j1 - j0); i++) {
				if (Board.hasPiece(i0, j0 + i)) {return false;}
			}
		} else if (j1 < j0) { //S
			for (int i = 1; i < Math.abs(j1 - j0); i++) {
				if (Board.hasPiece(i0, j0 - i)) {return false;}
			}
		} else if (i1 > i0) { //E
			for (int i = 1; i < Math.abs(i1 - i0); i++) {
				if (Board.hasPiece(i0 + i, j0)) {return false;}
			}
		} else if (i1 < i0) { //W
			for (int i = 1; i < Math.abs(i1 - i0); i++) {
				if (Board.hasPiece(i0 - i, j0)) {return false;}
			}
		}

		//return true as default
		return true;

	}
}
