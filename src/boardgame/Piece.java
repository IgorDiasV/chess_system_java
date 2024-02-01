package boardgame;

public abstract class Piece {
    protected Position position;
    private Board board;

    public Piece(Board board){
        this.board = board;
    }
    
    public Board getBoard(){
        return this.board;
    }
    
    public boolean possibleMove(Position position){
        return false;
    }

    public boolean isThereAnyPossibleMove(){
        return false;
    }
    

}
