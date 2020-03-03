package com.softsquared.android.corona.src.main.info;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.tabs.TabLayout;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.info.interfaces.InfoFragmentView;
import com.softsquared.android.corona.src.main.info.models.InfectedData;
import com.softsquared.android.corona.src.main.info.models.Info;

import java.util.ArrayList;
import java.util.List;


public class InfoFragment extends BaseFragment implements InfoFragmentView {

    //    ViewPager mViewPager;
//    NewsAdapter newsAdapter;
    TabLayout mTabLayout;

    private TextView mTextViewInfected, mTextViewDead, mTextViewCured, mTextViewSymptom,
            mTextViewInspection, mTextViewIsolation, mTextViewInfectedDiff, mTextViewDeadDiff,
            mTextViewCuredDiff, mTextViewSymptomDiff, mTextViewInspectionDiff,
            mTextViewIsolationDiff, mTextViewUpdate;


    //    ArrayList<CaringInfo> mArrayListCaringInfos = new ArrayList<>();
    private CombinedChart mChart;
    private ArrayList<InfectedData> mInfectedDataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
        setComponentView(view);
        getInfo();
        getGraph();
        return view;
    }

    @Override
    public void setComponentView(View v) {
        mTabLayout = v.findViewById(R.id.home_tab_indicator);
//        mArrayListCaringInfos.clear();
//        mArrayListCaringInfos.add(new CaringInfo(R.drawable.banner_invite, "", 1));
//        mArrayListCaringInfos.add(new CaringInfo(R.drawable.banner_tip, "", 2));
//        mArrayListCaringInfos.add(new CaringInfo(R.drawable.img_mask, "KF94마스크", 3));
//        mArrayListCaringInfos.add(new CaringInfo(R.drawable.img_hand, "손세정제", 3));
//
//        newsAdapter = new NewsAdapter(getChildFragmentManager(), mArrayListCaringInfos);
//
//        mViewPager = v.findViewById(R.id.fragment_info_vp);
//       뷰페이저 미리보기 설정//
//        mViewPager.setClipToPadding(false);
//        int dpValue = 25;
//        float d = getResources().getDisplayMetrics().density;
//        int margin = (int) (dpValue * d);
//        mViewPager.setPadding(margin/2, 0, margin, 0);
//        mViewPager.setPageMargin(margin/3);
//        mViewPager.setAdapter(newsAdapter);
//        mTabLayout.setupWithViewPager(mViewPager);

        mTextViewCured = v.findViewById(R.id.info_cure);
        mTextViewCuredDiff = v.findViewById(R.id.info_cure_plus);
        mTextViewDead = v.findViewById(R.id.info_death);
        mTextViewDeadDiff = v.findViewById(R.id.info_death_plus);
        mTextViewInfected = v.findViewById(R.id.info_infected);
        mTextViewInfectedDiff = v.findViewById(R.id.info_infected_plus);
        mTextViewInspection = v.findViewById(R.id.info_inspection);
        mTextViewInspectionDiff = v.findViewById(R.id.info_inspection_plus);
        mTextViewIsolation = v.findViewById(R.id.info_isolation);
        mTextViewIsolationDiff = v.findViewById(R.id.info_isolation_plus);
        mTextViewSymptom = v.findViewById(R.id.info_symptom);
        mTextViewSymptomDiff = v.findViewById(R.id.info_symptom_plus);
        mTextViewUpdate = v.findViewById(R.id.info_update);
        mChart = v.findViewById(R.id.fragment_info_chart);

//        addDummyData();
    }

    void addDummyData(){
        mInfectedDataList = new ArrayList<>();
        mInfectedDataList.add(new InfectedData(10, 10, "1/1"));
        mInfectedDataList.add(new InfectedData(3, 13, "1/2"));
        mInfectedDataList.add(new InfectedData(20, 33, "1/3"));
        mInfectedDataList.add(new InfectedData(40, 73, "1/4"));
        mInfectedDataList.add(new InfectedData(100, 173, "1/5"));
        mInfectedDataList.add(new InfectedData(1, 174, "1/6"));
        mInfectedDataList.add(new InfectedData(13, 187, "1/7"));
        mInfectedDataList.add(new InfectedData(30, 217, "1/8"));
        mInfectedDataList.add(new InfectedData(45, 262, "1/9"));
        mInfectedDataList.add(new InfectedData(0, 262, "1/10"));

        mInfectedDataList.add(new InfectedData(10, 272, "1/11"));
        mInfectedDataList.add(new InfectedData(3, 275, "1/12"));
        mInfectedDataList.add(new InfectedData(20, 295, "1/13"));
        mInfectedDataList.add(new InfectedData(40, 335, "1/14"));
        mInfectedDataList.add(new InfectedData(100, 435, "1/15"));
        mInfectedDataList.add(new InfectedData(1, 436, "1/16"));
        mInfectedDataList.add(new InfectedData(13, 449, "1/17"));
        mInfectedDataList.add(new InfectedData(30, 479, "1/18"));
        mInfectedDataList.add(new InfectedData(45, 534, "1/19"));
        mInfectedDataList.add(new InfectedData(0, 534, "1/21"));


        mInfectedDataList.add(new InfectedData(10, 272, "1/22"));
        mInfectedDataList.add(new InfectedData(3, 275, "1/23"));
        mInfectedDataList.add(new InfectedData(20, 295, "1/24"));
        mInfectedDataList.add(new InfectedData(40, 335, "1/25"));
        mInfectedDataList.add(new InfectedData(100, 435, "1/26"));
        mInfectedDataList.add(new InfectedData(1, 436, "1/27"));
        mInfectedDataList.add(new InfectedData(13, 449, "1/28"));
        mInfectedDataList.add(new InfectedData(30, 479, "1/29"));
        mInfectedDataList.add(new InfectedData(45, 534, "1/30"));
        mInfectedDataList.add(new InfectedData(0, 534, "1/31"));
    }

    void setChart(){
        ArrayList<String> dateArrayList = new ArrayList<>();
        ArrayList<Integer> dayInfectedList = new ArrayList<>();
        ArrayList<Integer> totalInfectedList = new ArrayList<>();

//        dateArrayList.add("");

        for(int i = 0; i < mInfectedDataList.size(); i++) {
            String date = mInfectedDataList.get(i).getDate();
//            date = date.replace('-', '/');
            dateArrayList.add(date);
            dayInfectedList.add(mInfectedDataList.get(i).getDayInfected());
            totalInfectedList.add(mInfectedDataList.get(i).getTotalInfected());
        }
//        dateArrayList.add("");

        CombinedData combinedData = new CombinedData();
        //Trust TP Data와 Invite TP Data그리고 두개의 합인 Data를 각각 설정하고 mChart에 데이터를 설정해줍니다.
        combinedData.setData(setDayInfectedData(dayInfectedList));
        combinedData.setData(setEarnedData(totalInfectedList));
        mChart.setData(combinedData);

        String[] dateList = dateArrayList.toArray(new String[dateArrayList.size()]);

        //Chart의 속성을 설정해줍니다.
        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMinimum(0);
//        xAxis.setAxisMaximum(50);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisLineColor(R.color.colorPrimary);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceMax(0.95f);
//        xAxis.setValueFormatter(new DateAxisFormatter(dateList));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));
        xAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
        xAxis.setLabelCount(7, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌


        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);

        YAxis yLAxis = mChart.getAxisLeft();
        yLAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
//        yLAxis.setAxisLineColor(Color.TRANSPARENT);
        yLAxis.setGranularity(100f);
        yLAxis.setDrawGridLines(true);
//        yLAxis.setGridColor(Color.WHITE);

        YAxis yRAxis = mChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);


        mChart.setExtraOffsets(10f, 10f, 10f, 10f);
