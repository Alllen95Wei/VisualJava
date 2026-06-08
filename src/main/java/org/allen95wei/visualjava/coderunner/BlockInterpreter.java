package org.allen95wei.visualjava.coderunner;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import org.allen95wei.visualjava.block.*;
import org.allen95wei.visualjava.block.condition.IfBlock;
import org.allen95wei.visualjava.block.process.PrintBlock;
import org.allen95wei.visualjava.block.process.SetBlock;
import org.allen95wei.visualjava.block.process.StartBlock;
import org.allen95wei.visualjava.block.variable.NumVariableBlock;
import org.allen95wei.visualjava.block.variable.StringVariableBlock;
import org.allen95wei.visualjava.coderunner.core.Flow;
import org.allen95wei.visualjava.coderunner.core.Step;
import org.allen95wei.visualjava.coderunner.core.arithmetic.*;
import org.allen95wei.visualjava.coderunner.core.control.SetStep;
import org.allen95wei.visualjava.coderunner.core.output.PrintStep;


public class BlockInterpreter {
    public static Flow run(List<Node> blocks) {
        Flow mainFlow = new Flow();

        ProcessBlock next = null;

        // Get the StartBlock
        for (var block: blocks) {
            if (block instanceof StartBlock) next = (ProcessBlock) block;
        }
        if (next == null) throw new IllegalArgumentException("No StartBlock found in the blocks list.");

        while (true) {
            mainFlow.addStep(blockToStep(next));
            if (next.getNextBlock() != null) next = (ProcessBlock) next.getNextBlock();
            else break;
        }

        return mainFlow;
    }

    public static Step blockToStep(Block block) {
        Step step = new SetStep("dummy", "dummy"); // Placeholder, will be overwritten in switch
        switch (block) {
            case PrintBlock pBlock -> {
                Block printTarget = pBlock.getPrintTarget();
                if (printTarget instanceof ValueBlock vBlock) {
                    step = new PrintStep(vBlock.getValue());
                } else if (printTarget instanceof VariableBlock vBlock) {
                    step = new PrintStep(vBlock.getValue(), true);
                } else {
                    throw new IllegalArgumentException("Block type is not printable: " + printTarget.getClass().getSimpleName());
                }
            }
            case SetBlock sBlock -> {
                Block varSource = sBlock.getVariableSource();
                Block valSource = sBlock.getValueSource();

                if (varSource instanceof NumVariableBlock numVarBlock) {
                    step = switch (valSource) {
                        case ValueBlock valBlock ->
                                new SetStep(numVarBlock.getValue(), Double.valueOf(valBlock.getValue()));
                        case VariableBlock varBlock -> new SetStep(numVarBlock.getValue(), varBlock.getValue(), true);
                        case BinaryOperatorBlock binOpBlock ->
                                new SetStep(numVarBlock.getValue(), (ArithmeticStep) blockToStep(binOpBlock));
                        default ->
                                throw new IllegalArgumentException("Undefined value source type: " + valSource.getClass().getSimpleName());
                    };
                } else if (varSource instanceof StringVariableBlock strVarBlock) {
                    step = switch (valSource) {
                        case ValueBlock valBlock -> new SetStep(strVarBlock.getValue(), valBlock.getValue());
                        case VariableBlock varBlock -> new SetStep(strVarBlock.getValue(), varBlock.getValue(), true);
                        default -> throw new IllegalArgumentException("Undefined value source type: " + valSource.getClass().getSimpleName());
                    };
                } else {
                    throw new IllegalArgumentException("Undefined variable source type: " + varSource.getClass().getSimpleName());
                }
            }
            case BinaryOperatorBlock binOpBlock -> {
                String operator = binOpBlock.getBlockText();
                Block leftBlock = binOpBlock.getLeftOperand();
                Block rightBlock = binOpBlock.getRightOperand();
                String leftKey = null, rightKey = null;
                Double leftVal = null, rightVal = null;

                if (leftBlock instanceof VariableBlock leftValBlock) {
                    // left operand is a variable
                    leftKey = leftValBlock.getValue();
                } else if (leftBlock instanceof ValueBlock leftValBlock) {
                    // left operand is a value
                    leftVal = Double.valueOf(leftValBlock.getValue());
                } else {
                    throw new IllegalArgumentException("Undefined left operand type: " + leftBlock.getClass().getSimpleName());
                }
                if (rightBlock instanceof VariableBlock rightValBlock) {
                    // left operand is a variable
                    rightKey = rightValBlock.getValue();
                } else if (rightBlock instanceof ValueBlock rightValBlock) {
                    // left operand is a value
                    rightVal = Double.valueOf(rightValBlock.getValue());
                } else {
                    throw new IllegalArgumentException("Undefined right operand type: " + rightBlock.getClass().getSimpleName());
                }

                switch (operator) {
                    case "+" -> {
                        if (leftKey != null && rightKey != null) {
                            step = new AddStep(leftKey, rightKey);
                        } else if (leftKey != null) {
                            step = new AddStep(leftKey, rightVal);
                        } else if (rightKey != null) {
                            step = new AddStep(leftVal, rightKey);
                        } else {
                            step = new AddStep(leftVal, rightVal);
                        }
                    }
                    case "-" -> {
                        if (leftKey != null && rightKey != null) {
                            step = new SubtractStep(leftKey, rightKey);
                        } else if (leftKey != null) {
                            step = new SubtractStep(leftKey, rightVal);
                        } else if (rightKey != null) {
                            step = new SubtractStep(leftVal, rightKey);
                        } else {
                            step = new SubtractStep(leftVal, rightVal);
                        }
                    }
                    case "*" -> {
                        if (leftKey != null && rightKey != null) {
                            step = new MultiplyStep(leftKey, rightKey);
                        } else if (leftKey != null) {
                            step = new MultiplyStep(leftKey, rightVal);
                        } else if (rightKey != null) {
                            step = new MultiplyStep(leftVal, rightKey);
                        } else {
                            step = new MultiplyStep(leftVal, rightVal);
                        }
                    }
                    case "/" -> {
                        if (leftKey != null && rightKey != null) {
                            step = new DivideStep(leftKey, rightKey);
                        } else if (leftKey != null) {
                            step = new DivideStep(leftKey, rightVal);
                        } else if (rightKey != null) {
                            step = new DivideStep(leftVal, rightKey);
                        } else {
                            step = new DivideStep(leftVal, rightVal);
                        }
                    }
                    default -> throw new IllegalArgumentException("Undefined operator: " + operator);
                }
            }
            case IfBlock ifBlock -> {
            }
            case DecisionBlock decisionBlock -> {

            }
            default -> throw new IllegalArgumentException("Undefined block type: " + block.getClass().getSimpleName());
        }

        return step;
    }
}