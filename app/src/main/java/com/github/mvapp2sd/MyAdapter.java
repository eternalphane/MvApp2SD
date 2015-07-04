package com.github.mvapp2sd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by EternalPhane on 2015/6/13.
 * GitHub: https://github.com/EternalPhane
 */
public class MyAdapter extends BaseAdapter {
    private List<MyAppInfo> AppInfoList;
    LayoutInflater inflater = null;
    public boolean flag_cbVisibility = false;

    public MyAdapter(Context context, List<MyAppInfo> appInfoList) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AppInfoList = appInfoList;
    }

    @Override
    public int getCount() {
        return AppInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return AppInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            view = inflater.inflate(R.layout.my_listitem, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        MyAppInfo appInfo = (MyAppInfo) getItem(position);
        holder.Icon.setImageDrawable(appInfo.GetIcon());
        holder.AppName.setText(appInfo.GetAppName());
        holder.PackageName.setText(appInfo.GetPackageName());
        holder.Check.setVisibility(flag_cbVisibility ? CheckBox.VISIBLE : CheckBox.GONE);
        holder.Check.setChecked(flag_cbVisibility ? ((MyAppInfo) getItem(position)).GetChecked() : false);
        holder.Check.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MyAppInfo) getItem(position)).SetChecked(((CheckBox) v).isChecked());
                        System.out.println(Integer.toString(position) + ": " + Boolean.toString(((CheckBox) v).isChecked()));
                    }
                }
        );
        return view;
    }

    private class ViewHolder {
        ImageView Icon;
        TextView AppName;
        TextView PackageName;
        CheckBox Check;

        public ViewHolder(View view) {
            this.Icon = (ImageView) view.findViewById(R.id.item_icon);
            this.AppName = (TextView) view.findViewById(R.id.item_app_name);
            this.PackageName = (TextView) view.findViewById(R.id.item_package_name);
            this.Check = (CheckBox) view.findViewById(R.id.item_check);
        }
    }
}
