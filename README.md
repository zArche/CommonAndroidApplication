安卓app开发模板<br>
	Update:
		Date:2016/5/20
		Content:
			*  网络请求放弃使用Volley，采用retrofit+okhttp+rxandroid模式
			*  加入了懒人开发神器butterkinfe
			*  数据库换成了activeandroid
			*  当然，有最牛的rxandroid和lambda

	1.简介<br>
	　封装一个通用型的app模板<br>
	　　*  用以后续app开发时可以快速进入项目细节开发<br>
	　　*  用以规范项目组织结构<br>
	　　*  提取封装常用的技术点，以期望最大化达到重用、方便<br>
	　　*  学习→_→<br>
	<br>
	2.内容<br>
	　主要已封装和将要封装的内容<br>
	　　*  网络操作框架Volley，简化网络调用<br>
	　　*  数据库框架Ormlite，可根据已有的实体模型类User扩展后续模型类(OMG!数据库操作居然能so so easy!!)<br>
	　　*  事件总线EventBus,在基类中封装方法，更进一小小步简化EventBus的使用<br>
	　　*  fastJson肯定是有的</坏笑><br>
	　　*  以面向对象的思维，封装一个中间层BaseActivity，提取通用方法、单例变量<br>
	　　*  一大堆Material Design控件的使用示例<br>
	<br>
	3.感谢前辈大神们所造的强大的轮子啊！<br>
	  * Android开源项目汇总: https://github.com/Trinea/android-open-project<br>
	  * Volley: https://android.googlesource.com/platform/frameworks/volley<br>
	  * Ormlite: https://github.com/j256/ormlite-android<br>
	  * EventBus: https://github.com/greenrobot/EventBus<br>
	  * fastJson: https://github.com/alibaba/fastjson<br>
	  * 在线android material design取色器: http://www.materialpalette.com/<br>
	    

