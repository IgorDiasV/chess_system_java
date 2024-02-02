package boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;
    
    public Board(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    public int getRows(){
        return rows;
    }
    public int getColumns(){
        return this.columns;
    }

    public Piece piece(int row, int column){
        return this.pieces[row][column];
    }

    public Piece piece(Position position){
        int row = position.getRow();
        int column = position.getColumn();
        return this.pieces[row][column];
    }
}
