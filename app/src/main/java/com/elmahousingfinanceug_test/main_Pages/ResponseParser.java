package com.elmahousingfinanceug_test.main_Pages;

import java.util.ArrayList;
import java.util.List;

public class ResponseParser {
    public static List<Transaction> parseToTransactions(String response) {
        List<Transaction> transactions = new ArrayList<>();

        String[] transactionsData = response.split("~");
        for (String transactionData : transactionsData) {
            String[] fields = transactionData.split("\\|");
            if (fields.length >= 20) {
                Transaction transaction = new Transaction();
                transaction.setReferenceNumber(fields[2]);
                transaction.setAccountNumber(fields[4]);
                transaction.setTransactionType(fields[8]);
                transaction.setTrxDate(fields[16]);
                transaction.setModuleDesc(fields[24]);
                transaction.setAmount(fields[12]);
                transactions.add(transaction);
            }
        }

        return transactions;
    }
}


