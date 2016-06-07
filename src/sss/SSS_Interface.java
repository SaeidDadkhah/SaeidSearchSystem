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

    private LuceneEngine luceneEngine;
    private SaeidEngine saeidEngine;

    private ArrayList<String> addresses;
    private ArrayList<String> bodies;

    private int mode;

    public static void main(String[] args) {
        int mode = SSS.MODE_INDEX;
        if (mode == SSS.MODE_LUCENE) {
            System.out.println("===========((sss.SSS_Interface TEST))===========");
            SSS_Interface SSS_interface = new SSS_Interface(mode);
            int nOfDocs = SSS_interface.addDoc("./files/comp.sys.ibm.pc.hardware");
            System.out.println("docs#: " + nOfDocs);
            SSS_interface.finishIndexing();
            System.out.println("===========((Searching))===========");
            try {
                SSS_interface.search("X-Mailer:\"ELM\" AND body:hawk");
                //            sss.LuceneEngine.showRes(docs);
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
            SSS_Interface sSS_interface = new SSS_Interface(mode);
            int nOfDocs = sSS_interface.addDocSaeid("./files/Sample.txt");
            System.out.println("docs#: " + nOfDocs);
            sSS_interface.finishIndexing();
            System.out.println("===========((Searching))===========");
            ArrayList<Integer> res = sSS_interface.saeidSearch("زرادخانه");
            for (Integer i : res)
                System.out.println(i);

        }
    }

    public SSS_Interface(int mode) {
        this.mode = mode;

        addresses = new ArrayList<>();
        bodies = new ArrayList<>();

        switch (mode) {
            case SSS.MODE_LUCENE:
                luceneEngine = new LuceneEngine();
                break;
            case SSS.MODE_INDEX:
                saeidEngine = new SaeidEngine();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int addDoc(String file) {
        switch (mode) {
            case SSS.MODE_LUCENE:
                return addDocLuceneRec(new File(file));
            case SSS.MODE_INDEX:
                return addDocSaeid(file);
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
            return 1;
        }
    }

    public int addDocSaeid(String fileAddress) {
        char[] buffer = new char[BUFFER_SIZE];
        String docSplitter = "<مقاله>";
        int docId = 1;

        BufferedReader br = null;
        long ras = System.currentTimeMillis();
        try {
            br = new BufferedReader(new FileReader(fileAddress));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert br != null;
            String doc = "";
            while (br.ready()) {
                br.read(buffer);
                doc += new String(buffer);
                String[] tmp = doc.split(docSplitter);
                for (int j = 0; j < tmp.length - 1; j++) {
                    tmp[j] = tmp[j].trim();
                    if (tmp[j].length() < SSS.MIN_DOC_LENGTH)
                        continue;
                    System.out.println(docId);
                    System.out.println(tmp[j].substring(0, 200));
                    saeidEngine.addDoc(tmp[j], docId);
                    docId++;
                }
                doc = tmp[tmp.length - 1];
            }
            saeidEngine.addDoc(doc, docId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long rae = System.currentTimeMillis();
        System.out.println("read and add time: " + (rae - ras));
        return docId;
    }

    public void finishIndexing() {
        switch (mode) {
            case SSS.MODE_LUCENE:
                luceneEngine.finishIndexing();
                break;
            case SSS.MODE_INDEX:
                saeidEngine.finishIndexing();
                break;
        }
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
            case SSS.MODE_INDEX:
                ArrayList<Integer> docIds = saeidEngine.search(query);

                for (int i = 0; i < docIds.size(); i++) {

                }
                break;
        }
    }

    public ArrayList<Integer> saeidSearch(String query) {
        return null;
    }

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public ArrayList<String> getBodies() {
        return bodies;
    }

}
