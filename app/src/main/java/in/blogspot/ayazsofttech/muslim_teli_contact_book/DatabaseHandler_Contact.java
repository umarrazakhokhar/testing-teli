package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ashfaque on 09/10/2017.
 */

public class DatabaseHandler_Contact extends SQLiteOpenHelper {


    //SQLiteDatabase db=this.getWritableDatabase();
    // Database Name
    private static final String DATABASE_NAME = "ASHU_DATABASE_Contact";

    // table name
    private static final String TABLE_NAME = "Contact_Table";


    // Contacts Table Columns names
    //private static final String ID = "id";

    private static final String Firebase_Mobile = "firebase_mobile";
    private static final String Firebase_Name = "firebase_name";
    private static final String Firebase_Surname = "firebase_surname";
    private static final String Firebase_Father = "firebase_father";
    private static final String Firebase_Category = "firebase_category";
    private static final String Firebase_District = "firebase_district";
    private static final String Firebase_City = "firebase_city";
    private static final String Firebase_State = "firebase_state";
    private static final String Firebase_HomeTown = "firebase_hometown";


    //private static final String Messages = "messages";
    //private static final String KEY_PH_NO = "phone_number";

    public DatabaseHandler_Contact(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Firebase_Mobile + " INTEGER  PRIMARY KEY ,"
                + Firebase_Name+ " TEXT , "
                + Firebase_Surname+ " TEXT , "
                + Firebase_Father + " TEXT , "
                + Firebase_Category + " TEXT , "
                + Firebase_District + " TEXT , "
                + Firebase_City+ " TEXT , "
                + Firebase_State+ " TEXT , "
                + Firebase_HomeTown+ " TEXT "
                +")";
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

    public boolean insertData2(String mobile,String name,String father,String surname,String city)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
       // contentValues.put(KEY_ID, ID);
        contentValues.put(Firebase_Mobile,mobile);
        contentValues.put(Firebase_Name, name);
        contentValues.put(Firebase_Father, father);
        contentValues.put(Firebase_Surname, surname);
        contentValues.put(Firebase_City, city);


        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
        {return false;}
        else
        { return true;}
    }
    public boolean insertData1(String mobile,String name,String surname,String father,String category,String district,String city,String state,String hometown)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        // contentValues.put(KEY_ID, ID);
        contentValues.put(Firebase_Mobile,mobile);
        contentValues.put(Firebase_Name, name);
        contentValues.put(Firebase_Surname, surname);
        contentValues.put(Firebase_Father, father);
        contentValues.put(Firebase_Category, category);
        contentValues.put(Firebase_District, district);
        contentValues.put(Firebase_City, city);
        contentValues.put(Firebase_State, state);
        contentValues.put(Firebase_HomeTown, hometown);

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

    public Cursor SelectData_count(String str)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" ";

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public Cursor SelectData_suranme_group()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT "+Firebase_Surname+" FROM "+TABLE_NAME+" GROUP BY "+Firebase_Surname+" order by "+Firebase_Surname;

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }
    public Cursor SelectData_city_group()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT "+Firebase_District+" FROM "+TABLE_NAME+" GROUP BY "+Firebase_District+" order by "+Firebase_District;

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }
    public Cursor SelectData_SearchBy(String SearchBy)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" Where"+
                " "+Firebase_Surname +" LIKE '%"+SearchBy+"%'"+
                " OR "+Firebase_City +" LIKE '%"+SearchBy+"%'"
                ;

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }


    public Cursor SelectData_Where_Name(String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" Where "+Firebase_Name +"='"+name+"'";

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public Cursor SelectData_Where_Mobile(String mobile)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" Where "+Firebase_Mobile +"="+mobile+"";

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }
    public Cursor SelectData_Where_All(String All)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" Where" +
                " "+Firebase_Mobile +" LIKE '%"+All+"%'"+
                " OR "+Firebase_Name +" LIKE '%"+All+"%'"+
                " OR "+Firebase_Surname +" LIKE '%"+All+"%'"+
                " OR "+Firebase_Father +" LIKE '%"+All+"%'"+
                " OR "+Firebase_Category +" LIKE '%"+All+"%'"+
                " OR "+Firebase_District +" LIKE '%"+All+"%'"+
                " OR "+Firebase_City +" LIKE '%"+All+"%'"+
                " OR "+Firebase_State +" LIKE '%"+All+"%'"+
                " OR "+Firebase_HomeTown +" LIKE '%"+All+"%'"+
                "ORDER BY random() "
                ;

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public Cursor SelectData_Where_search(String mobile,String name,String father,String surname,String city )
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" Where" +
                " "+Firebase_Mobile +" LIKE '%"+mobile+"%'"+
                " And "+Firebase_Name +" LIKE '%"+name+"%'"+
                " And "+Firebase_Surname +" LIKE '%"+surname+"%'"+
                " And "+Firebase_Father +" LIKE '%"+father+"%'"+
                " And "+Firebase_City +" LIKE '%"+city+"%'"+
                "ORDER BY random() "
                ;

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public Cursor SelectData_OrderBy()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String query="SELECT * FROM "+TABLE_NAME+" ORDER BY "+Firebase_Name+" ASC  ";

        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public void DeleteData(String Mobile)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        db.delete(TABLE_NAME,Firebase_Mobile+"="+Mobile,null);


    }
    public boolean UpdateData(String mobile,String name,String surname,String father,String category,String district,String city,String state,String hometown)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Firebase_Name, name);
        contentValues.put(Firebase_Surname, surname);
        contentValues.put(Firebase_Father, father);
        contentValues.put(Firebase_Category, category);
        contentValues.put(Firebase_District, district);
        contentValues.put(Firebase_City, city);
        contentValues.put(Firebase_State, state);
        contentValues.put(Firebase_HomeTown, hometown);

        long result=db.update(TABLE_NAME,contentValues,Firebase_Mobile+"='"+mobile+"'",null);

        if(result==-1)
        {return false;}
        else
        { return true;}
    }

    public void TRUNCATE_table()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        db.delete(TABLE_NAME, null, null);
        db.close();
    }



}