package org.allen95wei.visualjava;

public enum BlockType {

    DECISION,   // 判斷模塊
    PROCESS,    // 步驟模塊
    VARIABLE,   // 變數模塊
    CONDITION,  // 舊條件模塊

    IF,         // 新的 IF / 條件菱形
    AND,        // AND 邏輯模塊
    OR,         // OR 邏輯模塊
    NOT         // NOT 邏輯模塊
}