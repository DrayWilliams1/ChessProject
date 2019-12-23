package finalchess;
/**
 *
 * @author Drayton Williams and Cameron Hammel
 */
public class Pawn extends ChessPiece {

    public Pawn(int colour) {
        super(colour);
    }

    @Override
    public char getPieceName() {
        return 'p';
    }
}
