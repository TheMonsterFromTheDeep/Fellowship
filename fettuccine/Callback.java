package fettuccine;

public interface Callback {
    void activate(long tick);
    void loop(long tick);
}