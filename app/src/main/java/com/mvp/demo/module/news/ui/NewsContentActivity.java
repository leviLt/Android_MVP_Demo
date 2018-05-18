package com.mvp.demo.module.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mvp.demo.InitApp;
import com.mvp.demo.R;
import com.mvp.demo.module.news.model.MultiNewsArticleDataBean;

/**
 * @author 罗涛
 */
public class NewsContentActivity extends AppCompatActivity {
    private static final String TAG = "NewsContentActivity";
    private static final String IMG = "img";

    public static void launch(MultiNewsArticleDataBean bean) {
        InitApp.appContext.startActivity(new Intent(InitApp.appContext, NewsContentActivity.class)
                .putExtra(TAG, bean)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launch(MultiNewsArticleDataBean bean, String imgUrl) {
        InitApp.appContext.startActivity(new Intent(InitApp.appContext, NewsContentActivity.class)
                .putExtra(TAG, bean)
                .putExtra(IMG, imgUrl)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, NewsContentFragment.newInstance(getIntent().getParcelableExtra(TAG), getIntent().getStringExtra(IMG)));
    }
}
