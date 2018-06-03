package liza.weatherappdlc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import liza.weatherappdlc.Models.WeatherListItem;
import liza.weatherappdlc.R;

public class CustomAdapter extends ArrayAdapter<WeatherListItem> {


    private final LayoutInflater mInflater;
    private final Context mContext;
    ArrayList<WeatherListItem> weatherForecast;


    public CustomAdapter(@NonNull Context context, @NonNull ArrayList<WeatherListItem> objects) {
        super(context, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        weatherForecast = objects;
    }

    @Nullable
    @Override
    public WeatherListItem getItem(int position) {
        return weatherForecast.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            View view = mInflater.inflate(R.layout.list_item_layout,parent,false);
            viewHolder = ViewHolder.create((LinearLayout) view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }

        WeatherListItem item = weatherForecast.get(position);
        viewHolder.textViewDateLine1.setText(getDateString(item.getDtTxt(),"EEE, MMM dd"));
        viewHolder.textViewDateLine2.setText(getDateString(item.getDtTxt(),"hh a"));

        viewHolder.textViewLowest.setText(Html.fromHtml("&darr;") + item.getWeatherMain().getTempMin().toString() + mContext.getString(R.string.faren_unit));
        viewHolder.textViewHighest.setText(Html.fromHtml("&uarr;") +item.getWeatherMain().getTempMax().toString() + mContext.getString(R.string.faren_unit));
        viewHolder.imageView.setContentDescription(item.getWeatherSub().get(0).getDescription());

        Picasso.get().load(getImage(item.getWeatherSub().get(0).getIcon())).into(viewHolder.imageView);

        return viewHolder.rootView;

    }
    public String getDateString(String dateText, String format) {
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat(format,Locale.ENGLISH);
        Date date = null;
        try {
            date = originalFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return targetFormat.format(date);
    }
    private String getImage(String icon) {
        return "http://openweathermap.org/img/w/"+icon+".png";
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final ImageView imageView;
        public final TextView textViewDateLine1;
        public final TextView textViewDateLine2;
        public final TextView textViewLowest;
        public final TextView textViewHighest;

        private ViewHolder(LinearLayout rootView, ImageView imageView, TextView textViewDate1,TextView textViewDate2,TextView textViewLowest, TextView textViewHighest) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewLowest = textViewLowest;
            this.textViewHighest = textViewHighest;
            this.textViewDateLine1 = textViewDate1;
            this.textViewDateLine2 = textViewDate2;
        }

        public static ViewHolder create(LinearLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewDateLine1 = (TextView) rootView.findViewById(R.id.textViewDateLine1);
            TextView textViewDateLine2 = (TextView) rootView.findViewById(R.id.textViewDateLine2);
            TextView textViewLowest = (TextView) rootView.findViewById(R.id.textViewLowest);
            TextView textViewHighest = (TextView) rootView.findViewById(R.id.textViewHighest);
            return new ViewHolder(rootView, imageView, textViewDateLine1,textViewDateLine2, textViewLowest,textViewHighest);
        }
    }
}
