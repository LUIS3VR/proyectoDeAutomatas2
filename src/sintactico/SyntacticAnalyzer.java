package sintactico;

import codigoIntermedio.IntermediateCodeGenerator;
import java.io.IOException;
import java.util.Stack;
import lexico.LexicalAnalyser;

public class SyntacticAnalyzer {
 
    private final Stack<Integer> stack;
    private final LexicalAnalyser myLexicAnalyzer; 
    private final IntermediateCodeGenerator myGenerator;
    private boolean isTerminal;
    private int lastToken;
    private int token;
    private int posLex;
    private int foundToken;  
    private int expectedToken;  
    private int tokenPosition; 
    private String aux;
    private String status;
    
    private final int[][] firstMatrix = {
        {1  , 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500 },
        {501, 501, 501, 2  , 2  , 2  , 2  , 2  , 4  , 5  , 6  , 501, 501, 7  , 501, 8  , 501, 501, 501, 3  , 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501 },
        {502, 502, 10 , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 9  , 10 , 10 , 9  , 10 , 9  , 10 , 10 , 10 , 9  , 10 , 10 , 10 , 10 , 10 , 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 502, 10 , 502, 502, 502, 502, 502 },
        {503, 503, 503, 11 , 11 , 11 , 11 , 11 , 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503 },
        {504, 504, 504, 12 , 13 , 14 , 15 , 16 , 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504 },
        {505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 505, 18 , 505, 505, 17 , 18  },
        {506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 19 , 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506, 506 },
        {507, 507, 507, 507, 507, 507, 507, 507, 20 , 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507, 507 },
        {508, 508, 508, 508, 508, 508, 508, 508, 508, 21 , 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508, 508 },
        {509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 509, 23 , 509, 509, 22 , 509 },
        {510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 24 , 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510, 510 },
        {511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 26 , 25 , 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511, 511 },
        {512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 27 , 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512, 512 },
        {513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 28 , 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513, 513 },
        {514, 514, 514, 514, 514, 514, 514, 514, 514, 514, 514, 514, 514, 514, 514, 514, 514, 29 , 29 , 29 , 29 , 29 , 29 , 29 , 29 , 514, 514, 514, 514, 514, 514, 514, 514, 29 , 514, 514, 514, 514, 514, 514, 29 , 29 , 514, 514, 29 , 29  },
        {515, 515, 515, 515, 515, 515, 515, 515, 515, 515, 515, 515, 515, 515, 515, 515, 515, 31 , 31 , 31 , 31 , 31 , 31 , 31 , 31 , 515, 515, 515, 515, 515, 515, 30 , 515, 31 , 515, 515, 515, 515, 515, 515, 31 , 31 , 515, 515, 31 , 31  },
        {516, 516, 516, 516, 516, 516, 516, 516, 516, 516, 516, 516, 516, 516, 516, 516, 516, 32 , 32 , 32 , 32 , 32 , 32 , 32 , 32 , 516, 516, 516, 516, 516, 516, 32 , 516, 32 , 516, 516, 516, 516, 516, 516, 32 , 32 , 516, 516, 32 , 32  },
        {517, 517, 517, 517, 517, 517, 517, 517, 517, 517, 517, 517, 517, 517, 517, 517, 517, 34 , 34 , 34 , 34 , 34 , 34 , 34 , 34 , 517, 517, 517, 517, 517, 517, 34 , 33 , 34 , 517, 517, 517, 517, 517, 517, 34 , 34 , 517, 517, 34 , 34  },
        {518, 518, 518, 518, 518, 518, 518, 518, 518, 518, 518, 518, 518, 518, 518, 518, 518, 35 , 35 , 35 , 35 , 35 , 35 , 35 , 35 , 518, 518, 518, 518, 518, 518, 35 , 35 , 35 , 518, 518, 518, 518, 518, 518, 35 , 35 , 518, 518, 35 , 35  },
        {519, 519, 519, 519, 519, 519, 519, 519, 519, 519, 519, 519, 519, 519, 519, 519, 519, 37 , 37 , 37 , 37 , 37 , 37 , 37 , 37 , 519, 519, 519, 519, 519, 519, 519, 519, 36 , 519, 519, 519, 519, 519, 519, 37 , 519, 519, 519, 519, 519 },
        {520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 38 , 38 , 38 , 38 , 38 , 38 , 38 , 38 , 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 520, 38 , 520, 520, 520, 520, 520 },
        {521, 521, 521, 521, 521, 521, 521, 521, 521, 521, 521, 521, 521, 521, 521, 521, 521, 40 , 40 , 40 , 40 , 40 , 40 , 40 , 40 , 521, 521, 521, 521, 521, 521, 40 , 40 , 40 , 39 , 39 , 39 , 39 , 39 , 39 , 40 , 40 , 521, 521, 40 , 40  },
        {522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 522, 41 , 42 , 43 , 44 , 45 , 46 , 522, 522, 522, 522, 522, 522 },
        {523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 47 , 47 , 47 , 47 , 47 , 47 , 47 , 47 , 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 523, 47 , 523, 523, 523, 523, 523 },
        {524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 50 , 50 , 50 , 50 , 50 , 50 , 50 , 50 , 48 , 49 , 524, 524, 524, 524, 50 , 50 , 50 , 50 , 50 , 50 , 50 , 50 , 50 , 50 , 50 , 524, 524, 50 , 50  },
        {525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 51 , 51 , 51 , 51 , 51 , 51 , 51 , 51 , 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 525, 51 , 525, 525, 525, 525, 525 },
        {526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 54 , 54 , 54 , 54 , 54 , 54 , 54 , 54 , 54 , 54 , 52 , 53 , 526, 526, 54 , 54 , 54 , 54 , 54 , 54 , 54 , 54 , 54 , 54 , 54 , 526, 526, 54 , 54  },
        {527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 61 , 62 , 55 , 56 , 57 , 58 , 59 , 60 , 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 527, 63 , 527, 527, 527, 527, 527 }         
    };
    
