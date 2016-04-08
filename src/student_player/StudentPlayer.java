package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;

/**
 * A StudentPlayer class with an intelligent game move selection, based on
 * minimax algorithm.
 * 
 * @author Alex Ilea (260629223)
 * @date 2016/04/07
 *
 */
public class StudentPlayer extends HusPlayer {

	private HusBoardState currentBoardState;
	private double moveStartTime;
	private double moveDurationTime;
	
	/**
	 * Public constructor.
	 * Initializes player with my student ID
	 */
	public StudentPlayer() {
		super("260629223");
	}
	
	public HusBoardState getCurrentBoardState() {
		return currentBoardState;
	}

	public void setCurrentBoardState(HusBoardState currentBoardState) {
		this.currentBoardState = currentBoardState;
	}

	public double getMoveStartTime() {
		return moveStartTime;
	}

	public void setMoveStartTime(double moveStartTime) {
		this.moveStartTime = moveStartTime;
	}

	/**
	 * Returns the duration time of the current move in seconds
	 * @return double moveDurationTime
	 */
	public double getMoveDurationTime() {
		this.moveDurationTime = (System.nanoTime() - moveStartTime) / 1000000000.0;
		return moveDurationTime;
	}

	public void setMoveDurationTime(double moveDurationTime) {
		this.moveDurationTime = moveDurationTime;
	}

	/**
	 * Custom implementation of chooseMove method
	 * 
	 * @see hus.HusPlayer#chooseMove(hus.HusBoardState)
	 */
	public HusMove chooseMove(HusBoardState board_state) {

		setMoveStartTime(System.nanoTime());
		setCurrentBoardState(board_state);
		int bestValue = Integer.MIN_VALUE;		
									
		ArrayList<HusMove> moves = board_state.getLegalMoves();
		MoveTuple bestMove = new MoveTuple(moves.get(0), bestValue);
						
		// Search through currently available moves
		for (int i = 0; i < moves.size(); i++) {
			HusBoardState boardStateCopy = (HusBoardState) board_state.clone();
			boardStateCopy.move(moves.get(i));
			
			int moveCount = boardStateCopy.getLegalMoves().size();
			int depth = getDepth(board_state, moveCount);
			int result = minimax(boardStateCopy, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

			if (result > bestValue) {
				bestValue = result;
				bestMove.setMove(moves.get(i));
				bestMove.setScore(result);
			}
			
			if (isTimedOut()) {
				return bestMove.getMove();
			}
		}
		
		return bestMove.getMove();
	}

	/**
	 * Implementation of minimax search algorithm using alpha-beta pruning
	 * 
	 * Search is performed up to the given depth. In case where search takes more
	 * time than allowed by the game rules, algorithm breaks from recursion and
	 * returns the best value at the current depth.
	 * 
	 * @param boardState - state of the game board
	 * @param depth - depth of minimax search
	 * @param alpha - minimizer
	 * @param beta - maximizer
	 * @return bestValue - best score for a given board state
	 */
	private int minimax(HusBoardState boardState, int depth, int alpha, int beta) {
		
		ArrayList<HusMove> moves = boardState.getLegalMoves();
		
		if (depth == 0 || moves.size() == 0 || isTimedOut()) {
			int bestValue = seedCount(boardState.getPits()[player_id]);
			return bestValue;
		}

		if (boardState.getTurnPlayer() == player_id) {
			int bestValue = Integer.MIN_VALUE;
			for (int i = 0; i < moves.size(); i++) {
				HusBoardState boardStateCopy = (HusBoardState) boardState.clone();
				boardStateCopy.move(moves.get(i));
				int result = minimax(boardStateCopy, depth - 1, bestValue, beta);
				bestValue = Math.max(bestValue, result);				
				if (bestValue > beta) { return beta; }
			}
			return bestValue;
		} else {
			int bestValue = Integer.MAX_VALUE;
			for (int i = 0; i < moves.size(); i++) {
				HusBoardState boardStateCopy = (HusBoardState) boardState.clone();
				boardStateCopy.move(moves.get(i));
				int result = minimax(boardStateCopy, depth - 1, alpha, bestValue);
				bestValue = Math.min(bestValue, result);				
				if (bestValue < alpha) { return alpha; }
			}			
			return bestValue;
		}
	}

	/**
	 * Simple evaluation function used in minimax search algorithm. Evaluates
	 * the terminal node based on the current number of seeds in players pits.
	 * 
	 * @param playerPits
	 * @return seedCount - total number of seed in players pits
	 */
	private int seedCount(int[] playerPits) {

		int seedCount = 0;

		for (int i = 0; i < playerPits.length; i++) {
			seedCount += playerPits[i];
		}

		return seedCount;
	}
	
	/**
	 * Helper function to estimate depth that could be explored 
	 * within the given time limit. 
	 * 
	 * @param boardState - state of the board
	 * @param moveCount - number of potential moves to explore 
	 * @return depth - depth of the search
	 */
	private int getDepth(HusBoardState boardState, int moveCount) {
		
		int depth = 5;
		
		// increase depth only after first 5 turns to avoid timeouts
		if (boardState.getTurnNumber() > 5) {
			if (moveCount < 15) {
				depth = 6;
			} else if (moveCount < 10) {
				depth = 7;
			} else {
				depth = 5;
			}
		}
		
		return depth;
	}
	
	/**
	 * Helper function used to determine if current move is approaching the
	 * maximum time allowed by the game. For first move, allow up to 30 seconds,
	 * for subsequent moves allow up to 2 seconds.
	 * 
	 * @return - isTimedOut
	 */
	private boolean isTimedOut() {
	
		boolean isTimedOut = false;
		
		if (getCurrentBoardState().getTurnNumber() == 0 && getMoveDurationTime() > 29.98) {
			isTimedOut = true;
		}

		if (getCurrentBoardState().getTurnNumber() > 0 && getMoveDurationTime() > 1.98) {
			isTimedOut = true;
		}
				
		return isTimedOut;		
	}

	/**
	 * Defines a tuple that stores a HusMove and its corresponding score as
	 * calculated by evaluation function.
	 * 
	 * @author Alex Ilea (260629223)
	 * @date 2016/04/07
	 */
	class MoveTuple {

		private HusMove move;
		private int score;

		public MoveTuple(HusMove move, int score) {
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

}
