package sss;

import org.apache.lucene.document.Document;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Saeid Dadkhah on 2016-03-11 5:32 PM.
 * Project: SSS
 */
public class SSS_Interface {

    private static final int BUFFER_SIZE = 256 * 1024;

    private LuceneEngine ssse;

    private int mode;

    public static void main(String[] args) {
        int mode = SSS.MODE_INDEX;
        if (mode == SSS.MODE_LUCENE) {
            System.out.println("===========((sss.SSS_Interface TEST))===========");
            SSS_Interface SSS_interface = new SSS_Interface(mode);
            int nOfDocs = SSS_interface.addDocLucene("./files/comp.sys.ibm.pc.hardware");
            System.out.println("docs#: " + nOfDocs);
            SSS_interface.finishIndexing();
            System.out.println("===========((Searching))===========");
            try {
                Document[] docs = SSS_interface.search("X-Mailer:\"ELM\" AND body:hawk");
                //            sss.LuceneEngine.showRes(docs);
                for (Document doc : docs) {
                    System.out.println(doc.get(SSS_Fields.getName(SSS_Fields.F_NAME_FILE_ADDRESS)));
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
        ssse = new LuceneEngine();
        this.mode = mode;
    }

    public int addDocLucene(String file) {
        return addDocLuceneRec(new File(file));
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

                fields.add(SSS_Fields.getName(SSS_Fields.F_NAME_FILE_ADDRESS));
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
                    types.add(SSS_Fields.getType(SSS_Fields.getId(field)));
                }
                String body = "";
                do {
                    body += line;
                } while ((line = br.readLine()) != null);
                fields.add(SSS_Fields.getName(SSS_Fields.F_NAME_BODY));
                values.add(body);
                types.add(LuceneEngine.F_TYPE_TOKENIZE);
                ssse.addDoc(fields, values, types);
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
//                    ssse.addDoc(tmp[j], docId);
                    docId++;
                }
                doc = tmp[tmp.length - 1];
            }
//            ssse.addDoc(doc, docId);
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
        ssse.finishIndexing();
    }

    public Document[] search(String query)
            throws Exception {
        return ssse.search(query);
    }

    public ArrayList<Integer> saeidSearch(String query) {
        return null;
    }

    public String getDocName(int docId) {
        switch (mode) {
            case SSS.MODE_LUCENE:
                return null;
            case SSS.MODE_INDEX:
                return null;
            default:
                return null;
        }
    }

}
