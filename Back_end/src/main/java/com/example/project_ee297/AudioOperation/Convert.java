package com.example.project_ee297.AudioOperation;
import org.apache.commons.codec.EncoderException;
import org.springframework.stereotype.Service;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;

@Service
public class Convert {

    public void convertWavToMp3(String wavFilePath, String mp3FilePath) throws EncoderException {
        File wavFile = new File(wavFilePath);
        File mp3File = new File(mp3FilePath);

        AudioAttributes audioAttributes = new AudioAttributes();
        audioAttributes.setCodec("libmp3lame");
        audioAttributes.setBitRate(128000);
        audioAttributes.setChannels(2);
        audioAttributes.setSamplingRate(44100);

        EncodingAttributes encodingAttributes = new EncodingAttributes();
        encodingAttributes.setOutputFormat("mp3");
        encodingAttributes.setAudioAttributes(audioAttributes);

        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(wavFile), mp3File, encodingAttributes);
        } catch (ws.schild.jave.EncoderException e) {
            e.printStackTrace();
        }
    }
}
