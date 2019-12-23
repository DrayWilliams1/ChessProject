package finalchess;

import java.util.*;

/**
 * The purpose of this program is to implement a chess playing AI using a
 * minimax algorithm with alpha-beta pruning.
 *
 * @author Drayton Williams and Cameron Hammel
 */
public class FinalChess {

    private static final int BSIZE = 8; // fixed board size

    // Fixed piece colour values
    private static final int WHITE = 0;
    private static final int BLACK = 1;
    private static final int EMPTY = -1;

    private int globalDepth;
    private int kingPosW; // Human King Piece
    private int kingPosB; // AI King

    private int configs = 0; // Amount of board configurations tested by AI

    BoardTile[][] board;

    /**
     * Initial Board layout Uppercase: AI (Black) Lowercase: Human (White)
     * R/r = Rook 
     * N/n = Knight 
     * B/b = Bishop 
     * Q/q = Queen 
     * K/k = King 
     * P/p = Pawn
     *
     * RNBQKBNR 
     * PPPPPPPP 
     * -------- 
     * -------- 
     * -------- 
     * pppppppp 
     * rnbqkbnr
     */

    public FinalChess() {
        String play;
        Scanner input = new Scanner(System.in);

        System.out.println("*************************************************"
                + "*****************");
        System.out.println("-------Welcome to Drayton and Cameron's Chess "
                + "Demonstration!------");
        System.out.println("*************************************************"
                + "*****************");
        System.out.println("Our AI uses alpha-beta pruning to search a "
                + "tree-like ");
        System.out.println("structure for the optimal move against your own. "
                + "Dont be ");
        System.out.println("alarmed, but the AI will assume you are a seasoned"
                + " chess player.");
        System.out.println("The higher the tree depth you provide it, "
                + "the better of ");
        System.out.println("a player it will be so choose a depth wisely. We"
                + " recommend");
        System.out.println("a depth of 4 (for diffculty/speed considerations). "
                + "Good Luck!");
        System.out.println("**************************************************"
                + "****************");
        System.out.println("**************************************************"
                + "****************");
        System.out.print("Please enter a depth of search: ");
        globalDepth = input.nextInt();
        System.out.println();
        System.out.println("The AI will look " + globalDepth + " levels into "
                + "its miniMax structure!");
        System.out.println();
        System.out.println();
        System.out.println("***************************************************"
                + "***************");
        System.out.println("---------------------------GAMEPLAY HELP-----------"
                + "---------------");
        System.out.println("**************************************************"
                + "****************");
        System.out.println("Possible piece moves will be presented to you in "
                + "the form of");
        System.out.println("(x1,y1,x2,y2,captured piece) with no spaces betw"
                + "een them.");
        System.out.println("For example The beginning moves of 6050-6040- "
                + "suggest the you may");
        System.out.println("move the piece located at (6,0) to the locations "
                + "of (5,0) or (4,0)");
        System.out.println("With the - representing that you will be capturing"
                + " an empty (or null)");
        System.out.println("piece. When a pawn promotion move is available, "
                + "there will be a 'P'");
        System.out.println("on the end of the move");
        System.out.println("************************************************"
                + "******************");
        System.out.println();

        for (;;) {
            System.out.print("Are you ready to play? (y/n) ");
            play = input.next();
            System.out.println();

            if ("y".equals(play)) { // Yes selected
                String yourMove;

                System.out.println();
                System.out.println("Beginning Game!");
                System.out.println();

                this.board = new BoardTile[BSIZE][BSIZE]; // board of 64 tiles

                initLayout();

                printBoard();
                System.out.println();

                for (;;) {
                    configs = 0;
                    ChessPiece wKingExist = null;
                    ChessPiece bKingExist = null;

                    // locate human (White) king piece on board
                    while (!(board[kingPosW / BSIZE][kingPosW % BSIZE]
                            .getPiece().getPieceName() == 'k'
                            && board[kingPosW / BSIZE][kingPosW % BSIZE]
                            .getPiece().getColourVal() == WHITE)) {
                        kingPosW++;
                    }

                    // locate AI (Black) king piece on board
                    while (!(board[kingPosB / BSIZE][kingPosB % BSIZE]
                            .getPiece().getPieceName() == 'k'
                            && board[kingPosB / BSIZE][kingPosB % BSIZE]
                            .getPiece().getColourVal() == BLACK)) {
                        kingPosB++;
                    }
                    if (allPossibleMoves().isEmpty()) {
                        System.out.println("**********************"
                                + "**********************");
                        System.out.println("**********************"
                                + "**********************");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("-----------STALEMATE! "
                                + "THE AI WINS!----------");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("**********************"
                                + "**********************");
                        break;
                    }

                    System.out.println("Your Possible Moves: "
                            + allPossibleMoves());
                    System.out.print("Please enter your move: ");
                    for (;;) {
                        yourMove = input.next();
                        if (valid(allPossibleMoves(), yourMove)) {
                            move(yourMove);
                            System.out.println();
                            System.out.println("*****************"
                                    + "******************");
                            System.out.println("Your move Produced...");
                            System.out.println("*****************"
                                    + "******************");
                            printBoard();
                            System.out.println("*****************"
                                    + "******************");
                            System.out.println();
                            flipBoard();
                            break;
                        } else {
                            System.out.print("Invalid/Unavailable move!"
                                    + " Please enter again: ");
                        }
                    }

                    
                    System.out.println("AI is thinking...");
                    if ((ABPrune(globalDepth, Integer.MIN_VALUE,
                            Integer.MAX_VALUE, "", 0)).isEmpty()) {
                        System.out.println("**********************"
                                + "**********************");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("-------------STALEMATE! "
                                + "YOU WIN!------------");
                        System.out.println("*********************"
                                + "***********************");
                        System.out.println("*************************"
                                + "*******************");
                        System.out.println("************************"
                                + "********************");
                        break;
                    }
                    move(ABPrune(globalDepth, Integer.MIN_VALUE,
                            Integer.MAX_VALUE, "", 0));
                    flipBoard();
                    System.out.println("***********************************");
                    System.out.println("AIs move Produced...");
                    System.out.println("***********************************");
                    printBoard();
                    System.out.println("***********************************");
                    System.out.println("AI's Board Configurations Tested: "
                            + configs);
                    System.out.println();

         // For loop to check for possible checkmate after last moves were made
                    for (int i = 0; i < board.length; i++) {
                        for (int j = 0; j < board.length; j++) {
                            if (board[i][j].getPiece().getPieceName() == 'k'
                                    && board[i][j].getPiece().getColourVal()
                                    == BLACK) {
                                bKingExist = board[i][j].getPiece();
                            }
                            if (board[i][j].getPiece().getPieceName() == 'k'
                                    && board[i][j].getPiece().getColourVal()
                                    == WHITE) {
                                wKingExist = board[i][j].getPiece();
                            }
                        }
                    }

                 // Checking the existence of either players king for checkmate
                    if (wKingExist == null) {
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("*************************"
                                + "*******************");
                        System.out.println("************************"
                                + "********************");
                        System.out.println("------------CHECKMATE! "
                                + "THE AI WINS!---------");
                        System.out.println("**********************"
                                + "**********************");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("************************"
                                + "********************");
                        break;
                    }
                    if (bKingExist == null) {
                        System.out.println("*********************"
                                + "***********************");
                        System.out.println("***********************"
                                + "*********************");
                        System.out.println("**********************"
                                + "**********************");
                        System.out.println("************************"
                                + "********************");
                        System.out.println("--------------CHECKMATE! "
                                + "YOU WIN!-----------");
                        System.out.println("*************************"
                                + "*******************");
                        System.out.println("**************************"
                                + "******************");
                        System.out.println("************************"
                                + "********************");
                        break;
                    }
                }
                break;
            } else if ("n".equals(play)) { // No selected
                System.out.println("This is a final project."
                        + "..You are playing!");
                System.out.println("Take your time :)");
            } else { // Anything else
                System.out.println("No silly, it has got to be "
                        + "a yes or no....");
            }
        }
    }

