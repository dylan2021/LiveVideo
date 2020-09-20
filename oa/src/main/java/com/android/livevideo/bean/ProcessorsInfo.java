package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 *Gool
 */
public class ProcessorsInfo implements Serializable{


    /**
     * id : 1
     * createTime : null
     * updateTime : null
     * bizBuildSite : {"id":2,"createTime":"2018/09/22 09:33:40","updateTime":"2018/10/16 11:17:48","attachment":"string","code":"GD1001","description":"工地","invest":2600,"loadLevel":"1","name":"路基2","period":365,"pic":null,"startTime":"2018/10/20","endTime":"2019/11/16","realEndDate":null,"realStartDate":null,"roadLevel":"1","specification":{"SUBGRADE_WIDTH":"25","SUBGRADE_LENGTH":"2.5","SUBGRADE_END_CHAINAGE":"K6+285.68","SUBGRADE_START_CHAINAGE":"K0+000.00 "},"speed":80,"type":"1","qualified":false,"overdue":false,"bizContract":{"id":1,"createTime":"2018/09/22 15:43:55","updateTime":"2018/10/16 11:16:06","attachment":"string"},"designUnit":{"id":1,"createTime":"2018/08/17 18:21:12","updateTime":"2018/10/05 11:46:44","nameCn":"武汉测试公司01"},"constructionUnit":{"id":1,"createTime":"2018/08/17 18:21:12","updateTime":"2018/10/05 11:46:44","nameCn":"武汉测试公司01"}}
     * bizProcessorConfig : {"id":1,"createTime":"2018/10/24 15:39:38","updateTime":null,"type":"1","name":"桩基","configOrder":1}
     */

    private int id;
    private Object createTime;
    private Object updateTime;
    private BizBuildSiteBean bizBuildSite;
    private BizProcessorConfigBean bizProcessorConfig;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public BizBuildSiteBean getBizBuildSite() {
        return bizBuildSite;
    }

    public void setBizBuildSite(BizBuildSiteBean bizBuildSite) {
        this.bizBuildSite = bizBuildSite;
    }

    public BizProcessorConfigBean getBizProcessorConfig() {
        return bizProcessorConfig;
    }

    public void setBizProcessorConfig(BizProcessorConfigBean bizProcessorConfig) {
        this.bizProcessorConfig = bizProcessorConfig;
    }

    public static class BizBuildSiteBean {
        /**
         * id : 2
         * createTime : 2018/09/22 09:33:40
         * updateTime : 2018/10/16 11:17:48
         * attachment : string
         * code : GD1001
         * description : 工地
         * invest : 2600
         * loadLevel : 1
         * name : 路基2
         * period : 365
         * pic : null
         * startTime : 2018/10/20
         * endTime : 2019/11/16
         * realEndDate : null
         * realStartDate : null
         * roadLevel : 1
         * specification : {"SUBGRADE_WIDTH":"25","SUBGRADE_LENGTH":"2.5","SUBGRADE_END_CHAINAGE":"K6+285.68","SUBGRADE_START_CHAINAGE":"K0+000.00 "}
         * speed : 80
         * type : 1
         * qualified : false
         * overdue : false
         * bizContract : {"id":1,"createTime":"2018/09/22 15:43:55","updateTime":"2018/10/16 11:16:06","attachment":"string"}
         * designUnit : {"id":1,"createTime":"2018/08/17 18:21:12","updateTime":"2018/10/05 11:46:44","nameCn":"武汉测试公司01"}
         * constructionUnit : {"id":1,"createTime":"2018/08/17 18:21:12","updateTime":"2018/10/05 11:46:44","nameCn":"武汉测试公司01"}
         */

        private int id;
        private String createTime;
        private String updateTime;
        private List<FileInfo> attachment;
        private String code;
        private String description;
        private int invest;
        private String loadLevel;
        private String name;
        private int period;
        private List<FileInfo> pic;
        private String planBeginDate;
        private String planEndDate;
        private Object realEndDate;
        private Object realStartDate;
        private String roadLevel;
        private SpecificationBean specification;
        private int speed;
        private String type;
        private boolean qualified;
        private boolean overdue;
        private BizContractBean bizContract;
        private DesignUnitBean designUnit;
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

        public int getInvest() {
            return invest;
        }