    private final int[][] productions = {
    /* 00 */{0                                                                          },
    /* 01 */{5  , 100, 119, 101, 1  , 102                                               },
    /* 02 */{2  , 3  , 2                                                                },
    /* 03 */{2  , 6  , 2                                                                },
    /* 04 */{2  , 7  , 2                                                                },
    /* 05 */{2  , 8  , 2                                                                },
    /* 06 */{2  , 10 , 2                                                                },
    /* 07 */{2  , 12 , 2                                                                },
    /* 08 */{2  , 13 , 2                                                                },
    /* 09 */{1  , 1                                                                     },
    /* 10 */{1  , -1                                                                    },
    /* 11 */{7  , 4  , 200, 119, 201, 5  , 145, 202                                     },//200-201-202
    /* 12 */{1  , 103                                                                   },
    /* 13 */{1  , 104                                                                   },
    /* 14 */{1  , 105                                                                   },
    /* 15 */{1  , 106                                                                   },
    /* 16 */{1  , 107                                                                   },
    /* 17 */{4  , 144, 119, 201, 5                                                      },//201
    /* 18 */{1  , -1                                                                    },
    /* 19 */{6  , 119, 211, 130, 14 , 212, 145                                          },//211,212
    /* 20 */{6  , 108, 140, 119, 5  , 141, 145                                          },
    /* 21 */{7  , 109, 140, 14 , 221, 9  , 141, 145                                     },//221
    /* 22 */{4  , 144, 14 , 221, 9                                                      },//221
    /* 23 */{1  , -1                                                                    },
    /* 24 */{9  , 110, 140, 14 , 141, 208, 1  , 11 ,111,210                             },//208,210
    /* 25 */{3  , 112, 209, 1                                                           },//209
    /* 26 */{1  , -1                                                                    },
    /* 27 */{9  , 113, 213, 140, 14 , 141, 214, 1  , 114, 215                           },//213,214,215
    /* 28 */{14 , 115, 140, 119, 211, 130, 14 , 217, 145, 14 , 141, 218, 1  , 116, 219  },//211, 217, 218, 219
    /* 29 */{2  , 16 , 15                                                               },
    /* 30 */{3  , 131, 203, 14                                                          },
    /* 31 */{1  , -1                                                                    },
    /* 32 */{2  , 18 , 17                                                               },
    /* 33 */{3  , 132, 203, 18                                                          },//203
    /* 34 */{1  , -1                                                                    },
    /* 35 */{2  , 19 , 20                                                               },
    /* 36 */{1  , 133                                                                   },
    /* 37 */{1  , -1                                                                    },
    /* 38 */{2  , 23 , 21                                                               },
    /* 39 */{4  , 22 ,203, 23,207                                                       },//203-207
    /* 40 */{1  , -1                                                                    },
    /* 41 */{1  , 134                                                                   },
    /* 42 */{1  , 135                                                                   },
    /* 43 */{1  , 136                                                                   },
    /* 44 */{1  , 137                                                                   },
    /* 45 */{1  , 138                                                                   },
    /* 46 */{1  , 139                                                                   },
    /* 47 */{2  , 25 , 24                                                               },
    /* 48 */{4  , 125, 203, 23 ,205                                                     },//203,205
    /* 49 */{4  , 126, 203, 23 ,205                                                     },//203,205
    /* 50 */{1  , -1                                                                    },
    /* 51 */{3  , 27 , 206, 26                                                          },//206
    /* 52 */{4  , 127, 203, 25 ,220                                                     },//220
    /* 53 */{4  , 128, 203, 25 ,220                                                     },//220
    /* 54 */{1  , -1                                                                    },
    /* 55 */{1  , 119                                                                   },
    /* 56 */{1  , 120                                                                   },
    /* 57 */{1  , 121                                                                   },
    /* 58 */{1  , 122                                                                   },
    /* 59 */{1  , 123                                                                   },
    /* 60 */{1  , 124                                                                   },
    /* 61 */{1  , 117                                                                   },
    /* 62 */{1  , 118                                                                   },
    /* 63 */{3  , 140, 14 , 141                                                         }
    };

