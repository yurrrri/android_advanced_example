package mobile.database.dbtest02;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
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

        if (holder.tvContactName==null){
            holder.tvContactName = view.findViewById(R.id.tvContactName);
            holder.tvContactPhone = view.findViewById(R.id.tvContactPhone);
        }
//        TextView tvContactName = view.findViewById(R.id.tvContactName);
//        TextView tvContactPhone = view.findViewById(R.id.tvContactPhone);

        holder.tvContactName.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
        holder.tvContactPhone.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
    }

    static class ViewHolder {
        public ViewHolder(){
            tvContactName = null;
            tvContactPhone = null;
        }
        TextView tvContactName;
        TextView tvContactPhone;
    }
}
