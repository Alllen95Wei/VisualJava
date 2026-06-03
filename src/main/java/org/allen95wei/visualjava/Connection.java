package org.allen95wei.visualjava;

import javafx.scene.shape.Line;

public class Connection {

    private Block from;
    private Block to;
    private Line line;

    public Connection(
            Block from,
            Block to,
            Line line
    ) {
        this.from = from;
        this.to = to;
        this.line = line;
    }

    public Block getFrom() {
        return from;
    }

    public Block getTo() {
        return to;
    }

    public Line getLine() {
        return line;
    }
}
