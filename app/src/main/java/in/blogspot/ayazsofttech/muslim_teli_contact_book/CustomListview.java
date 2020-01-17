package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ashfaque on 30/09/2017.
 */

public class CustomListview extends ArrayAdapter<String> {

    private String[] title_name;
    private int[] mobile_number;

    private Activity context;

    public CustomListview(Activity context, String[] title_name,String[] mobile_number) {
        super(context, R.layout.listview_layout,title_name);

        this.context=context;
        this.title_name=title_name;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) r.getTag();
        }

        viewHolder.view_title_name.setText(title_name[position]);

        viewHolder.view_Round_title.setText(title_name[position].substring(0,1));


        return r;




    }

    class ViewHolder
    {
        TextView view_title_name,view_Round_title;
        ImageView ivw;
        ViewHolder(View v)
        {

            view_Round_title= (TextView) v.findViewById(R.id.Round_title);

            view_title_name= (TextView) v.findViewById(R.id.title_name);

        }
    }
}

