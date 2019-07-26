package com.example.testproject.database.operators;

import java.util.List;

public interface OperationCompleteListener {
    void onListInserted(Long[] result);

    void onItemInserted(Long result);

    void onItemDeleted(int rowsDeleted);

    void onItemUpdated(int rowsDeleted);

    void onTableDeleted();

    <T> void onItemSearched(T object);

    <T> void onItemListSearched(List<T> list);

    <T> void onListConvertedToLocalType(List<T> list);
}
