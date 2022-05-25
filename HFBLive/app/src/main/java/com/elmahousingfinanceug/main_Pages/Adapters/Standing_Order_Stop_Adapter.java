package com.elmahousingfinanceug.main_Pages.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.main_Pages.Standing_Orders;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;

import java.util.ArrayList;

public class Standing_Order_Stop_Adapter extends RecyclerView.Adapter<Standing_Order_Stop_Adapter.ViewHolder> {
    private ArrayList<String> mDataSet;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                orderName,
                payto,
                startDate,
                freq,
                ifNb,
                amount,
                sourceAccount;

        ViewHolder(View v) {
            super(v);
            orderName = v.findViewById(R.id.ordrNm);
            payto = v.findViewById(R.id.payto);
            startDate = v.findViewById(R.id.startDate);
            freq = v.findViewById(R.id.freq);
            ifNb = v.findViewById(R.id.ifnB);
            sourceAccount = v.findViewById(R.id.sourceAccount);
            amount = v.findViewById(R.id.amount);
        }
    }

    public void add(int position, String item) {
        mDataSet.add(position, item);
        notifyItemInserted(position);
    }

    public Standing_Order_Stop_Adapter(ArrayList<String> myDataSet) {
        mDataSet = myDataSet;
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @NonNull
    @Override
    public Standing_Order_Stop_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_standing_order, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = v.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String data = mDataSet.get(position);
        String[] splits = data.split("\\|");
        try {
            holder.orderName.setText(splits[0]);
            holder.payto.setText(splits[1]);
            holder.startDate.setText(splits[2]);
            holder.freq.setText(splits[3]);
            holder.ifNb.setText(splits[4]);
            holder.amount.setText(splits[5]);
            holder.sourceAccount.setText(splits[6]);

            setScaleAnimation(holder.itemView);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((Standing_Orders)context).gDialog = new Dialog(v.getContext());
                    //noinspection ConstantConditions
                    ((Standing_Orders)context).gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ((Standing_Orders)context).gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    ((Standing_Orders)context).gDialog.setContentView(R.layout.dialog_confirm);
                    final TextView txtMessage = ((Standing_Orders)context).gDialog.findViewById(R.id.dialog_message);
                    final TextView noBTN = ((Standing_Orders)context).gDialog.findViewById(R.id.noBTN);
                    final TextView yesBTN = ((Standing_Orders)context).gDialog.findViewById(R.id.yesBTN);
                    txtMessage.setText(String.format("%s %s ?",
                            v.getContext().getString(R.string.sureDeleteStOrd),
                            holder.orderName.getText().toString().trim()));
                    yesBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((Standing_Orders)context).quest = (
                                    "FORMID:O-StandingOrderDelete:"+
                                            "ORDERNAME:" + holder.orderName.getText().toString().trim() + ":" +
                                            "MERCHANTNAME:TRANSFER:"
                            );
                            //((Standing_Orders)context).am.connectOldTwo(context.getString(R.string.processingReq),((Standing_Orders)context).quest, ((Standing_Orders)context),"");
                            ((Standing_Orders)context).am.get((ResponseListener) context, ((Standing_Orders)context).quest, context.getString(R.string.processingReq),"");
                            ((Standing_Orders)context).gDialog.dismiss();
                        }
                    });
                    noBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((Standing_Orders)context).gDialog.dismiss();
                        }
                    });
                    ((Standing_Orders)context).gDialog.show();
                    return false;
                }
            });
        } catch (Exception vH){
            ((Standing_Orders)context).am.LogThis("ViewHolderError ► "+vH.getMessage());
            ((Standing_Orders)context).error();
            ((Standing_Orders)context).am.myDialog(context,context.getString(R.string.alert),context.getString(R.string.unexpectedError));
        }
    }
}