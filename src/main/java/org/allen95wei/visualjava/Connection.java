package org.allen95wei.visualjava;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Connection {

    private Block from;
    private Block to;

    private Circle fromNode;

    private Line line;

    public Connection(
            Block from,
            Block to,
            Circle fromNode,
            Line line
    ) {
        this.from = from;
        this.to = to;
        this.fromNode = fromNode;
        this.line = line;
    }

    public Block getFrom() {
        return from;
    }

    public Block getTo() {
        return to;
    }

    public Circle getFromNode() {
        return fromNode;
    }

    public Line getLine() {
        return line;
    }
}