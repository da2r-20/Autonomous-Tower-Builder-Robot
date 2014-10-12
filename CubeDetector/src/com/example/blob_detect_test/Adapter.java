package com.example.blob_detect_test;

import ioio.lib.api.exception.ConnectionLostException;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


/**
 * An Adapter object acts as a bridge between an AdapterView and the underlying data
 * for that view (the dropped down list in our case) . The Adapter provides access to the data items. 
 * The Adapter is also responsible for making a View for each item in the data set.
 */
public class Adapter {
//listener that receives events when one of seekbars change by a user.
private SeekBarListener mListener;

//The HSV values of the sliders of the action bar dropdown list
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

/**
 * The list adapter class is responsible for managing the dropdown view of the action bar.
 * It holds the six HSV seekbars (sliders).
 */
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

    /**
	 * this method returns the view of the action bar layout (when it is not "dropped down")
	 * @param position
	 * @param convertView 
	 * @param parent
	 */
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


    /**
	 * this method returns the view of the action bar layout when it is "dropped down"
	 * @param position
	 * @param convertView 
	 * @param parent
	 */
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.baseadapter_dropdown_layout, null);
            holder.text = (TextView)convertView.findViewById(R.id.textView1);
            holder.text_slider_value = (TextView)convertView.findViewById(R.id.textView2);
            holder.seekbar = (SeekBar)convertView.findViewById(R.id.seekBar1);
            if (position > 2){
            	//init the seekbar position
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

//ViewHolder (holds elements of view) of dropdown list
static class ViewHolder {
    TextView text;
    TextView text_slider_value;
    SeekBar seekbar;
}

//ViewHolder of actionbar (when it is not in dropdown mode)
static class ViewHolder2 {
    TextView text_title;
}

/**
 * This class is responsible for managing the update (by user touch) of the seekbars in the action bar dropdown list.
 */
public class onSeekbarChange implements OnSeekBarChangeListener{

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int position = (Integer) seekBar.getTag();
        if(mListener != null){
        	//update the textual value of the slider
        	sliderValues[position].setText(Integer.toString(progress));
        	//update the mListener, which will update the view and allow access to the HSV values for the Image Recognition Algo
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