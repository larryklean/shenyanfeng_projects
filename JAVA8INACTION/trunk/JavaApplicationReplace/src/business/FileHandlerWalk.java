/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
�ó�������
��ȡָ��Ŀ¼������ļ�
�Ѹ��ļ������ݵ��뵽���ݿ��
���ļ��ĸ�ʽ�������
�ı��ļ�
��һ�� ���ݿ���owner����
�ڶ��� ���ݿ�������
�����м��Ժ���� �ֶε�����
�������Կո�ָ�
 */
package business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class FileHandlerWalk {

    public void walkFile() {
        Path startPath = Paths.get("c:\\temp");
        FileHandler fileHandler = new FileHandler();
        try {
            Files.walkFileTree(startPath, fileHandler);
        } catch (IOException ex) {
            Logger.getLogger(FileHandlerWalk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        FileHandlerWalk fileWalk = new FileHandlerWalk();
        fileWalk.walkFile();
    }

}
