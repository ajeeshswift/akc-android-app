package com.swift.akc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swift.akc.R;
import com.swift.akc.extras.Constants;

public class HarvestFilterFragment extends BaseFragment {

    public HarvestFilterFragment() {

    }

    public static HarvestFilterFragment newInstance(String title) {
        HarvestFilterFragment comingSoonFragment = new HarvestFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_PARAM_TITLE, title);
        comingSoonFragment.setArguments(bundle);
        return comingSoonFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mParentView = inflater.inflate(R.layout.fr_harvest_filter, container, false);
        return mParentView;
    }
}
