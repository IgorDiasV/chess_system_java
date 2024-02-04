package boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;
    
    public Board(int rows, int columns){
        if(rows < 1 || columns < 1){
            throw new BoardException("Error creating board:  Rows and Columns must be greater than zero.");
        }
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
        if(!positionExists(row, column)){
            throw new BoardException("Position not on the board");
        }
        return this.pieces[row][column];
    }

    public Piece piece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Position not on the board");
        }
        int row = position.getRow();
        int column = position.getColumn();
        return this.pieces[row][column];
    }

    public void placePiece(Piece piece, Position position){
        if (this.thereIsAPiece(position)){
            throw new BoardException("There is already a piece on position" + position);
        }
        int row = position.getRow();
        int column = position.getColumn();

        piece.position = position;
        this.pieces[row][column] = piece;

    }

    private boolean positionExists(int row, int column){
        return row >= 0 && row < this.rows && column >= 0 && column < columns ;
    }

    public boolean positionExists(Position position){
        int row = position.getRow();
        int column = position.getColumn();
        return this.positionExists(row, column);

    }   
    
    public boolean thereIsAPiece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Position not on the board");
        }
        return this.piece(position) != null;
    }
}
