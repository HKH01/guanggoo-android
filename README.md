# guanggoo-android

[光谷社区](http://www.guanggoo.com) 第三方客户端。

**目录**
<!-- vim-markdown-toc GFM -->
* [屏幕截图](#屏幕截图)
* [功能列表](#功能列表)
* [界面特性](#界面特性)
* [API](#api)
* [License](#license)

<!-- vim-markdown-toc -->

## 屏幕截图

<img width="360" src="./screenshots/topic-list.png" align=center /> <img width="360" src="./screenshots/topic-detail.png" align=center />

## 功能列表

- [x] 登录
- [x] 首页主题列表
- [x] 主题详情
    - [x] 主题内容
    - [x] 评论列表
- [ ] 节点列表
- [ ] 节点主题列表
- [ ] 个人信息页
- [ ] 收藏的主题列表
- [ ] 浏览用户
- [ ] 评论
- [ ] 发表新主题
- [ ] 收藏
- [ ] 登出
- [ ] 注册
- [ ] 分享链接
- [ ] 已读/未读状态区分

## 界面特性

- [x] 主题详情支持动图和视频
- [ ] ToolBar 设定
    - [x] 如果当前 Fragment 栈里的数量大于 1，就显示返回按钮，可以滑出 Drawer，否则显示菜单，锁定 Drawer
    - [ ] 在合适的时候显示右侧菜单
- [ ] 列表下拉刷新
- [x] 列表上滑加载更多
    - [x] 主题列表页自动加载
    - [x] 主题评论点击手动加载
- [ ] Loading 动画
- [ ] 应用内处理图片与链接点开

## API

[docs/guanggoo-api.md](./docs/guanggoo-api.md)

因为光谷社区并未提供 API，所以是基于 DOM 解析，网站的前端界面改动有可能导致数据不可用，可以考虑做一个 API 监控脚本，定期测试 API 的可用性。

## License

[Apache License 2.0](https://github.com/mzlogin/guanggoo-android/blob/master/LICENSE)