    /**
     * Initializes the chess board with black and white pieces
     */
    public void initLayout() {
        // initialize board with empty squares in even rows
        for (int i = 0; i < board.length; i = i + 2) {
            for (int j = 0; j < board.length; j++) {
                // if an even space in the row --> white tile
                if ((j % 2 == 0)) { // white empty tile
                    this.board[i][j] = new BoardTile(i, j, WHITE);
                    placePiece(new NullPiece(EMPTY), i, j);
                } else { // black empty tile
                    this.board[i][j] = new BoardTile(i, j, BLACK);
                    placePiece(new NullPiece(EMPTY), i, j);
                }
            }
        }
        // initialize board with empty squares in odd rows
        for (int i = 1; i < board.length; i = i + 2) {
            for (int j = 0; j < board.length; j++) {
                // if an even space in the row --> black tile
                if ((j % 2 == 0)) { // black empty tile
                    this.board[i][j] = new BoardTile(i, j, BLACK);
                    placePiece(new NullPiece(EMPTY), i, j);
                } else { // white empty tile
                    this.board[i][j] = new BoardTile(i, j, WHITE);
                    placePiece(new NullPiece(EMPTY), i, j);
                }
            }
        }

        // Placing black pawns on initial board for each player
        for (int i = 0; i < board.length; i++) {
            placePiece(new Pawn(BLACK), 1, i);
        }
        // Placing white pawns on initial board for each player
        for (int i = 0; i < board.length; i++) {
            placePiece(new Pawn(WHITE), 6, i);
        }

        // Placing Rooks
        placePiece(new Rook(BLACK), 0, 0);
        placePiece(new Rook(BLACK), 0, 7);
        placePiece(new Rook(WHITE), 7, 0);
        placePiece(new Rook(WHITE), 7, 7);

        // Placing Knights
        placePiece(new Knight(BLACK), 0, 1);
        placePiece(new Knight(BLACK), 0, 6);
        placePiece(new Knight(WHITE), 7, 1);
        placePiece(new Knight(WHITE), 7, 6);

        // Placing Bishops
        placePiece(new Bishop(BLACK), 0, 2);
        placePiece(new Bishop(BLACK), 0, 5);
        placePiece(new Bishop(WHITE), 7, 2);
        placePiece(new Bishop(WHITE), 7, 5);

        // Placing Kings
        placePiece(new King(BLACK), 0, 4);
        placePiece(new King(WHITE), 7, 4);

        // Placing Queens
        placePiece(new Queen(BLACK), 0, 3);
        placePiece(new Queen(WHITE), 7, 3);
    }

    /**
     * Returns all possible moves for a player to make on the given chess board
     * configuration
     *
     * @return x1 ,y1 ,x2 ,y2 ,possible captured piece
     */
    public String allPossibleMoves() {
        ChessPiece testPiece;
        String moveList = "";
        int row, col;

        for (int i = 0; i < BSIZE * BSIZE; i++) {
            row = i / BSIZE;
            col = i % BSIZE;

            // check for white piece moves
            if (board[row][col].getPiece().getColourVal() == WHITE) {
                testPiece = board[row][col].getPiece();

                switch (testPiece.getPieceName()) {
                    case 'p': { // white pawn
                        moveList = moveList + generateMovesP(i, row, col);
                        break;
                    }
                    case 'n': { // white knight
                        moveList = moveList + generateMovesN(row, col);
                        break;
                    }
                    case 'b': { // white bishop
                        moveList = moveList + generateMovesB(row, col);
                        break;
                    }
                    case 'r': { // white rook
                        moveList = moveList + generateMovesR(row, col);
                        break;
                    }
                    case 'q': { // white queen
                        moveList = moveList + generateMovesQ(row, col);
                        break;
                    }
                    case 'k': { // white king
                        moveList = moveList + generateMovesK(i, row, col);
                        break;
                    }
                }
            }

        }
        return moveList;
    }

