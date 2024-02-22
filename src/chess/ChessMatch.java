package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch{

    private Board board;
    private Color currentPlayer;
    private int turn;
    private boolean check;
    private boolean checkMate;

    private List<Piece> pieceOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch(){
        this.board = new Board(8, 8);
        this.turn = 1;
        this.currentPlayer = Color.WHITE;
        this.initialSetup();
    }

    public int getTurn(){
        return turn;
    }

    public  Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck(){
        return check;
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
    public boolean[][] possibleMoves(ChessPossition sourcePosition){
        Position position = sourcePosition.toPosition();
        ValidateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }
    public ChessPiece performChessMove(ChessPossition sourceChessPossition, ChessPossition targetPossition){
        Position source = sourceChessPossition.toPosition();
        Position target = targetPossition.toPosition();
        ValidateSourcePosition(source);
        ValidateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if(testeCheck(currentPlayer)){
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        check = testeCheck(opponent(currentPlayer));
        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }else{
            nextTurn();
        }
        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target){
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null){
            pieceOnTheBoard.remove(capturedPiece);
            this.capturedPieces.add(capturedPiece);
        }
        return capturedPiece;

    }

    private void undoMove(Position source, Position target, Piece capturedPiece){
        Piece p = board.removePiece(target);
        board.placePiece(p, source);
        if (capturedPiece != null){
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            pieceOnTheBoard.add(capturedPiece);
        }
        

    }
    private void ValidateSourcePosition(Position position){
        if(!this.board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on the source position");
        }
        if(!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("There is no possible moves for the chosen piece");
        }
        if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException("The chosen piece is not yorus");
        }
    }

    private void ValidateTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){
            throw new ChessException("The chosen piece can't move to target position");
        }
        
    }
    public void nextTurn(){
        this.currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
        this.turn +=1;
    }

    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color){
        List<Piece> list = pieceOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece p: list){
            if( p instanceof King){
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + " King on  the board");
    }

    private boolean testeCheck(Color color){
        Position kingPosition = king(color).getChessPosition().toPosition();

        List<Piece> opponentPieces = pieceOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());

        for(Piece p: opponentPieces){
            boolean[][] mat = p.possibleMoves();
            if(mat[kingPosition.getRow()][kingPosition.getColumn()]){
                return true;
            }
        }

        return false;
    }

    private boolean testCheckMate(Color color){
        if(!testeCheck(color)) return false;
        List<Piece> pieces = pieceOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p: pieces){
            boolean[][] mat = p.possibleMoves();
            for(int i=0; i<board.getRows();i++){
                for(int j=0; j<board.getColumns(); j++){
                    if(mat[i][j]){
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testeCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean getCheckMate(){
        return checkMate;
    }
    public void placeNewPiece(char column, int row, ChessPiece piece){
        Position pos = new ChessPossition(column, row).toPosition();
        this.board.placePiece(piece, pos);
        this.pieceOnTheBoard.add(piece);

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