package application;

import chess.ChessPiece;

public class UI {
    public static void printBoard(ChessPiece[][] pieces){
        int rows = pieces.length;
        int columns = rows;
        for(int i=0; i<rows; i++){
            System.out.print(rows - i + " ");
            for (int j=0; j<columns; j++){
                printPiece(pieces[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");

    }
    public static void printPiece(ChessPiece piece){
        if  (piece == null) {
            System.out.print("-");
        }else{
            System.out.print(piece);
        }
        System.out.print(" ");


    }
}
