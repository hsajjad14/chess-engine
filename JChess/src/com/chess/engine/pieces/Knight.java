package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;

import static com.chess.engine.board.Move.*;

public class Knight extends Piece{
	//https://en.wikipedia.org/wiki/Knight_(chess)
	private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};
	
	public Knight(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.KNIGHT, piecePosition, pieceAlliance);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		// TODO Auto-generated method stub
		int candidateDestCoord;
		List<Move> legalMoves = new ArrayList<>();
		for(final int currentCandidate: CANDIDATE_MOVE_COORDINATES) {
			
			
			candidateDestCoord = this.piecePosition + currentCandidate;
			
			if(BoardUtils.isValidTileCoord(candidateDestCoord)) {
				
				if(isFirstColumnExclusion(this.piecePosition, currentCandidate)
					|| isSecondColumnExclusion(this.piecePosition, currentCandidate)
					|| isSeventhColumnExclusion(this.piecePosition, currentCandidate)
					|| isEighthColumnExclusion(this.piecePosition, currentCandidate)
						) {
					continue; // continue skips everything below, and goes back to start of loop (line 26)
				}
				final Tile candidateDestTile = board.getTile(candidateDestCoord);
			
				if(!candidateDestTile.isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestCoord));
					
				}else {
					final Piece pieceAtDest = candidateDestTile.getPiece();
					final Alliance pieceAlliance = pieceAtDest.getPieceAlliance();
					
					
					if(this.pieceAlliance != pieceAlliance) {
						// we can take this piece 
						legalMoves.add(new AttackMove(board, this, candidateDestCoord, pieceAtDest));
					}
				}
			}
			
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	
	// move edge cases
	// knights sitting on first column of board
	private static boolean isFirstColumnExclusion(final int curPost, final int candidateOffset) {
		
		// all cords for a given col
		return BoardUtils.FIRST_COLUMN[curPost] && ((candidateOffset == -17) || (candidateOffset == -10) 
				|| (candidateOffset == 6) || (candidateOffset == 15));
		
	}
	
	// knights sitting on second column of board
	private static boolean isSecondColumnExclusion(final int curPost, final int candidateOffset) {
		
		// all cords for a given col
		return BoardUtils.SECOND_COLUMN[curPost] && ((candidateOffset == -10) 
				|| (candidateOffset == 6));
	}
	
	
	// knights sitting on 7th column of board
		private static boolean isSeventhColumnExclusion(final int curPost, final int candidateOffset) {
			
			// all cords for a given col
			return BoardUtils.SEVENTH_COLUMN[curPost] && ((candidateOffset == 10) 
					|| (candidateOffset == -6));
		}
		
		
		// knights sitting on 8th column of board
		private static boolean isEighthColumnExclusion(final int curPost, final int candidateOffset) {
			
			// all cords for a given col
			return BoardUtils.EIGHTH_COLUMN[curPost] && ((candidateOffset == 10) || (candidateOffset == -15)
					|| (candidateOffset == 17) || (candidateOffset == -6));
		}
		
		
		
		@Override
		public String toString() {
			return Piece.PieceType.KNIGHT.toString();
		}
		
		
		@Override
		public Knight movePiece(final Move move) {
			// TODO Auto-generated method stub
			
			// creates new bishop in same alliance but new location
			return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
		}
		
		

		
	

}
