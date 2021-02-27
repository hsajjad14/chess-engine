package com.chess.engine.board;

public class BoardUtils {
	
	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);
	public static final boolean[] EIGHTH_COLUMN = initColumn(7);
	
	//for pawn jumps
	public static final boolean[] SEVENTH_RANK = initRow(8); // tile id that begins the row
	public static final boolean[] SECOND_RANK = initRow(48);
	
	
	public static final int NUM_TILES = 64;
	public static final int NUM_TILES_PER_ROW = 8;
	
	public static final boolean[] EIGHTH_RANK = initRow(0);
	public static final boolean[] SIXTH_RANK = initRow(16);
	public static final boolean[] FOURTH_RANK = initRow(32);
	public static final boolean[] FIFTH_RANK = initRow(24);
	public static final boolean[] THIRD_RANK = initRow(40);
	public static final boolean[] FIRST_RANK = initRow(56);
	
	private BoardUtils() {
		throw new RuntimeException("cant instantiate board utils!");
	}

	private static boolean[] initRow(int rowNum) {
		// TODO Auto-generated method stub
		
		final boolean[] row = new boolean[64];
		
		do {
			row[rowNum] = true;
			rowNum++;
			
		}while(rowNum % NUM_TILES_PER_ROW != 0);
		
		return row;
	}

	private static boolean[] initColumn(int colNum) {
		// TODO Auto-generated method stub
		final boolean[] column = new boolean[64];
		
		do {
			column[colNum] = true;
			colNum+=NUM_TILES_PER_ROW;
		}while(colNum < NUM_TILES);
		
		return column;
		
		
	}

	public static boolean isValidTileCoord(final int coordinate) {
		// TODO Auto-generated method stub
		if (coordinate >= 0 && coordinate < NUM_TILES) {
			return true;
		}
		return false;
	}
	
	
	

}
