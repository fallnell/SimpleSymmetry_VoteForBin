/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.sagalab.simplesymmetry;

import java.io.*;
import java.util.Calendar;
import java.util.List;
import javax.swing.JFrame;

/**
 * ファイル出力を取り扱うクラス
 *
 * @author sasaki
 */
public class Output extends JFrame {

    /**
     * ハフ平面の状態をcsvファイルへと出力
     *
     * @param _houghPlane ハフ平面
     */
//ρとθの分割数に対応した writeToCSV
    public static void writeToCSV1(double[][] _houghPlane) throws InterruptedException {
        String fileName = "SymmetricAxisHough" + fileNo + ".csv";
        String fileNameOnly = "SymmetricAxisHough" + fileNo;

        try {

            FileWriter fw = new FileWriter("files/planes/" + fileName, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            for (int i = 0; i < FuzzySymmetry.NUM_OF_DIVISION_ANGLES; i++) {

                pw.print(i * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES);
                pw.print("\t");
                pw.print(-800);
                pw.print("\t");
                pw.print(_houghPlane[i][0]);
                pw.println();

                for (int j = 1; j < FuzzySymmetry.NUM_OF_DIVISION_PIXELS - 1; j++) {
                    pw.print(i * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES);
                    pw.print("\t");
                    pw.print((j - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2)) * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_PIXELS);
//                    pw.print(j - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION)/2);
                    pw.print("\t");
                    pw.print(_houghPlane[i][j + 1]);
                    pw.println();
                }

                pw.print(i * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES);
                pw.print("\t");
                pw.print(800);
                pw.print("\t");
                pw.print(_houghPlane[i][FuzzySymmetry.NUM_OF_DIVISION_PIXELS - 1]);
                pw.println();

                pw.println();
            }

            pw.print(FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES * FuzzySymmetry.NUM_OF_DIVISION_ANGLES);
            pw.print("\t");
            pw.print(-800);
            pw.print("\t");
            pw.print(_houghPlane[(_houghPlane.length - 1) - (FuzzySymmetry.NUM_OF_DIVISION_ANGLES - 1)][0]);
            pw.println();

            for(int k = 1; k < FuzzySymmetry.NUM_OF_DIVISION_PIXELS - 1; k++){
                pw.print(FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES * FuzzySymmetry.NUM_OF_DIVISION_ANGLES);
                pw.print("\t");
                pw.print((k - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2)) * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_PIXELS);
//                pw.print(k - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION)/2);
                pw.print("\t");
                pw.print(_houghPlane[(_houghPlane.length - 1) - (FuzzySymmetry.NUM_OF_DIVISION_ANGLES - 1)][k + 1]);
                pw.println();
            }

            pw.print(FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES * FuzzySymmetry.NUM_OF_DIVISION_ANGLES);
            pw.print("\t");
            pw.print(800);
            pw.print("\t");
            pw.print(_houghPlane[(_houghPlane.length - 1) - (FuzzySymmetry.NUM_OF_DIVISION_ANGLES - 1)][FuzzySymmetry.NUM_OF_DIVISION_PIXELS - 1]);
            pw.println();


            pw.close();

            fileNo++;

            System.out.println("ハフ平面の出力が正常に終わりました");
            System.out.println("ファイル名:" + fileName);

            sendDataToGnuplot(fileNameOnly);

        } catch (IOException ex) {
        }

    }

    //ファジィ点ペア毎の投票結果表示用
    public static void writeToCSV2(double[][] _houghPlane, Point p1, Point p2) throws InterruptedException {
        String fileName = "SymmetricAxisHough" + fileNo + "((" + p1.getX() + "," + p1.getY() + "),(" + p2.getX() + "," + p2.getY() + "))" + ".csv";
        String fileNameOnly = "SymmetricAxisHough" + fileNo + "((" + p1.getX() + "," + p1.getY() + "),(" + p2.getX() + "," + p2.getY() + "))";

        try {

            FileWriter fw = new FileWriter("files/planes(pair)/" + fileName, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            for (int i = 0; i < FuzzySymmetry.NUM_OF_DIVISION_ANGLES; i++) {

                pw.print(i * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES);
                pw.print("\t");
                pw.print(-800);
                pw.print("\t");
                pw.print(_houghPlane[i][0]);
                pw.println();

                for (int j = 1; j < FuzzySymmetry.NUM_OF_DIVISION_PIXELS - 1; j++) {
                    pw.print(i * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES);
                    pw.print("\t");
                    pw.print((j - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2)) * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_PIXELS);
//                    pw.print(j - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION)/2);
                    pw.print("\t");
                    pw.print(_houghPlane[i][j + 1]);
                    pw.println();
                }

                pw.print(i * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES);
                pw.print("\t");
                pw.print(800);
                pw.print("\t");
                pw.print(_houghPlane[i][FuzzySymmetry.NUM_OF_DIVISION_PIXELS - 1]);
                pw.println();

                pw.println();
            }

            pw.print(FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES * FuzzySymmetry.NUM_OF_DIVISION_ANGLES);
            pw.print("\t");
            pw.print(-800);
            pw.print("\t");
            pw.print(_houghPlane[(_houghPlane.length - 1) - (FuzzySymmetry.NUM_OF_DIVISION_ANGLES - 1)][0]);
            pw.println();

            for (int k = 1; k < FuzzySymmetry.NUM_OF_DIVISION_PIXELS - 1; k++) {
                pw.print(FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES * FuzzySymmetry.NUM_OF_DIVISION_ANGLES);
                pw.print("\t");
                pw.print((k - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2)) * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_PIXELS);
//                pw.print(k - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS * FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION)/2);
                pw.print("\t");
                pw.print(_houghPlane[(_houghPlane.length - 1) - (FuzzySymmetry.NUM_OF_DIVISION_ANGLES - 1)][k + 1]);
                pw.println();
            }

            pw.print(FuzzySymmetry.MAGNIFICATION_OF_NUM_OF_DIVISION_ANGLES * FuzzySymmetry.NUM_OF_DIVISION_ANGLES);
            pw.print("\t");
            pw.print(800);
            pw.print("\t");
            pw.print(_houghPlane[(_houghPlane.length - 1) - (FuzzySymmetry.NUM_OF_DIVISION_ANGLES - 1)][FuzzySymmetry.NUM_OF_DIVISION_PIXELS - 1]);
            pw.println();


            pw.close();


            System.out.println("ハフ平面の出力が正常に終わりました");
            System.out.println("ファイル名:" + fileName);

            sendDataToGnuplot2(fileNameOnly);

        } catch (IOException ex) {
        }
    }

    //writeToCSV 初期version
    public static void writeToCSV3(double[][] _houghPlane) throws InterruptedException {
        String fileName = "SymmetricAxisHough" + fileNo + ".csv";
        String fileNameOnly = "SymmetricAxisHough" + fileNo;

        try {

            FileWriter fw = new FileWriter("files/planes/" + fileName, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            for (int i = 0; i < FuzzySymmetry.NUM_OF_DIVISION_ANGLES; i++) {
                for (int j = 0; j < FuzzySymmetry.NUM_OF_DIVISION_PIXELS; j++) {
                    pw.print(i);
                    pw.print("\t");
                    pw.print((j - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2)));
                    pw.print("\t");
                    pw.print(_houghPlane[i][j]);
                    pw.println();
                }
                pw.println();
            }

            pw.close();

            fileNo++;

            System.out.println("ハフ平面の出力が正常に終わりました");
            System.out.println("ファイル名:" + fileName);

            //sendDataToGnuplot(fileNameOnly);

        } catch (IOException ex) {
        }

    }

    /**
     * ハフ平面の状態をcsvファイルへと出力
     *
     * @param _houghPlane ハフ平面
     * @param _fileName ファイル名
     * @throws java.lang.InterruptedException
     */
    public static void writeToCSV(double[][] _houghPlane, String _fileName) throws InterruptedException {
        String fileName = _fileName + ".csv";

        try {
            FileWriter fw = new FileWriter("files/planes/" + fileName, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
                    for (int i = 0; i < FuzzySymmetry.NUM_OF_DIVISION_ANGLES; i++) {
                        for (int j = 0; j < FuzzySymmetry.NUM_OF_DIVISION_PIXELS; j++) {
                    pw.print(i);
                    pw.print("\t");
                    pw.print(j - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2));
                    pw.print("\t");
                    pw.print(_houghPlane[i][j]);
                    pw.println();
                }
                pw.println();
            }

            pw.close();

            fileNo++;

            System.out.println("ハフ平面の出力が正常に終わりました");
            System.out.println("ファイル名:" + fileName);

//            sendDataToGnuplot(_fileName);

        } catch (IOException ex) {
        }

    }

    /**
     * 点列の要素をcsvファイルへと出力
     *
     * @param _points 点列
     */
    public static void writeToCSV(List<Point> _points) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int milliSecond = calendar.get(Calendar.MILLISECOND);

        String fileName = year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + second + "_" + milliSecond + ".csv";

        try {

            FileWriter fw = new FileWriter("files/points/" + fileName, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            for (int i = 0; i < _points.size(); i++) {
                pw.print(_points.get(i).getX());
                pw.print(",");
                pw.print(_points.get(i).getY());
                pw.print(",");
                pw.print(_points.get(i).getF());
                pw.println();
            }

            pw.close();

            System.out.println("点列の出力が正常に終わりました");
            System.out.println("ファイル名:" + fileName);

        } catch (IOException ex) {
        }

    }


    /**
     * ハフ平面のデータをgnuplotへと渡してグラフをpngファイルへと出力
     *
     * @param _fileName ハフ平面のcsvファイル名
     * @throws IOException
     * @throws InterruptedException
     */
    public static void sendDataToGnuplot(String _fileName) throws IOException, InterruptedException {

        String GNUPLOT = "/usr/bin/gnuplot";

        if (PlatformUtils.isWindows()) {
            GNUPLOT = "/Program Files (x86)/gnuplot/bin/gnuplot.exe";
        } else if (PlatformUtils.isMac()) {
            GNUPLOT = "/usr/local/bin/gnuplot";
        }

        Process p = new ProcessBuilder(GNUPLOT, "-").start();
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(p.getOutputStream())));

        out.println("set terminal png");
        out.println("set output './files/planesPicture/" + _fileName + ".png'");
        out.println("set xrange [0:360]");
