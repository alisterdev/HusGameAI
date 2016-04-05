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
		// Get the contents of the pits so we can use it to make decisions.
		int[][] pits = board_state.getPits();
		
		// Use ``player_id`` and ``opponent_id`` to get my pits and opponent
		// pits.
		int[] my_pits = pits[player_id];
		int[] op_pits = pits[opponent_id];
		
		// Use code stored in ``mytools`` package.
		MyTools.getSomething();

		HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
		ArrayList<HusMove> moves = board_state.getLegalMoves();
		
		System.out.println("Start the adventure");
		Node finalres = minimax(cloned_board_state, 1, moves.get(0));
		System.out.println("done travesing" + finalres.getScore() + " Move: " + finalres.getMove().getPit());
		//System.out.println("Best move?: " + this.bestMove.getScore() + " | pit:" + this.bestMove.getMove().getPit());
//		// We can see the effects of a move like this...
//		HusBoardState cloned_board_state = (HusBoardState) board_state.clone();
//		cloned_board_state.move(move);
		// But since this is a placeholder algorithm, we won't act on that
		// information.
		
		HusMove move = moves.get(0);
		return finalres.getMove();
	}

	private Node minimax(HusBoardState boardState, int depth, HusMove lastMove) {
			
		ArrayList<HusMove> moves = boardState.getLegalMoves();		
		
		// If depth is 0, evaluate my board state
		if (depth == 0 || moves.size() == 1) {
			int bestValue = seedCount(boardState.getPits()[player_id]);
			Node n = new Node(lastMove, bestValue);
			//System.out.println("Depth: 0 | Pit: " + lastMove.getPit() + " | Best value: " + bestValue);
			return n;
		}
				
		// IF MY TURN
		if (boardState.getTurnPlayer() == player_id) {
								
			int bestValue = Integer.MIN_VALUE;
			Node bestNode = new Node(null, bestValue);
			
			for (int i = 0; i < moves.size(); i++) {	
				
				HusBoardState boardStateCopy = (HusBoardState) boardState.clone();
				boardStateCopy.move(moves.get(i));			
			
				Node newNode = minimax(boardStateCopy, depth - 1, moves.get(i));
				bestValue = Math.max(bestValue, newNode.getScore());
				
				if (bestValue == newNode.getScore()) {
					bestNode = newNode;
				}

			}
			//System.out.println("My Turn | Best value: " + bestNode.getScore() + " | Pit: " + bestNode.getMove().getPit());
			return bestNode;
			
		// IF OPPONENT TURN
		} else {
			
			int bestValue = Integer.MAX_VALUE;					
			Node bestNode = new Node(null, bestValue);
			
			for (int i = 0; i < moves.size(); i++) {	
				
				HusBoardState boardStateCopy = (HusBoardState) boardState.clone();
				boardStateCopy.move(moves.get(i));
				
				Node newNode = minimax(boardStateCopy, depth - 1, moves.get(i));
				bestValue = Math.min(bestValue, newNode.getScore());
				
				if (bestValue == newNode.getScore()) {
					bestNode = newNode;
				}
 											
			}
			//System.out.println("Opp Turn | Best value: " + bestNode.getScore());
			return bestNode;
				
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

