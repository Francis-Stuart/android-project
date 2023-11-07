
package model;

public class Event {
    private int eventID;
    private String sender;
    private String opponent;
    private EventStatus status;
    private String turn;
    private int move;

    public Event() {
        this.eventID = -1;
        this.sender = null;
        this.opponent = null;
        this.status = null;
        this.turn = null;
        this.move = -1;
    }

    public Event(int eventId, String sender, String opponent, EventStatus status, String turn, int move) {
        this.eventID = eventId;
        this.sender = sender;
        this.opponent = opponent;
        this.status = status;
        this.turn = turn;
        this.move = move;
    }

    public int getEventID() {
        return this.eventID;
    }

    public String getSender() {
        return this.sender;
    }

    public String getOpponent() {
        return this.opponent;
    }

    public EventStatus getStatus() {
        return this.status;
    }

    public String getTurn() {
        return this.turn;
    }

    public int getMove() {
        return this.move;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public boolean equals(Object Event) {
        Event newEvent = (Event)Event;
        return this.eventID == newEvent.eventID;
    }

    public static enum EventStatus {
        PENDING,
        DECLINED,
        ACCEPTED,
        PLAYING,
        COMPLETED,
        ABORTED;

        private EventStatus() {
        }
    }
}
