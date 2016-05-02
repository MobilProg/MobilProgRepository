package hu.uniobuda.nik.turistapp;

/**
 * Created by András on 2016.04.22..
 */
public class informaciok {
    private int image_p;
    private String nev_p;
    private String leiras_p;

    public informaciok(int image_p, String nev_p, String leiras_p) {
        this.image_p = image_p;
        this.nev_p = nev_p;
        this.leiras_p = leiras_p;
    }

    public int getImage_p() {
        return image_p;
    }

    public String getNev_p() {
        return nev_p;
    }

    public String getLeiras_p() {
        return leiras_p;
    }
}
