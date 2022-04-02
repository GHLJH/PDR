//example for PDR
package cn.cpy.example.pdrexample;

import cn.cpy.library.pdrlibrary.PDR;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements PDR.PDRCallBack {

    TextView tv_step;
    TextView tv_orient;
    TextView tv_coord;
    PDR pdr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_step = findViewById(R.id.tv_step);
        tv_orient = findViewById(R.id.tv_orient);
        tv_coord = findViewById(R.id.tv_coord);
        pdr = new PDR(this,this);
        pdr.registerSensor();
    }

    @Override
    public void PDRChange(){
        if(pdr.isOrient()){
            String str = "方向：" + pdr.getOrient();
            tv_orient.setText(str);
        }
        if(pdr.isStep()){
            String str = "步数：" + pdr.getStepNum();
            tv_step.setText(str);
            str = "坐标：" + pdr.getCoord()[0] + "," + pdr.getCoord()[1];
            tv_coord.setText(str);
        }
    }

    @Override

    protected void onDestroy(){
        super.onDestroy();
        pdr.unregisterSensor();
    }

}
