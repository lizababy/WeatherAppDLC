package liza.weatherappdlc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import liza.weatherappdlc.Models.WeatherListItem;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<WeatherListItem> temperatureList;

    public CustomAdapter(@NonNull Context context, @NonNull ArrayList<WeatherListItem> objects) {
        mContext = context;
        temperatureList = objects;
    }

    public void setData(ArrayList<WeatherListItem> list) {
        temperatureList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView textViewDateLine1;
        private final TextView textViewDateLine2;
        private final TextView textViewLowest;
        private final TextView textViewHighest;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewDateLine1 = itemView.findViewById(R.id.textViewDateLine1);
            textViewDateLine2 = itemView.findViewById(R.id.textViewDateLine2);
            textViewLowest =  itemView.findViewById(R.id.textViewLowest);
            textViewHighest = itemView.findViewById(R.id.textViewHighest);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WeatherListItem item = temperatureList.get(position);
        holder.textViewDateLine1.setText(getDateString(item.getDtTxt(),"EEE, MMM dd"));

        holder.textViewDateLine2.setText(getDateString(item.getDtTxt(),"hh a"));
        StringBuilder lowestTemp = new StringBuilder(Html.fromHtml(mContext.getString(R.string.down_arrow)));
        lowestTemp.append(item.getWeatherMain().getTempMin().toString());
        lowestTemp.append(mContext.getString(R.string.faren_unit));

        StringBuilder highestTemp = new StringBuilder(Html.fromHtml(mContext.getString(R.string.up_arrow)));
        highestTemp.append(item.getWeatherMain().getTempMax().toString());
        highestTemp.append(mContext.getString(R.string.faren_unit));

        holder.textViewLowest.setText(lowestTemp);
        holder.textViewHighest.setText(highestTemp);
        holder.imageView.setContentDescription(item.getWeatherSub().get(0).getDescription());

        Picasso.get().load(getImageString(item.getWeatherSub().get(0).getIcon())).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return temperatureList.size();
    }
    private String getDateString(String dateText, String format) {
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
    private String getImageString(String icon) {
        return mContext.getString(R.string.IMAGE_URL_PATH)+icon+".png";
    }


}
