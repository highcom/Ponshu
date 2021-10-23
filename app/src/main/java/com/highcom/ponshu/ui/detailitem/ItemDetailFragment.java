package com.highcom.ponshu.ui.detailitem;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ItemDetailFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private FragmentItemDetailBinding binding;
    private String mSelectBrandId;
    private EditText mTitle;
    private EditText mSubTitle;
    private EditText mPolishingRate;
    private EditText mBrewery;

    private SearchListFragment mSearchListFragment;
    private RadarChartItem mRadarChartItem;
    private LineChartItem mLineChartItem;
    private TextView mInputDateTextView;
    private SimpleDateFormat mSdf;
    private Date mSelectDate;

    private ItemDetailViewModel mItemDetailViewModel;
    private LiveData<Brand> mBrandLiveData;

    private ArrayList<Entry> mAromaEntryList;

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
          タイトルデータ設定
         */
        mTitle = binding.detailTitle;
        mTitle.setOnClickListener(v -> {
            // 銘柄一覧を表示する
            mSearchListFragment = new SearchListFragment(SakenowaDataCollector.getInstance().getBrandsList(), mTitle.getText().toString(), true,
                    name -> {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(mSearchListFragment).commit();
                        mTitle.setText(name);
                    });
            mSearchListFragment.setTitle(mTitle.getText().toString());
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_activity_main, mSearchListFragment).commit();
        });

        /**
         * サブタイトルデータ設定
         */
        mSubTitle = binding.detailSubtitle;

        /**
         * 特定名称データ設定
         */
        Spinner specificSpinner = binding.detailSpecificName;
        ArrayAdapter<String> specificAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        specificAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String[] specificItems = {"吟醸酒", "大吟醸酒", "純米酒", "純米吟醸酒", "純米大吟醸酒", "特別純米酒", "本醸造酒", "特別本醸造酒"};
        for (String specificItem : specificItems) specificAdapter.add(specificItem);
        specificSpinner.setAdapter(specificAdapter);

        /**
         * 精米歩合データ設定
         */
        mPolishingRate = binding.detailPolishingRate;

        /**
         * 酒造名データ設定
         */
        mBrewery = binding.detailBrewery;
        mBrewery.setOnClickListener(v -> {
            // 酒蔵一覧を表示する
            mSearchListFragment = new SearchListFragment(SakenowaDataCollector.getInstance().getmBreweryList(), mBrewery.getText().toString(), true,
                    name -> {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(mSearchListFragment).commit();
                        mBrewery.setText(name);
                    });
            mSearchListFragment.setTitle(mTitle.getText().toString());
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_activity_main, mSearchListFragment).commit();

        });

        /*
          香りデータ設定
         */
        mAromaEntryList = new ArrayList<>();

        mSdf = new SimpleDateFormat("yyyy/MM/dd");
        mSelectDate = new Date();

        mInputDateTextView = binding.detailInputDate;
        mInputDateTextView.setText(mSdf.format(mSelectDate));
        mInputDateTextView.setOnClickListener(this);

        // 香りレベルスピナー設定処理
        Spinner aromaSpinner = binding.detailAroma;
        ArrayAdapter<String> aromaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        aromaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Integer i = 1; i <= 10; i++) aromaAdapter.add(i.toString());
        aromaSpinner.setAdapter(aromaAdapter);

        Button registAromaButton = binding.detailAromaRegister;
        registAromaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x;
                // 登録初日からの経過日数を求める
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
            }
        });

        /*
          Firestoreデータ取得
         */
        mItemDetailViewModel = new ViewModelProvider(this).get(ItemDetailViewModel.class);
        mBrandLiveData = mItemDetailViewModel.getBrand(mSelectBrandId);
        mBrandLiveData.observe(getViewLifecycleOwner(), brand -> {
            mTitle.setText(brand.getTitle());
            mSubTitle.setText(brand.getSubtitle());
            mPolishingRate.setText(brand.getPolishingRate().toString());
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
     * 入力データをFirebaseへ登録処理
     *
     * Toobarのチェックボタンを押下時に呼ばれる
     */
    public void confirmEditData() {
        String title = mTitle.getText().toString();
        String subTitle = mSubTitle.getText().toString();
        Long polishingRate = Long.parseLong(mPolishingRate.getText().toString());
        List<Aroma> aromaList = new ArrayList<>();
        for (Entry entry : mAromaEntryList) {
            aromaList.add(new Aroma((long)entry.getX(), (long)entry.getY(), (Date)entry.getData()));
        }
        Brand brand = new Brand(title, subTitle, polishingRate, aromaList);
        mItemDetailViewModel.updateBrand(mSelectBrandId, brand);
    }
}