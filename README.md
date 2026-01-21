# BoxUtil
用作 [Starsector](https://fractalsoftworks.com/) Mod的 基于 OpenGL 4.3 的多线程图形库或引擎。

以 实例化渲染 和 GPU计算 为中心，在大多数近代硬件配置都能运行的前提下，实现各种更性能化与质量化的图形相关功能。

同时增加了一个光影包的接入点，用于统一且规范地管理各种会影响游戏整体画面的风格化渲染，也便于玩家随时在游戏中对其切换。


## 如何使用
所有包含的类或方法：参考该仓库或论坛发布页面附带的JavaDoc

用作渲染时的调用讲解：参考 [该指南](https://www.fossic.org/thread-15746-1-1.html)

内置的实际使用例：参考内置战役的源码，位于该仓库 backends/src/data/missions/BUtilTestMission


## 问题汇报
对于发生了使得游戏中止运行的错误，请附带上：
- 报错前操作
- 报错时所启用的Mod
- 报错时的log文件 `'游戏根目录'/starsector-core/starsector.log`
- 对于 ***Unexpected Error*** 报错，根据报错时弹窗第一段内容，根据内容中 `pid={一串数字}` 这一点，找到 `'游戏根目录'/starsector-core/hs_err_pid{一串数字}.log` 文件一并发送

对于安装了类似 ***Fast Rendering*** 的，包含**修改**游戏原本文件行为的其他Mod/补丁，移步寻找对应Mod/补丁的作者解决