//        out.println("set yrange [-800:800]");
        out.println("set yrange [-" + FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2 + ":" + FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2 + "]");
        out.println("set view 0, 0, 1, 1");
        out.println("unset ztics");
        out.println("set xlabel 't (Voting point on the θ axis)'");
        out.println("set label 1 's (Voting point on the ρ axis)' at 423,700 right");
        out.println("set label 1 rotate by 90");
        out.println("splot './files/planes/" + _fileName + ".csv' with pm3d" + " title '" + _fileName + "'");
        if (PlatformUtils.isWindows()) {
            out.println("set terminal windows");
        } else {
            out.println("set terminal x11");
        }
        out.println("set output");

        out.close();

        try {
            p.waitFor();
        } catch (InterruptedException e) {

        }

    }

    public static void sendDataToGnuplot2(String _fileName) throws IOException, InterruptedException {

        String GNUPLOT = "/usr/bin/gnuplot";

        if (PlatformUtils.isWindows()) {
            GNUPLOT = "/Program Files (x86)/gnuplot/bin/gnuplot.exe";
        } else if (PlatformUtils.isMac()) {
            GNUPLOT = "/usr/local/bin/gnuplot";
        }

        Process p = new ProcessBuilder(GNUPLOT, "-").start();
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(p.getOutputStream())));

        out.println("set terminal png");
        out.println("set output './files/planesPicture(pair)/" + _fileName + ".png'");
        out.println("set xrange [0:360]");
