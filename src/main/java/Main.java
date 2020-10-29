import dao.DatabaseCreator;
import procedures.ReportProcedures;

public class Main {
    public static void main(String[] args){

        DatabaseCreator.createTables();
        DatabaseCreator.populateData();

        ReportProcedures.createProcedureShowAllData();
        ReportProcedures.createProcedureShowCountedData();
        ReportProcedures.createProcedureShowCountedDataForSinger();

        System.out.println("\nRunning procedure SHOW_ALL_DATA: \n");
        ReportProcedures.runProcedureShowAllData();

        System.out.println("\nRunning procedure SHOW_COUNTED_DATA: \n");
        ReportProcedures.runProcedureShowCountedData();

        System.out.println("\nRunning procedure SHOW_COUNTED_DATA_FOR_SINGER: \n");
        ReportProcedures.runProcedureShowCountedDataForSinger(1);
        ReportProcedures.runProcedureShowCountedDataForSinger(2);
        ReportProcedures.runProcedureShowCountedDataForSinger(3);

        ReportProcedures.dropProcedureShowAllData();
        ReportProcedures.dropProcedureShowCountedData();
        ReportProcedures.dropProcedureShowCountedDataForSinger();

    }
}
