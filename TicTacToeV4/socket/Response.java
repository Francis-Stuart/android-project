//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package socket;

/**
 * The Response class which is the Servers response to a clients request
 */
public class Response {
    private ResponseStatus status;
    private String message;

    /**
     * the default constructor for the response class
     */
    public Response() {
        this.status = null;
        this.message = null;
    }

    /**
     * the initalized constructor for the response class
     * @param status the status
     * @param message the message
     */
    public Response(ResponseStatus status, String message) {
        this.message = message;
        this.status = status;
    }

    /**
     * gets the users message
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * gets the users status
     * @return the status
     */
    public ResponseStatus getStatus() {
        return this.status;
    }

    /**
     * sets the status
     */
    public void setStatus() {
        this.status = this.status;
    }

    /**
     * sets the message
     */
    public void setMessage() {
        this.message = this.message;
    }

    /**
     * The enumeration type for different response status
     */
    public static enum ResponseStatus {
        Success,
        Failure;

        private ResponseStatus() {
        }
    }
}
