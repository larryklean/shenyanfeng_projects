/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lisjsphandler;

import java.io.IOException;
import static java.nio.file.FileVisitResult.*;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.Path;
import java.lang.Override;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.StandardOpenOption;


/**
 *
 * @author HQST100207
 */
public class LisJspHandler extends SimpleFileVisitor<Path> {
    @Override
    public FileVisitResult visitFile(Path path,BasicFileAttributes attrs) throws IOException{        
        //����һ��PathMatcher����ʾ�ļ�ƥ��ģʽ
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.{jsp}");
        //Ҫ����ÿ��JSPǰ����Ǿ仰
        String gbkStr = "<%@page contentType=\"text/html;charset=GBK\" %>";
        //���ļ���jsp�ļ�ʱ,ע�⣬����ط���Ҫ��path.getFileName(),������ֻʹ��path
        try{
           if(pathMatcher.matches(path.getFileName())){ 
            System.out.println("visitFile = " + path.toString());
            //����ÿ���ļ�
            //��ÿ���ļ�����ǰ������ַ�������仰
            String content = "";//������ݺ���ļ�����
            content = content + gbkStr;//���ַ�������仰�����ļ���ǰ��
            content = content + System.lineSeparator();//���ϻ��з���
            //��������ļ�����ÿ�����ݼӵ�����ַ�����
            for(String line : Files.readAllLines(path,Charset.forName("GBK"))){
                content = content + line;
                content = content + System.lineSeparator();
            }
            //System.out.println("content = "  + content);            
            Files.write(path, content.getBytes(), StandardOpenOption.WRITE);  
        } 
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
        
        
        return FileVisitResult.CONTINUE;
    }
    
    public static void main(String[] args){
        try {
            System.out.println("in the main method");            
            LisJspHandler lisJspHandler = new LisJspHandler();
            Path path = Paths.get("E:\\CDM\\NetBeansProjects\\lis\\trunk\\lis\\web");            
            Files.walkFileTree(path, lisJspHandler);
        } catch (IOException ex) {
            Logger.getLogger(LisJspHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
