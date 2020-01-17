package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ashfaque on 09/10/2017.
 */

public class DatabaseHandler_Ali extends SQLiteOpenHelper {


    //SQLiteDatabase db=this.getWritableDatabase();
    // Database Name
    private static final String DATABASE_NAME = "ASHU_DATABase";

    // table name
    private static final String TABLE_NAME = "Calendar_Table";
    private static final String TABLE_NAME2 = "Hazrat_Table";

    // Contacts Table Columns names
    //private static final String ID = "id";
    private static final String Firebase_ID = "firebase_ID";
    private static final String Messages = "messages";
    //private static final String KEY_PH_NO = "phone_number";

    public DatabaseHandler_Ali(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Firebase_ID + " DATE  PRIMARY KEY,"
                + Messages+ " TEXT" + ")";

        db.execSQL(CREATE_TABLE);



    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public boolean insertData(String key, String message )
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
       // contentValues.put(KEY_ID, ID);
        contentValues.put(Firebase_ID,key);
        contentValues.put(Messages, message);

        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
        {return false;}
        else
        { return true;}
    }

    public Cursor SelectData()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" ";

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }
    public Cursor SelectData_Where(String key)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" Where "+Firebase_ID +"='"+key+"'";

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }
    public Cursor SelectData_OrderBy()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" ORDER BY "+Firebase_ID+" ASC  ";

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public void DeleteData(String key)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        db.delete(TABLE_NAME,Firebase_ID+"="+key,null);


    }
    public void UpdateData(String key, String message)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Messages, message);
        db.update(TABLE_NAME,contentValues,Firebase_ID+"='"+key+"'",null);


    }

    public void TRUNCATE_table()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        db.delete(TABLE_NAME, null, null);
        db.close();
    }



}