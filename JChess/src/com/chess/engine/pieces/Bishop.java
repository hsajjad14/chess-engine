package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;

public class Bishop extends Piece{
	
	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 9, 7};

	public Bishop(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.BISHOP, piecePosition, pieceAlliance);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		// TODO Auto-generated method stub
		
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int candidateCordinateOffset: this.CANDIDATE_MOVE_VECTOR_COORDINATES) {
			
			int candidateDestinationCoordinate = this.piecePosition;
			
			while(BoardUtils.isValidTileCoord(candidateDestinationCoordinate)) {
				
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCordinateOffset)
						|| isEighthColumnExclusion(candidateDestinationCoordinate, candidateCordinateOffset)) {
					break;
				}
				
				candidateDestinationCoordinate += candidateCordinateOffset;
				if(BoardUtils.isValidTileCoord(candidateDestinationCoordinate)) {
					
					final Tile candidateDestTile = board.getTile(candidateDestinationCoordinate);
					
					if(!candidateDestTile.isTileOccupied()) {
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
						
					}else {
						final Piece pieceAtDest = candidateDestTile.getPiece();
						final Alliance pieceAlliance = pieceAtDest.getPieceAlliance();
						
						
						if(this.pieceAlliance != pieceAlliance) {
							// we can take this piece 
							legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDest));
						}
						break;
					}
					
				}
				
			}
			
		}

		
		return ImmutableList.copyOf(legalMoves);
	}
	
	// bishop sitting on first column of board
		private static boolean isFirstColumnExclusion(final int curPost, final int candidateOffset) {
			
			// all cords for a given col
			return BoardUtils.FIRST_COLUMN[curPost] && ((candidateOffset == -9) || (candidateOffset == 7));
			
		}

		private static boolean isEighthColumnExclusion(final int curPost, final int candidateOffset) {
			
			// all cords for a given col
			return BoardUtils.EIGHTH_COLUMN[curPost] && ((candidateOffset == 9) || (candidateOffset == -7));
			
		}
		
		@Override
		public String toString() {
			return Piece.PieceType.BISHOP.toString();
		}

		@Override
		public Bishop movePiece(final Move move) {
			// TODO Auto-generated method stub
			
			// creates new bishop in same alliance but new location
			return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
		}
		
	
	
	

}
