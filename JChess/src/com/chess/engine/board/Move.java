package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
	
	final Board board;
	final Piece movedPiece;
	final int destinationCoordinate;
	
	public static final Move NULL_MOVE = new NullMove();
	
	
	private Move(final Board board,
			final Piece movedPiece,
			final int destCoord){
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destCoord;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime*result + this.destinationCoordinate;
		
		result = prime*result + this.movedPiece.hashCode();
		
		return result;


	}
	
	
	// equals method for a move
	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		
		
		if(!(other instanceof Move)) {
			return false;
		}
		
		final Move otherMove = (Move) other;
		
		return this.getDestinationCoordinate() == otherMove.getDestinationCoordinate()
				&& this.getMovedPiece().equals(otherMove.getMovedPiece());
		
	}
	
	// returns a new board using board builder in execute
	public Board execute() {
		// TODO Auto-generated method stub
		
		// make a new board 
		final Builder builder = new Builder();
		
		
		// for all pieces that aren't the moved piece for current player place them on the board
		// same as original location
		for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
			if(!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		
		
		// same for black
		// don't need if to check if move piece b/c its not oppenents turn to move
		for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		}
		
		// set piece for moved piece
		builder.setPiece(this.movedPiece.movePiece(this));
		
		// set the move maker to the opponent
		// so if current is white, set to black
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
		return builder.build();
	}
	
	public Piece getMovedPiece() {
		return this.movedPiece;
	}
	
	
	public boolean isAttack() {
		return false;
	}
	
	public boolean isCastlingMove() {
		return false;
	}
	
	public Piece getAttackedPiece() {
		return null;
	}

	// a major piece move
	public static final class MajorMove extends Move{

		public MajorMove(Board board, 
				Piece movedPiece, 
				int destCoord) {
			super(board, movedPiece, destCoord);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	public static class AttackMove extends Move{

		final Piece attackedPiece;
		
		public AttackMove(Board board, 
				Piece movedPiece, 
				int destCoord,
				final Piece attackedPiece) {
			super(board, movedPiece, destCoord);
			this.attackedPiece = attackedPiece;
			// TODO Auto-generated construct or stub
		}

		@Override
		public Board execute() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int hashCode() {
			return this.attackedPiece.hashCode() + super.hashCode();
		}
		
		@Override
		public boolean equals(final Object other) {
			if (this == other) {
				return true;
			}
			
			if(!(other instanceof AttackMove)) {
				return false;
			}
			
			final AttackMove otherattack = (AttackMove) other;
			return super.equals(otherattack) 
					&& getAttackedPiece().equals(otherattack.getAttackedPiece());
		}
		
		@Override
		public boolean isAttack() {
			return true;	
		}
		
		@Override
		public Piece getAttackedPiece() {
			return this.attackedPiece;
		}
		
		
		
	}

	public int getDestinationCoordinate() {
		// TODO Auto-generated method stub
		return this.destinationCoordinate;
	}
	
	public static final class PawnMove extends Move{

		public PawnMove(Board board, 
				Piece movedPiece, 
				int destCoord) {
			super(board, movedPiece, destCoord);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	public static class PawnAttackMove extends AttackMove{

		public PawnAttackMove(Board board, 
				Piece movedPiece, 
				int destCoord,
				final Piece attackedPiece) {
			super(board, movedPiece, destCoord, attackedPiece);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	
	
	public static class PawnEnPassantAttackMove extends PawnAttackMove{

		public PawnEnPassantAttackMove(Board board, 
				Piece movedPiece, 
				int destCoord,
				final Piece attackedPiece) {
			super(board, movedPiece, destCoord, attackedPiece);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	
	public static final class PawnJump extends Move{

		public PawnJump(Board board, 
				Piece movedPiece, 
				int destCoord) {
			super(board, movedPiece, destCoord);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Board execute() {
			
			final Builder builder = new Builder();
			
			
			// for all pieces that aren't the moved piece for current player place them on the board
			// same as original location
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			
			
			// same for black
			// don't need if to check if move piece b/c its not oppenents turn to move
			for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			
			final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
			
			
			// set piece for moved pawn
			builder.setPiece(movedPawn);
			
			builder.setEnPassantPawn(movedPawn);
			// set the move maker to the opponent
			// so if current is white, set to black
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		
	}
	
	
	
	static abstract class CastleMove extends Move{
		
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDestination;
		
		
		// destCoord is piece destcoord
		// castleRookDestination is where rook will go
		public CastleMove(Board board, 
				Piece movedPiece, 
				int destCoord,
				final Rook castleRook,
				final int castleRookStart,
				final int castleRookDestination) {
			super(board, movedPiece, destCoord);
			
			this.castleRook = castleRook;
			this.castleRookStart = castleRookStart;
			this.castleRookDestination = castleRookDestination;
			// TODO Auto-generated constructor stub
		}
		
		public Rook getCastleRook() {
			return this.castleRook;
		}
		
		@Override
		public boolean isCastlingMove() {
			return true;
		}
		
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			
			
			// for all pieces that aren't the moved piece for current player place them on the board
			// same as original location
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			
			
			// same for black
			// don't need if to check if move piece b/c its not oppenents turn to move
			for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			
			
			
			
			// set piece king
			builder.setPiece(this.movedPiece.movePiece(this));
			// set piece for king
			builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
			// set the move maker to the opponent
			// so if current is white, set to black
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		
	}
	
	// sub class of castlemove
	public static final class KingSideCastleMove extends CastleMove{

		public KingSideCastleMove(Board board, 
				Piece movedPiece, 
				int destCoord,
				final Rook castleRook,
				final int castleRookStart,
				final int castleRookDestination) {
			super(board, movedPiece, destCoord, castleRook, castleRookStart, castleRookDestination);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public String toString() {
			return "O-O";
		}
		
	}
	
	// sub class of castlemove
	public static final class QueenSideCastleMove extends CastleMove{

		public QueenSideCastleMove(Board board, 
				Piece movedPiece, 
				int destCoord,
				final Rook castleRook,
				final int castleRookStart,
				final int castleRookDestination) {
			super(board, movedPiece, destCoord, castleRook, castleRookStart, castleRookDestination);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public String toString() {
			return "O-O-O";
		}
		
	}
	
	
	public static final class NullMove extends Move{

		public NullMove() {
			super(null, null, -1);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Board execute() {
			throw new RuntimeException("cannot execute null move");
		}
		
	}
	
	
	
	public static class MoveFactory {
		
		private MoveFactory() {
			throw new RuntimeException("not instantiable");
		}
		
		public static Move createMove(final Board board,
				final int currentCoordinate,
				final int destinationCoordinate) {
			
			for (final Move move : board.getAllLegalMoves()) {
				if(move.getCurrentCoordinate() == currentCoordinate
						&& move.getDestinationCoordinate() == destinationCoordinate) {
					return move;
				}
			}
			
			return NULL_MOVE;
		}
		
		
	}

	public int getCurrentCoordinate() {
		// TODO Auto-generated method stub
		return this.getMovedPiece().getPiecePosition();
	}
		
		

}
