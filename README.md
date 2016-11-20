【前言】<br />
DataBinding是 google在2015年I/O大会上推出的一个数据绑定框架。提高了开发者的开发效率，具有高性能、工功能强等特点。DataBinding是基于MVVM模式，保证了xml中的数据唯一来源为ViewModel，去掉了Activity和Fragment中大部分UI相关代码，减少了findViewById的调用，保证UI更新执行在主线程。
<br />【项目源码】
https://github.com/JAYAndroid/DataBindingDemo.git
<br />【环境构建】
 Data Binding Library兼容到了Android2.1版本。要求Gradle版本不低于1.5， Android Studio至少1.3。
要使用DataBinding，需要在项目model的 build.gradle里加上如下设置:
   android{
   ...
	dataBinding{
    	enabled true;
	}
}
<br />【布局差异】
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
<br />【类名自动生成规则】
每个支持DataBinding的xml对应一个自动生成的Binding类。生成规则是，xml布局文件名称去掉下划线，根据驼峰标识，再加上Binding。如content_main.xml对应ContentMainBinding.java。当然如果需要自定义类名，可以在<data>标签上设置class属性值，如：

<!--注意，name和其他布局里面的name不能一样, 用class自定义生成的Binding classes 类名，也可
以使用全路径名来指定生成类存放的位置-->
<data class="CustomClassName">

<br />【绑定xml和Activity】
只需一行代码就可以绑定。通过调用DataBindingUtil的setContentView获取自动生成DataBinding类。
  CustomClassName viewDataBinding = DataBindingUtil.setContentView(this, R.layout.content_main);




<br />【不用findViewById】
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
<br />【绑定Object】
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
<br />【绑定点击事件】
点击事件的绑定有两种方式：
<br />① Method References
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
<br />② Listener Bindings
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
最后，别忘了添加如下的代码：
 SecondClickHandle secondClickHandle = new SecondClickHandle();
 viewDataBinding.setSecondClickHnalder(secondClickHandle);
<br />说明：lambda 表达式支持从xml回传参数到调用者。此次的theView代表原始方法onClick(View view)中的view，名称可自定义。
<br />【绑定数据到子布局】
首先声明命名空间bind：
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:bind="http://schemas.android.com/apk/res-auto"
然后在标签<include>中使用bind:xxx=”@{user}”，将数据“传递”进去。
<!--注意，使用了bind的include标签的父标签，不能是 <merge>。而且child_layout中的变量名必须和 bind:xxx的xxx一致 -->
<include
    layout="@layout/child_layout"
    bind:child="@{user}" />
在子布局中如下：
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="child"
            type="com.example.jay.databindingdemo.User" />
    </data>
<br />注：① 使用了bind的include的父标签不能是<merge>，如下所示是错误的：
<merge>
    <include layout="@layout/name"
        bind:user="@{user}"/>
</merge>
<br />② 子布局中的variable的name必须和父布局中bind:xxx的xxx一致，如上面父布局的xxx是child，所以子布局中的name也是child。
<br />【自动更新UI】
用来显示User.name 的TextView控件，如果name值发生变化，DataBinding支持动态更新UI，而且开发者无需关系UI线程切换问题。
首先，在User中的getName上增加@Bindable，并且在setName中设置完name值后，调用notifyPropertyChanged(BR.name)。前提是User extends BaseObservable。
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



当调用了mUser.setName会自动更新UI。当然如果在子线程调用setName也是可以的，DataBinding会自动处理，并更新UI。
       public void afterTextChangeCall(final CharSequence s, int start, int before, int count) {
            /**
             * 自动更新UI,不用关心线程切换问题
             */
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mUser.setName(s.toString());
//                }
//            }).start();

            mUser.setName(s.toString());
        }
<br />【支持使用表达式】
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_toRightOf="@id/tv_name"
    android:text="成年人"
    android:textSize="18sp"
    android:visibility="@{user.adult ? View.VISIBLE : View.GONE}" />
以上使用到了三元运算符表达式。
表达式中支持的操作符有：<br />
|                |              |
| -------------  |:------------:|
| 数字运算符     | + - / * %    |
| 字符串连接符   | +            |
| 逻辑运算符     | 	&& ||       |
| 二进制位操作符 | 	& | ^       |
| 一元运算符     | 	+ - ! ~     |
| 位移操作符     | 	>> >>> <<   |
| 比较运算符     | == > < >= <= |
| 实例运算符     | 	instanceof  |
| 集合操作       | 	Grouping()  |
| 实例强制转换   |   	Cast    |
| 方法调用       | Method calls |
| 属性访问       | Field access |
| 数组访问       | Array access |
| 三元运算符     |  	?:      |<br />

同时要注意，表达式中不支持的有：This 、new、super。
<br />【高级绑定——双向绑定】
双向绑定指的是，在某处改变了属性的值，其他用到这个属性值的地方，也会同步更新。双向绑定的符号是 @=，如下：
<EditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="双向绑定"
    android:text="@={user.age}" />
其它地方的控件TextView也使用了user.age
<TextView
    android:hint="双向绑定内容"
    android:text="@{@string/welcome(user.age)}"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
当用户在EditText输入内容时，TextView的内容会实时更新。
注：User的getAge需要加上@Bindable，并且在setAge里调用notifyPropertyChanged(BR.age)。

<br />【高级绑定——Adapter】
DataBinding除了支持Event Binding、Data Object Binding，还支持Dynamic Variables（暂且翻译成动态变量绑定）。如在RecyclerView.Adapter中的onBindViewHolder。
首先，声明DataBindingViewHolder，让它继承RecyclerView.ViewHolder：并提供getBinding方法
public class DataBindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private T binding;

    public DataBindingViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public T getBinding() {
        return binding;
    }
}

然后，声明DataBindingAdapter，让它继承RecyclerView.Adapter
，注意它复写的两个方法onCreateViewHolder
和onBindViewHolder：
@Override
public DataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.recycle_item, parent, false);
    return new DataBindingViewHolder(binding);
}

@Override
public void onBindViewHolder(DataBindingViewHolder holder, int position) {
    holder.getBinding().setVariable(BR.item, dataSources.get(position));
    holder.getBinding().executePendingBindings();
}
说明：onCreateViewHolder中，通过DataBindingUtil.inflate指定需要绑定的xml布局。在onBindViewHolder中，通过setVariable绑定数据到动态变量，并通过调用executePendingBindings来强制实时更新UI。
<br />【总结】<br />
DataBinding带来便利的同时，如果在xml过度使用DataBinding，会降低xml的可读性。同时需要注意表达式不要过于复杂，分清xml和Java的分界。
本文对DataBinding的内容进行了取舍，有些特性没有讲解到，如自定义属性、表达式连、动画等。感兴趣的朋友们，请自行查阅资料学习（建议参考官方开发者文档进行学习！）





