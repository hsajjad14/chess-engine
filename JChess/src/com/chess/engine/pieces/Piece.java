package com.chess.engine.pieces;

import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	
	// every piece has a position
	protected final int piecePosition;
	
	// white or black 'Alliance', use enum
	protected final Alliance pieceAlliance;
	
	Piece(final int piecePosition, final Alliance pieceAlliance){
		this.pieceAlliance = pieceAlliance;
		this.piecePosition = piecePosition;
		
	}
	
	// calculate legal moves for a piece 
	public abstract List<Move> calculateLegalMoves(final Board board);
	

}
