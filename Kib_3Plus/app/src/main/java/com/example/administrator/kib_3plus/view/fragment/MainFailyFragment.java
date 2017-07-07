package com.example.administrator.kib_3plus.view.fragment;

import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kib_3plus.Utils.DialogUtil;
import com.example.administrator.kib_3plus.Utils.LogUtils;
import com.example.administrator.kib_3plus.PublicData;
import com.example.administrator.kib_3plus.R;
import com.example.administrator.kib_3plus.Utils.BluetoothUtils;
import com.example.administrator.kib_3plus.Utils.TimeUtils;
import com.example.administrator.kib_3plus.ui.DialogFragment.MainDeleteDialogFragment;
import com.example.administrator.kib_3plus.view.fragment.Adapter.HomeAdapter;
import com.example.administrator.kib_3plus.view.fragment.base.BaseFragment;
import com.example.administrator.kib_3plus.view.fragment.interfaces.MyItemClickListener;
import com.example.administrator.kib_3plus.view.manage.ContentViewManage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.appscomm.bluetooth.mode.SportBTL28T;
import cn.appscomm.db.mode.ChildInfoDB;
import cn.appscomm.db.mode.SpoetL28TDB;
import cn.appscomm.presenter.implement.PBluetooth;
import cn.appscomm.presenter.implement.PDB;
import cn.appscomm.presenter.interfaces.PVBluetoothCallback;

import static com.example.administrator.kib_3plus.R.mipmap.bear;
import static com.example.administrator.kib_3plus.Utils.BluetoothUtils.checkGPSPermission;

/**
 * Created by cui on 2017/7/1.
 */

public class MainFailyFragment extends BaseFragment implements MyItemClickListener {

    private RecyclerView main_family_item_rl;
    private SwipeRefreshLayout main_family_sr;
    private TextView main_family_add_tv,main_family_edit_tv;
    private static MainFailyFragment mMainFailyFragment;
    public static String MAIN_TAG="Main";
    MainDeleteDialogFragment mMainDeleteDialogFragment;
    HomeAdapter mAdapter;
    private SpoetL28TDB deleteSpoetL28TDB;
    List<SpoetL28TDB> dataList= new ArrayList<>();

    List<ChildInfoDB> childDataList = new ArrayList<>();

    private boolean isEdit=false;

    public static MainFailyFragment getInstance(){
        if(mMainFailyFragment==null){
            mMainFailyFragment=new MainFailyFragment();
        }
        return mMainFailyFragment;
    }


    @Override
    public int getCreateViewLayoutId() {
        return R.layout.main_family_layout;
    }

