package org.allen95wei.visualjava;


import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Connection {

    private Block from;
    private Block to;
    private Line line;

    private Circle fromCircle;
    private Circle toCircle;

    // 舊版建構子 / Old constructor
    // 保留給 Main.java 使用，不要刪掉
    public Connection(
            Block from,
            Block to,
            Line line
    ) {
        this.from = from;
        this.to = to;
        this.line = line;

        this.fromCircle = from.getOutputCircle();
        this.toCircle = to.getInputCircle();
    }

    // 新版建構子 / New constructor
    // 給多節點積木使用 / Used for blocks with multiple nodes
    public Connection(
            Block from,
            Block to,
            Circle fromCircle,
            Circle toCircle,
            Line line
    ) {
        this.from = from;
        this.to = to;
        this.fromCircle = fromCircle;
        this.toCircle = toCircle;
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

    public Circle getFromCircle() {
        return fromCircle;
    }

    public Circle getToCircle() {
        return toCircle;
    }
}