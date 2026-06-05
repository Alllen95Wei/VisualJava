package org.allen95wei.visualjava;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Connection {

    private Block from;
    private Block to;
    private Line line;

    private Circle fromNode;

    private Circle fromCircle;
    private Circle toCircle;

    // 舊版建構子 / Old constructor
    // 給舊版 Main.java 使用 / Used by old Main.java
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

        this.fromCircle = from.getOutputCircle();
        this.toCircle = to.getInputCircle();
    }

    // 朋友版本建構子 / Friend's constructor
    // 給 Main.java 使用，記錄是哪一個輸出節點拉出的線
    // Used by Main.java to remember which output node created the line
    public Connection(
            Block from,
            Block to,
            Circle fromNode,
            Line line
    ) {
        this.from = from;
        this.to = to;
        this.fromCircle = fromNode;
        this.toCircle = to.getInputCircle();
        this.line = line;
    }

    // 新版建構子 / New constructor
    // 給多節點積木使用 / Used for blocks with multiple input/output nodes
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

    public Circle getFromNode() {
        return fromNode;
    }

    public Line getLine() {
        return line;
    }

    // 朋友 Main.java 需要這個 method / Friend's Main.java needs this method
    public Circle getFromNode() {
        return fromCircle;
    }

    // 可選：如果之後需要取得目標節點 / Optional: get target node
    public Circle getToNode() {
        return toCircle;
    }

    // 你的 EditorController.java 用這個 / Your EditorController.java uses this
    public Circle getFromCircle() {
        return fromCircle;
    }

    // 你的 EditorController.java 用這個 / Your EditorController.java uses this
    public Circle getToCircle() {
        return toCircle;
    }
}