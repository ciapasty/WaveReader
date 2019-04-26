import org.apache.commons.lang3.ArrayUtils;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WaveReader {

    // The range of 24 bit signed integers is −8,388,608 to 8,388,607
    private final static int INT24MAX_VALUE = 8388607;

    public WaveFile readWaveFile(String filename) throws IOException, UnsupportedAudioFileException {
        // TODO: Add file exists/is wav checks

        WaveFile waveFile = new WaveFile(filename);
        FileInputStream inputStream = new FileInputStream(filename);

        int bytesLeft = inputStream.available();

        // Read RIFF header
        bytesLeft -= readRIFFHeader(waveFile, inputStream);

        // Loop over headers
        byte[] buffer = new byte[4];
        ByteBuffer bb;
        do {
            bytesLeft -= inputStream.read(buffer);
            String headerName = new String(buffer);

            bytesLeft -= inputStream.read(buffer);
            bb = ByteBuffer.wrap(buffer);
            bb.order(waveFile.getByteOrder());
            int chunkSize = bb.getInt();
            chunkSize = (chunkSize % 2 == 1) ? (chunkSize + 1) : chunkSize;

            System.out.println(headerName + ": " + chunkSize);
            if (headerName.equals("fmt ")) {
                byte[] headerBytes = new byte[chunkSize];
                bytesLeft -= inputStream.read(headerBytes);
                readFmtHeader(waveFile, headerBytes);
                if (waveFile.channels > 2) {
                    throw new UnsupportedAudioFileException();
                }
            } else if (headerName.equals("data")) {
                waveFile.sampleCount = chunkSize/waveFile.blockAlign;
                bytesLeft -=readAudioData(waveFile, inputStream);
            } else {
                bytesLeft -= inputStream.skip(chunkSize);
            }

            System.out.println("Bytes left unread: " + (bytesLeft));
        } while(bytesLeft > 0);

        return waveFile;
    }

    private int readRIFFHeader(WaveFile waveFile, InputStream inputStream) throws IOException {
        int bytesRead = 0;
        byte[] buffer = new byte[4];
        ByteBuffer bb = ByteBuffer.wrap(buffer);

        // RIFF header
        bytesRead += inputStream.read(buffer);
        waveFile.riffHeader = new String(buffer);

        bb.order(waveFile.getByteOrder());

        // RIFF chunk size
        bytesRead += inputStream.read(buffer);
        int chunkSize = bb.getInt();

        // Wave file type
        bytesRead += inputStream.read(buffer);
        waveFile.fileType = new String(buffer);

        return bytesRead;
    }

    private void readFmtHeader(WaveFile waveFile, byte[] bytes) throws UnsupportedAudioFileException {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(waveFile.getByteOrder());
        waveFile.audioFormat = bb.getShort(0);
        waveFile.channels = bb.getShort(2);
        waveFile.samplingRate = bb.getInt(4);
        waveFile.byteRate = bb.getInt(8);
        waveFile.blockAlign = bb.getShort(12);
        waveFile.bitDepth = bb.getShort(14);
//        if (waveFile.audioFormat != 1) {
//            // TODO: Add fields from extra format data
//             short extensibleSize = bb.getShort(16);
//        }
    }

    private int readAudioData(WaveFile waveFile, InputStream inputStream) throws IOException, UnsupportedAudioFileException {
        byte[] dataBytes = new byte[waveFile.blockAlign];
        int bytesRead = 0;

        waveFile.samples = new double[waveFile.channels][waveFile.sampleCount];

        ByteBuffer bb;
        int bytesPerChannel = waveFile.blockAlign/waveFile.channels;
        for(int i = 0; i < waveFile.sampleCount; i++) {
            bytesRead += inputStream.read(dataBytes);
            bb = ByteBuffer.wrap(dataBytes);
            bb.order(waveFile.getByteOrder());
            for(int n = 0; n < waveFile.channels; n++) {
                int index = n*bytesPerChannel;
                switch (bytesPerChannel) {
                    case 1:
                        waveFile.samples[n][i] = (double)((short)(bb.get() & 0xff) - Byte.MAX_VALUE)/Byte.MAX_VALUE;
                        break;
                    case 2:
                        waveFile.samples[n][i] = (double)bb.getShort(index)/Short.MAX_VALUE;
                        break;
                    case 3:
                        waveFile.samples[n][i] = (double) intFrom3Bytes(
                                ArrayUtils.subarray(dataBytes, index, index+3),
                                waveFile.getByteOrder()
                            ) / INT24MAX_VALUE;
                        break;
                    case 4:
                        // TODO: Fix 32 bit - how is it represented in WAVE? -> Depends on additional format information (float or int32)
                        throw new UnsupportedAudioFileException("Unsupported bit rate: " + bytesPerChannel*8);
                        //waveFile.samples[n][i] = (double)bb.getInt(index)/Integer.MAX_VALUE;
                        //break;
                    default:
                        throw new UnsupportedAudioFileException("Unsupported bit rate: " + bytesPerChannel*8);
                }
            }
        }

        return bytesRead;
    }

    private int intFrom3Bytes(byte[] bytes, ByteOrder byteOrder) {
        if(byteOrder == ByteOrder.LITTLE_ENDIAN) {
            ArrayUtils.reverse(bytes);
        }
        int value = (((bytes[0] & 0xFF) << 16) | ((bytes[1] & 0xFF) <<  8) | (bytes[2] & 0xFF));
        // 24 bit signed integer fix
        if ((value & 0x00800000) > 0) {
            return value | 0xFF000000;
        } else {
            return value & 0x00FFFFFF;
        }
    }
}
