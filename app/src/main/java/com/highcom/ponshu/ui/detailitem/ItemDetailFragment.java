package com.highcom.ponshu.ui.detailitem;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.highcom.ponshu.MainActivity;
import com.highcom.ponshu.R;
import com.highcom.ponshu.ui.searchlist.SearchListFragment;
import com.highcom.ponshu.util.SakenowaDataCollector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ItemDetailFragment extends Fragment implements DatePickerDialog.OnDateSetListener, SearchListFragment.SearchListFragmentListener, View.OnClickListener {

    private EditText mTitle;
    private SearchListFragment mSearchListFragment;
    private RadarChart mRadarChart;
    private LineChart mLineChart;
    private TextView mInputDateTextView;
    private SimpleDateFormat mSdf;
    private Date mSelectDate;

    private FirebaseFirestore mDb;
    private ArrayList<Entry> mAromaEntryList;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        ((MainActivity)getActivity()).setMenuType(MainActivity.MENU_TYPE.EDIT_TITLE);
        getActivity().getFragmentManager().invalidateOptionsMenu();

        RadarChartInit(view);
        LineChartInit(view);

        mTitle = view.findViewById(R.id.detail_title);
        mSearchListFragment = new SearchListFragment(SakenowaDataCollector.getInstance().getBrandsList(), mTitle.getText().toString(), true, this);
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchListFragment.setTitle(mTitle.getText().toString());
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_activity_main, mSearchListFragment).commit();
            }
        });

        mAromaEntryList = new ArrayList<>();
        mSdf = new SimpleDateFormat("yyyy/MM/dd");
        mSelectDate = new Date();

        mInputDateTextView = view.findViewById(R.id.detail_input_date);
        mInputDateTextView.setText(mSdf.format(mSelectDate));
        mInputDateTextView.setOnClickListener(this);

        mDb = FirebaseFirestore.getInstance();
        DocumentReference documentReference = mDb.collection("AromaProgress").document("aramasa");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    ArrayList<Long> elapsedDayList = (ArrayList) documentSnapshot.get("elapsedDay");
                    ArrayList<Long> aromaList = (ArrayList) documentSnapshot.get("aroma");
                    if (elapsedDayList == null || aromaList == null) return;
                    for (int i = 0; i < elapsedDayList.size() && i < aromaList.size(); i++) {
                        Long x = elapsedDayList.get(i);
                        Long y = aromaList.get(i);
                        mAromaEntryList.add(new Entry((float)x, (float)y, new Date()));
                    }
                    if (mAromaEntryList.size() > 0) {
                        setLineData(mAromaEntryList);
                        LineChartInit(view);
                    }
                }
            }
        });

        Spinner aromaSpinner = view.findViewById(R.id.detail_aroma);
        ArrayAdapter<String> aromaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        aromaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Integer i = 1; i <= 10; i++) aromaAdapter.add(i.toString());
        aromaSpinner.setAdapter(aromaAdapter);

        Button registAromaButton = view.findViewById(R.id.detail_aroma_register);
        registAromaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x;
                if (mAromaEntryList.size() > 0) {
                    long baseX = ((Date)mAromaEntryList.get(0).getData()).getTime() / (1000 * 60 * 60 * 24);
                    long currentX = mSelectDate.getTime() / (1000 * 60 * 60 * 24);
                    x = currentX - baseX;
                } else {
                    x = 0;
                }
                float y = Float.parseFloat(aromaSpinner.getSelectedItem().toString());
                mAromaEntryList.add(new Entry(x, y, mSelectDate));
                Map<String, Object> elapsedDayData = new HashMap<>();
                for (Entry entry : mAromaEntryList) {
                    elapsedDayData.put("elapsedDay", (long) entry.getX());
                }
                documentReference.set(elapsedDayData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Map<String, Object> aromaData = new HashMap<>();
                        for (Entry entry : mAromaEntryList) {
                            elapsedDayData.put("aroma", (long) entry.getY());
                        }
                        Log.d("DATABASE", "documentSet:success");
                        documentReference.set(aromaData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                setLineData(mAromaEntryList);
//                              mLineChart.invalidate();
                                LineChartInit(view);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DATABASE", "documentSet:failure");
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onAdapterClicked(String name) {
        getActivity().getSupportFragmentManager().beginTransaction().remove(mSearchListFragment).commit();
        mTitle.setText(name);
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

    public void confirmEditData() {
        // TODO:Firebaseにデータ登録をする
        mTitle.setText("確定");
    }

    private void RadarChartInit(View view)
    {
        mRadarChart = view.findViewById(R.id.radar_chart);
        mRadarChart.setBackgroundColor(Color.rgb(60, 65, 82));

        mRadarChart.getDescription().setEnabled(false);

        mRadarChart.setWebLineWidth(1f);
        mRadarChart.setWebColor(Color.LTGRAY);
        mRadarChart.setWebLineWidthInner(1f);
        mRadarChart.setWebColorInner(Color.LTGRAY);
        mRadarChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv = new RadarMarkerView(getContext(), R.layout.radar_markerview);
        mv.setChartView(mRadarChart); // For bounds control
        mRadarChart.setMarker(mv); // Set the marker to the mRadarChart

        setRadarData();

        mRadarChart.animateXY(1400, 1400, Easing.EaseInOutQuad);

        XAxis xAxis = mRadarChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            private final String[] mActivities = new String[]{"甘味", "酸味", "辛味", "苦味", "渋味"};

            @Override
            public String getFormattedValue(float value) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = mRadarChart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = mRadarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);

        setRadarData();
    }

    private void LineChartInit(View view)
    {
        mLineChart = view.findViewById(R.id.line_chart);
        mLineChart.setViewPortOffsets(0, 0, 0, 0);
        mLineChart.setBackgroundColor(Color.rgb(104, 241, 175));

        // no description text
        mLineChart.getDescription().setEnabled(false);

        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(false);

        mLineChart.setDrawGridBackground(false);
        mLineChart.setMaxHighlightDistance(300);

        XAxis x = mLineChart.getXAxis();
        x.setEnabled(false);

        YAxis y = mLineChart.getAxisLeft();
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);

        mLineChart.getAxisRight().setEnabled(false);

        mLineChart.getLegend().setEnabled(false);

        mLineChart.animateXY(2000, 2000);

        // don't forget to refresh the drawing
        mLineChart.invalidate();

//        setLineData();
    }

    private void setRadarData() {

        float mul = 80;
        float min = 20;
        int cnt = 5;

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mul) + min;
            entries1.add(new RadarEntry(val1));

            float val2 = (float) (Math.random() * mul) + min;
            entries2.add(new RadarEntry(val2));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "香り");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
//            data.setValueTypeface(tfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mRadarChart.setData(data);
        mRadarChart.invalidate();
    }

    private void setLineData(ArrayList<Entry> values) {

//        ArrayList<Entry> values = new ArrayList<>();

//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * (range + 1)) + 20;
//            values.add(new Entry(i, val));
//        }
//        values.add(new Entry(0, 10));
//        values.add(new Entry(1, 6));
//        values.add(new Entry(2, 4));
//        values.add(new Entry(3, 2));
//        values.add(new Entry(4, 1));

        LineDataSet set1;

        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.WHITE);
            set1.setFillColor(Color.WHITE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return mLineChart.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            mLineChart.setData(data);
        }
    }
}