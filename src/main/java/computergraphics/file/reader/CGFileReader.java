package computergraphics.file.reader;

import computergraphics.file.reader.logic.FRBusinessLogic;
import computergraphics.file.reader.model.CasesLookupTable;

/**
 * Created by Christian on 29.11.2015.
 */
public class CGFileReader {
    private FRBusinessLogic frBusinessLogic;

    public CGFileReader() {
        this.frBusinessLogic = new FRBusinessLogic();
    }

    public CasesLookupTable readCasesLookupTable(String path) {
        return this.frBusinessLogic.readCasesLookupTable(path);
    }

    public CasesLookupTable readCasesLookupTableByRelativePath(String relativePath) {
        String path = this.frBusinessLogic.getAbsolutePathFromRelativePath(relativePath);
        return this.readCasesLookupTable(path);
    }
}
