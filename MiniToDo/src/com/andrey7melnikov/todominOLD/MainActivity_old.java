package com.andrey7melnikov.todominOLD;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrey7melnikov.todomin.DB;
import com.andrey7melnikov.todomin.DBman;
import com.andrey7melnikov.todomin.R;
import com.andrey7melnikov.todomin.R.id;
import com.andrey7melnikov.todomin.R.layout;
import com.andrey7melnikov.todomin.R.menu;
import com.andrey7melnikov.todomin.R.string;
import com.andrey7melnikov.utils.Mylog;

public class MainActivity_old extends FragmentActivity implements
		LoaderCallbacks<Cursor> {

	private static final int CM_DELETE_ID = 1;
	ListView lvData;
	DB db;
	SimpleCursorAdapter scAdapter, scAdapterMy;
	EditText textTask;
	Integer i = 0;
	CheckBox checkBoxImp;
	Intent intent;
	final String TAG = "States";

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// открываем подключение к БД
		db = new DB(this);
		db.open();

		// формируем столбцы сопоставления
		String[] from = new String[] { DB.COLUMN_TEXT };
		int[] to = new int[] { R.id.tvText };

		checkBoxImp = (CheckBox) findViewById(R.id.checkBoxImp);

		// создааем адаптер и настраиваем список
		scAdapterMy = new MyCursor(this, R.layout.item, null, from, to);
		scAdapterMy.setViewBinder(new MyViewBinder());

		scAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from,
				to, 0);
		lvData = (ListView) findViewById(R.id.lvdata);
		lvData.setAdapter(scAdapterMy);

		textTask = (EditText) findViewById(R.id.editTextTask);

		// добавляем контекстное меню к списку
		registerForContextMenu(lvData);
		//lvData.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Mylog.a("onItemClick in lvData.setOnItemClickListener, position - "
						+ position);
				Mylog.a("onItemClick in lvData.setOnItemClickListener, arg3 - "
						+ id);
				//db.delRec(id);
				getSupportLoaderManager().getLoader(0).forceLoad();

				// Object o = lvData.getItemAtPosition(position);
				/*
				 * write you handling code like... String st = "sdcard/"; File f
				 * = new File(st+o.toString()); // do whatever u want to do with
				 * 'f' File object
				 */
			}
		});
		// создаем лоадер для чтения данных
		getSupportLoaderManager().initLoader(0, null, this);
		Mylog.a("End onCreate");
	}
	
	public void CheckClck(View v) {
        //code to check if this checkbox is checked!
		Mylog.a("CheckClck START");
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
    		Mylog.a("CheckClck isChecked ");
        }
        Mylog.a("Перебор 1 Старт");
        int count = this.lvData.getAdapter().getCount();
        Mylog.a("Перебор 1 , count = " + count);
        for (int i = 0; i < count; i++) {
            Mylog.a("Перебор 1 , i= " + i);

            if (this.lvData.isItemChecked(i)) {
            	Mylog.a("Перебор 1 this.lvData.isItemChecked(i))  , i= " + i);
            	//lvData.remove(i)
            }
        }
        Mylog.a("Перебор 2 Старт");
        SparseBooleanArray sbArray = lvData.getCheckedItemPositions();
        Mylog.a("Перебор 2, sbArray.size(); - " + sbArray.size());
        for (int i = 0; i < sbArray.size(); i++) {
            int key = sbArray.keyAt(i);
            Mylog.a("Перебор 2, sbArray.keyAt(i); " +  sbArray.keyAt(i) + " i = " + i);
            if (sbArray.get(key))
            	Mylog.a("Перебор 2 if (sbArray.get(key)) ");
          }
    }

	// обработка нажатия кнопки добавить
	public void onButtonClick(View view) {
		// добавляем запись
		Mylog.a("onButtonClick");
		// db.addRec("sometext " + (scAdapter.getCount() + 1),
		// R.drawable.ic_launcher);
		// получаем новый курсор с данными
		// getSupportLoaderManager().getLoader(0).forceLoad();

		String taskStr = textTask.getText().toString();

		if (taskStr.length() == 0) {
			Mylog.a("taskStr.length() == 0");
			Toast.makeText(this, "empty task", Toast.LENGTH_SHORT).show();
		} else {
			if (checkBoxImp.isChecked()) {
				Mylog.a("CheckBoxImp.isChecked()");
				db.addRecI(taskStr);

			} else {
				Mylog.a("NOT CheckBoxImp.isChecked()");
				db.addRec(taskStr);

			}
			// db.addRec(taskStr)1;
			Mylog.a("Added task - " + taskStr);
			checkBoxImp.setChecked(false);
			getSupportLoaderManager().getLoader(0).forceLoad();
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Mylog.a("onOptionsItemSelected");
		Mylog.a("onOptionsItemSelecte, item.getItemId()" + item.getItemId());

		switch (item.getItemId()) {

		case R.id.action_settings:
			Mylog.a("onOptionsItemSelected - learDBmenu");
			break;

//		case R.id.clearDBmenu:
//			Mylog.a("onOptionsItemSelected - learDBmenu");
//			break;

		case R.id.SQLactivtyMenu:
			Mylog.a("onOptionsItemSelected - SQLactivtyMenu");
			intent = new Intent(this, DBman_old.class);
			startActivity(intent);

			break;

//		case R.id.SQLactivtyMenu2:
//			Mylog.a("onOptionsItemSelected - SQLactivtyMenu2");
//			intent = new Intent(this, DBman2.class);
//			startActivity(intent);
//
//			break;

		case R.id.ExitMenu:
			Mylog.a("onOptionsItemSelected - ExitMenu");
			break;

		}
		return super.onOptionsItemSelected(item);

	}

	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == CM_DELETE_ID) {
			// получаем из пункта контекстного меню данные по пункту списка
			AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
					.getMenuInfo();
			// извлекаем id записи и удаляем соответствующую запись в БД
			Mylog.a("onContextItemSelected, acmi.id " + acmi.id);
			db.delRec(acmi.id);
			// получаем новый курсор с данными
			getSupportLoaderManager().getLoader(0).forceLoad();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "MainActivity: onResume()");
		getSupportLoaderManager().initLoader(0, null, this);
		getSupportLoaderManager().getLoader(0).forceLoad();

	}

	protected void onDestroy() {
		super.onDestroy();
		// закрываем подключение при выходе
		Mylog.a("onDestroy()");
		db.close();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
		return new MyCursorLoader(this, db);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		scAdapterMy.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	static class MyCursorLoader extends CursorLoader {

		DB db;

		public MyCursorLoader(Context context, DB db) {
			super(context);
			this.db = db;
		}

		@Override
		public Cursor loadInBackground() {
			Cursor cursor = db.getAllData();
			return cursor;
		}

	}

	class MyCursor extends SimpleCursorAdapter {

		@SuppressWarnings("deprecation")
		public MyCursor(Context context, int layout, Cursor c, String[] from,
				int[] to) {
			super(context, layout, c, from, to);
			// TODO Auto-generated constructor stub
		}
	}

	class MyViewBinder implements SimpleCursorAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View v, Cursor arg_cur, int arg_int) {
			// TODO Auto-generated method stub
			i++;
			String str = arg_cur.getString(arg_cur
					.getColumnIndex(DB.COLUMN_TEXT));
			String str2 = arg_cur.getString(arg_cur
					.getColumnIndex(DB.COLUMN_IMP));
			((TextView) v).setTypeface(Typeface.DEFAULT);
			((TextView) v).setText(str);
			try {
				if (Integer.parseInt(str2) == 2) {
					((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
					((TextView) v).setText(str);
				}
			} catch (Exception e) {
			}
			return true;
			// if (arg_cur.moveToFirst()) {
			// String str =
			// arg_cur.getString(arg_cur.getColumnIndex(DB.COLUMN_TEXT));
			// Mylog.a("start setViewValue - str DB.COLUMN_TEXT - " + str);
			// str = arg_cur.getString(arg_cur.getColumnIndex(DB.COLUMN_IMP));
			// Mylog.a("start setViewValue - str DB.COLUMN_IMP - " + str);
			//
			// }
			// if (v instanceof ImageView) {
			// Mylog.a("");
			// } else if (v instanceof TextView) {
			// TextView textView = (TextView) view;
			// // do what you want with textView
			// }
			// Mylog.a(" setViewValue return false");
			// return false;
		}

	}

}
