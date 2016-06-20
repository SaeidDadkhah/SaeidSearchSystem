package sss;

import org.apache.lucene.document.Document;
import sss.engine.LuceneEngineFields;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Saeid Dadkhah on 2016-03-11 5:32 PM.
 * Project: SSS
 */
public class SSS_Interface {

    private static final int BUFFER_SIZE = 256 * 1024;
    private static final int RECORD_DIGITS_IN_ID = 2;
    private static final String DOC_SPLITTER = "<مقاله>";

    private LuceneEngine luceneEngine;
    private SaeidEngine saeidEngine;
    private String fileAddress;

    private ArrayList<String> addresses;
    private ArrayList<String> bodies;

    private int mode;

    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) {
        int mode = SSS.MODE_INDEX;
        if (mode == SSS.MODE_LUCENE) {
            System.out.println("===========((sss.SSS_Interface TEST))===========");
            SSS_Interface SSS_interface = new SSS_Interface("./files/comp.sys.ibm.pc.hardware", mode);
            int nOfDocs;
            try {
                nOfDocs = SSS_interface.addDoc();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            System.out.println("docs#: " + nOfDocs);
            SSS_interface.finishIndexing();
            System.out.println("===========((Searching))===========");
            try {
                SSS_interface.search("X-Mailer:\"ELM\" AND body:hawk");
                //            sss.LuceneEngine.showRes(docs);

                //noinspection Convert2streamapi
                for (String address : SSS_interface.addresses) {
                    System.out.println(address);
                    //                Desktop.getDesktop().open(new File(doc.get("file-address")));
                    //                ProcessBuilder pb = new ProcessBuilder("C:\\Program Files (x86)\\Sublime Text 3\\sublime_text.exe", doc.get("file-address"));
                    //                pb.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (mode == SSS.MODE_INDEX) {
            System.out.println("===========((sss.SSS_Interface TEST))===========");
            SSS_Interface sSS_interface = new SSS_Interface("./files/Sample.txt", mode);
            int nOfDocs;
            try {
                nOfDocs = sSS_interface.addDocSaeid();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            System.out.println("docs#: " + nOfDocs);
            sSS_interface.finishIndexing();
            System.out.println("===========((Searching))===========");
            try {
                sSS_interface.search("شهادت");
                System.out.println(sSS_interface.getBodies().get(0).substring(0, 200));
                sSS_interface.search("ژوزه");
                System.out.println(sSS_interface.getBodies().get(0).substring(0, 200));
                sSS_interface.search("فجر");
                System.out.println(sSS_interface.getBodies().get(0).substring(0, 200));
                sSS_interface.search("روانمايه");
                System.out.println(sSS_interface.getBodies().get(0).substring(0, 200));
                sSS_interface.search("رانه");
                System.out.println(sSS_interface.getBodies().get(0).substring(0, 200));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public SSS_Interface(String fileAddress, int mode) {
        this.fileAddress = fileAddress;
        this.mode = mode;

        addresses = new ArrayList<>();
        bodies = new ArrayList<>();

        switch (mode) {
            case SSS.MODE_LUCENE:
                luceneEngine = new LuceneEngine();
                saeidEngine = null;
                break;
            case SSS.MODE_INDEX:
                luceneEngine = null;
                saeidEngine = new SaeidEngine(mode);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int addDoc() throws Exception {
        switch (mode) {
            case SSS.MODE_LUCENE:
                return addDocLuceneRec(new File(fileAddress));
            case SSS.MODE_INDEX:
                return addDocSaeid();
            default:
                return -1;
        }
    }

    private int addDocLuceneRec(File file) {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            int res = 0;
            assert subFiles != null;
            for (File subFile : subFiles) {
                res = res + addDocLuceneRec(subFile);
            }
            return res;
        } else {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                ArrayList<String> fields = new ArrayList<>();
                ArrayList<String> values = new ArrayList<>();
                ArrayList<Integer> types = new ArrayList<>();

                fields.add(LuceneEngineFields.getName(LuceneEngineFields.F_NAME_FILE_ADDRESS));
                values.add(file.getPath());
                types.add(LuceneEngine.F_TYPE_NOT_TOKENIZE);

                String line;
                assert br != null;
                while ((line = br.readLine()) != null && line.length() != 0) {
                    int index = line.indexOf(':');
                    if (index == -1 || line.substring(0, index).trim().contains(" ")) {
                        int iTmp = values.size() - 1;
                        String tmp = values.get(iTmp);
                        values.remove(iTmp);
                        tmp += line;
                        values.add(tmp);
                        continue;
                    }
                    String field = line.substring(0, index).trim().toLowerCase();
                    fields.add(field);
                    values.add(line.substring(index + 1).trim());
                    types.add(LuceneEngineFields.getType(LuceneEngineFields.getId(field)));
                }
                String body = "";
                do {
                    body += line;
                } while ((line = br.readLine()) != null);
                fields.add(LuceneEngineFields.getName(LuceneEngineFields.F_NAME_BODY));
                values.add(body);
                types.add(LuceneEngine.F_TYPE_TOKENIZE);
                luceneEngine.addDoc(fields, values, types);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                assert br != null;
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    private int addDocSaeid() throws Exception {
        int numOfDocs = 0;
        char[] buffer = new char[BUFFER_SIZE];

        BufferedReader br = null;
        long ras = System.currentTimeMillis();
        try {
            br = new BufferedReader(new FileReader(fileAddress));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert br != null;
        String doc = "";
        int cluster = 0;
        int j = 0;
        while (br.ready()) {
            br.read(buffer);
            doc += new String(buffer);
            String[] tmp = doc.split(DOC_SPLITTER);
            if (tmp.length > 1 && cluster > 0) {
                tmp[0] = tmp[0].trim();
                System.out.println(getId(cluster - 1, j));
                System.out.println(tmp[0].substring(0, 200));
                saeidEngine.addDoc(tmp[0], getId(cluster - 1, j));
                numOfDocs++;
            }
            for (j = 1; j < tmp.length - 1; j++) {
                tmp[j] = tmp[j].trim();
//                if (tmp[j].length() < SSS.MIN_DOC_LENGTH)
//                    continue;
                System.out.println(getId(cluster, j));
                System.out.println(tmp[j].substring(0, 200));
                saeidEngine.addDoc(tmp[j], getId(cluster, j));
                numOfDocs++;
            }
            doc = tmp[tmp.length - 1];
            cluster++;
        }
        saeidEngine.addDoc(doc, getId(cluster, 0));
        numOfDocs++;
        long rae = System.currentTimeMillis();
        System.out.println("read and add time: " + (rae - ras));
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numOfDocs;
    }

    public void finishIndexing() {
        long fis = System.currentTimeMillis();
        switch (mode) {
            case SSS.MODE_LUCENE:
                luceneEngine.finishIndexing();
                break;
            case SSS.MODE_INDEX:
                saeidEngine.finishIndexing();
                saeidEngine.setMode(SSS.MODE_SEARCH);
                mode = SSS.MODE_SEARCH;
                break;
        }
        long fie = System.currentTimeMillis();
        System.out.println("finish indexing time: " + (fie - fis));
    }

    public void search(String query) throws Exception {
        addresses.clear();
        bodies.clear();
        switch (mode) {
            case SSS.MODE_LUCENE:
                Document[] docs = luceneEngine.search(query);

                for (Document doc : docs) {
                    addresses.add(doc.get(LuceneEngineFields.getName(LuceneEngineFields.F_NAME_FILE_ADDRESS)));
                    bodies.add(doc.get(LuceneEngineFields.getName(LuceneEngineFields.F_NAME_BODY)));
                }
                break;
            case SSS.MODE_SEARCH:
                ArrayList<Integer> docIds = saeidEngine.search(query);

                char[] buffer = new char[BUFFER_SIZE];

                for (Integer docId : docIds)
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(fileAddress));
                        addresses.add(fileAddress);
                        br.skip(getCluster(docId) * BUFFER_SIZE);
                        br.read(buffer);
                        String[] records = new String(buffer).split(DOC_SPLITTER);
                        int i = getRecord(docId);
                        if (i < records.length - 1)
                            bodies.add(records[i].trim());
                        else {
                            br.read(buffer);
                            bodies.add((records[i] + new String(buffer).split(DOC_SPLITTER)[0]).trim());
                        }
                        br.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                break;
            default:
                throw new Exception("You are not currently in a valid search mode.");
        }
    }

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public ArrayList<String> getBodies() {
        return bodies;
    }

    private int getId(int cluster, int record) {
        return cluster * ((int) Math.pow(10, RECORD_DIGITS_IN_ID)) + record;
    }

    private int getCluster(int id) {
        return id / ((int) Math.pow(10, RECORD_DIGITS_IN_ID));
    }

    private int getRecord(int id) {
        return id % ((int) Math.pow(10, RECORD_DIGITS_IN_ID));
    }

}
