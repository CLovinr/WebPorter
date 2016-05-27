package com.chenyg.wporter.simple;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.chenyg.wporter.util.BytesTool;

class WPorterFormUtil implements Closeable
{
    private InputStream in;
    private byte[] buf;
    private int offset = 0, len = 0;

    public WPorterFormUtil(InputStream in, int bufSize)
    {
        this.in = in;
        buf = new byte[bufSize];
    }

    private void dealBufArray(int needLength)
    {
        if (needLength > buf.length - (offset + len))
        {
            byte[] buf2 = new byte[buf.length];
            System.arraycopy(buf, offset, buf2, 0, len);
            this.buf = buf2;
            offset = 0;
        }
    }

    /**
     * 读取下一个无符号int整数
     *
     * @return
     * @throws IOException
     */
    public long nextUnInt() throws IOException
    {
        if (len < 4)
        {
            dealBufArray(4 - len);
            len += BytesTool.readAtLeast(buf, offset, 4 - len, in);
        }
        long n = 0xffffffffL & BytesTool.readInt(buf, offset);
        offset += 4;
        len -= 4;
        return n;
    }

    /**
     * 读取下一个无符号short
     *
     * @return
     * @throws IOException
     */
    public int nextUnShort() throws IOException
    {
        if (len < 2)
        {
            dealBufArray(2 - len);
            len += BytesTool.readAtLeast(buf, offset, 2 - len, in);
        }
        int n = BytesTool.readUnShort(buf, offset);
        offset += 2;
        len -= 2;
        return n;
    }

    /**
     * 读取下一个无符号字节
     *
     * @return
     * @throws IOException
     */
    public int nextUnByte() throws IOException
    {
        if (len == 0)
        {
            dealBufArray(1);
            len += BytesTool.readAtLeast(buf, offset, 1, in);
        }
        int n = 0xff & buf[offset];
        offset++;
        len--;
        return n;
    }

    /**
     * 读取下一个参数名称
     *
     * @param length
     * @param encoding
     * @return
     * @throws IOException
     */
    public String nextParamName(int length, String encoding) throws IOException
    {
        return new String(nextBytes(length), encoding);
    }


    public byte[] nextBytes(int length) throws IOException
    {

        byte[] bs;
        if (length > len)
        {
            bs = readBytes(length);
        } else
        {
            bs = new byte[length];
            System.arraycopy(buf, offset, bs, 0, length);
            offset += length;
            len -= length;
        }
        return bs;
    }

    /**
     * 要读取的字节数比较大
     *
     * @param length
     * @return
     * @throws IOException
     */
    private byte[] readBytes(int length) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        bos.write(buf, offset, len);
        length -= len;
        offset = 0;
        len = 0;
        while (length > 0)
        {
            int n = in.read(buf);
            if (n > length)
            {
                bos.write(buf, 0, length);
                offset = length;
                len = n - length;
                break;
            } else if (n > 0)
            {
                bos.write(buf, 0, n);
                length-=n;
            } else
            {
                throw new IOException("the stream is end!");
            }

        }
        return bos.toByteArray();
    }

    /**
     * 读取下一个参数值
     *
     * @param length
     * @param encoding
     * @return
     * @throws IOException
     */
    public String nextParamValue(int length, String encoding) throws IOException
    {
        return nextParamName(length, encoding);
    }

    /**
     * 读取下一段字节数组
     *
     * @param maxLength 读取的最大字节数,但至少为1
     * @return
     * @throws IOException
     */
    public ByteBuffer nextFileBytes(int maxLength) throws IOException
    {
        ByteBuffer buffer = ByteBuffer.wrap(buf);
        buffer.position(offset);
        if (len > maxLength)
        {
            buffer.limit(offset + maxLength);
            offset += maxLength;
            len -= maxLength;
        } else if (len > 0)
        {
            buffer.limit(offset + len);
            offset = 0;
            len = 0;
        } else
        {
            len = in.read(buf);
            if (len == -1)
            {
                throw new IOException("the stream is end!");
            }
            return nextFileBytes(maxLength);
        }

        return buffer;
    }

    @Override
    public void close() throws IOException
    {
        if (in != null)
        {
            in.close();
        }

    }
}
