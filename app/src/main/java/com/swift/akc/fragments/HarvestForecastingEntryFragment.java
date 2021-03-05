package com.swift.akc.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.swift.akc.R;
import com.swift.akc.activity.LandingPageActivity;
import com.swift.akc.adapters.PlantSeedListAdapter;
import com.swift.akc.extras.Constants;
import com.swift.akc.extras.Storage;
import com.swift.akc.extras.UIValidation;
import com.swift.akc.helper.ui.DatePickerView;
import com.swift.akc.network.ApiEndpoint;
import com.swift.akc.network.data.HarvestForcastingVO;
import com.swift.akc.network.data.PlantSeedListVO;
import com.swift.akc.network.data.PlantSeedVO;
import com.swift.akc.utils.DateUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HarvestForecastingEntryFragment extends BaseFragment implements TextWatcher,
        View.OnClickListener, AdapterView.OnItemClickListener {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String currentDate = sdf.format(new Date());

    EditText seedsown, cultivation, sowingdate;
    AutoCompleteTextView crop;
    DatePickerDialog picker;
    Button submit;

    private PlantSeedListAdapter mAdapter;

    private PlantSeedVO plantSeedVO;

    public HarvestForecastingEntryFragment() {

    }

    public static HarvestForecastingEntryFragment newInstance(String title) {
        HarvestForecastingEntryFragment comingSoonFragment = new HarvestForecastingEntryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_PARAM_TITLE, title);
        comingSoonFragment.setArguments(bundle);
        return comingSoonFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mParentView = inflater.inflate(R.layout.fr_harvest_forecasting_entry, container, false);
        return mParentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        villagename = (EditText) mParentView.findViewById(R.id.villagename);
        //crop = (EditText) mParentView.findViewById(R.id.crop);

        crop = mParentView.findViewById(R.id.autoCompletecrop);
        crop.addTextChangedListener(this);
        crop.setOnItemClickListener(this);
        crop.setThreshold(1);
        mAdapter = new PlantSeedListAdapter(getActivity(), R.layout.item_autocomplete, new ArrayList<>());
        crop.setAdapter(mAdapter);
        seedsown = (EditText) mParentView.findViewById(R.id.seedsown);
        cultivation = (EditText) mParentView.findViewById(R.id.cultivation);
        sowingdate = (EditText) mParentView.findViewById(R.id.sowingdate);
        submit = (Button) mParentView.findViewById(R.id.submit);
        crop.requestFocus();
        submit.setOnClickListener(this);

        sowingdate.setInputType(InputType.TYPE_NULL);
        sowingdate.setText(currentDate);
        sowingdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerView datePickerView = new DatePickerView();
                datePickerView.setDatePickerView(getActivity(), sowingdate);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            validate();
        }
    }

    private void validate() {
        if (crop.getText().toString().matches("")) {
            crop.setError("Enter Plant / Seed");
            crop.requestFocus();
            return;
        }

        if (seedsown.getText().toString().matches("")) {
            seedsown.setError("Enter Seed Sown in Kg's");
            seedsown.requestFocus();
            return;
        }

        if (cultivation.getText().toString().matches("")) {
            cultivation.setError("Enter Area Under Cultivation");
            cultivation.requestFocus();
            return;
        }

        if (sowingdate.getText().toString().matches("")) {
            sowingdate.setError("Choose Sowing Date");
            sowingdate.requestFocus();
            return;
        }
        harvestingforcastAPICall();
    }

    private void harvestingforcastAPICall() {
            JSONObject params = new JSONObject();
            try {
                params.put("plantId", plantSeedVO.getId());
                params.put("seeds", seedsown.getText().toString());
                params.put("area", cultivation.getText().toString());
                params.put("cropShowingDate", DateUtils.convertDateFormat(sowingdate.getText().toString()));
                params.put("farmId", Storage.selectedHarvestFarm.getFarmId());
                params.put("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                params.put("time", new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));

            } catch (Exception e) {
                e.printStackTrace();
            }

            Rx2AndroidNetworking.post(ApiEndpoint.HARVEST_FORECAST_API)
                    .addJSONObjectBody(params)
                    .build()
                    .getObjectObservable(HarvestForcastingVO.class)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<HarvestForcastingVO>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(HarvestForcastingVO object) {
                            Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_LONG).show();
                            crop.setText("");
                            seedsown.setText("");
                            cultivation.setText("");
                            sowingdate.setText(currentDate);
                            switchFragment(LandingPageActivity.FRAGMENT_HARVEST_FARM_SEARCH, "Harvest Forcasting Entry", true);
                        }

                        @Override
                        public void onError(Throwable e) {
                            hideLoading();
                            showApiError(e);

                        }

                        @Override
                        public void onComplete() {
                            hideLoading();
                        }
                    });
        }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (crop.isPerformingCompletion()) {
            // An item has been selected from the list. Ignore.
            return;
        }
        autoCompletePlantOrSeed(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        plantSeedVO = (PlantSeedVO) parent.getItemAtPosition(position);
        crop.setText(plantSeedVO.getFloraName());
    }

    private void autoCompletePlantOrSeed(String query) {
        Rx2AndroidNetworking.get(ApiEndpoint.FLORA_AUTOCOMPLETE_API)
                .addQueryParameter("query", query)
                .build()
                .getObjectObservable(PlantSeedListVO.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlantSeedListVO>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PlantSeedListVO plantSeedListVO) {
                        mAdapter.refresh(plantSeedListVO.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showApiError(e);
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }
}
