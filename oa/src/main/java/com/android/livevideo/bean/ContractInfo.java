package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Gool Lee
 * 合同段信息
 */
public class ContractInfo implements Serializable {

    private int id;
    private String createTime;
    private String updateTime;
    private List<FileInfo> attachment;
    private String code;
    private int contractUnitId;
    private String description;
    private String endChainage;
    private double invest;
    private double length;
    private String name;
    private int period;
    private List<FileInfo> pic;
    private String planBeginDate;
    private String planEndDate;
    private int projectId;
    private String realEndDate;
    private String realStartDate;
    private String startChainage;
    private int supervisionUnitId;
    private ContractCorporationBean contractCorporation;
    private ConstructionUnitBean constructionUnit;
    private SupervisionCorporationBean supervisionCorporation;
    /**
     * bizProject : {"id":1}
     */

    private ContractCorporationBean bizProject;

    public ConstructionUnitBean getConstructionUnit() {
        return constructionUnit;
    }

    public void setConstructionUnit(ConstructionUnitBean constructionUnit) {
        this.constructionUnit = constructionUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<FileInfo> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<FileInfo> attachment) {
        this.attachment = attachment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getContractUnitId() {
        return contractUnitId;
    }

    public void setContractUnitId(int contractUnitId) {
        this.contractUnitId = contractUnitId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndChainage() {
        return endChainage;
    }

    public void setEndChainage(String endChainage) {
        this.endChainage = endChainage;
    }

    public double getInvest() {
        return invest;
    }

    public void setInvest(double invest) {
        this.invest = invest;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public List<FileInfo> getPic() {
        return pic;
    }

    public void setPic(List<FileInfo> pic) {
        this.pic = pic;
    }

    public String getPlanBeginDate() {
        return planBeginDate;
    }

    public void setPlanBeginDate(String planBeginDate) {
        this.planBeginDate = planBeginDate;
    }

    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(String realEndDate) {
        this.realEndDate = realEndDate;
    }

    public String getRealStartDate() {
        return realStartDate;
    }

    public void setRealStartDate(String realStartDate) {
        this.realStartDate = realStartDate;
    }

    public String getStartChainage() {
        return startChainage;
    }

    public void setStartChainage(String startChainage) {
        this.startChainage = startChainage;
    }

    public int getSupervisionUnitId() {
        return supervisionUnitId;
    }

    public void setSupervisionUnitId(int supervisionUnitId) {
        this.supervisionUnitId = supervisionUnitId;
    }

    public ContractCorporationBean getContractCorporation() {
        return contractCorporation;
    }

    public void setContractCorporation(ContractCorporationBean contractCorporation) {
        this.contractCorporation = contractCorporation;
    }

    public SupervisionCorporationBean getSupervisionCorporation() {
        return supervisionCorporation;
    }

    public void setSupervisionCorporation(SupervisionCorporationBean supervisionCorporation) {
        this.supervisionCorporation = supervisionCorporation;
    }

    public ContractCorporationBean getBizProject() {
        return bizProject;
    }

    public void setBizProject(ContractCorporationBean bizProject) {
        this.bizProject = bizProject;
    }

    public static class ConstructionUnitBean implements Serializable {

        private String nameCn;

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }
    }

    public static class ContractCorporationBean implements Serializable {
        /**
         * id : 1
         * createTime : 2018-08-17T18:21:12
         * updateTime : 2018-08-27T17:57:17
         * nameCn : 武汉测试公司
         * shortNameCn : string
         * nameEn : SSLH
         * shortNameEn : string
         */

        private int id;
        private String createTime;
        private String updateTime;
        private String nameCn;
        private String shortNameCn;
        private String nameEn;
        private String shortNameEn;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }

        public String getShortNameCn() {
            return shortNameCn;
        }

        public void setShortNameCn(String shortNameCn) {
            this.shortNameCn = shortNameCn;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getShortNameEn() {
            return shortNameEn;
        }

        public void setShortNameEn(String shortNameEn) {
            this.shortNameEn = shortNameEn;
        }
    }

    public static class SupervisionCorporationBean implements Serializable {
        /**
         * id : 1
         * createTime : 2018-08-17T18:21:12
         * updateTime : 2018-08-27T17:57:17
         * nameCn : 武汉测试公司
         * shortNameCn : string
         * nameEn : SSLH
         * shortNameEn : string
         */

        private int id;
        private String createTime;
        private String updateTime;
        private String nameCn;
        private String shortNameCn;
        private String nameEn;
        private String shortNameEn;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }

        public String getShortNameCn() {
            return shortNameCn;
        }

        public void setShortNameCn(String shortNameCn) {
            this.shortNameCn = shortNameCn;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getShortNameEn() {
            return shortNameEn;
        }

        public void setShortNameEn(String shortNameEn) {
            this.shortNameEn = shortNameEn;
        }
    }
}
