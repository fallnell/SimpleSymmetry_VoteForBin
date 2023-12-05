package jp.sagalab.simplesymmetry;

import java.util.ArrayList;
import java.util.List;

/**
 * 投票結果を管理するクラス
 */
public class TreeData {
    /** ハフ投票結果 */
    private static List<NodeData> m_dataTree = new ArrayList<>();
//    /** 各階層の投票結果を保存したリストの最初のインデックス */
//    private static List<Integer> m_startOfFloorNodeIndex = new ArrayList<>();

    /** θの分割する数 */
    public static final int THETA_SPLIT_NUM = 2;

    /** ρの分割する数 */
    public static final int RHO_SPLIT_NUM = 2;

    public static final int SPLIT_TYPE_ALL = 0;

    public static final int SPLIT_TYPE_THETA = 1;

    public static final int SPLIT_TYPE_RHO = 2;

    public static final int SPLIT_TYPE = SPLIT_TYPE_ALL;


    /** 投票を行う階層 */
    private static int m_thisFloor = 0;

    public static final double[] m_rangeTheta = {0, 1};

    public static final double[] m_rangeRho = {-1, 1};

    private static double m_widthTheta;

    private static double m_widthRho;

    public static TreeData createStartHoughPlane(){
        return new TreeData();
    }

    public TreeData(){
        //投票データの初期化
        m_thisFloor = 0;
        m_dataTree.clear();

        m_widthTheta = Math.abs(m_rangeTheta[1] - m_rangeTheta[0]);
        m_widthRho = Math.abs(m_rangeRho[1] - m_rangeRho[0]);
//        m_startOfFloorNodeIndex.clear();

        //ルートノードの作成
        m_dataTree.add(new NodeData(0,
                m_rangeTheta[0], m_rangeTheta[1],
                m_rangeRho[0], m_rangeRho[1], -1, 0, SPLIT_TYPE));
        m_dataTree.get(0).setData(1.0, 0.0);
//        m_startOfFloorNodeIndex.add(0);
//        m_startOfFloorNodeIndex.add(m_dataTree.size());

        //子要素の作成（データ未入力）
        addNode(0);
        ++m_thisFloor;
//        m_startOfFloorNodeIndex.add(m_dataTree.size());
    }

    /**
     * データを入力する
     * @param _childIndex 子要素のインデックス
     * @param _possibility 可能性値
     * @param _necessity 必然性値
     */
    public static void setData(int _childIndex, double _possibility, double _necessity){
        //データ入力
        m_dataTree.get(_childIndex).setData(_possibility, _necessity);
        //子要素の作成（データ未入力）
        if(m_dataTree.get(_childIndex).m_isChildHave){
            addNode(_childIndex);
        }
    }

    /**
     * ノードを追加する
     * @param _parentIndex 親のインデックス
     */
    private static void addNode(int _parentIndex){
        int houghSize = m_dataTree.size();
        int node = m_dataTree.get(_parentIndex).getNode() + 1;

        double beginTheta = m_dataTree.get(_parentIndex).getBeginTheta();
        double endTheta = m_dataTree.get(_parentIndex).getEndTheta();
        double beginRho = m_dataTree.get(_parentIndex).getBeginRho();
        double endRho = m_dataTree.get(_parentIndex).getEndRho();

        double rangeTheta = (endTheta - beginTheta) / THETA_SPLIT_NUM;
        double rangeRho = (endRho - beginRho) / RHO_SPLIT_NUM;

        double theta = beginTheta;
        double rho = beginRho;

        //次のノードの分割タイプに応じて場合分け
        if(m_dataTree.get(_parentIndex).getSplitType() == SPLIT_TYPE_THETA){
            //θ方向に分割
            for(int i = 0; i < THETA_SPLIT_NUM; ++i){
                m_dataTree.add(new NodeData(node, theta, theta + rangeTheta,
                        beginRho, endRho, _parentIndex, m_dataTree.size(), SPLIT_TYPE_RHO));
                theta += rangeTheta;
            }
            for(int i = houghSize; i < houghSize + THETA_SPLIT_NUM; ++i){
                m_dataTree.get(_parentIndex).setChildIndex(i);
            }
        }else if(m_dataTree.get(_parentIndex).getSplitType() == SPLIT_TYPE_RHO){
            //ρ方向に分割
            for(int i = 0; i < RHO_SPLIT_NUM; ++i){
                m_dataTree.add(new NodeData(node, beginTheta, endTheta,
                        rho, rho + rangeRho, _parentIndex, m_dataTree.size(), SPLIT_TYPE_THETA));
                rho += rangeRho;
            }
            for(int i = houghSize; i < houghSize + RHO_SPLIT_NUM; ++i){
                m_dataTree.get(_parentIndex).setChildIndex(i);
            }
        }else{
            //θ-ρに分割
            for(int i = 0; i < THETA_SPLIT_NUM; ++i){
                for(int j = 0; j < RHO_SPLIT_NUM; ++j){
                    m_dataTree.add(new NodeData(node, theta, theta + rangeTheta,
                            rho, rho + rangeRho, _parentIndex, m_dataTree.size(), SPLIT_TYPE_ALL));
                    rho += rangeRho;
                }
                rho = beginRho;
                theta += rangeTheta;
            }
            for(int i = houghSize; i < houghSize + THETA_SPLIT_NUM * RHO_SPLIT_NUM; ++i){
                m_dataTree.get(_parentIndex).setChildIndex(i);
            }
        }
//        m_dataTree.add(new NodeData(node, beginTheta, beginTheta + rangeTheta,
//                beginRho, beginRho + rangeRho, _parentIndex, m_dataTree.size()));
//        m_dataTree.add(new NodeData(node, beginTheta, beginTheta + rangeTheta,
//                beginRho + rangeRho, endRho, _parentIndex, m_dataTree.size()));
//        m_dataTree.add(new NodeData(node, beginTheta + rangeTheta, endTheta,
//                beginRho, beginRho + rangeRho, _parentIndex, m_dataTree.size()));
//        m_dataTree.add(new NodeData(node, beginTheta + rangeTheta, endTheta,
//                beginRho + rangeRho, endRho, _parentIndex, m_dataTree.size()));
    }

