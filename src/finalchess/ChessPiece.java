package finalchess;
/**
 *
 * @author Drayton Williams and Cameron Hammel
 */
public class ChessPiece {
    private int colour;
    private String colourName;
    private char pieceName;
    
    public ChessPiece(int colour) {
        this.colour = colour;
        switch (this.colour) {
            case 0: { // white (human)
                this.colourName = "White";
                break;
            }
            case 1: { // black (computer)
                this.colourName = "Black";
                break;
            }
            case -1: { // null (empty)
                this.colourName = "-";
                break;
            }
        }
    }
    
    /**
     * Returns the colour of the chess piece as an integer
     * @return the value of the colour (0 - white, 1 - black)
     */
    public int getColourVal() {
        return this.colour;
    }
    
    /**
     * Returns the chess piece colour as a string (white or black). Helps for
     * visual representation.
     * @return the string name of the chess piece colour 
     */
    public String getColourName() {
        return this.colourName;
    }
    
    /**
     * 
     * @return the character representation for a specific piece on the board
     */
    public char getPieceName() {
        return '-';
    }
}