package com.softsquared.android.coronadoctor.src;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;

import com.softsquared.android.coronadoctor.R;


public abstract class BaseFragment extends Fragment {

    public ProgressDialog mProgressDialog;
    public AppCompatDialog progressDialog;
    public Activity activity;

    public void showCustomToast(final String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showProgressDialog(Activity activity) {
        if (mProgressDialog == null) {
            this.activity = activity;
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (!getActivity().isDestroyed() && activity!=null){
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public abstract void setComponentView(View v);
}

