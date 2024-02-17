package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import boardgame.BoardException;
import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPossition;
import chess.ChessPiece;
public class Program {

	public static void main(String[] args) {
        ChessMatch chessMatch = new ChessMatch();
        Scanner  scanner = new Scanner(System.in);
        List<ChessPiece> captured = new ArrayList<>();  
        while (true) {
            try{
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);
                System.out.println(" ");
                System.out.print("Source: ");
                ChessPossition source =  UI.readChessPossition(scanner);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getChessPieces(), possibleMoves);
                System.out.println();
                System.out.print("Target: ");
                ChessPossition target = UI.readChessPossition(scanner);
                ChessPiece capturedPiecce = chessMatch.performChessMove(source, target);
                if(capturedPiecce != null){
                    captured.add(capturedPiecce);
                }
            }catch (ChessException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }catch (InputMismatchException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }catch(BoardException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }

    }
}