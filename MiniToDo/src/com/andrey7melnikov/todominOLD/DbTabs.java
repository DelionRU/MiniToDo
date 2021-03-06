package com.andrey7melnikov.todominOLD;


import com.andrey7melnikov.todomin.R;
import com.andrey7melnikov.todomin.R.drawable;
import com.andrey7melnikov.todomin.R.id;
import com.andrey7melnikov.todomin.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

public class DbTabs extends Activity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_tabs);
        
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
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
        tabHost.setCurrentTabByTag("tag2");
        
        // ���������� ������������ �������
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
      public void onTabChanged(String tabId) {
        Toast.makeText(getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
      }
    });
    }
}