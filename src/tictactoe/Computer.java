/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

/**
 *
 * @author doha
 */
public class Computer extends Player{
    private final int[][] preferredMoves = {
         {1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2},
         {0, 1}, {1, 0}, {1, 2}, {2, 1}};
    
    public Computer(int player){
        super(player);
        this.player = player;
        System.out.println("Player 'Computer' created");
        
    }
    
    @Override
    public void play(Board board){
        Try(board);
        board.setPosition(attempt, player);
    }
    
    @Override
    public void Try(Board board){
//     for (int[] move : preferredMoves) {
//         if (cells[move[0]][move[1]].content == Seed.EMPTY) {
//            return move;
//         }
//      }
//      assert false : "No empty cell?!";
//      return null;
//   }    
    ////////////////////////////////////////////////    
//   do{
//            do{
//                System.out.print("Line: ");
//                attempt[0] = input.nextInt();
//                
//                if( attempt[0] > 3 ||attempt[0] < 1)
//                    System.out.println("Invalid line. It's 1, 2 or 3");
//                
//            }while( attempt[0] > 3 ||attempt[0] < 1);
//            
//            do{
//                System.out.print("Column: ");
//                attempt[1] = input.nextInt();
//                
//                if(attempt[1] > 3 ||attempt[1] < 1)
//                    System.out.println("Invalid column. É 1, 2 or 3");
//                
//            }while(attempt[1] > 3 ||attempt[1] < 1);
//            
//            attempt[0]--; 
//            attempt[1]--;
//            
//            if(!checkTry(attempt, board))
//                System.out.println("Placed already marked. Try other.");
//        }while( !checkTry(attempt, board) );
        
        for(int i =0; i<9; i++){
         if(board.fullBoard()){
        break;
        }
        attempt[0]=preferredMoves[i][0];
        attempt[1]=preferredMoves[i][1];
        if(checkTry(attempt, board)){
        break;
        }
       
    
        }
    }
}
