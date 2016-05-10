package cl.ozcc.inkanbike.objects;

/**
 * Created by root on 26-09-15.
 */
public class CItemSos {

    private String text;
    private int icon;

    public CItemSos(String T, int I){
        this.text = T;
        this.icon = I;
    }
    public String getText(){
        return this.text;
    }
    public void setText(String T){
        this.text = T;
    }
    public int getIcon(){
        return this.icon;
    }
    public void setIcon(int I){
        this.icon = I;
    }
}