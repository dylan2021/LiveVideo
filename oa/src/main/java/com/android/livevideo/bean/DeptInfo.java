package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Gool Lee
 */
public class DeptInfo implements Serializable {

    private int id;
    private int parentId;
    private String title;
    private int orderBy;
    private String deptDesc;
    private String managerName;
    private String deptName;
    private int employeeCount;
    private int total;
    private List<ChildrenBeanX> children;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public String getDeptDesc() {
        return deptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ChildrenBeanX> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBeanX> children) {
        this.children = children;
    }

    public static class ChildrenBeanX implements Serializable{
        /**
         * id : 3
         * parentId : 1
         * title : 技术部
         * orderBy : 2
         * children : [{"id":11,"parentId":3,"title":"公务部","orderBy":1,"deptDesc":"技术-公务部","managerName":"李兰修","employeeCount":1,"total":1},{"id":10,"parentId":3,"title":"品管部","orderBy":2,"deptDesc":"技术-品管部","managerName":"李兰修","employeeCount":0,"total":0}]
         * deptDesc : 开发二组
         * managerName : 超级管理员
         * employeeCount : 1
         * total : 2
         */

        private int id;
        private int parentId;
        private String title;
        private int orderBy;
        private String deptDesc;
        private String managerName;
        private int employeeCount;
        private int total;
        private List<ChildrenBean> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(int orderBy) {
            this.orderBy = orderBy;
        }

        public String getDeptDesc() {
            return deptDesc;
        }

        public void setDeptDesc(String deptDesc) {
            this.deptDesc = deptDesc;
        }

        public String getManagerName() {
            return managerName;
        }

        public void setManagerName(String managerName) {
            this.managerName = managerName;
        }

        public int getEmployeeCount() {
            return employeeCount;
        }

        public void setEmployeeCount(int employeeCount) {
            this.employeeCount = employeeCount;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

        public static class ChildrenBean implements Serializable {
            /**
             * id : 11
             * parentId : 3
             * title : 公务部
             * orderBy : 1
             * deptDesc : 技术-公务部
             * managerName : 李兰修
             * employeeCount : 1
             * total : 1
             */

            private int id;
            private int parentId;
            private String title;
            private int orderBy;
            private String deptDesc;
            private String managerName;
            private int employeeCount;
            private int total;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getOrderBy() {
                return orderBy;
            }

            public void setOrderBy(int orderBy) {
                this.orderBy = orderBy;
            }

            public String getDeptDesc() {
                return deptDesc;
            }

            public void setDeptDesc(String deptDesc) {
                this.deptDesc = deptDesc;
            }

            public String getManagerName() {
                return managerName;
            }

            public void setManagerName(String managerName) {
                this.managerName = managerName;
            }

            public int getEmployeeCount() {
                return employeeCount;
            }

            public void setEmployeeCount(int employeeCount) {
                this.employeeCount = employeeCount;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }
    }
}
