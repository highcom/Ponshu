package com.highcom.ponshu.util;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.highcom.ponshu.R;
import com.highcom.ponshu.ui.detailitem.RadarMarkerView;

import java.util.ArrayList;
import java.util.List;

public class RadarChartItem {
    private Context mContext;
    private RadarChart mRadarChart;

    public RadarChartItem(Context context, RadarChart radarChart) {
        mContext = context;
        mRadarChart = radarChart;
    }

    /**
     * レーダーチャート画面更新処理
     */
    public void updateRadarChart()
    {
        mRadarChart.setBackgroundColor(Color.rgb(60, 65, 82));

        mRadarChart.getDescription().setEnabled(false);

        mRadarChart.setWebLineWidth(1f);
        mRadarChart.setWebColor(Color.LTGRAY);
        mRadarChart.setWebLineWidthInner(1f);
        mRadarChart.setWebColorInner(Color.LTGRAY);
        mRadarChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv = new RadarMarkerView(mContext, R.layout.radar_markerview);
        mv.setChartView(mRadarChart); // For bounds control
        mRadarChart.setMarker(mv); // Set the marker to the mRadarChart

//        setRadarData();

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

//        setRadarData();
    }

    /**
     * レーターチャートデータ設定処理
     */
    public void setRadarData(List<RadarEntry> values) {

//        float mul = 80;
//        float min = 20;
//        int cnt = 5;
//
//        ArrayList<RadarEntry> entries1 = new ArrayList<>();
//        ArrayList<RadarEntry> entries2 = new ArrayList<>();
//
//        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
//        // the chart.
//        for (int i = 0; i < cnt; i++) {
//            float val1 = (float) (Math.random() * mul) + min;
//            entries1.add(new RadarEntry(val1));
//
//            float val2 = (float) (Math.random() * mul) + min;
//            entries2.add(new RadarEntry(val2));
//        }

        RadarDataSet set1 = new RadarDataSet(values, "香り");
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
}
