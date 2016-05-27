package com.chenyg.wporter.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageTool
{
    public static class SizeInfo
    {
        /**
         * 图片宽度
         */
        public int width;

        /**
         * 图片高度
         */
        public int height;
    }

    public static class ImageException extends Exception
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public ImageException()
        {
        }

        ImageException(String info)
        {
            super(info);
        }
    }

    /**
     * 得到图片尺寸信息
     *
     * @param file
     * @param MIMEType
     * @return
     * @throws ImageException
     */
    public static SizeInfo getImageInfo(File file, String MIMEType) throws ImageException
    {

        try
        {
            Iterator<ImageReader> iterator = ImageIO.getImageReadersByMIMEType(MIMEType);
            if (!iterator.hasNext())
            {
                throw new ImageException("cannot read the MIMEType '" + MIMEType + "'");
            }
            ImageReader imageReader = iterator.next();
            ImageInputStream imageInputStream;
            imageInputStream = ImageIO.createImageInputStream(file);
            imageReader.setInput(imageInputStream, true);
            SizeInfo info = new SizeInfo();
            info.width = imageReader.getWidth(0);
            info.height = imageReader.getHeight(0);
            imageReader.abort();
            return info;
        } catch (IOException e)
        {
            throw new ImageException(e.toString());
        }

    }
}
