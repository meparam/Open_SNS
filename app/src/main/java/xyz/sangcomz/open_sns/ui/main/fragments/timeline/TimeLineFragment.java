package xyz.sangcomz.open_sns.ui.main.fragments.timeline;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import xyz.sangcomz.open_sns.adapter.PostAdapter;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.core.common.BaseFragment;
import xyz.sangcomz.open_sns.core.common.view.DeclareView;
import xyz.sangcomz.open_sns.util.NoDataController;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends BaseFragment {

    TimeLineController timeLineController;
    NoDataController noDataController;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();

    @DeclareView(id = R.id.recyclerview)
    RecyclerView recyclerView;
    @DeclareView(id = R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @DeclareView(id = R.id.area_nodata)
    RelativeLayout areaNoData;


    LinearLayoutManager linearLayoutManager;



    int pastVisiblesItems, visibleItemCount, totalItemCount;

    int curPage = 1;

    private int totalPage;

    public TimeLineFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_time_line, container, false);
        bindView(rootView);
        timeLineController = new TimeLineController(getActivity(), this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        initAreaNoData();
        curPage = 1;
        recyclerView.setLayoutManager(linearLayoutManager);

        postAdapter = new PostAdapter(getActivity(), posts);
        recyclerView.setAdapter(postAdapter);

        timeLineController.GetPost(curPage++);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                curPage = 1;
                swipeRefreshLayout.setRefreshing(false);
                timeLineController.GetPost(curPage++);
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    if (curPage <= totalPage) {
                        timeLineController.GetPost(curPage++);
                    }
                }

            }
        });

        return rootView;
    }

    protected void initAreaNoData(){
        noDataController = new NoDataController(areaNoData, getActivity());
        noDataController.setNodata(R.drawable.ic_public_black_24dp, getString(R.string.msg_no_post));
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts.addAll(posts);
        if (posts.size() > 0) {
            areaNoData.setVisibility(View.GONE);
            postAdapter.notifyDataSetChanged();
        } else {
            areaNoData.setVisibility(View.VISIBLE);
        }
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
