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
    // 給舊版 Main.java 或舊程式使用 / Used by old Main.java or older code
    public Connection(
            Block from,
            Block to,
            Line line
    ) {
        this(
                from,
                to,
                from.getOutputCircle(),
                to.getInputCircle(),
                line
        );
    }

    // 朋友 Main.java 使用的建構子 / Constructor used by friend's Main.java
    // 記錄是哪一個黑色輸出節點拉出的線 / Records which black output node created the line
    public Connection(
            Block from,
            Block to,
            Circle fromNode,
            Line line
    ) {
        this(
                from,
                to,
                fromNode,
                to.getInputCircle(),
                line
        );
    }

    // 新版建構子 / New constructor
    // 給多節點積木使用，例如 IF、AND、OR、NOT
    // Used for multi-node blocks, such as IF, AND, OR, NOT
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

    // 給 Main.java 使用 / Used by Main.java
    // 和 getFromCircle() 是同一個意思，只是名字不同
    // Same meaning as getFromCircle(), just an older name
    public Circle getFromNode() {
        return fromCircle;
    }

    // 給 Main.java 或未來需要時使用 / Used by Main.java or future code if needed
    public Circle getToNode() {
        return toCircle;
    }

    // 給 EditorController.java 使用 / Used by EditorController.java
    public Circle getFromCircle() {
        return fromCircle;
    }

    // 給 EditorController.java 使用 / Used by EditorController.java
    public Circle getToCircle() {
        return toCircle;
    }
}