    /**
     * Returns all possible moves for the rooks
     *
     * @param x the row being tested on
     * @param y the column being tested on
     * @return all possible moves for the rooks
     */
    public String generateMovesR(int x, int y) {
        int increment = 1;
        String pieceList = "";
        ChessPiece oldPiece;

        for (int i = -1; i <= 1; i += 2) {
            try { // horizaontal (column) move check
                while (board[x][y + increment * i].getPiece().getColourVal()
                        == EMPTY) {
                    oldPiece = board[x][y + increment * i].getPiece();
                    board[x][y].removePiece();
                    board[x][y].setPiece(new NullPiece(EMPTY));
                    board[x][y + increment * i].removePiece();
                    board[x][y + increment * i].setPiece(new Rook(WHITE));

                    if (safeKingMove()) {
                        pieceList = pieceList + (x) + (y) + (x)
                                + (y + increment * i) + oldPiece.getPieceName();

                    }
                    board[x][y].removePiece();
                    board[x][y].setPiece(new Rook(WHITE));
                    board[x][y + increment * i].removePiece();
                    board[x][y + increment * i].setPiece(oldPiece);

                    increment++;
                }
                if (board[x][y + increment * i].getPiece().getColourVal()
                        == BLACK) {
                    oldPiece = board[x][y + increment * i].getPiece();
                    board[x][y].removePiece();
                    board[x][y].setPiece(new NullPiece(EMPTY));
                    board[x][y + increment * i].removePiece();
                    board[x][y + increment * i].setPiece(new Rook(WHITE));

                    if (safeKingMove()) {
                        pieceList = pieceList + (x) + (y) + (x)
                                + (y + increment * i) + oldPiece.getPieceName();

                    }
                    board[x][y].removePiece();
                    board[x][y].setPiece(new Rook(WHITE));
                    board[x][y + increment * i].removePiece();
                    board[x][y + increment * i].setPiece(oldPiece);
                }
            } catch (Exception ex) {
            }

            increment = 1;
            try { // vertical (row) move check
                while (board[x + increment * i][y].getPiece().getColourVal()
                        == EMPTY) {
                    oldPiece = board[x + increment * i][y].getPiece();
                    board[x][y].removePiece();
                    board[x][y].setPiece(new NullPiece(EMPTY));
                    board[x + increment * i][y].removePiece();
                    board[x + increment * i][y].setPiece(new Rook(WHITE));

                    if (safeKingMove()) {
                        pieceList = pieceList + (x) + (y) + (x + increment * i)
                                + (y) + oldPiece.getPieceName();

                    }
                    board[x][y].removePiece();
                    board[x][y].setPiece(new Rook(WHITE));
                    board[x + increment * i][y].removePiece();
                    board[x + increment * i][y].setPiece(oldPiece);

                    increment++;
                }
                if (board[x + increment * i][y].getPiece().getColourVal()
                        == BLACK) {
                    oldPiece = board[x + increment * i][y].getPiece();
                    board[x][y].removePiece();
                    board[x][y].setPiece(new NullPiece(EMPTY));
                    board[x + increment * i][y].removePiece();
                    board[x + increment * i][y].setPiece(new Rook(WHITE));

                    if (safeKingMove()) {
                        pieceList = pieceList + (x) + (y) + (x + increment * i)
                                + (y) + oldPiece.getPieceName();

                    }
                    board[x][y].removePiece();
                    board[x][y].setPiece(new Rook(WHITE));
                    board[x + increment * i][y].removePiece();
                    board[x + increment * i][y].setPiece(oldPiece);
                }
            } catch (Exception ex) {
            }
            increment = 1;
        }
        return pieceList;
    }

    /**
     * Returns all possible moves for the knights
     *
     * @param x the row being tested on
     * @param y the column being tested on
     * @return all possible moves for the knights
     */
    public String generateMovesN(int x, int y) {
        String pieceList = "";
        ChessPiece oldPiece;

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    if (board[x + i][y + j * 2].getPiece().getColourVal()
                            != WHITE) {
                        oldPiece = board[x + i][y + j * 2].getPiece();
                        board[x][y].removePiece();
                        board[x][y].setPiece(new NullPiece(EMPTY));
                        if (safeKingMove()) {
                            pieceList = pieceList + (x) + (y) + (x + i)
                                    + (y + j * 2) + oldPiece.getPieceName();
                        }
                        board[x][y].removePiece();
                        board[x][y].setPiece(new Knight(WHITE));
                        board[x + i][y + j * 2].removePiece();
                        board[x + i][y + j * 2].setPiece(oldPiece);
                    }
                } catch (Exception ex) {
                }

