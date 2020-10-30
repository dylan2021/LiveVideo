package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;

/**
 * <p>
 * 二维码扫描结果基类
 * </p>
 */
public class ScanResult {
    /**
     * 设备序列号
     */
    private String sn = "";

    /**
     * 设备型号
     */
    private String mode = "";

    /**
     * 设备验证码、设备安全码
     */
    private String regcode = "";

    /**
     * 设备随机码，备用
     */
    private String rd = "";

    /**
     * 设备能力
     */
    private String nc = "";

    /**
     * 设备安全验证码
     */
    private String sc = "";

    private String imeiCode = "";

    /**
     * 创建一个新的实例ScanResult.
     *
     * @param scanString
     */
    public ScanResult(String scanString) {
        // TODO Auto-generated constructor stub
    }

    /**
     * 创建一个新的实例ScanResult.
     */
    public ScanResult() {
        // TODO Auto-generated constructor stub
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getRegcode() {
        return regcode;
    }

    public void setRegcode(String regcode) {
        this.regcode = regcode;
    }

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getImeiCode() {
        return imeiCode;
    }

    public void setImeiCode(String imeiCode) {
        this.imeiCode = imeiCode;
    }

    @Override
    public String toString() {
        return "ScanResult{" +
                "sn='" + sn + '\'' +
                ", mode='" + mode + '\'' +
                ", regcode='" + regcode + '\'' +
                ", nc='" + nc + '\'' +
                ", sc='" + sc + '\'' +
                ", imeiCode='" + imeiCode + '\'' +
                '}';
    }
}
