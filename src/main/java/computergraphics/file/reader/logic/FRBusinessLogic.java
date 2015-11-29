package computergraphics.file.reader.logic;

import java.io.*;

import computergraphics.file.reader.model.CasesLookupTable;

/**
 * Created by Christian on 29.11.2015.
 */
public class FRBusinessLogic {
    private String readTextFile(String path) {
        String result = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                result += currentLine;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public CasesLookupTable readCasesLookupTable(String path) {
        CasesLookupTable clt = new CasesLookupTable(path);
        String input = this.readTextFile(path);
        String[] values = input.replaceAll("\\s+", "").split(",");
        for (String value : values) {
            try {
                clt.addElementToCasesLookupTable(Integer.parseInt(value));
            }
            catch (Exception ex) {
                throw ex;
            }
        }
        return clt;
    }

    public String getAbsolutePathFromRelativePath(String relativePath) {
        String path = new File("").getAbsolutePath();
        path += relativePath;
        return path;
    }
}
