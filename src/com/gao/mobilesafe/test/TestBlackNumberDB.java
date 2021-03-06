
package com.gao.mobilesafe.test;

import java.util.List;
import java.util.Random;

import com.gao.mobilesafe.db.BlackNumberDBOpenHelper;
import com.gao.mobilesafe.db.dao.BlackNumberDao;
import com.gao.mobilesafe.db.domain.BlackNumberInfo;

import android.test.AndroidTestCase;

public class TestBlackNumberDB extends AndroidTestCase {

    public void testCreateDB() throws Exception {
        BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
        helper.getWritableDatabase();
    }

    public void testAdd() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.add("110", "1");
    }

    public void testAll() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        long baseNumber = 1350000;
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            dao.add(String.valueOf(baseNumber + i), String.valueOf(random.nextInt(3) + 1));

        }
    }

    public void testFindAll() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        List<BlackNumberInfo> infos = dao.findAll();
        for (BlackNumberInfo blackNumberInfo : infos) {
            System.out.println(blackNumberInfo.toString());
        }
    }

    public void testDelete() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.delete("110");
    }

    public void testUpdate() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.update("110", "2");
    }

    public void testFind() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        boolean result = dao.find("110");
        assertEquals(true, result);
    }

}
