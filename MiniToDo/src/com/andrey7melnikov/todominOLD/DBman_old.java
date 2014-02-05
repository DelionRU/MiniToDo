package com.andrey7melnikov.todominOLD;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andrey7melnikov.todomin.DB;
import com.andrey7melnikov.todomin.R;
import com.andrey7melnikov.todomin.R.id;
import com.andrey7melnikov.todomin.R.layout;
import com.andrey7melnikov.utils.Mylog;

public class DBman_old extends Activity implements OnClickListener {

	final String LOG_TAG = "myLogs";

	Button btnAdd, btnRead, btnClear;
	EditText etName, etEmail;
	TextView dbstatus;
	String readrowFull, readrow;
	int impTemp;

	DB db;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dbman);

		db = new DB(this);
		db.open();

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);

		btnRead = (Button) findViewById(R.id.btnRead);
		btnRead.setOnClickListener(this);

		btnClear = (Button) findViewById(R.id.btnClear);
		btnClear.setOnClickListener(this);

		etName = (EditText) findViewById(R.id.etName);
		etEmail = (EditText) findViewById(R.id.etEmail);

		dbstatus = (TextView) findViewById(R.id.dbstatus);

		// ������� ������ ��� �������� � ���������� �������� ��
		// dbHelper = new DBHelper(this);
	}

	@Override
	public void onClick(View v) {

		// ������� ������ ��� ������
		 ContentValues cv = new ContentValues();

		// �������� ������ �� ����� �����
		String text = etName.getText().toString();
		String imp = etEmail.getText().toString();

		// ������������ � ��
		// SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (v.getId()) {

		case R.id.btnAdd:
			Log.d(LOG_TAG, "--- Insert in mytable: ---");
			// ���������� ������ ��� ������� � ���� ���: ������������ ������� -
			// ��������
			if (text == null ) {
				Mylog.a("DBman.onClick text == null");
				text =" ";
			}
			
			try {
				impTemp = Integer.parseInt(imp);
			} catch (Exception e) {
				Mylog.a("DBman.onClick cant impTemp = Integer.parseInt(imp); ");
				impTemp =1;
			}
			db.addRec(text);
			// ��������� ������ � �������� �� ID
			// long rowID = db.insert("mytable", null, cv);
			// db.addRecTest(name, email);

			Log.d(LOG_TAG, "row inserted");
			break;

		case R.id.btnRead:
			readrowFull = "";
			Log.d(LOG_TAG, "--- Rows in mytable: ---");
			// ������ ������ ���� ������ �� ������� mytable, �������� Cursor
			Cursor c = db.getAllData();
			// Cursor c = db.query("mytable", null, null, null, null, null,
			// null);

			// ������ ������� ������� �� ������ ������ �������
			// ���� � ������� ��� �����, �������� false
			if (c.moveToFirst()) {

				// ���������� ������ �������� �� ����� � �������
				int idColIndex = c.getColumnIndex( DB.COLUMN_ID);
				int textColIndex = c.getColumnIndex(DB.COLUMN_TEXT);
				int impColIndex = c.getColumnIndex(DB.COLUMN_IMP);
				int stateColIndex = c.getColumnIndex( DB.COLUMN_STATE);


				do {
					// �������� �������� �� ������� �������� � ����� ��� � ���
					Log.d(LOG_TAG,
							"ID = " + c.getInt(idColIndex) + ", TEXT = "
									+ c.getString(textColIndex) + ", IMP = "
									+ c.getString(impColIndex) + ", STATE= "
											+ c.getString(stateColIndex));
					readrow = "ID = " + c.getInt(idColIndex) + ", TEXT = "
							+ c.getString(textColIndex) + ", IMP = "
							+ c.getString(impColIndex) + ", STATE= "
									+ c.getString(stateColIndex);
					readrowFull = readrowFull + readrow + "\n";
					// ������� �� ��������� ������
					// � ���� ��������� ��� (������� - ���������), �� false -
					// ������� �� �����

				} while (c.moveToNext());
				dbstatus.setText(readrowFull);
			} else {
				Log.d(LOG_TAG, "0 rows");
				dbstatus.setText("Empty table");

			}

			c.close();
			break;
		case R.id.btnClear:
			Log.d(LOG_TAG, "--- Clear mytable: ---");
			// / db.delete();
			// // ������� ��� ������
			int clearCount = db.deleteAll();
			readrowFull = "deleted rows count = " + clearCount;
			Log.d(LOG_TAG, readrowFull);
			dbstatus.setText(readrowFull);
			break;
		}
		// ��������� ����������� � ��
		// dbHelper.close();
	}

}
