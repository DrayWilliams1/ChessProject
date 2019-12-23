package finalchess;
/**
 *
 * @author Drayton Williams and Cameron Hammel
 */
public class King extends ChessPiece{
    
    public King(int colour) {
        super(colour);
    }
    
    @Override
    public char getPieceName() {
        return 'k';
    }
}
