package com.elmahousingfinanceug_test.main_Pages.Adapters;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.elmahousingfinanceug_test.main_Pages.ATM_Locations;
import com.elmahousingfinanceug_test.main_Pages.Branches_Page;
import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.ZAtmBranch;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;

import java.util.ArrayList;

public class Branch_List_Adapter extends RecyclerView.Adapter<Branch_List_Adapter.ViewHolder> {
    private final ArrayList<String> branch_Set;
    private AllMethods am;
    private Context context;
    private double lat, longI;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView branchName,branchLoc,latt,lonn;

        ViewHolder(View v) {
            super(v);
            branchName = v.findViewById(R.id.brName);
            branchLoc = v.findViewById(R.id.brLoc);
            latt = v.findViewById(R.id.latitude);
            lonn = v.findViewById(R.id.longitude);
        }
    }

    public void add(int position, String item) {
        branch_Set.add(position, item);
        notifyItemInserted(position);
    }

    public Branch_List_Adapter(ArrayList<String> myDataset) {
        branch_Set = myDataset;
    }

    @NonNull
    @Override
    public Branch_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_branch, parent, false);
        ViewHolder vh = new ViewHolder(v);
        am = new AllMethods(v.getContext());
        context = v.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String data = branch_Set.get(position);
        final String[] splits = data.split("\\|");

        holder.branchName.setText(splits[0]);
        holder.branchLoc.setText(splits[1]);
        holder.latt.setText(splits[2]);
        holder.lonn.setText(splits[3]);

        setScaleAnimation(holder.itemView);

        holder.itemView.setOnClickListener((View.OnClickListener) v -> {
            lat = Double.parseDouble(holder.latt.getText().toString());
            longI = Double.parseDouble(holder.lonn.getText().toString());

            if(am.getViewBraches()) {
                ((Branches_Page)context).onGoToNew(lat, longI);
            } else if(am.getViewAgent()) {
                ((ZAtmBranch)context).onGoToNew(lat, longI);
            } else {
                ((ATM_Locations)context).onGoToNew(lat, longI);
            }
        });

        /*holder.itemView.setOnLongClickListener((View.OnLongClickListener) view -> {
            if(am.getViewAgent()) {
                try {
                    ((ZAtmBranch)context).onGoToNew(lat, longI);
                    if (ContextCompat.checkSelfPermission(((ZAtmBranch) context), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        ((ZAtmBranch)context).startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+PhoneNumberUtils.stripSeparators(holder.branchLoc.getText().toString().trim().replace(" ","")))));
                    }
                } catch (ActivityNotFoundException aE) {
                    am.LogThis("Call failed : "+aE.getMessage());
                }
            }
            return false;
        });*/
    }

    @Override
    public int getItemCount() {
        return branch_Set.size();
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }
}
