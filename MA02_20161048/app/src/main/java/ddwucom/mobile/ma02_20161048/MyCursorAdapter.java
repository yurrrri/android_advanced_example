package ddwucom.mobile.ma02_20161048;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    int layout;

    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder(); //뷰의 정보를 담는 viewholder
        view.setTag(holder); //홀더를 만들어 보관만
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag(); //viewholder 꺼내기

        if (holder.tvCategory==null){
            holder.tvCategory = view.findViewById(R.id.tvCat);
            holder.tvLoc = view.findViewById(R.id.tvLoc);
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvReview = view.findViewById(R.id.tvReview);
            holder.rate = view.findViewById(R.id.rate);
        }

        holder.tvCategory.setText(cursor.getString(cursor.getColumnIndex(PlaceDBHelper.COL_CATEGORY)));
        holder.tvLoc.setText(cursor.getString(cursor.getColumnIndex(PlaceDBHelper.COL_LOCATION)));
        holder.tvName.setText(cursor.getString(cursor.getColumnIndex(PlaceDBHelper.COL_NAME)));
        holder.tvReview.setText(cursor.getString(cursor.getColumnIndex(PlaceDBHelper.COL_REVIEW)));
        holder.rate.setRating(Float.parseFloat(cursor.getString(cursor.getColumnIndex(PlaceDBHelper.COL_RATE))));
    }

    static class ViewHolder {
        public ViewHolder(){
            tvCategory = null;
            tvName = null;
            tvReview = null;
            rate = null;
            tvLoc = null;
        }

        TextView tvCategory;
        TextView tvName;
        TextView tvReview;
        RatingBar rate;
        TextView tvLoc;
    }
}
