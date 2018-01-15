package org.mazhuang.guanggoo.topicdetail;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;
import org.mazhuang.guanggoo.data.entity.TopicDetail;

/**
 *
 * @author mazhuang
 * @date 2017/9/17
 */

public interface TopicDetailContract {
    interface Presenter extends BasePresenter {
        /**
         * 获取主题详情
         */
        void getTopicDetail();

        /**
         * 获取第 page 页的评论
         * @param page 页码
         */
        void getMoreComments(int page);

        /**
         * 发表新回复
         * @param content 回复内容
         */
        void comment(String content);

        /**
         * 收藏
         * @param state 当前状态
         */
        void favourite(String state);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 获取主题详情成功
         * @param topicDetail 主题详情
         */
        void onGetTopicDetailSucceed(TopicDetail topicDetail);

        /**
         * 获取主题详情失败
         * @param msg 失败提示信息
         */
        void onGetTopicDetailFailed(String msg);

        /**
         * 获取更多评论内容成功
         * @param topicDetail 包含更多评论内容的主题详情对象
         */
        void onGetMoreCommentsSucceed(TopicDetail topicDetail);

        /**
         * 获取更多评论内容失败
         * @param msg 失败提示信息
         */
        void onGetMoreCommentsFailed(String msg);

        /**
         * 发表评论成功
         */
        void onCommentSucceed();

        /**
         * 发表评论失败
         * @param msg 失败提示信息
         */
        void onCommentFailed(String msg);

        /**
         * 收藏成功
         * @param msg 用于 toast 的提示信息
         * @param nextState 下一个状态
         */
        void favouriteSuccess(String msg, String nextState);

        /**
         * 收藏失败
         * @param msg 失败提示信息
         */
        void favouriteFail(String msg);
    }
}
