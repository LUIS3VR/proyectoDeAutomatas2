package lexico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LexicalAnalyser {
    
    private BufferedReader myBufferReader;
    private StringBuilder myStringBuilder;   
    private int state;
    private int column;
    private int index;
    private char readChar;
    private String input;
    private String lexicLog = "";
    private String aux = "";
    
    private final int[][] stateMatrix = {
    {2  , 1  , 3  , 500, 500, 1  , 2  , 125, 126, 127, 128, 129, 9  , 10 , 11 , 12 , 13 , 14 , 15 , 17 , 140, 141, 142, 143, 144, 145, 18 , 0  , 0  , 0  , 500 },
    {2  , 1  , 2  , 2  , 100, 1  , 2  , 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 },
    {2  , 2  , 2  , 2  , 119, 2  , 2  , 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119 },
    {120, 120, 3  , 120, 4  , 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120 },
    {501, 501, 5  , 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501 },
    {121, 121, 5  , 121, 121, 6  , 6  , 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121, 121 },
    {502, 502, 8  , 502, 502, 502, 502, 7  , 7  , 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502 },
    {503, 503, 8  , 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503 },
    {122, 122, 8  , 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122, 122 },
    {504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 132, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504 },
    {505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 131, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505 },
    {133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 139, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133, 133 },
    {130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 138, 133, 130, 130, 130, 130, 130, 130, 130, 144, 130, 130, 130, 130, 130, 130 },
    {134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 135, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134 },
    {136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 137, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136 },
    {16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 506, 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16 , 16  },
    {507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 123, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507 },
    {17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 124, 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17 , 17  },
    {18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 18 , 146, 18  }
    };
 
    //this method is used to read the source file and transform it to a input string
    public String readFile () throws FileNotFoundException, IOException {
        this.myBufferReader = new BufferedReader(new FileReader("sourceCode.txt"));
        try {
            myStringBuilder = new StringBuilder();
            String line =  myBufferReader.readLine();
                while (line != null) {
                    myStringBuilder.append(line);
                    myStringBuilder.append('\n');
                    line = myBufferReader.readLine();
                }
            this.input = myStringBuilder.toString();
        } finally {
            myBufferReader.close();
        }
        return this.input;  
    }//end readFile

    public void scan(int index) throws IOException {

                //initial state
                state = 0;
                
                readFile();
                
                while( state <= 18 && index < input.length() ) {
                    
                    readChar = getCharacter(index);
                    aux += readChar;

                    column = relate(readChar, state);
                    state = stateMatrix[state][column];
                    
                    if(state == 0) { 
                        aux = "";
                    }
                    
                    index++;
                    this.index = index;
                }

                if( state >= 100 && state <= 146){
                    if( (state >= 140 && state <= 145) 
                            || (state >= 125 && state <= 129) 
                            || state == 131 
                            || state == 132) {
                        index++; 
                        this.index = index;
                    } else if( state == 139 
                            || state == 138 
                            || state == 135 
                            || state == 137 
                            || state == 123 
                            || state == 124 
                            || state == 146) {
                        index++; 
                        this.index = index;
                        aux = aux.substring(0 , aux.length());
                    } else {
                        aux = aux.substring(0 , aux.length() - 1);
                        if(state == 100){
                            state = getReservedWord(aux);
                        }
                    }

                    File lexicLogFile = new File("logLex.txt");
                    if (lexicLogFile.createNewFile()) {
                        System.out.println("lexic log created");
                    }
                    BufferedWriter myBufferedWriter = new BufferedWriter(new FileWriter(lexicLogFile));
                    lexicLog += getToken(state, readChar, aux) + "\n";
                    myBufferedWriter.write(lexicLog);
                    myBufferedWriter.flush();
                }
                
                //si la tabla nos lleva a un error
                else if(state >= 500) {
                    System.out.println(getError(state));
                }
    }//end analyze
 
    public char getCharacter(int index) {
        return input.charAt(index);
    }//end getCharacter
     
    public int getToken() {
        return this.state;  
    }//end getToken

    public int getIndex() {
        return this.index;
    }//end getIndex
 
    public String getAux(){
        return this.aux;
    }
    
    public int getReservedWord(String temp){
        switch(temp) {
            case "CLASS": return 100;
            case "BEGIN": return 101;
            case "ENDCLASS": return 102;
            case "INTEGER": return 103;
            case "FLOAT": return 104;
            case "CHAR": return 105;
            case "STRING": return 106;
            case "BOOLEAN": return 107;
            case "READ": return 108;
            case "PRINT": return 109;
            case "IF": return 110;
            case "ENDIF": return 111;
            case "ELSE": return 112;
            case "WHILE": return 113;
            case "ENDWHILE": return 114;
            case "FOR": return 115;
            case "ENDFOR": return 116;
            case "TRUE": return 117;
            case "FALSE": return 118;
            default: return 500;
        }
   }//end getReservedWord
 
 
    public String getToken(int state, char character, String aux){
        switch(state){
            case 100: return "100 : <" + aux + ", reserved_word>\n";
            case 101: return "101 : <" + aux + ", reserved_word>\n";
            case 102: return "102 : <" + aux + ", reserved_word>\n";
            case 103: return "103 : <" + aux + ", reserved_word>\n";
            case 104: return "104 : <" + aux + ", reserved_word>\n";
            case 105: return "105 : <" + aux + ", reserved_word>\n";
            case 106: return "106 : <" + aux + ", reserved_word>\n";
            case 107: return "107 : <" + aux + ", reserved_word>\n";
            case 108: return "108 : <" + aux + ", reserved_word>\n";
            case 109: return "109 : <" + aux + ", reserved_word>\n";
            case 110: return "110 : <" + aux + ", reserved_word>\n";
            case 111: return "111 : <" + aux + ", reserved_word>\n";
            case 112: return "112 : <" + aux + ", reserved_word>\n";
            case 113: return "113 : <" + aux + ", reserved_word>\n";
            case 114: return "114 : <" + aux + ", reserved_word>\n";
            case 115: return "115 : <" + aux + ", reserved_word>\n";
            case 116: return "116 : <" + aux + ", reserved_word>\n";
            case 117: return "117 : <" + aux + ", reserved_word>\n";
            case 118: return "118 : <" + aux + ", reserved_word>\n";
            case 119: return "119 : <" + aux + ", identifier>\n";
            case 120: return "120 : <" + aux + ", integer>\n";
            case 121: return "121 : <" + aux + ", real_number>\n";
            case 122: return "122 : <" + aux + ", scientific_notation_number>\n";
            case 123: return "123 : <" + aux + ", char>\n";                                          
            case 124: return "124 : <" + aux + ", string>\n";                                       
            case 125: return "125 : <" + character + ", aritmetic_operator>\n"; // +               
            case 126: return "126 : <" + character + ", aritmetic_operator>\n"; // -              
            case 127: return "127 : <" + character + ", aritmetic_operator>\n"; // *              
            case 128: return "128 : <" + character + ", aritmetic_operator>\n"; // /             
            case 129: return "129 : <" + character + ", aritmetic_operator>\n"; // %             
            case 130: return "130 : <" + aux + ", assignment_operator>\n"; // =
            case 131: return "131 : <" + aux + ", logic_operator_OR>\n"; // ||             
            case 132: return "132 : <" + aux + ", logic_operator_AND>\n"; // &&             
            case 133: return "133 : <" + aux + ", logic_operator_NOT>\n"; // !
            case 134: return "134 : <" + aux + ", relational_operator>\n"; // <
            case 135: return "135 : <" + aux + ", relational_operator>\n"; // <=      
            case 136: return "136 : <" + aux + ", relational_operator>\n"; // >
            case 137: return "137 : <" + aux + ", relational_operator>\n"; // >=      
            case 138: return "138 : <" + aux + ", relational_operator>\n"; // ==              
            case 139: return "139 : <" + aux + ", relational_operator>\n"; // !=         
            case 140: return "140 : <" + character + ", opening_parenthesis>\n"; // (                              
            case 141: return "141 : <" + character + ", closing_parenthesis>\n"; // )                           
            case 142: return "142 : <" + character + ", opening_square_bracket>\n"; // [                               
            case 143: return "143 : <" + character + ", closing_square_bracket>\n"; // ]                            
            case 144: return "144 : <" + character + ", comma\n"; // ,                                      
            case 145: return "145 : <" + character + ", semicolon(expression_ending)>\n";// ;                               
            case 146: return "146 : <" + aux + ", comment_line>\n"; // #                                
            default: return "FILE_ENDING";         
        }
    }//end getToken
   
   
    public String getError(int edo){
        switch(edo){
            case 500: return "Error 500, unknown simbol";
            case 501: return "Error 501, missing digit";
            case 502: return "Error 502, incomplete scientific notation";
            case 503: return "Error 503, missing digit";
            case 504: return "Error 504, missing ampersand";
            case 505: return "Error 505, missing pleca";
            case 506: return "Error 506, missing character";
            case 507: return "Error 507, missing ending single quote";
            case 598: return "Error 508, missing ending quote";
            default: return "getError() default case";
        }
    }//end getError

    //this method is used to determine the column number on the table
    public int relate(char character, int state) {
        
        if(character == 'E' && state==5) {
           return 5;
        } if(character == 'e' && state==5) {
           return 6;
        } if(character >= 'a' && character <='z') {
           return 0;
        } if(character >= 'A' && character <='Z') {
           return 1;
        } if(character >= '0' && character <='9') {
           return 2;
        }

        switch(character){
            case '_' : return 3;
            case '.' : return 4;
            case '+' : return 7;
            case '-' : return 8;
            case '*' : return 9;
            case '/' : return 10;
            case '%' : return 11;
            case '&' : return 12;
            case '|' : return 13;
            case '!' : return 14;
            case '=' : return 15;
            case '<' : return 16;
            case '>' : return 17;
            case '\'': return 18;
            case '"' : return 19;
            case '(' : return 20;
            case ')' : return 21;
            case '[' : return 22;
            case ']' : return 23;
            case ',' : return 24;
            case ';' : return 25;
            case '#' : return 26;
            case ' ' : return 27;
            case '\t': return 28;
            case '\n': return 29;
            default  : return 30;
        }
    }//end relate
}//end class