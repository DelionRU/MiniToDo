package com.andrey7melnikov.todomin;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.andrey7melnikov.utils.Mylog;
//at home 19-01
public class DBman extends Activity implements OnClickListener {

	final String LOG_TAG = "myLogs";

	Button btnAdd, btnRead, btnReadT, btnClear;
	EditText etName, etEmail;
	TextView dbstatus;
	String readrowFull, readrow;
	int impTemp, i;
	TableLayout ll;
	TableRow row;
	TextView column1t, column2t, column3t, column4t, column5t, column7t,
			column6t;
	DB db;
	String[] columns;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dbman2);

		db = new DB(this);
		db.open();

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);

		btnReadT = (Button) findViewById(R.id.btnReadT);
		btnReadT.setOnClickListener(this);

		btnRead = (Button) findViewById(R.id.btnRead);
		btnRead.setOnClickListener(this);

		btnClear = (Button) findViewById(R.id.btnClear);
		btnClear.setOnClickListener(this);

		etName = (EditText) findViewById(R.id.etName);
		etEmail = (EditText) findViewById(R.id.etEmail);

		dbstatus = (TextView) findViewById(R.id.dbstatus);
		ll = (TableLayout) findViewById(R.id.tablesql);
		ReadDBT();
		
		 /* TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
	        // �������������
	        tabHost.setup();
	        
	        TabHost.TabSpec tabSpec;
	        
	        // ������� ������� � ��������� ���
	        tabSpec = tabHost.newTabSpec("tag1");
	        // �������� �������
	        tabSpec.setIndicator("������� 1");
	        // ��������� id ���������� �� FrameLayout, �� � ������ ����������
	        tabSpec.setContent(R.id.tvTab1);
	        // ��������� � �������� �������
	        tabHost.addTab(tabSpec);
	        
	        tabSpec = tabHost.newTabSpec("tag2");
	        // ��������� �������� � ��������
	        // � ����� ������ ������ �������� ���� xml-����, 
	        // ������� ���������� �������� �� ��������� �������
	        tabSpec.setIndicator("������� 2", getResources().getDrawable(R.drawable.tab_icon_selector));
	        tabSpec.setContent(R.id.tvTab2);        
	        tabHost.addTab(tabSpec);
	        
	        tabSpec = tabHost.newTabSpec("tag3");
	        // ������� View �� layout-�����
	        View v = getLayoutInflater().inflate(R.layout.tab_header, null);
	        // � ������������� ���, ��� ���������
	        tabSpec.setIndicator(v);
	        tabSpec.setContent(R.id.tvTab3);        
	        tabHost.addTab(tabSpec);
	        
	        // ������ ������� ����� ������� �� ���������
	        tabHost.setCurrentTabByTag("tag1");
	        
	        // ���������� ������������ �������
	        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
	      public void onTabChanged(String tabId) {
	        Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
	      }
	    }); */
		// ������� ������ ��� �������� � ���������� �������� ��
		// dbHelper = new DBHelper(this);
	}

	/*
	 * public void ReadDB() { readrowFull = ""; Log.d(LOG_TAG,
	 * "--- Rows in mytable: ---"); // ������ ������ ���� ������ �� �������
	 * mytable, �������� Cursor Cursor c = db.getAllData();
	 * 
	 * // ������ ������� ������� �� ������ ������ ������� // ���� � ������� ���
	 * �����, �������� false if (c.moveToFirst()) {
	 * 
	 * // ���������� ������ �������� �� ����� � ������� int idColIndex =
	 * c.getColumnIndex(DB.COLUMN_ID); int textColIndex =
	 * c.getColumnIndex(DB.COLUMN_TEXT); int impColIndex =
	 * c.getColumnIndex(DB.COLUMN_IMP); int stateColIndex =
	 * c.getColumnIndex(DB.COLUMN_STATE);
	 * 
	 * 
	 * do { // �������� �������� �� ������� �������� � ����� ��� � ���
	 * Log.d(LOG_TAG, "ID = " + c.getInt(idColIndex) + ", TEXT = " +
	 * c.getString(textColIndex) + ", IMP = " + c.getString(impColIndex) +
	 * ", STATE= " + c.getString(stateColIndex)); readrow = "ID = " +
	 * c.getInt(idColIndex) + ", TEXT = " + c.getString(textColIndex) +
	 * ", IMP = " + c.getString(impColIndex) + ", STATE= " +
	 * c.getString(stateColIndex); readrowFull = readrowFull + readrow + "\n";
	 * // ������� �� ��������� ������ // � ���� ��������� ��� (������� -
	 * ���������), �� false - // ������� �� �����
	 * 
	 * } while (c.moveToNext()); dbstatus.setText(readrowFull); } else {
	 * Log.d(LOG_TAG, "0 rows"); dbstatus.setText("Empty table");
	 * 
	 * }
	 * 
	 * c.close();
	 * 
	 * }
	 */

	public void ReadDBT() {
		readrowFull = "";
		// ������ ������ ���� ������ �� ������� mytable, �������� Cursor
		Cursor c = db.getAllData();

		// ������ ������� ������� �� ������ ������ �������
		// ���� � ������� ��� �����, �������� false
		if (c.moveToFirst()) {


			String[] columns = { DB.COLUMN_ID, DB.COLUMN_IMP, DB.COLUMN_STATE,
					DB.COLUMN_TEXT, DB.DATE_ADD, DB.DATE_COMPL, DB.DATE_DEL };

			row = new TableRow(this);
			TableRow.LayoutParams lp = new TableRow.LayoutParams(
					TableRow.LayoutParams.WRAP_CONTENT);
			row.setLayoutParams(lp);
			//row.setPadding(5, 0, 5, 0);

			for (String col : columns) {
				// String strToPrint = num;
				column1t = new TextView(this);
				column1t.setBackgroundResource(R.drawable.cell_shape);
				column1t.setText(" " + col + " ");
				row.addView(column1t);

			}
			ll.addView(row);
			i = 1;
			do {
				row = new TableRow(this);
				
				for (String col : columns) {
					// String strToPrint = num;
					int id = c.getColumnIndex(col);
					
					column1t = new TextView(this);
					column1t.setBackgroundResource(R.drawable.cell_shape);
					column1t.setText(" " +c.getString(id)+ " ");
					row.addView(column1t);

				}
				ll.addView(row, i);
				i++;
				

			} while (c.moveToNext());
			// dbstatus.setText(readrowFull);

		} else {
			Log.d(LOG_TAG, "0 rows");
			dbstatus.setText("Empty table");

		}

		c.close();

	}

	@Override
	public void onClick(View v) {

		// ������������ � ��
		// SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (v.getId()) {

		case R.id.btnAdd:
			// ������� ������ ��� ������
			// ContentValues cv = new ContentValues();

			// �������� ������ �� ����� �����
			String text = etName.getText().toString();
			String imp = etEmail.getText().toString();

			// ���������� ������ ��� ������� � ���� ���: ������������ ������� -
			// ��������
			if (text == null) {
				Mylog.a("DBman.onClick text == null");
				text = " ";
			}

			try {
				impTemp = Integer.parseInt(imp);
			} catch (Exception e) {
				Mylog.a("DBman.onClick cant impTemp = Integer.parseInt(imp); ");
				impTemp = 1;
			}
			db.addRecI(text);
			// ��������� ������ � �������� �� ID
			// long rowID = db.insert("mytable", null, cv);
			// db.addRecTest(name, email);

			Log.d(LOG_TAG, "row inserted");
			ReadDBT();
			break;

		case R.id.btnRead:
			ReadDBT();
			break;

		case R.id.btnReadT:
			ReadDBT();
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
	
	
	
	
	
	
	
	
	//--------------------------------------------

	// ������� ����
	// ������� ������ ������
	
	/*
	 * 			// ���������� ������ �������� �� ����� � �������
			int idColIndex = c.getColumnIndex(DB.COLUMN_ID);
			int textColIndex = c.getColumnIndex(DB.COLUMN_TEXT);
			int impColIndex = c.getColumnIndex(DB.COLUMN_IMP);
			int stateColIndex = c.getColumnIndex(DB.COLUMN_STATE);
			int dateAddColIndex = c.getColumnIndex(DB.DATE_ADD);
			int dateComplColIndex = c.getColumnIndex(DB.DATE_COMPL);
			int dateDelIndex = c.getColumnIndex(DB.DATE_DEL);
			// columns[] = new String[1];
			// columns[] = {DB.COLUMN_ID};

	 * row = new TableRow(this); TableRow.LayoutParams lp = new
	 * TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	 * row.setLayoutParams(lp); row.setPadding(5, 0, 5, 0); // CheckBox checkBox
	 * = new CheckBox(this); column1t = new TextView(this);
	 * 
	 * 
	 * 
	 * 
	 * column2t = new TextView(this); column3t = new TextView(this); column4t =
	 * new TextView(this); column5t = new TextView(this); column6t = new
	 * TextView(this); column7t = new TextView(this);
	 * 
	 * // addBtn = new ImageButton(this); //
	 * addBtn.setImageResource(R.drawable.add); // minusBtn = new
	 * ImageButton(this); // minusBtn.setImageResource(R.drawable.minus); // qty
	 * = new TextView(this); // checkBox.setText("hello"); column1t.setText(" "
	 * +DB.COLUMN_ID + " "); column2t.setText(" " +DB.COLUMN_TEXT + " ");
	 * column3t.setText(" " +DB.COLUMN_IMP + " "); column4t.setText(" "
	 * +DB.COLUMN_STATE + " "); column5t.setText(" " +DB.DATE_ADD + " ");
	 * column6t.setText(" " +DB.DATE_COMPL + " "); column7t.setText(" "
	 * +DB.DATE_DEL + " ");
	 * 
	 * column1t.setBackgroundResource(R.drawable.cell_shape);
	 * column2t.setBackgroundResource(R.drawable.cell_shape);
	 * column3t.setBackgroundResource(R.drawable.cell_shape);
	 * column4t.setBackgroundResource(R.drawable.cell_shape);
	 * column5t.setBackgroundResource(R.drawable.cell_shape);
	 * column6t.setBackgroundResource(R.drawable.cell_shape);
	 * column7t.setBackgroundResource(R.drawable.cell_shape);
	 * 
	 * row.addView(column1t); row.addView(column2t); row.addView(column3t);
	 * row.addView(column4t);
	 */
	
	/*
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
	TableRow rowT = new TableRow(this);

	TableRow.LayoutParams lpT = new TableRow.LayoutParams(
			TableRow.LayoutParams.WRAP_CONTENT);
	rowT.setLayoutParams(lpT);
	// rowT.setBackgroundResource(R.drawable.cell_shape);
	rowT.setPadding(5, 0, 5, 0);
	// rowT.setLayoutParams(2,new
	// TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));

	column1t = new TextView(this);
	column2t = new TextView(this);
	column3t = new TextView(this);
	column4t = new TextView(this);
	column5t = new TextView(this);
	column6t = new TextView(this);
	column7t = new TextView(this);

	column1t.setBackgroundResource(R.drawable.cell_shape);
	column2t.setBackgroundResource(R.drawable.cell_shape);
	column3t.setBackgroundResource(R.drawable.cell_shape);
	column4t.setBackgroundResource(R.drawable.cell_shape);
	column5t.setBackgroundResource(R.drawable.cell_shape);
	column6t.setBackgroundResource(R.drawable.cell_shape);
	column7t.setBackgroundResource(R.drawable.cell_shape);

	column1t.setText(" " + c.getString(idColIndex) + " ");
	column2t.setText(" " + c.getString(textColIndex) + " ");
	column3t.setText(" " + c.getString(impColIndex) + " ");
	column4t.setText(" " + c.getString(stateColIndex) + " ");

	rowT.addView(column1t);

	rowT.addView(column2t);

	rowT.addView(column3t);
	rowT.addView(column4t);
	ll.addView(rowT);
	*/
	
		
	// ������� �� ��������� ������
	// � ���� ��������� ��� (������� - ���������), �� false -
	// ������� �� �����

}
