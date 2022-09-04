//package kr.ac.daejin.sauceApp;
//
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class preSavedSauce {
//    // [{"name" : "saucename", "isLiquid" : "1"}, { }, { }]
//    // 다음과 같은 json배열을 파싱하여 리스트로 가져온다.
//
//    List<SauceListManager.Sauce> savedSauceList = new ArrayList<>();
//    Gson gson = new Gson();
//
//    public preSavedSauce() {
//        this.savedSauceList.add(new SauceListManager.Sauce("간장","1"));
//        this.savedSauceList.add(new SauceListManager.Sauce("마요네즈","1"));
//        this.savedSauceList.add(new SauceListManager.Sauce("케첩","1"));
//        this.savedSauceList.add(new SauceListManager.Sauce("식초","1"));
//        this.savedSauceList.add(new SauceListManager.Sauce("머스타드","1"));
//        this.savedSauceList.add(new SauceListManager.Sauce("올리브오일","1"));
//        this.savedSauceList.add(new SauceListManager.Sauce("고추가루","0"));
//        this.savedSauceList.add(new SauceListManager.Sauce("설탕","0"));
//        this.savedSauceList.add(new SauceListManager.Sauce("다시다","0"));
//    }
//
//    public List<SauceListManager.Sauce> getSavedSauceList() {
//        return savedSauceList;
//    }
//}
