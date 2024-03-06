package application;

import java.security.InvalidParameterException;
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
        while (!chessMatch.getCheckMate()) {
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

                if(chessMatch.getPromoted() != null){
                    System.out.println("Enter piece for promotion (B/N/R/Q): ");
                    String type = scanner.nextLine().toUpperCase();
                    if(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")){
                        System.out.println("Invalid type for promotion"); 
                        type = scanner.nextLine().toUpperCase();           
                    }
                    
                    chessMatch.replacePromotedPiece(type);
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
        UI.clearScreen();
        UI.printMatch(chessMatch, captured);

    }
}