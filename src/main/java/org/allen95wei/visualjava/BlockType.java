package org.allen95wei.visualjava;

public enum BlockType {

    // ===== Process / Control blocks =====
    // 流程控制積木 / Process control blocks
    START,          // 開始積木 / Start block
    PRINT,          // 列印積木 / Print block
    SET,            // 設定變數積木 / Set variable block

    // ===== Comparison blocks =====
    // 比較積木 / Comparison blocks
    GREATER,        // 大於 / Greater than
    LESS,           // 小於 / Less than
    EQUAL,          // 等於 / Equal to

    // ===== Value / Arithmetic blocks =====
    // 值與四則運算積木 / Value and arithmetic blocks
    VALUE,          // 值積木 / Value block
    ADD,            // 加法 / Addition
    SUBTRACT,       // 減法 / Subtraction
    MULTIPLY,       // 乘法 / Multiplication
    DIVIDE,         // 除法 / Division

    // ===== Old basic blocks =====
    // 舊版基礎積木 / Old basic blocks
    DECISION,       // 判斷模塊 / Decision block
    PROCESS,        // 步驟模塊 / Process block
    VARIABLE,       // 變數模塊 / Variable block
    CONDITION,      // 舊條件模塊 / Old condition block

    // ===== Logic blocks =====
    // 邏輯積木 / Logic blocks
    IF,             // 如果 / If block
    ENDIF,
    AND,            // 且 / AND logic block
    OR,             // 或 / OR logic block
    NOT,            // 非 / NOT logic block

    // ===== Variable blocks =====
    // 變數積木 / Variable blocks
    STRING_VARIABLE, // 字串變數 / String variable
    NUM_VARIABLE     // 數值變數 / Number variable
}