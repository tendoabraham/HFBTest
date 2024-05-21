package com.elmahousingfinanceug_test.main_Pages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elmahousingfinanceug_test.R;

import java.text.DecimalFormat;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.bind(transaction, position);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView referenceNumberTextView;
//        private TextView accountNumberTextView;
        private TextView transactionTypeTextView;
        private TextView trxDateTextView;
        private TextView moduleDescTextView;
        private TextView amountTextView;
        private TextView transactionNumberTextView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionNumberTextView = itemView.findViewById(R.id.text_transaction_number);
            referenceNumberTextView = itemView.findViewById(R.id.text_reference_number);
//            accountNumberTextView = itemView.findViewById(R.id.text_account_number);
            transactionTypeTextView = itemView.findViewById(R.id.text_transaction_type);
            trxDateTextView = itemView.findViewById(R.id.text_trx_date);
            moduleDescTextView = itemView.findViewById(R.id.text_module_desc);
            amountTextView = itemView.findViewById(R.id.text_module_amount);
        }

        public void bind(Transaction transaction, int position) {
            String transactionTypeText = transaction.getTransactionType().equals("D") ? "DEBIT" : "CREDIT";
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            // Format the number
            String formattedNumber = decimalFormat.format(Double.parseDouble(transaction.getAmount()));

            transactionNumberTextView.setText("Transaction " + (position + 1));
            referenceNumberTextView.setText(transaction.getReferenceNumber());
//            accountNumberTextView.setText(transaction.getAccountNumber());
            transactionTypeTextView.setText(transactionTypeText);
            trxDateTextView.setText(transaction.getTrxDate());
            moduleDescTextView.setText(transaction.getModuleDesc());
            amountTextView.setText("UGX " + formattedNumber);
        }
    }
}

