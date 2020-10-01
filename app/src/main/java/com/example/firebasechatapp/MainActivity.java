package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.firebasechatapp.activities.AllUsersActivity;
import com.example.firebasechatapp.activities.LogInActivity;
import com.example.firebasechatapp.activities.SettingsActivity;
import com.example.firebasechatapp.adapter.SectionsPageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private Context context = MainActivity.this;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPageAdapter sectionsPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBarMainActivity);
        tabLayout = findViewById(R.id.tabLayoutMainActivity);
        viewPager = findViewById(R.id.viewPagerMainActivity);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat App");
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null)
        {
            startActivity(new Intent(context, LogInActivity.class));
        }
    } //

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_log_out_button,menu);
        return super.onCreateOptionsMenu(menu);
    }//

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuItemSignOut:
                firebaseAuth.signOut();
                startActivity(new Intent(context,LogInActivity.class));
                finish();
                break;
            case R.id.menuItemAccountSetting:
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menuItemAllUsers:
                Intent mIntent = new Intent(context, AllUsersActivity.class);
                startActivity(mIntent);
                break;
        }
        return super.onOptionsItemSelected(item);


    }
}