//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package socket;

/**
 * This is the gamingResponse class where it models the response to a request of type REQUEST_MOVE
 */
public class GamingResponse extends Response {
    private int move;
    private boolean active;

    /**
     * this is the default constructor for the GamingResponse
     */
    public GamingResponse() {
        this.move = 0;
        this.active = false;
    }

    /**
     * The initialized constructor for the class The server responds to the opponents last move
     * @param status the status of the response
     * @param message the message of the response
     * @param move the move of the game
     * @param active the check to see if a player is not moving
     */
    public GamingResponse(Response.ResponseStatus status, String message, int move, boolean active) {
        super(status, message);
        this.move = move;
        this.active = active;
    }

    /**
     *gets the move of the user
     * @return the move
     */
    public int getMove() {
        return this.move;
    }

    /**
     * gets the users activity
     * @return the active status
     */
    public boolean getActive() {
        return this.active;
    }

    /**
     * sets the move of the game
     * @param move the move
     */
    public void setMove(int move) {
        this.move = move;
    }

    /**
     *  sets the activity of the user
     * @param active the activity
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
