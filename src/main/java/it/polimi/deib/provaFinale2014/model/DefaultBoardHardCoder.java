package it.polimi.deib.provaFinale2014.model;

/**
 * Classe che gestisce il mapping della plancia di default
 */
public class DefaultBoardHardCoder {
	/**
	 * Inizializza la plancia
	 * @param regions regioni della plancia
	 * @param roads strade della plancia
	 * @param sheepsburg 
	 */
	public void initialize(Region[] regions, Road[] roads) {
		initializeRegions(regions);
		initializeRoads(regions, roads);
		initializeAdjacentRoadsForRegions(regions, roads);
		initializeAdjacentRoadsForRoads(roads);
	}
	
	/**
	 * Inizializza le regioni della plancia
	 * @param regions regioni della plancia
	 */
	private void initializeRegions(Region[] regions) {
		for (int i = 0; i < DefaultBoard.REGIONS; i++) {
			regions[i] = new Region();
			regions[i].setType(TerrainType.values()[i / DefaultBoard.REGIONSPERTYPE]);
		}
	}
	
	/**
	 * Inizializza le strade della plancia e imposta la coppia di regioni adiacenti ad ogni strada
	 * @param regions regioni della plancia
	 * @param roads strade della plancia
	 * @param sheepsburg
	 */
	private void initializeRoads(Region[] regions, Road[] roads) {
		// Array di valori delle strade della plancia di defualt
		final int[] values = {
				2, 3, 1, 2, 3, 4, 2, 1, 4, 3, 2, 1, 5, 2, 
				6, 1, 4, 5, 2, 6, 1, 3, 2, 5, 3, 1, 2, 3, 
				6, 4, 5, 6, 1, 5, 6, 4, 5, 3, 1, 2, 4, 1
		};
		// Array di regioni adiacenti alle strade della plancia di default
		final int[] firstAdjacencies = {
				0, 0, 0, 1, 1, 1, 2, 3, 2, 4, 4, 5, 6, 5, 
				7, 7, 8, 9, 8, 10, 10, 11, 12, 11, 13, 13,
				14, 15, 14, 16, 14, 17, 2, 2, 2, 5, 5, 8,
				8, 11, 11, 14
		};
		final int[] secondAdjacencies = {
				16, 17, 1, 17, 2, 3, 3, 4, 4, 5, 6, 6, 7,
				7, 8, 9, 9, 10, 10, 11, 12, 12, 13, 13, 14,
				15, 15, 16, 16, 17, 17, 18, 17, 18, 5, 18,
				8, 18, 11, 18, 14, 18
		};
		// Inizializza le strade della plancia di default
		// e le regioni a loro adiacenti
		for (int i = 0; i < DefaultBoard.ROADS; i++) {
			roads[i] = new Road(values[i], regions[firstAdjacencies[i]], regions[secondAdjacencies[i]]);
		}
	}
	
	/**
	 * Configura le strade adiacenti ad ogni strada
	 * @param regions le regioni della plancia
	 * @param roads le strade della plancia
	 * @param sheepsburg
	 */
	private void initializeAdjacentRoadsForRegions(Region[] regions, Road[] roads) {
		// Matrice delle strade adiacenti alle regioni della plancia di default
		final int[][] adjacencies = {
				{0, 1, 2},
				{2, 3, 4, 5},
				{4, 6, 8, 32, 33, 34},
				{5, 6, 7},
				{7, 8, 9, 10},
				{9, 11, 13, 34, 35, 36},
				{10, 11, 12},
				{12, 13, 14, 15},
				{14, 16, 18, 36, 37, 38},
				{15, 16, 17},
				{17, 18, 19, 20},
				{19, 21, 23, 38, 39, 40},
				{20, 21, 22},
				{22, 23, 24, 25},
				{24, 26, 28, 30, 40, 41},
				{25, 26, 27},
				{27, 28, 29, 0},
				{1, 3, 29, 30, 31, 32},
				{31, 33, 35, 37, 39, 41}
		};
		// Inizializza la matrice di adiacenza
		Road[][] adjacentRoads = new Road[DefaultBoard.REGIONS][];
		for (int i = 0; i < DefaultBoard.REGIONS; i++) {
			adjacentRoads[i] = new Road[adjacencies[i].length];
			for (int j = 0; j < adjacencies[i].length; j++) {
				adjacentRoads[i][j] = roads[adjacencies[i][j]];
			}
		}
		// Inizializza le adiacenze
		for (int i = 0; i < DefaultBoard.REGIONS; i++) {
			regions[i].configureAdjacentRoads(adjacentRoads[i]);
		}
	}
	/**
	 * Configura le strade che delimitano una regione
	 * @param regions le regioni della plancia
	 * @param roads le strade della plancia
	 * @param sheepsburg
	 */
	private void initializeAdjacentRoadsForRoads(Road[] roads) {
		// Matrice delle strade adiacenti alle strade della plancia di default
		final int[][] adjacencies = {
				{1, 29}, {0, 2, 3, 29}, {1, 3}, 
				{1, 2, 4, 32}, {3, 32, 5, 6}, {4, 6}, 
				{4, 5, 7, 8}, {6, 8}, {6, 7, 9, 34}, 
				{8, 10, 11, 34}, {9, 11}, {9, 10, 12, 13}, 
				{11, 13}, {11, 12, 14, 36}, {13, 15, 16, 36},
				{14, 16}, {14, 15, 17, 18}, {16, 18}, 
				{16, 17, 19, 38}, {18, 20, 21, 38}, {19, 21}, 
				{19, 20, 22, 23}, {21, 23}, {21, 22, 24, 40}, 
				{23, 25, 26, 40}, {24, 26}, {24, 25, 27, 28}, 
				{26, 28}, {26, 27, 29, 30}, {0, 1, 28, 30}, 
				{28, 29, 31, 41}, {30, 32, 33, 41}, {3, 4, 31, 33}, 
				{31, 32, 34, 35}, {8, 9, 33, 35}, {33, 34, 36, 37}, 
				{13, 14, 35, 37}, {35, 36, 38, 39}, {18, 19, 37, 39}, 
				{37, 38, 40, 41}, {23, 24, 39, 41}, {30, 31, 39, 40}
		};
		// Inizializza la matrice di adiacenza
		Road[][] adjacentRoads = new Road[DefaultBoard.ROADS][];
		for (int i = 0; i < DefaultBoard.ROADS; i++) {
			adjacentRoads[i] = new Road[adjacencies[i].length];
			for (int j = 0; j < adjacencies[i].length; j++) {
				adjacentRoads[i][j] = roads[adjacencies[i][j]];
			}
		}
		// Inizializza le adiacenze
		for (int i = 0; i < DefaultBoard.ROADS; i++) {
			roads[i].configureAdjacentRoads(adjacentRoads[i]);
		}
	}
}
