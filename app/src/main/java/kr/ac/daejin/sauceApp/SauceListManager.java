package kr.ac.daejin.sauceApp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class SauceListManager {

    static class SauceList{
        SauceParse sauceParse;
        List<Sauce> sauceList;

        // 처음 실행시 json 파일이 빈 파일이라면 null로 배열을 채움
        // json파일에 내용이 있다면 그 내용을 기반으로 sauceList객체에 등록함
        public SauceList() {
            this.sauceParse = new SauceParse();
            sauceList = new ArrayList<>();

            String json = sauceParse.getCurrentSauceInfo(this);

            try {
                if (json == null) {
                    for (int i = 0; i < 6; i++) {
                        this.sauceList.add(new Sauce("null", "null", "null"));
                    }
                    Gson gson = new Gson();
                    String s = gson.toJson(sauceList);
                    System.out.println("s = " + s);
                    sauceParse.saveSauceList(s);

                } else {
                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(json);

                    JsonArray data = jsonElement.getAsJsonArray();

                    for (JsonElement sauce : data) {
                        JsonObject JsonObject = sauce.getAsJsonObject();
                        String id = JsonObject.get("id").getAsString();
                        String name = JsonObject.get("name").getAsString();
                        String isLiquid = JsonObject.get("isLiquid").getAsString();

                        this.sauceList.add(new Sauce(name, isLiquid, id));
                    }
                }

            } catch (Exception e) {
                sauceParse.deleteJson();
            }


        }

        // 소스 리스트 json을 기반으로 sauceList객체에 등록시킴
        public void updateSauceList() {

            String json = sauceParse.getCurrentSauceInfo(this);
            if(json == null) return;

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);

            JsonArray data = jsonElement.getAsJsonArray();
            int i = 0;
            for (JsonElement sauce : data) {
                JsonObject JsonObject = sauce.getAsJsonObject();
                String id = JsonObject.get("id").getAsString();
                String name = JsonObject.get("name").getAsString();
                String isLiquid = JsonObject.get("isLiquid").getAsString();

                this.sauceList.set(i,new Sauce(name, isLiquid, id));
                i++;
            }
        }

        public List<Sauce> getSauceList() {
            return this.sauceList;
        }
    }

    static class Sauce {
        private String name;
        private String isLiquid;
        private String id;

        public Sauce(String name, String isLiquid, String id) {
            this.name = name;
            this.isLiquid = isLiquid;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getIsLiquid() {
            return isLiquid;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setIsLiquid(String isLiquid) {
            this.isLiquid = isLiquid;
        }
    }
}


