package com.example.gamesos;

public class OCo {
    private String giatri;
    private int trangthai;
    public OCo (){}
    public OCo (String x, int i){
        this.giatri=x;
        this.trangthai=i;
    }
    public String getGT()
    {
        return this.giatri;
    }
    public int getTT()
    {
        return this.trangthai;
    }
    public void setGT(String x)
    {
        this.giatri=x;
    }
    public void setTT(int x)
    {
        this.trangthai=x;
    }
}
