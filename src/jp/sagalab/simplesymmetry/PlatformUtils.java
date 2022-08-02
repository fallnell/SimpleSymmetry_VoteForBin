/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.io.File;
import java.util.Objects;

/**
 * プログラムを実行しているOSの情報に関するクラス
 * OSに寄らずにファイル保存やgnuplot出力ができるようにするために使用
 * @author sasaki
 */
public class PlatformUtils {

    /**
     * 使用しているOSがLinuxかどうかを判定
     * @return 使用しているOSがLinuxかどうか
     */
    public static boolean isLinux() {
        return OS_NAME.startsWith("linux");
    }

    /**
     * 使用しているOSがMacかどうかを判定
     * @return 使用しているOSがMacかどうか
     */
    public static boolean isMac() {
        return OS_NAME.startsWith("mac");
    }

    /**
     * 使用しているOSがwindowsがどうかを判定
     * @return 使用しているOSがwindowsがどうか
     */
    public static boolean isWindows() {

        return OS_NAME.startsWith("windows");

    }

    /**
     * 使用しているOSがSunOSかどうかを判定
     * @return 使用しているOSがSunOSかどうか
     */
    public static boolean isSunOS() {

        return OS_NAME.startsWith("sunos");
    }
    
    /**
     * ディレクトリを作成
     * @param _path ディレクトリのパス
     */
    public static void createDirectory(String _path){
        
        File newFile = new File(_path);
        if( newFile.mkdir() ){
            System.out.println("ディレクトリを作成しました．");
        }
        
    }

    public static void deleteDirectory(String _path){

        File dir = new File(_path);
        File[] files = dir.listFiles();

        System.out.println(_path);

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].delete()) {
                    System.out.println(i + "番目のファイルを削除");
                }
            }
        }
    }



    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

}
