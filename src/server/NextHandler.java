package server;

public class NextHandler {
    private boolean isCalledNext = false;

    public boolean isCalled() {
        return isCalledNext;
    }

    public void next() {
        isCalledNext = true;
    }
}
