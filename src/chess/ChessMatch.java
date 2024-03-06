package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch{

    private Board board;
    private Color currentPlayer;
    private int turn;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

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

    public ChessPiece getPromoted(){
        return promoted;
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

        ChessPiece movedPiece = (ChessPiece)board.piece(target);
        
        // special move promotion
        promoted = null;
        if(movedPiece instanceof Pawn){
            if (movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || movedPiece.getColor() == Color.BLACK && target.getRow() == 7) {
                promoted = (ChessPiece)board.piece(target);
                promoted = replacePromotedPiece("Q");
                
            }
        }
        check = testeCheck(opponent(currentPlayer));

        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }else{
            nextTurn();
        }

        //  special move en passant
        if(movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)){
            enPassantVulnerable = movedPiece;
        } else{
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) {
        if(promoted == null){
            throw new IllegalStateException("There is no piece to be promoted.");
        }

        if(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")){
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        pieceOnTheBoard.remove(p);
        
        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        pieceOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color){
        if(type.equals("B")) return new Bishop(board, color);
        if(type.equals("N")) return new Knight(board, color);
        if(type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
    }
    public ChessPiece getEnPassantVunerable(){
        return enPassantVulnerable;
    }
    private Piece makeMove(Position source, Position target){
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increasedMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null){
            pieceOnTheBoard.remove(capturedPiece);
            this.capturedPieces.add(capturedPiece);
        }

        // speical move castling kingside rook
        if(p instanceof King && target.getColumn() == source.getColumn() + 2){
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increasedMoveCount();
        }

        // speical move castling queenside rook
        if(p instanceof King && target.getColumn() == source.getColumn() - 2){
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increasedMoveCount();
        }

        //  special move en passant 
        if (p instanceof Pawn){
            if(source.getColumn() != target.getColumn() && capturedPiece == null){
                Position pawnPosition;
                if (p.getColor() == Color.WHITE){
                    pawnPosition = new Position(target.getRow() + 1 , target.getColumn());
                }else{
                    pawnPosition = new Position(target.getRow() - 1 , target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                pieceOnTheBoard.remove(capturedPiece);
            }
        }
        return capturedPiece;

    }

    private void undoMove(Position source, Position target, Piece capturedPiece){
        ChessPiece p = (ChessPiece)board.removePiece(target);
        p.decreasedMoveCount();
        board.placePiece(p, source);
        if (capturedPiece != null){
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            pieceOnTheBoard.add(capturedPiece);
        }

        
        // speical move castling kingside rook
        if(p instanceof King && target.getColumn() == source.getColumn() + 2){
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreasedMoveCount();
        }

        // speical move castling queenside rook
        if(p instanceof King && target.getColumn() == source.getColumn() - 2){
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreasedMoveCount();
        }
        
        //  special move en passant 
        if (p instanceof Pawn){
            if(source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable){
                ChessPiece pawn = (ChessPiece)board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE){
                    pawnPosition = new Position(3 , target.getColumn());
                }else{
                    pawnPosition = new Position(4 , target.getColumn());
                }

                board.placePiece(pawn, pawnPosition);
            }
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
        
        placeNewPiece('e', 1, new King(this.board, Color.WHITE, this));
        placeNewPiece('e', 8, new King(this.board, Color.BLACK, this));

        placeNewPiece('a', 2, new Pawn(this.board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(this.board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(this.board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(this.board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(this.board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(this.board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(this.board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(this.board, Color.WHITE, this));


        placeNewPiece('a', 7, new Pawn(this.board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(this.board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(this.board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(this.board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(this.board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(this.board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(this.board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(this.board, Color.BLACK, this));

        placeNewPiece('c', 1, new Bishop(this.board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(this.board, Color.WHITE));
        
        placeNewPiece('c', 8, new Bishop(this.board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(this.board, Color.BLACK));

        placeNewPiece('b', 1, new Knight(this.board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(this.board, Color.WHITE));

        placeNewPiece('b', 8, new Knight(this.board, Color.BLACK));
        placeNewPiece('g',8, new Knight(this.board, Color.BLACK));

        placeNewPiece('d',1, new Queen(this.board, Color.WHITE));
        placeNewPiece('d',8, new Queen(this.board, Color.BLACK));

    }

}