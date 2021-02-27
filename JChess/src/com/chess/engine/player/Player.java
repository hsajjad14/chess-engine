package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class Player {
	
	private final boolean isInCheck;
	
	protected final Board board;
	
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	
	Player(final Board board,
			final Collection<Move> legalMoves,
			final Collection<Move> opponentMoves){
		
		this.board = board;
		this.playerKing = establishKing();
		// concats all legal moves with castle moves
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(calculateKingCastles(legalMoves, opponentMoves), legalMoves));
		
		
		// the method returns a list of moves frm opponent that attack our king
		// if list is empty (isincheck = false) so nothing attacks the king
		// opposite for not empty
		this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
	
		
	
	
	}

	
	// returns list of all attacks on a tile
	protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
		
		final List<Move> attackMoves = new ArrayList<>();
		
		for(Move move : moves) {
			if(piecePosition == move.getDestinationCoordinate()) {
				attackMoves.add(move);
			}
		}
		
		return ImmutableList.copyOf(attackMoves);
	}
	
	public King getPlayerKing() {
		return this.playerKing;
	}

	private King establishKing() {
		for(final Piece piece: getActivePieces()) {
			if(piece.getPieceType().isKing()) {
				
				return (King) piece;
				
			}
		}
		
		throw new RuntimeException("should not reach here, no king");
	}
	
	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}
	
	public boolean isInCheck() {
		return this.isInCheck;
	}
	
	// in check, and can't move
	public boolean isInCheckMate() {
		return this.isInCheck && !hasEscapeMoves();
	}
	
	
	// go thru all legal moves, and if you can make a move
	// thats allowed, i.e not blocked by check or anything else (what isdone does)
	// return true
	protected boolean hasEscapeMoves() {
		for(Move move : this.legalMoves) {
			MoveTransition transition = makeMove(move);
			if(transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}

	// not in check and cant move
	public boolean isInStaleMate() {
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	public boolean isCastled() {
		return false;
	}
	
	public MoveTransition makeMove(final Move move) {
		
		if(!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		
		
		// make the move
		final Board transitionBoard = move.execute();
		// ex assume current player = white
		// after making a move current player changes to black for transitionBoard
		
		// so did white's king position have any attacks on it
		// so on white's king's tile position and black's legal move, is there an attack to it
		
		// attacks on opponent's king
		// current-players oppenents king
		// because after making a move we are no longer the current player
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(
				transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), 
				transitionBoard.currentPlayer().getLegalMoves());
		
		
		
		// if list of attacks on opponent's king are not empty
		if(!kingAttacks.isEmpty()) {
			// cant make this move
			return new MoveTransition(this.board,move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}
	
	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}
	
	public abstract Collection<Piece> getActivePieces();
	
	public abstract Alliance getAlliance();
	
	public abstract Player getOpponent();
	
	
	protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
			Collection<Move> opponentsLegals);

}
