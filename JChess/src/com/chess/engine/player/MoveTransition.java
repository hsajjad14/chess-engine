package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {
	// captures all the info you want to remember when making a new move
	// like the new board thats created 
	
	// new board after moving
	private final Board transitionBoard;
	
	// the move
	private final Move move;
	
	// tells us if we can do the move or not (checks if in check or something)
	private final MoveStatus moveStatus;
	
	public MoveTransition(final Board transitionBoard,
			final Move move,
			final MoveStatus moveStatus) {
		this.transitionBoard = transitionBoard;
		this.move = move;
		this.moveStatus = moveStatus;
	}

	public MoveStatus getMoveStatus() {
		// TODO Auto-generated method stub
		return this.moveStatus;
	}

	public Board getBoard() {
		// TODO Auto-generated method stub
		return this.transitionBoard;
	}

}
