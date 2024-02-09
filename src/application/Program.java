package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPossition;

public class Program {

	public static void main(String[] args) {
        ChessMatch chessMatch = new ChessMatch();
        Scanner  scanner = new Scanner(System.in);
        while (true) {
            try{
                UI.clearScreen();
                UI.printBoard(chessMatch.getChessPieces());
                System.out.println(" ");
                System.out.print("Source: ");
                ChessPossition source =  UI.readChessPossition(scanner);
                System.out.println();
                System.out.print("Target: ");
                ChessPossition target = UI.readChessPossition(scanner);
                chessMatch.performChessMove(source, target);
            }catch (ChessException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }catch (InputMismatchException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }

    }
}