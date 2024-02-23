package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece{
    private Color color;
    private int moveCount = 0;
    public ChessPiece(Board board, Color color){
        super(board);
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void  increasedMoveCount() {moveCount++;}

    public void  decreasedMoveCount() {moveCount--;}

    public int getMoveCount(){return moveCount;}
    
    public ChessPossition getChessPosition(){
        return ChessPossition.fromPosition(position);
    }
    protected boolean isThereOpponentPiece(Position position){
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p!=null && p.getColor() != color;
    }
}
