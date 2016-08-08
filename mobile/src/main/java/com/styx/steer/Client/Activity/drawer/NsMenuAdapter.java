package com.styx.steer.Client.Activity.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.styx.steer.Client.R;


public class NsMenuAdapter extends ArrayAdapter<NsMenuItemModel>
{
	
	/*
	 * public NsMenuAdapter(Context context, int resource, int
	 * textViewResourceId, String[] objects) { super(context,
	 * R.layout.ns_menu_row, textViewResourceId, objects); }
	 */
	
	public NsMenuAdapter(Context context)
	{
		super(context, 0);
	}
	
	public void addHeader(int title)
	{
		add(new NsMenuItemModel(title, -1, -1, true));
	}
	
	public void addItem(int title, int icon, int color)
	{
		add(new NsMenuItemModel(title, icon, color,false));
	}
	
	public void addItem(NsMenuItemModel itemModel)
	{
		add(itemModel);
	}
	
	@Override
	public int getViewTypeCount()
	{
		return 2;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		return getItem(position).isHeader ? 0 : 1;
	}
	
	@Override
	public boolean isEnabled(int position)
	{
		return !getItem(position).isHeader;
	}
	
	public static class ViewHolder
	{
		public final TextView textHolder;
		public final ImageView imageHolder;
        public final ImageView colorHolder;

		public final TextView textCounterHolder;
		
		public ViewHolder(TextView text1, ImageView image1, ImageView color1, TextView textcounter1)
		{
			this.textHolder = text1;
			this.imageHolder = image1;
            this.colorHolder = color1;
			this.textCounterHolder = textcounter1;
		}
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		NsMenuItemModel item = getItem(position);
		ViewHolder holder = null;
		View view = convertView;
		
		if (view == null)
		{
			int layout = R.layout.ns_menu_row_counter;
			if (item.isHeader)
				layout = R.layout.ns_menu_row_header;
			
			view = LayoutInflater.from(getContext()).inflate(layout, null);
			
			TextView text1 = (TextView) view.findViewById(R.id.menurow_title);
			ImageView image1 = (ImageView) view.findViewById(R.id.menurow_icon);
			TextView textcounter1 = (TextView) view.findViewById(R.id.menurow_counter);
            ImageView color1 = (ImageView) view.findViewById(R.id.color);
			view.setTag(new ViewHolder(text1, image1, color1, textcounter1));
		}
		
		Object tag = view.getTag();
		if (tag instanceof ViewHolder)
		{
			holder = (ViewHolder) tag;
		}
		
		if (item != null && holder != null)
		{
			if (holder.textHolder != null)
				holder.textHolder.setText(item.title);
			
			if (holder.textCounterHolder != null)
			{
				if (item.counter > 0)
				{
					holder.textCounterHolder.setVisibility(View.VISIBLE);
					holder.textCounterHolder.setText("" + item.counter);
				}
				else
				{
					holder.textCounterHolder.setVisibility(View.GONE);
				}
			}
			
			if (holder.imageHolder != null)
			{
				if (item.iconRes > 0)
				{
					holder.imageHolder.setVisibility(View.VISIBLE);
					holder.imageHolder.setImageResource(item.iconRes);
				}
				else
				{
					holder.imageHolder.setVisibility(View.GONE);
				}
			}

            if (holder.colorHolder != null)
            {
                if (item.color > 0)
                {
                    holder.colorHolder.setVisibility(View.VISIBLE);
                    holder.colorHolder.setImageResource(item.color);
                }
                else
                {
                    holder.colorHolder.setVisibility(View.GONE);
                }
            }

		}
		
		return view;
	}
	
}
