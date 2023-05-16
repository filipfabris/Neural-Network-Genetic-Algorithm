package ui.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataSet {

    public Map<Float, Float[]> data;
    String fileName;

    public DataSet(String fileName) throws IOException {
        this.data = new LinkedHashMap<>();
        this.fileName = fileName;
        this.readData();
    }

    public void readData() throws IOException {
        List<String> lines = Files.readAllLines( Paths.get(fileName));
        lines.remove( 0 ); // remove header

        for (String line : lines) {
            String[] parts = line.split(","); //x1,x2,...xn,yn

            Float[] xValues = new Float[parts.length-1]; //x1,x2,...xn
            for(int i = 0; i< parts.length-1; i++){
                xValues[i] = Float.parseFloat( parts[i] );
            }
            Float yValues = Float.parseFloat(parts[parts.length-1]); //y
            data.put(yValues, xValues);
        }
    }
}
