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
 * @author sasaki
 */
public class Output extends JFrame {

    /**
     * ハフ平面の状態をcsvファイルへと出力
     */
//ρとθの分割数に対応した writeToCSV
    public static void writeToCSVPossibility(int _fileNo) throws InterruptedException {
        String fileName = "SymmetricAxisPossibilityHough" + _fileNo + ".csv";
        String fileNameOnly = "SymmetricAxisPossibilityHough" + _fileNo;

        try {

            FileWriter fw = new FileWriter("files/csv/planesPossibility/" + fileName, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            double withTheta  = TreeData.getLowestNodeWithTheta();
            double withRho = TreeData.getLowestNodeWithRho();

            double beginTheta;
            double endTheta;
            double beginRho;
            double endRho;

            int skipCnt;

            pw.print(0 + "\t");
            pw.print(0 + "\t");
            pw.print(0);
            pw.println();
            pw.println();
            pw.print(0 + "\t");
            pw.print(0 + "\t");
            pw.print(1);
            pw.println();
            pw.println();

            for(double thetaPivot = withTheta/2; thetaPivot < TreeData.m_rangeTheta[1]; thetaPivot += withTheta) {
                beginTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot - withTheta/2);
                endTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot + withTheta/2);
                for(int k = 0; k < 2; ++k){
                    TreeData.NodeData leaf = TreeData.getHoughLeaf(thetaPivot, withRho/2 + TreeData.m_rangeRho[0]);

                    beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                    endRho = FuzzySymmetry.R * leaf.getEndRho();

                    if(k == 0){
                        pw.print(beginTheta + "\t");
                        pw.print(beginRho + "\t");
                        pw.print(leaf.getPossibility());
                        pw.println();
                        pw.print(beginTheta + "\t");
                        pw.print(endRho + "\t");
                        pw.print(leaf.getPossibility());
                        pw.println();
                    }else{
                        pw.print(endTheta + "\t");
                        pw.print(beginRho + "\t");
                        pw.print(leaf.getPossibility());
                        pw.println();
                        pw.print(endTheta + "\t");
                        pw.print(endRho + "\t");
                        pw.print(leaf.getPossibility());
                        pw.println();
                    }

                    skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                    for (double rhoPivot = (skipCnt + 0.5) * withRho + TreeData.m_rangeRho[0]; rhoPivot < TreeData.m_rangeRho[1] - withRho; rhoPivot += skipCnt * withRho) {
                        leaf = TreeData.getHoughLeaf(thetaPivot, rhoPivot);

                        beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                        endRho = FuzzySymmetry.R * leaf.getEndRho();

                        if(k == 0){
                            pw.print(beginTheta + "\t");
                            pw.print(beginRho + "\t");
                            pw.print(leaf.getPossibility());
                            pw.println();
                            pw.print(beginTheta + "\t");
                            pw.print(endRho + "\t");
                            pw.print(leaf.getPossibility());
                            pw.println();
                        }else{
                            pw.print(endTheta + "\t");
                            pw.print(beginRho + "\t");
                            pw.print(leaf.getPossibility());
                            pw.println();
                            pw.print(endTheta + "\t");
                            pw.print(endRho + "\t");
                            pw.print(leaf.getPossibility());
                            pw.println();
                        }
                        skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                    }
                    leaf = TreeData.getHoughLeaf(thetaPivot, -withRho/2 + TreeData.m_rangeRho[1]);

                    beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                    endRho = FuzzySymmetry.R * leaf.getEndRho();

                    if(k == 0){
                        pw.print(beginTheta + "\t");
                        pw.print(beginRho + "\t");
                        pw.print(leaf.getPossibility());
                        pw.println();
                        pw.print(beginTheta + "\t");
                        pw.print(endRho + "\t");
                        pw.print(leaf.getPossibility());
                        pw.println();
                    }else{
                        pw.print(endTheta + "\t");
                        pw.print(beginRho + "\t");
                        pw.print(leaf.getPossibility());
                        pw.println();
                        pw.print(endTheta + "\t");
                        pw.print(endRho + "\t");
                        pw.print(leaf.getPossibility());
                        pw.println();
                    }
                    pw.println();
                }
            }

            pw.close();

            System.out.println("ハフ平面(Possibility)CSVへの出力が正常に終わりました");
            sendDataToGnuplotPossibility(fileNameOnly);
            System.out.println("ハフ平面(Possibility)PNGへの出力が正常に終わりました");
            System.out.println("ファイル名:" + fileName + "\n");

        } catch (IOException ex) {
        }
    }

    public static void writeToCSVNecessityNodeOnlyHold(int _fileNo) throws InterruptedException {

        try {
            for(int i = 0; i <= TreeData.getThisFloor(); ++i){
                String fileName = "SymmetricAxisNecessityHoughOnlyHold" + _fileNo + "Node" + i + ".csv";
                String fileNameOnly = "SymmetricAxisNecessityHoughOnlyHold" + _fileNo  + "Node" + i;

                FileWriter fw = new FileWriter("files/csv/planesNecessityNodeOnlyHold/" + fileName, false);
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

                double withTheta  = TreeData.getLowestNodeWithTheta();
                double withRho = TreeData.getLowestNodeWithRho();

                double beginTheta;
                double endTheta;
                double beginRho;
                double endRho;

                int skipCnt;

                pw.print(0 + "\t");
                pw.print(0 + "\t");
                pw.print(0);
                pw.println();
                pw.println();
                pw.print(0 + "\t");
                pw.print(0 + "\t");
                pw.print(1);
                pw.println();
                pw.println();

                for(double thetaPivot = withTheta/2; thetaPivot < TreeData.m_rangeTheta[1]; thetaPivot += withTheta) {
                    beginTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot - withTheta/2);
                    endTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot + withTheta/2);
                    for(int k = 0; k < 2; ++k){
                        TreeData.NodeData leaf = TreeData.getHoughLeaf(thetaPivot, withRho/2 + TreeData.m_rangeRho[0]);

                        beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                        endRho = FuzzySymmetry.R * leaf.getEndRho();

                        if(k == 0){
                            pw.print(beginTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.isHold() && leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(beginTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.isHold() && leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }else{
                            pw.print(endTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.isHold() && leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(endTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.isHold() && leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }

                        skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                        for (double rhoPivot = (skipCnt + 0.5) * withRho + TreeData.m_rangeRho[0]; rhoPivot < TreeData.m_rangeRho[1] - withRho; rhoPivot += skipCnt * withRho) {
                            leaf = TreeData.getHoughLeaf(thetaPivot, rhoPivot);

                            beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                            endRho = FuzzySymmetry.R * leaf.getEndRho();

                            if(k == 0){
                                pw.print(beginTheta + "\t");
                                pw.print(beginRho + "\t");
                                if(leaf.isHold() && leaf.getNode() == i){
                                    pw.print(leaf.getNecessity());
                                }else {
                                    pw.print(0);
                                }
                                pw.println();
                                pw.print(beginTheta + "\t");
                                pw.print(endRho + "\t");
                                if(leaf.isHold() && leaf.getNode() == i){
                                    pw.print(leaf.getNecessity());
                                }else {
                                    pw.print(0);
                                }
                                pw.println();
                            }else{
                                pw.print(endTheta + "\t");
                                pw.print(beginRho + "\t");
                                if(leaf.isHold() && leaf.getNode() == i){
                                    pw.print(leaf.getNecessity());
                                }else {
                                    pw.print(0);
                                }
                                pw.println();
                                pw.print(endTheta + "\t");
                                pw.print(endRho + "\t");
                                if(leaf.isHold() && leaf.getNode() == i){
                                    pw.print(leaf.getNecessity());
                                }else {
                                    pw.print(0);
                                }
                                pw.println();
                            }
                            skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                        }
                        leaf = TreeData.getHoughLeaf(thetaPivot, -withRho/2 + TreeData.m_rangeRho[1]);

                        beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                        endRho = FuzzySymmetry.R * leaf.getEndRho();

                        if(k == 0){
                            pw.print(beginTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.isHold() && leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(beginTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.isHold() && leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }else{
                            pw.print(endTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.isHold() && leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(endTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.isHold() && leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }
                        pw.println();
                    }
                }

                pw.close();

                System.out.println("ハフ平面(Necessity)CSVへの出力が正常に終わりました");
                sendDataToGnuplotNecessityNodeOnlyHold(fileNameOnly);
                System.out.println("ハフ平面(Necessity)PNGへの出力が正常に終わりました");
                System.out.println("ファイル名:" + fileName + "\n");
            }

        } catch (IOException ex) {
        }
    }

    public static void writeToCSVNecessityNode(int _fileNo) throws InterruptedException {

        try {
            for(int i = 0; i <= TreeData.getThisFloor(); ++i){
                String fileName = "SymmetricAxisNecessityHough" + _fileNo + "Node" + i + ".csv";
                String fileNameOnly = "SymmetricAxisNecessityHough" + _fileNo  + "Node" + i;

                FileWriter fw = new FileWriter("files/csv/planesNecessityNode/" + fileName, false);
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

                double withTheta  = TreeData.getLowestNodeWithTheta();
                double withRho = TreeData.getLowestNodeWithRho();

                double beginTheta;
                double endTheta;
                double beginRho;
                double endRho;

                int skipCnt;

                pw.print(0 + "\t");
                pw.print(0 + "\t");
                pw.print(0);
                pw.println();
                pw.println();
                pw.print(0 + "\t");
                pw.print(0 + "\t");
                pw.print(1);
                pw.println();
                pw.println();

                for(double thetaPivot = withTheta/2; thetaPivot < TreeData.m_rangeTheta[1]; thetaPivot += withTheta) {
                    beginTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot - withTheta/2);
                    endTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot + withTheta/2);
                    for(int k = 0; k < 2; ++k){
                        TreeData.NodeData leaf = TreeData.getHoughLeaf(thetaPivot, withRho/2 + TreeData.m_rangeRho[0]);

                        beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                        endRho = FuzzySymmetry.R * leaf.getEndRho();

                        if(k == 0){
                            pw.print(beginTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(beginTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }else{
                            pw.print(endTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(endTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }

                        skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                        for (double rhoPivot = (skipCnt + 0.5) * withRho + TreeData.m_rangeRho[0]; rhoPivot < TreeData.m_rangeRho[1] - withRho; rhoPivot += skipCnt * withRho) {
                            leaf = TreeData.getHoughLeaf(thetaPivot, rhoPivot);

                            beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                            endRho = FuzzySymmetry.R * leaf.getEndRho();

                            if(k == 0){
                                pw.print(beginTheta + "\t");
                                pw.print(beginRho + "\t");
                                if(leaf.getNode() == i){
                                    pw.print(leaf.getNecessity());
                                }else {
                                    pw.print(0);
                                }
                                pw.println();
                                pw.print(beginTheta + "\t");
                                pw.print(endRho + "\t");
                                if(leaf.getNode() == i){
                                    pw.print(leaf.getNecessity());
                                }else {
                                    pw.print(0);
                                }
                                pw.println();
                            }else{
                                pw.print(endTheta + "\t");
                                pw.print(beginRho + "\t");
                                if(leaf.getNode() == i){
                                    pw.print(leaf.getNecessity());
                                }else {
                                    pw.print(0);
                                }
                                pw.println();
                                pw.print(endTheta + "\t");
                                pw.print(endRho + "\t");
                                if(leaf.getNode() == i){
                                    pw.print(leaf.getNecessity());
                                }else {
                                    pw.print(0);
                                }
                                pw.println();
                            }
                            skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                        }
                        leaf = TreeData.getHoughLeaf(thetaPivot, -withRho/2 + TreeData.m_rangeRho[1]);

                        beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                        endRho = FuzzySymmetry.R * leaf.getEndRho();

                        if(k == 0){
                            pw.print(beginTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(beginTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }else{
                            pw.print(endTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(endTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.getNode() == i){
                                pw.print(leaf.getNecessity());
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }
                        pw.println();
                    }
                }

                pw.close();

                System.out.println("ハフ平面(Necessity)CSVへの出力が正常に終わりました");
                sendDataToGnuplotNecessityNode(fileNameOnly);
                System.out.println("ハフ平面(Necessity)PNGへの出力が正常に終わりました");
                System.out.println("ファイル名:" + fileName + "\n");
            }

        } catch (IOException ex) {
        }
    }

    public static void writeToCSVNecessityOnlyHold(int _fileNo) throws InterruptedException {

        try {
            String fileName = "SymmetricAxisNecessityHoughOnlyHold" + _fileNo + ".csv";
            String fileNameOnly = "SymmetricAxisNecessityHoughOnlyHold" + _fileNo;

            FileWriter fw = new FileWriter("files/csv/planesNecessityOnlyHold/" + fileName, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            double withTheta  = TreeData.getLowestNodeWithTheta();
            double withRho = TreeData.getLowestNodeWithRho();

            double beginTheta;
            double endTheta;
            double beginRho;
            double endRho;

            int skipCnt;

            pw.print(0 + "\t");
            pw.print(0 + "\t");
            pw.print(0);
            pw.println();
            pw.println();
            pw.print(0 + "\t");
            pw.print(0 + "\t");
            pw.print(1);
            pw.println();
            pw.println();

            for(double thetaPivot = withTheta/2; thetaPivot < TreeData.m_rangeTheta[1]; thetaPivot += withTheta) {
                beginTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot - withTheta/2);
                endTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot + withTheta/2);
                for(int k = 0; k < 2; ++k){
                    TreeData.NodeData leaf = TreeData.getHoughLeaf(thetaPivot, withRho/2 + TreeData.m_rangeRho[0]);

                    beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                    endRho = FuzzySymmetry.R * leaf.getEndRho();

                    if(k == 0){
                        pw.print(beginTheta + "\t");
                        pw.print(beginRho + "\t");
                        if(leaf.isHold()){
                            pw.print(0.6);
                        }else {
                            pw.print(0);
                        }
                        pw.println();
                        pw.print(beginTheta + "\t");
                        pw.print(endRho + "\t");
                        if(leaf.isHold()){
                            pw.print(0.6);
                        }else {
                            pw.print(0);
                        }
                        pw.println();
                    }else{
                        pw.print(endTheta + "\t");
                        pw.print(beginRho + "\t");
                        if(leaf.isHold()){
                            pw.print(0.6);
                        }else {
                            pw.print(0);
                        }
                        pw.println();
                        pw.print(endTheta + "\t");
                        pw.print(endRho + "\t");
                        if(leaf.isHold()){
                            pw.print(0.6);
                        }else {
                            pw.print(0);
                        }
                        pw.println();
                    }

                    skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                    for (double rhoPivot = (skipCnt + 0.5) * withRho + TreeData.m_rangeRho[0]; rhoPivot < TreeData.m_rangeRho[1] - withRho; rhoPivot += skipCnt * withRho) {
                        leaf = TreeData.getHoughLeaf(thetaPivot, rhoPivot);

                        beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                        endRho = FuzzySymmetry.R * leaf.getEndRho();

                        if(k == 0){
                            pw.print(beginTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.isHold()){
                                pw.print(0.6);
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(beginTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.isHold()){
                                pw.print(0.6);
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }else{
                            pw.print(endTheta + "\t");
                            pw.print(beginRho + "\t");
                            if(leaf.isHold()){
                                pw.print(0.6);
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                            pw.print(endTheta + "\t");
                            pw.print(endRho + "\t");
                            if(leaf.isHold()){
                                pw.print(0.6);
                            }else {
                                pw.print(0);
                            }
                            pw.println();
                        }
                        skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                    }
                    leaf = TreeData.getHoughLeaf(thetaPivot, -withRho/2 + TreeData.m_rangeRho[1]);

                    beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                    endRho = FuzzySymmetry.R * leaf.getEndRho();

                    if(k == 0){
                        pw.print(beginTheta + "\t");
                        pw.print(beginRho + "\t");
                        if(leaf.isHold()){
                            pw.print(0.6);
                        }else {
                            pw.print(0);
                        }
                        pw.println();
                        pw.print(beginTheta + "\t");
                        pw.print(endRho + "\t");
                        if(leaf.isHold()){
                            pw.print(0.6);
                        }else {
                            pw.print(0);
                        }
                        pw.println();
                    }else{
                        pw.print(endTheta + "\t");
                        pw.print(beginRho + "\t");
                        if(leaf.isHold()){
                            pw.print(0.6);
                        }else {
                            pw.print(0);
                        }
                        pw.println();
                        pw.print(endTheta + "\t");
                        pw.print(endRho + "\t");
                        if(leaf.isHold()){
                            pw.print(0.6);
                        }else {
                            pw.print(0);
                        }
                        pw.println();
                    }
                    pw.println();
                }
            }

            pw.close();

            sendDataToGnuplotNecessityOnlyHold(fileNameOnly);
            System.out.println("ファイル名:" + fileName + "\n");

        } catch (IOException ex) {
        }
    }

    public static void writeToCSVNecessity(int _fileNo) throws InterruptedException {

        try {
            String fileName = "SymmetricAxisNecessityHough" + _fileNo + ".csv";
            String fileNameOnly = "SymmetricAxisNecessityHough" + _fileNo;

            FileWriter fw = new FileWriter("files/csv/planesNecessity/" + fileName, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            double withTheta  = TreeData.getLowestNodeWithTheta();
            double withRho = TreeData.getLowestNodeWithRho();

            double beginTheta;
            double endTheta;
            double beginRho;
            double endRho;

            int skipCnt;

            pw.print(0 + "\t");
            pw.print(0 + "\t");
            pw.print(0);
            pw.println();
            pw.println();
            pw.print(0 + "\t");
            pw.print(0 + "\t");
            pw.print(1);
            pw.println();
            pw.println();

            for(double thetaPivot = withTheta/2; thetaPivot < TreeData.m_rangeTheta[1]; thetaPivot += withTheta) {
                beginTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot - withTheta/2);
                endTheta = FuzzySymmetry.MAX_THETA/Math.PI * 180 * (thetaPivot + withTheta/2);
                for(int k = 0; k < 2; ++k){
                    TreeData.NodeData leaf = TreeData.getHoughLeaf(thetaPivot, withRho/2 + TreeData.m_rangeRho[0]);

                    beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                    endRho = FuzzySymmetry.R * leaf.getEndRho();

                    if(k == 0){
                        pw.print(beginTheta + "\t");
                        pw.print(beginRho + "\t");
                        pw.print(leaf.getNecessity());
                        pw.println();
                        pw.print(beginTheta + "\t");
                        pw.print(endRho + "\t");
                        pw.print(leaf.getNecessity());
                        pw.println();
                    }else{
                        pw.print(endTheta + "\t");
                        pw.print(beginRho + "\t");
                        pw.print(leaf.getNecessity());
                        pw.println();
                        pw.print(endTheta + "\t");
                        pw.print(endRho + "\t");
                        pw.print(leaf.getNecessity());
                        pw.println();
                    }

                    skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                    for (double rhoPivot = (skipCnt + 0.5) * withRho + TreeData.m_rangeRho[0]; rhoPivot < TreeData.m_rangeRho[1] - withRho; rhoPivot += skipCnt * withRho) {
                        leaf = TreeData.getHoughLeaf(thetaPivot, rhoPivot);

                        beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                        endRho = FuzzySymmetry.R * leaf.getEndRho();

                        if(k == 0){
                            pw.print(beginTheta + "\t");
                            pw.print(beginRho + "\t");
                            pw.print(leaf.getNecessity());
                            pw.println();
                            pw.print(beginTheta + "\t");
                            pw.print(endRho + "\t");
                            pw.print(leaf.getNecessity());
                            pw.println();
                        }else{
                            pw.print(endTheta + "\t");
                            pw.print(beginRho + "\t");
                            pw.print(leaf.getNecessity());
                            pw.println();
                            pw.print(endTheta + "\t");
                            pw.print(endRho + "\t");
                            pw.print(leaf.getNecessity());
                            pw.println();
                        }
                        skipCnt = (int)((leaf.getEndRho() - leaf.getBeginRho()) / withRho);

                    }
                    leaf = TreeData.getHoughLeaf(thetaPivot, -withRho/2 + TreeData.m_rangeRho[1]);

                    beginRho = FuzzySymmetry.R * leaf.getBeginRho();
                    endRho = FuzzySymmetry.R * leaf.getEndRho();

                    if(k == 0){
                        pw.print(beginTheta + "\t");
                        pw.print(beginRho + "\t");
                        pw.print(leaf.getNecessity());
                        pw.println();
                        pw.print(beginTheta + "\t");
                        pw.print(endRho + "\t");
                        pw.print(leaf.getNecessity());
                        pw.println();
                    }else{
                        pw.print(endTheta + "\t");
                        pw.print(beginRho + "\t");
                        pw.print(leaf.getNecessity());
                        pw.println();
                        pw.print(endTheta + "\t");
                        pw.print(endRho + "\t");
                        pw.print(leaf.getNecessity());
                        pw.println();
                    }
                    pw.println();
                }
            }

            pw.close();

            sendDataToGnuplotNecessity(fileNameOnly);
            System.out.println("ファイル名:" + fileName + "\n");

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
//    public static void writeToCSV(double[][] _houghPlane, String _fileName) throws InterruptedException {
//        String fileName = _fileName + ".csv";
//
//        try {
//            FileWriter fw = new FileWriter("files/planes/" + fileName, false);
//            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
//                    for (int i = 0; i < FuzzySymmetry.NUM_OF_DIVISION_ANGLES; i++) {
//                        for (int j = 0; j < FuzzySymmetry.NUM_OF_DIVISION_PIXELS; j++) {
//                    pw.print(i);
//                    pw.print("\t");
//                    pw.print(j - (FuzzySymmetry.NUM_OF_DIVISION_PIXELS / 2));
//                    pw.print("\t");
//                    pw.print(_houghPlane[i][j]);
//                    pw.println();
//                }
//                pw.println();
//            }
//
//            pw.close();
//
//            fileNo++;
//
//            System.out.println("ハフ平面の出力が正常に終わりました");
//            System.out.println("ファイル名:" + fileName);
//
////            sendDataToGnuplot(_fileName);
//
//        } catch (IOException ex) {
//        }
//
//    }

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
    public static void sendDataToGnuplotPossibility(String _fileName) throws IOException, InterruptedException {

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
        out.println("set output './files/pictures/possibilityHoughPicture/" + _fileName + ".png'");
        out.println("set xrange [0:" + FuzzySymmetry.MAX_THETA / Math.PI * 180 + "]");
        out.println("set yrange [-" + FuzzySymmetry.R + ":" + FuzzySymmetry.R + "]");
        out.println("set view 0, 0, 1, 1");
        out.println("unset ztics");
        out.println("set xlabel 't (Voting point on the θ axis)'");
        out.println("set label 1 's (Voting point on the ρ axis)' at 423,700 right");
        out.println("set label 1 rotate by 90");
        out.println("splot './files/csv/planesPossibility/" + _fileName + ".csv' with pm3d" + " title '" + _fileName + "'");
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

    public static void sendDataToGnuplotNecessityOnlyHold(String _fileName) throws IOException, InterruptedException {

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
        out.println("set output './files/pictures/necessityHoughOnlyHoldPicture/" + _fileName + ".png'");
        out.println("set xrange [0:" + FuzzySymmetry.MAX_THETA / Math.PI * 180 + "]");
        out.println("set yrange [-" + FuzzySymmetry.R + ":" + FuzzySymmetry.R + "]");
        out.println("set view 0, 0, 1, 1");
        out.println("unset ztics");
        out.println("set xlabel 't (Voting point on the θ axis)'");
        out.println("set label 1 's (Voting point on the ρ axis)' at 423,700 right");
        out.println("set label 1 rotate by 90");
        out.println("splot './files/csv/planesNecessityOnlyHold/" + _fileName + ".csv' with pm3d" + " title '" + _fileName + "'");
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

    public static void sendDataToGnuplotNecessity(String _fileName) throws IOException, InterruptedException {

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
        out.println("set output './files/pictures/necessityHoughPicture/" + _fileName + ".png'");
        out.println("set xrange [0:" + FuzzySymmetry.MAX_THETA / Math.PI * 180 + "]");
        out.println("set yrange [-" + FuzzySymmetry.R + ":" + FuzzySymmetry.R + "]");
        out.println("set view 0, 0, 1, 1");
        out.println("unset ztics");
        out.println("set xlabel 't (Voting point on the θ axis)'");
        out.println("set label 1 's (Voting point on the ρ axis)' at 423,700 right");
        out.println("set label 1 rotate by 90");
        out.println("splot './files/csv/planesNecessity/" + _fileName + ".csv' with pm3d" + " title '" + _fileName + "'");
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

    public static void sendDataToGnuplotNecessityNodeOnlyHold(String _fileName) throws IOException, InterruptedException {

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
        out.println("set output './files/pictures/necessityHoughNodeOnlyHoldPicture/" + _fileName + ".png'");
        out.println("set xrange [0:" + FuzzySymmetry.MAX_THETA / Math.PI * 180 + "]");
        out.println("set yrange [-" + FuzzySymmetry.R + ":" + FuzzySymmetry.R + "]");
        out.println("set view 0, 0, 1, 1");
        out.println("unset ztics");
        out.println("set xlabel 't (Voting point on the θ axis)'");
        out.println("set label 1 's (Voting point on the ρ axis)' at 423,700 right");
        out.println("set label 1 rotate by 90");
        out.println("splot './files/csv/planesNecessityNodeOnlyHold/" + _fileName + ".csv' with pm3d" + " title '" + _fileName + "'");
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

    public static void sendDataToGnuplotNecessityNode(String _fileName) throws IOException, InterruptedException {

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
        out.println("set output './files/pictures/necessityHoughNodePicture/" + _fileName + ".png'");
        out.println("set xrange [0:" + FuzzySymmetry.MAX_THETA / Math.PI * 180 + "]");
        out.println("set yrange [-" + FuzzySymmetry.R + ":" + FuzzySymmetry.R + "]");
        out.println("set view 0, 0, 1, 1");
        out.println("unset ztics");
        out.println("set xlabel 't (Voting point on the θ axis)'");
        out.println("set label 1 's (Voting point on the ρ axis)' at 423,700 right");
        out.println("set label 1 rotate by 90");
        out.println("splot './files/csv/planesNecessityNode/" + _fileName + ".csv' with pm3d" + " title '" + _fileName + "'");
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

//    public static void sendDataToGnuplot2(String _fileName) throws IOException, InterruptedException {
//
//        String GNUPLOT = "/usr/bin/gnuplot";
//
//        if (PlatformUtils.isWindows()) {
//            GNUPLOT = "/Program Files (x86)/gnuplot/bin/gnuplot.exe";
//        } else if (PlatformUtils.isMac()) {
//            GNUPLOT = "/usr/local/bin/gnuplot";
//        }
//
//        Process p = new ProcessBuilder(GNUPLOT, "-").start();
//        PrintWriter out = new PrintWriter(new BufferedWriter(
//                new OutputStreamWriter(p.getOutputStream())));
//
//        out.println("set terminal png");
//        out.println("set output './files/planesPicture(pair)/" + _fileName + ".png'");
//        out.println("set xrange [0:360]");
////        out.println("set yrange [-800:800]");
//        out.println("set yrange [-" + FuzzySymmetry.m_NumOfDivision / 2 + ":" + FuzzySymmetry.m_NumOfDivision / 2 + "]");
//        out.println("set view 0, 0, 1, 1");
//        out.println("unset ztics");
//        out.println("set xlabel 't (Voting point on the θ axis)'");
//        out.println("set label 1 's (Voting point on the ρ axis)' at 423,700 right");
//        out.println("set label 1 rotate by 90");
//        out.println("splot './files/planes(pair)/" + _fileName + ".csv' with pm3d" + " title '" + _fileName + "'");
//        if (PlatformUtils.isWindows()) {
//            out.println("set terminal windows");
//        } else {
//            out.println("set terminal x11");
//        }
//        out.println("set output");
//
//        out.close();
//
//        try {
//            p.waitFor();
//        } catch (InterruptedException e) {
//
//        }
//
//    }

    /**
     * 最新のファイル名を返す
     *
     * @return 最新のファイル名を返す
     *
     */
    public static String getFileName(int _fileNo) {
        return "SymmetricAxisHough" + _fileNo;
    }
    public static String getFileName2(int _fileNo) {
        return "SymmetricAxis" + _fileNo;
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
//    private static int fileNo = 0;
}
