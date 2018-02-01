/*
 *����ָ��Ŀ¼����������ļ�
 *�滻�ļ���ָ�����ַ���
 *�������ļ�
 */
package javaapplicationreplace;

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

/**
 *
 * @author HQST100207
 */
public class PrintFiles extends SimpleFileVisitor<Path> {
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
    
    public void fileReplace(Path file){
        StringBuffer stringBuffer = new StringBuffer();//�洢replace�������
        try {
            List<String> fileContent = Files.readAllLines(file);//ԭʼ���ļ������ַ����б�
            for (String content:fileContent){//�����б�
                String contentReplaced = content.replace("\\u20", " ").replace("\\n", System.getProperty("line.separator"));//\\u20�滻�ɿո� \\n�滻�ɻ���
                stringBuffer.append(contentReplaced).append(System.getProperty("line.separator"));//ÿ�к���
            }
            Path replacedFile = Paths.get(file + "_r");//�µ��ļ������ھɵ����ֺ����һ��_r
            Files.createFile(replacedFile);//�������ļ� ����ļ����� ��ʧ�� �׳��쳣
            Files.write(replacedFile, stringBuffer.toString().getBytes());//д�ļ�
        } catch (IOException ex) {
            Logger.getLogger(PrintFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        System.out.println("haha");
        
        Path startingDir = Paths.get("C:\\TEMP\\������_��ѹ���");
        PrintFiles pf = new PrintFiles();
        try {
            Files.walkFileTree(startingDir, pf);
        } catch (IOException ex) {
            Logger.getLogger(PrintFiles.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