    @Override
    public void findView(View inflateView, Bundle savedInstanceState) {
        super.findView(inflateView, savedInstanceState);
        main_family_item_rl= findViewById(R.id.main_family_item_rl);
        main_family_add_tv= findViewById(R.id.main_family_add_tv);
        main_family_edit_tv= findViewById(R.id.main_family_edit_tv);
        main_family_sr= findViewById(R.id.main_family_sr);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtils.i("initdata");
        childDataList= PDB.INSTANCE.getAllChildInfo();
        dataList.clear();
        String date=TimeUtils.getInstance().getTimeType("yyyy-MM-dd");
        LogUtils.i("date"+date);
        LogUtils.i("childDataList.size="+childDataList.size());
        for(ChildInfoDB childInfoDB : childDataList){
            LogUtils.i("childInfoDB="+childInfoDB.getMac());
            SpoetL28TDB spoetL28TDB= PDB.INSTANCE.getSportL28T(date,childInfoDB.getId());
            if(spoetL28TDB!=null){

                dataList.add(spoetL28TDB);
            }else{
               long time= TimeUtils.getInstance().getTime(date,"yyyy-MM-dd");
                spoetL28TDB=new SpoetL28TDB(childInfoDB.getId(),childInfoDB.getName(),childInfoDB.getMac(),0,0,time,date);
                PDB.INSTANCE.addSportL28T(spoetL28TDB);
                dataList.add(spoetL28TDB);
            }
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        main_family_add_tv.setOnClickListener(this);
        main_family_edit_tv.setOnClickListener(this);
    }

    @Override
    public void initView(View inflateView, Bundle savedInstanceState) {
        super.initView(inflateView, savedInstanceState);
        LogUtils.i("initView");
        main_family_item_rl.setLayoutManager(new GridLayoutManager(getContext(),2));
        mAdapter = new HomeAdapter(getContext(),dataList);
        mAdapter.setOnItemClickListener(this);
        main_family_item_rl.setAdapter(mAdapter);
        main_family_sr.setColorSchemeColors(Color.RED, Color.BLUE);
        main_family_sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
//                new MoreArticleTask().execute();
//                LogUtils.i("55555555");
                synData();

            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(childDataList!=null&&childDataList.size()>0){
                synData();
        }
    }

    private void synData() {
        boolean isSyn=false;
        for(SpoetL28TDB spoetL28TDB :dataList){
            LogUtils.i("spoetL28TDB.getMac()="+spoetL28TDB.getMac());
            if(!SetUpNewDeviceFragment.NO_BIND.equals(spoetL28TDB.getMac())){
                if(BluetoothUtils.isOpenBle()&&BluetoothUtils.checkGPSPermission()&&BluetoothUtils.checkGPSStatus(true)){
                    if(!PBluetooth.INSTANCE.isConnect(spoetL28TDB.getMac())){
                        PBluetooth.INSTANCE.connect(spoetL28TDB.getMac(),false,false);
                    }
                    BluetoothUtils.getSleepState(mPvBluetoothCallback,spoetL28TDB.getMac());
                    isSyn=true;
                }else{
                    //                    showToast("请打开蓝牙");

                }

            }
        }
        if(!isSyn){
            main_family_sr.setRefreshing(false);
        }
    }
    PVBluetoothCallback mPvBluetoothCallback=new PVBluetoothCallback() {
        @Override
        public void onSuccess(Object[] objects, int len, int dataType, String mac, BluetoothCommandType bluetoothCommandType) {
            LogUtils.i("len="+len);
            LogUtils.i("dataType="+dataType);
            LogUtils.i("mac="+mac);
            LogUtils.i("bluetoothCommandType="+bluetoothCommandType);
            if(objects!=null){
                LogUtils.i("objects="+objects[0].toString());

            }
            switch (bluetoothCommandType){
                case L28T_GET_SLEEP_MODEL:
                    LogUtils.i("获取L28T睡眠状态成功");
                    if((int)objects[0]==1){
                        PublicData.sleepState=true;
                    }else{
                        PublicData.sleepState=false;
                        BluetoothUtils.setDel(mPvBluetoothCallback,mac);

                    }
                    break;
                case L28T_SET_DEL:
                    LogUtils.i("设置手动删除成功");
                    BluetoothUtils.getStepTotal(mPvBluetoothCallback,mac);

                    break;
                case L28T_GET_STEP_COUNT:
                    LogUtils.i("获取总数成功");
                    LogUtils.i("objects="+objects[0].toString());
                    LogUtils.i("objects="+(int)objects[0]);
                    if((int)objects[0]>0){
                        BluetoothUtils.getStepData(mPvBluetoothCallback,mac,(int)objects[0]);

                    }else{
                        BluetoothUtils.setTime(mPvBluetoothCallback,mac);
                    }

                    break;
                case L28T_GET_STEP_DATA:
                    LogUtils.i("运动详细数据成功");

                    LinkedList<SportBTL28T> sportBTL28Ts=(LinkedList<SportBTL28T>)objects[0];
                    saveStepData(sportBTL28Ts,mac);
                    BluetoothUtils.delStepData(mPvBluetoothCallback,mac);
                    break;
                case L28T_DELETE_STEP_DATA:
                    LogUtils.i("删除运动数据成功");
                    BluetoothUtils.setTime(mPvBluetoothCallback,mac);
                    break;
                case L28T_BIND_TIME:
                    LogUtils.i("设置时间成功");
                    main_family_sr.setRefreshing(false);
                    break;
            }
        }

        @Override
        public void onFail(String mac, BluetoothCommandType bluetoothCommandType) {
            LogUtils.i("mac="+mac);
            LogUtils.i("bluetoothCommandType="+bluetoothCommandType);
            PBluetooth.INSTANCE.clearSendCommand(mac);
            main_family_sr.setRefreshing(false);

        }
    };

    private void saveStepData(LinkedList<SportBTL28T> sportBTL28Ts ,String mac) {
        for(SportBTL28T sportBTL28T:sportBTL28Ts){
            LogUtils.i("sportBTL28T.timeStamp="+sportBTL28T.timeStamp);
           String date= TimeUtils.getInstance().getStrTime(sportBTL28T.timeStamp,"yyyy-MM-dd");
            LogUtils.i("date="+date);
            SpoetL28TDB spoetL28TDB= PDB.INSTANCE.getSportL28T(date,mac);
            if(spoetL28TDB==null){
                for(ChildInfoDB childInfoDB:childDataList){
                    if(childInfoDB.getMac().equals(mac)){
                        SpoetL28TDB spoetL28TDB1=  new SpoetL28TDB(childInfoDB.getId()
                                ,childInfoDB.getName()
                                ,mac
                                ,sportBTL28T.step
                                ,sportBTL28T.calories
                                ,sportBTL28T.timeStamp
                                ,date);
                        LogUtils.i("spoetL28TDB1="+spoetL28TDB1.toString());
                        PDB.INSTANCE.addSportL28T(spoetL28TDB1);
                    }
                }
            }else{
                int activity=spoetL28TDB.getActivity()+sportBTL28T.step;
                int chores=spoetL28TDB.getChores()+sportBTL28T.calories;
                ContentValues values= new ContentValues();
                values.put("activity", activity);
                values.put("chores", chores);
                PDB.INSTANCE.updateAllSportL28T(values,spoetL28TDB.getuId());
            }
        }
        updetaView();


    }

    private void updetaView() {
        initData();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_member_delete_iv:
                LogUtils.i("main_member_delete_iv");

                break;
            case R.id.main_family_edit_tv:
                isEdit=!isEdit;
                mAdapter.setEdit(isEdit);
                LogUtils.i("main_family_edit_tv");
                break;
            case R.id.main_family_add_tv:
                LogUtils.i("main_family_add_tv");
                Bundle bundle=new Bundle();
                bundle.putString(MAIN_TAG,MAIN_TAG);
                ContentViewManage.getInstance().setFragmentType(ContentViewManage.ADD_MEMBER_FRAGMENT,bundle);
                break;
            case R.id.delete_dialog_cancel_bt:
                LogUtils.i("delete_dialog_cancel_bt");
                if(mMainDeleteDialogFragment!=null){
                    mMainDeleteDialogFragment.dismiss();
                }
                isEdit=!isEdit;
                mAdapter.setEdit(isEdit);
                break;
            case R.id.delete_dialog_confirm_bt:
                LogUtils.i("delete_dialog_confirm_bt");
                if(mMainDeleteDialogFragment!=null){
                    mMainDeleteDialogFragment.dismiss();
                }
                dataList.remove(deleteSpoetL28TDB);
                PBluetooth.INSTANCE.disConnect(deleteSpoetL28TDB.getMac());
                PDB.INSTANCE.deleteChildInfo(deleteSpoetL28TDB.getId());
                isEdit=!isEdit;
                mAdapter.setEdit(isEdit);
                break;
            case R.id.delete_dialog_delete_iv:
                LogUtils.i("delete_dialog_delete_iv");
                if(mMainDeleteDialogFragment!=null){
                    mMainDeleteDialogFragment.dismiss();
                }
                isEdit=!isEdit;
                mAdapter.setEdit(isEdit);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int postion) {
        LogUtils.i("postion="+postion);
        switch (view.getId()){
            case R.id.main_member_delete_iv:
                if(isEdit){
                    //family/child/delete
                     deleteSpoetL28TDB=  dataList.get(postion);
                    if(deleteSpoetL28TDB!=null){
                        mMainDeleteDialogFragment=MainDeleteDialogFragment.newInstance(deleteSpoetL28TDB.getName(),this);
                        mMainDeleteDialogFragment.show(getChildFragmentManager(),"MainDeleteDialogFragment");
                    }

                }
                break;
            case R.id.main_member_root_view:
                LogUtils.i("main_member_root_view=");

                break;
        }


//        synData();
    }
}
