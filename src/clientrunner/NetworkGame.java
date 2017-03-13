/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;

/**
 *
 * @author doha
 */
public class NetworkGame {
    

    private Board board;
    private int turn=1, who=1;
    private Player player;

    public Scanner input = new Scanner(System.in);

    
    public NetworkGame (int playerno){
        board = new Board();
         this.player = new Human(playerno);
        
        while( Play() );
    }
    

    

  
    
    public boolean Play(){
        board.showBoard();
        if(won() == 0 ){
            System.out.println("----------------------");
            System.out.println("\nTurn "+turn);
            System.out.println("It's turn of Player " + who() );
            
            //if(who()==1)
              player.play(board);
            
            
            
            if(board.fullBoard()){
                System.out.println("Full Board. Draw!");
                return false;
            }
            who++;
            turn++;

            return true;
        } else{
            if(won() == -1 )
                System.out.println("Player 1 won!");
            else
                System.out.println("Player 2 won!");
            
            return false;
        }
            
    }
    
    public int who(){
        
        if(who%2 == 1)
            return 1;
        else
            return 2;
    }
    
    public int won(){
        if(board.checkLines() == 1)
            return 1;
        if(board.checkColumns() == 1)
            return 1;
        if(board.checkDiagonals() == 1)
            return 1;
        
        if(board.checkLines() == -1)
            return -1;
        if(board.checkColumns() == -1)
            return -1;
        if(board.checkDiagonals() == -1)
            return -1;
        
        return 0;
    }
    
    
}