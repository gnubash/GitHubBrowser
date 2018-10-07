package com.antondevs.apps.githubbrowser.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antondevs.apps.githubbrowser.R;

import java.util.List;

/**
 * Created by Anton.
 */
public class UserReposFragment extends Fragment implements
        UserReposRecyclerViewAdapter.UserReposAdapterClickListener {

    private static final String LOGTAG = UserReposFragment.class.getSimpleName();

    public interface UserReposClickListener {

        void onRepoSelected(String repoFullName);
    }

    public interface ReposListScrollListener {

        void onScrollToBottom();
    }

    private Context contextReference;
    private UserReposClickListener repoSelectListener;
    private ReposListScrollListener repoScrollListener;

    private UserReposRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    public UserReposFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOGTAG, "onCreateView");
        View view = inflater.inflate(R.layout.user_repos_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.user_repos_recyclerview);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0 && layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    Log.d(LOGTAG, "onScrolled lastVisible = " +
                            layoutManager.findLastVisibleItemPosition() + " itemCount " +
                            layoutManager.getItemCount());
                    repoScrollListener.onScrollToBottom();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOGTAG, "onActivityCreated");

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(contextReference);

        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contextReference = context;
        if (getActivity() instanceof UserReposClickListener) {
            repoSelectListener = (UserReposClickListener) getActivity();
        }
        if (getActivity() instanceof ReposListScrollListener) {
            repoScrollListener = (ReposListScrollListener) getActivity();
        }
        Log.d(LOGTAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contextReference = null;
        Log.d(LOGTAG, "onDetach");
    }

    public void setReposList(List<String> userReposList) {
        Log.d(LOGTAG, "setReposList " + userReposList.toString());
        if (recyclerViewAdapter == null) {

            recyclerViewAdapter = new UserReposRecyclerViewAdapter(userReposList, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
        else {
            recyclerViewAdapter.swapReposList(userReposList);
        }

    }

    @Override
    public void onUserItemCLick(String itemName) {
        Log.d(LOGTAG, "onUserItemCLick itemName = " + itemName);
        repoSelectListener.onRepoSelected(itemName);
    }

}
