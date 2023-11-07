//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package socket;

/**
 * The Request class which handles a clients request that is sent to the server
 */
public class Request {
    private RequestType type;
    private String data;

    /**
     * the defualt constructor for the request class
     */
    public Request() {
        this.type = null;
        this.data = null;
    }

    /**
     * the initalized constructor for the request class
     * @param t the request type
     * @param d the data
     */
    public Request(RequestType t, String d) {
        this.type = t;
        this.data = d;
    }

    /**
     * sets the request type
     * @param t the type
     */
    public void setType(RequestType t) {
        this.type = t;
    }

    /**
     * sets the data
     * @param d the data
     */
    public void setData(String d) {
        this.data = d;
    }

    /**
     * gets the request type
     * @return the type
     */
    public RequestType getType() {
        return this.type;
    }

    /**
     * gets the users data
     * @return the data
     */
    public String getData() {
        return this.data;
    }

    /**
     * an enumerator type for different reqest types
     */
    public static enum RequestType {
        LOGIN,
        REGISTER,
        UPDATE_PAIRING,
        SEND_INVITATION,
        ACCEPT_INVITATION,
        DECLINE_INVITATION,
        ACKNOWLEDGE_RESPONSE,
        REQUEST_MOVE,
        SEND_MOVE,
        ABORT_GAME,
        COMPLETE_GAME;

        private RequestType() {
        }
    }
}
