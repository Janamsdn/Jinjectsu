package com.ericlouw.jinjectsu.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements IExampleView{

    private TextView textView;

    @Inject
    private IExamplePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textView = (TextView) this.findViewById(R.id.text_box);

        InjectionContainer.getInstalledInstance(this).beginScope(this);
        InjectionContainer.getInstalledInstance(this).inject(this);

        this.presenter.takeView(this);
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        InjectionContainer.getInstalledInstance(this).endScope();
    }

    @Override
    public void setText(String text) {
        this.textView.setText(text);
    }
}
