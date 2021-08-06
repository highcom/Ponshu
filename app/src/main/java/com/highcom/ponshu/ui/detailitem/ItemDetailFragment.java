package com.highcom.ponshu.ui.detailitem;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import com.highcom.ponshu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemDetailFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private RadarChart mRadarChart;
    private LineChart mLineChart;
    private TextView mInputDateTextView;
    private SimpleDateFormat mSdf;
    private Date mSelectDate;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        RadarChartInit(view);
        LineChartInit(view);

        mSdf = new SimpleDateFormat("yyyy/MM/dd");
        mSelectDate = new Date();
        mInputDateTextView = view.findViewById(R.id.detail_input_date);
        mInputDateTextView.setText(mSdf.format(mSelectDate));
        mInputDateTextView.setOnClickListener(this);

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

        setLineData(null);
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

    private void setLineData(ArrayList<Entry> values_) {

        ArrayList<Entry> values = new ArrayList<>();

//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * (range + 1)) + 20;
//            values.add(new Entry(i, val));
//        }
        values.add(new Entry(0, 10));
        values.add(new Entry(1, 6));
        values.add(new Entry(2, 4));
        values.add(new Entry(3, 2));
        values.add(new Entry(4, 1));

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