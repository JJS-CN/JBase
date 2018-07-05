package com.jjs.base.entity;

import java.io.File;

/**
 * 说明：选择图片
 * Created by jjs on 2018/7/4.
 */

public class ChoosePhotoEntity {
    public File mOriginalFile;//原始图片地址
    public File mCompressFile;//压缩之后图片地址

    public ChoosePhotoEntity(File originalFile, File compressFile) {
        mOriginalFile = originalFile;
        mCompressFile = compressFile;
    }

    public File getOriginalFile() {
        return mOriginalFile;
    }

    public void setOriginalFile(File originalFile) {
        mOriginalFile = originalFile;
    }

    public File getCompressFile() {
        return mCompressFile;
    }

    public void setCompressFile(File compressFile) {
        mCompressFile = compressFile;
    }
}
