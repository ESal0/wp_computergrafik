package computergraphics.file.reader.model;

import java.util.ArrayList;

/**
 * Created by Christian on 29.11.2015.
 */
public class CasesLookupTable {
    private String path;
    private ArrayList<Integer> casesLookupTable;

    public CasesLookupTable(String path) {
        this.path = path;
        casesLookupTable = new ArrayList<Integer>();
    }

    public String getPath() {
        return path;
    }

    public ArrayList<Integer> getCasesLookupTable() {
        return casesLookupTable;
    }

    public void addElementToCasesLookupTable(int elem) {
        this.casesLookupTable.add(elem);
    }
}