        public void setInvest(int invest) {
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

        public Object getRealEndDate() {
            return realEndDate;
        }

        public void setRealEndDate(Object realEndDate) {
            this.realEndDate = realEndDate;
        }

        public Object getRealStartDate() {
            return realStartDate;
        }

        public void setRealStartDate(Object realStartDate) {
            this.realStartDate = realStartDate;
        }

        public String getRoadLevel() {
            return roadLevel;
        }

        public void setRoadLevel(String roadLevel) {
            this.roadLevel = roadLevel;
        }

        public SpecificationBean getSpecification() {
            return specification;
        }

        public void setSpecification(SpecificationBean specification) {
            this.specification = specification;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
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

        public BizContractBean getBizContract() {
            return bizContract;
        }

        public void setBizContract(BizContractBean bizContract) {
            this.bizContract = bizContract;
        }

        public DesignUnitBean getDesignUnit() {
            return designUnit;
        }

        public void setDesignUnit(DesignUnitBean designUnit) {
            this.designUnit = designUnit;
        }

        public ConstructionUnitBean getConstructionUnit() {
            return constructionUnit;
        }

        public void setConstructionUnit(ConstructionUnitBean constructionUnit) {
            this.constructionUnit = constructionUnit;
        }

        public static class SpecificationBean {
            /**
             * SUBGRADE_WIDTH : 25
             * SUBGRADE_LENGTH : 2.5
             * SUBGRADE_END_CHAINAGE : K6+285.68
             * SUBGRADE_START_CHAINAGE : K0+000.00
             */

            private String SUBGRADE_WIDTH;
            private String SUBGRADE_LENGTH;
            private String SUBGRADE_END_CHAINAGE;
            private String SUBGRADE_START_CHAINAGE;

            public String getSUBGRADE_WIDTH() {
                return SUBGRADE_WIDTH;
            }

            public void setSUBGRADE_WIDTH(String SUBGRADE_WIDTH) {
                this.SUBGRADE_WIDTH = SUBGRADE_WIDTH;
            }

            public String getSUBGRADE_LENGTH() {
                return SUBGRADE_LENGTH;
            }

            public void setSUBGRADE_LENGTH(String SUBGRADE_LENGTH) {
                this.SUBGRADE_LENGTH = SUBGRADE_LENGTH;
            }

            public String getSUBGRADE_END_CHAINAGE() {
                return SUBGRADE_END_CHAINAGE;
            }

            public void setSUBGRADE_END_CHAINAGE(String SUBGRADE_END_CHAINAGE) {
                this.SUBGRADE_END_CHAINAGE = SUBGRADE_END_CHAINAGE;
            }

            public String getSUBGRADE_START_CHAINAGE() {
                return SUBGRADE_START_CHAINAGE;
            }

            public void setSUBGRADE_START_CHAINAGE(String SUBGRADE_START_CHAINAGE) {
                this.SUBGRADE_START_CHAINAGE = SUBGRADE_START_CHAINAGE;
            }
        }

        public static class BizContractBean {
            /**
             * id : 1
             * createTime : 2018/09/22 15:43:55
             * updateTime : 2018/10/16 11:16:06
             * attachment : string
             */

            private int id;
            private String createTime;
            private String updateTime;
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
        }

        public static class DesignUnitBean {
            /**
             * id : 1
             * createTime : 2018/08/17 18:21:12
             * updateTime : 2018/10/05 11:46:44
             * nameCn : 武汉测试公司01
             */

            private int id;
            private String createTime;
            private String updateTime;
            private String nameCn;

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
        }

        public static class ConstructionUnitBean {
            /**
             * id : 1
             * createTime : 2018/08/17 18:21:12
             * updateTime : 2018/10/05 11:46:44
             * nameCn : 武汉测试公司01
             */

            private int id;
            private String createTime;
            private String updateTime;
            private String nameCn;

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
        }
    }

    public static class BizProcessorConfigBean {
        /**
         * id : 1
         * createTime : 2018/10/24 15:39:38
         * updateTime : null
         * type : 1
         * name : 桩基
         * configOrder : 1
         */

        private int id;
        private String createTime;
        private Object updateTime;
        private String type;
        private String name;
        private int configOrder;

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

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getConfigOrder() {
            return configOrder;
        }

        public void setConfigOrder(int configOrder) {
            this.configOrder = configOrder;
        }
    }
}
