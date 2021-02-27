package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	
	protected final PieceType pieceType;
	
	// every piece has a position
	protected final int piecePosition;
	
	// white or black 'Alliance', use enum
	protected final Alliance pieceAlliance;
	
	protected final boolean isFirstMove;
	
	
	private final int cacedHashCode;
	
	Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance){
		this.pieceAlliance = pieceAlliance;
		this.piecePosition = piecePosition;
		this.pieceType = pieceType;
		
		//more work here
		this.isFirstMove = false;
		
		this.cacedHashCode  = computeHashCode();
	}
	
	private int computeHashCode() {
		// TODO Auto-generated method stub
		int result = pieceType.hashCode();
		
		result = 31*result + pieceAlliance.hashCode();
		result = 31*result + piecePosition;
		result = 31*result + (isFirstMove ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(!(other instanceof Piece)) {
			return false;
		}
		
		final Piece otherPiece = (Piece) other;
		
		return pieceAlliance == otherPiece.getPieceAlliance() 
				&& pieceType == otherPiece.getPieceType()
				&& isFirstMove == otherPiece.isFirstMove();
	}
	
	
	// hashcode, dont worry bout this, same as builtin implementation
	@Override
	public int hashCode() {
		
		return this.cacedHashCode;
	}
	
	public PieceType getPieceType() {
		return this.pieceType;
	}
	
	public int getPiecePosition() {
		return this.piecePosition;
	}
	
	
	
	public Alliance getPieceAlliance() {
		return this.pieceAlliance;
	}
	
	// calculate legal moves for a piece 
	// set
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	
	
	public boolean isFirstMove() {
		return this.isFirstMove;
	}
	
	// takes a move, applies move, returns a new piece
	// with updated piece position
	public abstract Piece movePiece(Move move);

	
	public enum PieceType {
		
		PAWN("P") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		KNIGHT("N") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		BISHOP("B") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		ROOK("R") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return true;
			}
		},
		QUEEN("Q") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		KING("K") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		private String pieceName;
		
		PieceType(final String pieceName){
			this.pieceName = pieceName;
		}
		
		@Override
		public String toString() {
			return this.pieceName;
		}

		public abstract boolean isKing();
		
		public abstract boolean isRook();
		
	};
}
