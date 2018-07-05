package com.jjs.demo;

import java.util.List;

/**
 * 说明：
 * Created by jjs on 2018/7/2.
 */

public class EntityJ extends AJBaseEntity {

    /**
     * b : 2
     * info : 189604486546
     * data : {"dd":1}
     * obj : [{"asss":"222"},{"asss":"33333"}]
     */

    public int b;
    public String info;
    public DataEntity data;
    public List<ObjEntity> obj;

    public static class DataEntity {
        /**
         * dd : 1
         */

        public int dd;
    }

    public static class ObjEntity {
        /**
         * asss : 222
         */

        public String asss;
    }
}
