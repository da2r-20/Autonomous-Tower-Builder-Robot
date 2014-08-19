package com.example.blob_detect_test;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Adapter {
private SeekBarListener mListener;
private TextView sliderValues[] = new TextView[6];

public interface SeekBarListener{
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser, int positionInList);
    public void onStartTrackingTouch(SeekBar seekBar, int positionInList);
    public void onStopTrackingTouch(SeekBar seekBar, int positionInList);
}



public listAdapter getAdapter(Context context, ArrayList<String> list, String title){
    return new listAdapter(context, list, title);
}

public void setSeekBarListener(SeekBarListener listener){
    mListener = listener;
}

public class listAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private onSeekbarChange mSeekListener;
    private ArrayList<String> itemsList;
    private String title;

    public listAdapter(Context context, ArrayList<String> list, String title){
        mInflater = LayoutInflater.from(context);
        if(mSeekListener == null){
            mSeekListener = new onSeekbarChange();
        }
        this.itemsList = list;
        this.title = title;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder2 holder;

        if(convertView == null){
            holder = new ViewHolder2();
            convertView = mInflater.inflate(R.layout.baseadapter_layout, null);
            holder.text_title = (TextView)convertView.findViewById(R.id.textView);
            convertView.setTag(R.layout.baseadapter_layout, holder);
        } else {
            holder = (ViewHolder2)convertView.getTag(R.layout.baseadapter_layout);
        }
        holder.text_title.setText(title);
        return convertView;
    }


    @Override 
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.baseadapter_dropdown_layout, null);
            holder.text = (TextView)convertView.findViewById(R.id.textView1);
            holder.text_slider_value = (TextView)convertView.findViewById(R.id.textView2);
            holder.seekbar = (SeekBar)convertView.findViewById(R.id.seekBar1);
            if (position > 2){
            	holder.seekbar.setProgress(255);
            }
            convertView.setTag(R.layout.baseadapter_dropdown_layout, holder);
        } else {
            holder = (ViewHolder)convertView.getTag(R.layout.baseadapter_dropdown_layout);
        }
        holder.text.setText(itemsList.get(position));
        holder.seekbar.setOnSeekBarChangeListener(mSeekListener);
        holder.seekbar.setTag(position);
        sliderValues[position] = (TextView)convertView.findViewById(R.id.textView2);
        return convertView;

    }

}

static class ViewHolder {
    TextView text;
    TextView text_slider_value;
    SeekBar seekbar;
}

static class ViewHolder2 {
    TextView text_title;
}


public class onSeekbarChange implements OnSeekBarChangeListener{

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int position = (Integer) seekBar.getTag();
        if(mListener != null){
        	sliderValues[position].setText(Integer.toString(progress));
            mListener.onProgressChanged(seekBar, progress, fromUser, position);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        int position = (Integer) seekBar.getTag();
        if(mListener != null){
            mListener.onStartTrackingTouch(seekBar, position);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int position = (Integer) seekBar.getTag();
        if(mListener != null){
            mListener.onStopTrackingTouch(seekBar, position);
        }
    }

}
}