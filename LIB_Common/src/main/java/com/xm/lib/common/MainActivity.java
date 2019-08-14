package com.xm.lib.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2;
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.Pair;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}


