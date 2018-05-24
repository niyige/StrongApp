package com.oyy.strong.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.oyy.strong.view.WaitingDialog;

/**
 * 所有Fragment请继承此类
 *
 * @author stanleyding
 */
public  class BaseV4Fragment extends Fragment{

    public Activity mContext;
    private WaitingDialog mDialog;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

    }

    public void showWaitingDialog() {
        if (mDialog == null) {
            mDialog = new WaitingDialog(mContext);
        }
        if (mDialog.isShowing()) {
            mDialog.cancel();
        }
        mDialog.show();
    }

    protected void showWaitingDialog(String dialogContent) {
        if (mDialog == null) {
            mDialog = new WaitingDialog(mContext);
        }
        if (mDialog.isShowing()) {
            mDialog.cancel();
        }
        mDialog.setDialogContent(dialogContent);
        mDialog.show();
    }

    public void cancelWaitingDialog() {
        if (mDialog == null) {
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.cancel();
        }
    }




}
