package codigoIntermedio;

import java.io.Serializable;

public class Variable extends Symbol implements Serializable{
    
    public Variable(String identifier ,Integer type, Integer direction){
        this.type = type;
        this.identifier = identifier;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "[id:"+identifier+",type:"+type+",direction:"+direction+",value:"+value+"]";
    }
}//end class