package finalchess;
/**
 *
 * @author Drayton Williams and Cameron Hammel
 */
public class Knight extends ChessPiece{
    
    public Knight(int colour){
        super(colour);
    }
    @Override
    public char getPieceName() {
        return 'n';
    }
}
