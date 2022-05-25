package com.elmahousingfinanceug.main_Pages.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elmahousingfinanceug.R;


public class AdapterKeyValue extends RecyclerView.Adapter<AdapterKeyValue.ViewHolder> {
    private final String[] mKeyZ, mValueZ;

    @Override
    public AdapterKeyValue.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_key_value, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterKeyValue.ViewHolder holder, final int position) {
        holder.keyS.setText(mKeyZ[position]);
        holder.valueS.setText(mValueZ[position]);
    }

    @Override
    public int getItemCount() {
        return mKeyZ.length;
    }

    public AdapterKeyValue(String[]showKeys, String[]showValues) {
        this.mKeyZ = showKeys;
        this.mValueZ = showValues;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView keyS, valueS;

        ViewHolder(View m) {
            super(m);
            keyS = m.findViewById(R.id.keyS);
            valueS = m.findViewById(R.id.valueS);
        }
    }
}
