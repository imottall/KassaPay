package com.avans.easypaykassa.SQLite;

import com.avans.easypaykassa.DomainModel.Balance;

import java.util.ArrayList;

/**
 * Created by Bart on 3-5-2017.
 */

public interface BalanceDAO {

    ArrayList<Balance> selectData();
    void insertData (Balance balance);
}
