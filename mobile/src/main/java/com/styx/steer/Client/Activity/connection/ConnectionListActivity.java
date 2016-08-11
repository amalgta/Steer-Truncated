package com.styx.steer.Client.Activity.connection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.styx.steer.Client.App.Steer;
import com.styx.steer.Client.Connection.Connection;
import com.styx.steer.Client.Connection.ConnectionList;
import com.styx.steer.Client.R;

public class ConnectionListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ConnectionsAdapter adapter;
    private ConnectionList connectionList;
    private Steer mApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mApplication = (Steer) this.getApplication();
        connectionList = mApplication.getConnections();
        adapter = new ConnectionsAdapter(this, connectionList);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //adapter = new ConnectionsAdapter(this, connectionList);
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.MyViewHolder> {
        private Context mContext;
        private ConnectionList connectionList;
        private int currentConnectionPosition;

        public ConnectionsAdapter(Context mContext, ConnectionList connectionList) {
            this.mContext = mContext;
            this.connectionList = connectionList;
            this.currentConnectionPosition = this.connectionList.getUsedPosition();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.connection_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Connection connection = connectionList.get(position);
            holder.connection_name.setText(connection.getName());
            holder.connection_ip.setText(connection.getAddress());
            Glide.with(mContext).load(connection.getThumbnail()).into(holder.connection_thumbnail);

            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder.overflow, position);
                }
            });

        }

        private void showPopupMenu(View view, int position) {
            // inflate menu
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.connection_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
            popup.show();
        }

        @Override
        public int getItemCount() {
            return this.connectionList.getCount();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView connection_name, connection_ip;
            public ImageView connection_thumbnail, overflow;

            public MyViewHolder(View view) {
                super(view);
                connection_name = (TextView) view.findViewById(R.id.connection_name);
                connection_ip = (TextView) view.findViewById(R.id.connection_ip);
                connection_thumbnail = (ImageView) view.findViewById(R.id.connection_thumbnail);
                overflow = (ImageView) view.findViewById(R.id.overflow);
            }
        }
        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            int position;

            public MyMenuItemClickListener(int position) {
                this.position = position;
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_remove:
                        //   Toast.makeText(mContext, connectionList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        new MaterialDialog.Builder(ConnectionListActivity.this)
                                .title("Delete")
                                .content("Remove connection configuration ?")
                                .positiveText("REMOVE")
                                .negativeText("CANCEL")
                                .showListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        connectionList.remove(position);
                                        connectionList.save();
                                        adapter.notifyItemRemoved(position);
                                    }
                                })
                                .cancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                    }
                                })
                                .dismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                    }
                                })
                                .show();
                        return true;
                    case R.id.action_edit:
                        //Toast.makeText(mContext, connectionList.get(0).getName(), Toast.LENGTH_SHORT).show();
                        //Snackbar.make(findViewById(R.id.test_ID), "Hello Snackbar", Snackbar.LENGTH_LONG).show();
                        return true;
                    default:
                }
                return false;
            }
        }
    }
}