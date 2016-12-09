package codigoIntermedio;

import java.io.Serializable;

public abstract class Symbol implements Serializable {
    protected Integer type = 0;
    protected Integer direction = 0;
    protected String identifier = "";
    protected String value = "0";

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}//end class