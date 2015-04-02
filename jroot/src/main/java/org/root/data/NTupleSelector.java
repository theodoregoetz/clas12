/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.data;

import java.util.ArrayList;
import java.util.Arrays;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

/**
 *
 * @author gavalian
 */
public class NTupleSelector {
    Operator operatorGT = new Operator(">", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
            @Override
            public double apply(final double... args) {
                if(args[0]>args[1]) return 1.0;
                return 0.0;
            }
    };
    
    Operator operatorLT = new Operator("<", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
        @Override
        public double apply(final double... args) {
            if(args[0]<args[1]) return 1.0;
            return 0.0;
        }
    };

    Operator operatorEQ = new Operator("==", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
        @Override
        public double apply(final double... args) {
            if(args[0]==args[1]) return 1.0;
            return 0.0;
        }
    };

    Operator operatorAND = new Operator("&", 2, true, Operator.PRECEDENCE_ADDITION) {
        @Override
        public double apply(final double... args) {
            if(args[0]>0.0&&args[1]>0.0) return 1.0;
            return 0.0;
        }
    };
    
    Operator operatorOR = new Operator("|", 2, true, Operator.PRECEDENCE_ADDITION) {
        @Override
        public double apply(final double... args) {
            if(args[0]>0.0||args[1]>0.0) return 1.0;
            return 0.0;
        }
    };    
    
    
    Expression expr = null;
    private ArrayList<String> variableList = new ArrayList<String>();
    
    public NTupleSelector(String selection, String[] variables){
        this.init(selection,variables);
    }

    
    public final void init(String selection, String[] variables){
        ExpressionBuilder builder = new ExpressionBuilder(selection)
                .operator(operatorAND)
                .operator(operatorOR)
                .operator(operatorGT)
                .operator(operatorLT)
                .operator(operatorEQ);
        builder.variables(variables);
        expr = builder.build();
        this.variableList.clear();
        this.variableList.addAll(Arrays.asList(variables));
    }
    
    public boolean isValid(NTupleRow row){
        for(int loop = 0; loop < this.variableList.size(); loop++){
            expr.setVariable(this.variableList.get(loop),row.get(loop));
        }
        double result = expr.evaluate();
        if(result>0.0) return true;
        return false;
    }
    
    
}
