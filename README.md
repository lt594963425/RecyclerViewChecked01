# 怎么获取item的ViewHolder #
正确：
```
recyclerView.getChildViewHolder(view);
```
错误
```
(MyHolder) view.getTag();
```

![这里写图片描述](http://img.blog.csdn.net/20170316000539816?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc3MxMTY4ODA1MjE5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
# 单选 #
单选都是通过标志位实现的，就是给bean添加Boolean属性，标明是否选中。
```
public class Person {
    private String name;
    private int age;
    private boolean checked;//是否选中
	...
}
```
## 单选：按钮 ##
### 效果图：  ###
![这里写图片描述](http://img.blog.csdn.net/20170315234732104?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc3MxMTY4ODA1MjE5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
### `RecyclerView`在xml中的布局 ###
```
<android.support.v7.widget.RecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
### `item_single_choice` ###
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="60dp"
              android:gravity="center_vertical"
              android:orientation="horizontal">

    <RadioButton
        android:id="@+id/rb"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="20dp"
        android:orientation="vertical">

        <TextView
            android:id="@+id/tv1"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="tv1"/>

        <TextView
            android:id="@+id/tv2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="tv2"/>
    </LinearLayout>

</LinearLayout>
```
### `SingleChoiceRecyAdapter`中使用标志位 ###
```
@Override
public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
    MyHolder holder = (MyHolder) viewHolder;
    Person person = list.get(position);

    holder.rb1.setChecked(person.isChecked());
    holder.tv1.setText("姓名：" + person.getName());
    holder.tv2.setText("年龄：" + person.getAge());
    
    if (onItemClickListener != null) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });
    }
}
```
###  在item的点击事件中根据标识位改变按钮 ###
```
adapter.setOnItemClickListener(new SingleChoiceRecyAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "position=" + position);

        for (int i = 0; i < list.size(); i++) {
            if (i == position ) {
                list.get(i).setChecked(true);//必须选择一个
            } else {
                list.get(i).setChecked(false);
            }
        }
        checkedPosition = position;
        adapter.notifyDataSetChanged();
    }
});
```
### 获取选中的item的pisition ###
```
btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Log.d(TAG, "单选的position=" + checkedPosition);
        Toast.makeText(SingleChoiceActivity.this, ""+checkedPosition, Toast.LENGTH_SHORT).show();
    }
});
```

## 单选：item背景色 ##
###效果图###
![这里写图片描述](http://img.blog.csdn.net/20170315235307654?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc3MxMTY4ODA1MjE5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

改变item的背景色，需要重写item的根布局，并实现接口`Checkable`
### ChoiceItemLayout ###:
```
public class ChoiceItemLayout extends LinearLayout implements Checkable {

    private boolean mChecked;
    public ChoiceItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackgroundResource(checked? R.color.colorAccent : android.R.color.transparent);
    }

    @Override
    public boolean isChecked() {
        return true;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
```
### item_bg_choice.xml ###
`ChoiceItemLayout`作为根布局
```
<?xml version="1.0" encoding="utf-8"?>
<com.qihu.recyclerviewchoice.view.ChoiceItemLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="60dp"
    android:gravity="center_vertical"
    android:orientation="horizontal">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="20dp"
        android:orientation="vertical">

        <TextView
            android:id="@+id/tv1"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="tv1"/>

        <TextView
            android:id="@+id/tv2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="tv2"/>
    </LinearLayout>

</com.qihu.recyclerviewchoice.view.ChoiceItemLayout>
```
### 根据标志位实现背景色的变化 ###
```
@Override
public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
    MyHolder holder = (MyHolder) viewHolder;
	
    ChoiceItemLayout layout = (com.qihu.recyclerviewchoice.view.ChoiceItemLayout) holder.itemView;
    layout.setChecked(person.isChecked());
	...
}
```
### 在item的点击事件中根据标识位更新背景色 ###
```
adapter.setOnItemClickListener(new BgSingleChoiceRecyAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "position=" + position);

        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                list.get(i).setChecked(true);
            } else {
                list.get(i).setChecked(false);
            }
        }
        checkedPosition = position;
        adapter.notifyDataSetChanged();
    }
});
```
### 获取选中的item的position ###
```
btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Log.d(TAG, "单选=" + checkedPosition);
        Toast.makeText(BgSingleChoiceActivity.this, "" + checkedPosition, Toast.LENGTH_SHORT).show();
    }
});
```
## 单选：带有HeadView和FootView ##
![这里写图片描述](http://img.blog.csdn.net/20170315235221122?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc3MxMTY4ODA1MjE5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
加了头尾布局个一个，这就要求我们在改变item的标识位时注意正确的position
```
adapter.setOnItemClickListener(new SingleChoiceHeadFootAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "position=" + position);

        for (int i = 0; i < list.size(); i++) {
            if (i == position - adapter.getHeadViewCount()) {//注意
                list.get(i).setChecked(true);
            } else {
                list.get(i).setChecked(false);
            }
        }
        checkedPosition = position- adapter.getHeadViewCount();//注意
        adapter.notifyDataSetChanged();
    }
});
```
添加头尾布局也在本篇博客。
包含头尾的adapter
```
public class SingleChoiceHeadFootAdapter extends RecyclerView.Adapter {

    private View headView;
    private View footView;
    public static final int HEAD = 1;
    public static final int NORMAL = 2;
    public static final int FOOT = 3;
    public List<Person> list;

    public SingleChoiceHeadFootAdapter(List<Person> list) {
        this.list = list;
    }


    public void addHeadView(View headView) {
        this.headView = headView;
    }

    public void addFootView(View footView) {
        this.footView = footView;
    }

    public int getHeadViewCount() {
        return headView == null ? 0 : 1;
    }

    public int getFootViewCount() {
        return footView == null ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new MyHolder(headView);
        }
        if (viewType == FOOT) {
            return new MyHolder(footView);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_choice, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == HEAD) {
            return;
        }
        if (getItemViewType(position) == FOOT) {
            return;
        }

        MyHolder holder = (MyHolder) viewHolder;
        Person person = list.get(position - getHeadViewCount());

        holder.rb.setChecked(person.isChecked());
        holder.tv1.setText("姓名："+person.getName());
        holder.tv2.setText("年龄："+person.getAge());

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + getHeadViewCount() + getFootViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeadViewCount()) {
            return HEAD;
        }
        if (position >= list.size() + getHeadViewCount()) {
            return FOOT;
        }
        return NORMAL;
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        public RadioButton rb;
        public TextView tv1;
        public TextView tv2;

        public MyHolder(View itemView) {
            super(itemView);
            if (itemView == headView) {
                return;
            }
            if (itemView == footView) {
                return;
            }
            rb = (RadioButton) itemView.findViewById(R.id.rb);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
```
添加HeaderView和FooterView
```
View headView = LayoutInflater.from(this).inflate(R.layout.layout_header, recyclerView, false);
View footView = LayoutInflater.from(this).inflate(R.layout.layout_footer, recyclerView, false);
adapter.addHeadView(headView);
adapter.addFootView(footView);
```

# 多选 #
同样是通过标识位来实现的。
## 多选：框 ##
### 效果图 ###
![这里写图片描述](http://img.blog.csdn.net/20170315234831559?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc3MxMTY4ODA1MjE5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
### 根据标识位改变多选框的效果 ###
```
MulipleRecyAdapter.MyHolder holder = (MulipleRecyAdapter.MyHolder) recyclerView.getChildViewHolder(view);
holder.checkBox.toggle();
```
### 获取选中的item的position ###
```
if (!posiList.contains(position) && holder.checkBox.isChecked()) {
    posiList.add(position);
} else if (posiList.contains(position) || !holder.checkBox.isChecked()) {
    posiList.remove(posiList.indexOf(position));
}
```
完整就是
```
adapter.setOnItemClickListener(new MulipleRecyAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "position=" + position);

        MulipleRecyAdapter.MyHolder holder = (MulipleRecyAdapter.MyHolder) recyclerView.getChildViewHolder(view);
        holder.checkBox.toggle();
//                list.get(position).setChecked(holder.checkBox.isChecked());//不需要

        if (!posiList.contains(position) && holder.checkBox.isChecked()) {
            posiList.add(position);
        } else if (posiList.contains(position) || !holder.checkBox.isChecked()) {
            posiList.remove(posiList.indexOf(position));
        }
    }
});
```

## 多选：改变item背景色 ##
###效果图###
![这里写图片描述](http://img.blog.csdn.net/20170315235428239?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc3MxMTY4ODA1MjE5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
同单选一样，改变item的背景色，必须重写item的根布局，并实现接口`Checkable`
### ChoiceItemLayout ###
```
public class ChoiceItemLayout extends LinearLayout implements Checkable {

    private boolean mChecked;
    public ChoiceItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackgroundResource(checked? R.color.colorAccent : android.R.color.transparent);
    }

    @Override
    public boolean isChecked() {
        return true;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
```
### 在adapter的item的点击事件中改变背景色和保存选中的item的position ###
```
adapter.setOnItemClickListener(new BgMultipleChoiceRecyAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "position=" + position);

        BgMultipleChoiceRecyAdapter.MyHolder holder = (BgMultipleChoiceRecyAdapter.MyHolder) recyclerView.getChildViewHolder(view);
        ChoiceItemLayout itemView = (ChoiceItemLayout) holder.itemView;
        itemView.toggle();

        if (!posiList.contains(position ) && itemView.isChecked()) {
            posiList.add(position);
        } else if (posiList.contains(position) || !itemView.isChecked()) {
            posiList.remove(posiList.indexOf(position ));
        }
    }
});
```
### 取出选中的item的position ###
```
btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < posiList.size(); i++) {
            sb.append(posiList.get(i) + ",");
        }
        Log.d(TAG, "sb=" + sb.toString());
        Toast.makeText(BgMultipleChoiceActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
    }
});
```
### 注意：多选改变背景色不需要在adapter根据标识位的变化 ###
不需要标识位来改变背景色，而是通过在adapter的`setOnItemClickListener(...)`中`itemView.toggle()`来实现。
```
@Override
public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
    MyHolder holder = (MyHolder) viewHolder;
    Person person = list.get(position);

    holder.tv1.setText("姓名：" + person.getName());
    holder.tv2.setText("年龄：" + person.getAge());

    if (onItemClickListener != null) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });
    }
}
```

## 多选：带有HeaderView和FooterView ##
### 效果图 ###
![这里写图片描述](http://img.blog.csdn.net/20170315235515843?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc3MxMTY4ODA1MjE5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

主要是注意下标识位的position
```
adapter.setOnItemClickListener(new MultipleChoiceHeadFootAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "position=" + position);

        MultipleChoiceHeadFootAdapter.MyHolder holder = (MultipleChoiceHeadFootAdapter.MyHolder) recyclerView.getChildViewHolder(view);
        holder.checkBox.toggle();

        if (!posiList.contains(position - adapter.getHeadViewCount()) && holder.checkBox.isChecked()) {
            posiList.add(position - adapter.getHeadViewCount());
        } else if (posiList.contains(position - adapter.getHeadViewCount()) || !holder.checkBox.isChecked()) {
            posiList.remove(posiList.indexOf(position - adapter.getHeadViewCount()));
        }
    }
});
```
#Demo #
https://git.oschina.net/RecyclerView/RecyclerViewChecked01
apk下载地址：http://fir.im/gb3x
