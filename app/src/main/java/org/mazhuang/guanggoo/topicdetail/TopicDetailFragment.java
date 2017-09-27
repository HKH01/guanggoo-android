package org.mazhuang.guanggoo.topicdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.Comment;
import org.mazhuang.guanggoo.data.entity.TopicDetail;
import org.mazhuang.guanggoo.util.ConstantUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mazhuang on 2017/9/17.
 */

public class TopicDetailFragment extends BaseFragment<TopicDetailContract.Presenter> implements TopicDetailContract.View, Commentable {

    private TopicDetail mTopicDetail;
    private CommentsListAdapter mAdapter;

    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.title) TextView mTitleTextView;
    @BindView(R.id.avatar) ImageView mAvatarImageView;
    @BindView(R.id.created_time) TextView mCreatedTimeTExtView;
    @BindView(R.id.author) TextView mAuthorTextView;
    @BindView(R.id.node) TextView mNodeTextView;

    @BindView(R.id.content) WebView mContentWebView;

    @BindView(R.id.comments) RecyclerView mCommentsRecyclerView;

    @BindView(R.id.load_more) TextView mLoadMoreTextView;

    @BindView(R.id.comment_view) View mCommentsView;
    @BindView(R.id.comment) EditText mCommentEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_topic_detail, container, false);

        ButterKnife.bind(this, root);

        initWebView();

        initRecyclerView();

        mPresenter.getTopicDetail();

        return root;
    }

    @OnClick({R.id.load_more, R.id.submit, R.id.cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_more: {
                mLoadMoreTextView.setEnabled(false);
                int page = (mAdapter.getSmallestFloor() - 1) /ConstantUtil.COMMENTS_PER_PAGE;
                if (page == 0) {
                    mLoadMoreTextView.setVisibility(View.GONE);
                    return;
                }
                mPresenter.getMoreComments(page);
            }
                break;

            case R.id.submit:
                if (TextUtils.isEmpty(mCommentEditText.getText())) {
                    Toast.makeText(getContext(), R.string.please_input_content, Toast.LENGTH_SHORT).show();
                } else {
                    mPresenter.comment(mCommentEditText.getText().toString());
                }
                break;

            case R.id.cancel:
                mCommentsView.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mCommentsRecyclerView.setLayoutManager(layoutManager);

        mCommentsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 1);
            }
        });
        if (mAdapter == null) {
            mAdapter = new CommentsListAdapter(mListener);
        }
        mCommentsRecyclerView.setAdapter(mAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCommentsRecyclerView.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        if (mContentWebView != null) {
            mContentWebView.loadUrl("about:blank");
            mContentWebView = null;
        }
        super.onDestroy();
    }

    private void initWebView() {
        mContentWebView.getSettings().setUseWideViewPort(false);
        mContentWebView.getSettings().setLoadWithOverviewMode(true);
        mContentWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mContentWebView.setWebChromeClient(new WebChromeClient());
        mContentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
        });
        mContentWebView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onGetTopicDetailSucceed(TopicDetail topicDetail) {

        if (getContext() == null) {
            return;
        }

        mTopicDetail = topicDetail;

        mTitleTextView.setText(topicDetail.getTopic().getTitle());
        Glide.with(getContext())
                .load(topicDetail.getTopic().getAvatar())
                .centerCrop()
                .crossFade()
                .into(mAvatarImageView);
        mCreatedTimeTExtView.setText(topicDetail.getTopic().getMeta().getCreatedTime());
        mAuthorTextView.setText(topicDetail.getTopic().getMeta().getAuthor().getUsername());
        mNodeTextView.setText(topicDetail.getTopic().getMeta().getNode().getTitle());

        // 相比 loadData，这个调用能解决中文乱码的问题
        mContentWebView.loadDataWithBaseURL(null, topicDetail.getContent() + "<style>img{display:inline; height:auto; max-width:100%;} a{word-break:break-all; word-wrap:break-word;} pre, code, pre code{word-wrap:normal; overflow:auto;} pre{padding:16px; bordor-radius:3px; border:1px solid #ccc;}</style>", "text/html", "UTF-8", null);

        mAdapter.setData(mTopicDetail.getComments());

        if (mAdapter.getSmallestFloor() > 1) {
            mLoadMoreTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetTopicDetailFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMoreCommentsSucceed(TopicDetail topicDetail) {
        mAdapter.addData(topicDetail.getComments());
        int page = (mAdapter.getSmallestFloor() - 1) /ConstantUtil.COMMENTS_PER_PAGE;
        if (page == 0) {
            mLoadMoreTextView.setVisibility(View.GONE);
        } else {
            mLoadMoreTextView.setEnabled(true);
        }
    }

    @Override
    public void onGetMoreCommentsFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        mLoadMoreTextView.setEnabled(true);
    }

    @Override
    public String getTitle() {
        return "主题详情";
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Comment item);
    }

    @Override
    public void onCommentSucceed() {
        mCommentEditText.setText("");
        mCommentsView.setVisibility(View.GONE);
        mPresenter.getTopicDetail();
    }

    @Override
    public void onCommentFailed(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCommentView() {
        mCommentsView.setVisibility(View.VISIBLE);
        mCommentEditText.requestFocus();
    }

    @Override
    public boolean onBackPressed() {
        if (mCommentsView.getVisibility() == View.VISIBLE) {
            mCommentsView.setVisibility(View.GONE);
            return true;
        }

        return super.onBackPressed();
    }
}
