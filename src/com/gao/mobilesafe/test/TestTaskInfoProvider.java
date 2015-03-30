
package com.gao.mobilesafe.test;

import android.test.AndroidTestCase;

import com.gao.mobilesafe.db.domain.TaskInfo;
import com.gao.mobilesafe.engine.TaskInfoProvider;

import java.util.List;

public class TestTaskInfoProvider extends AndroidTestCase {
    public void testGetTaskInfos() throws Exception {
        List<TaskInfo> infos = TaskInfoProvider.getTaskInfos(getContext());
        for (TaskInfo info : infos) {
            System.out.println(info.toString());
        }
    }
}
