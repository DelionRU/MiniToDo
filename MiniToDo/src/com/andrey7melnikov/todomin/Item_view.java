package com.andrey7melnikov.todomin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.andrey7melnikov.utils.Mylog;


public class Item_view extends Activity {
	DB db;
	CheckBox checkImp, checkCompl;
	Button buttonDel, buttonOk;
	TextView tasktext,DateAdd;
	ContentValues map;
	static long idIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_item_view);
		Mylog.a("1");
		checkImp = (CheckBox) findViewById(R.id.checkImp);
		checkCompl = (CheckBox) findViewById(R.id.checkCompl);
		buttonDel = (Button) findViewById(R.id.buttonDel);
		buttonOk = (Button) findViewById(R.id.buttonOk);
		tasktext = (TextView) findViewById(R.id.tasktext);
		DateAdd = (TextView) findViewById(R.id.DateAdd);

		Mylog.a("2");
	    Intent intent = getIntent();
	    idIntent = intent.getLongExtra("id", 0);
		db = new DB(this);
		db.open();
		Cursor cur = db.getDataID(idIntent);
		Mylog.a("3");
		
		if (cur.moveToFirst()) {
			do {
				map = new ContentValues();
				DatabaseUtils.cursorRowToContentValues(cur, map);
				//retVal.add(map);
			} while (cur.moveToNext());
		}
		map.getAsString(DB.COLUMN_TEXT);
		String text =map.getAsString(DB.COLUMN_TEXT);
		String imp = map.getAsString(DB.COLUMN_IMP);
		String state = map.getAsString(DB.COLUMN_STATE);
		String add = map.getAsString(DB.DATE_ADD);
		Mylog.a("4");
		try {
			if (Integer.parseInt(imp) == DB.IMP_IMP) {
				checkImp.setChecked(true);
			}
		} catch (Exception e) {
			Mylog.a("error Integer.parseInt(imp) == IMP_TASK");
		}
		
		try {
			if (Integer.parseInt(state) == DB.TASK_COMPL) {
				checkCompl.setChecked(true);
			}
		} catch (Exception e) {
			Mylog.a("error Integer.parseInt(state) == DB.TASK_COMPL");

		}

		tasktext.setText(text);
		DateAdd.setText(add);
		

	}
	
	 public ContentValues GetData(){
		 Mylog.a("GetData");
		 map = new ContentValues();
		// map.put(DB.COLUMN_ID, idIntent);
		 map.put(DB.COLUMN_TEXT, tasktext.getText().toString());
		 map.put(DB.DATE_ADD, DateAdd.getText().toString());
		 if (checkCompl.isChecked()) {
			 map.put(DB.COLUMN_STATE,DB.TASK_COMPL);
		} else {
			 map.put(DB.COLUMN_STATE,DB.TASK_ADD);
		}
		 if (checkImp.isChecked()) {
			 map.put(DB.COLUMN_IMP,DB.IMP_IMP);
		} else {
			 map.put(DB.COLUMN_IMP,DB.IMP_NORM);
		}
		 return map;

	    }
	
	 public void DeleteBtn(View v){
		 Mylog.a("DeleteBtn");
		 
	    }
	 
	 public void SaveBtn(View v){
		 Mylog.a("OkBtn");
		 map = GetData();
		 db.addRecCV(map, idIntent);
		 finish();
	    }
	 
	 


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_view, menu);
		return true;
	}

}
