package cl.ozcc.inkanbike.objects;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by root on 09-03-16.
 */
public class MessageUI {

    private Context ctx;
    public MessageUI(Context context){
        this.ctx = context;
    }

    public void InflateMessage(String message){

    }
    public void InflateToast(String message){
       Toast.makeText(ctx,message,Toast.LENGTH_LONG).show();
    }
    public void InflateDialog(String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this.ctx);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.show();
    }
    public AlertDialog GetAlertDialog(String title, String message){
        return new AlertDialog.Builder(ctx).setTitle(title).setMessage(message).setCancelable(false).create();
    }

    public AlertDialog.Builder GetAlertDialog(String title, String message, Boolean Cancelable){
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(Cancelable);
        alert.create();
        return  alert;
    }
}