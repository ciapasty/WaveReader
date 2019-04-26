import org.json.JSONObject;

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
    // double[channel][sample]
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

    // Generate waveform summary for simplified visualization
    private double[] getWaveformSummary(int length) {
        if (samples == null) { throw new NullPointerException(); }
        double[] waveformSummary;
        int blockLength = samples[0].length / length;
        int sampleTail = samples[0].length - (blockLength * length);
        if (blockLength < 1) {
            // TODO: Handle this properly
            throw new UnsupportedOperationException("Waveform too short. Not supported yet.");
        } else {
            waveformSummary = new double[length];
            int shift = 0;
            for (int i = 0; i < length; i++) {
                if (sampleTail > 0) {
                    waveformSummary[i] = getAverageFromBlock(shift, (blockLength + 1));
                    shift += blockLength + 1;
                    sampleTail--;
                } else {
                    waveformSummary[i] = getAverageFromBlock(shift, (blockLength));
                    shift += blockLength;
                }
            }
        }
        return waveformSummary;
    }

    private double getAverageFromBlock(int startIndex, int blockLength) {
        double blockAvg = 0.0;
        for (int i = startIndex; i < startIndex+blockLength; i++) {
            double channelAvg = 0.0;
            for (int n = 0; n < channels; n++) {
                channelAvg += samples[n][i];
            }
            blockAvg += channelAvg / channels;
        }
        return Math.abs(blockAvg / blockLength);
    }

    public String toJSON(int waveformLength) {
        JSONObject jo = new JSONObject();
        jo.put("name", filename);
        jo.put("length", getDuration());
        jo.put("waveform", getWaveformSummary(waveformLength));

        return jo.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(filename).append(" ").append(getDuration()).append("ms");
        sb.append(bitDepth).append("bps ").append(samplingRate/1000.0).append("kHz");
        return sb.toString();
    }
}
