package com.styx.steer.Client.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.R;
import com.styx.steer.Protocol.SteerActionReceiver;
import com.styx.steer.Protocol.action.FileExploreRequestAction;
import com.styx.steer.Protocol.action.FileExploreResponseAction;
import com.styx.steer.Protocol.action.SteerAction;

import java.util.ArrayList;
import java.util.Arrays;

public class FileExplorerActivity extends MainActivity implements SteerActionReceiver, OnItemClickListener
{
	private static final int REFRESH_MENU_ITEM_ID = 0;
	private static final int EXPLORE_ROOTS_MENU_ITEM_ID = 1;
	private static final int ANIM_DURATION_TEXT = 400;
	private static final int ANIM_DURATION_TOOLBAR = 200;
	ImageView path;
	private Steer application;
	private SharedPreferences preferences;
	private String directory;
	private ArrayList<String> files;
	private ArrayAdapter<String> adapter;
	private ListView listView;
	private TextView textView;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.application = (Steer) this.getApplication();
		
		this.preferences = this.application.getPreferences();

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.fileexplorer, null, false);
		mDrawer.addView(contentView, 0);
        mTitle.setText("File Explorer");
        startcontent();
        path = (ImageView) findViewById(R.id.path);
		this.listView = (ListView) this.findViewById(R.id.files);
		
		this.textView = (TextView) this.findViewById(R.id.directory);
		
		this.files = new ArrayList<String>();
		
		this.adapter = new ArrayAdapter<String>(this, R.layout.fileexplorer_row, this.files);
		this.listView.setAdapter(this.adapter);
		this.listView.setOnItemClickListener(this);



	}

    public void startcontent() {
        int actionbarSize = 112;
        getToolbar().setTranslationY(-actionbarSize);
        getToolTitle().setTranslationY(-actionbarSize);
        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(100);
        getToolTitle().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TEXT)
                .setStartDelay(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        path.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }
	
	protected void onResume()
	{
		super.onResume();
		
		this.application.registerActionReceiver(this);
		
		this.directory = this.preferences.getString("fileExplore_directory", "");
		
		this.refresh();
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.file_explorer_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

    public void Refresh(View v){
        this.refresh();
    }
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_explore_roots:
				this.exploreRoots();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
	
	protected void onPause()
	{
		super.onPause();
		this.application.unregisterActionReceiver(this);
		Editor editor = this.preferences.edit();
		editor.putString("fileExplore_directory", this.directory);
		editor.commit();
		finish();
	}
	
	public void receiveAction(SteerAction action)
	{
		if (action instanceof FileExploreResponseAction)
		{
			FileExploreResponseAction fera = (FileExploreResponseAction) action;
			
			this.directory = fera.directory;
			
			this.files.clear();
			this.files.addAll(Arrays.asList(fera.files));

			this.runOnUiThread(new Runnable()
			{
				public void run()
				{
					FileExplorerActivity.this.textView.setText(FileExplorerActivity.this.directory);
					FileExplorerActivity.this.adapter.notifyDataSetChanged();
					FileExplorerActivity.this.listView.setSelectionAfterHeaderView();
				}
			});
		}
	}

	private void sendFileExploreRequest(String fileString)
	{
		this.application.sendAction(new FileExploreRequestAction(this.directory, fileString));
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		this.sendFileExploreRequest(this.files.get(position));
	}
	
	private void refresh()
	{
		this.sendFileExploreRequest("");
	}
	
	private void exploreRoots()
	{
		this.directory = "";
		this.refresh();
	}
}
