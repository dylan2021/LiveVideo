
package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;
import static com.mm.android.deviceaddmodule.mobilecommon.AppConsume.PseudoJsonScanResult.filterInvalidString;
import static com.mm.android.deviceaddmodule.mobilecommon.AppConsume.PseudoJsonScanResult.filterInvalidString4Type;


public class OneColonScanResult extends ScanResult {

    /**
     * 创建一个新的实例OneColonScanResult.
     *
     * @param scanString
     */
    public OneColonScanResult(String scanString) {
        super(scanString);
        String[] resultStrings = scanString.split(":");
        this.setSn(filterInvalidString(resultStrings[0]));
        this.setMode(filterInvalidString4Type(resultStrings[1]));
    }

}
