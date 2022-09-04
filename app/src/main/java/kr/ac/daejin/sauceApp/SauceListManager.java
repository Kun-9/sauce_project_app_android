package kr.ac.daejin.sauceApp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        private int weight;

        public Sauce(String name, String isLiquid, String id) {
            this.name = name;
            this.isLiquid = isLiquid;
            this.id = id;
        }

        public Sauce(String name, String isLiquid, int weight) {
            this.name = name;
            this.isLiquid = isLiquid;
            this.weight = weight;
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

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    static class SauceComp {
        List<Sauce> sauceCompList;

        public SauceComp(List<Sauce> sauceComp) {
            this.sauceCompList = sauceComp;
        }

        public List<Sauce> getSauceCompList() {
            return sauceCompList;
        }
    }

    static class SauceCompList {
        List<SauceComp> sauceCompListList;

        public SauceCompList() {
            sauceCompListList = new ArrayList<>();
        }

        public List<SauceComp> getSauceCompListList() {
            return sauceCompListList;
        }

        void loadSavedCompSauceList(){
            File dir = new File("/data/data/kr.ac.daejin.SauceProjectApp/files/compFiles");
            File files[] = dir.listFiles();

            for (int i = 0; i < files.length; i++) {

                files[i].getName();

                // 현재 절대경로 값 path에 할당
                StringBuilder sb = new StringBuilder();
                try {

                    FileInputStream fos = new FileInputStream(files[i]);
                    InputStreamReader isr = new InputStreamReader(fos);
                    BufferedReader bufferedReader = new BufferedReader(isr);

                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    sb.toString();
//                    gson.fromJson(sb.toString(), SauceCompList);

                    fos.close();
//                    return sb.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("file: " + files[i]);
            }
        }
    }



    static class SauceCompManager {

        Gson gson = new Gson();
        private SauceCompList sauceCompList;

        public SauceCompManager() {
        }

        public SauceCompList getSauceCompList() {
            return sauceCompList;
        }

        // compFiles 안의 모든 조합파일을 읽어오고 객체에 등록한다.
        public void loadSavedSauceCompString() {

            sauceCompList = new SauceCompList();

//            String path = "/data/data/kr.ac.daejin.SauceProjectApp/files/compFiles";
//            File dir = new File(path);
//            // 절대경로에 chatLog.txt 라는 파일로 저장
//            File file = new File(path + "/" + compName + ".json");

//            Map<String, SauceComp> compMap = new Map<>();


            File dir = new File("/data/data/kr.ac.daejin.SauceProjectApp/files/compFiles");
            File files[] = dir.listFiles();

            for (int i = 0; i < files.length; i++) {
                StringBuilder sb = new StringBuilder();
                try {
                    FileInputStream fos = new FileInputStream(files[i]);
                    InputStreamReader isr = new InputStreamReader(fos);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    System.out.println("fileName = " + files[i].getName());

                    String line;
                    // 빈파일일때
                    if ((line = bufferedReader.readLine()) == null) {
                        return;
                    }
                    sb.append(line);

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(sb.toString());
                    System.out.println(sb.toString());

                    ArrayList<Sauce> sauces = new ArrayList<>();

                    for (int j = 0; j < jsonArray.length(); j++) {
                        String name = jsonArray.getJSONObject(j).getString("name");
                        String isLiquid = jsonArray.getJSONObject(j).getString("isLiquid");
                        int weight = jsonArray.getJSONObject(j).getInt("weight");

                        System.out.println("sauce" + i + " = " + name + " " + isLiquid + " " + weight);
                        sauces.add(new Sauce(name, isLiquid, weight));
                    }
                    SauceComp sauceComp = new SauceComp(sauces);
                    sauceCompList.getSauceCompListList().add(sauceComp);

                    fos.close();

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < sauceCompList.getSauceCompListList().size(); i++) {
                System.out.println(sauceCompList.getSauceCompListList().get(i).getSauceCompList().get(0).getName());
            }

        }

        // 객체를 만들고 compFiles에 저장
        public void saveSauceComp(String compName, SauceComp sauceComp) {

//            String compJson = gson.toJson(sauceCompList.getSauceCompListList().get(num).getSauceCompList());
            String compJson = gson.toJson(sauceComp.getSauceCompList());
            System.out.println(compJson);

            String path = "/data/data/kr.ac.daejin.SauceProjectApp/files/compFiles";

            try {
                File file = new File(path + "/" + compName + ".json");
                FileOutputStream fos = new FileOutputStream(file);

                // 입력받은 str값을 txt파일에 입력
                fos.write(compJson.getBytes());
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(compJson);
        }

    }



}



