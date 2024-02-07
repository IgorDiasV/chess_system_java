package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;
public class ChessMatch{

    private Board board;

    public ChessMatch(){
        this.board = new Board(8, 8);
        this.initialSetup();
    }

    public ChessPiece[][] getChessPieces(){
        int rows = this.board.getRows();
        int columns = this.board.getColumns();
        ChessPiece[][] pieces = new ChessPiece[rows][columns];
        for(int i=0; i<rows; i++){          
            for(int j=0; j<columns; j++){
                pieces[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return pieces;
    }

    public ChessPiece performChessMove(ChessPossition sourceChessPossition, ChessPossition targetPossition){
        Position source = sourceChessPossition.toPosition();
        Position target = targetPossition.toPosition();
        ValidateSourcePosition(source);
        Piece capturedPiece = makeMove(source, target);
        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target){
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        return capturedPiece;

    }
    private void ValidateSourcePosition(Position position){
        if(!this.board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on the source position");
        }
    }
    public void placeNewPiece(char column, int row, ChessPiece piece){
        Position pos = new ChessPossition(column, row).toPosition();
        this.board.placePiece(piece, pos);

    }
    public void initialSetup(){

        placeNewPiece('a', 1, new Rook(this.board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(this.board, Color.WHITE));
        placeNewPiece('a', 8, new Rook(this.board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(this.board, Color.BLACK));
        placeNewPiece('e', 1, new King(this.board, Color.WHITE));
        placeNewPiece('e', 8, new King(this.board, Color.BLACK));

    }

}