package com.android.livevideo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Gool Lee
 */
public class WorkApplyBean implements Serializable {

    private int id;
    private int changeLevel;
    private String createTime;
    private String actualFinishDate;
    private String updateTime;
    private String constructionPermitNo;
    private String supervisionPermitNo;
    private ConstructionUnitBean constructionUnit;
    private String constructionCertNo;
    private ContractUnitBean contractUnit;
    private String contractCertNo;
    private ProspectUnitBean prospectUnit;
    private String prospectCertNo;
    private DesignUnitBean designUnit;
    private String designCertNo;
    private SupervisionUnitBean supervisionUnit;
    private String supervisionCertNo;
    private DetectUnitBean detectUnit;
    private String detectCertNo;
    private ArchDrawAuditUnitBean archDrawAuditUnit;
    private String archDrawAuditCertNo;


    public String getActualFinishDate() {
        return actualFinishDate;
    }

    public void setActualFinishDate(String actualFinishDate) {
        this.actualFinishDate = actualFinishDate;
    }

    public int getChangeLevel() {
        return changeLevel;
    }

    public void setChangeLevel(int changeLevel) {
        this.changeLevel = changeLevel;
    }

    public String getChangeRemark() {
        return changeRemark;
    }

    public void setChangeRemark(String changeRemark) {
        this.changeRemark = changeRemark;
    }

    public String getDesignDraw() {
        return designDraw;
    }

    public void setDesignDraw(String designDraw) {
        this.designDraw = designDraw;
    }

    public String getDesignDrawNo() {
        return designDrawNo;
    }

    public void setDesignDrawNo(String designDrawNo) {
        this.designDrawNo = designDrawNo;
    }

    private String creatorUsername;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    private BizBuildSiteBean bizBuildSite;
    private String applyWorkDate;
    private String changePlace;
    private boolean isPrepare;
    private boolean isPlan;
    private boolean isEmployee;
    private boolean isDevice;
    private boolean isMaterial;
    private String remark;
    private String changeRemark;
    private String changeInvest;
    private String designDraw;
    private String designDrawNo;
    private List<DetailBean> detail;

    private BizProcessorBean bizProcessor;

    public String getChangePlace() {
        return changePlace;
    }

    public void setChangePlace(String changePlace) {
        this.changePlace = changePlace;
    }

    public String getChangeInvest() {
        return changeInvest;
    }

    public void setChangeInvest(String changeInvest) {
        this.changeInvest = changeInvest;
    }

    private boolean status;
    private List<FileInfo> pic;
    private List<FileInfo> attachment;

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

    public BizBuildSiteBean getBizBuildSite() {
        return bizBuildSite;
    }

    public void setBizBuildSite(BizBuildSiteBean bizBuildSite) {
        this.bizBuildSite = bizBuildSite;
    }

    public String getApplyWorkDate() {
        return applyWorkDate;
    }

    public void setApplyWorkDate(String applyWorkDate) {
        this.applyWorkDate = applyWorkDate;
    }

    public boolean isIsPrepare() {
        return isPrepare;
    }

    public void setIsPrepare(boolean isPrepare) {
        this.isPrepare = isPrepare;
    }

    public boolean isIsPlan() {
        return isPlan;
    }

    public void setIsPlan(boolean isPlan) {
        this.isPlan = isPlan;
    }

    public boolean isIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(boolean isEmployee) {
        this.isEmployee = isEmployee;
    }

    public boolean isIsDevice() {
        return isDevice;
    }

    public void setIsDevice(boolean isDevice) {
        this.isDevice = isDevice;
    }

    public boolean isIsMaterial() {
        return isMaterial;
    }