    //CONSTRUCTOR
    public SyntacticAnalyzer() {
        myLexicAnalyzer = new LexicalAnalyser();  
        stack = new Stack();
        myGenerator = new IntermediateCodeGenerator();
    }//end Constructor

    public void analyzeLexic() throws IOException {
        myLexicAnalyzer.scan(0);            
        posLex = myLexicAnalyzer.getIndex();
        token = myLexicAnalyzer.getToken();
        stack.push(0);
        parse();   
    }

    public void actions(int action){
        switch(action){
            case 200: 
                myGenerator.action200(lastToken); 
                break;
            case 201: 
                myGenerator.action201(aux);
                break;
            case 202: 
                myGenerator.action202();
                break;
            case 203:
                myGenerator.action203(lastToken);
                break;
            case 204:
                myGenerator.action204(aux);
                break;
            case 205:
                myGenerator.action205();
                break;
            case 206:
                myGenerator.action206(aux, lastToken);
                break;
            case 207:
                myGenerator.action207();
                break;
            case 208:
                myGenerator.action208();
                break;
            case 209:
                myGenerator.action209();
                break;
            case 210:
                myGenerator.action210();
                break;
            case 211:
                myGenerator.action211(aux);
                break;
            case 212:
                myGenerator.action212();
                break;
            case 213:
                myGenerator.action213();
                break;
            case 214:
                myGenerator.action214();
                break;
            case 215:
                myGenerator.action215();
                break;
            case 220:
                myGenerator.action220();
                break;
            case 221:
                myGenerator.action221();
                break;
            case 217:
                myGenerator.action217();
                break;
            case 218:
                myGenerator.action218();
                break;
            case 219:
                myGenerator.action219();
                break;
            default: System.out.println("NOT_SUPPOSED_TO_REACH_HERE[ACTION_SWITCH]");
        }
    }
    
