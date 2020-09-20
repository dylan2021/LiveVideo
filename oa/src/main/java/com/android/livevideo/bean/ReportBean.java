package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Gool Lee
 */
public class ReportBean implements Serializable {

    private List<ContentBean> content;

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean implements Serializable {

        private int id;
        private String createTime;
        private String updateTime;
        private int creator;
        private int updator;
        private String confirmInvest;
        private String description;
        private List<FileInfo> pic;
        private List<FileInfo> attachment;
        private String realBeginDate;
        private String realEndDate;
        private String reportInvest;
        private int status;
        private BizProcessorPlanBean bizProcessorPlan;
        private String creatorUsername;
        private String updatorUsername;

        public List<FileInfo> getAttachment() {
            return attachment;
        }

        public void setAttachment(List<FileInfo> attachment) {
            this.attachment = attachment;
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

        public int getCreator() {
            return creator;
        }

        public void setCreator(int creator) {
            this.creator = creator;
        }

        public int getUpdator() {
            return updator;
        }

        public void setUpdator(int updator) {
            this.updator = updator;
        }

        public String getConfirmInvest() {
            return confirmInvest;
        }

        public void setConfirmInvest(String confirmInvest) {
            this.confirmInvest = confirmInvest;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<FileInfo> getPic() {
            return pic;
        }

        public void setPic(List<FileInfo> pic) {
            this.pic = pic;
        }

        public String getRealBeginDate() {
            return realBeginDate;
        }

        public void setRealBeginDate(String realBeginDate) {
            this.realBeginDate = realBeginDate;
        }

        public String getRealEndDate() {
            return realEndDate;
        }

        public void setRealEndDate(String realEndDate) {
            this.realEndDate = realEndDate;
        }

        public String getReportInvest() {
            return reportInvest;
        }

        public void setReportInvest(String reportInvest) {
            this.reportInvest = reportInvest;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public BizProcessorPlanBean getBizProcessorPlan() {
            return bizProcessorPlan;
        }

        public void setBizProcessorPlan(BizProcessorPlanBean bizProcessorPlan) {
            this.bizProcessorPlan = bizProcessorPlan;
        }

        public String getCreatorUsername() {
            return creatorUsername;
        }

        public void setCreatorUsername(String creatorUsername) {
            this.creatorUsername = creatorUsername;
        }

        public String getUpdatorUsername() {
            return updatorUsername;
        }

        public void setUpdatorUsername(String updatorUsername) {
            this.updatorUsername = updatorUsername;
        }

        public static class BizProcessorPlanBean implements Serializable {
            /**
             * id : 38
             * createTime : 2018/10/15 11:52:10
             * updateTime : 2018/10/16 17:18:39
             * invest : 3000
             * name : 测试4
             * period : 6
             * actualPeriod : 0
             * startTime : 2018/10/15
             * endTime : 2018/10/20
             * planPercentage : 0
             * bizId : {"id":11,"createTime":null,"updateTime":null,"buildSiteId":0,"type":3,"name":null}
             * actualInvest : null
             * actualPercentage : 0
             * reportInvest : null
             * reportPercentage : 0
             * lastReportDate : null
             * confirmed : 1
             */

            private int id;
            private String createTime;
            private String updateTime;
            private String invest;
            private String name;
            private String period;
            private int actualPeriod;
            private String planBeginDate;
            private String planEndDate;
            private int planPercentage;
            private BizProcessorBean bizProcessor;
            private Object actualInvest;
            private int actualPercentage;
            private String reportInvest;
            private int reportPercentage;
            private String lastReportDate;
            private int confirmed;

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

            public String getInvest() {
                return invest;
            }

            public void setInvest(String invest) {
                this.invest = invest;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPeriod() {
                return period;
            }

            public void setPeriod(String period) {
                this.period = period;
            }

            public int getActualPeriod() {
                return actualPeriod;
            }

            public void setActualPeriod(int actualPeriod) {
                this.actualPeriod = actualPeriod;
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

            public int getPlanPercentage() {
                return planPercentage;
            }

            public void setPlanPercentage(int planPercentage) {
                this.planPercentage = planPercentage;
            }

            public BizProcessorBean getBizProcessor() {
                return bizProcessor;
            }

            public void setBizProcessor(BizProcessorBean bizProcessor) {
                this.bizProcessor = bizProcessor;
            }

            public Object getActualInvest() {
                return actualInvest;
            }

            public void setActualInvest(Object actualInvest) {
                this.actualInvest = actualInvest;
            }

            public int getActualPercentage() {
                return actualPercentage;
            }

            public void setActualPercentage(int actualPercentage) {
                this.actualPercentage = actualPercentage;
            }

            public String getReportInvest() {
                return reportInvest;
            }

            public void setReportInvest(String reportInvest) {
                this.reportInvest = reportInvest;
            }

            public int getReportPercentage() {
                return reportPercentage;
            }

            public void setReportPercentage(int reportPercentage) {
                this.reportPercentage = reportPercentage;
            }

            public String getLastReportDate() {
                return lastReportDate;
            }

            public void setLastReportDate(String lastReportDate) {
                this.lastReportDate = lastReportDate;
            }

            public int getConfirmed() {
                return confirmed;
            }

            public void setConfirmed(int confirmed) {
                this.confirmed = confirmed;
            }

            public static class BizProcessorBean implements Serializable {
                /**
                 * id : 11
                 * createTime : null
                 * updateTime : null
                 * buildSiteId : 0
                 * type : 3
                 * name : null
                 */

                private int id;
                private Object createTime;
                private Object updateTime;
                private int buildSiteId;
                private int type;
                private String name;
                /**
                 * bizBuildSite : {"id":2,"createTime":"2018/09/22 09:33:40","updateTime":"2018/10/16 11:17:48","attachment":null,"code":"GD1001","description":"工地","invest":2600,"loadLevel":"1","name":"路基2","period":365,"pic":null,"startTime":"2018/10/20","endTime":"2019/11/16","realEndDate":null,"realStartDate":null,"roadLevel":"1","specification":{"SUBGRADE_WIDTH":"25","SUBGRADE_LENGTH":"2.5","SUBGRADE_END_CHAINAGE":"K6+285.68","SUBGRADE_START_CHAINAGE":"K0+000.00 "},"speed":80,"type":"1","qualified":false,"overdue":false,"bizContract":{"id":1,"createTime":"2018/09/22 15:43:55","updateTime":"2018/10/16 11:16:06","attachment":null,"code":"T8","description":"test","endChainage":"k186+258","invest":10000,"length":186,"name":"合同8"}}
                 */

                private BizBuildSiteBean bizBuildSite;
                private BizProcessorConfigBean bizProcessorConfig;

                public BizProcessorConfigBean getBizProcessorConfig() {
                    return bizProcessorConfig;
                }

                public void setBizProcessorConfig(BizProcessorConfigBean bizProcessorConfig) {
                    this.bizProcessorConfig = bizProcessorConfig;
                }

                public static class BizProcessorConfigBean implements Serializable {
                    private String name;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }

                public BizBuildSiteBean getBizBuildSite() {
                    return bizBuildSite;
                }

                public void setBizBuildSite(BizBuildSiteBean bizBuildSite) {
                    this.bizBuildSite = bizBuildSite;
                }

                public static class BizBuildSiteBean implements Serializable {
                    private String name;
                    private BizContractBean bizContract;

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

                    public static class BizContractBean implements Serializable {
                        private String name;

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }
                    }
                }

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

                public int getBuildSiteId() {
                    return buildSiteId;
                }

                public void setBuildSiteId(int buildSiteId) {
                    this.buildSiteId = buildSiteId;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
