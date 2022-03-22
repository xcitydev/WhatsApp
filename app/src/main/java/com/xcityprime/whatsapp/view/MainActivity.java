package com.xcityprime.whatsapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xcityprime.whatsapp.R;
import com.xcityprime.whatsapp.databinding.ActivityMainBinding;
import com.xcityprime.whatsapp.menu.CallsFragment;
import com.xcityprime.whatsapp.menu.ChatsFragment;
import com.xcityprime.whatsapp.menu.StatusFragment;
import com.xcityprime.whatsapp.view.activities.contacts.ContactsActivity;
import com.xcityprime.whatsapp.view.activities.settings.SettingsActivity;

import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpWithViewpager(binding.viewPager);
        binding.tablayout.setupWithViewPager(binding.viewPager);
        setSupportActionBar(binding.toolbar);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageSelected(int position) {
                changeFabIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void setUpWithViewpager(ViewPager viewpager){
        MainActivity.SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new StatusFragment(), "Status");
        adapter.addFragment(new CallsFragment(), "Calls");

        viewpager.setAdapter(adapter);
    }
    private static class SectionPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }



        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_search:
                Toast.makeText(MainActivity.this, "Action search", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_new_group:
                Toast.makeText(MainActivity.this, "New Group", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_new_broadcast:
                Toast.makeText(MainActivity.this, "New Broadcast", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_x_web:
                Toast.makeText(MainActivity.this, "X Web", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_starred_messages:
                Toast.makeText(MainActivity.this, "Starred Messages", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeFabIcon(final int index){
        binding.fabAction.hide();
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               switch (index){
                   case 0:
                       binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_chat));
                           binding.fabAction.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                               }
                           });
                       break;
                   case 1:
                       binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_camera));
                       break;
                   case 2:
                       binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_call));
                       break;

               }
               binding.fabAction.show();
           }
       }, 400);
    }
}
