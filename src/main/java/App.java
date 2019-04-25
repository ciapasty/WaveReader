import java.io.IOException;

public class App {
    public static void main(String[] args) {
        WaveReader wr = new WaveReader();
        try {
            WaveFile waveFile = wr.readWaveFile("test8.wav");
            System.out.println(waveFile);
            WaveFile waveFile2 = wr.readWaveFile("test16.wav");
            System.out.println(waveFile2);
            WaveFile waveFile3 = wr.readWaveFile("test24.wav");
            System.out.println(waveFile3);
            WaveFile waveFile4 = wr.readWaveFile("test32.wav");
            System.out.println(waveFile4);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
