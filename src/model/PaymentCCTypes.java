package model;

import java.io.Serializable;

/**
 * Created by msp on 4/8/16.
 */
public class PaymentCCTypes  implements Serializable{

    private String AE;
    private String VI;
    private String MC;
    private String DI;

    private String key;
    private String value;


    @Override
    public String toString() {
        return "PaymentCCTypes{" +
                "AE='" + AE + '\'' +
                ", VI='" + VI + '\'' +
                ", MC='" + MC + '\'' +
                ", DI='" + DI + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getAE() {
        return AE;
    }

    public void setAE(String AE) {
        this.AE = AE;
    }

    public String getVI() {
        return VI;
    }

    public void setVI(String VI) {
        this.VI = VI;
    }

    public String getMC() {
        return MC;
    }

    public void setMC(String MC) {
        this.MC = MC;
    }

    public String getDI() {
        return DI;
    }

    public void setDI(String DI) {
        this.DI = DI;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