    public void parse() throws IOException {
        do{
            if (stack.peek() >= 200) {
                int action = stack.pop();
                actions(action);
            }   
            
            if (stack.peek() >= 100) {                
                expectedToken = stack.peek();
                foundToken = token;
                isTerminal = true;
            } else {
                if (tokenPosition <= 45) {
                    int n = firstMatrix[ stack.peek() ][ tokenPosition ];
                    int elements = productions[n][0];
                    stack.pop();
                    for(int j = elements; j > 0 ; j--){
                        stack.push(productions[n][j]);
                    }
                    isTerminal = false;                    
                }
            }
            if(stack.peek().equals(-1)){ 
                stack.pop(); 
            }
        } while( !isTerminal );

        if(stack.peek().equals(token)) {
            lastToken = token;
            aux = myLexicAnalyzer.getAux();
            stack.pop();
            myLexicAnalyzer.scan(posLex - 1); 
            posLex = myLexicAnalyzer.getIndex();
            token = myLexicAnalyzer.getToken();
            tokenPosition = token - 100;
            
            if( stack.empty() ) {
                status = "BUILD_SUCCESSFUL";
                myGenerator.saveCompilated();
                myGenerator.showQuadruplets();
            } else {
              parse();              
            }
        } else {
            status = "SYNTAX ERROR \n"+printError(foundToken, expectedToken);
        }
    }

