package com.highcom.ponshu;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Filter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.highcom.ponshu.databinding.ActivityMainBinding;
import com.highcom.ponshu.ui.searchlist.SearchListFragment;
import com.highcom.ponshu.util.SakenowaDataCollector;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchListFragment.SearchListFragmentListener {

    public enum MENU_TYPE {
        SEARCH_TITLE,
        EDIT_TITLE,
    }
    private MENU_TYPE mMenuType;

    private ActivityMainBinding binding;
    private SakenowaDataCollector mCollector;
    private SearchView mSearchView;
    private SearchListFragment mSearchListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMenuType = MENU_TYPE.SEARCH_TITLE;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mCollector = SakenowaDataCollector.getInstance();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_timeline, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // 検索バーの追加
        getMenuInflater().inflate(R.menu.main_menu, menu);

        if (mMenuType == MENU_TYPE.SEARCH_TITLE) {
            menu.findItem(R.id.menu_search_view).setVisible(true);
            menu.findItem(R.id.menu_done_button).setVisible(false);

            MenuItem menuItem = menu.findItem(R.id.menu_search_view);
            mSearchView = (SearchView)menuItem.getActionView();
            SearchView.SearchAutoComplete searchAutoComplete = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
            searchAutoComplete.setHintTextColor(Color.rgb(0xff, 0xff, 0xff));
            searchAutoComplete.setHint("検索キーワード");
            mSearchView.setOnQueryTextListener(this);

            mSearchView.setOnCloseListener(() -> {
                getSupportFragmentManager().beginTransaction().remove(mSearchListFragment).commit();
                mSearchListFragment = null;
                return false;
            });
        } else {
            menu.findItem(R.id.menu_search_view).setVisible(false);
            menu.findItem(R.id.menu_done_button).setVisible(true);
        }

        return true;
    }

    public void setSearchWordFilter(String searchWord) {
        Filter filter = mSearchListFragment.getFilter();
        if (TextUtils.isEmpty(searchWord)) {
            filter.filter(null);
        } else {
            filter.filter(searchWord);
        }
    }

    @Override
    public void onAdapterClicked(String name) {
        mSearchView.setQuery(name, true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mSearchListFragment == null) {
            setMenuType(MENU_TYPE.SEARCH_TITLE);
            mSearchListFragment = new SearchListFragment(mCollector.getBrandsList(), this);
            getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_activity_main, mSearchListFragment).commit();
        }
        setSearchWordFilter(newText);
        return false;
    }

    public void setMenuType(MENU_TYPE type) {
        mMenuType = type;
    }
}