package com.me.shots.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.me.shots.R;
import com.me.shots.Utils.ModulesActivity;

import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by HP-USER on 11-12-2017.
 */

public class ModuleAdapter extends BaseAdapter {
    int modulecount=0;
    String[] modulenames={"machine","machine","machine","machine","machine"};
    Context context=null;
    public ModuleAdapter(int modulecount, String[] modulenames, Context context){
        this.modulecount=modulecount;
        this.modulenames=modulenames;
        Log.e("mytag", "ModuleAdapter: "+modulecount );
        this.context=context;
    }

    @Override
    public int getCount() {
        int count=2*(modulecount/3)+(modulecount%3);
        Log.e("mytag", "getCount: "+count );
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view= LayoutInflater.from(context).inflate(R.layout.even_module_views,parent,false);


        WaveLoadingView leftmWaveLoadingView = (WaveLoadingView) view.findViewById(R.id.leftWaveLoadingView);
        WaveLoadingView centermWaveLoadingView = (WaveLoadingView) view.findViewById(R.id.centerWaveLoadingView);
        WaveLoadingView rightmWaveLoadingView = (WaveLoadingView) view.findViewById(R.id.rightWaveLoadingView);

        waveviews(leftmWaveLoadingView);
        waveviews(centermWaveLoadingView);
        waveviews(rightmWaveLoadingView);
        Log.e("mytag", "position: "+position );

        if(position%2==0)
        {
            int moduleIndex=getEvenViews(position);

            String name[]=modulenames[moduleIndex].split(".pptx");
            Log.e("mytag", "getView: "+moduleIndex+"==========="+modulenames[moduleIndex] );

            centermWaveLoadingView.setCenterTitle(name[0]);
            centermWaveLoadingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"center",Toast.LENGTH_SHORT).show();
                }
            });
            RelativeLayout left= (RelativeLayout) view.findViewById(R.id.left_layout);
            left.setVisibility(View.INVISIBLE);

            RelativeLayout right= (RelativeLayout) view.findViewById(R.id.right_layout);
            right.setVisibility(View.INVISIBLE);
        }
        else {
            int moduleIndex=getEvenViews(position);
            if(moduleIndex+2==modulecount){
                String name[]=modulenames[moduleIndex+1].split(".pptx");
                Log.e("mytag", "getView: "+moduleIndex+"==========="+modulenames[moduleIndex] );

                centermWaveLoadingView.setCenterTitle(name[0]);
                centermWaveLoadingView.setWaveColor(Color.parseColor("#D47FFF"));
                centermWaveLoadingView.setBorderColor(Color.parseColor("#D47FFF"));
                centermWaveLoadingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"center",Toast.LENGTH_SHORT).show();
                    }
                });
                RelativeLayout left= (RelativeLayout) view.findViewById(R.id.left_layout);
                left.setVisibility(View.INVISIBLE);

                RelativeLayout right= (RelativeLayout) view.findViewById(R.id.right_layout);
                right.setVisibility(View.INVISIBLE);

            }
            else
            {
                RelativeLayout center= (RelativeLayout) view.findViewById(R.id.center_layout);
                center.setVisibility(View.INVISIBLE);


                leftmWaveLoadingView.setWaveColor(Color.parseColor("#AAFF552A"));
                leftmWaveLoadingView.setBorderColor(Color.parseColor("#AAFF551A"));
                leftmWaveLoadingView.setProgressValue(60);
                String name[]=modulenames[moduleIndex+1].split(".pptx");
                leftmWaveLoadingView.setCenterTitle(name[0]);
                String name1[]=modulenames[moduleIndex+2].split(".pptx");
                leftmWaveLoadingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"Left",Toast.LENGTH_SHORT).show();

                    }
                });

                rightmWaveLoadingView.setWaveColor(Color.parseColor("#55FF2A"));
                rightmWaveLoadingView.setBorderColor(Color.parseColor("#55FF1A"));
                rightmWaveLoadingView.setProgressValue(60);
                rightmWaveLoadingView.setCenterTitle(name1[0]);
                rightmWaveLoadingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"Right",Toast.LENGTH_SHORT).show();

                    }
                });
            }


        }


        return view;
    }


    private void waveviews(WaveLoadingView mWaveLoadingView){
        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        //mWaveLoadingView.setTopTitle("Top Title");
        mWaveLoadingView.setCenterTitleColor(Color.GRAY);
        mWaveLoadingView.setBottomTitleSize(18);
        mWaveLoadingView.setProgressValue(60);
        mWaveLoadingView.setBorderWidth(7);
        mWaveLoadingView.setAmplitudeRatio(60);
        mWaveLoadingView.setWaveColor(Color.parseColor("#2AFFD4"));
        mWaveLoadingView.setBorderColor(Color.parseColor("#2BFFD4"));
        mWaveLoadingView.setTopTitleStrokeColor(Color.BLUE);
        mWaveLoadingView.setTopTitleStrokeWidth(2);
        mWaveLoadingView.setAnimDuration(2000);
        // mWaveLoadingView.pauseAnimation();
        // mWaveLoadingView.resumeAnimation();
        //   mWaveLoadingView.cancelAnimation();
        mWaveLoadingView.startAnimation();

    }

    private int getEvenViews(int mcount){
        return (mcount/2)*3;
    }

}
