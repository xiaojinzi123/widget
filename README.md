# widget
common widget for Android


## how to use

edit your main build.gradle file

```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

add dependencies to your module build.gralde

```
compile 'com.github.xiaojinzi123:widget:v1.1.4.2'
```

## I will introduce all usage of weiget

### RelativeSizeTextView
#### use

```
<com.move.widget.RelativeSizeTextView
            android:id="@+id/tv"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:padding="5dp"
            android:text="我是正常文本20sp"
            android:textSize="20sp"
            app:endText="我是蓝色后置文本为正常大小的150%"
            app:end_proportion="1.5"
            app:end_text_color="#0000FF"
            app:start_proportion="0.8"
            app:startText="我是前置红色文本为正常大小的80%"
            app:start_text_color="#FF0000" />
```

#### result
![](./imgs/1.png)

### XFlowLayout
#### use

```
<com.move.widget.XFlowLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="100dp"
        android:background="#789"
        android:padding="10dp">

        <TextView
            android:layout_width="100dp"
            android:layout_height="40dp"
            android:background="@drawable/demo_bg1"
            android:gravity="center"
            android:text="第1个"
            android:textColor="#FFFFFF" />

        <TextView
            android:layout_width="150dp"
            android:layout_height="40dp"
            android:background="@drawable/demo_bg1"
            android:gravity="center"
            android:text="第2个"
            android:textColor="#FFFFFF" />

        <TextView
            android:layout_width="60dp"
            android:layout_height="40dp"
            android:background="@drawable/demo_bg1"
            android:gravity="center"
            android:text="第3个"
            android:textColor="#FFFFFF" />

        <TextView
            android:layout_width="150dp"
            android:layout_height="40dp"
            android:background="@drawable/demo_bg1"
            android:gravity="center"
            android:text="第4个"
            android:textColor="#FFFFFF" />

        <TextView
            android:layout_width="150dp"
            android:layout_height="40dp"
            android:background="@drawable/demo_bg1"
            android:gravity="center"
            android:text="第5个"
            android:textColor="#FFFFFF" />

        <TextView
            android:layout_width="110dp"
            android:layout_height="40dp"
            android:background="@drawable/demo_bg1"
            android:gravity="center"
            android:text="第6个"
            android:textColor="#FFFFFF" />

        <TextView
            android:layout_width="60dp"
            android:layout_height="40dp"
            android:background="@drawable/demo_bg1"
            android:gravity="center"
            android:text="第7个"
            android:textColor="#FFFFFF" />

        <TextView
            android:layout_width="110dp"
            android:layout_height="20dp"
            android:background="@drawable/demo_bg1"
            android:gravity="center"
            android:text="第8个"
            android:textColor="#FFFFFF" />

        <TextView
            android:layout_width="60dp"
            android:layout_height="wrap_content"
            android:background="@drawable/demo_bg1"
            android:gravity="center"
            android:text="第9个"
            android:textColor="#FFFFFF" />

    </com.move.widget.XFlowLayout>
```

#### result
![](./imgs/2.png)
