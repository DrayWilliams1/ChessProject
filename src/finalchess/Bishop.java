package finalchess;
/**
 *
 * @author Drayton Williams and Cameron Hammel
 */
public class Bishop extends ChessPiece{
    
    public Bishop(int colour){
        super(colour);
    }
    
    @Override
    public char getPieceName() {
        return 'b';
    }
}
