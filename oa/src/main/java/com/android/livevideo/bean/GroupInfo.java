package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Gool
 */
public class GroupInfo implements Serializable {

    private int id;
    private int orderBy;
    private String title;
    private boolean isAllChecked;
    private List<ChildrenBean> children;

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAllChecked() {
        return isAllChecked;
    }

    public void setAllChecked(boolean allChecked) {
        isAllChecked = allChecked;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public GroupInfo(int id, String title, boolean isAllChecked, List<ChildrenBean> children,int orderBy) {
        this.id = id;
        this.title = title;
        this.orderBy = orderBy;
        this.isAllChecked = isAllChecked;
        this.children = children;
    }

    public static class ChildrenBean implements Serializable {
        private int id;
        private String employeeName;
        private boolean isChildChecked;

        public ChildrenBean(int id, String employeeName, boolean isChildChecked) {
            this.id = id;
            this.employeeName = employeeName;
            this.isChildChecked = isChildChecked;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public boolean isChildChecked() {
            return isChildChecked;
        }

        public void setChildChecked(boolean childChecked) {
            isChildChecked = childChecked;
        }
    }
}
