package hu.uniobuda.nik.turistapp;

/**
 * Created by Bence on 2016.04.04..
 */
public class Helyek
{
    private String cim;
    private String kep;

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public String getKep() {
        return kep;
    }

    public void setKep(String kep) {
        this.kep = kep;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String gps;
    private int id;


    public Helyek(String cim, String kep,String gps,int id) {
       this.id=id;
        this.cim=cim;
        this.gps=gps;
        this.kep=kep;
    }


}
