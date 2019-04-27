import waveReader.util.WaveFile;
import waveReader.util.WaveReader;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        WaveReader wr = new WaveReader();
        try {
//            waveReader.util.WaveFile waveFile = wr.readWaveFile("test8.wav");
//            System.out.println(waveFile);
//            System.out.println(waveFile.samples.length);
//            waveReader.util.WaveFile waveFile2 = wr.readWaveFile("test16.wav");
//            System.out.println(waveFile2);
            WaveFile waveFile3 = wr.readWaveFile("test24.wav");
            System.out.println(waveFile3);
            System.out.println((float)waveFile3.samples[0].length/1000f);
            System.out.println(waveFile3.toJSON(100));
//            waveReader.util.WaveFile waveFile4 = wr.readWaveFile("test32.wav");
//            System.out.println(waveFile4);
        } catch (UnsupportedAudioFileException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
