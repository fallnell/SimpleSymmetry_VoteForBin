
package jp.sagalab.simplesymmetry;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Snap {
    private final List<Bin> m_necessityBins = new ArrayList<>();

    private int m_numOfFuzzySymmetryAxis;

    private int m_k;

    private int m_l;

    public static Snap create(){
        return new Snap();
    }

    public Snap(){
        integrationBin();
        System.out.println("NumMax of Axis:" + m_numOfFuzzySymmetryAxis);
        if(m_numOfFuzzySymmetryAxis > 0){
            int binIndex = determiningStandardBinIndex(m_necessityBins);
            int[] kl = calculateFuzzyEuclid(m_necessityBins.get(binIndex));
            m_k = kl[0];
            m_l = kl[1];
            while (m_numOfFuzzySymmetryAxis > 0){
                if(isMach(binIndex, m_numOfFuzzySymmetryAxis, m_k, m_l)){
                    break;
                }else{
                    --m_numOfFuzzySymmetryAxis;
                }
            }
            System.out.println("Standard Axis Index:" + binIndex);
            System.out.println("k:" + m_k + ", l:" + m_l);
            System.out.println("Standard Axis Angle:" + (double) m_k / m_l * 180);
        }
        System.out.println("Num of Axis:" + m_numOfFuzzySymmetryAxis);
    }

    private boolean isMach(int _index, int _numOfAxis, int _k, int _l){
        int cnt = 0;
        for(int i = -_index; i < _numOfAxis - _index; ++i){
            double width = (double)_k / _l + TreeData.m_rangeTheta[1] / _numOfAxis * i;
            while(width < 0)
                width = TreeData.m_rangeTheta[1] + width;
            while(width >= TreeData.m_rangeTheta[1])
                width = width - TreeData.m_rangeTheta[1];
            for(Bin bin : m_necessityBins){
                if(bin.getEndTheta() < bin.getBeginTheta()){
                    if((bin.getBeginTheta() <= width && width < TreeData.m_rangeTheta[1]) || (TreeData.m_rangeTheta[0] <= width && width < bin.getEndTheta())){
                        ++cnt;
                        break;
                    }
                }else{
                    if(bin.getBeginTheta() <= width && width < bin.getEndTheta()){
                        ++cnt;
                        break;
                    }
                }
            }
        }
        if(cnt == _numOfAxis){
            return true;
        }else{
            return false;
        }
    }


    private void integrationBin(){

        double beginTheta = 0.0;
        double endTheta = 0.0;

        boolean changeAnalise = false;

        double pivotTheta = TreeData.getLowestNodeWithTheta()/2 + TreeData.m_rangeTheta[0];
        while (pivotTheta < TreeData.m_rangeTheta[1]){
            double pivotRho = TreeData.getLowestNodeWithRho()/2 + TreeData.m_rangeRho[0];
            while (pivotRho < TreeData.m_rangeRho[1]){
                TreeData.NodeData leaf = TreeData.getHoughLeaf(pivotTheta, pivotRho);
                if(leaf.isHold() && !changeAnalise){
                    beginTheta = leaf.getBeginTheta();
                    endTheta = leaf.getEndTheta();
                    changeAnalise = true;
                    break;
                }else if(leaf.isHold()){
                    if(endTheta < leaf.getEndTheta()){
                        endTheta = leaf.getEndTheta();
                    }
                    break;
                }
                pivotRho += TreeData.getLowestNodeWithRho();
            }

            pivotTheta += TreeData.getLowestNodeWithTheta();
            if(changeAnalise && TreeData.m_rangeRho[1] < pivotRho){
                changeAnalise = false;
                m_necessityBins.add(Bin.createBinOnlyTheta(beginTheta, endTheta));
            }
        }
        if(changeAnalise){
            m_necessityBins.add(Bin.createBinOnlyTheta(beginTheta, endTheta));
        }

        if(m_necessityBins.size() > 0){
            if(m_necessityBins.size() > 1 && (Math.abs(m_necessityBins.get(0).getBeginTheta() - TreeData.m_rangeTheta[0]) < 10e-6 && Math.abs(m_necessityBins.get(m_necessityBins.size() - 1).getEndTheta() - TreeData.m_rangeTheta[1]) < 10e-6)){
                beginTheta = m_necessityBins.get(m_necessityBins.size() - 1).getBeginTheta();
                endTheta = m_necessityBins.get(0).getEndTheta();
                m_necessityBins.remove(0);
                m_necessityBins.remove(m_necessityBins.size() - 1);
                m_necessityBins.add(0, Bin.createBinOnlyTheta(beginTheta, endTheta));
            }
            m_numOfFuzzySymmetryAxis = m_necessityBins.size();
        }else{
            m_numOfFuzzySymmetryAxis = 0;
        }

        for(int i = 0; i < m_necessityBins.size(); ++i){
            double width = m_necessityBins.get(i).getEndTheta() - m_necessityBins.get(i).getBeginTheta();
            if(width < 0)
                System.out.println(i + "番目：(" + 180 * m_necessityBins.get(i).getBeginTheta() + ", " + 180 * m_necessityBins.get(i).getEndTheta() +"), length:" + (1 + width));
            else
                System.out.println(i + "番目：(" + 180 * m_necessityBins.get(i).getBeginTheta() + ", " + 180 * m_necessityBins.get(i).getEndTheta() +"), length:" + width);
        }

        System.out.println("Num of Symmetry Axis：" + m_numOfFuzzySymmetryAxis);

    }

    private int determiningStandardBinIndex(List<Bin> _bins){
        int binIndex = 0;
        double minWidth = TreeData.m_rangeTheta[1];
        for(int i = 0; i < m_necessityBins.size(); ++i){
            double width = m_necessityBins.get(i).getEndTheta() - m_necessityBins.get(i).getBeginTheta();
            if(width < 0){
                width += 1;
            }
            if(width < minWidth){
                minWidth = width;
                binIndex = i;
            }
        }

        return binIndex;
    }


    private int[] calculateFuzzyEuclid(Bin _standardBin){
        double a = _standardBin.getPivotTheta();
        if(_standardBin.getEndTheta() < _standardBin.getBeginTheta()){
            a = 1 - _standardBin.getEndTheta() - _standardBin.getBeginTheta();
            if(a < 0){
                a = -a;
            }
        }
        double b = TreeData.m_rangeTheta[1];

        double g = a;
        double h = b;

        List<Integer> Q = new ArrayList<>();
        int[] kl = new int[2];

        while (true){
//            System.out.println("g:" + g +", h:" + h);
            int qMax = (int)Math.floor( (g / h) + 0.5);
//            System.out.println("Q_max:" + qMax);
            int start = 0;
            int end = Math.abs(qMax);
            int tempK = 0;
            int tempL = 0;

            List<Integer> tempQ;
            while (start <= end){
                tempQ = new ArrayList<>(Q);
                int middle = (start + end) / 2;

                if(qMax < 0){
                    tempQ.add(-1 * middle);
                }else{
                    tempQ.add(middle);
                }
                int k = numerator(tempQ, tempQ.size() - 1);
                int l = denominator(tempQ, tempQ.size() - 1);

//                System.out.println("角度(" + start + ", " + Math.abs(end) + "):" + (double)k / l * 180);
//                System.out.println("middle:" + middle);

                if(l != 0){
                    if(isSame(_standardBin,(double) k / l * b)){
                        tempK = k;
                        tempL = l;
                        end = middle - 1;
                    }else{
                        start = middle + 1;

                    }
                }

            }

            if(tempL != 0){
                kl[0] = tempK;
                kl[1] = tempL;
                return kl;
            }

            Q.add(qMax);
            double r = g - qMax * h;
            g = h;
            h = r;
        }
    }

    private int numerator(List<Integer> _Q, int _i){
        if(_i >= 0){
            return _Q.get(_i) * numerator(_Q,_i - 1) + numerator(_Q, _i - 2);
        }
        if(_i == -1){
            return 1;
        }
        return 0;
    }

    private int denominator(List<Integer> _Q, int _i){
        if(_i >= 0){
            return _Q.get(_i) * denominator(_Q,_i - 1) + denominator(_Q, _i - 2);
        }
        if(_i == -1){
            return 0;
        }
        return 1;
    }

    private boolean isSame(Bin _bin, double _b){
        if(_bin.getBeginTheta() < _bin.getEndTheta()){
            if(_bin.getBeginTheta() <= _b && _b < _bin.getEndTheta()){
                return true;
            }
        }else {
            if((_bin.getBeginTheta() <= _b && _b < TreeData.m_rangeTheta[1]) || (TreeData.m_rangeTheta[0] <= _b && _b < _bin.getEndTheta())){
                return true;
            }
        }
        return false;
    }

    public SymmetricAxis getStandardAxis(){
        return SymmetricAxis.create((double) m_k / m_l,
                0,0,0,0,null,
                Color.RED, 0.0, 0.0);
    }

    public int getAxisNum(){
        return m_numOfFuzzySymmetryAxis;
    }

}
