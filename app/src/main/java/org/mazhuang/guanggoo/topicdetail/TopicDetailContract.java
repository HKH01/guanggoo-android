package org.mazhuang.guanggoo.topicdetail;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.TopicDetail;

/**
 * Created by mazhuang on 2017/9/17.
 */

public interface TopicDetailContract {
    interface Presenter extends BasePresenter {
        void getTopicDetail();
        void getMoreComments(int page);
    }

    interface View extends BaseView<Presenter> {
        void onGetTopicDetailSucceed(TopicDetail topicDetail);
        void onGetTopicDetailFailed(String msg);

        void onGetMoreCommentsSucceed(TopicDetail topicDetail);
        void onGetMoreCommentsFailed(String msg);
    }
}
