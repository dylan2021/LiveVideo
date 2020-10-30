package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;
import static com.mm.android.deviceaddmodule.mobilecommon.AppConsume.PseudoJsonScanResult.filterInvalidString;
import static com.mm.android.deviceaddmodule.mobilecommon.AppConsume.PseudoJsonScanResult.filterInvalidString4Type;

/**
 * <p>
 * xxx：xxx:xxx格式二维码
 * </p>
 */
public class TwoColonsScanResult extends ScanResult {

    /**
     * 创建一个新的实例TwoColonsScanResult.
     *
     * @param scanString
     */
    public TwoColonsScanResult(String scanString) {
        super(scanString);
        String[] resultStrings = scanString.split(":");
        this.setSn(filterInvalidString(resultStrings[0]));
        this.setMode(filterInvalidString4Type(resultStrings[1]));
        this.setRegcode(filterInvalidString(resultStrings[2]));
    }

}
