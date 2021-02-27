package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;



import javax.imageio.ImageIO;
import javax.swing.*;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

public class Table {
	
	// two tile colours
	private final Color lightTileColor = Color.decode("#FFFACD");
	private final Color darkTileColor = Color.decode("#593E1A");
	
	private BoardDirection boardDirection;
	private Board chessBoard;
	
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);

	public static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);

	public static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
	
	private final JFrame gameFrame;
	
	private final BoardPanel boardPanel;
	
	private static String defaultPieceImagesPath = "images/";
	
	
	
	public Table() {
		this.gameFrame = new JFrame("Java Chess");
		this.gameFrame.setLayout(new BorderLayout());
		this.boardDirection = BoardDirection.NORMAL;
		final JMenuBar tableMenuBar = createTableMenuBar();
		
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		
		this.chessBoard = Board.createStandardBoard();
		
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		
		this.gameFrame.setVisible(true);
		
		
		
	}
	private JMenu createPreferencesMenu() {
		 final JMenu preferencesMenu = new JMenu("Preferences");
		 final JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");

		 flipBoardMenuItem.addActionListener(new ActionListener() {
			 @Override
	          public void actionPerformed(final ActionEvent e) {
	              boardDirection = boardDirection.opposite();
	              boardPanel.drawBoard(chessBoard);
	          }
			 });
		 preferencesMenu.add(flipBoardMenuItem); 
		 return preferencesMenu;
	}
	
	private JMenuBar createTableMenuBar() {
		// TODO Auto-generated method stub
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		return tableMenuBar;
		
	}

	private JMenu createFileMenu() {
		// TODO Auto-generated method stub
		final JMenu fileMenu = new JMenu("file");
		final JMenuItem openPGN = new JMenuItem("Load PGN file");
		
		openPGN.addActionListener(new ActionListener() {
			

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("open up that pgn file");
				
			}
		});
		fileMenu.add(openPGN);
		
		// exit
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		fileMenu.add(exitMenuItem);
		
		
		return fileMenu;
		
	}
	
	private class BoardPanel extends JPanel{
		
		final List<TilePanel> boardTiles;
		
		BoardPanel(){
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			
			for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel); // adds it to the list
				add(tilePanel); // adds it to the JPanel BoardPanel
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}

		// repaint method
		public void drawBoard(Board board) {
			// TODO Auto-generated method stub
			
			// remove everything
			removeAll();
			for(TilePanel boardTile : boardDirection.traverse(boardTiles)) {
				boardTile.drawTile(board);
				add(boardTile);
			}
			validate();
			repaint();
			
			
		}
		
	}
	
	private class TilePanel extends JPanel{
		
		// tile coord from 0 - 63
		private final int tileId;
		TilePanel(final BoardPanel boardPane,
				final int tileId){
			
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			this.assignTilePieceIcon(chessBoard);
			
			
			// mouse listener
			addMouseListener(new MouseListener() {
				

				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("entered mouse clicked");

					// TODO Auto-generated method stub
					
					// if player right clicks on a tile with right mouse button, 
					// cancel any piece selection it has
					if(SwingUtilities.isRightMouseButton(e)) {
						System.out.println("	entered right mouse b");
						
						sourceTile = null;
						humanMovedPiece = null;
						destinationTile = null;

					} else if(SwingUtilities.isLeftMouseButton(e)) {
						System.out.println("	entered left mouse b");
						
						if(sourceTile == null) {
							System.out.println("\t\t first click");
							// first click
							// user hasn't picked a source tile
							sourceTile = chessBoard.getTile(tileId);
							humanMovedPiece = sourceTile.getPiece();
							
							if(humanMovedPiece == null) {
								// on empty tile do nothing
								sourceTile = null;
							}
						}else {
							System.out.println("\t\t second click");
							// second click
							// we have priorly selected a sourcetile
							destinationTile = chessBoard.getTile(tileId);
							
							// looks for this move in a list of all legal moves
							final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
							
							// makes the move
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
							
							
							if(transition.getMoveStatus().isDone()) {
								//update chessboard
								chessBoard = transition.getBoard();
								// add move that was made to the move log
							}
							sourceTile = null;
							humanMovedPiece = null;
							destinationTile = null;

						}
						
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								boardPanel.drawBoard(chessBoard);
							}
							
						});
						
					}
					
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			validate();
			
		}
		
		public void drawTile(Board board) {
			// TODO Auto-generated method stub
			
			// redraw tile
			assignTileColor();
			assignTilePieceIcon(board);
			highlightLegals(board);
			validate();
			repaint();
			
		}
		private Collection<Move> pieceLegalMoves(final Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
		
		private void highlightLegals(final Board board) {
            if (true) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("images/green_dot.png")))));
                        }
                        catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

		private void assignTilePieceIcon(final Board board) {
			// to redraw, remove all components before
			this.removeAll();
			
			if(board.getTile(this.tileId).isTileOccupied()) {
				// if occupied tile, draw it
				
				
				try {
					// getPieceAlliance returns black alliance or white alliance
					// to string returns 'black' or 'white'
					// substring 0, 1 returns B or W
					
					// get piece gets Pawn, King etc.
					// to string gets 'P' or 'K' b/c each piece overwrites
					// its toString, to be 
					// Piece.PieceType.PAWN.toString();
					// which gives 'P'
					
					final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath  + 
							board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)
							+ board.getTile(this.tileId).getPiece().toString() + ".gif"));
					// white bishop = WB.gif
					this.add(new JLabel(new ImageIcon(image)));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
		private void assignTileColor() {
			// TODO Auto-generated method stub
			
			// do this so tile colours alternate
			if( BoardUtils.EIGHTH_RANK[this.tileId] ||
					BoardUtils.SIXTH_RANK[this.tileId] ||
					BoardUtils.FOURTH_RANK[this.tileId] ||
					BoardUtils.SECOND_RANK[this.tileId]) {
				
				setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
				
			}else if(BoardUtils.SEVENTH_RANK[this.tileId] ||
					BoardUtils.FIFTH_RANK[this.tileId] ||
					BoardUtils.THIRD_RANK[this.tileId] ||
					BoardUtils.FIRST_RANK[this.tileId]) {
				
				setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);

				
			}
			
			
		}
			
	}
	
	public enum BoardDirection{
		NORMAL {
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				// TODO Auto-generated method stub
				return boardTiles;
			}

			@Override
			BoardDirection opposite() {
				// TODO Auto-generated method stub
				return FLIPPED;
			}
		},
		FLIPPED {
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				// TODO Auto-generated method stub
				return Lists.reverse(boardTiles);
			}

			@Override
			BoardDirection opposite() {
				// TODO Auto-generated method stub
				 return NORMAL;
			}
		};
		
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		abstract BoardDirection opposite();
	}

}
