DataBinding 完整使用介绍篇
【前言】
DataBinding是 google在2015年I/O大会上推出的一个数据绑定框架。提高了开发者的开发效率，具有高性能、工功能强等特点。DataBinding是基于MVVM模式，保证了xml中的数据唯一来源为ViewModel，去掉了Activity和Fragment中大部分UI相关代码，减少了findViewById的调用，保证UI更新执行在主线程。
【环境构建】
 Data Binding Library兼容到了Android2.1版本。要求Gradle版本不低于1.5， Android Studio至少1.3。
要使用DataBinding，需要在项目model的 build.gradle里加上如下设置:
   android{
   ...
	dataBinding{
    	enabled true;
	}
}
【布局差异】
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
<data>
    <variable
        name=””
        Type=””/>

        <import type="android.view.View" alias="otherName"/>
</data>
    
	<!-- 此次放正常的布局 -->
</layout>


可以看出，使用了DataBinding的xml布局，根元素变成了<layout>，里面的<data>嵌套了<variable>，用于声明传递进来的Object变量名和类型。此外还可以使用<import>导入Object，就像在java代码中import一样。如果出现导入的多个Object具有相同的名称，则可以通过alias属性设置别名。如上，otherName也可以代表android.view.View。
【类名自动生成规则】
每个支持DataBinding的xml对应一个自动生成的Binding类。生成规则是，xml布局文件名称去掉下划线，根据驼峰标识，再加上Binding。如content_main.xml对应ContentMainBinding.java。当然如果需要自定义类名，可以在<data>标签上设置class属性值，如：

<!--注意，name和其他布局里面的name不能一样, 用class自定义生成的Binding classes 类名，也可
以使用全路径名来指定生成类存放的位置-->
<data class="CustomClassName">

【绑定xml和Activity】
只需一行代码就可以绑定。通过调用DataBindingUtil的setContentView获取自动生成DataBinding类。
  CustomClassName viewDataBinding = DataBindingUtil.setContentView(this, R.layout.content_main); 




【不用findViewById】
布局里有个TextView控件，id是tv_no_find_biew_by_id，
<TextView
    android:id="@+id/tv_no_find_biew_by_id"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:textColor="#0f0"
    android:textSize="16sp"
    tools:text="不用 findviewbyid 了" />
可通过viewDataBinding对它进行访问操作。
viewDataBinding.tvNoFindBiewById.setText("不用执行 findViewById 了");
【绑定Object】
创建一个JavaBean 类User，注意需要提供getXXX。如果当User的属性name变化了需要实时更新UI，那么User需要extends BaseObservable，并且在getName增加@Bindable，在setName调用
notifyPropertyChanged(BR.name)。
public class User extends BaseObservable{
    private String name;
    private String age;
    ...
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        /**
         * 通知更新UI界面
         */
        notifyPropertyChanged(BR.name);
    }
然后在xml中声明：
<variable
    name="user"
    type="com.example.jay.databindingdemo.User" />
注意，type的值是对应类的全路径名。然后就可以在xml中使用user，如下：
<TextView
    android:id="@+id/tv_name"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_toRightOf="@id/tv_tip"
    android:text="@{user.name}"
    android:textSize="18sp" />
说明：@{user.name} 调用了User中的getName，如果没有提供getxxx是无法访问User中的name属性。
mUser = new User("小明", "33", true);
viewDataBinding.setUser(mUser);
调用了以上代码后，就可以在xml中获取user的name值了。
【绑定点击事件】
点击事件的绑定有两种方式：
① Method References
声明事件处理类：
public class FirstClickHandle {

    /**
     * 注意：函数的签名必须和原来的 Listener 函数签名一致，但是方法名可以自定义
     * 例：OnClickListener 中的 void onClick(View v);
     *
     * @param view
     */
    public void onClickFirst(View view) {
        Toast.makeText(view.getContext(), "第一种点击绑定：方法引用", Toast.LENGTH_LONG).show();
    }
}

xml中声明：
<variable
    name="firstClickHandler"
    type="com.example.jay.databindingdemo.FirstClickHandle" />
Button方法引用：
<Button
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:onClick="@{firstClickHandler::onClickFirst}"
    android:text="Method References 绑定点击事件"
    android:textAllCaps="false" />
注：可以写成firstClickHandler::onClickFirst，也可以是firstClickHandler.onClickFirst。
最后记得FirstClickHandle和ViewDataBinding绑定：
FirstClickHandle firstClickHandle = new FirstClickHandle();
viewDataBinding.setFirstClickHandler(firstClickHandle);
② Listener Bindings
该方法的实现依赖lambda 表达式，可以灵活的指定函数的参数，函数的签名不需要和来的 Listener 函数签名一致，但是返回值必须一样，除非返回值是void。
声明事件处理类：
public class SecondClickHandle {

    /**
     * 函数的签名不需要和来的 Listener 函数签名一致，但是返回值必须一样，除非返回值是void
     *
     * @param view
     * @param user
     */
    public void onClickSecond(View view, User user) {
        Toast.makeText(view.getContext(), "xml传递来的user =" + user.getName(), Toast.LENGTH_LONG).show();
    }
}
xml中声明：
<variable
    name="secondClickHnalder"
    type="com.example.jay.databindingdemo.SecondClickHandle" />
Button通过lambda表达式引用:
<Button
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:onClick="@{(theview) -> secondClickHnalder.onClickSecond(theview, user)}"
    android:text="Listener Bindings 绑定点击事件"
    android:textAllCaps="false" />
说明：

