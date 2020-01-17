package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ashfaque on 30/09/2017.
 */

public class Custom_List_row extends ArrayAdapter<String> {

    private String[] Name,Father,City,Mobile,Verified,Surname,State;
    private int[] mobile_number;

    private Activity context;

    public Custom_List_row(Activity context,String[] Mobile,String[] Name,String[] Surname,String[] Father,String[] City,String[] State,String[] Verified) {
        super(context, R.layout.list_row,Mobile);
        //super(context, R.layout.list_row,Father);
        //super(context, R.layout.list_row,City);
        //super(context, R.layout.list_row,Mobile);
        //super(context, R.layout.list_row,Verified);

        this.context=context;
        this.Mobile=Mobile;
        this.Name=Name;
        this.Surname=Surname;
        this.Father=Father;
        this.City=City;
        this.State=State;

        this.Verified=Verified;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.list_row,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) r.getTag();
        }



        viewHolder.ViewHolder_Round_title.setText((Name[position]+"A").substring(0,1).toUpperCase());


        viewHolder.ViewHolder_Name.setText("  "+Name[position]);
        viewHolder.ViewHolder_Father.setText("  "+Father[position]);
        viewHolder.ViewHolder_City.setText("  "+City[position]+"-"+State[position]);
        viewHolder.ViewHolder_Mobile.setText("  "+Mobile[position].substring(0,7)+"...");
        viewHolder.ViewHolder_Verified.setText("  "+Surname[position]);




        return r;




    }

    class ViewHolder
    {
        TextView ViewHolder_Round_title,ViewHolder_Name,ViewHolder_Father,ViewHolder_City,ViewHolder_Mobile,ViewHolder_Verified;
        ImageView ivw;
        ViewHolder(View v)
        {

            ViewHolder_Round_title= (TextView) v.findViewById(R.id.Round_title);

            ViewHolder_Name= (TextView) v.findViewById(R.id.Name);
            ViewHolder_Father= (TextView) v.findViewById(R.id.Father);
            ViewHolder_City= (TextView) v.findViewById(R.id.City);
            ViewHolder_Mobile= (TextView) v.findViewById(R.id.Mobile);
            ViewHolder_Verified= (TextView) v.findViewById(R.id.Verified);

        }
    }
}

