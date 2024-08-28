项目公共工具
===
这里会对项目封装的工具类和向上提供能力的组件做出罗列和解析，以便项目成员清晰使用，提高开发效率。

换肤功能 使用
---
为了支持项目换肤和扩展平台马甲包需求，更新了项目架构，目前只在odin分支使用

### 如何创建一个平台包？

#### Step 1
    于根目录channel_config/groupAll.json内增加平台对象 

#### Step 2
    于根目录channel_config/channels/内增加平台json信息文件

#### Step 3
    于library-res组件内创建src/平台名称/文件夹，资源内容与main相同即可替换资源

一键切换环境功能 使用
---
为了方便测试和包管理，特增加此功能用于应用内切换环境

### 如何切换？
    当apk为debug模式时，在登录页面有"切换环境"文字，点击操作即可

Recyclerview 使用
---
为了能更敏捷高性能的使用列表，提高开发效率，为Recyclerview增加了databinding的特性，且为了提供高稳定性和更丰富的扩展功能，引入了[BRV](https://liangjingkanji.github.io/BRV/index.html)作为封装底座，具体用法如下：

### 如何创建一个列表？
项目封装后的使用非常简单，只需要三个步骤。
#### Step 1 创建你要显示的DataList和ItemList
    //数据集合
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>();
 
    //itemType集合
    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item);
                }
            });
#### Step 2 创建你的ItemList需要绑定的Model并指定对应的ItemView
注意：Recyclerview绑定的数据必须要继承BindModel

    //创建Model
    class A extends BindModel

    //绑定itemView 方法一
    A a = new A();
    a.setItemType(0);

    //方法二
    class A extends BindModel {
        @Override
        public int getItemType() {
            return 0;
        }
     }

    //填充数据
    ArrayList<BindModel> modelList = new ArrayList<>();
    A a = new A();
    a.setItemType(0);
    B b = new B();
    b.setItemType(1);
    modelList.add(a);
    modelList.add(b);
    datas.setValue(modelList);

这里的 “0” 对应的是itemType的索引

#### Step 3 在XML中将参数绑定
    //绑定数据
     <androidx.recyclerview.widget.RecyclerView
                android:layout_width="0dp"
                android:layout_height="0dp"
                app:itemData="@{model.datas}"
                itemViewType="@{model.itemType}"
                android:layout_marginStart="8dp"
                android:layout_marginEnd="8dp"
                android:layout_marginTop="5dp"
                app:layout_constraintTop_toBottomOf="@id/layout_toolbar"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintVertical_bias="0"/>

### Item如何使用绑定的数据？
使用databinding声明要绑定的数据类
注意：name必须是“m"，这是我在项目中配置的统一绑定名称，如果用其他名称会无法映射

    <layout>
    <data>
        <variable
            name="m"
            type="com.example.myapplication.ui.home.A" />
    </data>
    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:id="@+id/item_tv"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{m.textData}"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            />
    </androidx.constraintlayout.widget.ConstraintLayout>
    </layout>

### 更新列表数据
只需要操作DataList即可更新数据

    public class A extends BindModel {
    public ObservableField<String> name = new ObservableField<>();
    public String id = new String();
    }
    A a = new A();
    //更新单个item数据
    //绑定属性
    a.name.set("update");
    //普通属性
    a.id = "update"
    a.notifyChange();

    //增加或删除item
    ArrayList<BindModel> models = datas.getValue();
    models.remove(0);
    models.add(new A());
    datas.set(models);

    //全部数据刷新
    datas.ste(new ArrayList());
### 多类型列表
对于多类型列表使用也很简单，我们只需要增加ItemType即可

    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item);
                    add(R.layout.item2);
                }
            });
    ArrayList<BindModel> modelList = new ArrayList<>();
    A a = new A();
    a.setItemType(0);
    B b = new B();
    b.setItemType(1);
    modelList.add(a);
    modelList.add(b);
    datas.setValue(modelList);

### 点击监听
创建监听

    public final BaseDatabindingAdapter.onBindListener onBindListener = new BaseDatabindingAdapter.onBindListener() {

        @Override
        public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder, @NonNull View view, int itemViewType) {

        }

        @Override
        public void onItemClick(int modelPosition, int layoutPosition, int itemViewType) {
            if (itemViewType == R.layout.item_dividendagrt_send) {
                A bindModel = (A) bindModels.get(layoutPosition);
            }
        }

    };

注意：尽量少的使用onBind，这里的回调会在onBindViewHolder()函数调用

绑定监听

    <androidx.recyclerview.widget.RecyclerView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:itemData="@{model.datas}"
    itemViewType="@{model.itemType}"
    onBindListener="@{model.onBindListener}"/>

当然，你也可以在Model中复写onBind()函数，但不建议，因为不符合MVVM的职责分配

    public class A extends BindModel {
    @Override
    public void onBind(@NonNull BindingAdapter.BindingViewHolder bindingViewHolder) {
        super.onBind(bindingViewHolder);
    }
    }

### 头布局/脚布局
头布局使用

    //继承BindHead接口即可
    class A extends BindModel implements BindHead

脚布局使用

    //继承BindFooter接口即可
    class A extends BindModel implements BindFooter

### 头布局悬停
    class A extends BindModel implements BindHead {
    @Override
    public boolean getItemHover() {
        return true;
    }

    @Override
    public void setItemHover(boolean b) {
    }
    }

### 分割线
    <androidx.recyclerview.widget.RecyclerView
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:itemData="@{model.datas}"
            app:itemViewType="@{model.itemType}"
            onBindListener="@{model.onBindListener}"
            app:dividerDrawableId="@{R.drawable.divider_horizontal}"/>


### 自定义layoutManager

    public final RecyclerView.LayoutManager layoutManager = new FlexboxLayoutManager(getApplication());

    <androidx.recyclerview.widget.RecyclerView
    android:layout_width="0dp"
    android:layout_height="0dp"
    app:itemData="@{model.datas}"
    itemViewType="@{model.itemType}"
    layoutManager="@{model.layoutManager}"
    onBindListener="@{model.onBindListener}"/>
