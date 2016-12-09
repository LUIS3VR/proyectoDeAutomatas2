//package principal;
//
//import java.io.IOException;
//import runtime.Executer;
//import sintactico.SyntacticAnalyzer;
//
//public class Main {
//    
//    public static void main(String[] args) {
//        SyntacticAnalyzer analyzer = new SyntacticAnalyzer();
//        try {
//            analyzer.analyzeLexic();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Executer exe = new Executer();
//        exe.loadFile();
//        System.out.println(exe.execute());
//        exe.printSymbolTable();
//    }//end main
//}//end class