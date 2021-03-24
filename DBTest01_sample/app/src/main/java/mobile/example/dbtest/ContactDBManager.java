package mobile.example.dbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ContactDBManager {
    ContactDBHelper helper = null;
    Cursor cursor = null;

    public ContactDBManager(Context context) {
        helper = new ContactDBHelper(context);
    }

    public ArrayList<ContactDto> getAllContacts() {
        ArrayList contactList = new ArrayList();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + helper.TABLE_NAME, null);

        while (cursor.moveToNext()){
//            결과를 객체에 저장할 경우!
            ContactDto dto = new ContactDto();
            dto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            dto.setName(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
            dto.setPhone(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
            dto.setCategory(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_CAT)));
            contactList.add(dto);
        }

        cursor.close();
        helper.close();

        return contactList;
    }

    public boolean addNewContact(ContactDto dto) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put(helper.COL_NAME, dto.getName());
        row.put(helper.COL_PHONE, dto.getPhone());
        row.put(helper.COL_CAT, dto.getCategory());

        long count = db.insert(helper.TABLE_NAME, null, row);
        helper.close();
        if (count > 0) return true;
        return false;
    }

    public boolean updateContact(ContactDto dto) {
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(helper.COL_NAME, dto.getName());
        row.put(helper.COL_PHONE, dto.getPhone());
        row.put(helper.COL_CAT, dto.getCategory());

        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(dto.getId()) };
        int result = sqLiteDatabase.update(helper.TABLE_NAME, row, whereClause, whereArgs);
        helper.close();
        if (result > 0) return true;
        return false;
    }

    public boolean removeContact(int id) {
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        int result = sqLiteDatabase.delete(helper.TABLE_NAME, whereClause,whereArgs);
        helper.close();
        if (result > 0) return true;
        return false;
    }

    public String searchContact(String key){
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();

        String selection = "name=? or name like ?";
        String[] selectArgs = new String[]{key, '%'+key+'%'};
        Cursor cursor =
                sqLiteDatabase.query(helper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);

        String result = "";
        while (cursor.moveToNext()){
            result+=cursor.getString(cursor.getColumnIndex(helper.COL_CAT))+" - ";
            result+=cursor.getString(cursor.getColumnIndex(helper.COL_NAME))+" ( ";
            result+=cursor.getString(cursor.getColumnIndex(helper.COL_PHONE))+" ) \n";
        }

        cursor.close();
        helper.close();

        return result;
    }

    public void close() {
        if (helper != null) helper.close();
        if (cursor != null) cursor.close();
    };
}

