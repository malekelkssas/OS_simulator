package util;

import exceptions.NoSuchProcessException;
import exceptions.OSSimulatoeException;
import storage.Process;
import java.io.*;

public class Serializer {
    static FileOutputStream fileOut;
    static ObjectOutputStream out;
    static FileInputStream fileIn;
    static ObjectInputStream in;

    public static void serializeProcess(Process process) throws OSSimulatoeException {
        try {
            out = getOutStream(process.getID());
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
            throw new NoSuchProcessException(e.getMessage());
        }
    }

    public static ObjectOutputStream getOutStream(int id) throws IOException {
        String path = FileController.getPath(id);
        fileOut = new FileOutputStream(path);
        out = new ObjectOutputStream(fileOut);
        return out;
    }

    public static ObjectInputStream getInputStream(int id) throws OSSimulatoeException, IOException {
        String path = FileController.getPath(id);
        fileIn = new FileInputStream(path);
        in = new ObjectInputStream(fileIn);
        return in;
    }


}
