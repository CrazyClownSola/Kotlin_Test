# Kotlin_Test
> [Kotlin](http://kotlinlang.org/) with [Dagger2](https://github.com/google/dagger) + [DataBinding](https://developer.android.com/topic/libraries/data-binding/index.html) + [RxKotlin](https://github.com/ReactiveX/RxKotlin) + [Retrofit2.0](https://github.com/square/retrofit) + [ijkplayer](https://github.com/Bilibili/ijkplayer) + [picasso](https://github.com/square/picasso)
> 又是一种新的尝试，在自身原有研究的基础上，配合新语言Kotlin进行代码的重构，同时引入一些的新的概念
> 比方说[RxJava2](https://github.com/ReactiveX/RxJava)(对于背压进行优化的新版)，[ijkplayer](https://github.com/Bilibili/ijkplayer)(BiliBili的开源播放器，算是涉足一个新的领域吧，虽然离理解内部原理还有很长的一段距离)
> 也体验过Kotlin官方提供[Anko](https://github.com/Kotlin/anko)库，然而个人更加喜欢`Databinding`的界面编写方式，所以这里并没有采用`Anko`




## 一些踩坑的过程
> 当初接触到Kotlin的时候，个人非常喜欢这种语言的风格，和语言带来的Coding的乐趣，如果光是使用Kotlin去写一个app，本质不是什么太难的事情，当然Kotlin的开发组在考虑到这些，同时无私的共享了Anko这套库包
> Anko可以说是一改Android原有的界面编写方式，纯代码的界面构建方式，带来了不小的冲击，去体验了一下这样的冲击下带来的代码变动，然后……弃坑
> 原因其实很简单，个人更加习惯去用xml编写界面，可视性更高，同时配合DataBinding，界面编写不再显得那么乏味


### 坑1 Kotlin+Gradle
> 由于AndroidStudio 3.0 的升级还没有放出，当前2.3.3的版本对于Kotlin的适配有不少问题存在，其实每次看到gradle编译的时候出一些让人看不懂的错误的时候，的确很头疼
> 在不断爬坑和修复的过程中发现，在Gradle2.2.3的版本下，Kotlin比较稳定，能力有限没有很好的定位到具体是为啥

### 坑2 Kotlin+Dagger2
> Dagger2在设计的时候是以java为标准开发的，所以在用Kotlin写的时候，偶热会发现一些不适配的问题在里面
> 在我爬坑的时候就发现过 Dagger2在某些情况下会用到一个第三方库[auto-value](https://github.com/google/auto/tree/master/value)
> 然而很不幸的是auto-value用的java的方式进行编码，这就导致编译的时候会出错