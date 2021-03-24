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

//    화면
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder(); //viewholder 생성
        view.setTag(holder); //view에 viewholder를 보관만 ->내용을 채우는건 bindView에서!
        return view;
    }

//    화면에 내용 결합
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag(); //viewholder 객체 가져오기

//       holder에 화면 요소 채우기
        if (holder.tvContactName==null){
            holder.tvContactName = view.findViewById(R.id.tvContactName);
            holder.tvContactPhone = view.findViewById(R.id.tvContactPhone);
        }

        holder.tvContactName.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
        holder.tvContactPhone.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
//        TextView tvContactName = view.findViewById(R.id.tvContactName);
//        TextView tvContactPhone = view.findViewById(R.id.tvContactPhone);
//
//        tvContactName.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
//        tvContactPhone.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
    }

//    View Holder 객체
    static class ViewHolder {
        public ViewHolder(){
            tvContactName = null;
            tvContactPhone = null;
        }
        TextView tvContactName;
        TextView tvContactPhone;
    }
}