                try {
                    if (board[x + i * 2][y + j].getPiece().getColourVal()
                            != WHITE) {
                        oldPiece = board[x + i * 2][y + j].getPiece();
                        board[x][y].removePiece();
                        board[x][y].setPiece(new NullPiece(EMPTY));
                        if (safeKingMove()) {
                            pieceList = pieceList + (x) + (y) + (x + i * 2)
                                    + (y + j) + oldPiece.getPieceName();

                        }
                        board[x][y].removePiece();
                        board[x][y].setPiece(new Knight(WHITE));
                        board[x + i * 2][y + j].removePiece();
                        board[x + i * 2][y + j].setPiece(oldPiece);
                    }
                } catch (Exception ex) {
                }
            }
        }

        return pieceList;
    } // converted

    /**
     * Returns all possible moves for the bishops
     *
     * @param x the row being tested on
     * @param y the column being tested on
     * @return all possible moves for the bishops
     */
    public String generateMovesB(int x, int y) {
        int increment = 1;
        String pieceList = "";
        ChessPiece oldPiece;

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try { // try maintains that operations are performed while 
                    // piece is within board. Null pointers are caught
                    while (board[x + increment * i][y + increment * j]
                            .getPiece().getColourVal() == EMPTY) {
                        oldPiece = board[x + increment * i][y + increment * j]
                                .getPiece();
                        board[x][y].removePiece();
                        board[x][y].setPiece(new NullPiece(EMPTY));
                        board[x + increment * i][y + increment * j]
                                .removePiece();
                        board[x + increment * i][y + increment * j]
                                .setPiece(new Bishop(WHITE));

                        if (safeKingMove()) {
                            pieceList = pieceList + (x) + (y)
                                    + (x + increment * i) + (y + increment * j)
                                    + oldPiece.getPieceName();
                        }
                        board[x][y].removePiece();
                        board[x][y].setPiece(new Bishop(WHITE));
                        board[x + increment * i][y + increment * j]
                                .removePiece();
                        board[x + increment * i][y + increment * j]
                                .setPiece(oldPiece);
                        increment++;
                    }
                    if (board[x + increment * i][y + increment * j]
                            .getPiece().getColourVal() == BLACK) {
                        oldPiece = board[x + increment * i][y + increment * j]
                                .getPiece();
                        board[x][y].removePiece();
                        board[x][y].setPiece(new NullPiece(EMPTY));
                        board[x + increment * i][y + increment * j]
                                .removePiece();
                        board[x + increment * i][y + increment * j]
                                .setPiece(new Bishop(WHITE));

                        if (safeKingMove()) {
                            pieceList = pieceList + (x) + (y)
                                    + (x + increment * i) + (y + increment * j)
                                    + oldPiece.getPieceName();

                        }
                        board[x][y].removePiece();
                        board[x][y].setPiece(new Bishop(WHITE));
                        board[x + increment * i][y + increment * j]
                                .removePiece();
                        board[x + increment * i][y + increment * j]
                                .setPiece(oldPiece);
                    }

                } catch (Exception ex) {
                }
                increment = 1; // resets increment when checking 
            }
        }

        return pieceList;
    } // converted

    /**
     * Returns all possible moves for the pawns
     *
     * @param totalPos the one dimensional location of the piece (out of 64)
     * @param x the row being tested on
     * @param y the column being tested on
     * @return all possible moves for the pawns
     */
    public String generateMovesP(int totalPos, int x, int y) {
        String pieceList = "";
        ChessPiece oldPiece;

        for (int i = -1; i <= 1; i += 2) {
            try { // captures
                if (board[x - 1][y + i].getPiece().getColourVal() == BLACK
                        && totalPos >= 16) {
                    oldPiece = board[x - 1][y + i].getPiece();
                    board[x][y].removePiece();
                    board[x][y].setPiece(new NullPiece(EMPTY));
                    board[x - 1][y + i].removePiece();
                    board[x - 1][y + i].setPiece(new Pawn(WHITE));

                    if (safeKingMove()) {
                        pieceList = pieceList + (x) + (y) + (x - 1) + (y + i)
                                + oldPiece.getPieceName();

                    }

                    board[x][y].removePiece();
                    board[x][y].setPiece(new Pawn(WHITE));
                    board[x - 1][y + i].removePiece();
                    board[x - 1][y + i].setPiece(oldPiece);
                }
            } catch (Exception ex) {
            }

            try { // promotion + possible captures
                if (board[x - 1][y + i].getPiece().getColourVal() == BLACK
                        && totalPos < 16) {
                    // Possible pieces for pawn to be promoted to
                    ChessPiece[] promoOptions = {new Queen(WHITE),
                        new Rook(WHITE), new Bishop(WHITE), new Knight(WHITE)};
                    for (int j = 0; j < 4; j++) {
                        oldPiece = board[x - 1][y + i].getPiece();
                        board[x][y].removePiece();
                        board[x][y].setPiece(new NullPiece(EMPTY));
                        board[x - 1][y + i].removePiece();
                        board[x - 1][y + i].setPiece(promoOptions[j]);

                        // y1 (1), y2 (2), captured piece (3), new piece (4), 
                        // P (promotion) (5)
                        if (safeKingMove()) {
                            pieceList = pieceList + (y) + (y + j)
                                    + oldPiece.getPieceName()
                                    + promoOptions[j].getPieceName() + "P";

                        }

                        board[x][y].removePiece();
                        board[x][y].setPiece(new Pawn(WHITE));
                        board[x - 1][y + i].removePiece();
                        board[x - 1][y + i].setPiece(oldPiece);
                    }

                }
            } catch (Exception ex) {
            }
        }

        try { // Piece move up 1 space
            if (board[x - 1][y].getPiece().getColourVal() == EMPTY
                    && totalPos >= 16) {
                oldPiece = board[x - 1][y].getPiece();
                board[x][y].removePiece();
                board[x][y].setPiece(new NullPiece(EMPTY));
                board[x - 1][y].removePiece();
                board[x - 1][y].setPiece(new Pawn(WHITE));

                if (safeKingMove()) {
                    pieceList = pieceList + (x) + (y) + (x - 1) + (y)
                            + oldPiece.getPieceName();

                }

                board[x][y].removePiece();
                board[x][y].setPiece(new Pawn(WHITE));
                board[x - 1][y].removePiece();
                board[x - 1][y].setPiece(oldPiece);
            }
        } catch (Exception ex) {
        }

        try { // promotion w/o captures
            if (board[x - 1][y].getPiece().getColourVal() == EMPTY
                    && totalPos < 16) {
                // Possible pieces for pawn to be promoted to
                ChessPiece[] promoOptions = {new Queen(WHITE),
                    new Rook(WHITE), new Bishop(WHITE), new Knight(WHITE)};
                for (int j = 0; j < 4; j++) {
                    oldPiece = board[x - 1][y].getPiece();
                    board[x][y].removePiece();
                    board[x][y].setPiece(new NullPiece(EMPTY));
                    board[x - 1][y].removePiece();
                    board[x - 1][y].setPiece(promoOptions[j]);

                    if (safeKingMove()) {
                        pieceList = pieceList + (y) + (y + j)
                                + oldPiece.getPieceName() + promoOptions[j]
                                .getPieceName() + "P";

                    }

                    board[x][y].removePiece();
                    board[x][y].setPiece(new Pawn(WHITE));
                    board[x - 1][y].removePiece();
                    board[x - 1][y].setPiece(oldPiece);
                }

            }
        } catch (Exception ex) {
        }

        try { // Piece move up 2 spaces
            if (board[x - 1][y].getPiece().getColourVal() == EMPTY
                    && board[x - 2][y].getPiece().getColourVal() == EMPTY
                    && totalPos >= 48) {
                oldPiece = board[x - 2][y].getPiece();
                board[x][y].removePiece();
                board[x][y].setPiece(new NullPiece(EMPTY));
                board[x - 2][y].removePiece();
                board[x - 2][y].setPiece(new Pawn(WHITE));

                if (safeKingMove()) {
                    pieceList = pieceList + (x) + (y) + (x - 2) + (y)
                            + oldPiece.getPieceName();

                }

                board[x][y].removePiece();
                board[x][y].setPiece(new Pawn(WHITE));
                board[x - 2][y].removePiece();
                board[x - 2][y].setPiece(oldPiece);
            }
        } catch (Exception ex) {
        }

        return pieceList;
    }

    /**
     * Returns all possible moves for the queen
     *
     * @param x the row being tested on
     * @param y the column being tested on
     * @return all possible moves for the queen
     */
    public String generateMovesQ(int x, int y) {
        int increment = 1;
        String pieceList = "";
        ChessPiece oldPiece;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) { // increased efficiency of move search
                    try { // try maintains that operations are performed 
                        // while piece is within board. Null pointers are caught
                        while (board[x + increment * i][y + increment * j]
                                .getPiece().getColourVal() == EMPTY) {
                            oldPiece = board[x + increment * i]
                                    [y + increment * j].getPiece();
                            board[x][y].removePiece();
                            board[x][y].setPiece(new NullPiece(EMPTY));
                            board[x + increment * i][y + increment * j]
                                    .removePiece();
                            board[x + increment * i][y + increment * j]
                                    .setPiece(new Queen(WHITE));

                            if (safeKingMove()) {
                                pieceList = pieceList + (x) + (y)
                                        + (x + increment * i)
                                        + (y + increment * j)
                                        + oldPiece.getPieceName();
                            }
                            board[x][y].removePiece();
                            board[x][y].setPiece(new Queen(WHITE));
                            board[x + increment * i][y + increment * j]
                                    .removePiece();
                            board[x + increment * i][y + increment * j]
                                    .setPiece(oldPiece);
                            increment++;
                        }
                        if (board[x + increment * i][y + increment * j]
                                .getPiece().getColourVal() == BLACK) {
                            oldPiece = board[x + increment * i]
                                    [y + increment * j].getPiece();
                            board[x][y].removePiece();
                            board[x][y].setPiece(new NullPiece(EMPTY));
                            board[x + increment * i][y + increment * j]
                                    .removePiece();
                            board[x + increment * i][y + increment * j]
                                    .setPiece(new Queen(WHITE));

                            if (safeKingMove()) {
                                pieceList = pieceList + (x) + (y)
                                        + (x + increment * i)
                                        + (y + increment * j)
                                        + oldPiece.getPieceName();

                            }
                            board[x][y].removePiece();
                            board[x][y].setPiece(new Queen(WHITE));
                            board[x + increment * i][y + increment * j]
                                    .removePiece();
                            board[x + increment * i][y + increment * j]
                                    .setPiece(oldPiece);
                        }

                    } catch (Exception ex) {
                    }
                    increment = 1; // resets increment when checking 
                }
            }
        }
        return pieceList;
    }

    /**
     * Returns all possible moves for the king
     *
     * @param totalPos the one dimensional location of the piece (out of 64)
     * @param x the row being tested on
     * @param y the column being tested on
     * @return all possible moves for the king
     */
    public String generateMovesK(int totalPos, int x, int y) {
        String pieceList = "";
        ChessPiece oldPiece;

        // 9 used as upper bound for the 9 possible spaces a king can occupy
        // around itself and on its current possition
        for (int i = 0; i < 9; i++) {
            if (i != 4) { // keeps king from making a move to its own 
                // occupied space
                try {
                    if (board[x - 1 + i / 3][y - 1 + i % 3].getPiece()
                            .getColourVal() != WHITE) {

                        oldPiece = board[x - 1 + i / 3][y - 1 + i % 3]
                                .getPiece();
                        board[x][y].removePiece();
                        board[x][y].setPiece(new NullPiece(EMPTY));
                        board[x - 1 + i / 3][y - 1 + i % 3].removePiece();
                        board[x - 1 + i / 3][y - 1 + i % 3]
                                .setPiece(new King(WHITE));

                        int kingTemp = kingPosW;
                        kingPosW = totalPos + (i / 3) * 8 + i % 3 - 9;

                        if (safeKingMove()) {
                            pieceList = pieceList + (x) + (y) + (x - 1 + i / 3)
                                    + (y - 1 + i % 3) + oldPiece.getPieceName();
                        }
                        board[x][y].removePiece();
                        board[x][y].setPiece(new King(WHITE));
                        board[x - 1 + i / 3][y - 1 + i % 3].removePiece();
                        board[x - 1 + i / 3][y - 1 + i % 3].setPiece(oldPiece);
                        kingPosW = kingTemp;
                    }
                } catch (Exception ex) {
                } // Nothing to catch yet
            }
        }
        return pieceList;
    }

    public void placePiece(ChessPiece piece, int x, int y) {
        board[x][y].setPiece(piece);
    }

    /**
     * Monitors the position of the king piece on the board to determine whether
     * the move puts the piece in danger
     *
     * @return true if it's a safe move for the players king piece, false if not
     */
    public boolean safeKingMove() {
        int increment = 1;

        // Bishop/Queen Check (Diagonal)
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                // Check for any bishop or queen diagonally
                try { // try maintains that operations are performed while piece 
                    // is within board. Null pointers are caught
                    while (board[kingPosW / 8 + increment * i][kingPosW % 8
                            + increment * j].getPiece().getColourVal()
                            == EMPTY) {
                        increment++;
                    }
                    // Checks for (Black) AI Bishop
                    if (board[kingPosW / 8 + increment * i]
                            [kingPosW % 8 + increment * j]
                            .getPiece().getPieceName() == 'b'
                            && board[kingPosW / 8 + increment * i]
                                    [kingPosW % 8 + increment * j]
                            .getPiece().getColourVal() == BLACK) {
                        // danger
                        return false;
                    }

                    // Checks for (Black) AI queen
                    if (board[kingPosW / 8 + increment * i]
                            [kingPosW % 8 + increment * j].getPiece()
                            .getPieceName() == 'q'
                            && board[kingPosW / 8 + increment * i]
                                    [kingPosW % 8 + increment * j]
                            .getPiece().getColourVal() == BLACK) {
                        // danger
                        return false;
                    }
                } catch (Exception ex) {
                }
                increment = 1;
            }
        }

        // Rook/Queen Check (Horizontal and Vertical)
        for (int i = -1; i <= 1; i += 2) {
            // Check for rook or queen horizontally (column-wise)
            try { // try maintains that operations are performed while piece is
                // within board. Null pointers are caught
                while (board[kingPosW / BSIZE][kingPosW % BSIZE + increment * i]
                        .getPiece().getColourVal() == EMPTY) {
                    increment++;
                }
                // Checks for (Black) AI Rook
                if (board[kingPosW / BSIZE][kingPosW % BSIZE + increment * i]
                        .getPiece().getPieceName() == 'r'
                        && board[kingPosW / BSIZE]
                                [kingPosW % BSIZE + increment * i].getPiece()
                        .getColourVal() == BLACK) {
                    // danger
                    return false;
                }

                if (board[kingPosW / BSIZE][kingPosW % BSIZE + increment * i]
                        .getPiece().getPieceName() == 'q'
                        && board[kingPosW / BSIZE]
                                [kingPosW % BSIZE + increment * i]
                        .getPiece().getColourVal() == BLACK) {
                    // danger
                    return false;
                }
            } catch (Exception ex) {
            }
            increment = 1;

            // Check for rook or queen vertically (row-wise)
            try { // try maintains that operations are performed while piece 
                // is within board. Null pointers are caught
                while (board[kingPosW / BSIZE + increment * i][kingPosW % BSIZE]
                        .getPiece().getColourVal() == EMPTY) {
                    increment++;
                }
                // Checks for (Black) AI Rook
                if (board[kingPosW / BSIZE + increment * i][kingPosW % BSIZE]
                        .getPiece().getPieceName() == 'r'
                        && board[kingPosW / BSIZE + increment * i]
                                [kingPosW % BSIZE].getPiece()
                        .getColourVal() == BLACK) {
                    // danger
                    return false;
                }

                if (board[kingPosW / BSIZE + increment * i][kingPosW % BSIZE]
                        .getPiece().getPieceName() == 'q'
                        && board[kingPosW / BSIZE + increment * i]
                                [kingPosW % BSIZE].getPiece()
                        .getColourVal() == BLACK) {
                    // danger
                    return false;
                }
            } catch (Exception ex) {
            }
            increment = 1;
        }

        // Knight Check
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                // Check for (Black) AI knight
                try { // try maintains that operations are performed 
                    // while piece is within board. Null pointers are caught

                    // Checks for (Black) AI Knight
                    if (board[kingPosW / BSIZE + i][kingPosW % BSIZE + j * 2]
                            .getPiece().getPieceName() == 'n'
                            && board[kingPosW / BSIZE + i]
                                    [kingPosW % BSIZE + j * 2]
                            .getPiece().getColourVal() == BLACK) {
                        // danger
                        return false;
                    }
                } catch (Exception ex) {
                }
                try { // try maintains that operations are performed while 
                    // piece is within board. Null pointers are caught

                    // Checks for (Black) AI Knight
                    if (board[kingPosW / BSIZE + i * 2][kingPosW % BSIZE + j]
                            .getPiece().getPieceName() == 'n'
                            && board[kingPosW / BSIZE + i * 2][kingPosW % BSIZE 
                                    + j].getPiece()
                            .getColourVal() == BLACK) {
                        // danger
                        return false;
                    }
                } catch (Exception ex) {
                }
            }
        }

        // Pawn Check
        if (kingPosW >= BSIZE * 2) { // king not within top 2 rows
            try {
                if (board[kingPosW / BSIZE - 1][kingPosW % BSIZE - 1]
                        .getPiece().getPieceName() == 'p'
                        && board[kingPosW / BSIZE - 1][kingPosW % BSIZE - 1]
                        .getPiece().getColourVal() == BLACK) {
                    return false;
                }
            } catch (Exception ex) {
            }
            try {
                if (board[kingPosW / BSIZE - 1][kingPosW % BSIZE + 1]
                        .getPiece().getPieceName() == 'p'
                        && board[kingPosW / BSIZE - 1][kingPosW % BSIZE + 1]
                        .getPiece().getColourVal() == BLACK) {
                    return false;
                }
            } catch (Exception ex) {
            }

            // King Check
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        // Check for (Black) AI knight
                        try { // try maintains that operations are performed 
                       // while piece is within board. Null pointers are caught
                            if (board[kingPosW / BSIZE + i]
                                    [kingPosW % BSIZE + j].getPiece()
                                    .getPieceName() == 'k'
                                    && board[kingPosW / BSIZE + i]
                                            [kingPosW % BSIZE + j].getPiece()
                                    .getColourVal() == BLACK) {
                                // danger
                                return false;
                            }
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     *
     * @param depth the current depth of the search
     * @param alpha
     * @param beta
     * @param move
     * @param player the player move being tested and undone
     * @return
     */
    public String ABPrune(int depth, int alpha, int beta,
            String move, int player) {

        String moveList = allPossibleMoves();
        if (depth == 0 || moveList.length() == 0) {
            return move + (totBoardEval(moveList.length(), depth)
                    * (player * 2 - 1));
        }

        player = 1 - player; // makes it so player is either 1 or 0

        for (int i = 0; i < moveList.length(); i += 5) {
            move(moveList.substring(i, i + 5));

            flipBoard(); // flips board so AI can make move without 
            // duplicate of methods

            String rString = ABPrune(depth - 1, alpha, beta,
                    moveList.substring(i, i + 5), player);

            int val = Integer.valueOf(rString.substring(5));
            flipBoard();

            undo(moveList.substring(i, i + 5));

            if (player == 0) {
                if (val <= beta) {
                    beta = val;
                    if (depth == globalDepth) {
                        move = rString.substring(0, 5);
                    }
                }
            } else // player = 1
            {
                if (val > alpha) {
                    alpha = val;
                    if (depth == globalDepth) {
                        move = rString.substring(0, 5);
                    }
                }
            }
            if (alpha >= beta) {
                if (player == 0) {
                    return move + beta;
                } else {
                    return move + alpha;
                }
            }
        }
        if (player == 0) {
            return move + beta;
        } else {
            return move + alpha;
        }
    }

    public void flipBoard() {

        int tempKingPos;
        ChessPiece swapPiece = null;
        ChessPiece[] bPieceOptions = {new Queen(BLACK), new Rook(BLACK),
            new Bishop(BLACK), new Knight(BLACK), new King(BLACK),
            new Pawn(BLACK), new NullPiece(EMPTY)};
        ChessPiece[] wPieceOptions = {new Queen(WHITE), new Rook(WHITE),
            new Bishop(WHITE), new Knight(WHITE), new King(WHITE),
            new Pawn(WHITE), new NullPiece(EMPTY)};

        // for loop will iterate through only half of the board
        for (int i = 0; i < BSIZE * (BSIZE / 2); i++) {
            int row = i / BSIZE;
            int col = i % BSIZE;

            if (board[row][col].getPiece().getColourVal() == WHITE) {
                char pieceMatch = board[row][col].getPiece().getPieceName();

                for (int j = 0; j < bPieceOptions.length; j++) {
                    if (bPieceOptions[j].getPieceName() == pieceMatch) {
                        swapPiece = bPieceOptions[j];
                        break;
                    }
                }
            } else { // make piece white
                char pieceMatch = board[row][col].getPiece().getPieceName();

                for (int j = 0; j < wPieceOptions.length; j++) {
                    if (wPieceOptions[j].getPieceName() == pieceMatch) {
                        swapPiece = wPieceOptions[j];
                        break;
                    }
                }
            }

            if (board[(board.length - 1) - row][(board.length - 1) - col]
                    .getPiece().getColourVal() == WHITE) {
                char pieceMatch = board[(board.length - 1) - row]
                        [(board.length - 1) - col].getPiece().getPieceName();
                for (int j = 0; j < bPieceOptions.length; j++) {
                    if (bPieceOptions[j].getPieceName() == pieceMatch) {
                        board[row][col].removePiece();
                        board[row][col].setPiece(bPieceOptions[j]);

                        break;
                    }
                }
            } else { // make piece white
                char pieceMatch = board[(board.length - 1) - row]
                        [(board.length - 1) - col].getPiece().getPieceName();

                for (int j = 0; j < wPieceOptions.length; j++) {
                    if (wPieceOptions[j].getPieceName() == pieceMatch) {
                        board[row][col].removePiece();
                        board[row][col].setPiece(wPieceOptions[j]);

                        break;
                    }
                }
            }
            board[(board.length - 1) - row][(board.length - 1) - col]
                    .removePiece();
            board[(board.length - 1) - row][(board.length - 1) - col]
                    .setPiece(swapPiece);

            swapPiece = new NullPiece(EMPTY);

        }

        configs++;

        tempKingPos = kingPosW;
        kingPosW = ((BSIZE * BSIZE) - 1) - kingPosB;
        kingPosB = ((BSIZE * BSIZE) - 1) - tempKingPos;
    }

    public int totBoardEval(int list, int depth) {
        int score = 0;
        int material = boardEvalMaterial();
        score += boardEvalAttack();
        score += material;
        score += boardEvalMovability(list, depth);
        score += boardEvalPositional(material);
        flipBoard();
        material = boardEvalMaterial();
        score -= boardEvalAttack();
        score -= material;
        score -= boardEvalMovability(list, depth);
        score -= boardEvalPositional(material);
        flipBoard();
        return -(score + depth * 50);
    }

    /**
     *
     * @return the evaluated score for the ability to attack certain pieces
     */
    public int boardEvalAttack() {
        int score = 0;
        int tempPosW = kingPosW;

        for (int i = 0; i < BSIZE * BSIZE; i++) {
            int row = i / BSIZE;
            int col = i / BSIZE;
            switch (board[row][col].getPiece().getPieceName()) {
                case 'p': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        if (!safeKingMove()) {
                            score -= 70;
                        }
                    }
                    break;
                }
                case 'r': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        if (!safeKingMove()) {
                            score -= 500;
                        }
                    }
                    break;
                }
                case 'n': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        if (!safeKingMove()) {
                            score -= 300;
                        }
                    }
                    break;
                }
                case 'b': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        if (!safeKingMove()) {
                            score -= 300;
                        }
                    }
                    break;
                }
                case 'q': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        if (!safeKingMove()) {
                            score -= 900;
                        }
                    }
                    break;
                }
            }
        }
        kingPosW = tempPosW;
        if (!safeKingMove()) {
            score -= 200;
        }
        return score / 2;
    }

    /**
     *
     * @return the evaluated score for the boards material (chess pieces)
     */
    public int boardEvalMaterial() {
        int score = 0;
        int bishopNum = 0;

        for (int i = 0; i < BSIZE * BSIZE; i++) {
            int row = i / BSIZE;
            int col = i / BSIZE;
            switch (board[row][col].getPiece().getPieceName()) {
                case 'p': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        score += 100;
                    }
                    break;
                }
                case 'r': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        score += 500;
                    }
                    break;
                }
                case 'n': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        score += 320;
                    }
                    break;
                }
                case 'b': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        bishopNum += 1;
                    }
                    break;
                }
                case 'q': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        score += 900;
                    }
                    break;
                }
            }
        }
        if (bishopNum >= 2) {
            score += 330 * bishopNum;
        } else if (bishopNum == 1) { //if one bishop then penalty
            score += 250;
        }
        return score;
    }

    /**
     *
     * @param listLength
     * @param depth
     * @return the evaluated score for the ability for each piece to move around
     * the board
     */
    public int boardEvalMovability(int listLength, int depth) {
        int score = 0;
        score = listLength; // 5 pointer per valid move
        if (listLength == 0) {
            if (!safeKingMove()) { // if a checkmate
                score -= 300000 * depth;
                // wants to get you into stalemate more than checkmate
            } else { // if a stalemate
                score -= 250000 * depth;
            }
        }
        return score;
    }
