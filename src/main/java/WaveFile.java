import java.nio.ByteOrder;

public class WaveFile {
    public String filename;
    public String riffHeader;
    public String fileType;
    public int audioFormat;
    public int channels;
    public int samplingRate;
    public int byteRate;
    public int blockAlign;
    public int bitDepth;
    public int sampleCount;
    public double[][] samples;

    public WaveFile(String filename) {
        this.filename = filename;
    }

    public ByteOrder getByteOrder() {
        return riffHeader.equals("RIFX") ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
    }

    public int getDuration() {
        return (int)(((double)sampleCount/samplingRate)*1000);
    }

    @Override
    public String toString() {
        return filename + " " + getDuration() + "ms " + bitDepth + "bps " + samplingRate/1000.0 + "kHz "
                + channels + " channels\n";
                //+ Arrays.toString(samples[1]);
    }
}
