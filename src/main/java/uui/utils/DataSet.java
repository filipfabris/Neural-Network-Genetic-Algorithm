package uui.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataSet {

    public Map<Float, Float> data;
    String fileName;

    public DataSet(String fileName) {
        this.data = new LinkedHashMap<>();
        this.fileName = fileName;
    }

    public void readData() throws IOException {
        List<String> lines = Files.readAllLines( Paths.get(fileName));
        lines.remove( 0 ); // remove header

        for (String line : lines) {
            String[] parts = line.split(",");
            data.put(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]));
        }
    }
}
