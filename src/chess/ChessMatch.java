package chess;

import boardgame.Board;
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

    public void initialSetup(){
        Rook rookwhite1 = new Rook(this.board, Color.WHITE);
        Rook rookwhite2 = new Rook(this.board, Color.WHITE);
        Rook rookblack1 = new Rook(this.board, Color.BLACK);
        Rook rookblack2 = new Rook(this.board, Color.BLACK);

        this.board.placePiece(rookwhite1, new Position(7, 0));
        this.board.placePiece(rookwhite2, new Position(7, 7));
        this.board.placePiece(rookblack1, new Position(0, 0));
        this.board.placePiece(rookblack2, new Position(0, 7));

        King kingwhite = new King(this.board, Color.WHITE);
        King kingblack = new King(this.board, Color.BLACK);

        this.board.placePiece(kingwhite, new Position(7, 4));
        this.board.placePiece(kingblack, new Position(0, 4));

    }

}