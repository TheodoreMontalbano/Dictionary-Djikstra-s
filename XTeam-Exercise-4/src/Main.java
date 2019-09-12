import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

public class Main {
    public static void main(String [] args) {
        try {
        String cur = "";
        FileWriter outFileWriter = new FileWriter(new File("longchain2.txt"));
        BufferedWriter fw = new BufferedWriter(outFileWriter);
        for(int i = 0; i < 50; i++) {
            Random r = new Random();
            char c=' ';
            for(int j = 0; j < 10; j++) {
                c = (char)(r.nextInt(26) + 'a');
                fw.write(cur+c+"\n");
            }
            cur+=c;
        }
        fw.close();
        }catch (Exception e) {}
    }
}
