package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ashfaque on 30/09/2017.
 */

public class CustomListview_hazrat_ali extends ArrayAdapter<String> {

    private String[] images_name;
    private  int images;
    private Activity context;

    public CustomListview_hazrat_ali(Activity context, String[] images_name) {
        super(context, R.layout.listview_layout_hazrat_ali,images_name);

        this.context=context;
        this.images_name=images_name;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout_hazrat_ali,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) r.getTag();
        }

        viewHolder.tvw1.setText(images_name[position]);


        return r;




    }

    class ViewHolder
    {
        TextView tvw1;

        ViewHolder(View v)
        {
            tvw1= (TextView) v.findViewById(R.id.tvmessage);

        }
    }
}

