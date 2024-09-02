package cls.sisclass;

public class coursekey {
    private int ckey;
    private String cvalue;
    public coursekey(int ckey, String cvalue)
    {
        this.ckey = ckey;
        this.cvalue = cvalue;
    }
    public int getcKey() {
        return ckey;
    }
    public String getcValue()
    {
        return cvalue;
    }

    @Override
    public String toString() {
        return cvalue;
    }
}
