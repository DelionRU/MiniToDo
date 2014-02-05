package com.andrey7melnikov.todomin;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.andrey7melnikov.utils.Mylog;

public class Settings extends PreferenceActivity implements OnPreferenceClickListener {
  
  CheckBoxPreference chb3;
  PreferenceCategory categ2;
	DB db;

  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.settings_xml);
    
	db = new DB(this);
	db.open();
    findPreference("WidgetScreenSettings").setOnPreferenceClickListener(this);
    findPreference("DeleteTasks").setOnPreferenceClickListener(this);
    findPreference("DeleteDB").setOnPreferenceClickListener(this);
    
    
//    PreferenceScreen b = (PreferenceScreen) findPreference("WidgetScreenSettings");
//    b.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//
//        @Override
//        public boolean onPreferenceClick(Preference preference) {
//            PreferenceScreen a = (PreferenceScreen) preference;
//            a.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.white);
//            return false;
//        }
//    });
    
//    findPreference("DeleteTasks").setOnPreferenceClickListener(this);
//    
//   // DelDB.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//
//        @Override
//        public boolean onPreferenceClick(Preference preference) {
//            PreferenceScreen a = (PreferenceScreen) preference;
//            a.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.white);
//            return false;
//        }
//    });

//    chb3 = (CheckBoxPreference) findPreference("chb3");
//    categ2  = (PreferenceCategory) findPreference("categ2");
//    categ2.setEnabled(chb3.isChecked());
//    
//    chb3.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//      public boolean onPreferenceClick(Preference preference) {
//        categ2.setEnabled(chb3.isChecked());
//        return false;
//      }
//    });
    
    
    
  }
  
  public boolean onPreferenceClick(Preference preference) {
      String key = preference.getKey();
      
      if(key.equals("WidgetScreenSettings")){
    	  ((PreferenceScreen) preference).getDialog().getWindow().setBackgroundDrawableResource(android.R.color.white);
          return true;
      }
      
      if(key.equals("DeleteTasks")){
    	  Mylog.a("DeleteTasks");
    	  db.HideAll();
  		  Toast.makeText(this, "All tasks delete", Toast.LENGTH_LONG).show();


      }
      
      if(key.equals("DeleteDB")){
    	  Mylog.a("DeleteDB");
    	  Mylog.a("DeleteTasks");
    	  try {
    		  Toast.makeText(this, "Deleted = " + db.deleteAll(), Toast.LENGTH_LONG).show();
    	  }
    	  catch (Exception e) {
    		  Toast.makeText(this, "Can't Delete", Toast.LENGTH_LONG).show();
    	  }
    	  return true;
      }
      return false;
  }
}