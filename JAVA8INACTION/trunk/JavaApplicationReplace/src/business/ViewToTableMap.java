/*
 *����ָ��Ŀ¼����������ļ�
 *����ÿ��
 *��ÿ�н��д���
 *��ÿ�м�¼��.����ַ���Ϊtokenizer,�Ѽ�¼��
 *
 */
package business;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import model.ViewfieldToTablefield;
import model.ViewfieldToTablefieldPK;
import facade.ViewfieldToTablefieldJpaController;
import java.nio.charset.Charset;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;


/**
 *
 * @author HQST100207
 */
public class ViewToTableMap extends SimpleFileVisitor<Path> {
// Print information about
// each type of file.

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        if (attr.isSymbolicLink()) {
            System.out.format("Symbolic link: %s ", file);
        } else if (attr.isRegularFile()) {
            System.out.format("Regular file: %s ", file);
            fileReplace(file);
        } else {
            System.out.format("Other: %s ", file);
        }
        System.out.println("(" + attr.size() + "bytes)");
        return CONTINUE;
    }
// Print each directory visited.

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        System.out.format("Directory: %s%n", dir);
        return CONTINUE;
    }
// If there is some error accessing
// the file, let the user know.
// If you don't override this method
// and an error occurs, an IOException
// is thrown.

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
    
    /*
    �����ļ�����
    �滻�м������
    \\u20�滻�ɿո� \\n�滻�ɻ���
    ���ɵ�SQL �������׿�
    */
    public void fileReplace(Path file){
        StringBuffer stringBuffer = new StringBuffer();//�洢replace�������
        String[] lineContent = new String[6];//ÿ����4��Ŀ���ݣ���.�ָ����������ң������� table owner.table name.field name.view field.view nameλ���ļ�����
        ViewfieldToTablefieldPK viewfieldToTablefieldPK = new ViewfieldToTablefieldPK();//��������
        ViewfieldToTablefield viewfieldToTablefield = new ViewfieldToTablefield();
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DataHandlerPU");//�õ�ʵ������߹���
        ViewfieldToTablefieldJpaController viewfieldToTablefieldJpaController = new ViewfieldToTablefieldJpaController(entityManagerFactory);//jpa����
        try {
            List<String> fileContent = Files.readAllLines(file,Charset.forName("GBK"));//ԭʼ���ļ������ַ����б�
            String viewName = file.getFileName().toString();//��ͼ�����ִ����ļ�����
            for (String content:fileContent){//�����б�
                Scanner scanner = new Scanner(content);
                //System.out.println("content = " + content);
                scanner.useDelimiter("\\.");//���÷ָ���
                int i=0;//��������Ŀλ��
                while(scanner.hasNext()){
                    lineContent[i] = scanner.next().replace(" ", "").toUpperCase();//�ѿո�ȥ������ĸת��д
                    //System.out.println(lineContent[i]);
                    i++;
                }
                lineContent[4] = "DMART";//��ͼ�û� �˴�д�� 
                lineContent[5] = viewName.toUpperCase().replace(".TXT", "");//��ͼ����
                
                //System.out.println(lineContent[4]);
                //System.out.println(lineContent[5]);
                
                scanner = null;//���ó�null�����������ռ���
                //System.out.println("�ָ���");
                //String contentReplaced = content.replace("\\u20", " ").replace("\\n", System.getProperty("line.separator"));//\\u20�滻�ɿո� \\n�滻�ɻ���
                //stringBuffer.append(contentReplaced).append(System.getProperty("line.separator"));//ÿ�к���
                
                viewfieldToTablefieldPK.setTableOwner(lineContent[0]);
                viewfieldToTablefieldPK.setTableName(lineContent[1]);
                viewfieldToTablefieldPK.setTableField(lineContent[2]);
                viewfieldToTablefieldPK.setViewField(lineContent[3]);
                viewfieldToTablefieldPK.setViewOwner(lineContent[4]);
                viewfieldToTablefieldPK.setViewName(lineContent[5]);
                String operator = "DEFAULT";
                Date date = new Date();
                viewfieldToTablefield.setViewfieldToTablefieldPK(viewfieldToTablefieldPK);
                viewfieldToTablefield.setMakeOperator(operator);
                viewfieldToTablefield.setMakeDate(date);
                viewfieldToTablefield.setModifyOperator(operator);
                viewfieldToTablefield.setModifyDate(date);
                try {
                    if(viewfieldToTablefieldJpaController.findViewfieldToTablefield(viewfieldToTablefieldPK) == null){//�����ڱ��в�����ʱ��д��¼
                        viewfieldToTablefieldJpaController.create(viewfieldToTablefield);//д���
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ViewToTableMap.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                

                //��ն����е�����
                viewfieldToTablefieldPK.setTableOwner(null);
                viewfieldToTablefieldPK.setTableName(null);
                viewfieldToTablefieldPK.setTableField(null);
                viewfieldToTablefieldPK.setViewOwner(null);
                viewfieldToTablefieldPK.setViewName(null);
                viewfieldToTablefieldPK.setViewField(null);
                viewfieldToTablefield.setViewfieldToTablefieldPK(null);
                viewfieldToTablefield.setMakeOperator(null);
                viewfieldToTablefield.setMakeDate(null);
                viewfieldToTablefield.setModifyOperator(null);
                viewfieldToTablefield.setModifyDate(null);                
                
                date = null;
                lineContent = new String[]{"","","","","",""};//���������ÿһ��Ŀ
            }
        } catch (IOException ex) {
            Logger.getLogger(ViewToTableMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        System.out.println("haha");
        
        Path startingDir = Paths.get("C:\\TEMP");
        ViewToTableMap pf = new ViewToTableMap();
        try {
            Files.walkFileTree(startingDir, pf);
        } catch (IOException ex) {
            Logger.getLogger(ViewToTableMap.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
