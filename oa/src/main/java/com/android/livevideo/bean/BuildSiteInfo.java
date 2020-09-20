package com.android.livevideo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Gool Lee
 */
public class BuildSiteInfo implements Serializable {

    private int id;
    private String createTime;
    private String updateTime;
    private List<FileInfo> pic;
    private List<FileInfo> attachment;
    private String code;
    private String description;
    private double invest;
    private String loadLevel;
    private String name;
    private int period;
    private String planBeginDate;
    private String planEndDate;
    private String realEndDate;
    private String realStartDate;
    private String roadLevel;
    private double speed;
    private String type;
    private boolean qualified;
    private boolean overdue;

    private ConstructionUnitBean constructionUnit;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getInvest() {
        return invest;
    }

    public void setInvest(double invest) {
        this.invest = invest;
    }

    public String getLoadLevel() {
        return loadLevel;
    }

    public void setLoadLevel(String loadLevel) {
        this.loadLevel = loadLevel;
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

    public String getRoadLevel() {
        return roadLevel;
    }

    public void setRoadLevel(String roadLevel) {
        this.roadLevel = roadLevel;
    }


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isQualified() {
        return qualified;
    }

    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public ConstructionUnitBean getConstructionUnit() {
        return constructionUnit;
    }

    public void setConstructionUnit(ConstructionUnitBean constructionUnit) {
        this.constructionUnit = constructionUnit;
    }

    public static class ConstructionUnitBean {

        @SerializedName("id")
        private int idX;
        private String nameCn;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }
    }
    private BizContractBean bizContract;
    public BizContractBean getBizContract() {
        return bizContract;
    }

    public void setBizContract(BizContractBean bizContract) {
        this.bizContract = bizContract;
    }

    public static class BizContractBean implements Serializable{

        private String name;
        private int id;
        private BizProjectBean bizProject;

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

        public BizProjectBean getBizProject() {
            return bizProject;
        }

        public void setBizProject(BizProjectBean bizProject) {
            this.bizProject = bizProject;
        }

        public static class BizProjectBean implements Serializable{

            @SerializedName("name")
            private String nameX;

            public String getNameX() {
                return nameX;
            }

            public void setNameX(String nameX) {
                this.nameX = nameX;
            }
        }
    }
}
