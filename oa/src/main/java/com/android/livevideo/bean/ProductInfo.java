package com.android.livevideo.bean;

import java.util.List;

/**
 * Gool Lee
 */
public class ProductInfo {

    private int id;
    private int canDelete;
    private String name;
    private String result;
    private String determination;
    private String specification;
    private List<ProductInfo> bizProductDetailList;

    public List<ProductInfo> getBizProductDetailList() {
        return bizProductDetailList;
    }

    public void setBizProductDetailList(List<ProductInfo> bizProductDetailList) {
        this.bizProductDetailList = bizProductDetailList;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public int getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(int canDelete) {
        this.canDelete = canDelete;
    }

    public String getDetermination() {
        return determination;
    }

    public void setDetermination(String determination) {
        this.determination = determination;
    }

    private String type;
    private StandardBean standard;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StandardBean getStandard() {
        return standard;
    }

    public void setStandard(StandardBean standard) {
        this.standard = standard;
    }

    public static class StandardBean {
        /**
         * remark :
         * startPoint : 10
         * endPoint : 999
         */

        private String remark;
        private int startPoint;
        private int endPoint;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStartPoint() {
            return startPoint;
        }

        public void setStartPoint(int startPoint) {
            this.startPoint = startPoint;
        }

        public int getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(int endPoint) {
            this.endPoint = endPoint;
        }
    }
}
