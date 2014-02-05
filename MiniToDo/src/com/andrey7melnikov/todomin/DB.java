package com.andrey7melnikov.todomin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.andrey7melnikov.utils.Mylog;

public class DB {

	final String LOG_TAG = "myLogs";

	private static final String DB_NAME = "mydb";
	private static final int DB_VERSION = 11;
	// private static final String DB_TABLE = "mytab";
	private static final String DB_TABLE = "tasks";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_IMP = "important";
	public static final String COLUMN_STATE = "state";
	public static final String COLUMN_TEXT = "text";
	public static final String COLUMN_TEXT_FULL = "text_full";
	public static final String DATE_ADD = "date_add";
	public static final String DATE_COMPL = "date_compl";
	public static final String DATE_DEL = "date_del";
	
	public static final int TASK_ADD = 30;
	// public static final int TASK_IMP = 40;
	public static final int TASK_COMPL = 20;
	public static final int TASK_HIDE = 10;

	public static final int IMP_IMP = 20;
	public static final int IMP_NORM = 10;

	// private static final String DB_CREATE = "create table " + DB_TABLE + "("
	// + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_IMG
	// + " integer, " + COLUMN_TXT + " text" + ");";
	private static final String DB_CREATE = 
			"create table " + DB_TABLE + "("
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_IMP + " int, " 
			+ COLUMN_STATE + " int," 
			+ COLUMN_TEXT + " text," 
			+ COLUMN_TEXT_FULL + " text," 
			+ DATE_ADD + " text," 
			+ DATE_COMPL + " text," 
			+ DATE_DEL + " text" 
			+ ");";

	private final Context mCtx;

	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;

	public DB(Context ctx) {
		mCtx = ctx;
	}

	// открыть подключение
	public void open() {
		mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
		mDB = mDBHelper.getWritableDatabase();
	}

	public int deleteAll() {
		int a = mDB.delete(DB_TABLE, null, null);
		return a;
	}

