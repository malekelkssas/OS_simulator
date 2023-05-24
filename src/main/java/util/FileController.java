package util;

import constants.Constants;

import java.io.File;
import java.nio.file.Paths;

public class FileController {

    public static void removeProcess(int id) {
        String path = new String(getPath(id));
        File file = new File(path);
        if (file.exists()) {
            boolean deleted = file.delete();
        }
    }



    public static String getPath(int id) {
        return Paths.get("").toAbsolutePath().toString() + "//" +Constants.PROCESSES_LOCATION + id + Constants.DATA_EXTENSTION;
    }

    public static void mkdir(){
        File file = new File(Paths.get("").toAbsolutePath().toString()+"//processes");
        if (!file.isDirectory())
            file.mkdir();
    }
}
