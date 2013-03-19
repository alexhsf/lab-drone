package com.shigeodayo.ardrone.video;

import java.io.InputStream;

public interface VideoDecoder {

    public void decode(InputStream is, ImageListener listener);

}
