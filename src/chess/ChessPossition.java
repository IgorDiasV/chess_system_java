package chess;

import boardgame.Position;

public class ChessPossition {
    private char column;
    private int row;

    public ChessPossition(char column, int row){
            if (column < 'a' || column > 'h' || row < 1 || row > 8){
                throw new ChessException("Error instantiang ChessPosition. Valid values are from a1 to h8");
            }
            this.column = column;
            this.row = row;
    }

    protected Position toPosition(){
        return new Position(8 - this.row, this.column - 'a');
    }

    protected static ChessPossition fromPosition(Position position){
        return new ChessPossition((char)(position.getColumn() - 'a'), 8 - position.getRow());

    }

    @Override
    public String toString(){
        return "" + column + row;
    }
    
}
