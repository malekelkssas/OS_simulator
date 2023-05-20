package processesSwaper;

import constants.Constants;
import exceptions.OSSimulatoeException;
import storage.Process;

import java.io.*;
import java.nio.file.Paths;

public class Serializer {
    static FileOutputStream fileOut;
    static ObjectOutputStream out;
    static FileInputStream fileIn;
    static ObjectInputStream in;
    public static void serializeProcess(Process process) throws OSSimulatoeException {
        try {

            out = getOutStream(1);
            out.writeObject(process);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            throw new OSSimulatoeException(e.getMessage());
        }
    }

    public static Process deserializeProcess(int id) throws OSSimulatoeException {
        try {
            in = getInputStream(id);
            Process table = (Process) in.readObject();
            in.close();
            fileIn.close();
            return table;
        } catch (Exception e) {
            throw new OSSimulatoeException(e.getMessage());
        }
    }

    public static ObjectOutputStream getOutStream(int id) throws IOException {
        String path = getPath(1);
        fileOut = new FileOutputStream(path);
        out = new ObjectOutputStream(fileOut);
        return out;
    }

    public static ObjectInputStream getInputStream(int id) throws OSSimulatoeException, IOException {
        String path = getPath(id);
        fileIn = new FileInputStream(path);
        in = new ObjectInputStream(fileIn);
        return in;
    }

    public static String getPath(int id) {
        return Paths.get("").toAbsolutePath().toString()+ "//" +Constants.PROCESSES_LOCATION + id + Constants.DATA_EXTENSTION;
    }
}
