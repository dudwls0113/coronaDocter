package com.softsquared.android.coronadoctor.src;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softsquared.android.coronadoctor.R;
import com.softsquared.android.coronadoctor.src.main.MoneyPeopleAdapter;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;

public class CustomMoneyDialog extends Dialog {

    private Context mContext;

    RecyclerView recyclerView;
    ArrayList<String> mArrayListPeople;
    private MoneyPeopleAdapter moneyPeopleAdapter;
    Button btnCancle;
    LinearLayout mLinearCredit;

    public CustomMoneyDialog(@NonNull Context context, ArrayList<String> arrayList) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.custom_money_dialog);
        mArrayListPeople = arrayList;

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(mContext, 4);

        recyclerView = findViewById(R.id.rv_money_people);

        moneyPeopleAdapter = new MoneyPeopleAdapter(mArrayListPeople, mContext);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moneyPeopleAdapter);

        btnCancle = findViewById(R.id.dialog_infect_select_tv_ok);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mLinearCredit = findViewById(R.id.dialog_money_credit);
        mLinearCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //클립보드 사용 코드
                ClipboardManager clipboardManager = (ClipboardManager)mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("credit","국민 94160200069814"); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);

                //복사가 되었다면 토스트메시지 노출
                Toast.makeText(mContext,"복사되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
