package com.example.finalhomework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class AlertDialogImageAdapter  extends BaseAdapter {
    private LayoutInflater layoutInflater;

    AlertDialogImageAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return iconList.length;
    }

    @Override
    public Object getItem(int position) {
        return iconList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("InflateParams")
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AlertDialogViewHolder alertDialogViewHolder;

        if (convertView == null) {
            // This is an alertDialog, therefore it has no root
            convertView = layoutInflater.inflate(R.layout.icon, null);

            DisplayMetrics metrics = convertView.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;

            convertView.setLayoutParams(new GridView.LayoutParams(screenWidth / 6, screenWidth / 6));
            alertDialogViewHolder = new AlertDialogViewHolder();
            alertDialogViewHolder.icon = convertView.findViewById(R.id.image_choose_icon_entry);
            alertDialogViewHolder.iconName = convertView.findViewById(R.id.icon_name);
            convertView.setTag(alertDialogViewHolder);
        } else {
            alertDialogViewHolder = (AlertDialogViewHolder) convertView.getTag();
        }

        alertDialogViewHolder.icon.setAdjustViewBounds(true);
        alertDialogViewHolder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        alertDialogViewHolder.icon.setPadding(8, 8, 8, 8);
        alertDialogViewHolder.icon.setImageResource(iconList[position]);
        alertDialogViewHolder.iconName.setText((iconName[position]).toString());
        return convertView;
    }
    private  String[] iconName = {
            "早餐","午餐","晚餐",
            "生活", "服飾","交通",
            "飲料","醫療", "收入",
            "娛樂","進修","其他",
    };
    // This is your source for your icons, fill it with your own
    private Integer[] iconList = {
            R.drawable.bre,
            R.drawable.lun,
            R.drawable.dinner,
            R.drawable.tool,
            R.drawable.cloth,
            R.drawable.traffic,
            R.drawable.drink,
            R.drawable.medical,
            R.drawable.invest,
            R.drawable.enter,
            R.drawable.study,
            R.drawable.other
    };

    private class AlertDialogViewHolder {
        ImageView icon;
        TextView iconName;
    }
}