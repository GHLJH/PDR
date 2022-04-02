package cn.cpy.library.pdrlibrary;

import android.content.Context;

import cn.cpy.library.pdrlibrary.Sensor.OrientSensor;
import cn.cpy.library.pdrlibrary.Sensor.StepSensorAcceleration;
import cn.cpy.library.pdrlibrary.Sensor.StepSensorBase;

public class PDR implements StepSensorBase.StepCallBack, OrientSensor.OrientCallBack {

    private Context context;
    private StepSensorBase mStepSensor;
    private OrientSensor mOrientSensor;
    private PDRCallBack pdrCallBack;
    private boolean isstep = false;
    private boolean isorient = false;
    private int mStepLen = 50;
    private int errCode = 0;
    private int stepNum = 0;
    private float orient = 0;
    private float[] coord = {0,0};

    public interface PDRCallBack{
        //PDR回调
        void PDRChange();
    }

    //北为y轴正方向 东为x轴正方向 步长固定50
    @Override
    public void Step(int stepNum){
        this.stepNum=stepNum;
        coord[0]+=mStepLen*Math.sin(orient);
        coord[1]+=mStepLen*Math.cos(orient);
        isstep = true;
        pdrCallBack.PDRChange();
        isstep = false;
    }

    @Override
    public void Orient(float orient){
        this.orient=orient;
        isorient = true;
        pdrCallBack.PDRChange();
        isorient = false;
    }

    public PDR(Context context,PDRCallBack pdrCallBack){
        this.context=context;
        this.pdrCallBack=pdrCallBack;
    }

    public boolean registerSensor(){
        boolean flag = true;
        mOrientSensor = new OrientSensor(context,this);
        mStepSensor = new StepSensorAcceleration(context,this);
        if(!mOrientSensor.registerOrient()){
            errCode += 1;
            flag = false;
        }
        if(!mStepSensor.registerStep()){
            errCode += 2;
            flag = false;
        }
        return flag;
    }

    // 0 no err
    public int getErrCode(){
        return errCode;
    }

    public void unregisterSensor(){
        mStepSensor.unregisterStep();
        mOrientSensor.unregisterOrient();
    }

    public int getStepNum() {
        return stepNum;
    }

    //北为0 顺时针
    public int getOrient() {
        int orient = (int)Math.toDegrees(this.orient);
        if(orient < 0){
            orient += 360;
        }
        return orient;
    }

    public float[] getCoord() {
        return coord;
    }

    public void initCoord() {
        coord[0] = 0;
        coord[1] = 0;
    }

    public void setCoord(float[] coord){
        this.coord[0]=coord[0];
        this.coord[1]=coord[1];
    }

    public boolean isStep(){
        return isstep;
    }

    public boolean isOrient() {
        return isorient;
    }
}