//        out.println("set yrange [-800:800]");
        out.println("set yrange [-" + FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2 + ":" + FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2 + "]");
        out.println("set view 0, 0, 1, 1");
        out.println("unset ztics");
        out.println("set xlabel 't (Voting point on the θ axis)'");
        out.println("set label 1 's (Voting point on the ρ axis)' at 423,700 right");
        out.println("set label 1 rotate by 90");
        out.println("splot './files/planes(pair)/" + _fileName + ".csv' with pm3d" + " title '" + _fileName + "'");
        if (PlatformUtils.isWindows()) {
            out.println("set terminal windows");
        } else {
            out.println("set terminal x11");
        }
        out.println("set output");

        out.close();

        try {
            p.waitFor();
        } catch (InterruptedException e) {

        }

    }

    /**
     * 最新のファイル名を返す
     *
     * @return 最新のファイル名を返す
     *
     */
    public static String getFileName() {
        return "SymmetricAxisHough" + (fileNo - 1);
    }
    public static String getFileName2() {
        return "SymmetricAxis" + (fileNo - 1);
    }

    //
    public static void writeTimeDataToTxt(List<Double> _data, String _fileName) {
        try {

            //書き出し先
            FileWriter fw = new FileWriter("C:\\Users\\sasaki\\Dropbox\\journalThesis\\AustraliaEdmonds\\data\\" + _fileName + ".txt", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            for (int i = 0; i < _data.size(); i++) {
                pw.println(_data.get(i));
            }
            pw.println("*");
            pw.close();

            System.out.println("曲線列の出力が正常に終わりました");
            System.out.println("ファイル名:" + _fileName +".txt");

        } catch (IOException ex) {
        }

    }

    /**
     * ファイル名の通し番号
     */
    private static int fileNo = 0;
}