    public void setIsMaterial(boolean isMaterial) {
        this.isMaterial = isMaterial;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<FileInfo> getPic() {
        return pic;
    }

    public void setPic(List<FileInfo> pic) {
        this.pic = pic;
    }

    public List<FileInfo> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<FileInfo> attachment) {
        this.attachment = attachment;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public BizProcessorBean getBizProcessor() {
        return bizProcessor;
    }

    public void setBizProcessor(BizProcessorBean bizProcessor) {
        this.bizProcessor = bizProcessor;
    }

    public String getConstructionPermitNo() {
        return constructionPermitNo;
    }

    public void setConstructionPermitNo(String constructionPermitNo) {
        this.constructionPermitNo = constructionPermitNo;
    }

    public String getSupervisionPermitNo() {
        return supervisionPermitNo;
    }

    public void setSupervisionPermitNo(String supervisionPermitNo) {
        this.supervisionPermitNo = supervisionPermitNo;
    }

    public ConstructionUnitBean getConstructionUnit() {
        return constructionUnit;
    }

    public void setConstructionUnit(ConstructionUnitBean constructionUnit) {
        this.constructionUnit = constructionUnit;
    }

    public String getConstructionCertNo() {
        return constructionCertNo;
    }

    public void setConstructionCertNo(String constructionCertNo) {
        this.constructionCertNo = constructionCertNo;
    }

    public ContractUnitBean getContractUnit() {
        return contractUnit;
    }

    public void setContractUnit(ContractUnitBean contractUnit) {
        this.contractUnit = contractUnit;
    }

    public String getContractCertNo() {
        return contractCertNo;
    }

    public void setContractCertNo(String contractCertNo) {
        this.contractCertNo = contractCertNo;
    }

    public ProspectUnitBean getProspectUnit() {
        return prospectUnit;
    }

    public void setProspectUnit(ProspectUnitBean prospectUnit) {
        this.prospectUnit = prospectUnit;
    }

    public String getProspectCertNo() {
        return prospectCertNo;
    }

    public void setProspectCertNo(String prospectCertNo) {
        this.prospectCertNo = prospectCertNo;
    }

    public DesignUnitBean getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(DesignUnitBean designUnit) {
        this.designUnit = designUnit;
    }

    public String getDesignCertNo() {
        return designCertNo;
    }

    public void setDesignCertNo(String designCertNo) {
        this.designCertNo = designCertNo;
    }

    public SupervisionUnitBean getSupervisionUnit() {
        return supervisionUnit;
    }

    public void setSupervisionUnit(SupervisionUnitBean supervisionUnit) {
        this.supervisionUnit = supervisionUnit;
    }

    public String getSupervisionCertNo() {
        return supervisionCertNo;
    }

    public void setSupervisionCertNo(String supervisionCertNo) {
        this.supervisionCertNo = supervisionCertNo;
    }

    public DetectUnitBean getDetectUnit() {
        return detectUnit;
    }

    public void setDetectUnit(DetectUnitBean detectUnit) {
        this.detectUnit = detectUnit;
    }

    public String getDetectCertNo() {
        return detectCertNo;
    }

    public void setDetectCertNo(String detectCertNo) {
        this.detectCertNo = detectCertNo;
    }

    public ArchDrawAuditUnitBean getArchDrawAuditUnit() {
        return archDrawAuditUnit;
    }

    public void setArchDrawAuditUnit(ArchDrawAuditUnitBean archDrawAuditUnit) {
        this.archDrawAuditUnit = archDrawAuditUnit;
    }

    public String getArchDrawAuditCertNo() {
        return archDrawAuditCertNo;
    }

    public void setArchDrawAuditCertNo(String archDrawAuditCertNo) {
        this.archDrawAuditCertNo = archDrawAuditCertNo;
    }


    public static class BizBuildSiteBean implements Serializable{
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private String name;
        private int id;
        private String planBeginDate;
        private String planEndDate;
        private String realEndDate;
        private String realStartDate;
        private BizContractBean bizContract;

        public String getRealStartDate() {
            return realStartDate;
        }

        public void setRealStartDate(String realStartDate) {
            this.realStartDate = realStartDate;
        }

        public String getRealEndDate() {
            return realEndDate;
        }

        public void setRealEndDate(String realEndDate) {
            this.realEndDate = realEndDate;
        }

        public String getPlanEndDate() {
            return planEndDate;
        }

        public void setPlanEndDate(String planEndDate) {
            this.planEndDate = planEndDate;
        }

        public String getPlanBeginDate() {
            return planBeginDate;
        }

        public void setPlanBeginDate(String planBeginDate) {
            this.planBeginDate = planBeginDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BizContractBean getBizContract() {
            return bizContract;
        }

        public void setBizContract(BizContractBean bizContract) {
            this.bizContract = bizContract;
        }

        public static class BizContractBean implements Serializable{

            private String name;
            private BizProjectBean bizProject;

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

    public static class DetailBean implements Serializable{

        private int processorPlanId;
        /**
         * startTime : 2018/12/06
         * endTime : 2018/12/12
         * planInvest : 55.888
         * changePeriod : 0
         * changeInvest : -144.112
         */

        private String planBeginDate;
        private String name;
        private String planEndDate;
        private double planInvest;
        private int changePeriod;
        private double changeInvest;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProcessorPlanId() {
            return processorPlanId;
        }

        public void setProcessorPlanId(int processorPlanId) {
            this.processorPlanId = processorPlanId;
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

        public double getPlanInvest() {
            return planInvest;
        }

        public void setPlanInvest(double planInvest) {
            this.planInvest = planInvest;
        }

        public int getChangePeriod() {
            return changePeriod;
        }

        public void setChangePeriod(int changePeriod) {
            this.changePeriod = changePeriod;
        }

        public double getChangeInvest() {
            return changeInvest;
        }

        public void setChangeInvest(double changeInvest) {
            this.changeInvest = changeInvest;
        }
    }

    public static class BizProcessorBean implements  Serializable{
        /**
         * id : 1
         * createTime : null
         * updateTime : null
         */
        private BizProcessorConfigBean bizProcessorConfig;
        private BizBuildSiteBean bizBuildSite;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public BizProcessorConfigBean getBizProcessorConfig() {
            return bizProcessorConfig;
        }

        public void setBizProcessorConfig(BizProcessorConfigBean bizProcessorConfig) {
            this.bizProcessorConfig = bizProcessorConfig;
        }

        public BizBuildSiteBean getBizBuildSite() {
            return bizBuildSite;
        }

        public void setBizBuildSite(BizBuildSiteBean bizBuildSite) {
            this.bizBuildSite = bizBuildSite;
        }
    }

    public static class BizProcessorConfigBean implements Serializable{
        /**
         * id : 1
         * createTime : 2018/10/24 15:39:38
         * updateTime : 2018/11/05 08:25:49
         * type : 1
         * name : 其他
         * configOrder : 1
         */

        private String name;
        private int id;

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
    }

    public static class ConstructionUnitBean implements  Serializable{
        /**
         * id : 12
         * createTime : 2018/10/08 13:31:06
         * updateTime : 2018/10/08 14:00:49
         * nameCn : 中铁十八局集团有限公司
         * shortNameCn : 中铁十八局集团
         * nameEn : ZT
         * shortNameEn : null
         */

        @SerializedName("id")
        private int idX;
        @SerializedName("createTime")
        private String createTimeX;
        @SerializedName("updateTime")
        private String updateTimeX;
        private String nameCn;
        private String shortNameCn;
        private String nameEn;
        private Object shortNameEn;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getCreateTimeX() {
            return createTimeX;
        }

        public void setCreateTimeX(String createTimeX) {
            this.createTimeX = createTimeX;
        }

        public String getUpdateTimeX() {
            return updateTimeX;
        }

        public void setUpdateTimeX(String updateTimeX) {
            this.updateTimeX = updateTimeX;
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

        public Object getShortNameEn() {
            return shortNameEn;
        }

        public void setShortNameEn(Object shortNameEn) {
            this.shortNameEn = shortNameEn;
        }
    }

    public static class ContractUnitBean implements  Serializable{
        /**
         * id : 18
         * createTime : 2018/10/19 09:37:11
         * updateTime : null
         * nameCn : 方正集团
         * shortNameCn : h
         * nameEn : fznng
         * shortNameEn : hh
         */

        @SerializedName("id")
        private int idX;
        @SerializedName("createTime")
        private String createTimeX;
        @SerializedName("updateTime")
        private Object updateTimeX;
        private String nameCn;
        private String shortNameCn;
        private String nameEn;
        private String shortNameEn;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getCreateTimeX() {
            return createTimeX;
        }

        public void setCreateTimeX(String createTimeX) {
            this.createTimeX = createTimeX;
        }

        public Object getUpdateTimeX() {
            return updateTimeX;
        }

        public void setUpdateTimeX(Object updateTimeX) {
            this.updateTimeX = updateTimeX;
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

    public static class ProspectUnitBean implements  Serializable{
        /**
         * id : 17
         * createTime : 2018/10/08 13:40:55
         * updateTime : 2018/10/08 13:40:55
         * nameCn : 中交第四公路工程局有限公司
         * shortNameCn : 中交第四公路
         * nameEn : ZJ
         * shortNameEn : null
         */

        @SerializedName("id")
        private int idX;
        @SerializedName("createTime")
        private String createTimeX;
        @SerializedName("updateTime")
        private String updateTimeX;
        private String nameCn;
        private String shortNameCn;
        private String nameEn;
        private Object shortNameEn;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getCreateTimeX() {
            return createTimeX;
        }

        public void setCreateTimeX(String createTimeX) {
            this.createTimeX = createTimeX;
        }

        public String getUpdateTimeX() {
            return updateTimeX;
        }

        public void setUpdateTimeX(String updateTimeX) {
            this.updateTimeX = updateTimeX;
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

        public Object getShortNameEn() {
            return shortNameEn;
        }

        public void setShortNameEn(Object shortNameEn) {
            this.shortNameEn = shortNameEn;
        }
    }

    public static class DesignUnitBean implements  Serializable{
        /**
         * id : 18
         * createTime : 2018/10/19 09:37:11
         * updateTime : null
         * nameCn : 方正集团
         * shortNameCn : h
         * nameEn : fznng
         * shortNameEn : hh
         */

        @SerializedName("id")
        private int idX;
        @SerializedName("createTime")
        private String createTimeX;
        @SerializedName("updateTime")
        private Object updateTimeX;
        private String nameCn;
        private String shortNameCn;
        private String nameEn;
        private String shortNameEn;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getCreateTimeX() {
            return createTimeX;
        }

        public void setCreateTimeX(String createTimeX) {
            this.createTimeX = createTimeX;
        }

        public Object getUpdateTimeX() {
            return updateTimeX;
        }

        public void setUpdateTimeX(Object updateTimeX) {
            this.updateTimeX = updateTimeX;
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

    public static class SupervisionUnitBean implements  Serializable{
        /**
         * id : 17
         * createTime : 2018/10/08 13:40:55
         * updateTime : 2018/10/08 13:40:55
         * nameCn : 中交第四公路工程局有限公司
         * shortNameCn : 中交第四公路
         * nameEn : ZJ
         * shortNameEn : null
         */

        @SerializedName("id")
        private int idX;
        @SerializedName("createTime")
        private String createTimeX;
        @SerializedName("updateTime")
        private String updateTimeX;
        private String nameCn;
        private String shortNameCn;
        private String nameEn;
        private Object shortNameEn;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getCreateTimeX() {
            return createTimeX;
        }

        public void setCreateTimeX(String createTimeX) {
            this.createTimeX = createTimeX;
        }

        public String getUpdateTimeX() {
            return updateTimeX;
        }

        public void setUpdateTimeX(String updateTimeX) {
            this.updateTimeX = updateTimeX;
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

        public Object getShortNameEn() {
            return shortNameEn;
        }

        public void setShortNameEn(Object shortNameEn) {
            this.shortNameEn = shortNameEn;
        }
    }

    public static class DetectUnitBean implements  Serializable{
        /**
         * id : 6
         * createTime : 2018/10/08 13:40:55
         * updateTime : 2018/10/08 13:40:55
         * nameCn : 中交第四公路工程局有限公司
         * shortNameCn : 中交第四公路
         * nameEn : ZJ
         * shortNameEn : null
         */

        @SerializedName("id")
        private int idX;
        @SerializedName("createTime")
        private String createTimeX;
        @SerializedName("updateTime")
        private String updateTimeX;
        private String nameCn;
        private String shortNameCn;
        private String nameEn;
        private Object shortNameEn;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getCreateTimeX() {
            return createTimeX;
        }

        public void setCreateTimeX(String createTimeX) {
            this.createTimeX = createTimeX;
        }

        public String getUpdateTimeX() {
            return updateTimeX;
        }

        public void setUpdateTimeX(String updateTimeX) {
            this.updateTimeX = updateTimeX;
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

        public Object getShortNameEn() {
            return shortNameEn;
        }

        public void setShortNameEn(Object shortNameEn) {
            this.shortNameEn = shortNameEn;
        }
    }

    public static class ArchDrawAuditUnitBean implements  Serializable{
        /**
         * id : 18
         * createTime : 2018/10/19 09:37:11
         * updateTime : null
         * nameCn : 方正集团
         * shortNameCn : h
         * nameEn : fznng
         * shortNameEn : hh
         */

        @SerializedName("id")
        private int idX;
        @SerializedName("createTime")
        private String createTimeX;
        @SerializedName("updateTime")
        private Object updateTimeX;
        private String nameCn;
        private String shortNameCn;
        private String nameEn;
        private String shortNameEn;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getCreateTimeX() {
            return createTimeX;
        }

        public void setCreateTimeX(String createTimeX) {
            this.createTimeX = createTimeX;
        }

        public Object getUpdateTimeX() {
            return updateTimeX;
        }

        public void setUpdateTimeX(Object updateTimeX) {
            this.updateTimeX = updateTimeX;
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