    /**
     * 親になり得るビン群を返す
     * @param _nodeParent 起点のインデックス
     * @return 親のビン群
     */
    public static List<NodeData> getParentHough(int _nodeParent){
        List<NodeData> leafs = new ArrayList<>();
        int node = _nodeParent;
        int cnt = 0;
        if(!m_dataTree.get(node).isHold() && m_dataTree.get(node).isChildHave()){
            for(int childIndex : m_dataTree.get(node).getChildIndex()){
                if(m_dataTree.get(childIndex).isData()){
                    leafs.addAll(getParentHough(childIndex));
                    cnt++;
                }
            }
            if(leafs.size() == 0 && cnt == 0){
                leafs.add(m_dataTree.get(node));
            }
        }
        return leafs;
    }

    /** 投票結果を返す */
//    public static List<NodeData> getThisHough(){
//        int start = m_startOfFloorHough.get(m_thisFloor);
//        int end = m_startOfFloorHough.get(m_thisFloor + 1);
//        return new ArrayList<>(m_houghPlane.subList(start, end));
//    }

    public static List<NodeData> getTreeData(){
        return new ArrayList<>(m_dataTree);
    }

    /**
     * 特定のビンの子を返す
     * @param _parent 親のビン　
     * @return 子のビン群
     */
    public static List<NodeData> getChildren(NodeData _parent){
        List<NodeData> children = new ArrayList<>();
        for(int childIndex : _parent.getChildIndex()){
            children.add(m_dataTree.get(childIndex));
        }
        return children;
    }

    /**
     * 領域内に値(Theta, Rho)が存在するビンのデータを返す
     * @param _theta 角度
     * @param _rho　距離
     * @return 該当ビンのデータ
     */
    public static NodeData getHoughLeaf(double _theta, double _rho){
        int node = 0;
        boolean last = false;   //葉に到達したか判断する
        while(!last){
            //根から順に辿る
            if(m_dataTree.get(node).isChildHave()){
                for(int childIndex : m_dataTree.get(node).getChildIndex()){
                    //子のビンの領域内に値がある場合
                    if(m_dataTree.get(childIndex).isInside(_theta, _rho)){
                        //子がデータを持たない場合は親のビンを返す
                        if(!m_dataTree.get(childIndex).isData()){
                            last = true;
                            break;
                        }
                        //子がデータを持つ場合辿る
                        node = childIndex;
                        break;
                    }
                }
            }else{
                break;
            }
        }
        return m_dataTree.get(node);
    }

    /**
     * ツリーの葉を取得する
     * @param _nodeParent 起点のインデックス
     * @return 葉群
     */
    public static List<NodeData> getLeafNodes(int _nodeParent){
        List<NodeData> leafs = new ArrayList<>();
        int node = _nodeParent;
        //子を持つ場合に子のインデックス番号を取得
        if(m_dataTree.get(node).isChildHave()){
            for(int childIndex : m_dataTree.get(node).getChildIndex()){
                //子がデータを持っている場合に子に入る
                if(m_dataTree.get(childIndex).isData()){
                    leafs.addAll(getLeafNodes(childIndex));
                }
            }
        }
        //枝を辿って葉に到達したらその値を返す
        if(leafs.size() == 0){
            leafs.add(m_dataTree.get(node));
        }
        return leafs;
    }

    public static double getLowestNodeWithTheta(){
        return m_widthTheta / Math.pow(TreeData.THETA_SPLIT_NUM, TreeData.getThisFloor());
    }

    public static double getLowestNodeWithRho() {
        return m_widthRho / Math.pow(TreeData.THETA_SPLIT_NUM, TreeData.getThisFloor());
    }

    /** 次の階層が存在し得るか */
    public static boolean checkNextFloor(){
//        m_startOfFloorNodeIndex.set(m_thisFloor + 2, m_dataTree.size());
        if(m_thisFloor >= 8 && !NodeData.useTHRESHOLD){
            return false;
        }
        System.out.println("親の数：" + getParentHough(0).size());
        if(getParentHough(0).size() != 0){
            ++m_thisFloor;
            return true;
        }
        return false;
    }

