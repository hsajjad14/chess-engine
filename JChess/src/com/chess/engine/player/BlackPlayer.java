package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

public class BlackPlayer extends Player {

	public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves,
			final Collection<Move> blackStandardLegalMoves) {
		// TODO Auto-generated constructor stub
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		// TODO Auto-generated method stub
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		// TODO Auto-generated method stub
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		// TODO Auto-generated method stub
		return this.board.whitePlayer();
	}
	
	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, 
			final Collection<Move> opponentsLegals) {
		// TODO Auto-generated method stub
		
		final List<Move> kingCastles = new ArrayList<>();
		
		// first move and not in check
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			
			// only if second last two tiles (5 & 6 ) from back are not occupied , for king castles in black
			// black king side castle
			if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(7);
				
				// checks if there is a rook
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					
					// check if all tiles in between arent being attacked
					if(Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty()
							&& Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook()) {
						
						// 5 is where rook will go 6 is for king
						kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 6,
								(Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
					
					}
					
				}
				
			}
			
			// white queen side castle
			if(!this.board.getTile(1).isTileOccupied() && 
					!this.board.getTile(2).isTileOccupied() && 
					!this.board.getTile(3).isTileOccupied()) {
				
				final Tile rookTile = this.board.getTile(0);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					
					// check if all tiles in between arent being attacked
					if(Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty()
							&& Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook()) {
						
						kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 2,
								(Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
						
					}
					
				
				}
			}
		}
		
		return ImmutableList.copyOf(kingCastles);
	}

}
