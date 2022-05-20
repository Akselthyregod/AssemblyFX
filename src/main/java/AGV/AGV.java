package AGV;

import org.json.JSONObject;
import java.io.*;

public class AGV {
    public AGV() {

    }

    public JSONObject callGetStatus() throws IOException, InterruptedException {
        File file = new File("../AssemblyFX/src/main/resources/SpringBootExecutableJar-0.0.1-SNAPSHOT.jar");
        String filePath = file.getPath();
        String[] args = new String[]{"GetStatus"};

        Process process = Runtime.getRuntime().exec("java -jar " + filePath + " " + args[0]);
        InputStream in = process.getInputStream();

        String line = null;
        String newLine = "";
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            newLine = "";
            while ((line = reader.readLine()) != null) {
                line = reader.readLine();
                newLine = line;

            }
            reader.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject data = new JSONObject(newLine);
            return data;
        }catch (Exception e){
            return null;
        }


    }

    public void callSetProgram(String programName) throws IOException, InterruptedException {
        File file = new File("../AssemblyFX/src/main/resources/SpringBootExecutableJar-0.0.1-SNAPSHOT.jar");
        String filePath = file.getPath();
        String[] args = new String[]{programName};
        Process process = Runtime.getRuntime().exec("java -jar " + filePath + " " + args[0]);
    }
}