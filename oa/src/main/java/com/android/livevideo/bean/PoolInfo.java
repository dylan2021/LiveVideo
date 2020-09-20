package com.android.livevideo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Dylan
 */
public class PoolInfo implements Serializable {
    private String id;
    private String carNumber;
    private String createTime;
    private String updateTime;
    private String poolNo;
    private String name;
    private String putInPoolDate;

    public String getPutInPoolDate() {
        return putInPoolDate;
    }

    public void setPutInPoolDate(String putInPoolDate) {
        this.putInPoolDate = putInPoolDate;
    }

    public String getInPoolTime() {
        return inPoolTime;
    }

    public void setInPoolTime(String inPoolTime) {
        this.inPoolTime = inPoolTime;
    }

    private String inPoolTime;
    private String productDetailNo;
    private String unit;
    private String brand;
    private String manufacturer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String procNum;
    private String remark;
    private String lastUseTime;
    private String mixDate;
    private int productDetailId;
    private String ingredientNo;
    private String salinity;
    private String acidity;
    private String amount;
    private double mixPeriod;
    private String baumeDegree;
    private double phValue;
    private int creator;
    private String creatorName;
    private String productName;
    private String specification;
    private String address;

    private ObjectBean object;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private List<ComponentItemListBean> componentItemList;//

    public String getProcNum() {
        return procNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setProcNum(String procNum) {
        this.procNum = procNum;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPoolNo() {
        return poolNo;
    }

    public void setPoolNo(String poolNo) {
        this.poolNo = poolNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(String lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public String getMixDate() {
        return mixDate;
    }

    public void setMixDate(String mixDate) {
        this.mixDate = mixDate;
    }

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public String getIngredientNo() {
        return ingredientNo;
    }

    public void setIngredientNo(String ingredientNo) {
        this.ingredientNo = ingredientNo;
    }

    public String getSalinity() {
        return salinity;
    }

    public void setSalinity(String salinity) {
        this.salinity = salinity;
    }

    public String getAcidity() {
        return acidity;
    }

    public void setAcidity(String acidity) {
        this.acidity = acidity;
    }

    public String getBaumeDegree() {
        return baumeDegree;
    }

    public void setBaumeDegree(String baumeDegree) {
        this.baumeDegree = baumeDegree;
    }

    public double getPhValue() {
        return phValue;
    }

    public void setPhValue(double phValue) {
        this.phValue = phValue;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public List<ComponentItemListBean> getComponentItemList() {
        return componentItemList;
    }

    public void setComponentItemList(List<ComponentItemListBean> componentItemList) {
        this.componentItemList = componentItemList;
    }

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public String getProductDetailNo() {
        return productDetailNo;
    }

    public void setProductDetailNo(String productDetailNo) {
        this.productDetailNo = productDetailNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public static class ComponentItemListBean implements Serializable{

        @SerializedName("id")
        private String idX;
        private String name;
        private String amount;
        @SerializedName("remark")
        private Object remarkX;
        private String canDelete;
        private String manufacturer;
        private String manufactureDate;

        public String getIdX() {
            return idX;
        }

        public void setIdX(String idX) {
            this.idX = idX;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Object getRemarkX() {
            return remarkX;
        }

        public void setRemarkX(Object remarkX) {
            this.remarkX = remarkX;
        }

        public String getCanDelete() {
            return canDelete;
        }

        public void setCanDelete(String canDelete) {
            this.canDelete = canDelete;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getManufactureDate() {
            return manufactureDate;
        }

        public void setManufactureDate(String manufactureDate) {
            this.manufactureDate = manufactureDate;
        }
    }

    public static class ObjectBean implements Serializable{
        /**
         * remark : 备注
         * productName : 产品名称22
         * originPlace : 产地2
         * numberPlate : 车牌号2
         * driverName : 驾驶员
         */

        @SerializedName("remark")
        private String remarkX;
        private String originPlace;
        private String numberPlate;
        private String driverName;
        private String weighDate;
        private String checkDate;
        private String amount;
        private String productName;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        private String putInPoolDate;

        public String getPutInPoolDate() {
            return putInPoolDate;
        }

        public void setPutInPoolDate(String putInPoolDate) {
            this.putInPoolDate = putInPoolDate;
        }
        private String productSpecification;
        public String getProductSpecification() {
            return productSpecification;
        }

        public void setProductSpecification(String productSpecification) {
            this.productSpecification = productSpecification;
        }
        public String getCheckDate() {
            return checkDate;
        }

        public void setCheckDate(String checkDate) {
            this.checkDate = checkDate;
        }

        public String getWeighDate() {
            return weighDate;
        }

        public void setWeighDate(String weighDate) {
            this.weighDate = weighDate;
        }

        public String getRemarkX() {
            return remarkX;
        }

        public void setRemarkX(String remarkX) {
            this.remarkX = remarkX;
        }

        public String getOriginPlace() {
            return originPlace;
        }

        public void setOriginPlace(String originPlace) {
            this.originPlace = originPlace;
        }

        public String getNumberPlate() {
            return numberPlate;
        }

        public void setNumberPlate(String numberPlate) {
            this.numberPlate = numberPlate;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }
    }
}
