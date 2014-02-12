package com.andrey7melnikov.todomin;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.andrey7melnikov.utils.Mylog;

public class ToDoProvider extends ContentProvider {
	final String LOG_TAG = "myLogs";

	// // Константы для БД
	// БД
//	static final String DB_NAME = "mydbprov";
//	static final int DB_VERSION = 1;
//
//	// Таблица
//	static final String CONTACT_TABLE = "contacts";
//
//	// Поля
//	static final String CONTACT_ID = "_id";
//	static final String CONTACT_NAME = "name";
//	static final String CONTACT_EMAIL = "email";
	
	
	static final String DB_NAME = DB.DB_NAME;
	static final int DB_VERSION = DB.DB_VERSION;

	// Таблица
	static final String CONTACT_TABLE = DB.DB_TABLE;

	// Поля
	static final String CONTACT_ID = DB.COLUMN_ID;
	static final String CONTACT_NAME = DB.COLUMN_TEXT;
	static final String CONTACT_EMAIL = DB.DATE_ADD;

	// Скрипт создания таблицы
	static final String DB_CREATE = "create table " + CONTACT_TABLE + "("
			+ CONTACT_ID + " integer primary key autoincrement, "
			+ CONTACT_NAME + " text, " + CONTACT_EMAIL + " text" + ");";

	// // Uri
	// authority
	static final String AUTHORITY = "com.andrey7melnikov.todomin.provider";

	// path
	static final String CONTACT_PATH = "contacts";

	// Общий Uri
	public static final Uri CONTACT_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + CONTACT_PATH);

	// Типы данных
	// набор строк
	static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
			+ AUTHORITY + "." + CONTACT_PATH;

	// одна строка
	static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
			+ AUTHORITY + "." + CONTACT_PATH;

	// // UriMatcher
	// общий Uri
	static final int URI_CONTACTS = 1;

	// Uri с указанным ID
	static final int URI_CONTACTS_ID = 2;

	// описание и создание UriMatcher
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_CONTACTS);
		uriMatcher.addURI(AUTHORITY, CONTACT_PATH + "/#", URI_CONTACTS_ID);
	}

	DBHelper dbHelper;
	SQLiteDatabase SQLdb;

	public boolean onCreate() {
		Log.d(LOG_TAG, "ToDoProvider onCreate");
		dbHelper = new DBHelper(getContext());
		return true;
	}

	// чтение
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(LOG_TAG, "query, " + uri.toString());
		// проверяем Uri
		switch (uriMatcher.match(uri)) {
		case URI_CONTACTS: // общий Uri
			Log.d(LOG_TAG, "URI_CONTACTS");
			// если сортировка не указана, ставим свою - по имени
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = CONTACT_NAME + " ASC";
			}
			break;
		case URI_CONTACTS_ID: // Uri с ID
			String id = uri.getLastPathSegment();
			Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
			// добавляем ID к условию выборки
			if (TextUtils.isEmpty(selection)) {
				selection = CONTACT_ID + " = " + id;
			} else {
				selection = selection + " AND " + CONTACT_ID + " = " + id;
			}
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		SQLdb = dbHelper.getWritableDatabase();
		Cursor cursor = SQLdb.query(CONTACT_TABLE, projection, selection,
				selectionArgs, null, null, sortOrder);
		// просим ContentResolver уведомлять этот курсор
		// об изменениях данных в CONTACT_CONTENT_URI
		cursor.setNotificationUri(getContext().getContentResolver(),
				CONTACT_CONTENT_URI);
		return cursor;
	}

	public Uri insert(Uri uri, ContentValues values) {
		Log.d(LOG_TAG, "insert, " + uri.toString());
		if (uriMatcher.match(uri) != URI_CONTACTS)
			throw new IllegalArgumentException("Wrong URI: " + uri);

		SQLdb = dbHelper.getWritableDatabase();
		long rowID = SQLdb.insert(CONTACT_TABLE, null, values);
		Uri resultUri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowID);
		// уведомляем ContentResolver, что данные по адресу resultUri изменились
		getContext().getContentResolver().notifyChange(resultUri, null);
		return resultUri;
	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(LOG_TAG, "delete, " + uri.toString());
		switch (uriMatcher.match(uri)) {
		case URI_CONTACTS:
			Log.d(LOG_TAG, "URI_CONTACTS");
			break;
		case URI_CONTACTS_ID:
			String id = uri.getLastPathSegment();
			Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
			if (TextUtils.isEmpty(selection)) {
				selection = CONTACT_ID + " = " + id;
			} else {
				selection = selection + " AND " + CONTACT_ID + " = " + id;
			}
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		SQLdb = dbHelper.getWritableDatabase();
		int cnt = SQLdb.delete(CONTACT_TABLE, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return cnt;
	}

	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.d(LOG_TAG, "update, " + uri.toString());
		switch (uriMatcher.match(uri)) {
		case URI_CONTACTS:
			Log.d(LOG_TAG, "URI_CONTACTS");

			break;
		case URI_CONTACTS_ID:
			String id = uri.getLastPathSegment();
			Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
			if (TextUtils.isEmpty(selection)) {
				selection = CONTACT_ID + " = " + id;
			} else {
				selection = selection + " AND " + CONTACT_ID + " = " + id;
			}
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		SQLdb = dbHelper.getWritableDatabase();
		int cnt = SQLdb.update(CONTACT_TABLE, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return cnt;
	}

	public String getType(Uri uri) {
		Log.d(LOG_TAG, "getType, " + uri.toString());
		switch (uriMatcher.match(uri)) {
		case URI_CONTACTS:
			return CONTACT_CONTENT_TYPE;
		case URI_CONTACTS_ID:
			return CONTACT_CONTENT_ITEM_TYPE;
		}
		return null;
	}

	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			Mylog.a("DBHelper DBHelper");

		}

		public void onCreate(SQLiteDatabase db) {
			Mylog.a("DBHelper onCreate");

			db.execSQL(DB_CREATE);
			ContentValues cv = new ContentValues();
			for (int i = 1; i <= 3; i++) {
				cv.put(CONTACT_NAME, "name " + i);
				cv.put(CONTACT_EMAIL, "email " + i);
				db.insert(CONTACT_TABLE, null, cv);
			}
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Mylog.a("DBHelper onUpgrade");

		}
	}
}