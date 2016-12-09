package codigoIntermedio;

import java.io.Serializable;
import java.util.HashMap;

public class Quadruplet implements Serializable {
    private Integer operationCode;
    private String operand1;
    private String operand2;
    private String result;
    private HashMap<String, Symbol> symTab;

    public Quadruplet(Integer operationCode, String operand1, String operand2, String result){
        this.operationCode = operationCode;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }
    
    public int getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(int operationCode) {
        this.operationCode = operationCode;
    }

    public String getOperand1() {
        return operand1;
    }

    public void setOperand1(String operand1) {
        this.operand1 = operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public void setOperand2(String operand2) {
        this.operand2 = operand2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        if (operand1==null && operand2==null && result==null) return "["+this.operationCode+"("+operationCodeDescription(operationCode)+"), (), (), ()]";
        /*
        *   GOTOF
        */        
        if (operationCode==900) {
            return "["+this.operationCode+"("+operationCodeDescription(operationCode)+"), "
                    +symTab.get(operand1).getDirection()+"("+symTab.get(operand1).getIdentifier()+"), "
                    +", "+result+"]";
        }
        /*
        *   GOTO
        */        
        if (operationCode==901) {
            return "["+this.operationCode+"("+operationCodeDescription(operationCode)+"), "
                    +", , "+result+"]";
        }
        /*
        *   ASSIGNATION
        */        
        if (operationCode==130) {
            return "["+this.operationCode+"("+operationCodeDescription(operationCode)+"), (),"
                +symTab.get(operand2).getDirection()+"("+symTab.get(operand2).getIdentifier()+"), "
                +symTab.get(result).getDirection()+"("+symTab.get(result).getIdentifier()+")]";
        }
        /*
        *   PRINT
        */
        if (operationCode==109) {
            return "["+this.operationCode+"("+operationCodeDescription(operationCode)+"), (), (), "
                +symTab.get(result).getDirection()+"("+symTab.get(result).getIdentifier()+")]";
        }
        return "["+this.operationCode+"("+operationCodeDescription(operationCode)+"), "
                    +symTab.get(operand1).getDirection()+"("+symTab.get(operand1).getIdentifier()+"), "
                    +symTab.get(operand2).getDirection()+"("+symTab.get(operand2).getIdentifier()+"), "
                    +symTab.get(result).getDirection()+"("+symTab.get(result).getIdentifier()+"]";
    }//end toString
    
    public void setSymbolTable(HashMap<String, Symbol> table){
        this.symTab = table;
    }
    
    private String operationCodeDescription(int code){
        switch(code){
            case 108: return "INPUT";//READ
            case 109: return "OUTPUT";//PRINT
            case 125: return "+"; // +               
            case 126: return "-"; // -              
            case 127: return "*"; // *              
            case 128: return "/"; // /             
            case 129: return "%"; // %             
            case 130: return "="; // =
            case 131: return "OR"; // ||             
            case 132: return "AND"; // &&             
            case 133: return "NOT"; // !
            case 134: return "<"; // <
            case 135: return "<="; // <=      
            case 136: return ">"; // >
            case 137: return ">="; // >=      
            case 138: return "=="; // ==              
            case 139: return "!="; // !=   
            case 900: return "gotoF"; //goto falso
            case 901: return "goto"; //goto
            default: return "<unknown>";         
        }
    }
}//end class