package codigoIntermedio;

import java.io.Serializable;

public class Constant extends Symbol implements Serializable {
    
    public Constant(String value, Integer type, Integer direction){
        this.type = type;
        this.value = value;
        this.direction = direction;
    }//end constructor

    @Override
    public String getIdentifier() {
        return value;
    }
    
    @Override
    public String toString() {
        return "[type:"+type+",direction:"+direction+",value:"+value+"]";
    }
}//end Constant