//        mChart.setVisibleXRangeMaximum(20f);
        mChart.setDoubleTapToZoomEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setDescription(null);
        mChart.invalidate();
    }

    private BarData setDayInfectedData(ArrayList<Integer> infectedList){
        List<BarEntry> entries = new ArrayList<>();

        //x: 1부터 / float[invite , trust]
        //Trust TP Data와 Invite TP Data를 같이 BarData로 만듭니다.
        for(int i = 0; i < infectedList.size(); i++)
            entries.add(new BarEntry(i, new float[]{infectedList.get(i)}));

        int[] colors = {getResources().getColor(R.color.colorPrimary)};
        String[] labels = {"일일 확진자"};

        BarDataSet barDataSet = new BarDataSet(entries, "일일 확진자");
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextSize(10f);
        barDataSet.setValueTextColor(getResources().getColor(R.color.colorPrimary));
        barDataSet.setDrawIcons(false);
        barDataSet.setColors(colors);
        barDataSet.setStackLabels(labels);

        BarData data = new BarData(barDataSet);

        data.setBarWidth(0.4f);

        return data;
    }

    //합 데이터는 LineData로 만듭니다.
    private LineData setEarnedData(ArrayList<Integer> earnedTpList){
        List<Entry> entries= new ArrayList<>();

        for(int i = 0; i < earnedTpList.size(); i++)
            entries.add(new Entry(i, earnedTpList.get(i)));

        LineDataSet lineDataSet = new LineDataSet(entries, "총 확진자");
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(true);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(getResources().getColor(R.color.colorOrange));
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(getResources().getColor(R.color.colorOrange));
        lineDataSet.setCircleColor(getResources().getColor(R.color.colorOrange));
        lineDataSet.setCircleRadius(3);
        lineDataSet.setCircleHoleColor(getResources().getColor(R.color.colorOrange));
        lineDataSet.setCircleHoleRadius(1);
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);

        LineData data = new LineData(lineDataSet);

        return data;
    }


    void getInfo() {
        showProgressDialog(getActivity());
        final InfoFragmentService infoFragmentService = new InfoFragmentService(this);
        infoFragmentService.getStatistics();
    }

    void getGraph() {
        showProgressDialog(getActivity());
        final InfoFragmentService infoFragmentService = new InfoFragmentService(this);
        infoFragmentService.getGraph();
    }

    @Override
    public void validateSuccess(Info info) {
        hideProgressDialog();
        mTextViewUpdate.setText(info.getUpdatedTime());
        mTextViewCured.setText(info.getCured() + "");
        if (info.getCuredDiff() > 0) {
            mTextViewCuredDiff.setText("( +" + info.getCuredDiff() + " )");
        } else {
            mTextViewCuredDiff.setText("( " + info.getCuredDiff() + " )");
        }
        mTextViewDead.setText(info.getDead() + "");
        if (info.getDeadDiff() > 0) {
            mTextViewDeadDiff.setText("( +" + info.getDeadDiff() + " )");
        } else {
            mTextViewDeadDiff.setText("( " + info.getDeadDiff() + " )");
        }
        mTextViewInfected.setText(info.getInfected() + "");
        if (info.getInfectedDiff() > 0) {
            mTextViewInfectedDiff.setText("( +" + info.getInfectedDiff() + " )");
        } else {
            mTextViewInfectedDiff.setText("( " + info.getInfectedDiff() + " )");
        }
        mTextViewInspection.setText(info.getInspection() + "");
        if (info.getInspectionDiff() > 0) {
            mTextViewInspectionDiff.setText("( +" + info.getInspectionDiff() + " )");
        } else {
            mTextViewInspectionDiff.setText("( " + info.getInspectionDiff() + " )");
        }
        mTextViewIsolation.setText(info.getIsolation() + "");
        if (info.getIsolationDiff() > 0) {
            mTextViewIsolationDiff.setText("( +" + info.getIsolationDiff() + " )");
        } else {
            mTextViewIsolationDiff.setText("( " + info.getIsolationDiff() + " )");
        }
        mTextViewSymptom.setText(info.getSymptom() + "");
        if (info.getSymptomDiff() > 0) {
            mTextViewSymptomDiff.setText("( +" + info.getSymptomDiff() + " )");
        } else {
            mTextViewSymptomDiff.setText("( " + info.getSymptomDiff() + " )");
        }
    }

    @Override
    public void getGraphSuccess(ArrayList<InfectedData> info) {
        hideProgressDialog();
        mInfectedDataList = new ArrayList<>();
        mInfectedDataList.clear();
        mInfectedDataList.addAll(info);
        setChart();

    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        showCustomToast(message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }

    @Override
    public void onDestroy() {
        hideProgressDialog();
        super.onDestroy();
    }
}