	public void HideAll() {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_STATE, TASK_HIDE);
		mDB.update(DB_TABLE, cv, null, null);
	}
	// закрыть подключение
	public void close() {
		if (mDBHelper != null)
			mDBHelper.close();
	}

	// получить все данные из таблицы DB_TABLE
	public Cursor getAllData() {
		return mDB.query(DB_TABLE, null, null, null, null, null, null);
	}
	
	public Cursor getDataID(long id) {
		Cursor c;
		return c = mDB.query(DB_TABLE, null, COLUMN_ID + " = " + id, null,
				null, null, null);
	}

	public Cursor getAllDataSort() {
		return mDB.query(DB_TABLE, null,  DB.COLUMN_STATE + " != 10", null, null, null, DB.COLUMN_STATE
				+ " DESC," + DB.COLUMN_IMP
				+ " DESC");
	}

	// добавить запись в DB_TABLE
	private void addRec(String txt, int imp) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TEXT, txt);
		cv.put(COLUMN_IMP, imp);
		cv.put(COLUMN_STATE, TASK_ADD);
		cv.put(DATE_ADD, getCurDate());

		mDB.insert(DB_TABLE, null, cv);
	}
	
	public void addRecCV(ContentValues cv, long id) {
		//mDB.insert(DB_TABLE, null, cv);
		mDB.update(DB_TABLE, cv, COLUMN_ID + " = " + id, null);
	}

	public void addRecI(String txt) {
		addRec(txt, IMP_IMP);
	}
	
	public String getCurDate() {
		Calendar cal = Calendar.getInstance();
		Date currentLocalTime = cal.getTime();

		DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");

		String localTime = date.format(currentLocalTime);
		return localTime; 
	}
	
	public void addRec(String txt) {
		addRec(txt, 10);
	}

	// удалить запись из DB_TABLE
	public void delRec(long id) {
		mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
	}

	public void makeOK(long id) {
		// mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
		Cursor c = mDB.query(DB_TABLE, null, COLUMN_ID + " = " + id, null,
				null, null, null);
		ArrayList<ContentValues> retVal = new ArrayList<ContentValues>();
		ContentValues map = new ContentValues();
		;
		int i = 0;
		if (c.moveToFirst()) {
			do {
				map = new ContentValues();
				DatabaseUtils.cursorRowToContentValues(c, map);
				retVal.add(map);
				i++;
			} while (c.moveToNext());
		}
		Mylog.a("Перевод i = " + i);
		c.close();
		
		if (map.getAsInteger(COLUMN_STATE) == TASK_COMPL) {
			map.put(COLUMN_STATE, TASK_HIDE);
		} else {
			map.put(COLUMN_STATE, TASK_COMPL);
		}
		
		i = mDB.update(DB_TABLE, map, COLUMN_ID + " = " + id, null);
		Mylog.a("update i = " + i);

	}

	// класс по созданию и управлению БД
	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		// создаем и заполняем БД
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);
			Mylog.a("onCreate SQL");

			ContentValues cv = new ContentValues();
			for (int i = 1; i < 5; i++) {
				cv.put(COLUMN_TEXT, "sometext " + i);
				cv.put(COLUMN_IMP, 1);
				cv.put(COLUMN_STATE, 1);

				db.insert(DB_TABLE, null, cv);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Mylog.a("onUpgrade");
			Log.d(LOG_TAG, " --- onUpgrade database from " + oldVersion
					+ " to " + newVersion + " version --- ");

			if (oldVersion == 5 && newVersion == 6) {
				// Mylog.a("onUpgrade 5 to 6");
				// db.execSQL("DROP TABLE IF EXISTS '" + DB_TABLE_TASK + "'");
				// db.execSQL(DB_CREATE_TASK);
				//
				// ContentValues cv = new ContentValues();
				// for (int i = 1; i < 3; i++) {
				// cv.put(COLUMN_TEXT, "sometext " + i);
				// cv.put(COLUMN_IMP, 1);
				// cv.put(COLUMN_STATE, 1);
				// }
			}

			if (oldVersion == 6 && newVersion == 7) {
				Mylog.a("onUpgrade FROM " + oldVersion + " TO " + newVersion);
				db.execSQL("DROP TABLE IF EXISTS '" + DB_TABLE + "'");
				db.execSQL(DB_CREATE);

				ContentValues cv = new ContentValues();
				for (int i = 1; i < 3; i++) {
					cv.put(COLUMN_TEXT, "sometext " + i);
					cv.put(COLUMN_IMP, 1);
					cv.put(COLUMN_STATE, 1);

				}
				db.insert(DB_TABLE, null, cv);
			}

			if (oldVersion == 7 && newVersion == 8) {
				Mylog.a("onUpgrade FROM " + oldVersion + " TO " + newVersion);
				db.execSQL("DROP TABLE IF EXISTS '" + DB_TABLE + "'");
				db.execSQL(DB_CREATE);

				ContentValues cv = new ContentValues();
				for (int i = 1; i < 3; i++) {
					cv.put(COLUMN_TEXT, "sometext " + i);
					cv.put(COLUMN_IMP, 1);
					cv.put(COLUMN_STATE, 1);

				}
				db.insert(DB_TABLE, null, cv);
			}
			
			if (oldVersion == 9 && newVersion == 10) {
				Mylog.a("onUpgrade FROM " + oldVersion + " TO " + newVersion);
				db.execSQL("DROP TABLE IF EXISTS '" + DB_TABLE + "'");
				db.execSQL(DB_CREATE);

				ContentValues cv = new ContentValues();
				for (int i = 1; i < 3; i++) {
					cv.put(COLUMN_TEXT, "sometext " + i);
					cv.put(COLUMN_IMP, 10);
					cv.put(COLUMN_STATE, 30);
					cv.put(DATE_ADD, getCurDate());
					db.insert(DB_TABLE, null, cv);


				}
			}
			
			if (newVersion == 11) {
				Mylog.a("onUpgrade FROM " + oldVersion + " TO " + newVersion);
				db.execSQL("DROP TABLE IF EXISTS '" + DB_TABLE + "'");
				db.execSQL(DB_CREATE);

				ContentValues cv = new ContentValues();
				for (int i = 1; i < 3; i++) {
					cv.put(COLUMN_TEXT, "sometext " + i);
					cv.put(COLUMN_IMP, 10);
					cv.put(COLUMN_STATE, 30);
					cv.put(DATE_ADD, getCurDate());
					db.insert(DB_TABLE, null, cv);


				}
			}
		}

	}

	// Мусорка для апдейт ДБ

	/*
	 * if (oldVersion == 1 && newVersion == 2) { Mylog.a("onUpgrade 1 to 2");
	 * db.execSQL("DROP TABLE IF EXISTS '" + DB_TABLE + "'");
	 * Mylog.a("onUpgrade - drop succes"); db.execSQL(DB_CREATE); ContentValues
	 * cv = new ContentValues(); for (int i = 1; i < 5; i++) {
	 * cv.put(COLUMN_TXT, "sometext " + i); cv.put(COLUMN_IMG,
	 * R.drawable.ic_launcher); db.insert(DB_TABLE, null, cv); } }
	 * 
	 * if (oldVersion == 3 && newVersion == 4) { Mylog.a("onUpgrade 3 to 4");
	 * db.execSQL("DROP TABLE IF EXISTS '" + DB_TABLE_TASK + "'");
	 * db.execSQL(DB_CREATE_TASK); ContentValues cv = new ContentValues(); for
	 * (int i = 1; i < 3; i++) { cv.put(COLUMN_TXT, "Newtask N " + i);
	 * cv.put(COLUMN_IMG, R.drawable.ic_launcher); db.insert(DB_TABLE_TASK,
	 * null, cv); }
	 * 
	 * } if (oldVersion == 4 && newVersion == 5) { Mylog.a("onUpgrade 4 to 5");
	 * db.execSQL("DROP TABLE IF EXISTS '" + DB_TABLE_TASK + "'");
	 * db.execSQL(DB_CREATE_TASK);
	 * 
	 * ContentValues cv = new ContentValues(); for (int i = 1; i < 3; i++) {
	 * cv.put(COLUMN_TXT, "Newtask N " + i); cv.put(COLUMN_IMP, false);
	 * db.insert(DB_TABLE_TASK, null, cv); } }
	 */
}