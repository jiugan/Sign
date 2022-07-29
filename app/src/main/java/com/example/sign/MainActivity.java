package com.example.sign;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sign.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    //监听返回事件
    //两次滑动返回桌面，不结束当前activity
    //声明一个全局布尔值,默认为false
    public boolean isexit = false;
    //重写onKeyDown方法,
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
            //调用exit()方法
            exit();
        }
        return false;
    }

    private void exit() {
        Timer timer;//声明一个定时器
        if (!isexit) {  //如果isExit为false,执行下面代码
            isexit = true;  //改变值为true
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            timer = new Timer();  //得到定时器对象
            //执行定时任务,两秒内如果没有再次按下,把isExit值恢复为false,再次按下返回键时依然会进入if这段代码
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isexit = false;
                }
            }, 2000);
        } else {//如果两秒内再次按下了返回键,这时isExit的值已经在第一次按下时赋值为true了,因此不会进入if后的代码,直接执行下面的代码
            finish();
        }
    }

}