package com.mm.android.deviceaddmodule.helper;

import com.mm.android.deviceaddmodule.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class DeviceAddImageLoaderHelper {
    public static DisplayImageOptions getCommonOptions() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.adddevice_default)
                .showImageOnFail(R.drawable.adddevice_default)
                .considerExifParams(true).cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
        return displayImageOptions;
    }

    public static DisplayImageOptions getCommonOptions4success() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.adddevice_icon_success_default)
                .showImageOnFail(R.drawable.adddevice_icon_success_default)
                .considerExifParams(true).cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
        return displayImageOptions;
    }
}
