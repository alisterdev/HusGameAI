package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;

import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class StudentPlayer extends HusPlayer {
	
	/**
	 * You must modify this constructor to return your student number. This is
	 * important, because this is what the code that runs the competition uses
	 * to associate you with your agent. The constructor should do nothing else.
	 */
	public StudentPlayer() {
		super("260629223");
	}

	/**
	 * This is the primary method that you need to implement. The
	 * ``board_state`` object contains the current state of the game, which your
	 * agent can use to make decisions. See the class hus.RandomHusPlayer for
	 * another example agent.
	 */
	public HusMove chooseMove(HusBoardState board_state) {

		long startTime = System.nanoTime();  
		
		ArrayList<HusMove> moves = board_state.getLegalMoves();		
		int bestValue = Integer.MIN_VALUE;
		Node bestNode = new Node(moves.get(0), bestValue);
		
		// Loop through currently available moves
		for (int i = 0; i < moves.size(); i++) {	
			
			HusBoardState boardStateCopy = (HusBoardState) board_state.clone();
			boardStateCopy.move(moves.get(i));			
		
			int result = minimax(boardStateCopy, 5, moves.get(i), Integer.MIN_VALUE, Integer.MAX_VALUE);
						
			if (result > bestValue) {
				bestValue = result;
				bestNode.setMove(moves.get(i));
				bestNode.setScore(result);
			}
			
			//System.out.println("Move: " + moves.get(i).getPit() + " | Score: " + result);
		}
		
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Time per move " + board_state.getTurnNumber() + ": " + estimatedTime  / 1000000000.0);
		
		return bestNode.getMove();
	}
	
	private int minimax(HusBoardState boardState, int depth, HusMove lastMove, int min, int max) {
			
		ArrayList<HusMove> moves = boardState.getLegalMoves();		
		
		// If depth is 0, evaluate my board state
		if (depth == 0 || moves.size() == 1) {
			int bestValue = seedCount(boardState.getPits()[player_id]);			
			//System.out.println("Depth: 0 | Pit: " + lastMove.getPit() + " | Best value: " + bestValue);
			return bestValue;
		}
				
		// IF MY TURN
		if (boardState.getTurnPlayer() == player_id) {
								
			int bestValue = Integer.MIN_VALUE;
			for (int i = 0; i < moves.size(); i++) {					
				HusBoardState boardStateCopy = (HusBoardState) boardState.clone();
				boardStateCopy.move(moves.get(i));						
				int result = minimax(boardStateCopy, depth - 1, moves.get(i), bestValue, max);
				bestValue = Math.max(bestValue, result);
				if (bestValue > max) return max;
			}
			//System.out.println("My Turn | Best value: " + bestNode.getScore() + " | Pit: " + bestNode.getMove().getPit());
			return bestValue;
			
		} else {
			
			int bestValue = Integer.MAX_VALUE;					
			for (int i = 0; i < moves.size(); i++) {					
				HusBoardState boardStateCopy = (HusBoardState) boardState.clone();
				boardStateCopy.move(moves.get(i));				
				int result = minimax(boardStateCopy, depth - 1, moves.get(i), min, bestValue);
				bestValue = Math.min(bestValue, result);	
				if (bestValue < min) return min;
			}
			//System.out.println("Opp Turn | Best value: " + bestNode.getScore());
			return bestValue;		
			
		}			
	}
	
	
	/**
	 * Simple evaluation function based on the current number of seeds
	 * in players pits
	 * 
	 * @param int [] playerPits
	 * @return int seedCount 
	 */
	private int seedCount(int[] playerPits) {
		
		int seedCount = 0;
		
		for (int pitPos = 0; pitPos < playerPits.length; pitPos++) {
			seedCount += playerPits[pitPos];
		}
				
		return seedCount;
	}
	
}

class Node {
	
	private HusMove move;
	private int score;
		
	public Node(HusMove move, int score) {
		this.move = move;
		this.score = score;
	}

	public HusMove getMove() {
		return move;
	}

	public void setMove(HusMove move) {
		this.move = move;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
	
}

