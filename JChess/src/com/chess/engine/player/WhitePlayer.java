package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move.*;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

public class WhitePlayer extends Player{

	public WhitePlayer(final Board board, Collection<Move> whiteStandardLegalMoves,
			final Collection<Move> blackStandardLegalMoves) {
		// TODO Auto-generated constructor stub
		super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		// TODO Auto-generated method stub
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		// TODO Auto-generated method stub
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		// TODO Auto-generated method stub
		return this.board.blackPlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
			final Collection<Move> opponentsLegals) {
		// TODO Auto-generated method stub
		
		final List<Move> kingCastles = new ArrayList<>();
		
		// first move and not in check
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			
			// only if second last two tiles (62 & 61 ) from back are not occupied , for king castles in white
			// white king side castle
			if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(63);
				
				// checks if there is a rook
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					
					// check if all tiles in between arent being attacked
					if(Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty()
							&& Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook()) {
						
						kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62,
								(Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
					}
					
				}
				
			}
			
			// white queen side castle
			if(!this.board.getTile(59).isTileOccupied() && 
					!this.board.getTile(58).isTileOccupied() && 
					!this.board.getTile(57).isTileOccupied()) {
				
				final Tile rookTile = this.board.getTile(56);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					
					if(Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty()
							&& Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook()) {
						
						kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58,
								(Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
					}
					
				
				}
			}
		}
		
		return ImmutableList.copyOf(kingCastles);
	}

}
