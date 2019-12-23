package finalchess;
/**
 *
 * @author Drayton Williams and Cameron Hammel
 */
public class Rook extends ChessPiece {
    
    public Rook(int colour) {
        super(colour);
    }
    
    @Override
    public char getPieceName() {
        return 'r';
    }
}
