package hu.uniobuda.nik.turistapp;

/**
 * Created by Bence on 2016.03.20..
 */
public class Morse
{//B betu közzöti hely;K karakter közötti hely; R rövid jel; H hosszú jel;V szóköz;
    // MÉG KELL MAJD PARSOLNI!!!!
    private String MA="BRKH";
    private String MB="BHKRKRKR";
    private String MC="BHKRKHKR";
    private String MD="BHKRKR";
    private String ME="BR";
    private  String MF="BRKRKHKR";
    private String MG="BHKHKR";
    private String MH="BRKRKRKR";
    private String MI="BRKR";
    private  String MJ="BRKHKH";
    private String MK="BHKRKH";
    private String ML="BRKHKRKR";
    private String MM="BHKH";
    private String MN="BHKR";
    private String MO="BHKHKH";
    private  String MP="BRKHKHKR";
    private String MQ="BHKHKRKH";
    private String MR="BRKHKR";
    private String MS="BRKRKR";
    private String MT="BH";
    private String MU="BRKRKH";
    private String MV="BRKRKRKH";
    private String MW="BRKHKH";
    private String MX="BHKRKRKH";
    private String MY="BHKRKHKH";
    private String MZ="BHKHKRKR";
    private String MSpace="V";
    private String nyiltszoveg;


    public Morse(String nyiltszoveg) {
        this.nyiltszoveg = nyiltszoveg;

    }
    public String atalakaitas()
    {
        String kodoltszoveg="";
        for(int i=0; i<nyiltszoveg.length(); i++)
        {
            switch (nyiltszoveg.charAt(i))
            {
                case 'A': kodoltszoveg+=MA;break;
                case 'B': kodoltszoveg+=MB;break;
                case 'C': kodoltszoveg+=MC;break;
                case 'D': kodoltszoveg+=MD;break;
                case 'E': kodoltszoveg+=ME;break;
                case 'F': kodoltszoveg+=MF;break;
                case 'G': kodoltszoveg+=MG;break;
                case 'H': kodoltszoveg+=MH;break;
                case 'I': kodoltszoveg+=MI;break;
                case 'J': kodoltszoveg+=MJ;break;
                case 'K': kodoltszoveg+=MK;break;
                case 'L': kodoltszoveg+=ML;break;
                case 'M': kodoltszoveg+=MM;break;
                case 'N': kodoltszoveg+=MN;break;
                case 'O': kodoltszoveg+=MO;break;
                case 'P': kodoltszoveg+=MP;break;
                case 'Q': kodoltszoveg+=MQ;break;
                case 'R': kodoltszoveg+=MR;break;
                case 'S': kodoltszoveg+=MS;break;
                case 'T': kodoltszoveg+=MT;break;
                case 'U': kodoltszoveg+=MU;break;
                case 'V': kodoltszoveg+=MV;break;
                case 'W': kodoltszoveg+=MW;break;
                case 'X': kodoltszoveg+=MX;break;
                case 'Y': kodoltszoveg+=MY;break;
                case 'Z': kodoltszoveg+=MZ;break;
                case ' ':kodoltszoveg+=MSpace; break;

            }
        }

        return kodoltszoveg;

    }



}