    public String printError(int foundToken, int expectedToken){
        System.out.println("<"+foundToken+","+expectedToken+">");
        switch(foundToken){
            case 100: return "found CLASS, expecting " + auxErrorTable(expectedToken) + "\n";
            case 101: return "found BEGIN, expecting " + auxErrorTable(expectedToken) + "\n";
            case 102: return "found ENDCLASS, expecting " + auxErrorTable(expectedToken) + "\n";
            case 103: return "found INTEGER, expecting " + auxErrorTable(expectedToken) + "\n";
            case 104: return "found FLOAT, expecting " + auxErrorTable(expectedToken) + "\n";
            case 105: return "found CHAR, expecting " + auxErrorTable(expectedToken) + "\n";
            case 106: return "found STRING, expecting " + auxErrorTable(expectedToken) + "\n";
            case 107: return "found BOOLEAN, expecting " + auxErrorTable(expectedToken) + "\n";
            case 108: return "found READ, expecting " + auxErrorTable(expectedToken) + "\n";
            case 109: return "found PRINT, expecting " + auxErrorTable(expectedToken) + "\n";
            case 110: return "found IF, expecting " + auxErrorTable(expectedToken) + "\n";
            case 111: return "found ENDIF, expecting " + auxErrorTable(expectedToken) + "\n";
            case 112: return "found ELSE, expecting " + auxErrorTable(expectedToken) + "\n";
            case 113: return "found WHILE, expecting " + auxErrorTable(expectedToken) + "\n";
            case 114: return "found ENDWHILE, expecting " + auxErrorTable(expectedToken) + "\n";
            case 115: return "found FOR, expecting " + auxErrorTable(expectedToken) + "\n";
            case 116: return "found ENDFOR, expecting " + auxErrorTable(expectedToken) + "\n";
            case 117: return "found TRUE, expecting " + auxErrorTable(expectedToken) + "\n";
            case 118: return "found FALSE, expecting " + auxErrorTable(expectedToken) + "\n";        
            case 119: return "found identifier, expecting " + auxErrorTable(expectedToken) + "\n";
            case 120: return "found constant, expecting " + auxErrorTable(expectedToken) + "\n";
            case 121: return "found constant, expecting " + auxErrorTable(expectedToken) + "\n";
            case 122: return "found constant, expecting " + auxErrorTable(expectedToken) + "\n";
            case 123: return "found character, expecting " + auxErrorTable(expectedToken) + "\n";
            case 124: return "found string, expecting " + auxErrorTable(expectedToken) + "\n";
            case 125: return "found +, expecting " + auxErrorTable(expectedToken) + "\n";
            case 126: return "found -, expecting " + auxErrorTable(expectedToken) + "\n";
            case 127: return "found *, expecting " + auxErrorTable(expectedToken) + "\n";
            case 128: return "found /, expecting " + auxErrorTable(expectedToken) + "\n";
            case 129: return "found %, expecting " + auxErrorTable(expectedToken) + "\n";
            case 130: return "found =, expecting " + auxErrorTable(expectedToken) + "\n";
            case 131: return "found ||, expecting " + auxErrorTable(expectedToken) + "\n";
            case 132: return "found &&, expecting " + auxErrorTable(expectedToken) + "\n";
            case 133: return "found !, expecting " + auxErrorTable(expectedToken) + "\n";
            case 134: return "found <, expecting " + auxErrorTable(expectedToken) + "\n";
            case 135: return "found <=, expecting " + auxErrorTable(expectedToken) + "\n";
            case 136: return "found >, expecting " + auxErrorTable(expectedToken) + "\n";
            case 137: return "found >=, expecting " + auxErrorTable(expectedToken) + "\n";
            case 138: return "found ==, expecting " + auxErrorTable(expectedToken) + "\n";
            case 139: return "found !=, expecting " + auxErrorTable(expectedToken) + "\n";
            case 140: return "found (, expecting " + auxErrorTable(expectedToken) + "\n";
            case 141: return "found ), expecting " + auxErrorTable(expectedToken) + "\n";
            case 142: return "found [, expecting " + auxErrorTable(expectedToken) + "\n";
            case 143: return "found ], expecting " + auxErrorTable(expectedToken) + "\n";
            case 144: return "found ,, expecting " + auxErrorTable(expectedToken) + "\n";
            case 145: return "found ;, expecting  " + auxErrorTable(expectedToken) + "\n";
            case 146: return "found #, expecting " + auxErrorTable(expectedToken) + "\n";
            default : return "printError() default case";
       }
    }//end printError

    private String auxErrorTable(int expectedToken){
        switch(expectedToken){
            case 100: return "CLASS";
            case 101: return "BEGIN";
            case 102: return "ENDCLASS";
            case 103: return "INTEGER";
            case 104: return "FLOAT";
            case 105: return "CHAR";
            case 106: return "STRING";
            case 107: return "BOOLEAN";
            case 108: return "READ";
            case 109: return "PRINT";
            case 110: return "IF";
            case 111: return "ENDIF";
            case 112: return "ELSE";
            case 113: return "WHILE";
            case 114: return "ENDWHILE";
            case 115: return "FOR";
            case 116: return "ENDFOR";
            case 117: return "TRUE";
            case 118: return "FALSE";
            case 119: return "identifier";
            case 120: return "integer";
            case 121: return "real_number";
            case 122: return "scientific_notation_number";
            case 123: return "char";
            case 124: return "string";
            case 125: return "+";
            case 126: return "-";
            case 127: return "*";
            case 128: return "/";
            case 129: return "%";
            case 130: return "=";
            case 131: return "||";
            case 132: return "&&";
            case 133: return "!";
            case 134: return "<";
            case 135: return "<=";
            case 136: return ">";
            case 137: return ">=";
            case 138: return "==";
            case 139: return "!=";
            case 140: return "(";
            case 141: return ")";
            case 142: return "[";
            case 143: return "]";
            case 144: return ",";
            case 145: return ";";
            case 146: return "#";
            default : return "auxErrorTable() default ";
        }
    }//end auxErrorTable

    public String getStatus() {
        return status;
    }
}//end class