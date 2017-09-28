package org.mazhuang.guanggoo;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.base.FragmentCallBack;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.topicdetail.Commentable;
import org.mazhuang.guanggoo.util.ConstantUtil;

public class MainActivity extends AppCompatActivity
        implements FragmentCallBack, NavigationView.OnNavigationItemSelectedListener {

    private static String sStackName = MainActivity.class.getName();

    private long mLastBackPressTime;

    private ImageView mAvatarImageView;
    private TextView mUsernameTextView;

    private Menu mMenu;
    private MenuItem mCommentMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initLoginedInUserInfo();
    }

    private void initLoginedInUserInfo() {
        if (AuthInfoManager.getInstance().isLoginedIn()) {
            Glide.with(this)
                    .load(AuthInfoManager.getInstance().getAvatar())
                    .centerCrop()
                    .crossFade()
                    .into(mAvatarImageView);

            mUsernameTextView.setText(AuthInfoManager.getInstance().getUsername());
        }
    }

    private void initViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAvatarImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        mUsernameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // openPage(ConstantUtil.PROFILE_URL, getString(R.string.profile));
                if (AuthInfoManager.getInstance().isLoginedIn()) {
                    // TODO: 2017/9/17 跳转到个人页面
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    openPage(ConstantUtil.LOGIN_URL, getString(R.string.login));
                }
            }
        };
        mAvatarImageView.setOnClickListener(listener);
        mUsernameTextView.setOnClickListener(listener);

        openPage(ConstantUtil.BASE_URL, getString(R.string.default_order));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean canGoBack = (getSupportFragmentManager().getBackStackEntryCount() > 1);
                if (canGoBack) {
                    onBackPressed();
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getCurrentFragment();
                if (fragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    setTitle(baseFragment.getTitle());
                    boolean canGoBack = (getSupportFragmentManager().getBackStackEntryCount() > 1);
                    if (canGoBack) {
                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    } else {
                        toolbar.setNavigationIcon(R.drawable.ic_menu);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }

                    if (mCommentMenuItem != null) {
                        mCommentMenuItem.setVisible(fragment instanceof Commentable);
                    }
                }
            }
        });
    }

    public static void addFragmentToStack(FragmentManager fm, Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(sStackName);
        ft.commit();
    }

    public static void setOnlyFragmentToStack(FragmentManager fm, Fragment fragment) {
        fm.popBackStackImmediate(sStackName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        addFragmentToStack(fm, fragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof BaseFragment) {
                if (((BaseFragment) fragment).onBackPressed()) {
                    return;
                }
            }

            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                long backPressTime = System.nanoTime();
                long ONE_SECOND_NANO = 1000 * 1000 * 1000L;
                if (backPressTime - mLastBackPressTime > ONE_SECOND_NANO) {
                    Toast.makeText(this, getString(R.string.back_to_quit), Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                mLastBackPressTime = backPressTime;
                return;
            }

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        mCommentMenuItem = mMenu.findItem(R.id.action_comment);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_comment) {
            Fragment fragment = getCurrentFragment();
            if (fragment instanceof Commentable) {
                ((Commentable) fragment).showCommentView();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_home) {
            openPage(ConstantUtil.BASE_URL, getString(R.string.default_order));
        } else if (id == R.id.nav_nodes) {
            openPage(ConstantUtil.NODES_CLOUD_URL, getString(R.string.nodes_list));
        } else if (id == R.id.beginner_guide) {
            openPage(ConstantUtil.BEGINNER_GUIDE_URL, getString(R.string.beginner_guide));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    public void openPage(String url, String title) {
        BaseFragment fragment = FragmentFactory.getFragmentByUrl(url);
        if (fragment == null) {
            Toast.makeText(this, getString(R.string.error_happened), Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(BaseFragment.KEY_URL, url);
        bundle.putString(BaseFragment.KEY_TITLE, title);
        fragment.setArguments(bundle);
        if (fragment.isClearTop()) {
            setOnlyFragmentToStack(getSupportFragmentManager(), fragment);
        } else {
            addFragmentToStack(getSupportFragmentManager(), fragment);
        }
    }
}
