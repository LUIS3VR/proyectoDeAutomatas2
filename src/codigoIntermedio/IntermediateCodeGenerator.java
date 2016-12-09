package codigoIntermedio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class IntermediateCodeGenerator implements Serializable {
    private final Stack<Integer> operators;
    private final Stack<String> operands;
    private final Stack<String> otherOperands;
    private final Stack<Variable> avail;
    private final Stack<Integer> types;
    private final Stack<Integer> jumps;
    private final ArrayList<Quadruplet> quadruplets;
    private final HashMap<String, Symbol> symbolTable;
    
    private Integer count;

    private final int[][] semanticTable = {
        /*00*/{103,	104,	103,	107,	500,	1},
        /*01*/{104,	104,	500,	107,	500,	0},
        /*02*/{104,	104,	500,	107,	500,	1},
        /*03*/{104,	104,	500,	107,	500,	1},
        /*04*/{500,	500,	500,	107,	500,	1},
        /*05*/{500,	500,	500,	107,	500,	0},
        /*06*/{500,	500,	500,	107,	500,	0},
        /*07*/{500,	500,	500,	107,	500,	1},
        /*08*/{500,	500,	500,	107,	107,	1},
        /*09*/{500,	500,	500,	500,	500,	1},
        /*10*/{500,	500,	500,	500,	500,	1},
    };
    
    public IntermediateCodeGenerator(){
        operands = new Stack<>();
        operators = new Stack<>();
        otherOperands = new Stack<>();
        avail = new Stack<>();
        for (int i = 1000; i < 1200; i++) {
            String name = "T"+i;
            avail.push(new Variable(name, null,i));
        }
        types = new Stack();
        quadruplets = new ArrayList<>();
        symbolTable = new HashMap<>();
        jumps = new Stack<>();
    }//end constructor
    
    //Feed the quadruplets list
    private void addQuadruplet(int opCod, String op1, String op2, String res) {
        Quadruplet quad = new Quadruplet(opCod, op1, op2, res);
        quad.setSymbolTable(symbolTable);
        quadruplets.add(quad);
    }//end addquadruplet
    
    /*
    *   TEST TO SEE QUADRUPLETS, THIS IS MENT TO BE STORED IN FILES LATER ON
    */
    public void showQuadruplets(){
        if (!quadruplets.isEmpty()) {
            System.out.println("\n====QUADRUPLETS====\n");
            for (Quadruplet quadruplet : quadruplets) {
                System.out.println(quadruplets.indexOf(quadruplet)+"|"+quadruplet);
            }
            System.out.println("\n==END_QUADRUPLETS==\n");
        }
    }//end showQuadruplets
    
    public ArrayList<Quadruplet> getQuadruplets(){
        return quadruplets;
    }
    
    public HashMap<String, Symbol> getSymbolTable(){
        return symbolTable;
    }
    
    /*
    *   SAVES THE OBJECT
    */
    public void saveCompilated(){
        try {
            FileOutputStream fileOut = new FileOutputStream("sourceCode.ser");
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
            outStream.writeObject(this);
            outStream.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end saveCompilated
    
    //obtain column and analyse
    private int semanticAnalysis(int i, int j){
        int k = 0;
        if (j == 127 || j == 125 || j == 126) {
            k = 0;
        } 
        if (j == 128) {
            k = 1;
        }
        if (j == 129) {
            k = 2;
        }
        if (j >= 134 && j <= 139) {
            k = 3;
        }
        if (j == 131 || j == 132) {
            k = 4;
        }
        if (j == 130) {
            k = 5;
        }
        return semanticTable[i][k];
    }//end relateForSemanticAnalysis
    
    private int relateSemanticRow(String operand1, String operand2) {
        if (symbolTable.containsKey(operand1) && symbolTable.containsKey(operand2)) {
            /*00*/if (symbolTable.get(operand1).getType() == 103 && symbolTable.get(operand2).getType() == 103) return 0;
            /*01*/if (symbolTable.get(operand1).getType() == 103 && symbolTable.get(operand2).getType() == 104) return 1;
            /*02*/if (symbolTable.get(operand1).getType() == 104 && symbolTable.get(operand2).getType() == 103) return 2;
            /*03*/if (symbolTable.get(operand1).getType() == 104 && symbolTable.get(operand2).getType() == 104) return 3;
            /*04*/if (symbolTable.get(operand1).getType() == 105 && symbolTable.get(operand2).getType() == 105) return 4;
            /*05*/if (symbolTable.get(operand1).getType() == 105 && symbolTable.get(operand2).getType() == 106) return 5;
            /*06*/if (symbolTable.get(operand1).getType() == 106 && symbolTable.get(operand2).getType() == 105) return 6;
            /*07*/if (symbolTable.get(operand1).getType() == 106 && symbolTable.get(operand2).getType() == 106) return 7;
            /*08*/if (symbolTable.get(operand1).getType() == 107 && symbolTable.get(operand2).getType() == 107) return 8;
            /*09*/if (symbolTable.get(operand1).getType() == 106 && symbolTable.get(operand2).getType() == 103) return 9;
            /*10*/if (symbolTable.get(operand1).getType() == 106 && symbolTable.get(operand2).getType() == 104) return 10;
        }
        return -1;
    }//end relateSemanticRow

    /*
    *   DECLARATION ACTIONS
    */
    public void action200(int type){
        types.push(type);
    }//end action200
    
    public void action201(String id){
        otherOperands.push(id);
    }//end action201
    
    public void action202(){
            int type = types.pop();
            while(!otherOperands.empty()){
                String operand = otherOperands.pop();
                if (symbolTable.containsKey(operand)) {
                } else {
                    symbolTable.put(operand, new Variable(operand, type, symbolTable.size()+200));                    
                }
            }
    }//end action202
    
    /*
    *   EXPRESSIONS ACTIONS
    */
    public void action203(int operador){
        operators.push(operador);
    }//end action2013
    
    public void action204(String id){
        operands.push(id);
    }//end action204
    
    public void action205(){
        if (operators.peek() == 125 || operators.peek() == 126) {
        String operand2 = operands.pop();
        types.pop();
        String operand1 = operands.pop();
        types.pop();
        int resType = semanticAnalysis(relateSemanticRow(operand1, operand2), operators.peek());
            if (resType < 500) {
                Variable res = avail.pop();
                res.setType(resType);
                types.push(resType);
                operands.push(res.getIdentifier());
                symbolTable.put(res.getIdentifier(), res);
                addQuadruplet(operators.pop(), operand1, operand2, res.getIdentifier());
            } else {
                System.out.println("ERROR_SEMANTICO [A205]");
            }
        }
    }//end action205
    
    public void action220(){
        if (operators.peek() == 127 || operators.peek() == 128) {
        String operand2 = operands.pop();
        types.pop();
        String operand1 = operands.pop();
        types.pop();
        int resType = semanticAnalysis(relateSemanticRow(operand1, operand2), operators.peek());
            if (resType < 500) {
                Variable res = avail.pop();
                res.setType(resType);
                types.push(resType);
                operands.push(res.getIdentifier());
                symbolTable.put(res.getIdentifier(), res);
                addQuadruplet(operators.pop(), operand1, operand2, res.getIdentifier());
            } else {
                System.out.println("ERROR_SEMANTICO [A220]");
            }
        }
    }//end action220
    
    /*
    *   IDENTIFY FACT ACTIONS
    */
    public void action206(String id, int token){
        switch(token){
            case 117:
                operands.push(id);
                if (!symbolTable.containsKey(id)) {
                    symbolTable.put(id, new Constant(id, 107, symbolTable.size()+1000));
                }
                types.push(107);
                break;
            case 118:
                operands.push(id);
                if (!symbolTable.containsKey(id)) {
                    symbolTable.put(id, new Constant(id, 107, symbolTable.size()+1000));
                }
                types.push(107);
                break;
            case 119:
                if (symbolTable.containsKey(id)) {
                    operands.push(id);
                    types.push(symbolTable.get(id).getType());
                } else {
                    System.out.println(id+"VARIABLE NO ENCOTRADA [A206]");
                }
                break;
            case 120:
                operands.push(id);
                if (!symbolTable.containsKey(id)) {
                    symbolTable.put(id, new Constant(id,103,symbolTable.size()+1000));                    
                }
                types.push(103);
                break;
            case 121:
                operands.push(id);
                if (!symbolTable.containsKey(id)) {
                    symbolTable.put(id, new Constant(id,104,symbolTable.size()+1000));                    
                }
                types.push(104);
                break;
            case 122:
                operands.push(id);
                if (!symbolTable.containsKey(id)) {
                    symbolTable.put(id, new Constant(id,122,symbolTable.size()+1000));                    
                }
                types.push(122);
                break;
            case 123:
                operands.push(id);
                if (!symbolTable.containsKey(id)) {
                    symbolTable.put(id, new Constant(id,105,symbolTable.size()+1000));                    
                }
                types.push(105);
                break;                
            case 124:
                operands.push(id);
                if (!symbolTable.containsKey(id)) {
                    symbolTable.put(id, new Constant(id,106,symbolTable.size()+1000));                    
                }
                types.push(106);
                break;
            case 141:
                break;
            default:
                System.out.println("default [A206]");
        }
    }//end action206
    
    public void action207(){
        String operand2 = operands.pop();
        types.pop();
        String operand1 = operands.pop();
        types.pop();
        int type = semanticAnalysis(relateSemanticRow(operand1, operand2), operators.peek());
            if (type<500) {
                Variable res = avail.pop();
                res.setType(type);
                types.push(type);
                operands.push(res.getIdentifier());
                symbolTable.put(res.getIdentifier(), res);
                addQuadruplet(operators.pop(), operand1, operand2, res.getIdentifier());
            } else {
                System.out.println("ERROR_SEMANTICO [A207]");
            }            
    }//end action207
    
    /*
    *   IF ACTIONS
    */
    public void action208(){
        int aux = types.pop();
        if (aux==107) {
            String res = operands.pop();
            addQuadruplet(900, res, null, null);
            count = quadruplets.size();
            jumps.push(count-1);
        } else {
            System.out.println("ERROR SEMANTICO ( IF [A208] )");
        }
    }//end action208
    
    public void action209(){
        addQuadruplet(901, null, null, null);
        count = quadruplets.size();
        quadruplets.get(jumps.pop()).setResult(""+count);
        jumps.push(count-1);
    }//end action209
    
    public void action210(){
        count = quadruplets.size();
        quadruplets.get(jumps.pop()).setResult(""+count);
    }//end action210
     
    /*
    *   ASIGN ACTION
    */   
    public void action211(String id){
        if (symbolTable.containsKey(id)) {
            operands.push(id);
            types.push(symbolTable.get(id).getType());            
        } else {
            System.out.println("VARIABLE "+id+" NO HA SIDO DECLARADA");
        }
    }//end action211

    public void action212(){
        types.pop();
        String operand2 = operands.pop();
        types.pop();
        String operand1 = operands.pop();
        int type = semanticAnalysis(relateSemanticRow(operand1, operand2), 130);
            if (type==1) {
                addQuadruplet(130, null, operand2, operand1);
            } else {
                System.out.println("ERROR_SEMANTICO [A212]");
            }
    }//end action212

    /*
    *    WHILE ACTIONS
    */
    public void action213(){
        count = quadruplets.size();
        jumps.push(count);
    }//end action213
    
    public void action214(){
        int aux = types.pop();
        if (aux==107) {
            String res = operands.pop();
            addQuadruplet(900, res, null, null);
            count = quadruplets.size();
            jumps.push(count-1);
        } else {
            System.out.println("ERROR SEMANTICO ( WHILE [A214] )");
        }
    }//end action214
        
    public void action215(){
        int fa = jumps.pop();
        int re = jumps.pop();
        addQuadruplet(901, null, null, ""+re);
        count = quadruplets.size();
        quadruplets.get(fa).setResult(""+count);
    }//end action215
    
    /*
    *   FOR ACTIONS
    */
    public void action217(){
        String exp1 = operands.pop();
        types.pop();
        String id = operands.peek();
        addQuadruplet(130, null, exp1, id);
    }//end action217
    
    public void action218(){
        Variable tf = avail.pop();
        String exp2 = operands.pop();
        Variable tx = avail.pop();
        types.pop();
        tf.setIdentifier("tf");
        tx.setIdentifier("tx");
        symbolTable.put(tf.getIdentifier(), tf);
        symbolTable.put(tx.getIdentifier(), tx);
        addQuadruplet(130, null, exp2, tf.getIdentifier());
        addQuadruplet(135, operands.peek(), tf.getIdentifier(), tx.getIdentifier());
        addQuadruplet(900, tx.getIdentifier(), null, null);
        count = quadruplets.size();
        jumps.push(count-2);
    }//end action218

    public void action219(){
        String id = operands.pop();
        types.pop();
        Variable uno = new Variable("1", 130, symbolTable.size()+200);
        uno.setValue("1");
        symbolTable.put("1", uno);
        addQuadruplet(125, id, uno.getIdentifier(), id);
        int ret = jumps.peek();
        count = quadruplets.size();
        quadruplets.get(jumps.pop()+1).setResult(""+(count+1));
        addQuadruplet(901, null, null, ""+ret);
    }//end action219
    
    /*
    *   PRINT ACTIONS
    */
    public void action221(){
        String res = operands.pop();
        types.pop();
        addQuadruplet(109, null, null, res);
    }
    /*
    *   TEST METHODS
    */
    public void testShowStacks(){
        System.out.println("operators      : "+operators);
        System.out.println("operands       : "+operands);
        System.out.println("other operands : "+otherOperands);
        System.out.println("types          : "+types);
        System.out.println("jumps          : "+jumps);
        System.out.println("SYMBOL TABLE");
        for (Map.Entry<String, Symbol> entry: symbolTable.entrySet()) {
            System.out.println(entry);
        }
    }//end testShowStacks
}//end class