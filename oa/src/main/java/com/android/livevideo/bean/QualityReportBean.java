package com.android.livevideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 *Gool
 */
public class QualityReportBean implements Serializable {

    private List<ContentBean> content;

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean implements Serializable{

        private int id;
        private String createTime;
        private String updateTime;
        private String checkTime;
        private String creator;
        private int updator;
        private int status;
        private String checker;
        private String confirmor;
        private String confirmTime;
        private String reformSuggest;
        private String mustReformTime;
        private String reformor;
        private String reformTime;
        private BizProcessorBean bizProcessor;
        private String creatorUsername;
        private String reformDesc;
        private String confirmorUsername;
        private List<DetailBean> detail;
        private List<FileInfo> attachment;
        private List<FileInfo> pic;

        public String getReformDesc() {
            return reformDesc;
        }

        public void setReformDesc(String reformDesc) {
            this.reformDesc = reformDesc;
        }

        public String getChecker() {
            return checker;
        }

        public void setChecker(String checker) {
            this.checker = checker;
        }

        public String getCheckTime() {
            return checkTime;
        }

        public void setCheckTime(String checkTime) {
            this.checkTime = checkTime;
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

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public int getUpdator() {
            return updator;
        }

        public void setUpdator(int updator) {
            this.updator = updator;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        public String getConfirmor() {
            return confirmor;
        }

        public void setConfirmor(String confirmor) {
            this.confirmor = confirmor;
        }

        public String getConfirmTime() {
            return confirmTime;
        }

        public void setConfirmTime(String confirmTime) {
            this.confirmTime = confirmTime;
        }

        public String getReformSuggest() {
            return reformSuggest;
        }

        public void setReformSuggest(String reformSuggest) {
            this.reformSuggest = reformSuggest;
        }

        public String getMustReformTime() {
            return mustReformTime;
        }

        public void setMustReformTime(String mustReformTime) {
            this.mustReformTime = mustReformTime;
        }

        public String getReformor() {
            return reformor;
        }

        public void setReformor(String reformor) {
            this.reformor = reformor;
        }

        public String getReformTime() {
            return reformTime;
        }

        public void setReformTime(String reformTime) {
            this.reformTime = reformTime;
        }

        public BizProcessorBean getBizProcessor() {
            return bizProcessor;
        }

        public void setBizProcessor(BizProcessorBean bizProcessor) {
            this.bizProcessor = bizProcessor;
        }

        public String getCreatorUsername() {
            return creatorUsername;
        }

        public void setCreatorUsername(String creatorUsername) {
            this.creatorUsername = creatorUsername;
        }

        public String getConfirmorUsername() {
            return confirmorUsername;
        }

        public void setConfirmorUsername(String confirmorUsername) {
            this.confirmorUsername = confirmorUsername;
        }

        public List<DetailBean> getDetail() {
            return detail;
        }

        public void setDetail(List<DetailBean> detail) {
            this.detail = detail;
        }

        public List<FileInfo> getAttachment() {
            return attachment;
        }

        public void setAttachment(List<FileInfo> attachment) {
            this.attachment = attachment;
        }

        public List<FileInfo> getPic() {
            return pic;
        }

        public void setPic(List<FileInfo> pic) {
            this.pic = pic;
        }

        public static class BizProcessorBean implements Serializable{
            /**
             * id : 1
             * createTime : null
             * updateTime : null
             * bizBuildSite : {"id":2,"createTime":"2018/09/22 09:33:40","updateTime":"2018/10/16 11:17:48","attachment":"string","code":"GD1001","description":"工地","invest":2600,"loadLevel":"1","name":"路基2","period":365,"pic":null,"startTime":"2018/10/20","endTime":"2019/11/16","realEndDate":null,"realStartDate":null,"roadLevel":"1","specification":{"SUBGRADE_WIDTH":"25","SUBGRADE_LENGTH":"2.5","SUBGRADE_END_CHAINAGE":"K6+285.68","SUBGRADE_START_CHAINAGE":"K0+000.00  "},"speed":80,"type":"1","qualified":false,"overdue":false,"bizContract":{"id":1,"createTime":"2018/09/22 15:43:55","updateTime":"2018/10/16 11:16:06","attachment":"string","code":"T8","description":"test","endChainage":"k186+258","invest":10000,"length":186,"name":"合同8","period":365,"pic":null,"startTime":"2018/09/22","endTime":"2018/09/22","realEndDate":null,"realStartDate":null,"startChainage":"k0+000","contractCorporation":{"id":1,"createTime":"2018/08/17 18:21:12","updateTime":"2018/10/05 11:46:44","nameCn":"武汉测试公司01","shortNameCn":"test","nameEn":"SSLH","shortNameEn":"sslh"},"supervisionCorporation":{"id":1,"createTime":"2018/08/17 18:21:12","updateTime":"2018/10/05 11:46:44","nameCn":"武汉测试公司01","shortNameCn":"test","nameEn":"SSLH","shortNameEn":"sslh"},"bizProject":{"id":1,"createTime":"2018/09/22 15:19:04","updateTime":"2018/09/22 15:19:04","attachment":"string","code":"BH1001","description":"string","endLocation":"string","invest":10.22,"length":100,"name":"贵州高速公路项目1","period":11,"pic":"string","startTime":"2018/09/22","endTime":"2018/09/22","startLocation":"string"}},"designUnit":{"id":1,"createTime":"2018/08/17 18:21:12","updateTime":"2018/10/05 11:46:44","nameCn":"武汉测试公司01","shortNameCn":"test","nameEn":"SSLH","shortNameEn":"sslh"},"constructionUnit":{"id":1,"createTime":"2018/08/17 18:21:12","updateTime":"2018/10/05 11:46:44","nameCn":"武汉测试公司01","shortNameCn":"test","nameEn":"SSLH","shortNameEn":"sslh"}}
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

            public static class BizBuildSiteBean implements Serializable{

                private int id;
                private String createTime;
                private String updateTime;
                private String code;
                private String description;
                private double invest;
                private String loadLevel;
                private String name;
                private int period;
                private String planBeginDate;
                private String planEndDate;
                private Object realEndDate;
                private Object realStartDate;
                private String roadLevel;
                private int speed;
                private String type;
                private boolean qualified;
                private boolean overdue;
                private BizContractBean bizContract;

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

                public static class BizContractBean implements Serializable{

                    private int id;
                    private String createTime;
                    private String updateTime;
                    private String code;
                    private String description;
                    private String endChainage;
                    private double invest;
                    private double length;
                    private String name;
                    private int period;
                    private String planBeginDate;
                    private String planEndDate;
                    private Object realEndDate;
                    private Object realStartDate;
                    private String startChainage;

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

                    public String getStartChainage() {
                        return startChainage;
                    }

                    public void setStartChainage(String startChainage) {
                        this.startChainage = startChainage;
                    }
                }
            }

            public static class BizProcessorConfigBean implements Serializable{
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

        public static class DetailBean implements Serializable{
            /**
             * qualifyConfigId : 2
             * reportResult : 1
             * confirmResult : 1
             */

            private int qualifyConfigId;
            private String reportResult;
            private String confirmResult;

            public int getQualifyConfigId() {
                return qualifyConfigId;
            }

            public void setQualifyConfigId(int qualifyConfigId) {
                this.qualifyConfigId = qualifyConfigId;
            }

            public String getReportResult() {
                return reportResult;
            }

            public void setReportResult(String reportResult) {
                this.reportResult = reportResult;
            }

            public String getConfirmResult() {
                return confirmResult;
            }

            public void setConfirmResult(String confirmResult) {
                this.confirmResult = confirmResult;
            }
        }
    }
}
