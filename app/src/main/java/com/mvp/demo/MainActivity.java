package com.mvp.demo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.mvp.demo.base.BaseActivity;
import com.mvp.demo.module.media.ui.MediaChannelFragment;
import com.mvp.demo.module.news.ui.NewsFragment;
import com.mvp.demo.module.photo.ui.PhotoFragment;
import com.mvp.demo.module.video.ui.VideosFragment;
import com.mvp.demo.widget.helper.BottomNavigationViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

/**
 * @author 罗涛
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomNavigationSelectItem";
    private static final int FRAGMENT_NEWS = 0;
    private static final int FRAGMENT_PHOTO = 1;
    private static final int FRAGMENT_VIDEO = 2;
    private static final int FRAGMENT_MEDIA = 3;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.mBottomNavigationView)
    BottomNavigationView mMBottomNavigationView;
    @BindView(R.id.mDrawerLayout)
    DrawerLayout mMDrawerLayout;
    private NewsFragment mNewsFragment;
    private PhotoFragment mPhotoFragment;
    private VideosFragment mVideosFragment;
    private MediaChannelFragment mMediaChannelFragment;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            showFragment(FRAGMENT_NEWS);
        } else {
            mNewsFragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag(NewsFragment.class.getName());
            mPhotoFragment = (PhotoFragment) getSupportFragmentManager().findFragmentByTag(PhotoFragment.class.getName());
            mVideosFragment = (VideosFragment) getSupportFragmentManager().findFragmentByTag(VideosFragment.class.getName());
            mMediaChannelFragment = (MediaChannelFragment) getSupportFragmentManager().findFragmentByTag(MediaChannelFragment.class.getName());
            showFragment(savedInstanceState.getInt(POSITION));
            mMBottomNavigationView.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        }
        initView();
    }

    private void initView() {
        mToolbar.inflateMenu(R.menu.menu_activity_main);
        //完全显示三个及以上的item
        BottomNavigationViewHelper.disableShiftMode(mMBottomNavigationView);
        //支持toolbar
        initToolbar(mToolbar, "新闻", true);
        //        //设置bottom监听
        mMBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_news:
                        showFragment(FRAGMENT_NEWS);
                        break;
                    case R.id.action_photo:
                        showFragment(FRAGMENT_PHOTO);
                        break;
                    case R.id.action_video:
                        showFragment(FRAGMENT_VIDEO);
                        break;
                    case R.id.action_media:
                        showFragment(FRAGMENT_MEDIA);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mMDrawerLayout, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mMDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void showFragment(int index) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);
        position = index;
        switch (index) {
            case FRAGMENT_NEWS:
                mToolbar.setTitle("头条");
                if (mNewsFragment == null) {
                    mNewsFragment = NewsFragment.newInstance();
                    fragmentTransaction.add(R.id.container, mNewsFragment, NewsFragment.class.getName());
                } else {
                    fragmentTransaction.show(mNewsFragment);
                }
                fragmentTransaction.commit();
                break;
            case FRAGMENT_PHOTO:
                mToolbar.setTitle("图片");
                if (mPhotoFragment == null) {
                    mPhotoFragment = PhotoFragment.newInstance();
                    fragmentTransaction.add(R.id.container, mPhotoFragment, PhotoFragment.class.getName());
                } else {
                    fragmentTransaction.show(mPhotoFragment);
                }
                fragmentTransaction.commit();
                break;
            case FRAGMENT_VIDEO:
                mToolbar.setTitle("视频");
                if (mVideosFragment == null) {
                    mVideosFragment = VideosFragment.newInstance();
                    fragmentTransaction.add(R.id.container, mVideosFragment, VideosFragment.class.getName());
                } else {
                    fragmentTransaction.show(mVideosFragment);
                }
                fragmentTransaction.commit();
                break;
            case FRAGMENT_MEDIA:
                mToolbar.setTitle("头条号");
                if (mMediaChannelFragment == null) {
                    mMediaChannelFragment = MediaChannelFragment.newInstance();
                    fragmentTransaction.add(R.id.container, mMediaChannelFragment, MediaChannelFragment.class.getName());
                } else {
                    fragmentTransaction.show(mMediaChannelFragment);
                }
                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mNewsFragment != null) {
            fragmentTransaction.hide(mNewsFragment);
        }
        if (mPhotoFragment != null) {
            fragmentTransaction.hide(mPhotoFragment);
        }
        if (mVideosFragment!=null){
            fragmentTransaction.hide(mVideosFragment);
        }
        if (mMediaChannelFragment!=null){
            fragmentTransaction.hide(mMediaChannelFragment);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(POSITION, position);
        outState.putInt(SELECT_ITEM, mMBottomNavigationView.getSelectedItemId());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }
}
