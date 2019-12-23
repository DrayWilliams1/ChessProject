package finalchess;
/**
 *
 * @author Drayton Williams and Cameron Hammel
 */
public class NullPiece extends ChessPiece{
    
    public NullPiece(int colour) {
        super(colour);
    }
    
    @Override
    public char getPieceName() {
        return '-';
    }
}
