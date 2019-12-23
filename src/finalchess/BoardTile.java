package finalchess;
/**
 *
 * @author Drayton Williams and Cameron Hammel
 */
public class BoardTile {
    private int xCoordinate;
    private int yCoordinate;
    private ChessPiece tilePiece; // piece currently on tile (if exists)
    private String colourName;
    private int colour;
    
    public BoardTile(int x, int y, int colour) {
        this.xCoordinate = x;
        this.yCoordinate = y;
        
        this.colour = colour;
        switch (this.colour) {
            case 0: { // white 
                this.colourName = "White";
                break;
            }
            case 1: { // black
                this.colourName = "Black";
                break;
            }
        }
        this.tilePiece = null;
    }

    public boolean isEmpty() { // or isOccupied (reverse)
        if(this.tilePiece.getColourVal() == -1) {
            return true;
        }
        return false;
    }

    /* 
     * Sets the current chess piece on the tile
     */
    public void setPiece(ChessPiece p) {
        this.tilePiece = p;
    }

    /* 
     * Obtains the current chess piece on the tile
     */
    public ChessPiece getPiece() {
        return tilePiece;
    }
    
    /**
     * Removes the piece currently occupying the tile
     */
    public void removePiece() {
        this.tilePiece = null;
    }
    
    /* 
     * Returns the integer value for the colour of that tile
     */
    public int getTileVal() {
        return this.colour;
    }
    
    /* 
     * Returns the string name of the colour of the tile
     */
    public String getTileColour() {
        return this.colourName;
    }
}