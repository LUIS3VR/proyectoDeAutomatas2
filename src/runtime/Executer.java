package runtime;

import codigoIntermedio.IntermediateCodeGenerator;
import codigoIntermedio.Quadruplet;
import codigoIntermedio.Symbol;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Executer {

    private IntermediateCodeGenerator execGen;
    private HashMap<String, Symbol> execSymTab;
    private ArrayList<Quadruplet> quadruplets;

    public void loadFile(){
        try {
            FileInputStream fileIn = new FileInputStream("sourceCode.ser");
            ObjectInputStream inStream = new ObjectInputStream(fileIn);
            execGen = (IntermediateCodeGenerator) inStream.readObject();
            execSymTab = execGen.getSymbolTable();
            quadruplets = execGen.getQuadruplets();
            inStream.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ce){
            ce.printStackTrace();
        }        
    }//end loadFile
    
    public String execute(){
        String output = "";
        for (int i = 0; i < quadruplets.size(); i++) {
            Quadruplet quadruplet = quadruplets.get(i);
            switch (quadruplet.getOperationCode()) {
                case 109:
                    output += ">"+execSymTab.get(quadruplet.getResult()).getValue()+"\n";
                    break;
                case 125:
                    execSymTab.get(quadruplet.getResult()).setValue(""+(Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) + Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())));
                    break;
                case 126:
                    execSymTab.get(quadruplet.getResult()).setValue(""+(Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) - Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())));
                    break;
                case 127:
                    execSymTab.get(quadruplet.getResult()).setValue(""+(Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) * Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())));
                    break;
                case 128:
                    execSymTab.get(quadruplet.getResult()).setValue(""+(Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) / Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())));
                    break;
                case 129:
                    execSymTab.get(quadruplet.getResult()).setValue(""+(Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) % Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())));
                    break;
                case 130:
                    execSymTab.get(quadruplet.getResult()).setValue(execSymTab.get(quadruplet.getOperand2()).getValue());
                    break;
                case 131:
                    if (execSymTab.get(quadruplet.getOperand1()).getValue()=="FALSE" || execSymTab.get(quadruplet.getOperand2()).getValue()=="FALSE") execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    if (execSymTab.get(quadruplet.getOperand1()).getValue()=="FALSE" || execSymTab.get(quadruplet.getOperand2()).getValue()=="TRUE") execSymTab.get(quadruplet.getResult()).setValue("TRUE");
                    if (execSymTab.get(quadruplet.getOperand1()).getValue()=="TRUE" || execSymTab.get(quadruplet.getOperand2()).getValue()=="FALSE") execSymTab.get(quadruplet.getResult()).setValue("TRUE");
                    if (execSymTab.get(quadruplet.getOperand1()).getValue()=="TRUE" || execSymTab.get(quadruplet.getOperand2()).getValue()=="TRUE") execSymTab.get(quadruplet.getResult()).setValue("TRUE");
                    break;
                case 132:
                    if (execSymTab.get(quadruplet.getOperand1()).getValue()=="FALSE" && execSymTab.get(quadruplet.getOperand2()).getValue()=="FALSE") execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    if (execSymTab.get(quadruplet.getOperand1()).getValue()=="FALSE" && execSymTab.get(quadruplet.getOperand2()).getValue()=="TRUE") execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    if (execSymTab.get(quadruplet.getOperand1()).getValue()=="TRUE" && execSymTab.get(quadruplet.getOperand2()).getValue()=="FALSE") execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    if (execSymTab.get(quadruplet.getOperand1()).getValue()=="TRUE" && execSymTab.get(quadruplet.getOperand2()).getValue()=="TRUE") execSymTab.get(quadruplet.getResult()).setValue("TRUE");
                    break;
                case 133:
                    break;
                case 134:
                    if (Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) < Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())==true) {
                        execSymTab.get(quadruplet.getResult()).setValue("TRUE");
                    } else {
                        execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    }
                    break;
                case 135:
                    if (Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) <= Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())==true) {
                        execSymTab.get(quadruplet.getResult()).setValue("TRUE");                        
                    } else {
                        execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    }
                    break;
                case 136:
                    if (Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) > Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())==true) {
                        execSymTab.get(quadruplet.getResult()).setValue("TRUE");                        
                    } else {
                        execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    }
                    break;
                case 137:
                    if (Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) >= Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())==true) {
                        execSymTab.get(quadruplet.getResult()).setValue("TRUE");                        
                    } else {
                        execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    }
                    break;
                case 138:
                    if (Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) == Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())==true) {
                        execSymTab.get(quadruplet.getResult()).setValue("TRUE");                        
                    } else {
                        
                        execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    }
                    break;
                case 139:
                    if (Integer.valueOf(execSymTab.get(quadruplet.getOperand1()).getValue()) != Integer.valueOf(execSymTab.get(quadruplet.getOperand2()).getValue())==true) {
                        execSymTab.get(quadruplet.getResult()).setValue("TRUE");                        
                    } else {
                        execSymTab.get(quadruplet.getResult()).setValue("FALSE");
                    }
                    break;
                case 900:
                    if (execSymTab.get(quadruplets.get(i-1).getResult()).getValue().equals("FALSE")) {
                        i = Integer.valueOf(quadruplet.getResult())-1;
                    }
                    break;
                case 901:
                    i = Integer.valueOf(quadruplet.getResult())-1;
                    break;
                default:
                    throw new AssertionError();
            }//end switch
        }//end for
        return output;
    }//end execute

    public ArrayList<Quadruplet> getQuadruplets() {
        return quadruplets;
    }
    
    public void printSymbolTable(){
        System.out.println("SYMBOL TABLE");
        for (Map.Entry<String, Symbol> entry: execSymTab.entrySet()) {
            System.out.println(entry);
        }
    }
}//end class Executer