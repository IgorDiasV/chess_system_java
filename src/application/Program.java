package application;

import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPossition;

public class Program {

	public static void main(String[] args) {
        ChessMatch chessMatch = new ChessMatch();
        UI.printBoard(chessMatch.getChessPieces());
        Scanner  scanner = new Scanner(System.in);
        while (true) {
            UI.printBoard(chessMatch.getChessPieces());
            System.out.println(" ");
            System.out.print("Source: ");
            ChessPossition source =  UI.readChessPossition(scanner);
            System.out.println();
            System.out.print("Target: ");
            ChessPossition target = UI.readChessPossition(scanner);
            chessMatch.performChessMove(source, target);

        }

    }
}