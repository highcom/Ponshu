package com.highcom.ponshu.ui.detailitem;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarEntry;
import com.highcom.ponshu.MainActivity;
import com.highcom.ponshu.R;
import com.highcom.ponshu.databinding.FragmentItemDetailBinding;
import com.highcom.ponshu.datamodel.Aroma;
import com.highcom.ponshu.datamodel.Brand;
import com.highcom.ponshu.ui.searchlist.SearchListFragment;
import com.highcom.ponshu.util.LineChartItem;
import com.highcom.ponshu.util.RadarChartItem;
import com.highcom.ponshu.util.SakenowaDataCollector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ItemDetailFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private FragmentItemDetailBinding binding;
    private String mSelectBrandId;
    private EditText mTitle;
    private EditText mSubTitle;
    private Spinner mSpecificSpinner;
    private EditText mPolishingRate;
    private EditText mBrewery;
    private EditText mArea;
    private EditText mRawMaterial;
    private EditText mCapacity;
    private EditText mStorageTemperature;
    private Spinner mHowToDrinkSpinner;

    private SearchListFragment mSearchListFragment;
    private RadarChartItem mRadarChartItem;
    private LineChartItem mLineChartItem;
    private TextView mInputDateTextView;
    private SimpleDateFormat mSdf;
    private Date mSelectDate;

    private ItemDetailViewModel mItemDetailViewModel;
    private LiveData<Brand> mBrandLiveData;

    private ArrayList<RadarEntry> mTasteEntryList;
    private ArrayList<Entry> mAromaEntryList;

    private static String[] mSpecificItems = {"?????????", "????????????", "?????????", "???????????????", "??????????????????", "???????????????", "????????????", "??????????????????"};
    private static String[] mHowToDrinkItems = {"?????????", "?????????", "?????????", "??????", "?????????", "?????????", "?????????", "??????", "??????", "???????????????"};

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mSelectBrandId = args.getString("BRAND_ID");
        } else {
            mSelectBrandId = null;
        }

        binding = FragmentItemDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ((MainActivity)getActivity()).setMenuType(MainActivity.MENU_TYPE.EDIT_TITLE);
        getActivity().getFragmentManager().invalidateOptionsMenu();

        mRadarChartItem = new RadarChartItem(getContext(), binding.radarChart);
        mRadarChartItem.updateRadarChart();
        mLineChartItem = new LineChartItem(binding.lineChart);
        mLineChartItem.updateLineChart();

        /*
          ???????????????????????????
         */
        mTitle = binding.detailTitle;
        mTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // ???????????????????????????
                    mSearchListFragment = new SearchListFragment(SakenowaDataCollector.getInstance().getBrandsList(), mTitle.getText().toString(), true,
                            name -> {
                                getActivity().getSupportFragmentManager().beginTransaction().remove(mSearchListFragment).commit();
                                mTitle.setText(name);
                                // ???????????????????????????
                                InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            });
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_activity_main, mSearchListFragment).commit();
                }
            }
        });

        /**
         * ?????????????????????????????????
         */
        mSubTitle = binding.detailSubtitle;

        /**
         * ???????????????????????????
         */
        mSpecificSpinner = binding.detailSpecificName;
        ArrayAdapter<String> specificAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        specificAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String specificItem : mSpecificItems) specificAdapter.add(specificItem);
        mSpecificSpinner.setAdapter(specificAdapter);

        /**
         * ???????????????????????????
         */
        mPolishingRate = binding.detailPolishingRate;

        /**
         * ????????????????????????
         */
        mBrewery = binding.detailBrewery;
        mBrewery.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // ???????????????????????????
                mSearchListFragment = new SearchListFragment(SakenowaDataCollector.getInstance().getBreweryList(), mBrewery.getText().toString(), true,
                        name -> {
                            getActivity().getSupportFragmentManager().beginTransaction().remove(mSearchListFragment).commit();
                            mBrewery.setText(name);
                            // ???????????????????????????
                            InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        });
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_activity_main, mSearchListFragment).commit();
            }
        });

        /*
          ?????????????????????
         */
        mArea = binding.detailArea;
        mArea.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // ???????????????????????????
                mSearchListFragment = new SearchListFragment(SakenowaDataCollector.getInstance().getAreaList(), mArea.getText().toString(), true,
                        name -> {
                            getActivity().getSupportFragmentManager().beginTransaction().remove(mSearchListFragment).commit();
                            mArea.setText(name);
                            // ???????????????????????????
                            InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        });
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_activity_main, mSearchListFragment).commit();
            }
        });

        /*
          ????????????????????????
         */
        mRawMaterial = binding.detailRawMaterial;

        /*
          ????????????????????????
         */
        mCapacity = binding.detailCapacity;

        /*
          ???????????????????????????
         */
        mStorageTemperature = binding.detailStorageTemperature;

        /*
          ????????????????????????
         */
        mHowToDrinkSpinner = binding.detailHowToDrink;
        ArrayAdapter<String> howToDrinkAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        howToDrinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String howToDrinkItem : mHowToDrinkItems) howToDrinkAdapter.add(howToDrinkItem);
        mHowToDrinkSpinner.setAdapter(howToDrinkAdapter);

        /*
          ?????????????????????
         */
        mTasteEntryList = new ArrayList<>();

        List<Spinner> tastesSpinner = new ArrayList<>();
        tastesSpinner.add(binding.detailSweetness);
        tastesSpinner.add(binding.detailSourness);
        tastesSpinner.add(binding.detailPungent);
        tastesSpinner.add(binding.detailBitterness);
        tastesSpinner.add(binding.detailAstringent);
        for (Spinner tasteSpinner : tastesSpinner) {
            ArrayAdapter<String> tasteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
            tasteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for (Integer i = 1; i <= 5; i++) tasteAdapter.add(i.toString());
            tasteSpinner.setAdapter(tasteAdapter);
        }

        Button registTasteButton = binding.detailTasteRegister;
        registTasteButton.setOnClickListener(v -> {
            mTasteEntryList.clear();
            for (Spinner tasteSpinner : tastesSpinner) {
                float val = Float.parseFloat(tasteSpinner.getSelectedItem().toString()) * 20;
                mTasteEntryList.add(new RadarEntry(val));
            }
            mRadarChartItem.setRadarData(mTasteEntryList);
            mRadarChartItem.updateRadarChart();
        });

        /*
          ?????????????????????
         */
        mAromaEntryList = new ArrayList<>();

        mSdf = new SimpleDateFormat("yyyy/MM/dd");
        mSelectDate = new Date();

        mInputDateTextView = binding.detailInputDate;
        mInputDateTextView.setText(mSdf.format(mSelectDate));
        mInputDateTextView.setOnClickListener(this);

        // ???????????????????????????????????????
        Spinner aromaSpinner = binding.detailAroma;
        ArrayAdapter<String> aromaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        aromaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Integer i = 1; i <= 10; i++) aromaAdapter.add(i.toString());
        aromaSpinner.setAdapter(aromaAdapter);

        Button registAromaButton = binding.detailAromaRegister;
        registAromaButton.setOnClickListener(v -> {
            float x;
            // ?????????????????????????????????????????????
            if (mAromaEntryList.size() > 0) {
                long baseX = ((Date)mAromaEntryList.get(0).getData()).getTime() / (1000 * 60 * 60 * 24);
                long currentX = mSelectDate.getTime() / (1000 * 60 * 60 * 24);
                x = currentX - baseX;
            } else {
                x = 0;
            }
            float y = Float.parseFloat(aromaSpinner.getSelectedItem().toString());
            mAromaEntryList.add(new Entry(x, y, mSelectDate));
            mLineChartItem.setLineData(mAromaEntryList);
            mLineChartItem.updateLineChart();
        });

        /*
          Firestore???????????????
         */
        mItemDetailViewModel = new ViewModelProvider(this).get(ItemDetailViewModel.class);
        mBrandLiveData = mItemDetailViewModel.getBrand(mSelectBrandId);
        mBrandLiveData.observe(getViewLifecycleOwner(), brand -> {
            mTitle.setText(brand.getTitle());
            mSubTitle.setText(brand.getSubtitle());
            for (int i = 0; i < mSpecificItems.length; i++) {
                if (mSpecificItems[i] == brand.getSpecific()) mSpecificSpinner.setSelection(i);
            }
            mPolishingRate.setText(brand.getPolishingRate().toString());
            mBrewery.setText(brand.getBrewery());
            mArea.setText(brand.getArea());
            mRawMaterial.setText(brand.getRawMaterial());
            mCapacity.setText(brand.getCapacity().toString());
            mStorageTemperature.setText(brand.getStorageTemperature().toString());
            for (int i = 0; i < mHowToDrinkItems.length; i++) {
                if (mHowToDrinkItems[i] == brand.getHowToDrink()) mHowToDrinkSpinner.setSelection(i);
            }
            for (Long taste : brand.getTasteList()) {
                mTasteEntryList.add(new RadarEntry(taste));
            }
            mRadarChartItem.setRadarData(mTasteEntryList);
            for (Aroma aroma : brand.getAromaList()) {
                mAromaEntryList.add(new Entry(aroma.getElapsedCount(), aroma.getAromaLevel(), aroma.getElapsedDate()));
            }
            mLineChartItem.setLineData(mAromaEntryList);
        });

        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mSelectDate = new Date(year - 1900, month, dayOfMonth);
        mInputDateTextView.setText(mSdf.format(mSelectDate));
    }

    @Override
    public void onClick(View v) {
        DialogFragment newFragment = new DatePick(this);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    /**
     * ??????????????????Firebase???????????????
     *
     * Toobar???????????????????????????????????????????????????
     */
    public boolean confirmEditData() {
        String title = mTitle.getText().toString();
        // ???????????????????????????????????????
        if (Objects.equals(title, "")) return false;
        String subTitle = mSubTitle.getText().toString();
        String specific = mSpecificSpinner.getSelectedItem().toString();
        Long polishingRate = Long.parseLong(mPolishingRate.getText().toString());
        String brewery = mBrewery.getText().toString();
        String area = mArea.getText().toString();
        String rawMaterial = mRawMaterial.getText().toString();
        Long capacity = Long.parseLong(mCapacity.getText().toString());
        Long storageTemperature = Long.parseLong(mStorageTemperature.getText().toString());
        String howToDrink = mHowToDrinkSpinner.getSelectedItem().toString();
        List<Long> tasteList = new ArrayList<>();
        for (RadarEntry entry : mTasteEntryList) {
            tasteList.add((long) entry.getY());
        }
        List<Aroma> aromaList = new ArrayList<>();
        for (Entry entry : mAromaEntryList) {
            aromaList.add(new Aroma((long)entry.getX(), (long)entry.getY(), (Date)entry.getData()));
        }
        Brand brand = new Brand(title, subTitle, specific, polishingRate, brewery, area, rawMaterial, capacity, storageTemperature, howToDrink, tasteList, aromaList);
        mItemDetailViewModel.updateBrand(mSelectBrandId, brand);
        return true;
    }
}