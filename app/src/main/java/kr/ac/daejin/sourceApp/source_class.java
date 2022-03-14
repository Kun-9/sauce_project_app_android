package kr.ac.daejin.sourceApp;

import java.util.ArrayList;

public class source_class {

    public static class Source {

        private String name;
        private int isLiquid;

        public Source(String name, int isLiquid) {
            this.name = name;
            this.isLiquid = isLiquid;
        }

        public String getInfo() {
            String liquid = "가루";
            if (isLiquid == 1) {
                liquid = "액체";
            }
            return name + ",  " + liquid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setIsLiquid(int isLiquid) {
            this.isLiquid = isLiquid;
        }

        public int getIsLiquid() {
            return isLiquid;
        }

    }


    public static class SourceList{

        private ArrayList<Source> source_list = new ArrayList<Source>();
//        private ArrayList<Source> current_source_list = new ArrayList<Source>();
        private Source[] current_source_list = {null, null, null, null, null, null};

        private int[] current_source_exist = {0, 0, 0, 0, 0, 0};
        private ArrayList<String> source_comp = new ArrayList<String>();

        public void addSource(String name, int isLiquid) {
            source_list.add(new Source(name, isLiquid));
        }

        public void regist_current_source(int cartridgeNum, int sourceNum) {
            if (cartridgeNum < 0 || cartridgeNum > 5) {
                System.out.println("1~6까지의 번호만 입력해주세요.");
            } else {
                Source temp = source_list.get(sourceNum);
                current_source_list[cartridgeNum] = temp;
//                current_source_list.set(cartridgeNum, temp);
                current_source_exist[cartridgeNum] = 1;
            }
        }

        public void saveSourceComp(String SourceStr) {
            source_comp.add(SourceStr);
        }

        public void deleteCurrentSource(int num) {
//            current_source_list.set(num, null);
            current_source_list[num] = null;
            current_source_exist[num] = 0;
        }

        public ArrayList<String> getSourceCompList() {
            return source_comp;
        }

        public ArrayList<Source> getSourceList() {
            return source_list;
        }

        public Source[] getCurrentSourceList() {
            return current_source_list;
        }

        public int[] getCurrentSourceExist() {
            return current_source_exist;
        }

    }

}