    public static int getThisFloor() {
        return m_thisFloor;
    }


    /**
     * データ保存用子クラス
     */
    public static class NodeData {

        /**
         * コンストラクタ
         * @param _node ノード
         * @param _beginTheta ビンのθ始点
         * @param _endTheta ビンのθ終点
         * @param _beginRho ビンのρ始点
         * @param _endRho ビンのρ終点
         * @param _parentIndex 親のインデックス
         * @param _thisIndex インデックス
         */
        private NodeData(int _node, double _beginTheta, double _endTheta, double _beginRho, double _endRho,
                         int _parentIndex, int _thisIndex, int _splitType){
            m_node = _node;

            m_binOfPossibility = Bin.createBox(_beginTheta, _endTheta, _beginRho, _endRho);
            m_binOfNecessity = Bin.createBox(_beginTheta, _endTheta, _beginRho, _endRho);

            m_parentIndex = _parentIndex;
            m_thisIndex = _thisIndex;

            m_isChildHave = false;
            m_isData = false;
            m_isHold = false;

            m_splitType = _splitType;
        }

        /**
         * データを入力し、各フラグの設定を変更する
         * @param _possibility 可能性値
         * @param _necessity 必然性値
         */
        public void setData(double _possibility, double _necessity){
            m_binOfPossibility.setGrade(_possibility);
            m_binOfNecessity.setGrade(_necessity);
            m_isData = true;

            if(!useTHRESHOLD){
                m_isChildHave = true;
            }
            else if(m_binOfNecessity.getGrade() >= THRESHOLD_NECESSITY){
                m_isHold = true;
            }else if(m_binOfPossibility.getGrade() > THRESHOLD_POSSIBILITY){
                m_isChildHave = true;
            }
        }

        /**
         * 可能性値を取得する
         * @return 可能性値
         */
        public double getPossibility() {
            return m_binOfPossibility.getGrade();
        }

        /**
         * 必然性値を取得する
         * @return 必然性値
         */
        public double getNecessity(){
            return m_binOfNecessity.getGrade();
        }

        /**
         * ノードを取得する
         * @return ノード
         */
        public int getNode() {
            return m_node;
        }

        /**
         * インデックスを取得する
         * @return インデックス
         */
        public int getThisIndex() {
            return m_thisIndex;
        }

        /**
         * 親のインデックスを取得する
         * @return 親のインデックス
         */
        public int getParentIndex(){
            return m_parentIndex;
        }

        /**
         * 子の所持状態を取得する
         * @return 子を持っている : true, 子を持たない : false
         */
        public boolean isChildHave() {
            return m_isChildHave;
        }

        /**
         * データの所持状態を取得する
         * @return データを持つ : true, データを持たない : false
         */
        public boolean isData(){return m_isData;}

        /**
         * 葉が閾値を満たしているかを取得する
         * @return 閾値を満たす : true, 閾値を満たさない : false
         */
        public boolean isHold(){return m_isHold;}

        /**
         * 子のインデックス番号をセットする
         * @param _childIndex 子のインデックス
         */
        public void setChildIndex(int _childIndex) {
            m_childIndex.add(_childIndex);
        }

        public double getBeginTheta() {
            return m_binOfPossibility.getBeginTheta();
        }

        public double getEndTheta() {
            return m_binOfPossibility.getEndTheta();
        }

        public double getBeginRho() {
            return m_binOfPossibility.getBeginRho();
        }
        public double getEndRho() {
            return m_binOfPossibility.getEndRho();
        }

        public double getRangeTheta(){
            return m_binOfPossibility.getRangeTheta();
        }

        public double getRangeRho(){
            return m_binOfPossibility.getRangeRho();
        }

        public int getSplitType(){
            return m_splitType;
        }

        public boolean isInside(double _theta, double _rho){
            if((getBeginTheta() <= _theta && _theta < getEndTheta())
                    && (getBeginRho() <= _rho && _rho <= getEndRho())){
                return true;
            }
            return false;
        }

        /**
         * 子のインデックス群を取得する
         * @return 子のインデックス群
         */
        public List<Integer> getChildIndex() {
            return m_childIndex;
        }

        /** ノード */
        private final int m_node;

        /** ビンのインデックス */
        private final int m_thisIndex;

        /** 親のビンのインデックス */
        private final int m_parentIndex;

        /** 子のビンのインデックス群 */
        private final List<Integer> m_childIndex = new ArrayList<>();

        /** 子の所持状態 */
        private boolean m_isChildHave;

        /** データの所持状態 */
        private boolean m_isData;

        /** 閾値を満たしているか確認用 */
        private boolean m_isHold;

        /** 可能性値のビン */
        private final Bin m_binOfPossibility;

        /** 必然性値のビン */
        private final Bin m_binOfNecessity;

        /** 閾値(可能性値) */
        private final double THRESHOLD_POSSIBILITY = 0.6;

        /** 閾値(必然性値) */
        private final double THRESHOLD_NECESSITY = 0.1;

        public static final boolean useTHRESHOLD = true;

        private final int m_splitType;
    }
}
