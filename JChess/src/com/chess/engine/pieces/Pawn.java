package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece{
	
	// 16 is double jump, 7 and 9 are attacs
	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {8, 16, 7, 9};
	
	public Pawn(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.PAWN, piecePosition, pieceAlliance);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		// TODO Auto-generated method stub
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currentCandidateOffset: this.CANDIDATE_MOVE_VECTOR_COORDINATES) {
			
			// for white apply -8 for black apply 8
			int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset*this.pieceAlliance.getDirection();
		
			if(BoardUtils.isValidTileCoord(candidateDestinationCoordinate)) {
				//skips this loop
				continue;
			}
			
			if(currentCandidateOffset == 8 
					&& !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				
				// this is a legal move!
				legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				
			}else if (currentCandidateOffset == 16
					&& this.isFirstMove() && 
					(BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) 
					|| (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite())) {
				// pawn jumps 2 on first move 
				
				// now we want to make sure the tile before the second jump tile is empty, cant jump over pieces
				
				final int behindCandidateDestinationCoordinate = this.piecePosition + this.pieceAlliance.getDirection()*8;
				
				// if first tile is not occ, and second tile is not occupied then move
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()
						&& !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					
				}
				
				//7 and 9 are attacks
			}else if (currentCandidateOffset == 7
					&& !(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()
					|| BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {
				// so if attacking 7 dir on edge columns, doesn't work
				
				// capturing piece
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCand = board.getTile(candidateDestinationCoordinate).getPiece();
					
					// have to attack enemy
					if (this.pieceAlliance != pieceOnCand.getPieceAlliance()) {
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
						
					}
					
				}
				
			}else if (currentCandidateOffset == 9
					&&
					!(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()
							|| BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {
				
				// capturing piece
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCand = board.getTile(candidateDestinationCoordinate).getPiece();
					
					// have to attack enemy
					if (this.pieceAlliance != pieceOnCand.getPieceAlliance()) {
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
						
					}
					
				}
				
				
			}
			
			
		
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	
	@Override
	public String toString() {
		return Piece.PieceType.PAWN.toString();
	}
	
	@Override
	public Pawn movePiece(final Move move) {
		// TODO Auto-generated method stub
		
		// creates new bishop in same alliance but new location
		return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}

}
