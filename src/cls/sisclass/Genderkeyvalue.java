package cls.sisclass;

public class Genderkeyvalue {
    private int gkey;
    private String gvalue;
    public Genderkeyvalue(int gkey, String gvalue)
    {
        this.gkey = gkey;
        this.gvalue = gvalue;
    }
    public int getgKey() {
        return gkey;
    }
    public String getgValue()
    {
        return gvalue;
    }

    @Override
    public String toString() {
        return gvalue;
    }
}

