package cl.ozcc.inkanbike.objects;

/**
 * Created by root on 26-09-15.
 */
public class CItemBikes {

    private String text;
    private String icon;

    public CItemBikes(String T, String I){
        this.text = T;
        this.icon = I;
    }
    public String getText(){
        return this.text;
    }
    public void setText(String T){
        this.text = T;
    }
    public String getIcon(){
        return this.icon;
    }
    public void setIcon(String I){
        this.icon = I;
    }
}