///////////////////////////////// BOARD SCORE LAYOUTS FOR POSITIONAL EVALUATION
// obtained from website below
// http://chessprogramming.wikispaces.com/Simplified+evaluation+function
    private int pawnBoard[][] = {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        {5, 5, 10, 25, 25, 10, 5, 5},
        {0, 0, 0, 20, 20, 0, 0, 0},
        {5, -5, -10, 0, 0, -10, -5, 5},
        {5, 10, 10, -20, -20, 10, 10, 5},
        {0, 0, 0, 0, 0, 0, 0, 0}};
    private int rookBoard[][] = {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {5, 10, 10, 10, 10, 10, 10, 5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {0, 0, 0, 5, 5, 0, 0, 0}};
    private int knightBoard[][] = {
        {-50, -40, -30, -30, -30, -30, -40, -50},
        {-40, -20, 0, 0, 0, 0, -20, -40},
        {-30, 0, 10, 15, 15, 10, 0, -30},
        {-30, 5, 15, 20, 20, 15, 5, -30},
        {-30, 0, 15, 20, 20, 15, 0, -30},
        {-30, 5, 10, 15, 15, 10, 5, -30},
        {-40, -20, 0, 5, 5, 0, -20, -40},
        {-50, -40, -30, -30, -30, -30, -40, -50}};
    private int bishopBoard[][] = {
        {-20, -10, -10, -10, -10, -10, -10, -20},
        {-10, 0, 0, 0, 0, 0, 0, -10},
        {-10, 0, 5, 10, 10, 5, 0, -10},
        {-10, 5, 5, 10, 10, 5, 5, -10},
        {-10, 0, 10, 10, 10, 10, 0, -10},
        {-10, 10, 10, 10, 10, 10, 10, -10},
        {-10, 5, 0, 0, 0, 0, 5, -10},
        {-20, -10, -10, -10, -10, -10, -10, -20}};
    private int queenBoard[][] = {
        {-20, -10, -10, -5, -5, -10, -10, -20},
        {-10, 0, 0, 0, 0, 0, 0, -10},
        {-10, 0, 5, 5, 5, 5, 0, -10},
        {-5, 0, 5, 5, 5, 5, 0, -5},
        {0, 0, 5, 5, 5, 5, 0, -5},
        {-10, 5, 5, 5, 5, 5, 0, -10},
        {-10, 0, 5, 0, 0, 0, 0, -10},
        {-20, -10, -10, -5, -5, -10, -10, -20}};
    private int kingMidBoard[][] = {
        {-30, -40, -40, -50, -50, -40, -40, -30},
        {-30, -40, -40, -50, -50, -40, -40, -30},
        {-30, -40, -40, -50, -50, -40, -40, -30},
        {-30, -40, -40, -50, -50, -40, -40, -30},
        {-20, -30, -30, -40, -40, -30, -30, -20},
        {-10, -20, -20, -20, -20, -20, -20, -10},
        {20, 20, 0, 0, 0, 0, 20, 20},
        {20, 30, 10, 0, 0, 10, 30, 20}};
    private int kingEndBoard[][] = {
        {-50, -40, -30, -20, -20, -30, -40, -50},
        {-30, -20, -10, 0, 0, -10, -20, -30},
        {-30, -10, 20, 30, 30, 20, -10, -30},
        {-30, -10, 30, 40, 40, 30, -10, -30},
        {-30, -10, 30, 40, 40, 30, -10, -30},
        {-30, -10, 20, 30, 30, 20, -10, -30},
        {-30, -30, 0, 0, 0, 0, -30, -30},
        {-50, -30, -30, -30, -30, -30, -30, -50}};

    /**
     *
     * @param material the pieces on the board
     * @return the evaluated score for the current positions being held by
     * pieces on the chess board
     */
    public int boardEvalPositional(int material) {
        int score = 0;

        for (int i = 0; i < BSIZE * BSIZE; i++) {
            int row = i / BSIZE;
            int col = i / BSIZE;
            switch (board[row][col].getPiece().getPieceName()) {
                case 'p': {
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        score += pawnBoard[row][col];
                    }
                    break;
                }
                case 'r': { // rook
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        score += rookBoard[row][col];
                    }
                    break;
                }
                case 'n': { // knight
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        score += knightBoard[row][col];
                    }
                    break;
                }
                case 'b': { // bishop
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        score += bishopBoard[row][col];
                    }
                    break;
                }
                case 'q': { // queen
                    if (board[row][col].getPiece().getColourVal() == WHITE) {
                        score += queenBoard[row][col];
                    }
                    break;
                }
                case 'k': { // king
                    if (material >= 1750) {
                        score += kingMidBoard[row][col];
                        score += generateMovesK(kingPosW,
                                row, col).length() * 10;
                    } else {
                        score += kingEndBoard[row][col];
                        score += generateMovesK(kingPosW,
                                row, col).length() * 30;
                    }
                    break;
                }
            }
        }
        return score;
    }

    /**
     * Moves a piece to the specified location on a chess board
     *
     * @param move the move to be performed
     */
    public void move(String move) {
        if (move.charAt(4) != 'P') { // if not a pawn promotion move (x1(0),
            // y1(1), x2(2), y2(3) ,captured piece(4)) // or move.length-1
            board[Character.getNumericValue(move.charAt(2))][Character
                    .getNumericValue(move.charAt(3))].removePiece();
            board[Character.getNumericValue(move.charAt(2))][Character
                    .getNumericValue(move.charAt(3))].setPiece(board[Character
                    .getNumericValue(move.charAt(0))][Character
                    .getNumericValue(move.charAt(1))]
                    .getPiece());
            board[Character.getNumericValue(move.charAt(0))][Character
                    .getNumericValue(move.charAt(1))].removePiece();
            board[Character.getNumericValue(move.charAt(0))][Character
                    .getNumericValue(move.charAt(1))]
                    .setPiece(new NullPiece(EMPTY));
            if (board[Character.getNumericValue(move.charAt(2))][Character
                    .getNumericValue(move.charAt(3))].getPiece().getColourVal()
                    == WHITE
                    && board[Character.getNumericValue(move.charAt(2))]
                            [Character.getNumericValue(move.charAt(3))]
                    .getPiece().getPieceName() == 'k') {
                kingPosW = BSIZE * Character.getNumericValue(move.charAt(2))
                        + Character.getNumericValue(move.charAt(3));
            }
        } else { // pawn promotion (col1(0), col2(1), captured piece(2),
            // new piece(3), P (promotion)(4))
            ChessPiece[] promoOptions = {new Queen(WHITE), new Rook(WHITE),
                new Bishop(WHITE), new Knight(WHITE)};
            ChessPiece promoSelect = null;
            board[1][Character.getNumericValue(move.charAt(0))].removePiece();
            board[1][Character.getNumericValue(move.charAt(0))]
                    .setPiece(new NullPiece(EMPTY));
            board[0][Character.getNumericValue(move.charAt(1))].removePiece();

            for (ChessPiece promoOption : promoOptions) {
                if (move.charAt(3) == promoOption.getPieceName()) {
                    promoSelect = promoOption;
                    break;
                }
            }
            board[0][Character.getNumericValue(move
                    .charAt(1))].setPiece(promoSelect);
        }
    }

    /**
     * Undoes a move performed on a chess board (for search score purposes)
     *
     * @param move the move to be undone
     */
    public void undo(String move) {
        if (move.charAt(4) != 'P') { // if not a pawn promotion move (x1(0), 
            // y1(1), x2(2), y2(3) ,captured piece(4)) // or move.length-1
            ChessPiece[] pieceOptions = {new Queen(BLACK), new Rook(BLACK),
                new Bishop(BLACK), new Knight(BLACK), new King(BLACK),
                new Pawn(BLACK), new NullPiece(EMPTY)};
            ChessPiece pieceSelect = null;
            board[Character.getNumericValue(move.charAt(0))][Character
                    .getNumericValue(move.charAt(1))].removePiece();
            board[Character.getNumericValue(move.charAt(0))][Character
                    .getNumericValue(move.charAt(1))].setPiece(board[Character
                    .getNumericValue(move.charAt(2))][Character
                    .getNumericValue(move.charAt(3))]
                    .getPiece());
            board[Character.getNumericValue(move.charAt(2))][Character
                    .getNumericValue(move.charAt(3))].removePiece();

            for (ChessPiece option : pieceOptions) {
                if (move.charAt(4) == option.getPieceName()) {
                    pieceSelect = option;
                    break;
                }
            }
            board[Character.getNumericValue(move.charAt(2))][Character
                    .getNumericValue(move.charAt(3))].setPiece(pieceSelect);
            if (board[Character.getNumericValue(move.charAt(0))][Character
                    .getNumericValue(move.charAt(1))].getPiece().getColourVal()
                    == WHITE
                    && board[Character.getNumericValue(move.charAt(0))]
                            [Character.getNumericValue(move.charAt(1))]
                    .getPiece().getPieceName() == 'k') {
                kingPosW = BSIZE * Character.getNumericValue(move.charAt(0))
                        + Character.getNumericValue(move.charAt(1));
            }
        } else { // pawn promotion (col1(0), col2(1), captured piece(2), 
            // new piece(3), P (promotion)(4))
            ChessPiece[] promoOptions = {new Queen(WHITE), new Rook(WHITE),
                new Bishop(WHITE), new Knight(WHITE)};
            ChessPiece promoSelect = null;
            board[1][Character.getNumericValue(move.charAt(0))].removePiece();
            board[1][Character.getNumericValue(move.charAt(0))]
                    .setPiece(new Pawn(WHITE));
            board[0][Character.getNumericValue(move.charAt(1))].removePiece();

            for (ChessPiece promoOption : promoOptions) {
                if (move.charAt(2) == promoOption.getPieceName()) {
                    promoSelect = promoOption;
                    break;
                }
            }
            board[0][Character.getNumericValue(move.charAt(1))]
                    .setPiece(promoSelect);
        }
    }

    /**
     * Returns true if a move is allowed based on the total amount of possible\
     * moves available
     *
     * @param allMove all possible available moves
     * @param yourMove the move to be performed
     * @return
     */
    public boolean valid(String allMove, String yourMove) {
        if (yourMove.length() >= 5 && yourMove.length() <= 6) {
            if (Character.isDigit(yourMove.charAt(0))) {
                return allMove.contains(yourMove);
            }
        }
        return false;
    }

    /**
     * Prints the chess board out
     */
    public void printBoard() {

        ///// Testing empty board with proper tile colour
        for (int i = 0; i < board.length; i++) {
            System.out.print("[");
            for (int j = 0; j < board.length; j++) {
                if (board[i][j].getPiece().getColourVal() != EMPTY) {
                    // if a black chess piece --> AI character
                    if (board[i][j].getPiece().getColourVal() == BLACK) {
                        char compPiece;
                        compPiece = Character.toUpperCase(board[i][j]
                                .getPiece().getPieceName());
                        System.out.print(compPiece);
                    } else {
                        System.out.print(board[i][j].getPiece().getPieceName());
                    }

                } else { // empty tile
                    System.out.print("-");
                }
                if (j != board.length - 1) {
                    System.out.print("|");
                }
            }
            System.out.print("]");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        FinalChess game = new FinalChess();
    }
}
